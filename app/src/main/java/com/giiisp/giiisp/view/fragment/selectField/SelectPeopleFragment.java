package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.PeopleBean;
import com.giiisp.giiisp.dto.PeopleVO;
import com.giiisp.giiisp.dto.SubjectBean;
import com.giiisp.giiisp.dto.SubjectVO;
import com.giiisp.giiisp.presenter.WholePresenter;
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
 * 选择学者
 */
public class SelectPeopleFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {

    public static final String TYPE = "type";
    @BindView(R.id.tag_subject)
    TagFlowLayout mTagSubject;
    @BindView(R.id.tag_major)
    TagFlowLayout mTagMajor;
    @BindView(R.id.tag_people_system)
    TagFlowLayout mTagPeopleSystem;
    @BindView(R.id.tag_people_user)
    TagFlowLayout mTagPeopleUser;
    @BindView(R.id.btn_next)
    Button mButton;
    private List<MajorVO> mMajorVOS = new ArrayList<>();
    private List<SubjectVO> mSubjectVOS = new ArrayList<>();
    private List<PeopleVO> mPeopleVOSystem = new ArrayList<>();
    private List<PeopleVO> mPeopleVOUser = new ArrayList<>();
    private TagAdapter majorAdapter;
    private TagAdapter peopleSystemAdapter;
    private TagAdapter peopleUserAdapter;


    public static SelectPeopleFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(TYPE, type);

        SelectPeopleFragment fragment = new SelectPeopleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_select_people;
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


        //学科
        mTagSubject.setAdapter(new TagAdapter<SubjectVO>(mSubjectVOS) {
            @Override
            public View getView(FlowLayout parent, int position, SubjectVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_subject_layout,
                                mTagSubject, false);
                tv.setText(isChinese() ? o.getName() : o.getEnarea());
                return tv;
            }
        });


        mTagSubject.setOnTagClickListener((view, position, parent) -> {
            // TODO: 2018/7/25 高鹏 这里需不需要获取学科对应的专业？
            HashMap<String, Object> map = new HashMap<>();
            map.put("pid", mSubjectVOS.get(position).getId());
            map.put("uid", getUserID());
            map.put("language", SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
            map.put("sname", "");
            presenter.getDataAll("110", map);
            return true;
        });

        majorAdapter = new TagAdapter<MajorVO>(mMajorVOS) {
            @Override
            public View getView(FlowLayout parent, int position, MajorVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_subject_layout,
                                mTagMajor, false);
                tv.setText(isChinese() ? o.getName() : o.getEnarea());
                return tv;
            }
        };
        //专业
        mTagMajor.setAdapter(majorAdapter);
        mTagMajor.setOnSelectListener(selectPosSet -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uid", getUserID());
            map.put("mid", mMajorVOS.get((int) (new ArrayList(selectPosSet)).get(0)).getId());
            map.put("language", SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
            presenter.getDataAll("116", map);
        });

        //推荐的学者
        peopleSystemAdapter = new TagAdapter<PeopleVO>(mPeopleVOSystem) {
            @Override
            public View getView(FlowLayout parent, int position, PeopleVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_layout,
                                mTagPeopleSystem, false);
                tv.setText(o.getName());
                return tv;
            }
        };
        mTagPeopleSystem.setAdapter(peopleSystemAdapter);
        mTagPeopleSystem.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });

        //关注的学者
        peopleUserAdapter = new TagAdapter<PeopleVO>(mPeopleVOUser) {
            @Override
            public View getView(FlowLayout parent, int position, PeopleVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_layout,
                                mTagPeopleUser, false);
                tv.setText(o.getName());
                return tv;
            }
        };
        mTagPeopleUser.setAdapter(peopleUserAdapter);


        mTagPeopleUser.setOnSelectListener(selectPosSet -> {
//            StringBuffer a = new StringBuffer();
//            for (int position : selectPosSet) {
//                a.append(txt.get(position));
//            }
//            ToastUtils.showShort(a);
        });

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        presenter.getDataAll("115", map);// TODO: 2018/7/25 高鹏 这里是默认获取关注的专业吗？
        map.put("language", SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
        presenter.getDataAll("117", map);//获取专注的学者
        map.put("pname", "");
        presenter.getDataAll("109", map);//获取学科

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        //进入首页
        SelectFieldActivity.newRxBus(SelectFieldActivity.MAIN);
    }

    @Override
    public void onSuccess(String url, BaseBean baseBean) {
        super.onSuccess(url, baseBean);
        switch (url) {
            case "109":
                SubjectBean bean3 = (SubjectBean) baseBean;
                mSubjectVOS.clear();
                mSubjectVOS.addAll(bean3.getMajors());
                majorAdapter.notifyDataChanged();
                break;
            case "110":
                MajorBean bean4 = (MajorBean) baseBean;
                mMajorVOS.clear();
                mMajorVOS.addAll(bean4.getMajors());
                majorAdapter.notifyDataChanged();
                break;
            case "115":
                MajorBean bean = (MajorBean) baseBean;
                mMajorVOS.clear();
                mMajorVOS.addAll(bean.getMajors());
                majorAdapter.notifyDataChanged();
                break;
            case "116":
                PeopleBean bean1 = (PeopleBean) baseBean;
                mPeopleVOSystem.clear();
                mPeopleVOSystem.addAll(bean1.getUlist());
                peopleSystemAdapter.notifyDataChanged();
                break;
            case "117":
                PeopleBean bean2 = (PeopleBean) baseBean;
                mPeopleVOUser.clear();
                mPeopleVOUser.addAll(bean2.getUlist());
                peopleUserAdapter.notifyDataChanged();
                break;
            default:
                break;
        }
    }
}
