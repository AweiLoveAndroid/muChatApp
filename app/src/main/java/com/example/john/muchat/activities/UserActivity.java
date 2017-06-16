package com.example.john.muchat.activities;

import android.content.Intent;

import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.john.muchat.R;

public class UserActivity extends Activity {
    private Fragment currentFragment;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_main2;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        currentFragment.onActivityResult(requestCode,resultCode,data);
    }
}
