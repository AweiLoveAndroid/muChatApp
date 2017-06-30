package com.example.common.app;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by John on 2017/5/19.
 */

public abstract class Activity extends AppCompatActivity {
    protected PlaceHolderView placeHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();//在界面初始化之前调用的窗口
        if(initArgs(getIntent().getExtras())){
            setContentView(getContentLayoutId());
            initBefore();
            initWidget();
            initData();
        }else {
            finish();
        }

    }
    protected void initWindow(){

    }

    //初始化控件调用之前
    protected void initBefore(){

    }
    protected boolean initArgs(Bundle bundle){
        return true;
    }
    //得到当前界面的资源文件ID
    protected abstract int getContentLayoutId();
    //初始化控件
    protected void initWidget(){
        ButterKnife.bind(this);
    }
    //初始化数据
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();//导航界面点击返回时
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        ///得到当前活动下的所有碎片
        List<android.support.v4.app.Fragment>fragments=getSupportFragmentManager().getFragments();
        //遍历碎片
        if(fragments!=null&&fragments.size()>0){
            for(Fragment fragment:fragments){
                //判断是否是我们自定义的fragment
                if(fragment instanceof com.example.common.app.Fragment){
                    //处理我们自定义fragment的返回按钮
                    if(((com.example.common.app.Fragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();////导航界面点击返回时
    }

    public void setPlaceHolderView(PlaceHolderView placeHolderView){
        this.placeHolderView=placeHolderView;
    }
}
