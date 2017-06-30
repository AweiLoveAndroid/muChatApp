package com.example.factory.data.message;

import com.example.factory.model.card.MessageCard;

/**
 * 进行消息卡片的消费
 * Created by John on 2017/6/29.
 */

public interface MessageCenter {
    void dispatch(MessageCard...cards);
}
