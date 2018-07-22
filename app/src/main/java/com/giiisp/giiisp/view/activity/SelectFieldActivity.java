package com.giiisp.giiisp.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.entity.APPConstants;
import com.giiisp.giiisp.utils.RxBus;
import com.giiisp.giiisp.view.fragment.selectField.SelectFieldFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SelectFieldActivity extends BaseActivity {
    private static final String TAG = "SelectFieldActivity";
    private static final String TOFRAGMENT = "toFragment";
    public static final int FIELD = 1;
    public static final int WORD = 2;
    public static final int PEOPLE = 3;
    public static final int MAIN = 4;

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
    @BindView(R.id.rg_title)
    RadioGroup mRgTitle;

    public static void intentActivity(Activity context) {
        Bundle bundle = new Bundle();
        Intent sIntent = new Intent(context, SelectFieldActivity.class);
        sIntent.putExtras(bundle);
        context.startActivity(sIntent);
    }

    public static void newRxBus(int toFragment) {
        Map<String, Object> map = new HashMap<>();
        map.put(APPConstants.MyBus.TO, TAG);
        map.put(TOFRAGMENT, toFragment);
        RxBus.getInstance().send(map);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_select_field;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        loadRootFragment(R.id.rl_select_field, SelectFieldFragment.newInstance(1));
        RxBus.getInstance().toObservable(Map.class)
                .subscribe((Map map) -> {
                    if (TAG.equals(map.get(APPConstants.MyBus.TO))) {
                        setColor((int) map.get(TOFRAGMENT));
                        switch ((int) map.get(TOFRAGMENT)) {
                            case FIELD://领域
                                mRgTitle.setBackgroundResource(R.mipmap.guide_tab_left);
                                break;
                            case WORD://关键字
                                mRgTitle.setBackgroundResource(R.mipmap.guide_tab_center);
                                break;
                            case PEOPLE://学者
                                mRgTitle.setBackgroundResource(R.mipmap.guide_tab_right);
                                break;
                            case MAIN:
                                GiiispActivity.actionActivity(this);
                                break;
                            default:
                                break;
                        }

                    }
                });
        mRgTitle.setBackgroundResource(R.mipmap.guide_tab_left);
        setColor(FIELD);
    }

    private void setColor(int toFragment) {
        mRbField.setTextColor(
                getResources().getColor(toFragment == FIELD ? R.color.colorWhite : R.color.blue));
        mRbWord.setTextColor(
                getResources().getColor(toFragment == WORD ? R.color.colorWhite : R.color.blue));
        mRbPeople.setTextColor(
                getResources().getColor(toFragment == PEOPLE ? R.color.colorWhite : R.color.blue));
    }

}
