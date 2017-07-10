package com.example.john.muchat.fragments.panel;

import android.view.View;

import com.example.common.Face.Face;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.john.muchat.R;

import java.util.List;

/**
 * Created by John on 2017/7/9.
 */
public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> beans, AdapterListener<Face.Bean> listener) {
        super(beans, listener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
