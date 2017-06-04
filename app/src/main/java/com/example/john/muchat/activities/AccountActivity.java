package com.example.john.muchat.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.account.UpdateInfoFragment;
import com.yalantis.ucrop.UCrop;

public class AccountActivity extends Activity {

    private Fragment currentFragment;

    /**
     * 别的活动进入账户活动的入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        currentFragment=new UpdateInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,currentFragment).commit();
    }
    //activity中收到剪切图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        currentFragment.onActivityResult(requestCode,resultCode,data);
    }
}
