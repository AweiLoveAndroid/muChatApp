package com.example.common.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 * Created by John on 2017/6/30.
 */

public interface DbDataSource<Data> extends DataSource {
    //基本的数据源加载方法，传递一个callback回掉
    void load(SuccessCallback<List<Data>>callback);
}
