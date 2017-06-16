package com.example.john.muchat.fragments.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.example.common.app.PresenterFragment;
import com.example.common.factory.presenter.BaseContract;
import com.example.factory.presenter.account.RegisterContract;
import com.example.factory.presenter.account.RegisterPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View {
    private AccountTrigger accountTrigger;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loading)
    Loading loading;
    @BindView(R.id.submit)
    Button submit;

    public RegisterFragment() {
        // Required empty public constructor
    }
    @OnClick(R.id.submit)
    void onSubmitClick(){
        String phone=this.phone.getText().toString();
        String name=this.name.getText().toString();
        String password=this.password.getText().toString();
        //调用p层进行注册
        presenter.register(phone,name,password);
    }
    @OnClick(R.id.login)
    void onShowLoginClick(){
        //界面切换,暂时还是别切了
        //accountTrigger.triggerView();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loading.start();//开始loading
        //让控件失去焦点
        this.phone.setEnabled(false);
        this.name.setEnabled(false);
        this.password.setEnabled(false);
        submit.setEnabled(false);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //需要显示错误的时候触发
        loading.stop();//停止loading
        this.phone.setEnabled(true);
        this.name.setEnabled(true);
        this.password.setEnabled(true);
        submit.setEnabled(true);
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到activity的引用
        accountTrigger= (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_register;
    }

    @Override
    public void registerSuccess() {
        //注册成功跳转到main activity
        MainActivity.show(getContext());
        getActivity().finish();//关闭当前界面
    }
}
