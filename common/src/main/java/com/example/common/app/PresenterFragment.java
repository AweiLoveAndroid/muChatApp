package com.example.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import com.example.common.factory.presenter.BaseContract;

/**
 * Created by John on 2017/6/10.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends Fragment implements BaseContract.View<Presenter>{

    protected Presenter presenter;

    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //在界面onAttch之后初始化presenter
        initPresenter();
    }

    @Override
    public void showError(int str) {
        Application.showToast(str);

    }

    @Override
    public void showLoading() {
        //TODO 显示一个loading

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter=presenter;
    }
}
