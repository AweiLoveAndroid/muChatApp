package com.example.john.muchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.persistence.Account;
import com.igexin.sdk.PushConsts;

/**
 * 个推消息接收器
 * Created by John on 2017/6/17.
 */

public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG=MessageReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null)
            return;
        Bundle bundle=intent.getExtras();
        //判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:
                Log.d(TAG,"getClientId:"+bundle.toString());
                //当ID初始化的时候获取设备ID
                onClientInit(bundle.getString("clientid"));
                break;
            case PushConsts.GET_MSG_DATA:
                //常规的消息送达
                byte[]payload=bundle.getByteArray("payload");
                if(payload!=null){
                    String message=new String(payload);
                    Log.d(TAG,"GET_MSG_DATA:"+message);
                    onMessageArrived(message);
                }
                break;
            default:
                Log.d(TAG,"OTHER:"+bundle.toString());
                break;
        }
    }

    /**
     * 当ID初始化的时候
     * @param cid 设备ID
     */
    private void onClientInit(String cid){
        //设置设备ID
        Account.setPushId(cid);
        if(Account.isLogin()){
            //账户登录状态，进行pushID绑定
            AccountHelper.bindPush(null);
        }

    }

    /**
     * 消息达到时候
     * @param message 新消息
     */
    private void onMessageArrived(String message){
        //factory处理
        Factory.dispatchPush(message);
    }
}
