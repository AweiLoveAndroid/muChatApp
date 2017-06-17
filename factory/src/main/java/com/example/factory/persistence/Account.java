package com.example.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * Created by John on 2017/6/16.
 */

public class Account {
    private static String token;
    private static String userId;
    private static String account;

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN="KEY_TOKEN";
    private static final String KEY_USER_ID="KEY_USER_ID";
    private static final String KEY_ACCOUNT="KEY_ACCOUNT";



    private static boolean isBind;

    //存储到xml文件
    private static void save(Context context) {
        //获取数据持久化的SP
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().
                putString(KEY_PUSH_ID, pushId).
                putBoolean(KEY_IS_BIND, isBind).
                putString(KEY_TOKEN,token).
                putString(KEY_USER_ID,userId).
                putString(KEY_ACCOUNT,account)
                .apply();
    }

    //设备的推送ID
    private static String pushId = "test";


    /**
     * 设置存储设备的ID
     *
     * @param pushId
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    /**
     * 进行数据加载
     *
     * @param context
     */
    public static void load(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        pushId = sharedPreferences.getString(KEY_PUSH_ID, "");
        isBind = sharedPreferences.getBoolean(KEY_IS_BIND, false);
        token = sharedPreferences.getString(KEY_TOKEN, "");
        account = sharedPreferences.getString(KEY_ACCOUNT, "");
        userId = sharedPreferences.getString(KEY_USER_ID, "");

    }

    /**
     * 获取推送ID
     *
     * @return
     */
    public static String getPushId() {
        return pushId;
    }


    /**
     * 是否已经绑定到了服务器
     *
     * @return
     */
    public static boolean isBind() {
        return isBind;
    }

    /**
     * 设置绑定状态
     */
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.app());
    }



    /**
     * 返回当前账号是否登陆
     *
     * @return
     */
    public static boolean isLogin() {
        //用户ID和token均不为空
        return !TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(token);
    }

    /**
     * 用户信息是否已经完善
     * @return
     */
    public static boolean isComplete(){
        //TODO
        return isLogin();
    }

    /**
     * 获取当前登录的用户信息
     * @return
     */
    public static User getUser(){
        //如果为null就返回新的user，否则就是从数据库中找
        return TextUtils.isEmpty(userId)?new User():SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    /**
     * 保存我自己的信息到持久化数据中
     *
     * @param model
     */
    public static void login(AccountRspModel model) {
        //存储当前登录用户的基本信息
        Account.token=model.getToken();
        Account.account=model.getAccount();
        Account.userId=model.getUser().getId();
        save(Factory.app());
    }
    //获取token
    public static String getToken(){
        return token;
    }
}
