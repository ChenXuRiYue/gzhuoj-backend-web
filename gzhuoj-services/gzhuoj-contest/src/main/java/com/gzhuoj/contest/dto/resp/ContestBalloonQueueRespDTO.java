package com.gzhuoj.contest.dto.resp;

import com.gzhuoj.contest.model.entity.ContestBalloonDO;
import lombok.Data;

import java.util.List;

@Data
public class ContestBalloonQueueRespDTO {
    /**
     * 距离比赛开始的时间
     */
    private Integer acTime;

    /**
     * 队伍账号
     */
    private String teamAccount;

    /**
     * 比赛室号
     */
    private String room;

    /**
     * problem status，0 ac、1 fb
     */
    private Integer pst;

    /**
     * balloon status， 0未发、1已发
     */
    private Integer bst;

    /**
     * 题目在比赛中的实际编号
     */
    private Integer actualNum;

    /**
     * 题目编号
     */
    private Integer problemId;
}
