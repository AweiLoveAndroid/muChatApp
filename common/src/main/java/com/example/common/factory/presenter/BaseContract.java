package com.example.common.factory.presenter;

import android.support.annotation.StringRes;

import com.example.common.widget.recycler.RecyclerAdapter;

/**
 * MVP模式中公共基本契约
 * Created by John on 2017/6/10.
 */

public interface BaseContract {
    interface View<T extends Presenter>{
        //显示一个字符串错误
        void showError(@StringRes int str);
        void showLoading();//显示进度条
        void setPresenter(T presenter);//支持设置一个Presenter
    }
    interface Presenter{
        void start();//公用的开始方法
        void destroy();//公用的销毁出发
    }
    interface RecyclerView<T extends Presenter,ViewModel> extends View<T>{
        RecyclerAdapter<ViewModel>getRecyclerAdapter();
        void onAdapterDataChanged();
    }
}
