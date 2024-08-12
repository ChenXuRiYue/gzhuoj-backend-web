package com.gzhuoj.usr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.*;
import com.gzhuoj.usr.dto.resp.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.service.admin.AdminService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-user/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 用户总表查询
     * @param requestParam 关键字查询
     * @return 用户分页查询返回数据
     */
    @GetMapping("/userManager/userList")
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
    // TODO 更友好的导入
//    @PostMapping("/usermanager/usergen/csv")
//    public Result<>


    /**
     * 修改事件状态 -> 题目、文章、比赛开发状态
     * @param requestParam 事件请求参数
     */
    @GetMapping("/changeStatus")
    public Result<Void> changeStatus(AdminChangeStatusReqDTO requestParam){
        adminService.changeStatus(requestParam);
        return Results.success();
    }


    @GetMapping("/filemanager")
    public Result<Void> fileManager(AdminFileManagerReqDTO  requestParam){
        return Results.success(adminService.fileManager(requestParam));
    }

}
