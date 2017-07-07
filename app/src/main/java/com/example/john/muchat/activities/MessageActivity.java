package com.example.john.muchat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.common.app.Fragment;
import com.example.common.factory.model.Author;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.message.ChatGroupFragment;
import com.example.john.muchat.fragments.message.ChatUserFragment;

public class MessageActivity extends com.example.common.app.Activity {
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID:";//接收者ID
    public static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";//判断接受对象是人还是群
    private String receiverId;
    private boolean isGroup;

    /**
     * 显示人的聊天界面
     *
     * @param context 上下文
     * @param author  人的信息
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty(author.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 通过Session发起聊天
     *
     * @param context 上下文
     * @param session Session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || TextUtils.isEmpty(session.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    /**
     * 显示群的聊天界面
     *
     * @param context
     * @param group
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        receiverId = bundle.getString(KEY_RECEIVER_ID);
        isGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(receiverId);
    }

    @Override
    protected void initWidget() {
        setTitle("");
        Fragment fragment;
        if (isGroup)
            fragment = new ChatGroupFragment();
        else
            fragment = new ChatUserFragment();
        Bundle bundle = new Bundle();//从activity传递参数到fragment
        bundle.putString(KEY_RECEIVER_ID, receiverId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).commit();
        super.initWidget();
    }
}
