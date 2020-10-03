package com.tooyi.shirospring.controller;

import com.tooyi.shirospring.entity.UserInfo;
import com.tooyi.shirospring.service.UserInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserInfoController {
    @Resource
    UserInfoService userInfoService;

    /**
     * 按username账户从数据库中取出用户信息
     *
     * @param username 账户
     * @return
     */
    @GetMapping("/userList")
    @RequiresPermissions("userInfo:view") // 权限管理.
    public UserInfo findUserInfoByUsername(@RequestParam String username) {
        System.err.println("get用户");
        return userInfoService.findByUsername(username);
    }

    /**
     * 简单模拟从数据库添加用户信息成功
     *
     * @return
     */
    @PostMapping("/userAdd")
    @RequiresPermissions("userInfo:add")
    public String addUserInfo() {
        return "addUserInfo success!";
    }

    /**
     * 简单模拟从数据库删除用户成功
     *
     * @return
     */
    @DeleteMapping("/userDelete")
    @RequiresPermissions("userInfo:delete")
    public String deleteUserInfo() {
        return "deleteUserInfo success!";
    }
}
