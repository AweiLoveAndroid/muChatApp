package com.example.john.muchat.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.common.app.Fragment;
import com.example.common.app.ToolbarActivity;
import com.example.john.muchat.R;
import com.example.john.muchat.fragments.search.SearchGroupFragment;
import com.example.john.muchat.fragments.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {
    public static final String EXTRA_TYPE="EXTRA_TYPE";
    public static final int TYPE_USER=1;
    public static final int TYPE_GROUP=2;

    private SearchFragment searchFragment;
    private int type;//具体显示的类型

    /**
     * 显示搜索界面
     * @param context 上下文
     * @param type 用户还是群
     */
    public static void show(Context context, int type){
        Intent intent=new Intent(context,SearchActivity.class);
        intent.putExtra(EXTRA_TYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type=bundle.getInt(EXTRA_TYPE);
        return type==TYPE_USER||type==TYPE_GROUP;//搜索人或者群
    }

    @Override
    protected void initWidget() {
        //显示对应的fragment
        super.initWidget();
        Fragment fragment;
        if(type==TYPE_USER){
            SearchUserFragment searchUserFragment=new SearchUserFragment();
            fragment=searchUserFragment;
            searchFragment=searchUserFragment;
        }else {
            SearchGroupFragment searchGroupFragment=new SearchGroupFragment();
            fragment=searchGroupFragment;
            searchFragment=searchGroupFragment;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //初始化菜单
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);//找到搜索菜单
        SearchView  searchView= (SearchView) searchItem.getActionView();
        if(searchView!=null){
            // 拿到一个搜索管理器
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            // 添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // 当点击了提交按钮的时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // 当文字改变的时候，咱们不会及时搜索，只在为null的情况下进行搜索
                    if (TextUtils.isEmpty(s)) {
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    //搜索的发起点
    private void search(String query) {
        if(searchFragment==null){
            return;
        }else {
            searchFragment.search(query);
        }
    }

    //搜索的fragment必须继承的接口
    public interface SearchFragment{
        void search(String content);
    }
}
