package com.giiisp.giiisp.view.impl;

import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.entity.BaseEntity;

/**
 *
 * Created by Thinkpad on 2017/4/28.
 */


public interface BaseImpl {
    void onSuccess(BaseEntity entity);
    void onFailure(String msg, Exception ex);
    void onSuccessNew(String url, BaseBean baseBean);

    void onFailNew(String url, String msg);
}
