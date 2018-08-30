package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
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
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择领域
 */
public class SelectFieldFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements MyRecyclerAdapter.OnMyItemClick {

    public static final String TYPE = "type";
    @BindView(R.id.et_subject)
    EditText mEtSubject;
    @BindView(R.id.btn_next)
    Button mButton;
    @BindView(R.id.rv_subject)
    RecyclerView mRecyclerSubject;
    @BindView(R.id.rv_major)
    RecyclerView mRecyclerMajor;
    private String etText = "";
    private List<ClickEntity> mEntitySubject = new ArrayList<>();
    private List<ClickEntity> mEntityMajor = new ArrayList<>();
    private MyRecyclerAdapter mAdapterSubject;
    private MyRecyclerAdapter mAdapterMajor;
    private GridLayoutManager mManagerSubject;
    private GridLayoutManager mManagerMajor;

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

        mAdapterSubject = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mEntitySubject, 1, this);
        mManagerSubject = new GridLayoutManager(getActivity(), 2);
        mManagerSubject.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerSubject.setLayoutManager(mManagerSubject);
        mRecyclerSubject.setAdapter(mAdapterSubject);


        mAdapterMajor = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mEntityMajor, 2, this);
        mManagerMajor = new GridLayoutManager(getActivity(), 2);
        mManagerMajor.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerMajor.setLayoutManager(mManagerMajor);
        mRecyclerMajor.setAdapter(mAdapterMajor);

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
                mEntitySubject.clear();
                for (SubjectVO vo : bean.getList()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setSubjectVO(vo);
                    mEntitySubject.add(entity1);
                }
                if (mEntitySubject.size() < 7) {
                    mManagerSubject.setOrientation(GridLayoutManager.VERTICAL);
                    mManagerSubject.setSpanCount(3);
                } else {
                    mManagerSubject.setOrientation(GridLayoutManager.HORIZONTAL);
                    mManagerSubject.setSpanCount(2);
                }
                mRecyclerSubject.setLayoutManager(mManagerSubject);
                mAdapterSubject.notifyDataSetChanged();

                mEntityMajor.clear();
                mAdapterMajor.clearSelectIds();
                mAdapterMajor.notifyDataSetChanged();
                break;
            case "110":
                MajorBean bean1 = (MajorBean) entity;
                if (bean1.getMajors().size() == 0) {
                    ToastUtils.showShort("暂无专业信息");
                }
                mEntityMajor.clear();
                for (MajorVO vo : bean1.getMajors()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setMajorVO(vo);
                    mEntityMajor.add(entity1);
                }
                if (mEntityMajor.size() < 7) {
                    mManagerMajor.setOrientation(GridLayoutManager.VERTICAL);
                    mManagerMajor.setSpanCount(3);
                } else {
                    mManagerMajor.setOrientation(GridLayoutManager.HORIZONTAL);
                    mManagerMajor.setSpanCount(2);
                }
                mRecyclerMajor.setLayoutManager(mManagerMajor);
                mAdapterMajor.notifyDataSetChanged();

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

    @Override
    public void myItemClick(int type, String id) {
        switch (type) {
            case 1:
                HashMap<String, Object> map = new HashMap<>();
                map.put("pid", id);
                map.put("uid", getUserID());
                map.put("language", getLanguage());
                map.put("sname", etText);
                presenter.getDataAll("110", map);
                break;
            case 2:
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("mid", id);
                map1.put("uid", getUserID());
                presenter.getDataAll("111", map1);
                break;
            default:
                break;
        }
    }
}
