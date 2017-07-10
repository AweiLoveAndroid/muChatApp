package com.example.factory.model.api.message;

import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;

import java.util.Date;
import java.util.UUID;

/**
 * Created by John on 2017/7/5.
 */

public class MessageCreateModel {
    private String id;
    private String content;
    private String attach;

    // 消息类型
    private int type = Message.TYPE_STR;


    // 接收者 可为空
    private String receiverId;

    // 接收者类型，群，人
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MessageCreateModel() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }


    public String getContent() {
        return content;
    }


    public String getAttach() {
        return attach;
    }


    public int getType() {
        return type;
    }


    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    //同步到卡片的最新状态
    public void refreshByCard() {
        if(card==null)
            return;
        //刷新内容和附件
        this.content=card.getContent();
        this.attach=card.getAttach();
    }

    /**
     * 建造者模式，快速的建立一个发送model
     */
    public static class Builder {
        private MessageCreateModel model;

        public Builder() {
            this.model = new MessageCreateModel();
        }

        //设置接收者
        public Builder receiver(String receiverId, int receiverType) {
            this.model.receiverId = receiverId;
            this.model.receiverType = receiverType;
            return this;
        }

        //设置内容
        public Builder content(String content, int type) {
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        public Builder attach(String attch) {
            this.model.attach = attch;
            return this;
        }

        public MessageCreateModel build() {
            return this.model;
        }

    }

    private MessageCard card;

    public MessageCard buildCard() {
        if (card == null) {
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());
            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return this.card;
    }

    /**
     * 把一个message转换为创建状态的model
     * @param message
     * @return
     */
    public static MessageCreateModel buildWithMessage(Message message) {
        MessageCreateModel model = new MessageCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();
        if (message.getReceiver() != null) {
            model.receiverId = message.getReceiver().getId();
            model.receiverType = message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = message.RECEIVER_TYPE_GROUP;
        }
        return model;
    }
}
