package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.PeopleBean;
import com.giiisp.giiisp.dto.PeopleVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.model.GlideApp;
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
    //    @BindView(R.id.tag_subject)
//    TagFlowLayout mTagSubject;
    @BindView(R.id.tag_major)
    TagFlowLayout mTagMajor;
    @BindView(R.id.tag_people_system)
    TagFlowLayout mTagPeopleSystem;
    @BindView(R.id.tag_people_user)
    TagFlowLayout mTagPeopleUser;
    @BindView(R.id.btn_next)
    Button mButton;
    private List<MajorVO> mMajorVOS = new ArrayList<>();
    //    private List<SubjectVO> mSubjectVOS = new ArrayList<>();
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
//        mTagSubject.setAdapter(new TagAdapter<SubjectVO>(mSubjectVOS) {
//            @Override
//            public View getView(FlowLayout parent, int position, SubjectVO o) {
//                TextView tv = (TextView) LayoutInflater
//                        .from(getActivity()).inflate(R.layout.tag_select_people_subject_layout,
//                                mTagSubject, false);
//                tv.setText(isChinese() ? o.getName() : o.getEnarea());
//                return tv;
//            }
//        });


//        mTagSubject.setOnTagClickListener((view, position, parent) -> {
//            // 这里需不需要获取学科对应的专业？
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("pid", mSubjectVOS.get(position).getId());
//            map.put("uid", getUserID());
//            map.put("language", getLanguage());
//            map.put("sname", "");
//            presenter.getDataAll("110", map);
//            return true;
//        });

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
        mTagMajor.setMaxSelectCount(1);
        mTagMajor.setAdapter(majorAdapter);
        mTagMajor.setOnTagClickListener((view, position, parent) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uid", getUserID());
            map.put("mid", mMajorVOS.get(position).getId());
            map.put("language", getLanguage());
            presenter.getDataAll("116", map);
            return true;
        });

        //推荐的学者
        peopleSystemAdapter = new TagAdapter<PeopleVO>(mPeopleVOSystem) {
            @Override
            public View getView(FlowLayout parent, int position, PeopleVO o) {
                LinearLayout ll = (LinearLayout) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_layout,
                                mTagPeopleSystem, false);
                TextView tv = ll.findViewById(R.id.tv_name);
                tv.setText(o.getName());
                ImageView img = ll.findViewById(R.id.img_head);
                GlideApp.with(getActivity())
                        .load(o.getAvatar())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(img);
                return ll;
            }
        };
        mTagPeopleSystem.setAdapter(peopleSystemAdapter);
        mTagPeopleSystem.setOnTagClickListener((view, position, parent) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("sid", mPeopleVOSystem.get(position).getId());
            map.put("uid", getUserID());
            presenter.getDataAll("118", map);
            return true;
        });

        //关注的学者
        peopleUserAdapter = new TagAdapter<PeopleVO>(mPeopleVOUser) {
            @Override
            public View getView(FlowLayout parent, int position, PeopleVO o) {
                LinearLayout ll = (LinearLayout) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_people_layout,
                                mTagPeopleUser, false);
                TextView tv = ll.findViewById(R.id.tv_name);
                tv.setText(o.getName());
                ImageView img = ll.findViewById(R.id.img_head);
                GlideApp.with(getActivity())
                        .load(o.getAvatar())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(img);
                return ll;
            }
        };
        mTagPeopleUser.setAdapter(peopleUserAdapter);
        mTagPeopleUser.setOnTagClickListener((view, position, parent) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("sid", mPeopleVOUser.get(position).getId());
            map.put("uid", getUserID());
            presenter.getDataAll("118", map);
            return true;
        });

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        presenter.getDataAll("115", map);//获取关注的专业
        map.put("language", getLanguage());
        presenter.getDataAll("117", map);//获取专注的学者

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        //进入首页
        SelectFieldActivity.newRxBus(SelectFieldActivity.MAIN);
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
//            case "109":
//                SubjectBean bean3 = (SubjectBean) baseBean;
//                mSubjectVOS.clear();
//                mSubjectVOS.addAll(bean3.getList());
//                majorAdapter.notifyDataChanged();
//                break;
//            case "110":
//                MajorBean bean4 = (MajorBean) baseBean;
//                mMajorVOS.clear();
//                mMajorVOS.addAll(bean4.getMajors());
//                majorAdapter.notifyDataChanged();
//                break;
            case "115"://关注的专业
                MajorBean bean = (MajorBean) baseBean;
                mMajorVOS.clear();
                mMajorVOS.addAll(bean.getMajors());
                majorAdapter.notifyDataChanged();
                if (bean.getMajors() != null && bean.getMajors().size() != 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uid", getUserID());
                    map.put("mid", mMajorVOS.get(0).getId());
                    map.put("language", getLanguage());
                    presenter.getDataAll("116", map);
                    majorAdapter.setSelectedList(0);
                }
                break;
            case "116"://推荐的学者
                PeopleBean bean1 = (PeopleBean) baseBean;
                mPeopleVOSystem.clear();
                mPeopleVOSystem.addAll(bean1.getUlist());
                peopleSystemAdapter.notifyDataChanged();
                break;
            case "117"://关注的学者
                PeopleBean bean2 = (PeopleBean) baseBean;
                mPeopleVOUser.clear();
                mPeopleVOUser.addAll(bean2.getUlist());
                peopleUserAdapter.notifyDataChanged();
                break;
            case "118":
                HashMap<String, Object> map = new HashMap<>();
                map.put("uid", getUserID());
                map.put("language", getLanguage());
                presenter.getDataAll("117", map);//获取专注的学者
                break;
            default:
                break;
        }
    }
}
