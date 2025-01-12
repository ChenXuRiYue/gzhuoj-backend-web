package com.gzhuoj.contest.model.pojo;

import common.enums.SubmissionStatus;
import common.pojo.Pair;
import lombok.*;

import java.util.PriorityQueue;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PersonalProblemResults {
    // 题目 -> A B C...
    private String problemNum;

    // -1 有罚时且未通过， 0 未提 , 1 已经通过， 2 pending, 3 系统性错误（不计入罚时）
    private Integer status;

    // 榜单上的最新时间。
    // 1. 如果已经通过，第一次提交i通过 的时间。
    // 2. 如果未通过，最后一次提交i未通过 的时间。
    private Long latestTime;

    // 有效罚时提交次数。
    private Integer penaltyCount;

    private PriorityQueue<Pair<Long, SubmissionStatus>> submissionQue;

    PersonalProblemResults(String problemNum){
        this.problemNum = problemNum;
        this.status = 0;
        this.penaltyCount = 0;
    }


    public static PersonalProblemResults init(String problemNum) {
        return PersonalProblemResults.builder()
                .problemNum(problemNum)
                .status(0)
                .penaltyCount(0)
                .latestTime(0L)
                .submissionQue(new PriorityQueue<>( // 初始化大顶堆
                        (o1, o2) -> {
                            if(o1.getLeft().equals(o2.getLeft())) {
                                return o1.getRight().compareTo(o2.getRight());
                            } else return o2.getLeft().compareTo(o1.getLeft());
                        }
                ))
                .build();
    }
}
