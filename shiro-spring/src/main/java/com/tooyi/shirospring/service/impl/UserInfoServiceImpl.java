package com.tooyi.shirospring.service.impl;

import com.tooyi.shirospring.dao.UserInfoDao;
import com.tooyi.shirospring.entity.UserInfo;
import com.tooyi.shirospring.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    UserInfoDao userInfoDao;
    @Override
    public UserInfo findByUsername(String username) {
        return userInfoDao.findByUsername(username);
    }
}