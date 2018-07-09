package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.view.fragment.selectField.SelectFieldFragment;

import butterknife.BindView;

public class SelectFieldActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.rb_field)
    RadioButton mRbField;
    @BindView(R.id.rb_word)
    RadioButton mRbWord;
    @BindView(R.id.rb_people)
    RadioButton mRbPeople;
    @BindView(R.id.img_people)
    ImageView mImgPeople;

    public static void intentActivity(Activity context ) {
       Bundle bundle = new Bundle();
       Intent sIntent = new Intent(context, SelectFieldActivity.class);
       sIntent.putExtras(bundle);
       context.startActivity(sIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_select_field;
    }

    @Override
    public void initView() {
        loadRootFragment(R.id.rl_select_field, SelectFieldFragment.newInstance());
    }

}
