package com.giiisp.giiisp.view.fragment.selectField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.WordBean;
import com.giiisp.giiisp.dto.WordVO;
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
 * 选择关键字
 */
public class SelectWordFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {

    public static final String TYPE = "type";
    @BindView(R.id.tag_word_system)
    TagFlowLayout mTagWordSystem;
    @BindView(R.id.tag_word_user)
    TagFlowLayout mTagWordUser;
    @BindView(R.id.btn_next)
    Button mButton;
    private List<WordVO> wordSystemList = new ArrayList<>();
    private List<WordVO> wordUserList = new ArrayList<>();
    private TagAdapter systemAdapter;
    private TagAdapter userAdapter;

    public static SelectWordFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(TYPE, type);

        SelectWordFragment fragment = new SelectWordFragment();
        fragment.setArguments(args);
        return fragment;
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
                break;
        }

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

        mTagWordSystem.setOnSelectListener(selectPosSet -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("aid", wordSystemList.get((int) (new ArrayList(selectPosSet)).get(0)).getId());
            map.put("uid", getUserID());
            presenter.getDataAll("114", map);
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

        mTagWordUser.setOnSelectListener(selectPosSet -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("aid", wordUserList.get((int) (new ArrayList(selectPosSet)).get(0)).getId());
            map.put("uid", getUserID());
            presenter.getDataAll("114", map);
        });

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("language",
                SPUtils.getInstance().getString(UrlConstants.LANGUAGE, "1"));
        presenter.getDataAll("112", map);
        presenter.getDataAll("113", map);

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        start(SelectPeopleFragment.newInstance(1));
        SelectFieldActivity.newRxBus(SelectFieldActivity.WORD);
    }

    @Override
    public void onSuccess(String url, BaseBean baseBean) {
        super.onSuccess(url, baseBean);
        switch (url) {
            case "112":
                WordBean bean = (WordBean) baseBean;
                wordSystemList.clear();
                wordSystemList.addAll(bean.getAlist());
                systemAdapter.notifyDataChanged();
                break;
            case "113":
                WordBean bean1 = (WordBean) baseBean;
                wordUserList.clear();
                wordUserList.addAll(bean1.getAlist());
                userAdapter.notifyDataChanged();
                break;
            case "114":
                ToastUtils.showShort("关注成功！");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFail(String url, String msg) {
        super.onFail(url, msg);
        switch (url) {
            case "114":
                ToastUtils.showShort("关注失败，请重试");
                break;
            default:
                break;
        }
    }
}
