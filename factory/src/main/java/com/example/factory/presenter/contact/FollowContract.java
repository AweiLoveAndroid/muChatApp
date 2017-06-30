package com.example.factory.presenter.contact;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.card.UserCard;

/**
 * 关注的接口定义
 * Created by John on 2017/6/25.
 */

public interface FollowContract {
    //任务调度者
    interface Presenter extends BaseContract.Presenter{
        void follow(String id);//关注一个人
    }
    interface View extends BaseContract.View<Presenter>{
        //成功的情况下返回一个用户信息
        void onFollowSucceed(UserCard userCard);
    }
}
