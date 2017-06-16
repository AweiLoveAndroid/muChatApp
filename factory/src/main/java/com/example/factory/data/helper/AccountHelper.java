package com.example.factory.data.helper;

import android.util.Log;
import android.widget.Toast;

import com.example.common.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RegisterModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.model.db.User;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;

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
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步请求
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                //请求成功返回
                //获得全局model，内部使用的gson解析
                Log.d("response:", "success");
                RspModel<AccountRspModel> rspModel = response.body();
                Log.d("response:", rspModel.toString());
                if (rspModel.success()) {
                    //拿到实体
                    AccountRspModel accountRspModel = rspModel.getResult();
                    //判断绑定状态
                    if (accountRspModel.isBind()) {
                        User user = accountRspModel.getUser();
                        //TODO 进行数据库写入和缓存绑定,然后返回
                        callback.onDataLoaded(user);
                    } else {
                        callback.onDataLoaded(accountRspModel.getUser());
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
                Log.d("response:", "failure");
                Log.d("response:", "Throwable t is :" + t.toString());
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备ID进行绑定从操作
     *
     * @param callback
     */
    public static void bindPush(DataSource.Callback<User> callback) {
        //先抛出一个错误,绑定暂时还没有进行
        callback.onDataNotAvailable(R.string.app_name);
    }
}
