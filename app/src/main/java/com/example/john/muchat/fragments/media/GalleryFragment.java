package com.example.john.muchat.fragments.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.common.tools.UiTool;
import com.example.common.widget.GalleryView;
import com.example.john.muchat.R;

import net.qiujuer.genius.ui.Ui;

/**
 * 图片选择fragment
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectionChangeListener {
    private GalleryView galleryView;
    private OnSelectedListener listener;


    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * 设置事件监听并且返回自己
     *
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //获取gallery view
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryView = (GalleryView) view.findViewById(R.id.galleryView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        galleryView.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectCountChanged(int count) {
        if (count > 0) {
            dismiss();//如果选中图片,那就隐藏
            if (listener != null) {
                //得到选中的图片的路径
                String paths[] = galleryView.getSelectImagePath();
                listener.onSelectedImage(paths[0]);
                listener = null;
            }
        }
    }

    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {
        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null) {
                return;
            }
            //获取屏幕高度
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            //得到状态栏的高度
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}
