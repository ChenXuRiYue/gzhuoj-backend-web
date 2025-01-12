package com.gzhuoj.contest.service.team;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.model.entity.TeamDO;

import java.util.List;

public interface TeamService extends IService<TeamDO> {
     List<TeamDO> queryTeamListByAccounts(List<String> accounts);
}
