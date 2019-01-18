package com.giiisp.giiisp.base;

import android.app.AlertDialog;
import android.os.Bundle;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.view.activity.LogInActivity;
import com.giiisp.giiisp.view.impl.BaseImpl;

public abstract class BaseMvpActivity<V, T extends BasePresenter> extends BaseActivity implements BaseImpl {
    public T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = initPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null && presenter.isViewAttached()) {
            presenter.detachView();
        }
        super.onDestroy();
    }

    protected abstract T initPresenter();


    @Override
    public void onSuccessNew(String url, BaseBean baseEntity) {

    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onFailure(String msg, Exception ex) {

    }

    @Override
    public void onFailNew(String url, String msg) {
        ToastUtils.showShort(msg);
    }

    public boolean isLoginIn() {
        return ObjectUtils.isNotEmpty(getUserID());
    }

    public void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题 Attempt to invoke virtual method 'android.content.res.Resources$Theme' on a null object reference
	at android.app.AlertDialog.resolveDialogTheme(AlertDialog.java:225)
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(null);
        normalDialog.setTitle(R.string.no_login);
        normalDialog.setPositiveButton(R.string.confirm,
                (dialog, which) -> {
                    SPUtils.getInstance().put(UrlConstants.UID, "");
                    SPUtils.getInstance().put(UrlConstants.UNAME, "");
                    LogInActivity.actionActivity(this);
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("id", uid);
//                    presenter.getDataAll("105", map);
                    finish();
                });
        normalDialog.setNegativeButton(R.string.cancel, null);
        // 显示
        normalDialog.show();
    }
}
