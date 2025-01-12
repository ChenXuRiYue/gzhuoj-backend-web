package com.gzhuoj.board;

import com.gzhuoj.board.data.Case1;
import com.gzhuoj.board.data.Case2;
import com.gzhuoj.board.data.Case3;
import com.gzhuoj.board.data.Case4;
import com.gzhuoj.contest.ContestApplication;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import com.gzhuoj.contest.service.board.ContestBoardService;
import com.gzhuoj.contest.util.BoardUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


@SpringBootTest(classes = ContestApplication.class)
class BoardTest {
    @Resource
    private ContestBoardService contestBoardService;
    @Resource
    private BoardUtil boardUtil;

    // 所有测试点。
    @Test
    void testAllCase() {
        checkTestCase1();
        checkTestCase2();
        checkTestCase3();
        checkTestCase4();
    }

    List<PersonalScore> constructAndGetResult(List<UpdateScoreAttempt> attempts) {
        String contestNum = attempts.get(0).getContestNum();

        // 维护榜单。
        for (UpdateScoreAttempt attempt : attempts) {
            boardUtil.updateBoardAttempt(attempt);
        }

        // 排查榜单情况
        return boardUtil.rangeViewByLimit(contestNum, 0, 10);
    }
    boolean checkResult(List<PersonalScore> result, List<String> output) {
        for (int i = 0; i < result.size(); i++) {
            PersonalScore personalScore = result.get(i);
            String expect = output.get(i);
            String actual = personalScore.getCompetitor().getAccount();
            if (!expect.equals(actual)) {
                return false;
            }
        }
        return true;
    }

    @Test
    void checkTestCase1() {
        // 捏造数据：
        List<UpdateScoreAttempt> input = Case1.getInput();
        List<String> output = Case1.getOutput();
        var result = constructAndGetResult(input);
        assert (checkResult(result, output));
    }

    /**
     * 方便聚焦一个选手行为的测试用例。
     */
    @Test
    void checkTestCase2() {
        List<UpdateScoreAttempt> input = Case2.getInput();
        var result = constructAndGetResult(input);
    }

    @Test
    void checkTestCase3() {
        List<UpdateScoreAttempt> input = Case3.getInput();
        var result = constructAndGetResult(input);
    }

    @Test
    void checkTestCase4() {
        List<UpdateScoreAttempt> input = Case4.getInput();
        List<String> output = Case4.getOutput();
        var result = constructAndGetResult(input);
        assert (checkResult(result, output));
    }
}
