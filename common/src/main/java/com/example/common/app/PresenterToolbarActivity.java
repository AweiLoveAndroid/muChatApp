package com.example.common.app;

import com.example.common.factory.presenter.BaseContract;

/**
 * Created by John on 2017/6/24.
 */

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity implements BaseContract.View<Presenter>{
   protected Presenter presenter;

   @Override
   protected void initBefore() {
      super.initBefore();
      initPresenter();
   }

   public void showError(int str){
      if(placeHolderView!=null){
         placeHolderView.triggerError(str);
      }else {
         Application.showToast(str);
      }
   }
   public void showLoading(){
      if(placeHolderView!=null){
         placeHolderView.triggerLoading();
      }
   }
   protected abstract Presenter initPresenter();
   public void setPresenter(Presenter presenter){
      this.presenter=presenter;
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      //界面关闭时销毁
      if(presenter!=null){
         presenter.destroy();
      }
   }

   protected void hideLoading(){
      if(placeHolderView!=null){
         placeHolderView.triggerOk();
      }
   }
}
