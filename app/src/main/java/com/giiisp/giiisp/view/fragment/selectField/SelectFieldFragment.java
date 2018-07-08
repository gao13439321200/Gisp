package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.base.BasePresenter;
import com.giiisp.giiisp.widget.CustomSpinner;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择领域
 */
public class SelectFieldFragment extends BaseMvpFragment {

    @BindView(R.id.ll_subject)
    LinearLayout mLlSubject;
    @BindView(R.id.tag_subject)
    TagFlowLayout mTagSubject;
    @BindView(R.id.ll_major)
    LinearLayout mLlMajor;
    @BindView(R.id.tag_major)
    TagFlowLayout mTagMajor;

    public static SelectFieldFragment newInstance() {

        Bundle args = new Bundle();

        SelectFieldFragment fragment = new SelectFieldFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_select_field;
    }

    @Override
    public void initView() {
        List<String> txt = new ArrayList<>();
        txt.add("哈哈");
        txt.add("呵呵");
        txt.add("嘿嘿哈哈哈哈哈哈哈");
        txt.add("哈哈");
        txt.add("呵呵");
        txt.add("嘿嘿哈哈哈哈哈哈哈");
        CustomSpinner mSpinnerSubject = new CustomSpinner(getActivity(), "请选择", txt);
        mSpinnerSubject.setOnCustomItemCheckedListener(position -> {

        });
        mLlSubject.addView(mSpinnerSubject);

        CustomSpinner mSpinnerMajor = new CustomSpinner(getActivity(), "请选择", txt);
        mSpinnerMajor.setOnCustomItemCheckedListener(position -> {

        });
        mLlMajor.addView(mSpinnerMajor);

        mTagSubject.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagSubject, false);
                tv.setText(o);
                return tv;
            }
        });

        mTagSubject.setOnSelectListener(selectPosSet -> {
            StringBuffer a = new StringBuffer();
            for (int position : selectPosSet) {
                a.append(txt.get(position));
            }
            ToastUtils.showShort(a);
        });


        mTagMajor.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagSubject, false);
                tv.setText(o);
                return tv;
            }
        });

        mTagMajor.setOnSelectListener(selectPosSet -> {
            StringBuffer a = new StringBuffer();
            for (int position : selectPosSet) {
                a.append(txt.get(position));
            }
            ToastUtils.showShort(a);
        });
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        start(SelectWordFragment.newInstance());
    }
}
