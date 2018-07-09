package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.base.BasePresenter;
import com.giiisp.giiisp.view.activity.SelectFieldActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择学者
 */
public class SelectPeopleFragment extends BaseMvpFragment {

    @BindView(R.id.tag_subject)
    TagFlowLayout mTagSubject;
    @BindView(R.id.tag_major)
    TagFlowLayout mTagMajor;
    @BindView(R.id.tag_people_system)
    TagFlowLayout mTagPeopleSystem;
    @BindView(R.id.tag_people_user)
    TagFlowLayout mTagPeopleUser;

    public static SelectPeopleFragment newInstance() {

        Bundle args = new Bundle();

        SelectPeopleFragment fragment = new SelectPeopleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_select_people;
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
        //学科
        mTagSubject.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_subject_layout,
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


        //专业
        mTagMajor.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_subject_layout,
                                mTagMajor, false);
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

        //推荐的学者
        mTagPeopleSystem.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_layout,
                                mTagPeopleSystem, false);
                tv.setText(o);
                return tv;
            }
        });


        mTagPeopleSystem.setOnSelectListener(selectPosSet -> {
            StringBuffer a = new StringBuffer();
            for (int position : selectPosSet) {
                a.append(txt.get(position));
            }
            ToastUtils.showShort(a);
        });

        //关注的学者
        mTagPeopleUser.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_layout,
                                mTagPeopleUser, false);
                tv.setText(o);
                return tv;
            }
        });


        mTagPeopleUser.setOnSelectListener(selectPosSet -> {
            StringBuffer a = new StringBuffer();
            for (int position : selectPosSet) {
                a.append(txt.get(position));
            }
            ToastUtils.showShort(a);
        });
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        //进入首页
        SelectFieldActivity.newRxBus(SelectFieldActivity.MAIN);
    }
}
