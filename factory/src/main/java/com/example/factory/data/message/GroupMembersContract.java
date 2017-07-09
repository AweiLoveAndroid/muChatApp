package com.example.factory.data.message;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.view.MemberUserModel;

/**
 * Created by John on 2017/7/9.
 */

public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();
    }
}
