package com.example.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.message.MessageCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 聊天presenter基础类
 * Created by John on 2017/7/4.
 */

public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {
    protected String receiverId;
    protected int receiverType;

    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.receiverId = receiverId;
        this.receiverType = receiverType;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if (view == null)
            return;
        List<Message> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, messages);//进行界面刷新
    }

    @Override
    public void pushText(String content) {
        MessageCreateModel model = new MessageCreateModel.Builder()
                .receiver(receiverId, receiverType)
                .content(content, Message.TYPE_STR)
                .build();
        MessageHelper.push(model);//进行网络发送
    }

    @Override
    public void pushAudio(String path) {
        //TODO
    }

    @Override
    public void pushImages(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {
        //确定消息可以重新发送
        if (Account.isLogin() && Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {
            //更改状态
            message.setStatus(Message.STATUS_CREATED);
            MessageCreateModel model = MessageCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }
}
