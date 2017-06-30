package com.example.factory.presenter.search;

import com.example.common.factory.presenter.BasePresenter;

/**
 * 搜索群的实现
 * Created by John on 2017/6/24.
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter {
    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
