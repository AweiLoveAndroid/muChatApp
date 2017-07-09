package com.example.common.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.example.common.R;
import com.example.common.factory.presenter.BaseContract;

/**
 * Created by John on 2017/6/24.
 */

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity implements BaseContract.View<Presenter>{
   protected Presenter presenter;
    protected ProgressDialog mLoadingDialog;

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
      }else {
          ProgressDialog dialog = mLoadingDialog;
          if (dialog == null) {
              dialog = new ProgressDialog(this,R.style.AppTheme_Dialog_Alert_Light);
              // 不可触摸取消
              dialog.setCanceledOnTouchOutside(false);
              // 强制取消关闭界面
              dialog.setCancelable(true);
              dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                  @Override
                  public void onCancel(DialogInterface dialog) {
                      finish();
                  }
              });
              mLoadingDialog = dialog;
          }
          dialog.setMessage(getText(R.string.prompt_loading));
          dialog.show();
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
    protected void hideDialogLoading() {
        ProgressDialog dialog = mLoadingDialog;
        if (dialog != null) {
            mLoadingDialog = null;
            dialog.dismiss();
        }
    }

   protected void hideLoading(){
       // 不管你怎么样，我先隐藏我
       hideDialogLoading();

       if (placeHolderView != null) {
           placeHolderView.triggerOk();
       }
   }
}
