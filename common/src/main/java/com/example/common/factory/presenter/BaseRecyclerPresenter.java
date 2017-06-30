package com.example.common.factory.presenter;

import android.support.v7.util.DiffUtil;

import com.example.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * 对recyclerView进行简单的presenter封装
 * Created by John on 2017/6/29.
 */

public class BaseRecyclerPresenter<ViewMode,View extends BaseContract.RecyclerView>
        extends BasePresenter<View> {
    public BaseRecyclerPresenter(View view) {
        super(view);
    }
    protected void refreshData(final List<ViewMode>dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view=getView();
                if(view==null)
                    return;
                //更新数据并且刷新界面
                RecyclerAdapter<ViewMode>adapter=view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    /**
     * 刷新界面操作
     * @param diffResult 结果集
     * @param dataList 新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult,final List<ViewMode>dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //主线程运行
                refreshDataOnUiThread(diffResult,dataList);
            }
        });
    }

    protected void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult,final List<ViewMode>dataList){
        View view=getView();
        if(view==null){
            return;
        }
        RecyclerAdapter<ViewMode>adapter=view.getRecyclerAdapter();
        //改变界面集合并且不通知界面刷新
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        view.onAdapterDataChanged();//通知界面刷新占位布局
        diffResult.dispatchUpdatesTo(adapter);//进行增量更新

    }
}
