package com.gzhuoj.contest.dto;


import com.gzhuoj.contest.model.pojo.PersonalProblemResults;
import lombok.Builder;
import lombok.Data;

import java.util.List;


// 榜单上的竞赛选手表现数据
@Data
@Builder
public class ContestantPerformanceDTO {
    /// 基本信息
    private String teamAccount;
    private String teamName;
    private String schoolName;
    private String teamMember;
    private Integer teamType;
    private String coach;

    // 选手表现信息
    private Integer rank;
    private Integer passedProblemNum;
    private Integer penalty;
    private Integer dirt;

    private List<PersonalProblemResults> problemResults;
}
