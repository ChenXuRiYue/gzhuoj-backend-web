package com.gzhuoj.contest.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


// 即选手在排行榜上的一条数据。包括了过题数，以及每一道题的通过时间。方便完成维护以及可视化工作。
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalScore {
    // 个人基本信息
    private CompetitorBasicInfo competitor;
    // 已通过题目编号，以及该通过题目的首次通过时间。 精确到秒级 （事实上，数据库是精确到毫秒级的，但是对外开发的视图中没有必要精确到秒。）
    private Map<String , PersonalProblemResults> problemResultsDetails;

    // 罚时 总数
    private Integer penalty;

    public static PersonalScore init(CompetitorBasicInfo comparator) {
        return builder()
                .competitor(comparator)
                .problemResultsDetails(new HashMap<>())
                .penalty(0)
                .build();
    }
}
