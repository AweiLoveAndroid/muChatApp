package com.example.john.muchat;

import com.example.john.muchat.activities.MainActivity;
import com.example.john.muchat.assist.PermissionsFragment;

public class LaunchActivity extends com.example.common.app.Activity {

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionsFragment.haveAllPermissions(this,getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        }
    }
}
