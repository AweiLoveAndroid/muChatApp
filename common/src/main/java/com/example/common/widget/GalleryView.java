package com.example.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.common.R;
import com.example.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by John on 2017/5/23.
 */

public class GalleryView extends RecyclerView {
    private static final int MIN_IMAGE_FILE_SIZE=10*1024;//最小的 图片大小
    private static final int MAX_IMAGE_COUNT=3;//最大的选中图片数量
    private static final int LOADER_ID=100;
    private LoaderCallBack loaderCallBack=new LoaderCallBack();
    private List<Image>selectImages=new LinkedList<>();
    private SelectionChangeListener listener;
    private Adapter adapter=new Adapter();
    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        //recyclerView里面使用表格布局，四行
        setLayoutManager(new GridLayoutManager(getContext(),4));
        setAdapter(adapter);
        adapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                //cell点击操作，如果点击是允许的，那么更新cell的状态，然后更新界面
                //如果不允许点击（已经达到最大的选中数量）那么就不刷新界面
                if(onItemSelectClick(image)){
                    holder.updateData(image);
                }
            }
        });
    }
    //初始化方法，返回一个LOADER_ID,可用于销毁LOADER
    public int setup(LoaderManager loaderManager,SelectionChangeListener listener){
        this.listener=listener;
        loaderManager.initLoader(LOADER_ID,null,loaderCallBack);
        return LOADER_ID;
    }

    //得到选中的图片的全部地址,返回一个数组
    public String[] getSelectImagePath(){
        String[] path=new String[selectImages.size()];
        int index=0;
        for(Image image:selectImages){
            path[index++]=image.path;
        }
        return path;
    }

    //可以进行清空选中的图片
    public void clear(){
        for (Image image : selectImages) {
            image.isSelect=false;
        }
        selectImages.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 用于实际的数据加载的Loader Callback
     */
    private class LoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor>{
        private final String[] IMAGE_PROJECTION=new String[]{
                MediaStore.Images.Media._ID,//ID
                MediaStore.Images.Media.DATA,//图片的路径
                MediaStore.Images.Media.DATE_ADDED//图片的创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id==LOADER_ID){
                //先判断是不是需要的ID
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2]+" DESC");
            }
            return null;
        }

        /**
         * 通知adapter数据更改的方法
         * @param images 新的数据
         */

        private void updateSource(List<Image>images){
            adapter.replace(images);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //当loader加载完成时
            List<Image>images=new ArrayList<>();
            //判断是否有数据
            if(data!=null){
                int count=data.getCount();
                if(count>0){
                    data.moveToFirst();//移动游标到开始,然后开始遍历
                    //得到对应列的坐标
                    int indexID=data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath=data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate=data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do{
                        int id=data.getInt(indexID);
                        String path=data.getString(indexPath);
                        long dateTime=data.getLong(indexDate);
                        //判断文件是否存在
                        File file=new File(path);
                        if(!file.exists()||file.length()<MIN_IMAGE_FILE_SIZE){
                            //图片不存在或者图片太小则不做处理
                            continue;
                        }
                        //添加一条新的数据
                        Image image=new Image();
                        image.path=path;
                        image.id=id;
                        image.date=dateTime;
                        images.add(image);
                    }while(data.moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            updateSource(null);//界面清空操作
        }
    }


    //通知选中状态改变
    private void notifySelectChanged(){
        //得到监听者并判断是否有监听者，然后进行回掉数量变化
        SelectionChangeListener selectionChangeListener=listener;
        if(selectionChangeListener!=null){
            selectionChangeListener.onSelectCountChanged(selectImages.size());
        }
    }
    /**
     * cell点击的具体逻辑，
     * @param image
     * @return true代表需要刷新界面，false则代表不需要刷新界面
     */
    private boolean onItemSelectClick(Image image){
        boolean runRefresh;//是否进行刷新
        if(selectImages.contains(image)){
            selectImages.remove(image);//如果之前在，那么现在就移除
            image.isSelect=false;
            runRefresh=true;//状态已经改变，则需要更新。
        }else{
            if(selectImages.size()>=MAX_IMAGE_COUNT){
                String str=getResources().getString(R.string.label_gallery_select_max_size);
                str=String.format(str,MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                runRefresh=false;
            }else{
                selectImages.add(image);
                image.isSelect=true;
                runRefresh=true;
            }
        }
        if(runRefresh)
            notifySelectChanged();
        return true;
    }

    private static class Image{
        int id;//图片id
        String path;//图片路径
        long date;//存储日期
        boolean isSelect;//是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }
    private class Adapter extends RecyclerAdapter<Image>{

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View view, int viewType) {
            return new GalleryView.ViewHolder(view);
        }

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }
    }
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
        private ImageView imageView;
        private View shadow;
        private CheckBox select;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image);
            shadow=itemView.findViewById(R.id.shadow);
            select= (CheckBox) itemView.findViewById(R.id.select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)//按照本地路径加载
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存，直接加载本地图片
                    .centerCrop()//居中剪切
                    .placeholder(R.color.grey_200)//设置默认显示图片
                    .into(imageView);
            shadow.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            select.setChecked(image.isSelect);
        }
    }

    public interface SelectionChangeListener{
        void onSelectCountChanged(int count);
    }

}
