package com.example.factory.model.db;

import com.example.common.factory.model.Author;
import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.Date;
import java.util.Objects;

/**
 * Created by John on 2017/6/11.
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author, DiffUiDataCallback.UiDataDiffer<User> {
    public static final int MAN = 1;
    public static final int WOMAN = 2;
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String description;
    @Column
    private int sex = 0;

    @Column
    private int follows;
    @Column
    private int following;
    @Column
    private boolean isFollow;

    @Column
    private Date modifyAt;

    @Column
    private String alias;//对好友的备注信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (sex != user.sex) return false;
        if (follows != user.follows) return false;
        if (following != user.following) return false;
        if (isFollow != user.isFollow) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (portrait != null ? !portrait.equals(user.portrait) : user.portrait != null)
            return false;
        if (description != null ? !description.equals(user.description) : user.description != null)
            return false;
        if (modifyAt != null ? !modifyAt.equals(user.modifyAt) : user.modifyAt != null)
            return false;
        return alias != null ? alias.equals(user.alias) : user.alias == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", description='" + description + '\'' +
                ", sex=" + sex +
                ", follows=" + follows +
                ", following=" + following +
                ", isFollow=" + isFollow +
                ", modifyAt=" + modifyAt +
                ", alias='" + alias + '\'' +
                '}';
    }

    @Override
    public boolean isSame(User old) {
        if (this == old) return true;
        return Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(User old) {
        return this == old ||
                (Objects.equals(name, old.name)
                        && Objects.equals(sex, old.sex)
                        && Objects.equals(portrait, old.portrait)
                        && Objects.equals(isFollow, old.isFollow));
    }
}
