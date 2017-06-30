package com.example.factory.presenter.contact;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.User;

/**
 * Created by John on 2017/6/27.
 */

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter{
        User getUserPersonal();//获取用户信息
    }
    interface View extends BaseContract.View<Presenter>{
        String getUserId();
        void onLoadDone(User user);
        void allowSayHello(boolean isAllow);
        void setFollowStatus(boolean isFollow);
    }
}
