package com.example.factory.data.helper;


import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.common.Common;
import com.example.common.app.Application;
import com.example.common.utils.PicturesCompressor;
import com.example.common.utils.StreamUtil;
import com.example.factory.Factory;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.model.api.message.MessageCreateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.NetWork;
import com.example.factory.net.RemoteService;
import com.example.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息工具类
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MessageHelper {
    // 从本地找消息
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MessageCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //如果是已经发送过的消息，则不能重新发送
                //如果是文件类型，需要先上传在发送
                //直接发送进行网络调度
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);
                //1上传到云服务器
                //2上传到自己的服务器
                if (card.getType() != Message.TYPE_STR) {
                    //不是文字类型
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {
                        //没有上传到云服务器
                        String content;
                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }
                        if (TextUtils.isEmpty(content)) {
                            //失败
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }
                        //成功则把网络路径进行替换
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);
                        //卡片内容改变了，model也需要更改
                        model.refreshByCard();
                    }
                }
                RemoteService service = NetWork.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && response.isSuccessful()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //是否是账户异常
                            Factory.decodeRspCode(rspModel, null);
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        card.setStatus(Message.TYPE_FILE);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false) // 倒序查询
                .querySingle();
    }

    private static String uploadAudio(String content) {
        // 上传语音
        File file=new File(content);
        if(!file.exists()||file.length()<=0)
            return null;
        //上传并返回
        return UploadHelper.uploadAudio(content);
    }

    private static String uploadPicture(String path) {
        File file = null;
        try {
            file = Glide.with(Factory.app())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            //进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png",cacheDir, SystemClock.uptimeMillis());
            try {
                //压缩工具类
                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile,
                        Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                    String ossPath = UploadHelper.uploadImage(tempFile);//上传
                    StreamUtil.delete(tempFile);//清理缓存
                    return ossPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
