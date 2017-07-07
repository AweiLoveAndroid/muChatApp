package com.example.john.muchat.fragments.message;


import android.support.v4.app.Fragment;

import com.example.factory.model.db.Group;
import com.example.factory.presenter.message.ChatContract;
import com.example.john.muchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGroupFragment extends ChatFragment<Group>implements ChatContract.GroupView {


    public ChatGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_chat_group;
    }


    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
