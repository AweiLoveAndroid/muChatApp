package com.example.factory.data.user;

import com.example.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 * Created by John on 2017/6/28.
 */

public interface UserCenter {
    //分发处理一堆卡片,并更新到数据库
    void dispatch(UserCard... userCards);
}
