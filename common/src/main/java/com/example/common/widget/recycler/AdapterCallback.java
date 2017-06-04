package com.example.common.widget.recycler;

/**
 * Created by John on 2017/5/20.
 */

public interface AdapterCallback<Data> {
    void update(Data data,RecyclerAdapter.ViewHolder<Data> holder);
}
