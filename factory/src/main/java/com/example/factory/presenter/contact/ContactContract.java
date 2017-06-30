package com.example.factory.presenter.contact;

import com.example.common.factory.presenter.BaseContract;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.User;

import java.util.List;

/**
 * Created by John on 2017/6/26.
 */

public interface ContactContract {
    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter,User>{
        //界面只能刷新界面集合，不能进行单个数据更新
        //void onDone(List<User>users);
        RecyclerAdapter<User>getRecyclerAdapter();
        void onAdapterDataChanged();
    }
}
