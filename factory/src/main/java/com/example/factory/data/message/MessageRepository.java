package com.example.factory.data.message;

import android.support.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 聊天记录列表
 * Created by John on 2017/7/4.
 */

public class MessageRepository extends BaseDbRepository<Message> implements MessageDataSource {
    private String receiverId;//聊天对象ID

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(receiverId)).
                        and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt,false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);//反转返回的集合
        super.onListQueryResult(transaction, tResult);
    }

    @Override
    protected boolean isRequired(Message message) {
        //receiverId如果是发送者，group为空的情况下就是发送给我
        //如果接收者不为空，那就是发送给某个人的
        return (receiverId.equalsIgnoreCase(message.getSender().getId())
                && message.getGroup() == null)
                || (message.getReceiver() != null &&
                receiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }
}
