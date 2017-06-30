package com.example.factory.data.group;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 * Created by John on 2017/6/29.
 */

public interface GroupCenter {
    void dispatch(GroupCard...cards);
    void dispatch(GroupMemberCard...cards);
}
