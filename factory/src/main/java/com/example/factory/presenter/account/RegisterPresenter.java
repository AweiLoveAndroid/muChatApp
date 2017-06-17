package com.example.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.example.common.Common;
import com.example.common.factory.data.DataSource;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * Created by John on 2017/6/10.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements
        RegisterContract.Presenter,DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        //调用开始方法，在start中启动loading
        start();
        //得到view接口
        RegisterContract.View view=getView();
        //校验
        if(!checkMobile(phone)){
            //手机号不合法
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if(name.length()<2){
            //姓名至少两位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if(password.length()<6){
            //密码需要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else {
            //进行网络请求并设置回送接口为自己
            RegisterModel model=new RegisterModel(phone,password,name,Account.getPushId());
            AccountHelper.register(model,this);//构造model，进行请求调用
        }
    }

    /**
     * 检查手机号是否合法
     * @param phone 手机号码
     * @return 合法就为true
     */
    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone)&&Pattern.matches(Common.Constance.REGEX_MOBILE,phone);
    }

    //当网络请求成功
    @Override
    public void onDataLoaded(User user) {
        //告知界面注册成功
        final RegisterContract.View view=getView();
        if(view==null){
            return;//不保证处于主线程状态
        }
        //线程切换
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        //网络请求告知注册失败
        final RegisterContract.View view=getView();
        if(view==null){
            return;//不保证处于主线程状态
        }
        //线程切换
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //主界面显示注册失败
                view.showError(strRes);
            }
        });
    }
}
