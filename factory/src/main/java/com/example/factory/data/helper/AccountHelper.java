package com.example.factory.data.helper;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.common.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RegisterModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.model.db.AppDataBase;
import com.example.factory.model.db.User;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by John on 2017/6/10.
 */

public class AccountHelper {

    /**
     * 注册的接口，异步的调用
     *
     * @param model    传入一个注册的model
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        //调用retrofit对网络请求接口做代理
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登陆的接口
     *
     * @param model    登陆的model
     * @param callback
     */
    public static void login(LoginModel model, final DataSource.Callback<User> callback) {
        //调用retrofit对网络请求接口做代理
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        //异步请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备ID进行绑定从操作
     *
     * @param callback
     */
    public static void bindPush(DataSource.Callback<User> callback) {
        //先检查push id是否为空
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId))
            return;
        //调用retrofit对网络请求接口做代理
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }


    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            //请求成功返回
            //获得全局model，内部使用的gson解析
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                //拿到实体
                AccountRspModel accountRspModel = rspModel.getResult();
                //获取我的信息
                User user = accountRspModel.getUser();
                user.save();
                      /*  FlowManager.getModelAdapter(User.class).save(user);
                        DatabaseDefinition definition=FlowManager.getDatabase(AppDataBase.class);
                        definition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                FlowManager.getModelAdapter(User.class).save(user);
                            }
                        }).build().execute();*/
                Account.login(accountRspModel);//同步到持久化xml
                //判断绑定状态
                if (accountRspModel.isBind()) {
                    Account.setBind(true);//设置绑定状态
                    if (callback != null)
                        callback.onDataLoaded(user);
                } else {
                    //callback.onDataLoaded(accountRspModel.getUser());
                    //进行绑定
                    bindPush(callback);
                }
            } else {
                //TODO 对返回的RspModel中失败code进行解析，解析到对应的String资源上面
                // callback.onDataNotAvailable();
                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //网络请求失败
            if (callback != null)
                callback.onDataNotAvailable(R.string.data_network_error);
        }
    }
}
