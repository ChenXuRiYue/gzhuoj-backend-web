package com.gzhuoj.contest.service.team.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.mapper.TeamMapper;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl extends ServiceImpl<TeamMapper, TeamDO> implements TeamService {

    @Override
    public List<TeamDO> queryTeamListByAccounts(List<String> accounts) {
        QueryWrapper<TeamDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("team_account", accounts);
        return baseMapper.selectList(queryWrapper);
    }
}
