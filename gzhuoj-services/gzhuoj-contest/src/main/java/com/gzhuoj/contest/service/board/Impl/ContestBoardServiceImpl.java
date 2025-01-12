package com.gzhuoj.contest.service.board.Impl;

import com.gzhuoj.contest.dto.ContestantPerformanceDTO;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import com.gzhuoj.contest.model.pojo.PersonalProblemResults;
import com.gzhuoj.contest.service.board.ContestBoardService;
import com.gzhuoj.contest.service.team.TeamService;
import com.gzhuoj.contest.util.BoardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestBoardServiceImpl implements ContestBoardService {
    private final BoardUtil boardUtil;
    private final TeamService teamService;

    @Override
    public List<ContestantPerformanceDTO> getContestBoard(Integer contestNum) {
        List<PersonalScore> personalScores = boardUtil.rangeViewByLimit(String.valueOf(contestNum), 1, 1000000000);
        return convToContestPerformanceDTO(personalScores);
    }

    private List<ContestantPerformanceDTO> convToContestPerformanceDTO(List<PersonalScore> personalScores) {
        List<String> teamAccounts = personalScores.stream().map(x -> x.getCompetitor().getAccount()).toList();
        List<TeamDO> teamDOS = teamService.queryTeamListByAccounts(teamAccounts);
        Map<String, TeamDO> teamMap = teamDOS.stream().collect(Collectors.toMap(TeamDO::getTeamAccount, x -> x));
        return personalScores.stream().map(x -> {
            TeamDO teamDO = teamMap.get(x.getCompetitor().getAccount());
            return ContestantPerformanceDTO.builder()
                    .coach(teamDO.getCoach())
                    .teamAccount(teamDO.getTeamAccount())
                    .teamName(teamDO.getTeamName())
                    .teamMember(teamDO.getTeamMember())
                    .teamType(teamDO.getTeamType())
                    .schoolName(teamDO.getSchool())
                    .passedProblemNum(boardUtil.calPassedProblemCount(x))
                    .penalty(boardUtil.calPunishCount(x))
                    .dirt(boardUtil.calPassTimeSum(x).intValue())
                    // convTo problemResults:
                    .problemResults(convToPersonalSingleProblemResults(x.getProblemResultsDetails()))
                    .build();
        }).collect(Collectors.toList());
    }

    private List<PersonalProblemResults> convToPersonalSingleProblemResults(Map<String, PersonalProblemResults> problemResultsDetails) {
        return problemResultsDetails.values().stream().map(x -> PersonalProblemResults.builder()
                .problemNum(x.getProblemNum())
                .status(x.getStatus())
                .latestTime(x.getLatestTime())
                .penaltyCount(x.getPenaltyCount())
                .build()).collect(Collectors.toList());
    }
}
