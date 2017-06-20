package com.example.john.muchat.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.app.Activity;
import com.example.factory.persistence.Account;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.AccountActivity;
import com.example.john.muchat.assist.PermissionsFragment;
import com.example.john.muchat.fragments.main.ActiveFragment;
import com.example.john.muchat.fragments.main.ContactFragment;
import com.example.john.muchat.fragments.main.GroupFragment;
import com.example.john.muchat.helper.Navhelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener
        , Navhelper.OnTabChangeListener<Integer> {
    @BindView(R.id.action)
    FloatActionButton action;
    @BindView(R.id.appbar)
    View appbar;
    @BindView(R.id.portrait)
    View portrait;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout_container)
    FrameLayout container;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    public static void show(Context context){
        //进入主界面
        context.startActivity(new Intent(context,MainActivity.class));
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        navhelper = new Navhelper<>(getSupportFragmentManager(), R.id.layout_container,
                this,this);//初始化工具类
        navhelper.add(R.id.action_home, new Navhelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new Navhelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new Navhelper.Tab<>(ContactFragment.class, R.string.title_contact));
        navigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this).load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(appbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                        //this指的是view Target
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        Menu menu=navigation.getMenu();
        menu.performIdentifierAction(R.id.action_home,0);//触发首次选中home
    }

    @OnClick(R.id.search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.action)
    void onActionClick() {
        AccountActivity.show(this);
    }

    private Navhelper<Integer> navhelper;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return navhelper.performClickMenu(item.getItemId());
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        Log.d("MainActivity:","initArgs");
        if(Account.isComplete()){
            //如果用户信息完全，正常
            Log.d("MainActivity:","用户信息完全");
            return super.initArgs(bundle);
        }else{
            Log.d("MainActivity:","用户信息不完全");
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    public void onTabChanged(Navhelper.Tab<Integer> newTab, Navhelper.Tab<Integer> oldTab) {
        title.setText(newTab.extra);//从额外字段中取出title资源ID
        //设置浮动按钮动画
        float transY=0;
        float rotation=0;
        if(Objects.equals(newTab.extra,R.string.title_home)){
            transY= Ui.dipToPx(getResources(),76);//主界面时隐藏
        }else{
            if(Objects.equals(newTab.extra,R.string.title_group)){
                action.setImageResource(R.drawable.ic_group_add);
                rotation=-360;
            }else{
                action.setImageResource(R.drawable.ic_contact_add);
                rotation=360;
            }
        }
        //开始动画，先旋转再竖直平移，附带弹性效果（插值器）
        action.animate().rotation(rotation)
                .translationY(transY)
                .setDuration(480)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .start();
    }
}
