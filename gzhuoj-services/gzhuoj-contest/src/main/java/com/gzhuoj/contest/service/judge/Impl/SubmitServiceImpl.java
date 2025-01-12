package com.gzhuoj.contest.service.judge.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.mapper.SubmitCodeMapper;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.model.entity.SubmitCodeDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuacm.sdk.contest.model.dto.SubmitRemoteDTO;
import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import com.gzhuoj.contest.service.judge.SubmitService;
import com.gzhuoj.contest.util.BoardUtil;
import common.enums.SubmissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, SubmitDO> implements SubmitService {
    private final SubmitCodeMapper submitCodeMapper;
    private final BoardUtil boardUtil;

    @Override
    public SubmitDO getSubmitDO(Integer submitId) {
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, submitId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateSubmitDO(SubmitRemoteDTO requestParam) {
        LambdaUpdateWrapper<SubmitDO> updateWrapper = Wrappers.lambdaUpdate(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, requestParam.getSubmitId())
                .ne(SubmitDO::getStatus, SubmissionStatus.STATUS_CANCELLED.getCode());

        // 更新数据库。
        int update = baseMapper.update(BeanUtil.toBean(requestParam, SubmitDO.class), updateWrapper);

        // 防止redis 采用缓存降级策略
        try {
            // 维护榜单
            boardUtil.updateBoardAttempt(convUpdateScoreAttempt(requestParam));
            // TODO 当采取必要的宕机维护情形时。这里应该使用 redis 维护一个提交生效的set 结构。
        } catch (Exception e) {
            // TODO 完善管理员通知机制。等待赛时状态维护面板开发。及时反馈redis 宕机情况。
            // TODO 增加定时任务防止宕机情形。
        }

        return update != 0;
    }

    public UpdateScoreAttempt convUpdateScoreAttempt(SubmitRemoteDTO param) {
        UpdateScoreAttempt res = new UpdateScoreAttempt();
        // TODO 检查Name 是否有必要传入
        res.setCompetitor(new CompetitorBasicInfo(param.getTeamAccount(), null));
        res.setContestNum(String.valueOf(param.getContestNum()));
        res.setProblemNum(String.valueOf(param.getProblemNum()));
        // 区分 pass TIme 和 punish time.
        res.setSubmitTime(param.getSubmitTime().getTime());
        res.setSubmissionStatus(SubmissionStatus.getSubmissionStatusByCode(param.getStatus()));
        return res;
    }

    @Override
    public String getCode(Integer submitId) {
        LambdaQueryWrapper<SubmitCodeDO> queryWrapper = Wrappers.lambdaQuery(SubmitCodeDO.class)
                .eq(SubmitCodeDO::getSubmitId, submitId);
        SubmitCodeDO submitCodeDO = submitCodeMapper.selectOne(queryWrapper);
        return submitCodeDO.getCode();
    }
}
