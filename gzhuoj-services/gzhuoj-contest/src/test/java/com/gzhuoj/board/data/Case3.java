package com.gzhuoj.board.data;

import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import common.enums.SubmissionStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Case3 extends Case0Super{

    // case1：
    /**
     * # 普通测试
     * ## 数据细节：
     * 1. 简单情况模拟。由 case1 抽出来的数据
     * ## 总榜情况
     * 1. 李白  3 题  4 罚时 ： 100 200 300
     * ## 提交列表
     * 时间：    account  题目     result
     * 1        李白      1       wa
     * 100      李白      2       wa
     * 100      李白      2       ac
     * 130      李白      3       wa
     * 200      李白      2       ac
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
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(100L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 3
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(100L)
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),


                // 4
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(130L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 5
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(200L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 6
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(250L)
                        .ProblemNum("3")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER)
                        .build(),

                // 7
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
