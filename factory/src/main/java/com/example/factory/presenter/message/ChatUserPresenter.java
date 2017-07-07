package com.example.factory.presenter.message;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

/**
 * Created by John on 2017/7/4.
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {
    public ChatUserPresenter( ChatContract.UserView view, String receiverId) {
        //数据源，界面，接收者，接收者类型
        super(new MessageRepository(receiverId),view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        User receiver= UserHelper.findFromLocal(receiverId);
        getView().onInit(receiver);
    }
}
