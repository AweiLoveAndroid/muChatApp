package com.example.factory.data.helper;

import com.example.common.factory.data.DataSource;
import com.example.common.utils.CollectionUtil;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by John on 2017/6/19.
 */

public class UserHelper {
    //更新用户信息
    public static void update(UserUpdateModel model, final DataSource.Callback callback) {
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        //网络请求
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel=response.body();
                if(rspModel.success()){
                    UserCard userCard =rspModel.getResult();
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
                    callback.onDataLoaded(userCard);
                }else {
                    //错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
    //刷新联系人
    public static void refreshContacts() {
        RemoteService service = NetWork.remote();
        //得到一个call
        service.userContacts().enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel=response.body();
                if(rspModel.success()){
                    List<UserCard>cards=rspModel.getResult();
                    if(cards==null||cards.size()==0)
                        return;
                    Factory.getUserCenter().dispatch(CollectionUtil.toArray(cards,UserCard.class));
                }else {
                    Factory.decodeRspCode(rspModel,null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {

            }
        });
    }

    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel=response.body();
                if(rspModel.success()){
                    callback.onDataLoaded(rspModel.getResult());
                }else {
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;//把当前的调度者返回
    }


    /**
     * 关注的网络请求
     * @param id
     * @param callback
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        //得到一个call
        final Call<RspModel<UserCard>> call = service.userFollow(id);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel=response.body();
                if(rspModel.success()){
                    UserCard userCard=rspModel.getResult();
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);

                    //TODO 通知联系人列表刷新
                    callback.onDataLoaded(userCard);
                }else {
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
    //从本地查询用户信息
    public static User findFromLocal(String id){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    //从网络查询用户信息
    public static User findFromNet(String id){
        RemoteService remoteService=NetWork.remote();
        try {
            Response<RspModel<UserCard>> response= remoteService.userFind(id).execute();
            UserCard userCard=response.body().getResult();
            if(userCard!=null){
                //TODO 数据库的刷新但是没有通知
                User user=userCard.build();
                Factory.getUserCenter().dispatch(userCard);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索一个用户，优先本地缓存然后再从网络拉取
     * @param id
     * @return
     */
    public static User search(String id){
        User user=findFromLocal(id);
        if(user==null){
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络
     * @param id
     * @return
     */
    public static User searchFirstOfNet(String id){
        User user=findFromNet(id);
        if(user==null){
            return findFromLocal(id);
        }
        return user;
    }
}
