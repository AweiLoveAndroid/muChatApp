package com.example.john.muchat.fragments.main;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.common.widget.GalleryView;
import com.example.john.muchat.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends com.example.common.app.Fragment {
    @BindView(R.id.galleryView)
    GalleryView galleryView;


    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        //动态申请运行时权限
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }else{
            galleryView.setup(getLoaderManager(), new GalleryView.SelectionChangeListener() {
                @Override
                public void onSelectCountChanged(int count) {
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 0:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    galleryView.setup(getLoaderManager(), new GalleryView.SelectionChangeListener() {
                        @Override
                        public void onSelectCountChanged(int count) {
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "你拒绝了权限申请！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
