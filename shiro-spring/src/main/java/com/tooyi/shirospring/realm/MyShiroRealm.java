package com.tooyi.shirospring.realm;

import com.tooyi.shirospring.entity.SysPermission;
import com.tooyi.shirospring.entity.SysRole;
import com.tooyi.shirospring.entity.UserInfo;
import com.tooyi.shirospring.service.UserInfoService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 能进入这里说明用户已经通过验证了
        UserInfo userInfo = (UserInfo) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (SysRole role : userInfo.getRoles()) {
            simpleAuthorizationInfo.addRole(role.getName());
            for (SysPermission permission : role.getPermissions()) {
                simpleAuthorizationInfo.addStringPermission(permission.getName());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户输入的账户
        String username = (String) authenticationToken.getPrincipal();
        System.out.println(authenticationToken.getPrincipal());
        // 通过username从数据库中查找 UserInfo 对象
        // 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = userInfoService.findByUsername(username);
        if (null == userInfo) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                userInfo, // 用户名
                userInfo.getPassword(), // 密码
                ByteSource.Util.bytes(userInfo.getSalt()), // salt=username+salt
                getName() // realm name
        );
        return simpleAuthenticationInfo;
    }
}
