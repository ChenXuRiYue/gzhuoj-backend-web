package com.gzhuoj.contest.service.board;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.gzhuoj.contest.dto.ContestantPerformanceDTO;
import com.gzhuoj.contest.model.pojo.Board;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import org.gzhuoj.common.sdk.convention.result.Result;

import java.util.List;

public interface ContestBoardService {
    List<ContestantPerformanceDTO> getContestBoard(Integer contestNum);
}
