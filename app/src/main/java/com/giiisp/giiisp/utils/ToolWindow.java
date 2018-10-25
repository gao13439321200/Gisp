package com.giiisp.giiisp.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by Gao on 2017/3/21.
 */

public class ToolWindow {
    /**
     * 设置背景透明度
     *
     * @param bgAlpha 0.0-1.0
     */
    public static void backgroundAlpha(Activity mContext, float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }
}
