package com.example.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.common.R;

/**
 * Created by John on 2017/6/24.
 */

public abstract class ToolbarActivity extends Activity {
    protected Toolbar toolbar;

    @Override
    protected void initWidget() {
        initToolbar((Toolbar) findViewById(R.id.toolbar));
        super.initWidget();
    }

    public void initToolbar(Toolbar toolbar){
        this.toolbar=toolbar;
        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    protected void initTitleNeedBack(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            //左上角返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
