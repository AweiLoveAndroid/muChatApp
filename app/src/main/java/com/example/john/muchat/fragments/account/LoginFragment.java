package com.example.john.muchat.fragments.account;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.EditText;

import com.example.common.app.PresenterFragment;
import com.example.factory.presenter.account.LoginContract;
import com.example.factory.presenter.account.LoginPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter>
implements LoginContract.View{
    @BindView(R.id.edit_phone)
    EditText phone;
    @BindView(R.id.edit_password)
    EditText password;
    @BindView(R.id.loading)
    Loading loading;
    @BindView(R.id.btn_submit)
    Button submit;

    private AccountTrigger accountTrigger;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到activity的引用
        accountTrigger = (AccountTrigger) context;
    }
    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone=this.phone.getText().toString();
        String password=this.password.getText().toString();
        //调用p层进行注册
        presenter.login(phone,password);
    }
    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick(){
        Log.d("click","login");
        accountTrigger.triggerView();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loading.start();//开始loading
        //让控件失去焦点
        this.phone.setEnabled(false);
        this.password.setEnabled(false);
        submit.setEnabled(false);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //需要显示错误的时候触发
        loading.stop();//停止loading
        this.phone.setEnabled(true);
        this.password.setEnabled(true);
        submit.setEnabled(true);
    }
    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    public void loginSuccess() {
        //注册成功跳转到main activity
        MainActivity.show(getContext());
        getActivity().finish();//关闭当前界面
    }
}
