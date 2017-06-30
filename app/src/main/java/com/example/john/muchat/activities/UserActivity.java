package com.example.john.muchat.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.user.UpdateInfoFragment;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * 用户信息界面
 */
public class UserActivity extends Activity {
    @BindView(R.id.background)
    ImageView background;
    private Fragment currentFragment;

    /**
     * 显示界面的入口方法
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,UserActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main2;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        currentFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        currentFragment=new UpdateInfoFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.container,currentFragment).commit();
        loadBingPic();
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
                            Glide.with(UserActivity.this)
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
