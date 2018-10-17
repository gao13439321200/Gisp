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

public class SubscribeNewFragment extends BaseMvpFragment<BaseImpl, WholePresenter>
        implements MyRecyclerAdapter.OnMyItemClick,
        BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        ListItemClick,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_major)
    RecyclerView mRvMajor;
    @BindView(R.id.rv_people)
    RecyclerView mRvPeople;
    @BindView(R.id.rv_word)
    RecyclerView mRvWord;
    @BindView(R.id.rv_paper)
    RecyclerView mRvPaper;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<ClickEntity> mLvMajor = new ArrayList<>();
    private List<ClickEntity> mLvPeople = new ArrayList<>();
    private List<ClickEntity> mLvWord = new ArrayList<>();
    private List<ClickEntity> mLvPaper = new ArrayList<>();
    private ItemClickAdapter mItemClickAdapter;
    private MyRecyclerAdapter mAdapterMajor;
    private MyRecyclerAdapter mAdapterPeople;
    private MyRecyclerAdapter mAdapterWord;
    private GridLayoutManager mManagerMajor;
    private GridLayoutManager mManagerPeople;
    private GridLayoutManager mManagerWord;
    private String type;
    private int page = 0;
    private List<String> selectMajorIds = new ArrayList<>();
    private List<String> selectPeopleIds = new ArrayList<>();
    private List<String> selectWordIds = new ArrayList<>();
    private List<String> allMajorIDs = new ArrayList<>();
    private List<String> allPeopleIDs = new ArrayList<>();
    private List<String> allWordIDs = new ArrayList<>();
    private boolean majorFinish = false;
    private boolean wordFinish = false;
    private boolean peopleFinish = false;
    private RxDownload mRxDownload;

    public static SubscribeNewFragment newInstance() {
        Bundle args = new Bundle();
        SubscribeNewFragment fragment = new SubscribeNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_subscribe;
    }

    @Override
    public void initView() {
//        type = getArguments().getString("type");
        //专业
        mAdapterMajor = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mLvMajor, 3, (type, id) -> {
            if (selectMajorIds.contains(id)) {
                selectMajorIds.remove(id);
            } else {
                selectMajorIds.add(id);
            }
            getPaperData();
        });
        mManagerMajor = new GridLayoutManager(getActivity(), 1);
        mManagerMajor.setOrientation(GridLayoutManager.HORIZONTAL);
        mRvMajor.setLayoutManager(mManagerMajor);
        mRvMajor.setAdapter(mAdapterMajor);

        //学者
        mAdapterPeople = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mLvPeople, 3, (type, id) -> {
            if (selectPeopleIds.contains(id)) {
                selectPeopleIds.remove(id);
            } else {
                selectPeopleIds.add(id);
            }
            getPaperData();
        });
        mManagerPeople = new GridLayoutManager(getActivity(), 1);
        mManagerPeople.setOrientation(GridLayoutManager.HORIZONTAL);
        mRvPeople.setLayoutManager(mManagerPeople);
        mRvPeople.setAdapter(mAdapterPeople);

        //关键字
        mAdapterWord = new MyRecyclerAdapter(R.layout.tag_select_item_layout,
                mLvWord, 3, (type, id) -> {
            if (selectWordIds.contains(id)) {
                selectWordIds.remove(id);
            } else {
                selectWordIds.add(id);
            }
            getPaperData();
        });
        mManagerWord = new GridLayoutManager(getActivity(), 1);
        mManagerWord.setOrientation(GridLayoutManager.HORIZONTAL);
        mRvWord.setLayoutManager(mManagerWord);
        mRvWord.setAdapter(mAdapterWord);


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
        presenter.getDataAll("115", map);//专业115,关键字113,学者117
        presenter.getDataAll("113", map);//专业115,关键字113,学者117
        presenter.getDataAll("117", map);//专业115,关键字113,学者117
    }

    @Override
    public void myItemClick(int type, String id) {

        if (selectMajorIds.contains(id)) {
            selectMajorIds.remove(id);
        } else {
            selectMajorIds.add(id);
        }
        getPaperData();

    }

    private void getPaperData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("pageno", page);
        if (selectWordIds.size() == 0 && selectMajorIds.size() == 0 && selectPeopleIds.size() == 0) {
            map.put("aid", getLabelString(allWordIDs));
            map.put("mid", getLabelString(allMajorIDs));
            map.put("auid", getLabelString(allPeopleIDs));
        } else {
            map.put("aid", getLabelString(selectWordIds));
            map.put("mid", getLabelString(selectMajorIds));
            map.put("auid", getLabelString(selectPeopleIds));
        }
        presenter.getDataAll("219", map);
    }

    private static String getLabelString(List<String> selectIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String mId : selectIds) {
            stringBuilder.append(mId);
            stringBuilder.append("#");
        }
        String sId = "";
        if (stringBuilder.length() > 1)
            sId = stringBuilder.substring(0, stringBuilder.length() - 1);
        return sId;
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
                break;
            case "217":
                ToastUtils.showShort("取消收藏成功！");
                break;
            case "216":
                DownloadInfoBean infoBean = (DownloadInfoBean) baseBean;
                if (infoBean == null) {
                    ToastUtils.showShort("下载信息异常，请重试");
                    break;
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
                break;
            case "219"://获取论文列表
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
                break;
            case "113"://专业115,关键字113,学者117
                wordFinish = true;
                WordBean bean1 = (WordBean) baseBean;
                for (WordVO vo : bean1.getAlist()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setWordVO(vo);
                    mLvWord.add(entity1);
                    allWordIDs.add(vo.getId());
                }
                mAdapterWord.notifyDataSetChanged();
                if (wordFinish && majorFinish && peopleFinish)
                    getPaperData();
                break;
            case "115"://专业115,关键字113,学者117
                //mLvMajor.clear();
                //mAdapterMajor.clearSelectIds();
                //allMajorIDs.clear();
                majorFinish = true;
                MajorBean bean2 = (MajorBean) baseBean;
                for (MajorVO vo : bean2.getMajors()) {
                    ClickEntity entity1 = new ClickEntity();
                    WordVO vo1 = new WordVO();
                    vo1.setId(vo.getId());
                    vo1.setAntistop(vo.getName());
                    entity1.setWordVO(vo1);
                    mLvMajor.add(entity1);
                    allMajorIDs.add(vo.getId());
                }
                mAdapterMajor.notifyDataSetChanged();
                if (wordFinish && majorFinish && peopleFinish)
                    getPaperData();
                break;
            case "117"://专业115,关键字113,学者117
                peopleFinish = true;
                PeopleBean peopleBean1 = (PeopleBean) baseBean;
                for (PeopleVO vo : peopleBean1.getUlist()) {
                    ClickEntity entity1 = new ClickEntity();
                    WordVO vo1 = new WordVO();
                    vo1.setId(vo.getId());
                    vo1.setAntistop(vo.getName());
                    entity1.setWordVO(vo1);
                    mLvPeople.add(entity1);
                    allPeopleIDs.add(vo.getId());
                }
                mAdapterPeople.notifyDataSetChanged();
                if (wordFinish && majorFinish && peopleFinish)
                    getPaperData();
                break;
            default:
                break;
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
