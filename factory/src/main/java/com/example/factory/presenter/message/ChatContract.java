package com.example.factory.presenter.message;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

/**
 * Created by John on 2017/7/4.
 */

public interface ChatContract {
    interface Presenter extends BaseContract.Presenter{
        void pushText(String content);
        void PushAudio(String path);
        void pushImages(String[] paths);//图片可以多选，所以发送地址数组
        boolean rePush(Message message);//重新发送
    }
    interface View<InitModel>extends BaseContract.RecyclerView<Presenter,Message>{
        void onInit(InitModel model);//初始化的model
    }
    interface UserView extends View<User>{

    }
    interface GroupView extends View<Group>{

    }
}
