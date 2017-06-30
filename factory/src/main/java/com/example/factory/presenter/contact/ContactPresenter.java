package com.example.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.example.common.factory.data.DataSource;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.user.ContactDataSource;
import com.example.factory.data.user.ContactRepository;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by John on 2017/6/26.
 */

public class ContactPresenter extends BaseSourcePresenter<User,User,ContactDataSource,ContactContract.View>
        implements ContactContract.Presenter,DataSource.SuccessCallback<List<User>> {
    public ContactPresenter(ContactContract.View view) {
        //初始化数据仓库
        super(new ContactRepository(),view);
    }


    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContacts();
    }
    //转换为user
    /*final List<User>users= new ArrayList<>();
                for (UserCard userCard : user) {
        users.add(userCard.build());
    }

    //在事物中进行处理
    DatabaseDefinition definition= FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
        @Override
        public void execute(DatabaseWrapper databaseWrapper) {
            FlowManager.getModelAdapter(User.class).saveAll(users);
        }
    }).build().execute();
    List<User>old=getView().getRecyclerAdapter().getItems();
    diff(old,users);*/



    @Override
    public void onDataLoaded(List<User> users) {
        final ContactContract.View view=getView();
        if(view==null)
            return;
        RecyclerAdapter<User> adapter=view.getRecyclerAdapter();
        List<User>old=adapter.getItems();
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //调用基类方法进行界面刷新
        refreshData(result,users);
    }

}
