package com.example.john.muchat.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**完成对fragment的调度与重用，达到最优fragment切换
 * Created by John on 2017/5/22.
 */

public class Navhelper<T> {
    private final FragmentManager fragmentManager;
    private final int containerID;
    private final SparseArray<Tab<T>>tabs=new SparseArray<>();//所有的tab集合
    private final Context context;
    private final OnTabChangeListener<T> listener;
    private Tab<T> currentTab;

    public Navhelper(FragmentManager fragmentManager, int containerID,
                     Context context, OnTabChangeListener listener) {
        this.fragmentManager = fragmentManager;
        this.containerID = containerID;
        this.context = context;
        this.listener = listener;
    }

    //添加tab
    public Navhelper<T> add(int menuID,Tab<T>tab){
        tabs.put(menuID,tab);
        return this;
    }
    //获取当前显示的tab
    public Tab<T> getCurrentTab(){
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     * @param menuID 菜单的ID
     * @return 能否处理
     */
    public boolean performClickMenu(int menuID){
        Tab<T>tab=tabs.get(menuID);
        if(tab!=null){
            doSelect(tab);
            return true;
        }
        return false;
    }

    private void doSelect(Tab<T>tab){
        Tab<T>oldTab=null;
        if(currentTab!=null){
            oldTab=currentTab;
            //如果当前tab就是即将点击的tab，不做操作（或者做一个刷新操作）
            if(oldTab==tab){
                notifyTabReselect(tab);//这就是那个刷新操作，暂时不做操作
                return;
            }
        }
        //赋值并且调用切换方法
        currentTab=tab;
        doTabChange(currentTab,oldTab);
    }

    private void doTabChange(Tab<T>newTab,Tab<T>oldTab){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        if(oldTab!=null){
            if(oldTab.fragment!=null){
                transaction.detach(oldTab.fragment);//从界面移除但是还在fragment的缓存空间中
            }
        }
        if(newTab!=null){
            if(newTab.fragment==null){
                //首次新建
                Fragment fragment=Fragment.instantiate(context,newTab.aClass.getName());
                //缓存
                newTab.fragment=fragment;
                //提交到fragmentManager
                transaction.add(containerID,fragment,newTab.aClass.getName());
            }else{
                transaction.attach(newTab.fragment);
            }
        }
        transaction.commit();//提交事物
        notifyTabSelect(newTab, oldTab);//通知回掉
    }


    //回掉监听器
    private void notifyTabSelect(Tab<T>newTab,Tab<T>oldTab){
        if(listener!=null){
            listener.onTabChanged(newTab, oldTab);
        }

    }
    private void notifyTabReselect(Tab<T>tab){

    }

    public static class Tab<T>{
        public Class<?> aClass;//fragment对应的class信息
        public T extra;//用户自己设定需要使用的额外字段
        Fragment fragment;

        public Tab(Class<?> aClass, T extra) {
            this.aClass = aClass;
            this.extra = extra;
        }
    }
    //定义事件处理完成后的回掉接口
    public interface OnTabChangeListener<T>{
        void onTabChanged(Tab<T>newTab,Tab<T>oldTab);
    }
}
