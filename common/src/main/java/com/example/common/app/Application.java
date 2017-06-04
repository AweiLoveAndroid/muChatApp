package com.example.common.app;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

/**
 * Created by John on 2017/6/1.
 */

public class Application extends android.app.Application {
    private static Application instance;
    //private static android.app.Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    //外部获取单例
    public static Application getInstance(){
        return instance;
    }
    /**
     * 获取缓存文件夹地址
     * @return //临时文件
     */
    public static File getCacheDirFile(){
        return instance.getCacheDir();
    }

    public static File getPortraitTmpFile(){
        //得到头像目录的缓存地址
        File dir=new File(getCacheDirFile(),"portrait");
        //创建所有的对应文件夹
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        //删除旧的一些缓存文件
        File files[]=dir.listFiles();
        if(files!=null&&files.length>0){
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
        File path=new File(dir, SystemClock.uptimeMillis()+".jpg");
        return path.getAbsoluteFile();
    }

    /**
     * 获取音频文件本地地址
     * @param isTmp 是否是缓存文件
     * @return
     */
    public static File getAudioTmpFile(boolean isTmp){
        File dir=new File(getCacheDirFile(),"audio");
        dir.mkdirs();
        File files[]=dir.listFiles();
        if(files!=null&&files.length>0){
            for (File file : files) {
                file.delete();
            }
        }
        File path=new File(getCacheDirFile(),isTmp?"tmp.mp3":SystemClock.uptimeMillis()+"tmp.mp3");
        return path.getAbsoluteFile();
    }

    public  static void showToast(final String msg){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //msgId传递的是字符串资源
    public static void showToast(@StringRes int msgId){
        showToast(instance.getString(msgId));
    }
}
