package com.gzhuoj.usr.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzhuoj.usr.dto.req.user.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.user.UserLoginRespDTO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// 仅加载UserController。 测试 controller 层的请求接收响应情况。 不涉及真实的业务逻辑
// 1. 学习的点。找到方法模拟service 层响应。 （模拟被屏蔽的层）
// 2. 测试的目的怎么描述
// 2.1 仅测试 网络接口层（HTTP 响应解析 逻辑代码的编写正确性）
// 2.2（明确的输入， 明确的输出）
@WebMvcTest(UserController.class)
public class UserControllerTest {
    // 创建Mock 模拟对象。并且定义其行为响应逻辑，完成屏蔽层的模拟
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Test
    public void testLogin() throws Exception {
        // 1. 模拟参数
        // 1.1 模拟请求参数 -> 账户信息
        UserLoginReqDTO requestParam = UserLoginReqDTO.builder()
                .userAccount("test")
                .password("123")
                .build();
        // 1.2 模拟 HttpServletResponse
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        // 1.3 返回值模拟
        UserLoginRespDTO userLoginRespDTO = UserLoginRespDTO.builder()
                .userAccount("test")
                .username("test")
                .build();
        // 2. 屏蔽层反馈预设
        Mockito.when(userService.login(Mockito.any(UserLoginReqDTO.class), Mockito.any(HttpServletResponse.class)))
                .thenReturn(userLoginRespDTO);

        // 模拟请求并且断言结果
        mockMvc.perform(post("/api/gzhuoj-user/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    // 它是 Jackson 提供的一个核心工具类，用于在 Java 和 JSON 之间进行序列化和反序列化。
                    .content(new ObjectMapper().writeValueAsString(requestParam)))
                // 断言
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userAccount").value("test"))
                .andExpect(jsonPath("$.data.username").value("test"));

         // 4. 验证服务层交互
        // 限制是： 了次数， （匹配目标的定位规则：）方法调用详情，方法传参详情
            Mockito.verify(userService, Mockito.times(1))
                    .login(Mockito.any(UserLoginReqDTO.class), Mockito.any(HttpServletResponse.class));

    }
}