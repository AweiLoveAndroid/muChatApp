package com.example.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by John on 2017/6/26.
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    private List<T>oldList,newList;

    public DiffUiDataCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=oldList.get(oldItemPosition);
        T beanNew=newList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=oldList.get(oldItemPosition);
        T beanNew=newList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }
    //比较者
    //泛型的目的是和同类型数据进行比较
    public interface UiDataDiffer<T>{
        boolean isSame(T old);//传递旧的数据进行比较
        boolean isUiContentSame(T old);
    }
}
