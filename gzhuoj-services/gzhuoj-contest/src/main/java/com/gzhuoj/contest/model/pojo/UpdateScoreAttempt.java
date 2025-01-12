package com.gzhuoj.contest.model.pojo;

import common.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateScoreAttempt {
    private String contestNum;
    private CompetitorBasicInfo competitor;
    // 通过时间。这里只需要关注相对时间
    private Long  submitTime;
    private String ProblemNum;
    private SubmissionStatus submissionStatus;
}
