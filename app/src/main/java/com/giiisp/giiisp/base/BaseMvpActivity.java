package com.giiisp.giiisp.base;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.entity.BaseEntity;
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
}
