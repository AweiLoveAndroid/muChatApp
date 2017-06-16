package com.example.john.muchat.fragments.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.example.common.app.Application;
import com.example.common.widget.PortraitView;
import com.example.factory.Factory;
import com.example.factory.net.UploadHelper;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新信息的碎片
 */
public class UpdateInfoFragment extends com.example.common.app.Fragment {
    @BindView(R.id.portrait)
    PortraitView portraitView;
    public UpdateInfoFragment(){

    }
    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.portrait)
    void onPortraitClick(){
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options=new UCrop.Options();
                //设置图片处理格式为jpeg
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置压缩后的图片精度
                options.setCompressionQuality(96);

                File dPath= Application.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                        .withAspectRatio(1,1)//1:1比例
                        .withMaxResultSize(520,520)//最大尺寸
                        .withOptions(options)//相关参数
                        .start(getActivity());//启动
                //Toast.makeText(getContext(), "测试", Toast.LENGTH_SHORT).show();
            }
        }).show(getChildFragmentManager(),GalleryFragment.class.getName());
        //show的时候使用getChildFragmentManager，tag就是class名字
    }

    //收到从activity传来的回调，然后取出值进行加载
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if(resultUri!=null){
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(portraitView);
        //拿到本地文件地址
        final String localPath=uri.getPath();
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url=UploadHelper.uploadPortrait(localPath);
            }
        });
    }
}
