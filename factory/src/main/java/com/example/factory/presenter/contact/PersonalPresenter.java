package com.example.factory.presenter.contact;

import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.Factory;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by John on 2017/6/27.
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {
    private User user;
    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view=getView();
                if(view!=null){
                    String id=getView().getUserId();
                    User user= UserHelper.searchFirstOfNet(id);
                    onLoaded(view,user);
                }
            }
        });

    }

    public void onLoaded(final PersonalContract.View view, final User user){
        this.user=user;
        boolean isSelf=user.getId().equalsIgnoreCase(Account.getUserId());
        final boolean isFollow=isSelf||user.isFollow();//是否已经关注
        final boolean allowSayHello=isFollow&&!isSelf;
        //切换到UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                PersonalContract.View view=getView();
                if(view==null)
                    return;
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
