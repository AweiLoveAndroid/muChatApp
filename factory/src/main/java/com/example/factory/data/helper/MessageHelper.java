package com.example.factory.data.helper;


import com.example.factory.Factory;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.model.api.message.MessageCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息工具类
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MessageHelper {
    // 从本地找消息
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MessageCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //如果是已经发送过的消息，则不能重新发送
                //如果是文件类型，需要先上传在发送
                //直接发送进行网络调度
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);
                RemoteService service = NetWork.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && response.isSuccessful()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //是否是账户异常
                            Factory.decodeRspCode(rspModel, null);
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        card.setStatus(Message.TYPE_FILE);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }
}
