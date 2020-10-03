package com.tooyi.shirospring.service;

import com.tooyi.shirospring.entity.UserInfo;

public interface UserInfoService {
    /** 通过username查找用户信息；*/
    public UserInfo findByUsername(String username);
}