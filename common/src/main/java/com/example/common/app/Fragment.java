package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by John on 2017/5/19.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;
    protected PlaceHolderView placeHolderView;
    protected boolean isFirstInitData=true;

    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.placeHolderView = placeHolderView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());//初始化参数
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot==null){
            int layID=getContentLayoutID();
            View root=inflater.inflate(layID,container,false);
            initWidget(root);
            mRoot=root;
        }else{
            if(mRoot.getParent()!=null){
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isFirstInitData){
            isFirstInitData=false;
            onFirstInit();
        }
        initData();
    }

    //首次初始化数据
    protected void onFirstInit(){

    }
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    protected abstract int getContentLayoutID();
    protected void initWidget(View root){
        mRootUnbinder=ButterKnife.bind(this,root);
    }
    protected void initData(){

    }

    /**
     * 返回按键触发时调用
     * @return
     * 返回true表示逻辑已经处理，activity不用finish
     * 返回false表示逻辑没有处理，activity走自己的逻辑
     */
    public boolean onBackPressed(){
        return false;
    }
}
