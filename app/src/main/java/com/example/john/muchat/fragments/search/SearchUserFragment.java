package com.example.john.muchat.fragments.search;


import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.contact.FollowContract;
import com.example.factory.presenter.contact.FollowPresenter;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.PersonalActivity;
import com.example.john.muchat.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;
import net.qiujuer.genius.ui.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements
        SearchActivity.SearchFragment,SearchContract.UserView{

    private RecyclerAdapter<UserCard> adapter;

    @BindView(R.id.empty)
    EmptyView emptyView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;


    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void search(String content) {
        presenter.search(content);
    }

    @Override
    protected void initData() {
        super.initData();
        //发起首次搜索
        search("");
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter=new RecyclerAdapter<UserCard>() {
            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View view, int viewType) {
                return new SearchUserFragment.ViewHolder(view);
            }

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search_list;
            }
        });
        emptyView.bind(recyclerView);//初始化占位布局
        setPlaceHolderView(emptyView);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据成功的情况下返回数据
        adapter.replace(userCards);
        //如果有数据就OK，否则就显示空buju
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount()>0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        //初始化presenter
        return new SearchUserPresenter(this);
    }
    //每一个cell的布局操作
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
    implements FollowContract.View{
        @BindView(R.id.portrait)
        PortraitView portraitView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.follow)
        ImageView follow;

        private FollowContract.Presenter presenter;

        public ViewHolder(View itemView) {
            super(itemView);
            //当前view和presenter绑定
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
           /* Glide.with(SearchUserFragment.this)
                    .load(userCard.getPortrait())
                    .centerCrop()
                    .into(portraitView);*/
           portraitView.setup(Glide.with(SearchUserFragment.this),userCard);
            name.setText(userCard.getName());
            follow.setEnabled(!userCard.isFollow());
            Log.d("SearchUserFragment:",userCard.toString());
        }
        @OnClick(R.id.follow)
        void onFollowClick(){
            //发起关注
            presenter.follow(data.getId());
        }

        @OnClick(R.id.portrait)
        void onPortraitClick(){
            //显示信息
            PersonalActivity.show(getContext(),data.getId());
        }

        @Override
        public void showError(@StringRes int str) {
            if(follow.getDrawable() instanceof LoadingDrawable){
                LoadingDrawable drawable= (LoadingDrawable) follow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();//失败就停止动画并且显示一个圆圈
            }
        }

        @Override
        public void showLoading() {
            int minSize= (int) Ui.dipToPx(getResources(),22);
            int maxSize= (int) Ui.dipToPx(getResources(),30);
            LoadingDrawable drawable=new LoadingCircleDrawable(minSize,maxSize);
            drawable.setBackgroundColor(0);
            int color[]=new int[]{UiCompat.getColor(getResources(),R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            follow.setImageDrawable(drawable);
            drawable.start();//启动动画
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            this.presenter=presenter;
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            //更改draw able状态
            if(follow.getDrawable() instanceof LoadingDrawable){
                ((LoadingDrawable) follow.getDrawable()).stop();
                //设置为默认的
                follow.setImageResource(R.drawable.sel_opt_done_add);
            }
            //发起更新
            updateData(userCard);
        }
    }
}
