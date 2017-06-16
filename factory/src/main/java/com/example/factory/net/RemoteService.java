package com.example.factory.net;

import com.example.factory.model.api.RegisterModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.RspModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求的接口
 * Created by John on 2017/6/11.
 */

public interface RemoteService {
    /**
     * 网络请求的一个注册接口
     * @param model 传入的register model
     * @return 返回的是Rsp model
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>>accountRegister(@Body RegisterModel model);

}
