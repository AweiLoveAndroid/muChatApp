package com.example.factory.presenter.account;

import android.support.annotation.StringRes;

import com.example.common.factory.presenter.BaseContract;

/**
 * Created by John on 2017/6/10.
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        void loginSuccess();
    }
    interface Presenter extends BaseContract.Presenter{
        //发起注册
        void login(String phone,String password);
    }
}
