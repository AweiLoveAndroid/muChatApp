package com.example.factory.data;

import android.support.annotation.NonNull;

import com.example.common.factory.data.DbDataSource;
import com.example.common.utils.CollectionUtil;
import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.db.BaseDbModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础的数据库仓库
 * Created by John on 2017/6/30.
 */

public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>
,DbHelper.ChangedListener<Data>,QueryTransaction.QueryResultListCallback<Data>{
    protected LinkedList<Data>dataList=new LinkedList<>();//当前缓存的数据
    private SuccessCallback<List<Data>>callback;
    private Class<Data>dataClass;//当前泛型对应的真实class信息

    @SuppressWarnings("unchecked")
    public BaseDbRepository() {
        // 拿当前类的范型数组信息
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());
        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SuccessCallback<List<Data>> callback) {
        this.callback=callback;
        //进行数据库监听
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        //取消监听，销毁数据
        this.callback=null;
        DbHelper.removeChangedListener(dataClass,this);
        dataList.clear();
    }

    //数据库统一通知的地方
    @Override
    public void onDataSave(Data[] list) {
        boolean isChanged = false;
        // 当数据库数据变更的操作
        for (Data data : list) {
            // 是关注的人，同时不是我自己
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        // 有数据变更，则进行界面刷新
        if (isChanged)
            notifyDataChange();
    }

    @Override
    public void onDataDelete(Data[] list) {
        // 在删除情况下不用进行过滤判断
        // 但数据库数据删除的操作
        boolean isChanged = false;
        for (Data data : list) {
            if (dataList.remove(data))
                isChanged = true;
        }

        // 有数据变更，则进行界面刷新
        if (isChanged)
            notifyDataChange();
    }
    // 数据库统一通知的地方：增加／更改

    protected abstract boolean isRequired(Data data);


    //DbFlow框架通知的回调
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if(tResult.size()==0){
            dataList.clear();
            notifyDataChange();
            return;
        }
        Data[]users= CollectionUtil.toArray(tResult,dataClass);
        onDataSave(users);
    }

    private void notifyDataChange(){
        SuccessCallback<List<Data>>callback=this.callback;
        if(callback!=null)
            callback.onDataLoaded(dataList);
    }
    // 插入或者更新
    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }
    // 更新操作，更新某个坐标下的数据
    protected void replace(int index, Data data) {
        dataList.remove(index);
        dataList.add(index, data);
    }

    // 添加方法
    protected void insert(Data data) {
        dataList.add(data);
    }


    // 查询一个数据是否在当前的缓存数据中，如果在则返回坐标
    protected int indexOf(Data newData) {
        int index = -1;
        for (Data data : dataList) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }
    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener() {
        DbHelper.addChangedListener(dataClass, this);
    }
}
