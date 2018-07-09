package com.giiisp.giiisp.utils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ToolString {

    //获取控件上的文字（String）
    public static String getString(View view) {
        if (view instanceof EditText)
            return ((EditText) view).getText().toString().trim();
        else if (view instanceof Button)
            return ((Button) view).getText().toString().trim();
        else if (view instanceof TextView)
            return ((TextView) view).getText().toString().trim();
        return "";
    }
}
