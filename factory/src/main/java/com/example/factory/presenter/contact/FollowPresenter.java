package com.example.factory.presenter.contact;

import android.support.annotation.StringRes;

import com.example.common.factory.data.DataSource;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by John on 2017/6/25.
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter,DataSource.Callback<UserCard> {
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id,this);
    }

    @Override
    public void onDataLoaded(final UserCard user) {
        final FollowContract.View view=getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(user);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final FollowContract.View view=getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
