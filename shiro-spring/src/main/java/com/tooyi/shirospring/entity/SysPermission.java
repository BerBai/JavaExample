package com.tooyi.shirospring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class SysPermission {
    @Id
    @GeneratedValue
    // 主键.
    private Long id;
    // 权限名称,如 user:select
    private String name;
    // 权限描述,用于UI显示
    private String description;
    // 权限地址.
    private String url;

    @JsonIgnoreProperties(value = {"permissions"})
    @ManyToMany
    @JoinTable(name = "SysRolePermission", joinColumns = {@JoinColumn(name = "permissionId")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})

    // List<> 一个权限可以被多个角色使用
    private List<SysRole> roles;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}
