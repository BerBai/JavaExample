package com.tooyi.shirospring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class SysRole {
    @Id
    @GeneratedValue
    // 主键.
    private Long id;
    // 角色名称,如 admin/user
    private String name;
    // 角色描述,用于UI显示
    private String description;
    // 角色 -- 权限关系：多对多
    @JsonIgnoreProperties(value = {"roles"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SysRolePermission", joinColumns = {@JoinColumn(name = "roleId")}
            , inverseJoinColumns = {@JoinColumn(name = "permissionId")})
    private List<SysPermission> permissions;
    // 用户 -- 角色关系：多对多
    @JsonIgnoreProperties(value = {"roles"})
    @ManyToMany
    @JoinTable(name = "SysUserRole", joinColumns = {@JoinColumn(name = "roleId")}
            , inverseJoinColumns = {@JoinColumn(name = "uid")})
    private List<UserInfo> userInfos;// 一个角色对应多个用户

    /**
     * getter and setter
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}
