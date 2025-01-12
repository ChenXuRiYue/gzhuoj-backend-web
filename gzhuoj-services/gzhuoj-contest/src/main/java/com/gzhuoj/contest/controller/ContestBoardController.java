package com.gzhuoj.contest.controller;


import com.gzhuoj.contest.dto.ContestantPerformanceDTO;
import com.gzhuoj.contest.dto.req.board.BoardViewReq;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import com.gzhuoj.contest.service.board.ContestBoardService;
import lombok.RequiredArgsConstructor;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest/board")
public class ContestBoardController {

    private final ContestBoardService contestBoardService;

    @PostMapping("/show")
    public Result<List<ContestantPerformanceDTO>> boardViewWithLimit(@RequestBody BoardViewReq boardViewReq) {
        return Results.success(contestBoardService.getContestBoard(boardViewReq.getContestNum()));
    }
    // 该接口用于测试。
}
