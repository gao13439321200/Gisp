package com.giiisp.giiisp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.DownloadImgInfoVO;
import com.giiisp.giiisp.dto.DownloadInfoBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.PaperMainBean;
import com.giiisp.giiisp.dto.PaperMainVO;
import com.giiisp.giiisp.dto.PeopleBean;
import com.giiisp.giiisp.dto.PeopleVO;
import com.giiisp.giiisp.dto.WordBean;
import com.giiisp.giiisp.dto.WordVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.DownloadController;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.PaperDetailsActivity;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.view.fragment.selectField.MyRecyclerAdapter;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;

public class SubscribeItemFragment extends BaseMvpFragment<BaseImpl, WholePresenter>
        implements MyRecyclerAdapter.OnMyItemClick,
        BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        ListItemClick,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_label)
    RecyclerView mRvLabel;
    @BindView(R.id.rv_paper)
    RecyclerView mRvPaper;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<ClickEntity> mLvLabel = new ArrayList<>();
    private List<ClickEntity> mLvPaper = new ArrayList<>();
    private ItemClickAdapter mItemClickAdapter;
    private MyRecyclerAdapter mAdapterSystem;
    private GridLayoutManager mManagerUser;
    private String type;
    private int page = 0;
    private List<String> selectIDs = new ArrayList<>();
    private List<String> allIDs = new ArrayList<>();
    private RxDownload mRxDownload;

    public static SubscribeItemFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        SubscribeItemFragment fragment = new SubscribeItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_subscribe;
    }

    @Override
    public void initView() {
        type = getArguments().getString("type");
        mAdapterSystem = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mLvLabel, 3, this);
        mManagerUser = new GridLayoutManager(getActivity(), 2);
        mManagerUser.setOrientation(GridLayoutManager.HORIZONTAL);
        mRvLabel.setLayoutManager(mManagerUser);
        mRvLabel.setAdapter(mAdapterSystem);
        mRxDownload = RxDownload.getInstance(getContext());
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAuxiliary);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRvPaper.setLayoutManager(manager);
        mItemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(),
                R.layout.item_paper, mLvPaper, this);
        mItemClickAdapter.bindToRecyclerView(mRvPaper);
        mItemClickAdapter.setOnItemClickListener(this);
        mItemClickAdapter.disableLoadMoreIfNotFullPage();
        mItemClickAdapter.setEmptyView(R.layout.empty_view);
        mRvPaper.setAdapter(mItemClickAdapter);

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("language", getLanguage());
        presenter.getDataAll(type, map);//专业115,关键字113,学者117
    }

    @Override
    public void myItemClick(int type, String id) {
        if (selectIDs.contains(id)) {
            selectIDs.remove(id);
        } else {
            selectIDs.add(id);
        }
        getPaperData();

    }

    private void getPaperData() {
        StringBuilder stringBuilder = new StringBuilder();
//        if (selectIDs.size() == 0) {
//            mLvPaper.clear();
//            mItemClickAdapter.notifyDataSetChanged();
//            swipeRefreshLayout.setRefreshing(false);
//            return;
        for (String mId : selectIDs.size() == 0 ? allIDs : selectIDs) {
            stringBuilder.append(mId);
            stringBuilder.append("#");
        }
        String sId = "";
        if (stringBuilder.length() > 1)
            sId = stringBuilder.substring(0, stringBuilder.length() - 1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("pageno", page);
        map.put("aid", "");
        map.put("mid", "");
        map.put("auid", "");
        switch (this.type) {
            case "113":
                map.put("aid", sId);
                break;
            case "115":
                map.put("mid", sId);
                break;
            case "117":
                map.put("auid", sId);
                break;
            default:
                break;
        }
        presenter.getDataAll("219", map);
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "213":
                ToastUtils.showShort("收藏成功！");
                return;
            case "217":
                ToastUtils.showShort("取消收藏成功！");
                return;
            case "216":
                DownloadInfoBean infoBean = (DownloadInfoBean) baseBean;
                if (infoBean == null) {
                    ToastUtils.showShort("下载信息异常，请重试");
                    return;
                }
                DownloadController mDownloadController = new DownloadController(new TextView(context), new ImageView(context));
                mDownloadController.handleClick(new DownloadController.Callback() {
                    @Override
                    public void startDownload() {
                        start(infoBean);
                    }

                    @Override
                    public void pauseDownload() {
                    }

                    @Override
                    public void install() {
                        //                        installApk();
                        Utils.showToast("下载完成");
                    }
                });
                return;
            default:
                break;
        }


        if (!"219".equals(url)) {
            mLvLabel.clear();
            mAdapterSystem.clearSelectIds();
            allIDs.clear();
            switch (url) {
                case "113":
                    WordBean bean1 = (WordBean) baseBean;
                    for (WordVO vo : bean1.getAlist()) {
                        ClickEntity entity1 = new ClickEntity();
                        entity1.setWordVO(vo);
                        mLvLabel.add(entity1);
                        allIDs.add(vo.getId());
//                    mAdapterSystem.setSelectIdsData(vo.getId());
                    }
                    break;
                case "115":
                    MajorBean bean2 = (MajorBean) baseBean;
                    for (MajorVO vo : bean2.getMajors()) {
                        ClickEntity entity1 = new ClickEntity();
                        WordVO vo1 = new WordVO();
                        vo1.setId(vo.getId());
                        vo1.setAntistop(vo.getName());
                        entity1.setWordVO(vo1);
                        mLvLabel.add(entity1);
                        allIDs.add(vo.getId());
                    }
                    break;
                case "117":
                    PeopleBean peopleBean1 = (PeopleBean) baseBean;
                    for (PeopleVO vo : peopleBean1.getUlist()) {
                        ClickEntity entity1 = new ClickEntity();
                        WordVO vo1 = new WordVO();
                        vo1.setId(vo.getId());
                        vo1.setAntistop(vo.getName());
                        entity1.setWordVO(vo1);
                        mLvLabel.add(entity1);
                        allIDs.add(vo.getId());
                    }
                    break;
                default:
                    break;
            }
            if (mLvLabel.size() < 7) {
                mManagerUser.setOrientation(GridLayoutManager.VERTICAL);
                mManagerUser.setSpanCount(3);
            } else {
                mManagerUser.setOrientation(GridLayoutManager.HORIZONTAL);
                mManagerUser.setSpanCount(2);
            }
            mRvLabel.setLayoutManager(mManagerUser);
            mAdapterSystem.notifyDataSetChanged();
            getPaperData();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            PaperMainBean bean = (PaperMainBean) baseBean;
            if (page == 0) {
                mLvPaper.clear();
            }
            for (PaperMainVO vo :
                    bean.getList()) {
                ClickEntity en = new ClickEntity();
                en.setPaperMainVO(vo);
                mLvPaper.add(en);
            }
            if (page == 0) {
                mItemClickAdapter.setNewData(mLvPaper);
            } else {
                mItemClickAdapter.addData(mLvPaper);
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PaperMainVO vo = mItemClickAdapter.getItem(position).getPaperMainVO();
        PaperDetailsActivity.actionActivityNew(getContext(), vo.getId(),
                "1", "online_paper", vo.getMyLanguage(), getActivity().getClass().getName());
    }

    @Override
    public void onRefresh() {
        page = 0;
        getPaperData();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getPaperData();
    }

    @Override
    public void listClick(String type, String pid, String version, String language) {
        HashMap<String, Object> map = new HashMap<>();
        switch (type) {
            case "collect"://收藏
                map.put("uid", getUserID());
                map.put("pid", pid);
                map.put("picid", "");
                map.put("type", "2");
                map.put("version", version);
                map.put("language", getLanguage());
                presenter.getDataAll("213", map);
                break;
            case "nocollect"://取消收藏
                map.put("uid", getUserID());
                map.put("pid", pid);
                map.put("picid", "");
                map.put("type", "2");
                map.put("version", version);
                map.put("language", getLanguage());
                presenter.getDataAll("217", map);
                break;
            case "download"://下载
                map.put("uid", getUserID());
                map.put("pid", pid);
                map.put("version", version);
                map.put("language", language);
                map.put("type", "2");
                presenter.getDataAll("216", map);
                break;
            case "add"://订阅
                ToastUtils.showShort("测试-添加");
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void start(DownloadInfoBean infoBean) {
        ArrayList<DownloadBean> list = new ArrayList<>();
        for (DownloadImgInfoVO vo : infoBean.getPics()) {
            String imgPath = BASE_IMG_URL + vo.getPicurl();
            String mp3Path = BASE_IMG_URL + vo.getRurl();
            if (DataBaseHelper.getSingleton(context).recordNotExists(imgPath)) {
                DownloadBean downloadBean = new DownloadBean
                        .Builder(imgPath)
                        .setSaveName(Utils.fileName(imgPath))//文件名
                        .setExtra1(infoBean.getId())//论文id
                        .setExtra2(vo.getPicid())//录音id
                        .setExtra3(infoBean.getType())//类型1论文2综述
                        .setExtra4("img")//格式
                        .setTitle(infoBean.getTitle())
                        .setExtra5(infoBean.getLanguage())
                        .setTime(infoBean.getDowntime())
                        .setVersion(infoBean.getVersion())
                        .setPhotoNum(infoBean.getPics().size())
                        .build();
                list.add(downloadBean);
            }
            if (DataBaseHelper.getSingleton(context).recordNotExists(mp3Path)) {
                DownloadBean downloadBean = new DownloadBean
                        .Builder(mp3Path)
                        .setSaveName(Utils.fileName(mp3Path))//文件名
                        .setExtra1(infoBean.getId())//论文id
                        .setExtra2(vo.getRid())//录音id
                        .setExtra3(infoBean.getType())//类型1论文2综述
                        .setExtra4("mp3")//格式
                        .setExtra5(infoBean.getLanguage())//语言
                        .setTitle(infoBean.getTitle())
                        .setTime(infoBean.getDowntime())
                        .setVersion(infoBean.getVersion())
                        .build();
                list.add(downloadBean);
            }
        }
        if (list.size() > 0) {
            new RxPermissions(getActivity())
                    .request(WRITE_EXTERNAL_STORAGE)
                    .doOnNext(granted -> {
                        if (!granted) {
                            throw new RuntimeException("no permission");
                        }
                    })
                    .compose(mRxDownload.transformMulti(list, infoBean.getId()))
                    .subscribe(o -> Utils.showToast("下载开始"), throwable -> {
                        android.util.Log.w("--->>", throwable);
                        Utils.showToast("下载中");
                    });
        } else {
            Utils.showToast("下载完成");
        }
    }
}
