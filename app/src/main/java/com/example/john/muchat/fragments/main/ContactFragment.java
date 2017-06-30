package com.example.john.muchat.fragments.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.presenter.contact.ContactContract;
import com.example.factory.presenter.contact.ContactPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.MessageActivity;
import com.example.john.muchat.activities.PersonalActivity;
import com.example.john.muchat.fragments.search.SearchUserFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends PresenterFragment<ContactContract.Presenter> implements ContactContract.View{
    @BindView(R.id.empty)
    EmptyView emptyView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    //适配器user可以直接从数据库查询
    private RecyclerAdapter<User> adapter;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(final View root) {
        super.initWidget(root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter=new RecyclerAdapter<User>() {
            @Override
            protected ViewHolder<User> onCreateViewHolder(View view, int viewType) {
                return new ContactFragment.ViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, User userCard) {
                return R.layout.cell_contact_list;
            }
        });
        //事件监听
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                MessageActivity.show(getContext(),user);//跳转到聊天界面
            }
        });
        emptyView.bind(recyclerView);//初始化占位布局
        setPlaceHolderView(emptyView);
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        return new ContactPresenter(this);//初始化presenter
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        presenter.start();//进行数据加载
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount()>0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User>{
        @BindView(R.id.im_portrait)
        PortraitView portraitView;
        @BindView(R.id.txt_name)
        TextView name;
        @BindView(R.id.txt_desc)
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            portraitView.setup(Glide.with(ContactFragment.this),user);
            name.setText(user.getName());
            description.setText(user.getDescription());
        }
        @OnClick(R.id.im_portrait)
        void OnPortraitClick(){
            PersonalActivity.show(getContext(),data.getId());
        }
    }
}
