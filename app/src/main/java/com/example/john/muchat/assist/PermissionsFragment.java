package com.example.john.muchat.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.app.Application;
import com.example.common.widget.GalleryView;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {
    //权限回调的标识
    private static final int RC=100;


    public PermissionsFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_permissions, container, false);
        //找到按钮
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();//申请权限
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //界面显示的时候进行刷新
        refreshState(getView());
    }

    //刷新布局中的图片状态
    private void refreshState(View view) {
        if(view==null)
            return;
        Context context=getContext();
        view.findViewById(R.id.permission_network).
                setVisibility(haveNetworkPermission(context)?View.VISIBLE:View.GONE);
        view.findViewById(R.id.permission_read).
                setVisibility(haveReadPermission(context)?View.VISIBLE:View.GONE);
        view.findViewById(R.id.permission_write).
                setVisibility(haveWritePermission(context)?View.VISIBLE:View.GONE);
        view.findViewById(R.id.permission_record).
                setVisibility(haveRecordPermission(context)?View.VISIBLE:View.GONE);
    }

    //准备需要检查的网络权限
    private static boolean haveNetworkPermission(Context context) {
        String permissions[] = new String[]{Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE};
        return EasyPermissions.hasPermissions(context,permissions);
    }
    //准备需要检查的外部存储读取权限
    private static boolean haveReadPermission(Context context) {
        String permissions[] = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(context,permissions);
    }
    //准备需要检查的外部存储写入权限
    private static boolean haveWritePermission(Context context) {
        String permissions[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(context,permissions);
    }
    //准备需要检查的录音权限
    private static boolean haveRecordPermission(Context context) {
        String permissions[] = new String[]{Manifest.permission.RECORD_AUDIO};
        return EasyPermissions.hasPermissions(context,permissions);
    }

    private static void show(FragmentManager manager){
        new PermissionsFragment().show(manager,PermissionsFragment.class.getName());
    }

    public static boolean haveAllPermissions(Context context,FragmentManager manager){
        //检查所有的权限
        boolean haveAll=haveNetworkPermission(context)&&haveReadPermission(context)
                &&haveRecordPermission(context)&&haveWritePermission(context);
        //显示当前申请权限的界面
        if(!haveAll){
            show(manager);
        }
        return haveAll;
    }


    @AfterPermissionGranted(RC)
    private void requestPermissions(){
        String permissions[] = new String[]{Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(getContext(),permissions)){
            Application.showToast(R.string.label_permission_ok);
            //在fragment中可以直接调用getview得到根布局
            refreshState(getView());
        }else{
            EasyPermissions.requestPermissions(this,getString(R.string.title_assist_permissions),
                    RC,permissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //如果有没有申请成功的权限存在则弹出dialog，用户点击进入设置界面自行打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    //权限申请时候回调的方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
