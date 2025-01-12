package com.gzhuoj.contest.util;

import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import com.gzhuoj.contest.model.pojo.PersonalProblemResults;
import common.enums.SubmissionStatus;
import common.pojo.Pair;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BoardUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;
    private final HashOperations<String, String, Object> hashOperations;

    public BoardUtil(RedisTemplate<String, Object> redisTemplate, ZSetOperations<String, String> zSetOperations, HashOperations<String, String, Object> hashOperations) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = zSetOperations;
        this.hashOperations = hashOperations;
    }

    private String convToZSetKey(String contestNum) {
        return "ZSet-rank" + contestNum;
    }

    private String convToHashKey(String contestNum) {
        return "hash-detail" + contestNum;
    }

    /**
     * 分数哈希 ，三个统计维度。 与下方函数比较，该函数不是增加，而是直接插入。 用于防止缓存出现问题，导致重新启动要进行批量数据插入
     * x : 过题数
     * y : 每道题首次统过时间 之和。
     * z : 已通过题目的罚时提交次数
     * f = x * (10 ^ 12) - y * 20 - z
     */
    public Double scoreHash(Integer x, Integer y, Long z) {
        return Math.pow(10, 12) * x - 20.0 * y - z;
    }

    /**
     * 比赛开始时，进行一些必要的初始化
     * 为每一个选手分配一个排名结构
     */
    public void initBoard(String contestNum, List<CompetitorBasicInfo> competitors, Integer durations, TimeUnit timeUnit) {
        // 设置过期时间。
        for (CompetitorBasicInfo competitor : competitors) {
            zSetOperations.add(convToZSetKey(contestNum), competitor.getAccount(), 0);
            hashOperations.put(convToHashKey(contestNum), competitor.getAccount(), PersonalScore.init(competitor));
        }
        // 统一设置数据结构过期时间
        redisTemplate.expire(convToZSetKey(contestNum), durations * 2, timeUnit);
        redisTemplate.expire(convToHashKey(contestNum), durations * 2, timeUnit);
    }

    /**
     * result 判定
     * 0 判断为失败
     * 1。 通过积分
     */
    public static Integer StatusType(SubmissionStatus result) {
        if (Objects.equals(result, SubmissionStatus.ACCEPTED)) {
            return 0;
        } else if (SubmissionStatus.WRONG_ANSWER.equals(result) || SubmissionStatus.PRESENTATION_ERROR.equals(result) || SubmissionStatus.TIME_LIMIT_EXCEED.equals(result) || SubmissionStatus.MEMORY_LIMIT_EXCEED.equals(result) || SubmissionStatus.RUNTIME_ERROR.equals(result) || SubmissionStatus.OUTPUT_LIMIT_EXCEED.equals(result)) {
            return 1;
        } else if (result.equals(SubmissionStatus.PENDING)) { // 同时处理 pending 情况。
            return 2;
        } else return 3; // 系统性错误。
    }

    /**
     * 要禁止比赛过程中修改编号。 否则会出现bug. 或者定时任务扫描可以解决该问题
     * 封装一个内容
     * passTime : 首次通过的时间。 PunishTime 罚时次数。
     */

    public void updateBoardAttempt(UpdateScoreAttempt param) {
        // 获取个人竞赛信息
        PersonalScore personalScore = getPersonScore(param.getContestNum(), param.getCompetitor());

        // 结合当前选手的过题情况，以及最新提交状态更新 record 信息
        updatePersonalScore(param, personalScore);

        // 更新存储层中的排行榜
        updateRedisBoard(param, personalScore);
    }

    // 根据多种情形更新选手个人提交信息记录状态。
    void updatePersonalScore(UpdateScoreAttempt param, PersonalScore personalScore) {
        Map<String, PersonalProblemResults> problemResultDetails = personalScore.getProblemResultsDetails();

        // 获取对应题目的提交情况
        PersonalProblemResults record = problemResultDetails.getOrDefault(param.getProblemNum(), PersonalProblemResults.init(param.getProblemNum()));

        // 根据提交的情况更新结果
        if(StatusType(param.getSubmissionStatus()) == 0) {
            updatePersonalProblemResultWhenAccepted(param, record);
        } else if(StatusType(param.getSubmissionStatus()) == 1) {
            updatePersonalProblemResultWhenRejected(param, record);
        } else if(StatusType(param.getSubmissionStatus()) == 2) {
            updatePersonalProblemResultWhenPending(param, record);
        } else if(StatusType(param.getSubmissionStatus()) == 3) {
            updatePersonalProblemResultWhenUnknown(param, record);
        }

        // 指向的是和 personScore 的同一片空间
        problemResultDetails.put(param.getProblemNum(), record);
    }

    // 分四种情况分别维护题目的提交状态。
    void updatePersonalProblemResultWhenAccepted(UpdateScoreAttempt param, PersonalProblemResults record) {
        // 当前题目的状态为已通过。
        PriorityQueue<Pair<Long, SubmissionStatus>> que = record.getSubmissionQue();
        while(!que.isEmpty()) {
            Pair<Long, SubmissionStatus> peek = que.peek();
            if(peek == null) {
                que.poll();
            } else if(peek.getLeft() > param.getSubmitTime()) { // 上次提交时间比该次晚。
                if(StatusType(peek.getRight()) == 1) { // 将多余计算的罚时处理
                    record.setPenaltyCount(record.getPenaltyCount() - 1);
                }
                que.poll();
            } else break;
        }
        if(!que.isEmpty() && que.peek().getRight() != SubmissionStatus.ACCEPTED) {
            que.add(Pair.of(param.getSubmitTime(), param.getSubmissionStatus()));
        }

        if(record.getStatus() == 1 && record.getLatestTime() > param.getSubmitTime()) {
            record.setLatestTime(param.getSubmitTime());
        } else if(record.getStatus() != 1){
            record.setStatus(1);
            record.setLatestTime(param.getSubmitTime());
        }
    }

    void updatePersonalProblemResultWhenRejected(UpdateScoreAttempt param, PersonalProblemResults record) {
        // 无意义提交。
        if(record.getStatus() == 1 && record.getLatestTime() > param.getSubmitTime()) {
            return;
        }
        // 更新该题目的提交状态
        record.getSubmissionQue().add(Pair.of(param.getSubmitTime(), param.getSubmissionStatus()));
        if(record.getLatestTime() < param.getSubmitTime()) {
            record.setLatestTime(param.getSubmitTime());
            record.setStatus(-1);
        }
        record.setPenaltyCount(record.getPenaltyCount() + 1);
    }

    void updatePersonalProblemResultWhenPending(UpdateScoreAttempt param, PersonalProblemResults record) {
        // 无意义提交
        if(record.getStatus() == 1) {
            return;
        }
        // 更新题目与提交状态
        if(record.getLatestTime() < param.getSubmitTime()) {
            record.setLatestTime(param.getSubmitTime());
            record.setStatus(2);
        }
    }

    void updatePersonalProblemResultWhenUnknown(UpdateScoreAttempt param, PersonalProblemResults record) {
        // 无意义提交
        if(record.getStatus() == 1) {
            return;
        }
        // 更新题目与提交状态
        if(record.getLatestTime() < param.getSubmitTime()) {
            record.setLatestTime(param.getSubmitTime());
            record.setStatus(3);
        }
    }



    public void updateRedisBoard(UpdateScoreAttempt param, PersonalScore personalScoreDetail) {
        // 记录罚时次数。
        Integer passedProblemCount = calPassedProblemCount(personalScoreDetail);
        Integer punishCount = calPunishCount(personalScoreDetail);
        Long passTimeSum = calPassTimeSum(personalScoreDetail);

        Double scoreHash = scoreHash(passedProblemCount, punishCount ,passTimeSum);

        zSetOperations.add(convToZSetKey(param.getContestNum()), param.getCompetitor().getAccount(), scoreHash);

        // 2. 更新个人状态
        hashOperations.put(convToHashKey(param.getContestNum()), param.getCompetitor().getAccount(), personalScoreDetail);
        // 完成校验，判定为有效提交。 更新排行榜排名。
    }

    public Integer calPassedProblemCount(PersonalScore personalScoreDetail) {
        return personalScoreDetail.getProblemResultsDetails().values().stream().filter(x -> x.getStatus() == 1).mapToInt(x -> 1).sum();
    }

    public Integer calPunishCount(PersonalScore personalScoreDetail) {
        int count = 0;
        for (Map.Entry<String, PersonalProblemResults> entry : personalScoreDetail.getProblemResultsDetails().entrySet()) {
            if (entry.getValue().getStatus() == 1) {
                count += entry.getValue().getPenaltyCount();
            }
        }
        return count;
    }

    public Long calPassTimeSum(PersonalScore personalScoreDetail) {
        return personalScoreDetail.getProblemResultsDetails().values().stream().filter(x -> x.getStatus() == 1).mapToLong(PersonalProblemResults::getLatestTime).sum();
    }

    // 获取选手过题详情。
    public PersonalScore getPersonScore(String contestNum, CompetitorBasicInfo competitor) {
        PersonalScore personalScore = (PersonalScore) hashOperations.get(convToHashKey(contestNum), competitor.getAccount());
        if (personalScore == null) {
            personalScore = PersonalScore.init(competitor);
        }
        return personalScore;
    }


    /**
     * 查相关的API
     * 1. 查出给定范围，并且给出列表。
     */
    public List<PersonalScore> rangeViewByLimit(String contestNum, Integer l, Integer r) {
        // 方便调试： 打印 排名以及分数情况。
        // zSetOperations.reverseRangeWithScores(convToZSetKey(String.valueOf(contestNum)), 0, -1);
        List<String> competitors = new ArrayList<>(Objects.requireNonNull(zSetOperations.reverseRange(convToZSetKey(String.valueOf(contestNum)), l, r)));

        return competitors.stream().map(x -> (PersonalScore) hashOperations.get(convToHashKey(String.valueOf(contestNum)), x)).collect(Collectors.toList());
    }
}
