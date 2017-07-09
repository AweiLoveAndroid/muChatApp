package com.example.factory.model.api.group;

/**
 * Created by John on 2017/7/8.
 */

import java.util.HashSet;
import java.util.Set;

/**
 * 群创建的Model
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupCreateModel {
    private String name;// 群名称
    private String description;// 群描述
    private String picture;// 群图片
    private Set<String> users = new HashSet<>();

    public GroupCreateModel(String name, String description, String picture, Set<String> users) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.users = users;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
