package com.gzhuoj.board.data;

import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import common.enums.SubmissionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Case4 extends Case0Super{

    // case1：

    /**
     * # 并行场景测试
     * ## 数据细节：
     * 1. 模拟并行评测机下，时间无序的数据情况
     * ## 总榜情况
     * 1. 李白  3 题  4 罚时 ： 100 200 300
     * ## 提交列表
     * 时间：    account  题目     result
     * 110      李白      1       wa
     * 80       李白      1       ac
     * 100      杜甫      1       ac
     */

    public static List<UpdateScoreAttempt> getInput() {
        String contestNum = UUID.randomUUID().toString();
        return new ArrayList<>(List.of(new UpdateScoreAttempt[]{
                // 1 - 时间无序，模拟李白的 WA 提交
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(110L) // 按照提交列表调整时间
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.WRONG_ANSWER) // wa
                        .build(),

                // 2 - 时间无序，模拟李白的 AC 提交
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(80L) // 按照提交列表调整时间
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.ACCEPTED) // ac
                        .build(),

                // 3 - 模拟杜甫的 AC 提交
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("杜甫", "杜甫"))
                        .submitTime(100L) // 按照提交列表调整时间
                        .ProblemNum("1")
                        .submissionStatus(SubmissionStatus.ACCEPTED) // ac
                        .build(),
        }));
    }


    public static List<String> getOutput() {
       return calOutput(getInput());
    }
}
