package com.example.common;

/**
 * Created by John on 2017/5/19.
 */

public class Common {
    //一些长久参数
    public interface Constance{
        //手机号的正则表达式,11位手机号
        String REGEX_MOBILE="[1][3,4,5,7,8][0-9]{9}$";
        //基础的网络请求地址
        String API_URL="http://192.168.1.103:8080/api/";
    }
}
