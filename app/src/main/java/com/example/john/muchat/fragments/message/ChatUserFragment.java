package com.example.john.muchat.fragments.message;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.widget.PortraitView;
import com.example.factory.model.db.User;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.ChatUserPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatUserFragment extends ChatFragment<User>implements ChatContract.UserView {
    @BindView(R.id.portrait)
    PortraitView portraitView;

    private MenuItem userInfoMenuItem;


    public ChatUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_chat_user;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = this.toolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onPortraitClick();
                }
                return false;
            }
        });
        userInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);

    }

    //进行高度运算进行图片变化
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = portraitView;
        MenuItem menuItem = userInfoMenuItem;
        if (view == null || menuItem == null)
            return;
        if (verticalOffset == 0) {
            //完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
            //隐藏menu菜单
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            verticalOffset = Math.abs(verticalOffset);
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                //关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {
                //中间状态
                float process = 1 - verticalOffset / (float) totalScrollRange;
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(process);
                view.setScaleY(process);
                view.setAlpha(process);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255 - (int) (255 * process));
            }
        }
    }

    @OnClick(R.id.portrait)
    void onPortraitClick() {
        PersonalActivity.show(getContext(), receiverId);
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this,receiverId);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Glide.with(this)
                .load(R.drawable.default_banner_chat)
                .centerCrop()
                .into(new ViewTarget<CollapsingToolbarLayout,GlideDrawable>(collapsingToolbarLayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setContentScrim(resource.getCurrent());
                    }
                });
    }

    @Override
    public void onInit(User user) {
        //对聊天对象的信息初始化操作
        portraitView.setup(Glide.with(this),user.getPortrait());
        collapsingToolbarLayout.setTitle(user.getName());

    }
}
