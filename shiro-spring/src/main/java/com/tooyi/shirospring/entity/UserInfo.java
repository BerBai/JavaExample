package com.tooyi.shirospring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserInfo {
    @Id
    @GeneratedValue
    private Long id; // 主键.
    @Column(unique = true)
    private String username; // 登录账户,唯一.
    private String name; // 名称(匿名或真实姓名),用于UI显示
    private String password; // 密码.
    private String salt; // 加密密码的盐
    @JsonIgnoreProperties(value = {"userInfos"})
    @ManyToMany(fetch = FetchType.EAGER) // 立即从数据库中进行加载数据
    @JoinTable(name = "SysUserRole", joinColumns = @JoinColumn(name = "uid"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<SysRole> roles; // 一个用户具有多个角色

    /**
     * getter and setter
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}