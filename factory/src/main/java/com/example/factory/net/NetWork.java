package com.example.factory.net;

import com.example.common.Common;
import com.example.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 * Created by John on 2017/6/11.
 */

public class NetWork {
    public static Retrofit getRetrofit(){
        //得到一个ok http的client
        OkHttpClient client=new OkHttpClient.Builder().build();
        Retrofit.Builder builder=new Retrofit.Builder();
        //设置电脑连接
        return builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
    }
}
