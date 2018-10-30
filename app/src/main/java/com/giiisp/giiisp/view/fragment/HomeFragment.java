package com.giiisp.giiisp.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.AppInfoBean;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.HeadImgBean;
import com.giiisp.giiisp.dto.HotImgBean;
import com.giiisp.giiisp.dto.HotImgVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.view.activity.FragmentActivity;
import com.giiisp.giiisp.view.activity.LogInActivity;
import com.giiisp.giiisp.view.activity.SearchActivity;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.MultipleItemQuickAdapter;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.giiisp.giiisp.widget.UpdatePopupWindow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giiisp.giiisp.R.id.recycler_view;
import static com.giiisp.giiisp.base.BaseActivity.uid;
//import static com.giiisp.giiisp.base.BaseActivity.token;

/**
 * home页面
 * Created by Thinkpad on 2017/5/19.
 */

public class HomeFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_news_spot)
    ImageView tvNewsSpot;
    @BindView(R.id.tv_home_news)
    TextView tvHomeNews;
    @BindView(R.id.tv_home_search)
    TextView tvHomeSearch;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private MultipleItemQuickAdapter multipleItemQuickAdapter;
    private String string;
    private static final int CHANGE_ITEM = 1;
    private Timer timer;
    private MyHandler myHandler;
    private List<HotImgVO> list1 = new ArrayList<>();
    private List<HotImgVO> list2 = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_home;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("1005", param1);
        args.putString("1006", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAuxiliary);
        swipeRefreshLayout.setOnRefreshListener(this);
        List<ClickEntity> list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        multipleItemQuickAdapter = new MultipleItemQuickAdapter((BaseActivity) getActivity(), list);
        recyclerView.setAdapter(multipleItemQuickAdapter);
        View notDataView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) recyclerView.getParent(), false);
        multipleItemQuickAdapter.setEmptyView(notDataView);
    }

    @Override
    public void initData() {
        super.initData();
        String type = getArguments().getString("1005");
        string = getArguments().getString("1006");
        initNetwork();

    }

    @Override
    public void initNetwork() {
        super.initNetwork();
        HashMap<String, Object> map = new HashMap<>();
        map.put("language", getLanguage());
        swipeRefreshLayout.setRefreshing(true);
        presenter.getDataAll("201", map);
        map = new HashMap<>();
        map.put("type", "1");
        map.put("version", AppUtils.getAppVersionName());
        presenter.getDataAll("119", map);
        if (multipleItemQuickAdapter != null)
            multipleItemQuickAdapter.setNewData(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fl_news, R.id.tv_home_search, R.id.tv_home_history, R.id.tv_home_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_news:
                FragmentActivity.actionActivity(getContext(), "news");
                break;
            case R.id.tv_home_search:
                if (uid.equals("15")) {
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(getActivity());
                    normalDialog.setIcon(null);
                    normalDialog.setTitle(R.string.need_login);
                    normalDialog.setPositiveButton(R.string.register,
                            (dialogInterface, i) -> LogInActivity.actionActivity(getActivity()));
                    normalDialog.setNegativeButton(R.string.cancel, null);
                    // 显示
                    normalDialog.show();
                } else
                    SearchActivity.actionActivity(getActivity());
                break;
            case R.id.tv_home_history:
                FragmentActivity.actionActivity(getActivity(), "plays");
                break;
            case R.id.tv_home_download:
                FragmentActivity.actionActivity(getContext(), "download_activity");
                break;
        }
    }

    private int cnt1 = -1; //表示当前最右边显示的item的position
    private int cnt2 = -1; //表示当前最右边显示的item的position
