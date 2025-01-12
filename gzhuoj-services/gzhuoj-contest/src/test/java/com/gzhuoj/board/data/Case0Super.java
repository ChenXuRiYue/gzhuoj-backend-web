package com.gzhuoj.board.data;

import com.gzhuoj.contest.model.pojo.PersonalScore;
import com.gzhuoj.contest.model.pojo.PersonalProblemResults;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import com.gzhuoj.contest.util.BoardUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

// 封装了测试样例的一些公共方法。
public class Case0Super {
    // 对拍，从input 中计算出一个正确的答案。该提交中考虑了以下情况：
    // 1. 数据进行预处理。非实时响应的（虽然事实上，该段测试代码需要一个实时性的情况）
    // 2. 并行的，提交时间时间具有无序性。
    // TODO 对拍等待进一步测试。
    static List<String> calOutput(List<UpdateScoreAttempt> input) {
        // 排序处理乱序情况。
        input.sort((o1, o2) -> {
            if (Objects.equals(o1.getSubmitTime(), o2.getSubmitTime())) {
                return o1.getProblemNum().compareTo(o2.getProblemNum());
            } else {
                return o1.getSubmitTime().compareTo(o2.getSubmitTime());
            }
        });

        // 统计个人提交情况。
        Map<String, PersonalScore> map = new HashMap<>();
        List<Pair<Integer, String>> List = new ArrayList<>();
        for(UpdateScoreAttempt attempt : input) {
            // 处理出信息
            String account = attempt.getCompetitor().getAccount();
            String problemNum = attempt.getProblemNum();

            // 取出选手的提交信息。
            PersonalScore score = map.getOrDefault(account, PersonalScore.init(attempt.getCompetitor()));
            Map<String, PersonalProblemResults> problemResultsDetails = score.getProblemResultsDetails();
            PersonalProblemResults record = problemResultsDetails.getOrDefault(problemNum, PersonalProblemResults.init(problemNum));

            // 维护个人情况。
            int status = BoardUtil.StatusType(attempt.getSubmissionStatus());
            if(record.getStatus() == 1) { // 当前题目已经通过。不做处理
                continue;
            }
            record.setLatestTime(attempt.getSubmitTime());
            if(status == 0) { // ac 提交
                record.setStatus(1);
            } else if(status == 1) { // reject && 产生罚时
                record.setStatus(-1);
                record.setPenaltyCount(record.getPenaltyCount() + 1);
            } else if(status == 2){                // penalty 提交
                record.setStatus(2);
            } else record.setStatus(3);

            // 更新map
            problemResultsDetails.put(problemNum, record);
            score.setProblemResultsDetails(problemResultsDetails);
            map.put(account, score);
        }

        // 获取个人排名：
        List<Pair<Pair<Integer, Long>, String>> list = new ArrayList<>();
        for(String account : map.keySet()) {
            PersonalScore score = map.get(account);
            int passedProblem = 0;
            long pentalty = 0;
            for(Map.Entry<String, PersonalProblemResults> entry : score.getProblemResultsDetails().entrySet()) {
                if (entry.getValue().getStatus() == 1) {
                    passedProblem++;
                    pentalty += entry.getValue().getLatestTime();
                    pentalty += entry.getValue().getPenaltyCount() * 20L;
                }
            }
            list.add(Pair.of(Pair.of(passedProblem, pentalty), account));
        }

        // 排序、并且反馈结果。
        list.sort((o1, o2) -> {
            // 一维度升序。 二维度降序。
            // 一二维度相同，就不管了，比赛中将两者按照同名词处理。
            if(o1.getLeft().getLeft().equals(o2.getLeft().getLeft())) {
                return o1.getLeft().getRight().compareTo(o2.getLeft().getRight());
            }else return o2.getLeft().getLeft().compareTo(o1.getLeft().getLeft());
        });
        return list.stream().map(Pair::getRight).toList();
    }
}
