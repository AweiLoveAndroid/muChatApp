package com.example.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by John on 2017/5/20.
 */

public abstract class RecyclerAdapter<Data> extends
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements
        View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> dataList;
    private AdapterListener<Data> listener;

    /**
     * 重写三个构造方法，分别为无参的，传data list的，传listener和data list的
     * 无参的构造方法调用传listener的方法，listener默认为空
     * 传listener的方法调用传listener和data list的，data list也默认为空
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        protected Data data;
        Unbinder unbinder;
        private AdapterCallback<Data> callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        //用于绑定数据的触发
        void bind(Data data) {
            this.data = data;
            onBind(data);
        }

        //holder对自己对应的data进行更新操作
        protected abstract void onBind(Data data);

        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }

    protected abstract ViewHolder<Data> onCreateViewHolder(View view, int viewType);

    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    //设置适配器监听
    public void setListener(AdapterListener<Data> adapterListener) {
        this.listener = adapterListener;
    }

    /**
     * 插入一条数据和插入一堆数据
     * 然后通知界面更新
     *
     * @param data
     */
    public void add(Data data) {
        dataList.add(data);
        notifyItemInserted(dataList.size() - 1);
    }

    public void add(Data... datas) {
        if (datas != null && datas.length > 0) {
            int startPos = dataList.size();
            Collections.addAll(dataList, datas);
            notifyItemRangeInserted(startPos, datas.length);
        }
    }

    public void add(Collection<Data> datas) {
        if (datas != null && datas.size() > 0) {
            int startPos = dataList.size();
            dataList.addAll(datas);
            notifyItemRangeInserted(startPos, datas.size());
        }
    }

    //清除操作
    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    //替换操作
    public void replace(Collection<Data> datas) {
        dataList.clear();
        if (datas == null || datas.size() == 0) {
            return;
        }
        dataList.addAll(datas);
        notifyDataSetChanged();
    }



    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, dataList.get(position));
    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);
        ViewHolder<Data> holder = onCreateViewHolder(view, viewType);
        view.setTag(R.id.tag_recycler_holder, holder);//设置view的tag
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        holder.unbinder = ButterKnife.bind(holder, view);//界面注解绑定
        holder.callback = this;//绑定callback
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        Data data = dataList.get(position);//得到需要绑定的数据
        holder.bind(data);//触发holder的绑定方法
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            //根据坐标更新视图
            dataList.remove(pos);
            dataList.add(pos, data);
            //通知坐标下的数据更新
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.listener != null) {
            int pos = viewHolder.getAdapterPosition();//得到view holder当前适配器中的坐标
            this.listener.onItemClick(viewHolder, dataList.get(pos));//回调方法
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.listener != null) {
            int pos = viewHolder.getAdapterPosition();//得到view holder当前适配器中的坐标
            this.listener.onItemLongClick(viewHolder, dataList.get(pos));//回调方法
            return true;
        }
        return false;
    }


    public interface AdapterListener<Data> {
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 对回掉接口做一次实现
     *
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data> {

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }

    public List<Data> getItems(){
        return dataList;
    }
}
