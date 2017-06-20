package com.example.john.muchat.fragments.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.common.app.Application;
import com.example.common.app.PresenterFragment;
import com.example.common.widget.PortraitView;
import com.example.factory.Factory;
import com.example.factory.net.UploadHelper;
import com.example.factory.presenter.user.UpdateInfoContract;
import com.example.factory.presenter.user.UpdateInfoPresenter;
import com.example.john.muchat.R;
import com.example.john.muchat.activities.MainActivity;
import com.example.john.muchat.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新信息的碎片
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {
    @BindView(R.id.sex)
    ImageView sex;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.loading)
    Loading loading;
    @BindView(R.id.btn_submit)
    Button submit;
    @BindView(R.id.portrait)
    PortraitView portraitView;

    private String portraitPath;//头像路径
    private boolean isMan = true;

    public UpdateInfoFragment() {

    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = description.getText().toString();
        Log.d("UpdateInfoFragment:", "desc:" + desc);
        presenter.update(portraitPath, desc, isMan);
    }

    @OnClick(R.id.portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                //设置图片处理格式为jpeg
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置压缩后的图片精度
                options.setCompressionQuality(96);
                File dPath = Application.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1, 1)//1:1比例
                        .withMaxResultSize(520, 520)//最大尺寸
                        .withOptions(options)//相关参数
                        .start(getActivity());//启动
                //Toast.makeText(getContext(), "测试", Toast.LENGTH_SHORT).show();
            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
        //show的时候使用getChildFragmentManager，tag就是class名字
    }

    //收到从activity传来的回调，然后取出值进行加载
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
            //final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {
        portraitPath = uri.getPath();//得到头像地址
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(portraitView);
    }

    @Override
    public void updateSucceed() {
        //注册成功跳转到main activity
        Log.d("UpdateInfoFragment:", "succeed");
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        //初始化presenteru
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loading.start();//开始loading
        Log.d("UpdateInfoFragment", "showLoading");
        //让控件失去焦点
        this.portraitView.setEnabled(false);
        this.sex.setEnabled(false);
        this.description.setEnabled(false);
        submit.setEnabled(false);
    }

    @OnClick(R.id.sex)
    public void onSexClick() {
        //性别图片点击的时候触发
        isMan = !isMan;
        Drawable drawable = ContextCompat.getDrawable(getContext(), isMan ?
                R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        sex.setImageDrawable(drawable);
        sex.getBackground().setLevel(isMan ? 0 : 1);//设置背景的层级切换颜色
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //需要显示错误的时候触发
        Log.d("UpdateInfoFragment", "showError");
        loading.stop();//停止loading
        this.description.setEnabled(true);
        this.portraitView.setEnabled(true);
        this.sex.setEnabled(true);
        submit.setEnabled(true);
    }
}
