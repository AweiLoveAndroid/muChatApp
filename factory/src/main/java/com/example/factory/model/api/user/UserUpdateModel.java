package com.example.factory.model.api.user;

/**
 * 用户更新信息
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class UserUpdateModel {
    private String name;
    private String portrait;
    private String description;
    private int sex;

    public UserUpdateModel(String name, String portrait, String desc, int sex) {
        this.name = name;
        this.portrait = portrait;
        this.description = desc;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserUpdateModel{" +
                "name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", description='" + description + '\'' +
                ", sex=" + sex +
                '}';
    }
}
