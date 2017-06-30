package com.example.john.muchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.view.View;

import com.example.factory.persistence.Account;
import com.example.john.muchat.activities.AccountActivity;
import com.example.john.muchat.activities.MainActivity;
import com.example.john.muchat.assist.PermissionsFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends com.example.common.app.Activity {
    private ColorDrawable backgroundDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //拿到根布局
        View root=findViewById(R.id.launch);
        //获取颜色
        int color= UiCompat.getColor(getResources(),R.color.colorPrimary);
        //创建drawable
        ColorDrawable drawable=new ColorDrawable(color);
        //设置给背景
        root.setBackground(drawable);
        backgroundDrawable=drawable;
    }

    /**
     * 给背景设置动画
     * @param endProgress  动画的结束进度
     * @param endCallback  动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback){
        //获取一个结束的颜色
        int finalColor= Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator evaluator=new ArgbEvaluator();
        int endColor= (int) evaluator.
                evaluate(endProgress,backgroundDrawable.getColor(),finalColor);
        //构建一个属性动画
        ValueAnimator animator= ObjectAnimator.ofObject(this,property,evaluator,endColor);
        animator.setDuration(1500);//设置时间
        animator.setIntValues(backgroundDrawable.getColor(),endColor);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //结束时出发
                endCallback.run();
            }
        });
        animator.start();
    }

    private Property<LaunchActivity,Object> property=new Property<LaunchActivity, Object>(Object.class,"color") {
        @Override
        public Object get(LaunchActivity object) {
            return object.backgroundDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.backgroundDrawable.setColor((Integer) value);
        }
    };

    @Override
    protected void initData() {
        super.initData();
        //动画进入到50%等待pushId
        startAnim(0.8f, new Runnable() {
            @Override
            public void run() {
                //检查等待状态
                waitPushReceiverId();
            }
        });
    }

    /**
     * 等待个推框架对pushId设置
     */
    private void waitPushReceiverId(){
        if(Account.isLogin()){
            //已经登陆，判断是否绑定，如果没有绑定就等待广播接收器进行绑定
            if(Account.isBind()){
                skip();
                return;
            }
        }else {
            //在没有登陆的情况下拿到了pushId
            if(!TextUtils.isEmpty(Account.getPushId())){
                skip();
                return;
            }

        }
        //循环等待
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        },500);
    }

    public void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });

    }

    //真实的跳转
    public void reallySkip(){
        //权限检测
        if(PermissionsFragment.haveAllPermissions(this,getSupportFragmentManager())){
            //检查跳转到主页还是登陆
            if(Account.isLogin()){
                MainActivity.show(this);
            }else {
                AccountActivity.show(this);
            }
            finish();
        }
    }
}
