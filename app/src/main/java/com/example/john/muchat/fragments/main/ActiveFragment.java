package com.example.john.muchat.fragments.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.PresenterFragment;
import com.example.common.utils.DateTimeUtil;
import com.example.common.widget.EmptyView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Session;
import com.example.factory.presenter.message.SessionContract;
import com.example.factory.presenter.message.SessionPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.MessageActivity;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends PresenterFragment<SessionContract.Presenter>
        implements SessionContract.View {
    @BindView(R.id.empty)
    EmptyView emptyView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<Session> adapter;


    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                // 返回cell的布局id
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });

        // 点击事件监听
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), session);
            }
        });

        // 初始化占位布局
        emptyView.bind(recyclerView);
        setPlaceHolderView(emptyView);

    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行一次数据加载
        presenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    // 界面数据渲染
    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());
            mContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mTime.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
        }
    }
}
