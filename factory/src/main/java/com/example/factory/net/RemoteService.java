package com.example.factory.net;

import com.example.factory.model.api.RegisterModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.model.api.group.GroupCreateModel;
import com.example.factory.model.api.group.GroupMemberAddModel;
import com.example.factory.model.api.message.MessageCreateModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的接口
 * Created by John on 2017/6/11.
 */

public interface RemoteService {
    /**
     * 网络请求的一个注册接口
     *
     * @param model 传入的register model
     * @return 返回的是Rsp model
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    //登陆
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备ID
     * @param pushID 设备ID
     * @return
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true,value = "pushId") String pushID);

    //用户更新的接口
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    //用户搜索的接口
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name")String name);

    //用户关注的接口
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId")String userId);

    //获取联系人列表
    @GET("user/contact")
    Call<RspModel<List<UserCard>>>userContacts();

    //查询用户信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId")String userId);

    @POST("msg")
    Call<RspModel<MessageCard>>msgPush(@Body MessageCreateModel model);
    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    // 拉取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    // 群搜索的接口
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    // 我的群的成员列表
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 给群添加成员
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);
}
