package com.gzhuoj.judgeserver.remote.DTO.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubmitRemoteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键 ID, @TableId 注解定义字段为表的主键，type 表示主键类型，IdType.AUTO 表示随着数据库 ID 自增
     */
    @TableId(value = "submit_id", type = IdType.AUTO)
    private Integer submitId;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 题目编号
     */
    private Integer problemId;

    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 提交语言
     */
    private Integer language;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * 评测时间
     */
    private Integer execTime;

    /**
     * 评测结果
     */
    private Integer status;

    /**
     * 代码大小 -> byte
     */
    private Integer codeSize;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 评测机
     */
    private String judger;
}
