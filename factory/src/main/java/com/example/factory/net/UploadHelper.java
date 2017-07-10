package com.example.factory.net;

import android.text.format.DateFormat;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.common.utils.HashUtil;
import com.example.factory.Factory;

import java.io.File;
import java.util.Date;

/**
 * Created by John on 2017/6/3.
 */

public class UploadHelper {
    //存储区域
    public static final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
    private static final String BUCKET_NAME="muchat2017";

    private static OSS getClient() {

    // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider
                ("LTAIq8r1Xk4P0J6m", "nkqdmy8KcdrERQNvZgzRftw95l3ldz");

        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }


    //上传方法
    private static String upload(String objkey,String path){
        //构造上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objkey, path);
        try{
            //初始化上传的client
            OSS client=getClient();
            //开始上传
            PutObjectResult result=client.putObject(request);
            //得到一个外网可访问的地址
            String url=client.presignPublicObjectURL(BUCKET_NAME,objkey);
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传图片
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path){
        String key=getImageObjKey(path);
        return upload(key,path);
    }
    public static String uploadPortrait(String path){
        String key=getPortraitObjKey(path);
        return upload(key,path);
    }
    public static String uploadAudio(String path){
        String key=getAudioObjKey(path);
        return upload(key,path);
    }

    //分月存储
    private static String getDataString(){
        return DateFormat.format("yyyyMM",new Date()).toString();
    }
    private static String getImageObjKey(String path){
        String fileMd5= HashUtil.getMD5String(new File(path));
        String dateString=getDataString();
        return String.format("image/%s/%s.jpg",dateString,fileMd5);
    }
    private static String getPortraitObjKey(String path){
        String fileMd5= HashUtil.getMD5String(new File(path));
        String dateString=getDataString();
        return String.format("portrait/%s/%s.jpg",dateString,fileMd5);
    }
    private static String getAudioObjKey(String path){
        String fileMd5= HashUtil.getMD5String(new File(path));
        String dateString=getDataString();
        return String.format("audio/%s/%s.mp3",dateString,fileMd5);
    }
}
