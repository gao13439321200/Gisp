package com.giiisp.giiisp.base;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.LogInActivity;
import com.giiisp.giiisp.view.impl.BaseImpl;

import static com.giiisp.giiisp.api.UrlConstants.CN;

/**
 * ----------BigGod be here!----------/
 * <p>
 * 类描述： 解决fragment重叠问题
 */

/**
 * Fragment 重叠问题
 * 1>程序崩溃
 * 2>程序切换后台
 * Fragment  show   是否隐藏的状态
 */

public abstract class BaseMvpFragment<V, P extends BasePresenter> extends BaseFragment implements BaseImpl {
    public P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        if (presenter != null && presenter.isViewAttached()) {
            presenter.detachView();
        }
        super.onDestroy();
    }

    protected abstract P initPresenter();

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {

    }

    @Override
    public void onFailNew(String url, String msg) {
        timeout = true;
        Log.i("--->>", "onFailure: " + "P: " + presenter.getClass().getSimpleName() + " : " + msg + "" + msg);
        if (ObjectUtils.isNotEmpty(msg))
            Utils.showToast(msg);
    }

    public void onFailure(String msg, Exception ex) {
        timeout = true;
        Log.i("--->>", "onFailure: " + "P: " + presenter.getClass().getSimpleName() + " : " + msg + "" + ex.toString());
        if (isShow)
            Utils.showToast(msg);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i("--->>", "onHiddenChanged: " + hidden);
        if (timeout)
            initNetwork();
        if (!hidden) {
            isShow = true;
        }
        super.onHiddenChanged(hidden);

    }

    public boolean isChinese() {
        return CN.equals(SPUtils.getInstance().getString(UrlConstants.LANGUAGE, CN));
    }

    public String getLanguage() {
        return SPUtils.getInstance().getString(UrlConstants.LANGUAGE, CN);
    }

    public String getUserID() {
        return SPUtils.getInstance().getString(UrlConstants.UID, "");
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
        if (getContext() == null)
            return;
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getContext());
        normalDialog.setIcon(null);
        normalDialog.setTitle(R.string.no_login);
        normalDialog.setPositiveButton(R.string.confirm,
                (dialog, which) -> {
                    SPUtils.getInstance().put(UrlConstants.UID, "");
                    SPUtils.getInstance().put(UrlConstants.UNAME, "");
                    LogInActivity.actionActivity(getContext());
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("id", uid);
//                    presenter.getDataAll("105", map);
                    getActivity().finish();
                });
        normalDialog.setNegativeButton(R.string.cancel, null);
        // 显示
        normalDialog.show();
    }

}
