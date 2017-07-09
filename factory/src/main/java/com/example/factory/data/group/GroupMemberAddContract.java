package com.example.factory.data.group;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.presenter.group.GroupCreateContract;

/**
 * Created by John on 2017/7/9.
 */

public interface GroupMemberAddContract {
    interface Presenter extends BaseContract.Presenter {
        // 提交成员
        void submit();

        // 更改一个Model的选中状态
        void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected);
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, GroupCreateContract.ViewModel> {
        // 添加群成员成功
        void onAddedSucceed();

        // 获取群的Id
        String getGroupId();
    }
}