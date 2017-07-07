package com.example.factory.presenter.message;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.Session;

/**
 * Created by John on 2017/7/6.
 */

public interface SessionContract {
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}
