package com.example.common.factory.data;

import android.support.annotation.StringRes;

/**
 * Created by John on 2017/6/11.
 */

public interface DataSource {
    /**
     * 同时包括了成功与失败的接口
     * @param <T> 任意类型
     */
    interface Callback<T> extends SuccessCallback<T>,FailCallback{

    }
    /**
     * 只关注成功的接口
     * @param <T>
     */
    interface SuccessCallback<T>{
        void onDataLoaded(T user);
    }

    /**
     * 只关注失败的接口
     */
    interface FailCallback{
        void onDataNotAvailable(@StringRes int strRes);
    }

    void dispose();
}
