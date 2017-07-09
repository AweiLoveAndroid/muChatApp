package com.example.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import net.qiujuer.widget.airpanel.AirPanelLinearLayout;

/**
 * Created by John on 2017/7/2.
 */

public class MessageLayout extends AirPanelLinearLayout {
    public MessageLayout(Context context) {
        super(context);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MessageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            insets.left=0;
            insets.right=0;
            insets.top=0;
        }
        return super.fitSystemWindows(insets);
    }
}
