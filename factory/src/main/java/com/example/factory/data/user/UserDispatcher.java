package com.example.factory.data.user;

import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by John on 2017/6/28.
 */

public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    //单线程池:处理卡片一个个的消息处理
    private final Executor executor= Executors.newSingleThreadExecutor();

    public static UserCenter instance(){
        if(instance==null){
            synchronized (UserDispatcher.class){
                if(instance==null){
                    instance=new UserDispatcher();
                }
            }
        }
        return instance;
    }
    @Override
    public void dispatch(UserCard... userCards) {
        if(userCards==null||userCards.length==0){
            return;
        }
        executor.execute(new UserCardHandler(userCards));
    }

    private class UserCardHandler implements Runnable{
        private final UserCard[]cards;
        UserCardHandler(UserCard[]cards){
            this.cards=cards;
        }
        @Override
        public void run() {
            //当被线程调度的时候触发
            List<User>users=new ArrayList<>();
            for (UserCard card : cards) {
                if(card==null|| TextUtils.isEmpty(card.getId())){
                    continue;
                }
                users.add(card.build());
            }
            //进行数据库存储并分发通知
            DbHelper.save(User.class,users.toArray(new User[0]));
        }
    }
}
