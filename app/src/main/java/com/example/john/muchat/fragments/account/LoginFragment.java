package com.example.john.muchat.fragments.account;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.john.muchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends com.example.common.app.Fragment {

    private AccountTrigger accountTrigger;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到activity的引用
        accountTrigger = (AccountTrigger) context;
    }

   /* @Override
    public void onResume() {
        super.onResume();
        //进行一次切换
        accountTrigger.triggerView();
    }*/

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_login;
    }

}
