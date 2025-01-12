package com.gzhuoj.judgeserver.controller;

import cn.hutool.core.bean.BeanUtil;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import com.gzhuoj.judgeserver.dto.req.ToJudgeReqDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_PARAM_NOT_FOUND_ERROR;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-judge-server")
public class JudgeController {
    private final JudgeServerService judgeServerService;

    @PostMapping("/judge")
    public Result<Void> judge(@RequestBody ToJudgeReqDTO requestParam) {
        validateRequest(requestParam);

        SubmitDO submitDO = convertToSubmitDO(requestParam);
        judgeServerService.judge(submitDO);

        return Results.success().setMessage("成功发送！");
    }

    private void validateRequest(ToJudgeReqDTO requestParam) {
        if (requestParam == null || requestParam.getSubmitDTO() == null ||
                requestParam.getJudgeServerIp() == null || requestParam.getJudgeServerPort() == null) {
            log.error("Judge Param error, ToJudgeReqDTO = {}", requestParam);
            throw new ServiceException("Invalid judge parameters provided.", JUDGE_PARAM_NOT_FOUND_ERROR);
        }
    }

    private SubmitDO convertToSubmitDO(ToJudgeReqDTO requestParam) {
        try {
            return BeanUtil.toBean(requestParam.getSubmitDTO(), SubmitDO.class);
        } catch (Exception e) {
            log.error("Error converting ToJudgeReqDTO to SubmitDO, ToJudgeReqDTO = {}", requestParam, e);
            throw new ServiceException("Error converting request testData.", JUDGE_PARAM_NOT_FOUND_ERROR);
        }
    }
}
