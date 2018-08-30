package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.PeopleBean;
import com.giiisp.giiisp.dto.PeopleVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.view.activity.SelectFieldActivity;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择学者
 */
public class SelectPeopleFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements MyRecyclerAdapter.OnMyItemClick {

    public static final String TYPE = "type";
    @BindView(R.id.btn_next)
    Button mButton;
    @BindView(R.id.rv_user)
    RecyclerView mRecyclerUser;
    @BindView(R.id.rv_major)
    RecyclerView mRecyclerMajor;
    @BindView(R.id.rv_system)
    RecyclerView mRecyclerSystem;

    private List<ClickEntity> mEntitySystem = new ArrayList<>();
    private List<ClickEntity> mEntityUser = new ArrayList<>();
    private List<ClickEntity> mEntityMajor = new ArrayList<>();
    private GridLayoutManager mManagerSystem;
    private GridLayoutManager mManagerUser;
    private MyRecyclerAdapter mAdapterSystem;
    private MyRecyclerAdapter mAdapterUser;
    private MyRecyclerAdapter mAdapterMajor;


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

        //专业
        mAdapterMajor = new MyRecyclerAdapter(R.layout.tag_select_people_subject_layout,
                mEntityMajor, 5, this);
        GridLayoutManager mManagerMajor = new GridLayoutManager(getActivity(), 1);
        mManagerMajor.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerMajor.setLayoutManager(mManagerMajor);
        mRecyclerMajor.setAdapter(mAdapterMajor);

        //推荐的学者
        mAdapterSystem = new MyRecyclerAdapter(R.layout.tag_select_people_layout,
                mEntitySystem, 6, this);
        mManagerSystem = new GridLayoutManager(getActivity(), 2);
        mManagerSystem.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerSystem.setLayoutManager(mManagerSystem);
        mRecyclerSystem.setAdapter(mAdapterSystem);

        //关注的学者
        mAdapterUser = new MyRecyclerAdapter(R.layout.tag_select_people_layout,
                mEntityUser, 7, this);
        mManagerUser = new GridLayoutManager(getActivity(), 2);
        mManagerUser.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerUser.setLayoutManager(mManagerUser);
        mRecyclerUser.setAdapter(mAdapterUser);

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        presenter.getDataAll("115", map);//获取关注的专业
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
        HashMap<String, Object> map = new HashMap<>();
        switch (url) {
            case "115"://关注的专业
                MajorBean bean1 = (MajorBean) baseBean;
                mEntityMajor.clear();
                for (MajorVO vo : bean1.getMajors()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setMajorVO(vo);
                    mEntityMajor.add(entity1);
                }
                mAdapterMajor.notifyDataSetChanged();

                if (bean1.getMajors() != null && bean1.getMajors().size() != 0) {
                    map.put("uid", getUserID());
                    map.put("mid", bean1.getMajors().get(0).getId());
                    map.put("language", getLanguage());
                    presenter.getDataAll("116", map);
                    mAdapterMajor.setSelectIdsData(bean1.getMajors().get(0).getId());
                    mAdapterMajor.notifyDataSetChanged();
                }
                break;
            case "116"://推荐的学者
                PeopleBean peopleBean = (PeopleBean) baseBean;

                mEntitySystem.clear();
                for (PeopleVO vo : peopleBean.getUlist()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setPeopleVO(vo);
                    mEntitySystem.add(entity1);
                }

                if (mEntitySystem.size() < 7) {
                    mManagerSystem.setOrientation(GridLayoutManager.VERTICAL);
                    mManagerSystem.setSpanCount(3);
                } else {
                    mManagerSystem.setOrientation(GridLayoutManager.HORIZONTAL);
                    mManagerSystem.setSpanCount(2);
                }
                mRecyclerSystem.setLayoutManager(mManagerSystem);
                mAdapterSystem.notifyDataSetChanged();

                map.put("uid", getUserID());
                map.put("language", getLanguage());
                presenter.getDataAll("117", map);//获取专注的学者
                break;
            case "117"://关注的学者
                PeopleBean peopleBean1 = (PeopleBean) baseBean;
                mEntityUser.clear();
                mAdapterUser.clearSelectIds();
                mAdapterSystem.clearSelectIds();
                for (PeopleVO vo : peopleBean1.getUlist()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setPeopleVO(vo);
                    mEntityUser.add(entity1);
                    mAdapterSystem.setSelectIdsData(vo.getId());
                    mAdapterUser.setSelectIdsData(vo.getId());
                }
                if (mEntityUser.size() < 7) {
                    mManagerUser.setOrientation(GridLayoutManager.VERTICAL);
                    mManagerUser.setSpanCount(3);
                } else {
                    mManagerUser.setOrientation(GridLayoutManager.HORIZONTAL);
                    mManagerUser.setSpanCount(2);
                }
                mRecyclerUser.setLayoutManager(mManagerUser);
                mAdapterUser.notifyDataSetChanged();
                mAdapterSystem.notifyDataSetChanged();

                break;
            case "118":
                map.put("uid", getUserID());
                map.put("language", getLanguage());
                presenter.getDataAll("117", map);//获取专注的学者
                break;
            default:
                break;
        }
    }

    @Override
    public void myItemClick(int type, String id) {
        HashMap<String, Object> map = new HashMap<>();
        switch (type) {
            case 5:
                map.put("uid", getUserID());
                map.put("mid", id);
                map.put("language", getLanguage());
                presenter.getDataAll("116", map);
                break;
            case 6:
            case 7:
                map.put("sid", id);
                map.put("uid", getUserID());
                presenter.getDataAll("118", map);
                break;
            default:
                break;
        }
    }
}
