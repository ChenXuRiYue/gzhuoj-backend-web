package com.gzhuoj.judgeserver.judge;

import cn.hutool.json.JSONObject;
import com.gzhuacm.sdk.judgeserver.api.ContestRemoteService;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.judgeserver.judge.handler.AbstractJudgeTemplate;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode;
import common.enums.Language;
import common.enums.SubmissionStatus;
import common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_PROBLEM_RESOURCES_NOT_FOUND_ERROR;

@Component
@Slf4j
@RequiredArgsConstructor
public class JudgeStrategy {
    private final LanguageConfigLoader languageConfigLoader;

    private final ContestRemoteService contestRemoteService;

    private final JudgeRun judgeRun;

    public Map<String, Object> judge(ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        String userFileId = null;
        try{
            LanguageConfig languageConfig = languageConfigLoader
                    .getLanguageConfigByName(Language.getLangById(submitDO.getLanguage()));
            if(languageConfig.getCompileCommand() != null){
                // 通过沙箱编译后返回全局唯一的编译后文件的唯一标识
                userFileId = Compiler.compiler(languageConfig,
                        contestRemoteService.getCode(submitDO.getSubmitId()).getData(),
                        Language.getLangById(submitDO.getLanguage())
                );
            }

            // 当前工作目录
            Path absolutePath = Paths.get("").toAbsolutePath();
            Path relativePath = Paths.get("testData", "public", "problem", problemRespDTO.getAttach(), "test_case");
            // test绝对路径
            Path testCasePath = absolutePath.resolve(relativePath);
            // 读取testCase目录下的每一个.in
            List<List<String>> allTestFromLocal = getAllTestFromLocal(testCasePath);
            List<String> testCaseInputList = allTestFromLocal.get(0);
            List<String> testCaseOutputList = allTestFromLocal.get(1);

            // 对每个测试点进行多线程评测
            List<JSONObject> result = judgeRun.MulThreadJudge(testCaseInputList, testCaseOutputList, submitDO, problemRespDTO, userFileId);
            return getJudgeInfo(result, problemRespDTO, submitDO);
        } catch (Exception e){
            log.error("Error during judging process", e);
            return Collections.singletonMap("error", "An error occurred during the judging process.");
        }
    }

    /**
     * 对每个测试点结果进行分析，返回本次提交的整体结果
     */
    private Map<String, Object> getJudgeInfo(List<JSONObject> result, ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        int maxTime = 0;
        int maxMemory = 0;
        Integer resStatus = -1;
        String errorMsg = null;
        for(JSONObject jsonObject : result){
            int time = jsonObject.getLong("time").intValue();
            int memory = jsonObject.getLong("memory").intValue();
            Integer status = jsonObject.getInt("status");
            if(resStatus == -1 && status != SubmissionStatus.ACCEPTED.getCode()){
                resStatus = status;
                errorMsg = jsonObject.getStr("errMsg");
            }
            maxTime = Math.max(maxTime, time);
            maxMemory = Math.max(maxMemory, memory);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("maxTime", maxTime);
        resultMap.put("maxMemory", maxMemory);
        if(resStatus == -1){
            resultMap.put("code", SubmissionStatus.ACCEPTED.getCode());
        } else {
            resultMap.put("code", resStatus);
            resultMap.put("msg", errorMsg);
        }
        return resultMap;
    }

    private List<List<String>> getAllTestFromLocal(Path testCasePath) throws IOException {
        if (Files.notExists(testCasePath) || !Files.isDirectory(testCasePath)) {
            throw new ServiceException(JUDGE_PROBLEM_RESOURCES_NOT_FOUND_ERROR);
        }
        Map<String, List<Path>> testCaseMap = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(testCasePath)) {
            for (Path childPath : directoryStream) {
                String fileName = childPath.getFileName().toString();
                if (fileName.endsWith(".in") || fileName.endsWith(".out")) {
                    String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                    testCaseMap.computeIfAbsent(baseName, k -> new ArrayList<>()).add(childPath);
                }
            }
        }

        List<String> testCaseInputList = new ArrayList<>();
        List<String> testCaseOutputList = new ArrayList<>();

        for (Map.Entry<String, List<Path>> entry : testCaseMap.entrySet()) {
            List<Path> paths = entry.getValue();
            if (paths.size() != 2) {
                throw new ServiceException(BaseErrorCode.JUDGE_TESTCASE_NUMBER_NOT_SAME_ERROR);
            }
            for (Path path : paths) {
                if (path.toString().endsWith(".in")) {
                    testCaseInputList.add(path.toString());
                } else {
                    String outputHash = computeFileHash(path);
                    testCaseOutputList.add(outputHash);
                }
            }
        }
        return Arrays.asList(testCaseInputList, testCaseOutputList);
    }
    private String computeFileHash(Path filePath) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return DigestUtils.md5DigestAsHex(output.toString().getBytes(StandardCharsets.UTF_8));
    }
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\11493\\Desktop\\GZHUOJ\\GZHUOJ-backend-java\\testData\\public\\problem\\2024-08-17_C7ZK1B0PJ8S6NTIV\\test_case");
        if (Files.exists(path)){
            System.out.println("yes");
        }
    }
}