//    private boolean isSlidingByHand = false; //表示是否是手在滑动
//    private boolean isSlidingAuto = true; //表示是否自动滑动

    private RecyclerView getRecyclerView1() {
        return multipleItemQuickAdapter.getRecyclerView1();
    }

    private RecyclerView getRecyclerView2() {
        return multipleItemQuickAdapter.getRecyclerView2();
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        switch (url) {
            case "119"://更新
                AppInfoBean appInfo = (AppInfoBean) baseBean;
                if (appInfo != null && appInfo.getAppInfo() != null && ObjectUtils.isNotEmpty(appInfo.getAppInfo().getVersionCode())) {
                    if (!appInfo.getAppInfo().getVersionCode().equals(AppUtils.getAppVersionName())) {
                        UpdatePopupWindow updatePopupWindow = new UpdatePopupWindow(getActivity(), appInfo);
                        updatePopupWindow.showPopupWindow();
                    }
                }
                break;
            case "201"://首页轮播图
                HeadImgBean bean = (HeadImgBean) baseBean;
                ClickEntity clickEntityHead = new ClickEntity(R.layout.item_home_head, "head");
                clickEntityHead.setHeadImgBean(bean);
                multipleItemQuickAdapter.addData(clickEntityHead);
                HashMap<String, Object> map = new HashMap<>();
                map.put("language", getLanguage());
                swipeRefreshLayout.setRefreshing(true);
                presenter.getDataAll("202", map);
                break;
            case "202"://热门和综述
                HotImgBean bean1 = (HotImgBean) baseBean;
//                List<HotImgVO> list = bean1.getList();
//                bean1.getList().addAll(list);
                ClickEntity clickEntityHot = new ClickEntity(R.layout.item_home_recycler, getString(R.string.in_a_column));
                clickEntityHot.setHotImgBean(bean1);
                multipleItemQuickAdapter.addData(clickEntityHot);
                ClickEntity clickEntity = new ClickEntity(R.layout.item_home_recycler, getString(R.string.hot_recommended));
                clickEntity.setHotImgBean(bean1);
                multipleItemQuickAdapter.addData(clickEntity);
                for (HotImgVO vo : bean1.getList()) {
                    if ("1".equals(vo.getType())) {
                        list1.add(vo);
                    } else {
                        list2.add(vo);
                    }
                }
                timer = new Timer();
                myHandler = new MyHandler(this);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
//                        if (isSlidingAuto) {
                        myHandler.sendEmptyMessage(CHANGE_ITEM);
//                        }
                    }
                }, 500, 3000);
//                initListener();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onFailure(String msg, Exception ex) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        super.onFailure(msg, ex);
    }

    @Override
    public void onFailNew(String url, String msg) {
        switch (url) {
            case "119":

                break;
            default:
                super.onFailNew(url, msg);
                break;
        }
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }


    @Override
    public void onRefresh() {
        initNetwork();
    }


//    private void initListener() {
//        getRecyclerView1().addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
//                switch (newState) {
//                    case SCROLL_STATE_IDLE:  //（静止没有滚动）
//                        if (isSlidingByHand) {
//                            Message msg = myHandler.obtainMessage();
//                            msg.arg1 = firstVisibleItemPosition;
//                            msg.what = CHANGE_ITEM;
//                            myHandler.sendMessage(msg);
//                        }
//                        break;
//                    case SCROLL_STATE_DRAGGING:  //（正在被外部拖拽,一般为用户正在用手指滚动）
//                        isSlidingByHand = true;
//                        isSlidingAuto = false;
//                        break;
//                    case SCROLL_STATE_SETTLING:  //（自动滚动）
//                        if (isSlidingByHand) {
//                            isSlidingAuto = false;
//                        } else {
//                            isSlidingAuto = true;
//                        }
//                        break;
//                }
//            }
//        });
//    }

    private void stopTimer() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        cnt1 = -1;
        cnt2 = -1;
    }

    private static class MyHandler extends Handler {
        WeakReference<HomeFragment> weakReference;

        public MyHandler(HomeFragment mActivity) {
            this.weakReference = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final HomeFragment mActivity = weakReference.get();
//            if (mActivity.isSlidingByHand) {
//                mActivity.cnt = msg.arg1;
//                mActivity.isSlidingByHand = false;
//                mActivity.isSlidingAuto = true;
//                mActivity.cnt += 3;
//                mActivity.getRecyclerView1().smoothScrollToPosition(mActivity.cnt);
//            } else {
            if (mActivity.cnt1 != -1 && mActivity.getRecyclerView1() != null) {
                mActivity.cnt1 = ((LinearLayoutManager) mActivity.getRecyclerView1().getLayoutManager()).findFirstVisibleItemPosition() + 2;
            }
            if (mActivity.cnt2 != -1 && mActivity.getRecyclerView2() != null) {
                mActivity.cnt2 = ((LinearLayoutManager) mActivity.getRecyclerView2().getLayoutManager()).findFirstVisibleItemPosition() + 2;
            }

            mActivity.cnt1 = setData(mActivity.cnt1, mActivity.list1.size() - 1, mActivity.getRecyclerView1());
            mActivity.cnt2 = setData(mActivity.cnt2, mActivity.list2.size() - 1, mActivity.getRecyclerView2());
//            }
        }

        private int setData(int cnt, int count, RecyclerView view) {
//            LogUtils.a("aaaaa====cnt:" + cnt + ",count:" + count);
            if (cnt == -1) {
                cnt = 2;
                return 2;
            }
            if (cnt == count) {//已经是最后一页了
                if (view != null)
                    view.smoothScrollToPosition(0);
                cnt = 2;
                return 2;
            } else if (cnt > count - 3) {//下一页是最后一页
                cnt = count;
            } else {
                cnt += 3;
            }
            Log.d("MyHandler", "mActivity.cnt:" + cnt);
            if (view != null)
                view.smoothScrollToPosition(cnt);
            return cnt;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
