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
 * 选择关键字
 */
public class SelectWordFragment extends BaseMvpFragment {

    @BindView(R.id.tag_word_system)
    TagFlowLayout mTagWordSystem;
    @BindView(R.id.tag_word_user)
    TagFlowLayout mTagWordUser;

    public static SelectWordFragment newInstance() {

        Bundle args = new Bundle();

        SelectWordFragment fragment = new SelectWordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_select_word;
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

        mTagWordSystem.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagWordSystem, false);
                tv.setText(o);
                return tv;
            }
        });

        mTagWordSystem.setOnSelectListener(selectPosSet -> {
            StringBuffer a = new StringBuffer();
            for (int position : selectPosSet) {
                a.append(txt.get(position));
            }
            ToastUtils.showShort(a);
        });


        mTagWordUser.setAdapter(new TagAdapter<String>(txt) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagWordUser, false);
                tv.setText(o);
                return tv;
            }
        });

        mTagWordUser.setOnSelectListener(selectPosSet -> {
            StringBuffer a = new StringBuffer();
            for (int position : selectPosSet) {
                a.append(txt.get(position));
            }
            ToastUtils.showShort(a);
        });
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        start(SelectPeopleFragment.newInstance());
        SelectFieldActivity.newRxBus(SelectFieldActivity.WORD);
    }
}
