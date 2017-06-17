package com.example.factory;

import android.support.annotation.StringRes;

import com.example.common.app.Application;
import com.example.common.factory.data.DataSource;
import com.example.factory.model.api.account.RspModel;
import com.example.factory.persistence.Account;
import com.example.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by John on 2017/6/3.
 */

public class Factory {
    //单例模式
    private static final Factory instance;
    //全局的线程池
    private final Executor executor;
    private Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        //新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                //设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //设置一个过滤器，数据库级别的model不进行数据转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    //返回全局的application
    public static Application app() {
        return Application.getInstance();
    }

    /**
     * 异步运行的方法
     *
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单例和线程池，异步执行
        instance.executor.execute(runnable);
    }

    public static Gson getGson() {
        //返回一个全局的gson
        return instance.gson;
    }

    /**
     * 进行错误数据的解析,把网络返回的code值进行同意的规划并且返回为一个string 资源
     *
     * @param model
     * @param callback 用于返回一个错误的资源ID
     */
    public static void decodeRspCode(RspModel model, DataSource.FailCallback callback) {
        if (model == null)
            return;

        //进行code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    public static void decodeRspCode(@StringRes int resId, DataSource.FailCallback callback) {
        if (callback != null) {
            callback.onDataNotAvailable(resId);
        }
    }

    /**
     * 收到账户退出的消息需要进行账户退出，重新登陆
     */
    public void logout() {

    }

    /**
     * 处理推送来的消息
     *
     * @param message
     */
    public static void dispatchPush(String message) {
        //TODO
    }

    /**
     * Factory中的初始化
     */
    public static void setup() {
        //数据初始化的时候打开数据库
        FlowManager.init(new FlowConfig.Builder(app()).openDatabasesOnInit(true).build());
        //持久化的数据初始化
        Account.load(app());
    }
}
