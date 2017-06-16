package com.example.factory.presenter.account;

import android.support.annotation.StringRes;

import com.example.common.factory.presenter.BaseContract;

/**
 * Created by John on 2017/6/10.
 */

public interface RegisterContract {
    interface View extends BaseContract.View<Presenter>{
        void registerSuccess();

    }
    interface Presenter extends BaseContract.Presenter{
        //发起注册
        void register(String phone,String name,String password);
        //检查手机号
        boolean checkMobile(String phone);
    }
}
