package com.gzhuoj.usr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserGenReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.service.AdminService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/admin")
public class AdminController {
    private final AdminService adminService;

    /**
     * 用户权限分页查询
     * @param requestParam search -> 模糊查询字符串
     * @return 用户 账号 + 用户名 + 权限组成
     */
    @GetMapping("/privilege/list")
    public Result<IPage<AdminPrivilegeListRespDTO>> privilegeList(AdminPrivilegeListReqDTO requestParam){
        return Results.success(adminService.privilegeList(requestParam));
    }

    /**
     * 用户总表查询
     * @param requestParam 关键字查询
     * @return 用户分页查询返回数据
     */
    @GetMapping("/usermanager/userList")
    public Result<IPage<AdminUserListRespDTO>> userManagerList(AdminUserListReqDTO requestParam){
        return Results.success(adminService.userManagerList(requestParam));
    }

    /**
     * 用户删除
     * @param userAccount 用户账号
     */
    @DeleteMapping("/usermanager/delete")
    public Result<Void> userDelete(@RequestParam String userAccount){
        adminService.deleteUser(userAccount);
        return Results.success();
    }

    /**
     * 用户批量生成
     * @param requestParam 用户数据转编的URL编码字符串
     * @return 生成的用户信息集合
     */
    @PostMapping("/usermanager/usergen")
    public Result<List<AdminUserGenRespDTO>> userGen(@RequestBody AdminUserGenReqDTO requestParam){
        return Results.success(adminService.userGen(requestParam));
    }
}