package com.gzhuoj.board.data;

import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import common.enums.SubmissionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Case2 extends Case0Super{

    /**
     * # 普通测试
     * ## 数据细节：
     * 方便调试一个人连续过题的计算情况。
     * ## 总榜情况
     * 1. 李白  2 题
     * ## 提交列表
     * 时间：    account  题目     result
     * 1        李白      1       ac
     * 100      李白      2       ac
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
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),

                // 2
                UpdateScoreAttempt.builder()
                        .contestNum(contestNum)
                        .competitor(new CompetitorBasicInfo("李白", "李白"))
                        .submitTime(100L)
                        .ProblemNum("2")
                        .submissionStatus(SubmissionStatus.ACCEPTED)
                        .build(),
        }));
    }
}
