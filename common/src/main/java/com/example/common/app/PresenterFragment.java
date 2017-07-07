package com.example.common.app;

import android.content.Context;

import com.example.common.factory.presenter.BaseContract;
import com.example.common.widget.convention.PlaceHolderView;

/**
 * Created by John on 2017/6/10.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends Fragment implements BaseContract.View<Presenter>{

    protected Presenter presenter;

    protected PlaceHolderView placeHolderView;

    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //在界面onAttch之后初始化presenter
        initPresenter();
    }

    @Override
    public void showError(int str) {
        if(placeHolderView!=null){
            placeHolderView.triggerError(str);
            return;
        }else {
            Application.showToast(str);
        }
        Application.showToast(str);
    }

    @Override
    public void showLoading() {
        //TODO 显示一个loading
        if(placeHolderView!=null){
            placeHolderView.triggerLoading();
        }
    }

    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.placeHolderView = placeHolderView;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter!=null)
            presenter.destroy();
    }
}
