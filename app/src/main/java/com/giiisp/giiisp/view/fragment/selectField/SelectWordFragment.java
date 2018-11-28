package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.WordBean;
import com.giiisp.giiisp.dto.WordVO;
import com.giiisp.giiisp.entity.APPConstants;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.RxBus;
import com.giiisp.giiisp.view.activity.SelectFieldActivity;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择关键字
 */
public class SelectWordFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements MyRecyclerAdapter.OnMyItemClick {

    private static final String TAG = "SelectWordFragment";

    public static final String TYPE = "type";
    @BindView(R.id.tag_word_system)
    TagFlowLayout mTagWordSystem;
    @BindView(R.id.tag_word_user)
    TagFlowLayout mTagWordUser;
    @BindView(R.id.rv_word_user)
    RecyclerView mRecyclerUser;
    @BindView(R.id.rv_word_system)
    RecyclerView mRecyclerSystem;
    @BindView(R.id.btn_next)
    Button mButton;
    private List<WordVO> wordSystemList = new ArrayList<>();
    private List<WordVO> wordUserList = new ArrayList<>();
    private TagAdapter systemAdapter;
    private TagAdapter userAdapter;
    private List<ClickEntity> mEntitySystem = new ArrayList<>();
    private List<ClickEntity> mEntityUser = new ArrayList<>();
    private GridLayoutManager mManagerSystem;
    private GridLayoutManager mManagerUser;
    private MyRecyclerAdapter mAdapterSystem;
    private MyRecyclerAdapter mAdapterUser;

    public static SelectWordFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(TYPE, type);

        SelectWordFragment fragment = new SelectWordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void newRxBus() {
        Map<String, Object> map = new HashMap<>();
        map.put(APPConstants.MyBus.TO, TAG);
        RxBus.getInstance().send(map);
    }


    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_select_word;
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
                HashMap<String, Object> map = new HashMap<>();
                map.put("uid", getUserID());
                map.put("language", getLanguage());
                presenter.getDataAll("113", map);
                break;
        }

        mAdapterSystem = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mEntitySystem, 3, this);
        mManagerSystem = new GridLayoutManager(getActivity(), 2);
        mManagerSystem.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerSystem.setLayoutManager(mManagerSystem);
        mRecyclerSystem.setAdapter(mAdapterSystem);

        mAdapterUser = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mEntityUser, 4, this);
        mManagerUser = new GridLayoutManager(getActivity(), 2);
        mManagerUser.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerUser.setLayoutManager(mManagerUser);
        mRecyclerUser.setAdapter(mAdapterUser);


        systemAdapter = new TagAdapter<WordVO>(wordSystemList) {
            @Override
            public View getView(FlowLayout parent, int position, WordVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagWordSystem, false);
                tv.setText(o.getAntistop());
                return tv;
            }
        };

        mTagWordSystem.setAdapter(systemAdapter);

        mTagWordSystem.setOnTagClickListener((view, position, parent) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("aid", wordSystemList.get(position).getId());
            map.put("uid", getUserID());
            presenter.getDataAll("114", map);
            return true;
        });

        userAdapter = new TagAdapter<WordVO>(wordUserList) {
            @Override
            public View getView(FlowLayout parent, int position, WordVO o) {
                TextView tv = (TextView) LayoutInflater
                        .from(getActivity()).inflate(R.layout.tag_select_item_layout,
                                mTagWordUser, false);
                tv.setText(o.getAntistop());
                return tv;
            }
        };
        mTagWordUser.setAdapter(userAdapter);

        mTagWordUser.setOnTagClickListener((view, position, parent) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("aid", wordUserList.get(position).getId());
            map.put("uid", getUserID());
            presenter.getDataAll("114", map);
            return true;
        });

        requestData();

        RxBus.getInstance().register(Map.class, map1 -> {
            switch ((String) map1.get(APPConstants.MyBus.TO)) {
                case TAG:
                    requestData();
                    break;
                default:
                    break;
            }
        });

    }

    private void requestData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("language",
                getLanguage());
        presenter.getDataAll("112", map);
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        start(SelectPeopleFragment.newInstance(1));
        SelectFieldActivity.newRxBus(SelectFieldActivity.PEOPLE);
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        HashMap<String, Object> map = new HashMap<>();
        switch (url) {
            case "112"://推荐的关键字
                WordBean bean = (WordBean) baseBean;
                mEntitySystem.clear();
                for (WordVO vo : bean.getAlist()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setWordVO(vo);
                    mEntitySystem.add(entity);
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
                mEntityUser.clear();
                mAdapterUser.clearSelectIds();
                mAdapterUser.notifyDataSetChanged();
                map.put("uid", getUserID());
                map.put("language", getLanguage());
                presenter.getDataAll("113", map);
                break;
            case "113"://关注的关键字
                WordBean bean1 = (WordBean) baseBean;
                mEntityUser.clear();
                mAdapterUser.clearSelectIds();
                mAdapterSystem.clearSelectIds();
                for (WordVO vo : bean1.getAlist()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setWordVO(vo);
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
            case "114":
                map.put("uid", getUserID());
                map.put("language", getLanguage());
                presenter.getDataAll("113", map);
                //更新学者界面
                SelectPeopleFragment.newRxBus();
                break;
            default:
                break;
        }
    }

    @Override
    public void myItemClick(int type, String id) {
        switch (type) {
            case 3:
            case 4:
                HashMap<String, Object> map = new HashMap<>();
                map.put("aid", id);
                map.put("uid", getUserID());
                presenter.getDataAll("114", map);
                break;
            default:
                break;
        }
    }
}
