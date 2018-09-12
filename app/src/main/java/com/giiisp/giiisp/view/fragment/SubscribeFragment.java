package com.giiisp.giiisp.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseFragment;
import com.giiisp.giiisp.view.adapter.ViewPagerAdapter;
import com.giiisp.giiisp.widget.MViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SubscribeFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tl_my_attention)
    TabLayout tlMySubscription;
    @BindView(R.id.vp_my_attention)
    MViewPager vpMySubscription;
    @BindView(R.id.tv_title_rt)
    TextView mTvTitleRt;
    private ArrayList<BaseFragment> fragmentList;

    public static SubscribeFragment newInstance() {
        Bundle args = new Bundle();
        SubscribeFragment fragment = new SubscribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_subscribe;
    }

    @Override
    public void initView() {
        mTvTitleRt.setText(R.string.subscribe);
        List<String> titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add(getString(R.string.major));
        titleList.add(getString(R.string.keyword));
        titleList.add(getString(R.string.scholars));
        fragmentList.add(SubscribeItemFragment.newInstance("115"));
        fragmentList.add(SubscribeItemFragment.newInstance("113"));
        fragmentList.add(SubscribeItemFragment.newInstance("117"));
        vpMySubscription.setAdapter(new ViewPagerAdapter(getFragmentManager(), fragmentList, titleList));
        tlMySubscription.setupWithViewPager(vpMySubscription);
        vpMySubscription.setCurrentItem(0);
        vpMySubscription.setOffscreenPageLimit(2);
        tlMySubscription.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                tlMySubscription.setBackgroundResource(R.mipmap.guide_tab_left);
                break;
            case 1:
                tlMySubscription.setBackgroundResource(R.mipmap.guide_tab_center);
                break;
            case 2:
                tlMySubscription.setBackgroundResource(R.mipmap.guide_tab_right);
                break;
        }
        for (BaseFragment baseFragment : fragmentList) {
            baseFragment.attention();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick({R.id.iv_play, R.id.iv_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
                break;
            case R.id.iv_download:
                break;
        }
    }
}
