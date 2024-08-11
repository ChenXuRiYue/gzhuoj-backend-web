package com.gzhuoj.contest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.contest.constant.enums.SubmissionStatus;
import com.gzhuoj.contest.dto.req.Judge.RegContestJudgeSubmitReqDTO;
import com.gzhuoj.contest.judge.JudgeDispatcher;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.remote.ProblemRemoteService;
import com.gzhuoj.contest.service.JudgeService;
import com.gzhuoj.contest.service.RegContestService;
import com.gzhuoj.contest.validator.JudgeValidator;
import com.gzhuoj.contest.validator.PreCheckValidator;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static common.convention.errorcode.BaseErrorCode.CONTEST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {
    private final JudgeValidator judgeValidator;
    private final PreCheckValidator preCheckValidator;
    private final JudgeDispatcher judgeDispatcher;
    private final RegContestService regContestService;
    private final SubmitMapper submitMapper;
    @Override
    public boolean submit(RegContestJudgeSubmitReqDTO requestParam) {
        // 先判断提交的代码是否符合规范
        judgeValidator.submitArgChecker(requestParam);
        SubmitDO submitDO = SubmitDO.builder()
                .contestId(requestParam.getCid())
                .teamAccount(requestParam.getTeamAccount())
                .problemId(requestParam.getProblemId())
                .language(requestParam.getLanguage())
                .codeSize(requestParam.getCode().getBytes().length)
                // 进入pending状态
                .status(SubmissionStatus.PENDING.getCode())
                .submitTime(new Date())
                .build();
        // 先将提交放入数据库，防止丢失数据
        preCheckValidator.contestPreCheckAndSave(requestParam, submitDO);

        // 将submit放入到评测队列中待下一步处理
        judgeDispatcher.sendTask(submitDO.getSubmitId(), requestParam.getProblemId());
        return true;
    }

    @Override
    public SubmitDO getSubmitDO(Integer submitId) {
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, submitId);
        return submitMapper.selectOne(queryWrapper);
    }
}
