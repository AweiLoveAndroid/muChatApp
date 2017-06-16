package com.example.common.factory.presenter;

/**
 * Created by John on 2017/6/10.
 */

public class BasePresenter<T extends BaseContract.View>implements BaseContract.Presenter {
    protected T myView;
    public BasePresenter(T view){
        setView(view);
    }
    protected void setView(T view){
        myView=view;
        myView.setPresenter(this);
    }
    //供子类使用的get view的操作,不允许被复写
    protected final T getView(){
        return myView;
    }
    @Override
    public void start() {
        //开始的时候进行loading调用
        T view=myView;
        if(view!=null){
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        T view=myView;
        myView=null;
        if(view!=null){
            view.setPresenter(null);
        }
    }
}
