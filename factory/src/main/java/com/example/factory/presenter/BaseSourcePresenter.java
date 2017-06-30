package com.example.factory.presenter;

import com.example.common.factory.data.DataSource;
import com.example.common.factory.data.DbDataSource;
import com.example.common.factory.presenter.BaseContract;
import com.example.common.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 基础的仓库源的presenter定义
 * Created by John on 2017/6/30.
 */

public abstract class BaseSourcePresenter<Data, ViewModel, Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SuccessCallback<List<Data>> {

    protected Source source;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.source = source;
    }

    @Override
    public void start() {
        super.start();
        if (source != null)
            this.source.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.source.dispose();
        this.source = null;
    }
}
