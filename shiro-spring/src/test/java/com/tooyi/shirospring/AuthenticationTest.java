package com.tooyi.shirospring;

import com.tooyi.shirospring.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Shiro 简单测试验证
 */
public class AuthenticationTest {
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    SimpleAccountRealm simpleAccountRealm2 = new SimpleAccountRealm();
    @Before // 在方法开始前添加一个用户
    public void addUser() {
        simpleAccountRealm.addAccount("tooyi", "123456");
        simpleAccountRealm2.addAccount("tooyi2", "123456", "admin", "user");
    }
    @Test
    public void testAuthentication() {
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);
        // 2.主体提交认证请求
        // 设置SecurityManager环境
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 获取当前主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("tooyi", "123456");
        System.err.println("token:" + token.toString());
        // 登录
        subject.login(token);
        // subject.isAuthenticated()方法返回一个boolean值,用于判断用户是否认证成功
        // 输出true
        System.err.println("isAuthenticated:" + subject.isAuthenticated());
        // 登出
        subject.logout();
        // 输出false
        System.err.println("isAuthenticated:" + subject.isAuthenticated());
    }

    @Test
    public void testAuthentication2() {
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm2);
        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager); // 设置SecurityManager环境
        Subject subject = SecurityUtils.getSubject(); // 获取当前主体
        UsernamePasswordToken token = new UsernamePasswordToken("tooyi2", "123456");
        subject.login(token); // 登录
        // subject.isAuthenticated()方法返回一个boolean值,用于判断用户是否认证成功
        System.out.println("isAuthenticated:" + subject.isAuthenticated()); // 输出true
        // 判断subject是否具有admin和user两个角色权限,如没有则会报错
        subject.checkRoles("admin","user");
//        subject.checkRole("xxx"); // 报错
    }

    @Test
    public void testAuthentication3() {
        MyRealm myRealm = new MyRealm(); // 实现自己的 Realm 实例
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(myRealm);
        // 2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager); // 设置SecurityManager环境
        Subject subject = SecurityUtils.getSubject(); // 获取当前主体
        UsernamePasswordToken token = new UsernamePasswordToken("tooyi", "123456");
        subject.login(token); // 登录
        // subject.isAuthenticated()方法返回一个boolean值,用于判断用户是否认证成功
        System.err.println("isAuthenticated:" + subject.isAuthenticated()); // 输出true
        // 判断subject是否具有admin和user两个角色权限,如没有则会报错
        subject.checkRoles("admin", "user");
//        subject.checkRole("xxx"); // 报错
        // 判断subject是否具有user:add权限
        subject.checkPermission("user:add");
    }

    @Test
    public void testMd5() {
        String password = "123456";
        // 生成随机数
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 加密次数：2
        int times = 2;
        // 加密算法
        String alogrithmName = "md5";
        String encodePassword = new SimpleHash(alogrithmName, password, salt, times).toString();
        System.err.printf("原始密码是 %s , 盐是： %s, 运算次数是： %d, 运算出来的密文是：%s ",password,salt,times,encodePassword);
    }
}