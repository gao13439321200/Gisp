package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.SubjectBean;
import com.giiisp.giiisp.dto.SubjectVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.view.activity.SelectFieldActivity;
import com.giiisp.giiisp.view.impl.BaseImpl;
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
public class SelectFieldFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {

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

        mTagSubject.setOnTagClickListener((view, position, parent) -> {
            LogUtils.v("id:" + mSubjectVOList.get(position).getId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("pid", mSubjectVOList.get(position).getId());
            map.put("uid", getUserID());
            map.put("language", getLanguage());
            map.put("sname", etText);
            presenter.getDataAll("110", map);
            return true;
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
        mTagMajor.setOnTagClickListener((view, position, parent) -> {
            LogUtils.v("id:" + mMajorVOList.get(position).getId());
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("mid", mMajorVOList.get(position).getId());
            map1.put("uid", getUserID());
            presenter.getDataAll("111", map1);
            return true;
        });
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onSuccessNew(String url, BaseBean entity) {
        super.onSuccessNew(url, entity);
        switch (url) {
            case "109":
                SubjectBean bean = (SubjectBean) entity;
                if (bean.getList().size() == 0) {
                    ToastUtils.showShort("暂无学科信息");
                }
                mSubjectVOList.clear();
                mSubjectVOList.addAll(bean.getList());
                mSubjectAdapter.notifyDataChanged();
                mMajorVOList.clear();
                mMajorAdapter.notifyDataChanged();
                break;
            case "110":
                MajorBean bean1 = (MajorBean) entity;
                if (bean1.getMajors().size() == 0) {
                    ToastUtils.showShort("暂无专业信息");
                }
                mMajorVOList.clear();
                mMajorVOList.addAll(bean1.getMajors());
                mMajorAdapter.notifyDataChanged();
                break;
            case "111":
//                ToastUtils.showShort("成功！");
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.btn_select, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                KeyboardUtils.hideSoftInput(mEtSubject);
                etText = ToolString.getString(mEtSubject);
                HashMap<String, Object> map = new HashMap<>();
                map.put("language", getLanguage());
                map.put("pname", ObjectUtils.isNotEmpty(etText) ? etText : "");
                presenter.getDataAll("109", map);
                break;
            case R.id.btn_next:
                start(SelectWordFragment.newInstance(1));
                SelectFieldActivity.newRxBus(SelectFieldActivity.WORD);
                break;
        }
    }
}
