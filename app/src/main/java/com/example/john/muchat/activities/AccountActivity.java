package com.example.john.muchat.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.sdk.android.common.utils.HttpUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.account.AccountTrigger;
import com.example.john.muchat.fragments.account.LoginFragment;
import com.example.john.muchat.fragments.account.RegisterFragment;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountActivity extends Activity implements AccountTrigger {

    private Fragment currentFragment;
    private Fragment loginFragment;
    private Fragment registerFragment;

    @BindView(R.id.background)
    ImageView background;

    /**
     * 别的活动进入账户活动的入口
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //默认是登陆界面,此时注册界面为空
        loginFragment = new LoginFragment();
        currentFragment=loginFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, currentFragment).commit();
        //初始化登陆背景图片
        loadBingPic();
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (currentFragment == loginFragment) {
            if (registerFragment == null) {
                //默认情况下为空,初始化
                registerFragment = new RegisterFragment();
            }
            fragment=registerFragment;
            //fragment = registerFragment;
        } else {
            fragment = loginFragment;
        }
        //重新赋值当前正在显示的fragment
        currentFragment = fragment;
        //切换显示
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    //加载必应每日一图
    public void loadBingPic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://guolin.tech/api/bing_pic")
                            .build();
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(AccountActivity.this)
                                    .load(responseData)
                                    .centerCrop()
                                    .into(background);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
