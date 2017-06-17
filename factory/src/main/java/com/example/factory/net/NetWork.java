package com.example.factory.net;

import android.text.TextUtils;

import com.example.common.Common;
import com.example.factory.Factory;
import com.example.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 * Created by John on 2017/6/11.
 */

public class NetWork {
    private static NetWork instance;
    private Retrofit retrofit;
    static {
        instance=new NetWork();
    }

    private NetWork() {

    }

    public static Retrofit getRetrofit(){
        //得到一个ok http的client
        if(instance.retrofit!=null){
            return instance.retrofit;
        }
        OkHttpClient client=new OkHttpClient.Builder().
                //给请求添加拦截器
                addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original=chain.request();//拿到请求
                Request.Builder builder=original.newBuilder();//重新进行builder
                if(!TextUtils.isEmpty(Account.getToken())){
                    //注入一个token
                    builder.addHeader("token",Account.getToken());
                }
                builder.addHeader("Content-Type","application/json");
                Request newRequest=builder.build();
                return chain.proceed(newRequest);
            }
        }).build();
        Retrofit.Builder builder=new Retrofit.Builder();
        //设置电脑连接
        instance.retrofit= builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     * @return
     */
    public static RemoteService remote(){
        return NetWork.getRetrofit().create(RemoteService.class);
    }
}
