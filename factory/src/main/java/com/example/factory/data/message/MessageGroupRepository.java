package com.example.factory.data.message;

import android.support.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 聊天记录列表
 * Created by John on 2017/7/4.
 */
public class MessageGroupRepository extends BaseDbRepository<Message>
        implements MessageDataSource {
    // 聊天的群Id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);


        // 无论是直接发还是别人发，只要是发到这个群的，
        // 那个这个group_id就是receiverId
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Message message) {
        // 如果消息的Group不为空，则一定是发送到一个群的
        // 如果群Id等于我们需要的，那就是通过
        return message.getGroup() != null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        // 反转返回的集合
        Collections.reverse(tResult);
        // 然后再调度
        super.onListQueryResult(transaction, tResult);
    }
}

