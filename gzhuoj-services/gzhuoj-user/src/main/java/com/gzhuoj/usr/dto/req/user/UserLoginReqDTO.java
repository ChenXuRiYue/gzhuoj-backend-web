package com.gzhuoj.usr.dto.req.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginReqDTO {
    /**
     *  用户账号
     */
    private String userAccount;

    /**
     *  用户密码
     */
    private String password;
}
