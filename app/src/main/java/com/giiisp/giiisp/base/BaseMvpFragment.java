package com.giiisp.giiisp.base;

import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.impl.BaseImpl;


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
        return "1".equals(SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
    }

    public String getLanguage() {
        return SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1");
    }

    public String getUserID() {
        return SPUtils.getInstance().getString(UrlConstants.UID, "");
    }

}
