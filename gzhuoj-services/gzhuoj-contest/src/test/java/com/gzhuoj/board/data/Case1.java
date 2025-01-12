package com.gzhuoj.board.data;

import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import common.enums.SubmissionStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Case1 extends Case0Super{

    // case1：
    /**
     * # 普通测试
     * ## 数据细节：
     * 1. 假设每个人交了一次之后不会再交。
     * 2. 假设
     * ## 总榜情况
     * 1. 李白  3 题  4 罚时 ： 100 200 300
     * 2. 杜甫  2 题  4 罚时：  100 200
     * 3. 苏轼  2 题  4 罚时：  100 201
     * ## 提交列表
     * 时间：    account  题目     result
     * 1        李白      1       wa
     * 50       杜甫      1       wa
     * 60       苏轼      1       wa
     * 100      李白      2       wa
     * 100      李白      2       ac
     * 100      杜甫      1       ac
     * 100      苏轼      1       ac
     * 120      苏轼      3       wa
     * 130      李白      3       wa
     * 140      杜甫      2       wa
     * 150      杜甫      2       wa
     * 160      苏轼      3       wa
     * 170      苏轼      3       wa
     * 180      杜甫      2       wa
     * 200      李白      2       ac
     * 200      杜甫      2       ac
     * 201      苏轼      3       ac
     * 250      李白      3       wa
     * 300      李白      3       ac
     */


    public static List<UpdateScoreAttempt> getInput() {
        String contestNum = UUID.randomUUID().toString();
        return new ArrayList<>(List.of(new UpdateScoreAttempt[]{
                // 1
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(1L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 2
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(50L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 3
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("苏轼", "苏轼"))
                        .submitTime(60L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 4
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(100L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 5
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(100L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 6
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(100L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 7
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("苏轼", "苏轼"))
                        .submitTime(100L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 8
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("苏轼", "苏轼"))
                        .submitTime(120L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 9
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(130L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 10
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(140L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 11
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(150L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 12
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("苏轼", "苏轼"))
                        .submitTime(160L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 13
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("苏轼", "苏轼"))
                        .submitTime(170L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 14
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(180L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 15
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(200L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 16
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(200L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 17
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("苏轼", "苏轼"))
                        .submitTime(201L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 18
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(250L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 19
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(300L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),
        }));
    }

    public static List<String> getOutput() {
        return calOutput(getInput());
    }
}
