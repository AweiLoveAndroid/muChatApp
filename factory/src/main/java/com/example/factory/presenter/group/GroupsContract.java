package com.example.factory.presenter.group;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;

/**
 * 群列表
 * Created by John on 2017/7/8.
 */
public interface GroupsContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
