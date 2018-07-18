package com.giiisp.giiisp.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {
    /**
     * @param isEnglish true  ：点击英文，把中文设置未选中
     *                  false ：点击中文，把英文设置未选中
     */
    public static void set(Activity activity, boolean isEnglish) {

        Configuration configuration = activity.getResources().getConfiguration();
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        if (isEnglish) {
            //设置英文
            configuration.locale = Locale.ENGLISH;
        } else {
            //设置中文
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }
        //更新配置
        activity.getResources().updateConfiguration(configuration, displayMetrics);
        activity.recreate();
    }
}
