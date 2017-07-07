package com.example.john.muchat.fragments.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.PresenterFragment;
import com.example.common.utils.DisplayUtil;
import com.example.common.widget.PortraitView;
import com.example.common.widget.adapter.TextWatcherAdapter;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.message.ChatContract;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.MessageActivity;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by John on 2017/7/2.
 */

public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel> {
    protected Adapter adapter;
    protected String receiverId;
    @BindView(R.id.CollapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.submit)
    View submit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        receiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initToolbar();
        initAppbar();
        initEditContent();
        adapter = new Adapter();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter.start();
    }

    protected void initToolbar() {
        Toolbar toolbar = this.toolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    //界面的appBar设置监听，得到关闭与打开时的进度
    protected void initAppbar() {
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    private void initEditContent() {
        content.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMessage = !TextUtils.isEmpty(content);
                submit.setActivated(needSendMessage);
            }
        });
    }

    @OnClick(R.id.face)
    void onFaceClick() {

    }

    @OnClick(R.id.record)
    void onRecordClick() {

    }

    @OnClick(R.id.submit)
    void onSubmitClick() {
        if (submit.isActivated()) {
            String content = this.content.getText().toString().trim();
            this.content.setText("");
            presenter.pushText(content);
            recyclerView.scrollBy(0, DisplayUtil.dp2px(getContext(),60));
        } else {
            onMoreClick();
        }
    }


    private void onMoreClick() {
        //TODO
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View view, int viewType) {
            switch (viewType) {
                //左右相同
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(view);
                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(view);
                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(view);
                default:
                    return new TextHolder(view);
            }
        }

        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()) {
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }
    }

    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.portrait)
        PortraitView portraitView;
        @Nullable
        @BindView(R.id.loading)
        Loading loading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            sender.load();//进行数据加载
            portraitView.setup(Glide.with(ChatFragment.this), sender);//头像加载
            if (loading != null) {
                //右边布局
                int status = message.getStatus();
                if (status == message.STATUS_DONE) {
                    loading.stop();
                    loading.setVisibility(View.GONE);
                } else if (status == message.STATUS_CREATED) {
                    loading.setVisibility(View.VISIBLE);
                    loading.setProgress(0);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    loading.start();
                } else if (status == message.STATUS_FAILED) {
                    loading.setVisibility(View.VISIBLE);
                    loading.stop();
                    loading.setProgress(1);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }
                portraitView.setEnabled(status == message.STATUS_FAILED);
            } else {
                //左边布局
            }
        }


        @OnClick(R.id.portrait)
        void onRePushClick() {
            //重新发送
            if (loading != null && presenter.rePush(data)) {
                //状态改变需要刷新界面信息
                updateData(data);
            }
        }
    }

    class TextHolder extends BaseHolder {
        @BindView(R.id.content)
        TextView content;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            this.content.setText(message.getContent());
            //TODO
        }
    }

    class AudioHolder extends BaseHolder {
        @BindView(R.id.content)
        TextView content;

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }

    class PicHolder extends BaseHolder {
        //TODO
        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }
}
