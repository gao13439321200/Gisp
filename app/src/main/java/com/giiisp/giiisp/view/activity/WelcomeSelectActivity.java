package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;

import butterknife.OnClick;

public class WelcomeSelectActivity extends BaseActivity {

    public static void intentActivity(Activity context) {
        Bundle bundle = new Bundle();

        Intent sIntent = new Intent(context, WelcomeSelectActivity.class);
        sIntent.putExtras(bundle);
        context.startActivity(sIntent);
        context.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_welcome_select;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.tv_select, R.id.tv_to_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                SelectFieldActivity.intentActivity(this);
                break;
            case R.id.tv_to_main:
                GiiispActivity.actionActivity(this);
                break;
        }
    }
}
