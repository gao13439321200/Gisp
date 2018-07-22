package com.giiisp.giiisp.view.impl;

public interface MyCallBack<T> {
    void onSuccess(String url, T t);

    void onFail(String url, String msg);

}
