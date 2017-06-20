package com.example.factory.presenter.user;

import com.example.common.factory.presenter.BaseContract;

/**
 * 更新用户信息基本契约
 * Created by John on 2017/6/19.
 */

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void update(String photoFilePath,String desc,boolean isMan);
    }
    interface View extends BaseContract.View<Presenter>{
        void updateSucceed();
    }
}
