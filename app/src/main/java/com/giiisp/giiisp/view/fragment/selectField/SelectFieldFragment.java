package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.SubjectBean;
import com.giiisp.giiisp.dto.SubjectVO;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.view.activity.SelectFieldActivity;
import com.giiisp.giiisp.view.impl.MyCallBack;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择领域
 */
public class SelectFieldFragment extends BaseMvpFragment<MyCallBack, WholePresenter> {

    public static final String TYPE = "type";
    //    @BindView(R.id.ll_subject)
//    LinearLayout mLlSubject;
    @BindView(R.id.et_subject)
    EditText mEtSubject;
    @BindView(R.id.tag_subject)
    TagFlowLayout mTagSubject;
    //    @BindView(R.id.ll_major)
//    LinearLayout mLlMajor;
    @BindView(R.id.tag_major)
    TagFlowLayout mTagMajor;
    @BindView(R.id.btn_next)
    Button mButton;
    private List<SubjectVO> mSubjectVOList = new ArrayList<>();
    private List<MajorVO> mMajorVOList = new ArrayList<>();
    private TagAdapter mSubjectAdapter;
    private TagAdapter mMajorAdapter;
    private String pid = "";
    private String cid = "";
    private String etText = "";

    public static SelectFieldFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        SelectFieldFragment fragment = new SelectFieldFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_select_field;
    }

    @Override
    public void initView() {
        int type = getArguments().getInt(TYPE);
        switch (type) {
            case 1://未选择
                mButton.setVisibility(View.VISIBLE);
                break;
            case 2://已选择
                mButton.setVisibility(View.GONE);
                break;
        }


        List<String> txt = new ArrayList<>();
        txt.add("哈哈");
        txt.add("呵呵");
        txt.add("嘿嘿哈哈哈哈哈哈哈");
        txt.add("哈哈");
        txt.add("呵呵");
        txt.add("嘿嘿哈哈哈哈哈哈哈");
//        CustomSpinner mSpinnerSubject = new CustomSpinner(getActivity(), "请选择", txt);
//        mSpinnerSubject.setOnCustomItemCheckedListener(position -> {

//        });
//        mLlSubject.addView(mSpinnerSubject);

//        CustomSpinner mSpinnerMajor = new CustomSpinner(getActivity(), "请选择", txt);
//        mSpinnerMajor.setOnCustomItemCheckedListener(position -> {
//
//        });
//        mLlMajor.addView(mSpinnerMajor);
        mSubjectAdapter = new TagAdapter<SubjectVO>(mSubjectVOList) {
            @Override
            public View getView(FlowLayout parent, int position, SubjectVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagSubject, false);
                tv.setText(isChinese() ? o.getName() : o.getEnarea());
                return tv;
            }
        };
        mTagSubject.setMaxSelectCount(1);
        mTagSubject.setAdapter(mSubjectAdapter);

        mTagSubject.setOnSelectListener(selectPosSet -> {
            pid = mSubjectVOList.get((int) (new ArrayList(selectPosSet)).get(0)).getId();
            //单选
            HashMap<String, Object> map = new HashMap<>();
            map.put("pid", pid);
            map.put("uid", getUserID());
            map.put("language", SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
            map.put("sname", etText);
            presenter.getDataAll("110", map);
        });

        mMajorAdapter = new TagAdapter<MajorVO>(mMajorVOList) {
            @Override
            public View getView(FlowLayout parent, int position, MajorVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagSubject, false);
                tv.setText(isChinese() ? o.getName() : o.getEnarea());
                return tv;
            }
        };
        mTagMajor.setAdapter(mMajorAdapter);
        mTagMajor.setMaxSelectCount(1);
        mTagMajor.setOnSelectListener(selectPosSet -> cid = mMajorVOList.get((int) (new ArrayList(selectPosSet)).get(0)).getId());
    }

    @Override
    public void onSuccess(String url, BaseBean entity) {
        super.onSuccess(url, entity);
        switch (url) {
            case "109":
                SubjectBean bean = (SubjectBean) entity;
                mSubjectVOList.clear();
                mSubjectVOList.addAll(bean.getMajors());
                mSubjectAdapter.notifyDataChanged();
                mMajorVOList.clear();
                mMajorAdapter.notifyDataChanged();
                break;
            case "110":
                MajorBean bean1 = (MajorBean) entity;
                mMajorVOList.clear();
                mMajorVOList.addAll(bean1.getMajors());
                mMajorAdapter.notifyDataChanged();
                break;
            case "111":
                start(SelectWordFragment.newInstance(1));
                SelectFieldActivity.newRxBus(SelectFieldActivity.WORD);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.btn_select, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                if (ObjectUtils.isEmpty(ToolString.getString(mEtSubject))) {
                    ToastUtils.showShort("请输入搜索内容");
                    break;
                }
                etText = ToolString.getString(mEtSubject);
                HashMap<String, Object> map = new HashMap<>();
                map.put("language", SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
                map.put("pname", ToolString.getString(mEtSubject));
                presenter.getDataAll("109", map);
                break;
            case R.id.btn_next:
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("mid", cid);
                map1.put("uid", getUserID());
                presenter.getDataAll("111", map1);
                break;
        }
    }
}
