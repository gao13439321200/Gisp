package com.giiisp.giiisp.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.base.BaseApp;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.CourseBean;
import com.giiisp.giiisp.dto.CourseVO;
import com.giiisp.giiisp.dto.DownloadImgInfoVO;
import com.giiisp.giiisp.dto.DownloadInfoBean;
import com.giiisp.giiisp.dto.DubbingListBean;
import com.giiisp.giiisp.dto.DubbingListVO;
import com.giiisp.giiisp.dto.FansBean;
import com.giiisp.giiisp.dto.FansVO;
import com.giiisp.giiisp.dto.FollowBean;
import com.giiisp.giiisp.dto.FollowVO;
import com.giiisp.giiisp.dto.MIneInfoBean;
import com.giiisp.giiisp.dto.MyAnswerBean;
import com.giiisp.giiisp.dto.MyAnswerVO;
import com.giiisp.giiisp.dto.PaperBean;
import com.giiisp.giiisp.dto.PaperLiteratureBean;
import com.giiisp.giiisp.dto.PaperLiteratureVO;
import com.giiisp.giiisp.dto.PaperMainBean;
import com.giiisp.giiisp.dto.PaperMainVO;
import com.giiisp.giiisp.dto.PaperQaBean;
import com.giiisp.giiisp.dto.PaperQaVO;
import com.giiisp.giiisp.dto.PeopleBean;
import com.giiisp.giiisp.dto.PeopleVO;
import com.giiisp.giiisp.dto.PlayNoteBean;
import com.giiisp.giiisp.dto.PlayNoteVo;
import com.giiisp.giiisp.entity.APPConstants;
import com.giiisp.giiisp.entity.AnswerBean;
import com.giiisp.giiisp.entity.AnswerEntity;
import com.giiisp.giiisp.entity.AnswerQUizXBean;
import com.giiisp.giiisp.entity.AnswerQuizRowsBean;
import com.giiisp.giiisp.entity.AntistopEntity;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.DaoSession;
import com.giiisp.giiisp.entity.DownloadController;
import com.giiisp.giiisp.entity.FansConcernedEntity;
import com.giiisp.giiisp.entity.HomeSearchEntity;
import com.giiisp.giiisp.entity.LiteratureEntity;
import com.giiisp.giiisp.entity.MsgEntity;
import com.giiisp.giiisp.entity.MyScholarBean;
import com.giiisp.giiisp.entity.Note;
import com.giiisp.giiisp.entity.NoteDao;
import com.giiisp.giiisp.entity.QAEntity;
import com.giiisp.giiisp.entity.QuizBean;
import com.giiisp.giiisp.entity.QuizEntity;
import com.giiisp.giiisp.entity.ScholarEntity;
import com.giiisp.giiisp.entity.SearchHistoryEntity;
import com.giiisp.giiisp.entity.SubscribeEntity;
import com.giiisp.giiisp.entity.UserInfoEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.Log;
import com.giiisp.giiisp.utils.PackageUtil;
import com.giiisp.giiisp.utils.RxBus;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.DubbingActivity;
import com.giiisp.giiisp.view.activity.FragmentActivity;
import com.giiisp.giiisp.view.activity.LogInActivity;
import com.giiisp.giiisp.view.activity.PaperDetailsActivity;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ExpandableItemAdapter;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.view.adapter.ItemDragAdapter;
import com.giiisp.giiisp.view.adapter.MultipleItemQuickAdapter;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.giiisp.giiisp.widget.CustomSpinner;
import com.giiisp.giiisp.widget.recording.Util;
import com.shuyu.waveview.AudioPlayer;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;
import static com.giiisp.giiisp.base.BaseActivity.uid;
import static com.giiisp.giiisp.view.activity.PaperDetailsActivity.CN;
import static com.giiisp.giiisp.view.activity.PaperDetailsActivity.EN;

/**
 * 众多Fragment的基类
 * Created by Thinkpad on 2017/5/19.
 */

public class BannerRecyclerViewFragment extends BaseMvpFragment<BaseImpl, WholePresenter>
        implements BaseImpl, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemLongClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, ListItemClick {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_rt)
    TextView tvTitleRt;
    @BindView(R.id.line_banner)
    FrameLayout lineBanner;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_back_rt)
    TextView tvBackRt;
    @BindView(R.id.tv_download_number)
    TextView tvDownloadNumber;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rl_banner)
    RelativeLayout rlBanner;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_news_spot)
    ImageView tvNewsSpot;
    @BindView(R.id.tv_home_news)
    ImageView tvHomeNews;
    @BindView(R.id.fl_news)
    FrameLayout flNews;
    @BindView(R.id.ll_spinner_all)
    LinearLayout mLLSpinnerAll;
    @BindView(R.id.ll_spinner1)
    LinearLayout mLLSpinner1;
    @BindView(R.id.ll_spinner2)
    LinearLayout mLLSpinner2;
    ArrayList<ClickEntity> list = new ArrayList<>();
    ItemClickAdapter itemClickAdapter = null;
    private ArrayList<String> listSearch = new ArrayList<>();
    private RxDownload mRxDownload;
    private ExpandableItemAdapter expandableItemAdapter;
    private ItemDragAdapter mDragAdapter;
    private MultipleItemQuickAdapter multipleItemQuickAdapter;
    private String string;
    private String imageId = "";
    int downloadNunber;
    private ExpandableItemAdapter dubbingAdapter;
    private int page = 0;
    protected AudioPlayer audioPlayer;
    private boolean mIsPlay;
    private boolean isPause;
    private String filePath = "";
    private String filePathTwo = "";
    private String searchContent;
    private TextView tvVoice;
    private CustomSpinner mSpinnerSubject;
    private CustomSpinner mSpinnerSubject2;

    private int isSave = -1;
    private int changePosition;
    private int version = -1;
    private int pageSize = 20;//每页的数据个数

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public ItemClickAdapter getItemClickAdapter() {
        return itemClickAdapter;
    }

    public void setItemClickAdapter(ItemClickAdapter itemClickAdapter) {
        this.itemClickAdapter = itemClickAdapter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_banner_recyclerview;
    }

/*    public void setmDownloadController(DownloadController mDownloadController) {
        this.mDownloadController = mDownloadController;
    }*/

    public static BannerRecyclerViewFragment newInstance(String param1, String param2) {
        BannerRecyclerViewFragment fragment = new BannerRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString("1005", param1);
        args.putString("1006", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void newRxBus(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put(APPConstants.MyBus.TO, TAG);
        map.put("type", type);
        RxBus.getInstance().send(map);
    }


    @SuppressLint("CheckResult")
    public void loadDownloadNunber() {
        RxDownload.getInstance(getContext()).getTotalDownloadRecords()
                .map(new Function<List<DownloadRecord>, List<String>>() {
                    @Override
                    public List<String> apply(List<DownloadRecord> downloadRecords) throws Exception {
                        List<String> missionIds = new ArrayList<>();
                        for (DownloadRecord each : downloadRecords) {
                            if (each.getFlag() != DownloadFlag.COMPLETED && each.getExtra1() != null && !missionIds.contains(each.getExtra1())) {
                                missionIds.add(each.getExtra1());
                            }
                        }
                        return missionIds;
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> downloadBeen) throws Exception {
                        downloadNunber = downloadBeen.size();
                        if (downloadNunber != 0) {
                            tvDownloadNumber.setText("" + downloadNunber);
                        } else {
                            tvDownloadNumber.setText("");
                        }
                        if (type.equals("download")) {
                            if (getParentFragment() != null && getParentFragment() instanceof CollectionDownloadFragment) {
                                if (downloadNunber != 0) {
                                    ((CollectionDownloadFragment) getParentFragment()).setUpTabBadge("" + downloadNunber);
                                } else {
                                    ((CollectionDownloadFragment) getParentFragment()).setUpTabBadge("");
                                }
                            }
                        }
                        Log.i("--->>11", "loadDownloadNunber: " + downloadNunber);
                    }
                });
        Log.i("--->>22", "loadDownloadNunber: " + downloadNunber);

    }

    @SuppressLint("CheckResult")
    public void loadDownloadData() {
//        ArrayList<ClickEntity> res0 = new ArrayList<>();
//        ArrayList<ClickEntity> res1 = new ArrayList<>();
//        ArrayList<ClickEntity> res2 = new ArrayList<>();
//        List<String> missionIdAll = RxDownload.getInstance(getContext()).getMissionIdAll(getContext());

        //读取所有的记录，然后分类
        RxDownload.getInstance(getContext()).getTotalDownloadRecords()
                .map((Function<List<DownloadRecord>, List<ClickEntity>>) downloadRecords -> {
                    List<String> missionIds = new ArrayList<>();//任务id
                    List<String> missionIdDownloads = new ArrayList<>();//正在下载的任务id
                    List<String> paperIds = new ArrayList<>();//录音id
                    ArrayList<ClickEntity> res_missionIds = new ArrayList<>();
                    ArrayList<ClickEntity> res_paperIds = new ArrayList<>();
                    ArrayList<ClickEntity> res_all = new ArrayList<>();
                    ArrayList<ClickEntity> downloads = new ArrayList<>();

                    for (DownloadRecord each : downloadRecords) {

                        if (each.getExtra1() != null && !missionIds.contains(each.getExtra1())) {
                            missionIds.add(each.getExtra1());
                            res_missionIds.add(setNewClickEntity(each, 0));
                        }
                        if (each.getExtra1() != null && !missionIdDownloads.contains(each.getExtra1())) {
                            missionIdDownloads.add(each.getExtra1());
                            downloads.add(setNewClickEntity(each, 0));
                        }

                        if (each.getExtra2() != null && !paperIds.contains(each.getExtra2())) {
                            paperIds.add(each.getExtra2());
                            res_paperIds.add(setNewClickEntity(each, 1));
                        }
                        ClickEntity lv2 = new ClickEntity(2);
                        lv2.setString(each.getExtra2());
                        lv2.setUrl(each.getExtra1());
                        lv2.record = each;
                        lv2.setPhotoRecord(each.getExtra4());
                        lv2.setPhotoOrder(each.getExtra3());
                        lv2.setPaperId(each.getExtra1());
                        lv2.setItemType(2);
                        lv2.setLanguage(each.getExtra5());
                        lv2.setVersion(each.getVersion());
                        lv2.setAdapter(expandableItemAdapter);
                        lv2.setTitle(each.getTitle());
                        //                            lv2.setActivity((BaseActivity) getActivity());
                        res_all.add(lv2);

                    }

                    for (ClickEntity clickEntity : res_all) {
                        for (ClickEntity entity : res_paperIds) {
                            if (Objects.equals(entity.getString(), clickEntity.getString())) {
                                entity.addSubItem(clickEntity);
                                clickEntity.setClickEntity(entity);

                            }
                        }
                    }
                    for (ClickEntity entity : res_paperIds) {
                        for (ClickEntity clickEntitys : res_missionIds) {
                            if (Objects.equals(clickEntitys.getPaperId(), entity.getPaperId())) {
                                if (entity.getSubItems() != null) {
                                    List<ClickEntity> subItems = entity.getSubItems();
                                    boolean isDownload = true;
                                    for (ClickEntity subItem : subItems) {
                                        if (subItem.record.getFlag() != DownloadFlag.COMPLETED) {
                                            isDownload = false;
                                            break;
                                        }
                                    }

                                    if (isDownload) {
                                        clickEntitys.addSubItem(entity);
                                    }
                                }
                            }
                        }
                        for (ClickEntity clickEntitys : downloads) {
                            if (Objects.equals(clickEntitys.getPaperId(), entity.getPaperId())) {
                                if (entity.getSubItems() != null) {
                                    List<ClickEntity> subItems = entity.getSubItems();
                                    boolean isDownload = true;
                                    for (ClickEntity subItem : subItems) {
                                        if (subItem.record.getFlag() != DownloadFlag.COMPLETED) {
                                            isDownload = false;
                                            break;
                                        }
                                    }

                                    if (!isDownload) {
                                        clickEntitys.addSubItem(entity);
                                    }
                                }
                            }
                        }
                    }
                    Log.i("--->>", "apply:1 " + Thread.currentThread().getName() + tvDownloadNumber.getText() + type);

                    switch (type) {
                        case "paper_download":
                        case "summary_download":
                            return res_missionIds;
                        case "download":
                        case "summary_list":

                            return downloads;
                    }
                    Log.i("--->", "apply: " + res_missionIds.size());
                    return res_missionIds;
                })
                .subscribe(downloadBeen -> {
                    swipeRefreshLayout.setRefreshing(false);
                    switch (type) {
                        case "paper_download":
                            mDragAdapter.setNewData(null);
                            List<ClickEntity> entities = new ArrayList<>();
                            for (ClickEntity clickEntity : downloadBeen) {
                                List<ClickEntity> subItems = clickEntity.getSubItems();
                                if (subItems != null && subItems.size() > 0 && "1".equals(subItems.get(0).getPaperType())) {
                                    entities.add(clickEntity);
                                }
                            }
                            mDragAdapter.addData(entities);
                            break;
                        case "summary_download":
                            mDragAdapter.setNewData(null);
                            List<ClickEntity> entities2 = new ArrayList<>();
                            for (ClickEntity clickEntity : downloadBeen) {
                                List<ClickEntity> subItems = clickEntity.getSubItems();
                                if (subItems != null && subItems.size() > 0 && "2".equals(subItems.get(0).getPaperType())) {
                                    entities2.add(clickEntity);
                                }
                            }
                            mDragAdapter.addData(entities2);
                            break;
                        case "download":
                            expandableItemAdapter.setNewData(null);
                            for (ClickEntity clickEntity : downloadBeen) {
                                List<ClickEntity> subItems = clickEntity.getSubItems();
                                if (subItems != null && subItems.size() > 0) {
                                    expandableItemAdapter.addData(clickEntity);
                                }
                            }
                            expandableItemAdapter.expandAll();
                            break;

                        default:
                            Log.i("--->>", "apply:2 " + Thread.currentThread().getName());
                            break;
                    }
                });
    }

    @android.support.annotation.NonNull
    private ClickEntity setNewClickEntity(DownloadRecord each, int type) {
        ClickEntity lv0 = new ClickEntity(type);
        lv0.setString(each.getExtra2());
        lv0.setUrl(each.getUrl());
        lv0.setPaperId(each.getExtra1());
        lv0.setItemType(type);
        lv0.setPaperType(each.getExtra3());
        lv0.setLanguage(each.getExtra5());
        lv0.setTime(each.getTime());
        lv0.setTitle(each.getTitle());
        lv0.setPhotoRecord(each.getExtra4());
        lv0.setVersion(each.getVersion());
        lv0.setPhotoNumber(each.getPhotoNum());
        lv0.record = each;
        return lv0;
    }

    OnItemDragListener listener = new OnItemDragListener() {
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            Log.d(TAG, "drag start");
            BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            //                holder.setTextColor(R.id.tv, Color.WHITE);
        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            Log.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            Log.d(TAG, "drag end");
            BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            //                holder.setTextColor(R.id.tv, Color.BLACK);
        }
    };
    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            Log.d(TAG, "view swiped start: " + pos);
            BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            //                holder.setTextColor(R.id.tv, Color.WHITE);
        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            Log.d(TAG, "View reset: " + pos);
            BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            //                holder.setTextColor(R.id.tv, Color.BLACK);
        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
            Log.d(TAG, "View Swiped: " + pos);
            ClickEntity item = mDragAdapter.getItem(pos);
            if (item != null) {
                final String url = mDragAdapter.getItem(pos).getPaperId();


                mRxDownload.deleteAll(url, true).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Utils.showToast(url);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
                List<ClickEntity> subItems = item.getSubItems();
                if (subItems != null && subItems.size() > 0) {
                    for (ClickEntity subItem : subItems) {

                        mRxDownload.deleteAll(subItem.getString(), true).subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                //                                Utils.showToast(url);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
            canvas.drawColor(ContextCompat.getColor(getActivity(), R.color.colorBackground));
            //                canvas.drawText("Just some text", 0, 40, paint);
        }
    };

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        RxBus.getInstance().toObservable(Map.class).subscribe(map -> {
            if (TAG.equals(map.get(APPConstants.MyBus.TO))) {
                switch ((String) map.get("type")) {
                    case "play":
                        if (itemClickAdapter != null)
                            itemClickAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAuxiliary);
        swipeRefreshLayout.setOnRefreshListener(this);
        //        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setRefreshing(true);
        mRxDownload = RxDownload.getInstance(getContext());

        View notDataView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) recyclerView.getParent(), false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        switch (type) {
            case "he":

                lineBanner.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.scholars_details_page);
                list.clear();


                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                multipleItemQuickAdapter = new MultipleItemQuickAdapter((BaseActivity) getActivity(), list);
                multipleItemQuickAdapter.setOnItemChildClickListener(this);
                multipleItemQuickAdapter.setOnItemClickListener(this);
                multipleItemQuickAdapter.setEmptyView(notDataView);
                recyclerView.setAdapter(multipleItemQuickAdapter);
                break;
            case "scholar_list"://学者
                tvTitle.setText(R.string.scholars);
                lineBanner.setVisibility(View.VISIBLE);
                GridLayoutManager grid = new GridLayoutManager(getContext(), 3);
                recyclerView.setLayoutManager(grid);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_scholar, null, type);
                recyclerView.setAdapter(itemClickAdapter);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "plays":

                lineBanner.setVisibility(View.VISIBLE);
                tvBack.setVisibility(View.VISIBLE);
//                ivMenu.setVisibility(View.VISIBLE);
//                ivMenu.setImageResource(R.mipmap.record_play_empty);
//                ivMenu.setOnClickListener(v -> {
//                    itemClickAdapter.setNewData(null);
//                    ToastUtils.showShort("清除成功！");
//                });
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_collection, this.list, type);
                tvTitle.setText(R.string.plays);
                itemClickAdapter.setOnItemLongClickListener(this);
                break;
            case "play":

                lineBanner.setVisibility(View.VISIBLE);
                tvBack.setVisibility(View.VISIBLE);
//                ivMenu.setVisibility(View.VISIBLE);
//                ivMenu.setImageResource(R.mipmap.record_play_empty);
//                ivMenu.setOnClickListener(v -> {
//                    itemClickAdapter.setNewData(null);
//                    ToastUtils.showShort("清除成功！");
//                });
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_collection, this.list, type);
                itemClickAdapter.setOnItemLongClickListener(this);
                if (Objects.equals("giiisp", string)) {
                    tvBack.setVisibility(View.GONE);
                }
                tvTitle.setText(R.string.play);
                break;
            case "course"://教程列表
                lineBanner.setVisibility(View.VISIBLE);
                tvBack.setVisibility(View.VISIBLE);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_course, this.list, type);
                itemClickAdapter.setOnItemLongClickListener(this);
                tvTitle.setText(R.string.course);
                break;
            case "paper_qa"://论文详情评论问答列表
                list.clear();
                swipeRefreshLayout.setEnabled(false);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_questions_answers_new, this.list, type);
                itemClickAdapter.setOnItemChildClickListener(this);
                initpaly();
                break;
            case "paper_literature"://论文详情文献
                swipeRefreshLayout.setEnabled(false);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper_indexes_new, this.list, type);
                break;
            case "paper_label":
                swipeRefreshLayout.setEnabled(false);
//                this.list.clear();
//                this.list.add(new ClickEntity(getString(R.string.technology_label)));
//                this.list.add(new ClickEntity(getString(R.string.mathematics_label)));
//                this.list.add(new ClickEntity(getString(R.string.data_labels)));
//                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper_label, this.list, type);

                break;
            case "summary_list"://综述
                tvTitleRt.setVisibility(View.VISIBLE);
                rlBanner.setVisibility(View.VISIBLE);
//                mLLSpinnerAll.setVisibility(View.VISIBLE);
                lineBanner.setVisibility(View.GONE);
                tvTitleRt.setText(R.string.review_list);
                tvBackRt.setVisibility(View.VISIBLE);
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                recyclerView.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                        PaperMainVO vo = itemClickAdapter.getData().get(position).getPaperMainVO();
                        ArrayList<String> arrayVersion = new ArrayList<>();
                        for (PaperMainVO.VlistBean bean : vo.getVlist()) {
                            switch (bean.getVersion()) {
                                case 2://完整
                                    arrayVersion.add("2");
                                    break;
                                case 3://摘要
                                    arrayVersion.add("3");
                                    break;
                                case 4://精华
                                    arrayVersion.add("4");
                                    break;
                            }
                        }
                        String version = arrayVersion.size() > 0 ? arrayVersion.get(0) : "1";
                        PaperDetailsActivity.actionActivityNew(context, vo.getId(), version,
                                "home", getLanguage(), getActivity().getClass().getName());
                    }
                });
                List<String> txt = new ArrayList<>();
                //一级菜单
                mSpinnerSubject = new CustomSpinner(getActivity(), "请选择", txt);
                mSpinnerSubject.setOnCustomItemCheckedListener(position -> {

                });
                mLLSpinner1.addView(mSpinnerSubject);
                //二级菜单
                mSpinnerSubject2 = new CustomSpinner(getActivity(), "请选择", txt);
                mSpinnerSubject2.setOnCustomItemCheckedListener(position -> {

                });
                mLLSpinner2.addView(mSpinnerSubject2);
                break;
            case "paper_list"://论文
                tvTitleRt.setVisibility(View.VISIBLE);
                tvBackRt.setVisibility(View.VISIBLE);
                tvTitleRt.setText(R.string.paper_list);
//                mLLSpinnerAll.setVisibility(View.VISIBLE);
                rlBanner.setVisibility(View.VISIBLE);
                lineBanner.setVisibility(View.GONE);
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
//                recyclerView.addOnItemTouchListener(new OnItemClickListener() {
//                    @Override
//                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        PaperMainVO vo = itemClickAdapter.getData().get(position).getPaperMainVO();
//                        ArrayList<String> arrayVersion = new ArrayList<>();
//                        for (PaperMainVO.VlistBean bean : vo.getVlist()) {
//                            switch (bean.getVersion()) {
//                                case 2://完整
//                                    arrayVersion.add("2");
//                                    break;
//                                case 3://摘要
//                                    arrayVersion.add("3");
//                                    break;
//                                case 4://精华
//                                    arrayVersion.add("4");
//                                    break;
//                            }
//                        }
//                        PaperDetailsActivity.actionActivity(context, vo.getId(), arrayVersion, "home");
//                    }
//                });
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                List<String> txt1 = new ArrayList<>();
                //一级菜单
                mSpinnerSubject = new CustomSpinner(getActivity(), "请选择", txt1);
                mSpinnerSubject.setOnCustomItemCheckedListener(position -> {

                });
                mLLSpinner1.addView(mSpinnerSubject);
                //二级菜单
                mSpinnerSubject2 = new CustomSpinner(getActivity(), "请选择", txt1);
                mSpinnerSubject2.setOnCustomItemCheckedListener(position -> {

                });
                mLLSpinner2.addView(mSpinnerSubject2);
                break;
            case "paper_download"://论文下载
            case "summary_download"://综述下载
                list.clear();
                mDragAdapter = new ItemDragAdapter((BaseActivity) getActivity(), list);
                //                ItemDragAndSwipeCallback mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mDragAdapter);
                //                ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
                //                mItemTouchHelper.attachToRecyclerView(recyclerView);
                //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
                //                mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
                //                mDragAdapter.enableSwipeItem();
                //                mDragAdapter.setOnItemSwipeListener(onItemSwipeListener);
                //                mDragAdapter.enableDragItem(mItemTouchHelper);
                //                mDragAdapter.setOnItemDragListener(listener);
                mDragAdapter.setOnItemClickListener(this);
                mDragAdapter.setOnItemLongClickListener(this);
                mDragAdapter.setEmptyView(notDataView);
                recyclerView.setAdapter(mDragAdapter);
                break;
            case "download"://下载中
                expandableItemAdapter = new ExpandableItemAdapter((BaseActivity) getActivity(),
                        this,
                        R.layout.item_download_finished,
                        R.layout.item_paper_page,
                        R.layout.item_download_progress,
                        list, type);
                recyclerView.setAdapter(expandableItemAdapter);
                expandableItemAdapter.setOnItemClickListener(this);
                expandableItemAdapter.setEmptyView(notDataView);
                llDownload.setVisibility(View.VISIBLE);
                break;
            case "my_paper":
                tvTitle.setText(R.string.my_paper_list);
                lineBanner.setVisibility(View.VISIBLE);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "my_review":
                tvTitle.setText(R.string.my_review_list);
                lineBanner.setVisibility(View.VISIBLE);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "collection_paper"://首页收藏论文
            case "collection_summary"://首页收藏综述
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();

//                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_collectionchild, this.list, type);
//                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
//                itemClickAdapter.setOnItemChildClickListener(this);
                break;
            case "popular":
                tvTitle.setText(R.string.play);
                tvBack.setVisibility(View.GONE);
                lineBanner.setVisibility(View.VISIBLE);
                DaoSession daoSession1 = BaseApp.app.getDaoSession();
                NoteDao noteDao2 = daoSession1.getNoteDao();
                Query<Note> notesQuery3 = noteDao2.queryBuilder().where(NoteDao.Properties.Type.eq("play")).orderAsc(NoteDao.Properties.Id).build();
                List<Note> listnote4 = notesQuery3.list();
                list.clear();
                for (int i = 0; i < listnote4.size(); i++) {
                    list.add(new ClickEntity(listnote4.get(i)));
                }
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_collection, this.list, type);
                break;
            case "wait_dubbing": // 待配音列表
                tvTitle.setText(R.string.voice_file_list);
//                ivMenu.setImageResource(R.mipmap.dubbing_refresh);
//                ivMenu.setVisibility(View.VISIBLE);
                list.clear();
                dubbingAdapter = new ExpandableItemAdapter((BaseActivity) getActivity(),
                        R.layout.item_title_dubbing, R.layout.item_waiting_dubbing, this.list, type);
                dubbingAdapter.setOnItemChildClickListener(this);
                dubbingAdapter.setOnItemClickListener(this);
                dubbingAdapter.setOnLoadMoreListener(this, recyclerView);
                dubbingAdapter.disableLoadMoreIfNotFullPage();
                dubbingAdapter.setEmptyView(notDataView);
                recyclerView.setAdapter(dubbingAdapter);
                lineBanner.setVisibility(View.VISIBLE);
                break;
            case "subscribe":
            case "newest":
                this.list.clear();
                tvTitleRt.setText(R.string.subscribe);
                rlBanner.setVisibility(View.VISIBLE);
                lineBanner.setVisibility(View.GONE);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "notice":
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_message_notification, this.list, type);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "interactive":
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_message_interaction, this.list, type);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "answer":
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_message_answers, this.list, type);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "questions":
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_message_questions, this.list, type);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "search_hint":
                swipeRefreshLayout.setEnabled(false);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_search, this.list, type);
                break;
            case "search_result":
                this.list.clear();
                swipeRefreshLayout.setEnabled(false);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_search_result, this.list, type);
                break;
            case "search_scholar"://搜索粉丝
                swipeRefreshLayout.setEnabled(false);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_search_scholar, this.list, type);
                break;
            case "mine_scholar"://我的粉丝
                lineBanner.setVisibility(View.VISIBLE);
                if (Objects.equals("" + uid, string)) {
                    tvTitle.setText(R.string.my_fans);
                } else {
                    tvTitle.setText(R.string.ta_fans);
                }
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_search_scholar, this.list, type);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "mine_follow"://我的关注
                lineBanner.setVisibility(View.VISIBLE);
                if (Objects.equals("" + uid, string)) {
                    tvTitle.setText(R.string.my_follow);
                } else {
                    tvTitle.setText(R.string.ta_follow);
                }
                list.clear();
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_search_scholar, this.list, type);
                itemClickAdapter.setOnLoadMoreListener(this, recyclerView);
                itemClickAdapter.disableLoadMoreIfNotFullPage();
                break;
            case "search_paper"://搜索论文
                swipeRefreshLayout.setEnabled(false);
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper_indexes, this.list, type);
                break;
            default:
                itemClickAdapter = new ItemClickAdapter((BaseActivity) getActivity(), R.layout.item_paper, this.list, type, this);

        }
        if (itemClickAdapter != null) {
            itemClickAdapter.setEmptyView(notDataView);
            itemClickAdapter.setOnItemChildClickListener(this);
            itemClickAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(itemClickAdapter);
        }
        //        if (itemClickAdapter != null)
        //            itemClickAdapter.disableLoadMoreIfNotFullPage();
        initNetwork();
        loadDownloadNunber();
    }

    /**
     * 网络请求数据
     */
    @Override
    public void initNetwork() {
        if (swipeRefreshLayout == null)
            return;
        ArrayMap<String, Object> map = new ArrayMap<>();
        HashMap<String, Object> hMap = new HashMap<>();
//        map.put("token", token);
        switch (type) {
            case "he":
                hMap.put("uid", uid);
                hMap.put("language", getLanguage());
                presenter.getDataAll("306", hMap);
                //这里需要获取作者论文和综述，暂时不用了
                break;
            case "course":
                hMap.put("pageno", page);
                presenter.getDataAll("321", hMap);
                break;
            case "scholar_list":
                hMap.put("pageno", page);
                presenter.getDataAll("218", hMap);
                break;
//            case "plays":

//                DaoSession daoSessions = BaseApp.app.getDaoSession();
//                NoteDao noteDaos = daoSessions.getNoteDao();
//                Query<Note> notesQuerys = noteDaos.queryBuilder().where(NoteDao.Properties.Type.eq("plays")).orderDesc(NoteDao.Properties.Time).build();
//                list.clear();
//                List<Note> listnotes = notesQuerys.list();
//                for (int i = 0; i < listnotes.size(); i++) {
//                    list.add(new ClickEntity(listnotes.get(i)));
//                }
//                itemClickAdapter.setNewData(list);
//                swipeRefreshLayout.setRefreshing(false);
//                break;
            case "plays":
            case "play":
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
//                hMap.put("version",version);
                presenter.getDataAll("214", hMap);

//                DaoSession daoSession = BaseApp.app.getDaoSession();
//                NoteDao noteDao = daoSession.getNoteDao();
//                Query<Note> notesQuery = noteDao.queryBuilder().where(NoteDao.Properties.Type.eq("play")).orderDesc(NoteDao.Properties.Time).build();
//                List<Note> listnote = notesQuery.list();
//                list.clear();
//                for (int i = 0; i < listnote.size(); i++) {
//                    list.add(new ClickEntity(listnote.get(i)));
//                }
//                itemClickAdapter.setNewData(list);
//                swipeRefreshLayout.setRefreshing(false);
                break;
            case "paper_qa"://论文详情评论问答
                if (TextUtils.isEmpty(imageId))
                    return;
                hMap.put("pid", string);
                hMap.put("imgid", imageId);
                hMap.put("pageno", page);
                presenter.getDataAll("206", hMap);


//                map.put("id", imageId);
//                if (swipeRefreshLayout != null) {
//                    presenter.getListOfQuizAndAnswerData(map);
//                }
                break;
            case "paper_literature"://论文详情文献
                hMap.put("pid", string);
                presenter.getDataAll("208", hMap);
                break;
            case "paper_label":
                map.put("id", string);
                presenter.getListOfAntistopData(map);
                break;
            case "summary_list"://首页综述列表
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", 2);
                presenter.getDataAll("209", hMap);
                break;
            case "paper_list"://首页论文列表
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", 1);
                presenter.getDataAll("209", hMap);
                break;
            case "paper_download":
            case "summary_download":
                loadDownloadData();
                break;
            case "download":
                loadDownloadData();
                break;
            case "my_paper"://我的论文
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", "1");
                presenter.getDataAll("312", hMap);
                break;
            case "my_review"://我的综述
//                map.put("uid", uid);
//                map.put("page", page);
//
//                map.put("isOneOrTwo", 2);
//                presenter.getListNewPaperData(map);
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", "2");
                presenter.getDataAll("312", hMap);

                break;
            case "collection_paper":
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", 1);
                hMap.put("ftype", 1);
                presenter.getDataAll("212", hMap);

//                map.put("uid", uid);
//                map.put("page", page);
//                //                map.put("upTime", "asc");
//                map.put("isOneOrTwo", 1);
//                presenter.getListFollowedPaperData(map);
                break;
            case "collection_summary":
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", 2);
                hMap.put("ftype", 1);
                presenter.getDataAll("212", hMap);
//                map.put("uid", uid);
//                map.put("page", page);
//                //                map.put("upTime", "asc");
//                map.put("isOneOrTwo", 2);
//                presenter.getListFollowedPaperData(map);
                break;
            case "popular":
                break;
            case "wait_dubbing"://待配音列表
//                map.put("uid", uid);
//                map.put("page", page);
//                presenter.getWaitRecordPaperListData(map);
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                presenter.getDataAll("316", hMap);
                break;
            case "subscribe"://订阅
            case "newest":
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                hMap.put("type", 2);
                hMap.put("ftype", 2);
                presenter.getDataAll("212", hMap);
                break;
            case "notice":
                map.put("uid", uid);
                map.put("page", page);
                map.put("type", 1);
                presenter.getMsgListData(map);
                break;
            case "interactive":
                map.put("uid", uid);
                map.put("page", page);
                map.put("type", 2);
                presenter.getMsgListData(map);
                break;
            case "answer":
//                map.put("page", page);
//                map.put("uid", uid);
//                presenter.getListAnswerData(map);
                hMap.put("pageno", page);
                hMap.put("uid", getUserID());
                presenter.getDataAll("314", hMap);
                break;
            case "questions":
//                map.put("uid", uid);
//                map.put("page", page);
//                presenter.getListQuizData(map);
                hMap.put("pageno", page);
                hMap.put("uid", getUserID());
                presenter.getDataAll("313", hMap);
                break;
            case "search_hint":
                map.put("uid", uid);
                map.put("page", page);
                presenter.getSearchHistoryData(map);
                break;
            case "search_result":
                break;
            case "search_scholar":
                break;
            case "mine_scholar":
//                map.put("page", page);
//                map.put("uid", string);
//                presenter.getListUserFollowedData(map);
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                presenter.getDataAll("307", hMap);
                break;
            case "mine_follow":
//                map.put("page", page);
//                map.put("uid", string);
//                presenter.getListUserFollowData(map);
                hMap.put("uid", getUserID());
                hMap.put("pageno", page);
                presenter.getDataAll("308", hMap);
                break;
            case "search_paper":
                break;
            default:

        }

    }

    @SuppressLint("CheckResult")
    private void start(String url) {
        new RxPermissions(getActivity())
                .request(WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .compose(mRxDownload.<Boolean>transformService(url))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Utils.showToast(R.string.download_begins);
                    }
                });

    }

    private void pause(String url) {
        mRxDownload.pauseServiceDownload(url).subscribe();
    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() == null) {
            throw new NullPointerException("Arguments is null!!!");
        }
        type = getArguments().getString("1005");
        string = getArguments().getString("1006");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe
    public void onMessageSearch(String userInfoEntity) {
        if (!TextUtils.isEmpty(userInfoEntity))
            searchContent = userInfoEntity;
        if (Objects.equals(type, "search_result")) {
            ArrayMap<String, Object> map = new ArrayMap<>();
//            map.put("token", token);
            map.put("uid", uid);
            map.put("content", searchContent);
            presenter.getHomeSearchData(map);
        } else if (Objects.equals(type, "search_hint")) {
            initNetwork();
        }
    }

    @Subscribe
    public void onMessage(int userInfoEntity) {
        Log.i(TAG, "onMessage: download");
        if (type.equals("download")) {
            loadDownloadData();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (type) {
            case "summary_list":
            case "paper_list":
            case "paper_download":
            case "summary_download":
            case "download":
            case "collection_paper":
            case "collection_summary":
            case "subscribe":
            case "search_paper":
                loadDownloadNunber();
                break;
            default:

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (expandableItemAdapter != null) {
            List<ClickEntity> list = expandableItemAdapter.getData();
            for (ClickEntity each : list) {
                zlc.season.rxdownload2.function.Utils.dispose(each.disposable);
            }
        }
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @OnClick({R.id.tv_back, R.id.fl_menu, R.id.fl_news, R.id.tv_back_rt, R.id.iv_play, R.id.iv_download, R.id.tv_all_start, R.id.tv_all_suspended})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all_start:
                if (expandableItemAdapter == null)
                    return;
                List<ClickEntity> data = expandableItemAdapter.getData();
                if (data != null && data.size() > 0) {
                    for (ClickEntity clickEntity : data) {
                        List<ClickEntity> subItems = clickEntity.getSubItems();
                        if (subItems != null && subItems.size() > 0) {
                            for (ClickEntity subItem : subItems) {
                                List<ClickEntity> items = subItem.getSubItems();
                                if (items != null && items.size() > 0) {
                                    for (ClickEntity item : items) {
                                        if (item != null && item.record != null && item.record.getUrl() != null) {
                                            mRxDownload.serviceDownload(item.record.getUrl())
                                                    .subscribe(new Consumer<Object>() {
                                                        @Override
                                                        public void accept(Object o) throws Exception {

                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {
                                                            Log.d("--->>", throwable.toString());
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Log.i("--->>", "onViewClicked: startAll");
                break;
            case R.id.tv_all_suspended:
                if (expandableItemAdapter == null)
                    return;
                List<ClickEntity> aata = expandableItemAdapter.getData();
                if (aata != null && aata.size() > 0) {
                    for (ClickEntity clickEntity : aata) {
                        List<ClickEntity> subItems = clickEntity.getSubItems();
                        if (subItems != null && subItems.size() > 0) {
                            for (ClickEntity subItem : subItems) {
                                List<ClickEntity> items = subItem.getSubItems();
                                if (items != null && items.size() > 0) {
                                    for (ClickEntity item : items) {
                                        if (item != null && item.record != null && item.record.getUrl() != null) {
                                            mRxDownload.pauseServiceDownload(item.record.getUrl())
                                                    .subscribe(new Consumer<Object>() {
                                                        @Override
                                                        public void accept(Object o) throws Exception {

                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {
                                                            Log.d("--->>", throwable.toString());
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Log.i("--->>", "onViewClicked: pauseAll");
                break;
            case R.id.tv_back:
            case R.id.tv_back_rt:
                getActivity().finish();
                break;
            case R.id.fl_news:
                switch (type) {
                    case "plays":
                        FragmentActivity.actionActivity(getContext(), "news");
                        break;
                }
                break;
            case R.id.fl_menu:
                switch (type) {
                    case "plays":
                    case "play":
                        showPlayDialog();
                        //                        itemClickAdapter.setNewData(null);
                        break;
                    case "wait_dubbing":
                        page = 0;
                        initNetwork();
                        break;
                }
                break;
            case R.id.iv_play:
                FragmentActivity.actionActivity(getActivity(), "play");
                break;
            case R.id.iv_download:
                FragmentActivity.actionActivity(getContext(), "download_activity");
                break;
        }
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
        switch (type) {
            case "he":
                if (entity instanceof UserInfoEntity) {
                    UserInfoEntity userInfoEntity = (UserInfoEntity) entity;
                    multipleItemQuickAdapter.setNewData(null);
                    if (userInfoEntity.getUserInfo() != null) {
                        ClickEntity clickEntity = new ClickEntity(R.layout.item_user_info, "head");
                        clickEntity.setUserInfoEntity(userInfoEntity);
                        clickEntity.setPaperId(string);
                        multipleItemQuickAdapter.addData(clickEntity);
                    }
                    if (userInfoEntity.getAuthen() != null) {
                        ClickEntity clickEntity = new ClickEntity(R.layout.item_recycler_head, "authentication_info");
                        clickEntity.setUserInfoEntity(userInfoEntity);
                        multipleItemQuickAdapter.addData(clickEntity);
                    }
                    if (userInfoEntity.getIntroduction() != null && userInfoEntity.getIntroduction().size() > 0) {
                        ClickEntity clickEntity = new ClickEntity(R.layout.item_recycler_head, "scholar_education");
                        clickEntity.setUserInfoEntity(userInfoEntity);
                        multipleItemQuickAdapter.addData(clickEntity);
                    }

                    if (userInfoEntity.getPaper() != null && !TextUtils.isEmpty(userInfoEntity.getPaper().getId())) {
                        ClickEntity clickEntity = new ClickEntity(R.layout.item_recycler_head, "paper_indexes");
                        clickEntity.setUserInfoEntity(userInfoEntity);
                        multipleItemQuickAdapter.addData(clickEntity);

                    }
                    if (userInfoEntity.getSummarize() != null && !TextUtils.isEmpty(userInfoEntity.getSummarize().getId())) {
                        ClickEntity clickEntity = new ClickEntity(R.layout.item_recycler_head, "summarize_indexes");
                        clickEntity.setUserInfoEntity(userInfoEntity);
                        multipleItemQuickAdapter.addData(clickEntity);

                    }
                } else {
                    onRefresh();
                }
                break;
            case "scholar":
                break;

            case "plays":
                break;
            case "play":
                break;
            case "paper_qa":
                if (entity instanceof QAEntity) {
                    QAEntity.QuizInfoBean quizInfo = ((QAEntity) entity).getQuizInfo();
                    if (quizInfo != null) {
                        List<QAEntity.QuizInfoBean.RowsBeanXXXX> rows = quizInfo.getRows();
                        itemClickAdapter.setNewData(null);
                        for (QAEntity.QuizInfoBean.RowsBeanXXXX row : rows) {
                            ClickEntity clickEntity = new ClickEntity(row);
                            clickEntity.setPaperId(((QAEntity) entity).getAid() + "");
                            itemClickAdapter.addData(clickEntity);
                        }
                    }
                }
                break;
            case "paper_literature":


                if (entity instanceof LiteratureEntity) {
                    itemClickAdapter.setNewData(null);
                    List<LiteratureEntity.LiteratureInfoBean> literatureInfo = ((LiteratureEntity) entity).getLiteratureInfo();
                    for (LiteratureEntity.LiteratureInfoBean literatureInfoBean : literatureInfo) {
                        itemClickAdapter.addData(new ClickEntity(literatureInfoBean));
                    }
                }
                break;
            case "paper_label":
                if (entity instanceof AntistopEntity) {
                    AntistopEntity.AntistopInfoBean antistopInfo = ((AntistopEntity) entity).getAntistopInfo();
                    List<AntistopEntity.AntistopInfoBean.RowsBeanXX> rows = antistopInfo.getRows();
                    for (AntistopEntity.AntistopInfoBean.RowsBeanXX row : rows) {
                        for (AntistopEntity.AntistopInfoBean.RowsBeanXX.DataBean.RowsBeanX rowsBeanXX : row.getData().getRows()) {
                            if (itemClickAdapter.getItemCount() >= 3) {
                                ClickEntity item = itemClickAdapter.getItem(2);
                                if (item != null) {
                                    item.getList().add(rowsBeanXX.getAntistop());
                                }
                            }
                        }
                        for (AntistopEntity.AntistopInfoBean.RowsBeanXX.DataBean.RowsBeanX rowsBeanX : row.getMaths().getRows()) {
                            if (itemClickAdapter.getItemCount() >= 3) {
                                ClickEntity item = itemClickAdapter.getItem(1);
                                if (item != null)
                                    item.getList().add(rowsBeanX.getAntistop());
                            }
                        }
                        for (AntistopEntity.AntistopInfoBean.RowsBeanXX.DataBean.RowsBeanX rowsBean : row.getScience().getRows()) {
                            if (itemClickAdapter.getItemCount() >= 3) {
                                ClickEntity item = itemClickAdapter.getItem(0);
                                if (item != null)
                                    item.getList().add(rowsBean.getAntistop());
                            }
                        }

                    }
                    itemClickAdapter.notifyDataSetChanged();
                }
                break;
            case "paper_download":
            case "summary_download":
                break;
            case "download":
                break;
            case "release_paper":
                break;
            case "collection_paper":
            case "collection_summary":
                if (entity instanceof SubscribeEntity) {
                    itemClickAdapter.loadMoreComplete();
                    if (itemClickAdapter == null || ((SubscribeEntity) entity).getPageInfo() == null || ((SubscribeEntity) entity).getPageInfo().getRows() == null)
                        return;
                    if (page == 1) {
                        itemClickAdapter.setNewData(null);
                    }
                    List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX> rows = ((SubscribeEntity) entity).getPageInfo().getRows();
                    for (SubscribeEntity.PageInfoBean.RowsBeanXXXXX row : rows) {
                        itemClickAdapter.addData(new ClickEntity(row));
                    }
                    if (itemClickAdapter.getItemCount() < ((SubscribeEntity) entity).getTotal()) {
                        page++;
                    } else {
                        itemClickAdapter.loadMoreEnd(false);
                    }
                } else {
                    if (itemClickAdapter.getItemCount() > changePosition) {
                        ClickEntity item = itemClickAdapter.getItem(changePosition);
                        if (item != null && item.getSubscribeEntityRows() != null) {
                            SubscribeEntity.PageInfoBean.RowsBeanXXXXX subscribeEntityRows = item.getSubscribeEntityRows();
                            String isFollowed = "";
                            if (isSave == 10) {
                                isFollowed = "1";
                            } else if (isSave == 20) {
                                isFollowed = "0";
                            }
                            switch (version) {
                                case 0:
                                    if (subscribeEntityRows.getPhotoOne() != null && subscribeEntityRows.getPhotoOne().getRows() != null && subscribeEntityRows.getPhotoOne().getRows().size() == 1 && subscribeEntityRows.getPhotoOne().getRows().get(0) != null) {
                                        subscribeEntityRows.getPhotoOne().getRows().get(0).setIsFollowed(isFollowed);
                                    }
                                    break;
                                case 1:
                                    if (subscribeEntityRows.getPhotoTwo() != null && subscribeEntityRows.getPhotoTwo().getRows() != null && subscribeEntityRows.getPhotoTwo().getRows().size() == 1 && subscribeEntityRows.getPhotoTwo().getRows().get(0) != null) {
                                        subscribeEntityRows.getPhotoTwo().getRows().get(0).setIsFollowed(isFollowed);
                                    }
                                    break;
                                case 2:
                                    if (subscribeEntityRows.getPhotoThree() != null && subscribeEntityRows.getPhotoThree().getRows() != null && subscribeEntityRows.getPhotoThree().getRows().size() == 1 && subscribeEntityRows.getPhotoThree().getRows().get(0) != null) {
                                        subscribeEntityRows.getPhotoThree().getRows().get(0).setIsFollowed(isFollowed);
                                    }
                                    break;
                            }
                            itemClickAdapter.notifyItemChanged(changePosition);
                        }
                    }
                    //                    onRefresh();
                }
                break;
            case "popular":
                break;
            case "wait_dubbing":
//                if (entity instanceof WaitRecordPaperEntity) {
//                    dubbingAdapter.loadMoreComplete();
//                    if (dubbingAdapter == null || ((WaitRecordPaperEntity) entity).getWaitRecordPaper() == null || ((WaitRecordPaperEntity) entity).getWaitRecordPaper().getRows() == null) {
//                        dubbingAdapter.loadMoreEnd(false);
//                        return;
//                    }
//                    if (page == 1) {
//                        dubbingAdapter.setNewData(null);
//                    }
//
//                    List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX> rows = ((WaitRecordPaperEntity) entity).getWaitRecordPaper().getRows();
//                    if (rows != null && rows.size() > 0) {
//                        for (SubscribeEntity.PageInfoBean.RowsBeanXXXXX subscribeEntityRows : rows) {
//                            ClickEntity clickEntity0 = new ClickEntity(subscribeEntityRows);
//                            SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean photoOne = subscribeEntityRows.getPhotoOne();
//                            SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean photoTwo = subscribeEntityRows.getPhotoTwo();
//                            SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean photoThree = subscribeEntityRows.getPhotoThree();
//                            List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX> photoThreeRows = photoThree.getRows();
//                            List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX> photoTwoRows = photoTwo.getRows();
//                            List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX> photoOneRows = photoOne.getRows();
//                            if (photoThreeRows != null && photoThreeRows.size() == 1) {
//                                SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX = photoThreeRows.get(0);
//                                if (!Objects.equals("1", rowsBeanXXXX.getStatus()))
//                                    initEntity(subscribeEntityRows, clickEntity0, photoThreeRows, rowsBeanXXXX, "2");
//
//                            }
//                            if (photoTwoRows != null && photoTwoRows.size() == 1) {
//                                SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX = photoTwoRows.get(0);
//                                if (!Objects.equals("1", rowsBeanXXXX.getStatus()))
//                                    initEntity(subscribeEntityRows, clickEntity0, photoTwoRows, rowsBeanXXXX, "1");
//                            }
//                            if (photoOneRows != null && photoOneRows.size() == 1) {
//                                SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX = photoOneRows.get(0);
//                                if (!Objects.equals("1", rowsBeanXXXX.getStatus()))
//                                    initEntity(subscribeEntityRows, clickEntity0, photoOneRows, rowsBeanXXXX, "0");
//                            }
//                            clickEntity0.setItemType(0);
//                            clickEntity0.setLevel(0);
//                            dubbingAdapter.addData(clickEntity0);
//                        }
//                        if (dubbingAdapter.getItemCount() < ((WaitRecordPaperEntity) entity).getWaitRecordPaper().getTotal()) {
//                            page++;
//                        } else {
//                            dubbingAdapter.loadMoreEnd(false);
//                        }
//                        dubbingAdapter.expandAll();
//                    } else {
//                        dubbingAdapter.loadMoreEnd(false);
//                    }
//                } else {
//                    onRefresh();
//                }
                break;
            case "collection":
                break;
            case "subscribe":
            case "paper_list":
            case "my_review":
            case "my_paper":
            case "summary_list":
//                if (entity instanceof SubscribeEntity) {
//                    itemClickAdapter.loadMoreComplete();
//                    if (itemClickAdapter == null || ((SubscribeEntity) entity).getPageInfo() == null || ((SubscribeEntity) entity).getPageInfo().getRows() == null) {
//                        itemClickAdapter.loadMoreEnd(false);
//                        return;
//                    }
//                    if (page == 1) {
//                        itemClickAdapter.setNewData(null);
//                    }
//                    SubscribeEntity.PageInfoBean pageInfo = ((SubscribeEntity) entity).getPageInfo();
//                    if (pageInfo != null) {
//                        List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX> rows = pageInfo.getRows();
//                        if (rows != null && rows.size() > 0) {
//                            for (SubscribeEntity.PageInfoBean.RowsBeanXXXXX row : rows) {
//                                itemClickAdapter.addData(new ClickEntity(row));
//                            }
//                            if (itemClickAdapter.getItemCount() < pageInfo.getTotal()) {
//                                page++;
//                            } else {
//                                itemClickAdapter.loadMoreEnd(false);
//                            }
//                        } else {
//                            itemClickAdapter.loadMoreEnd(false);
//                        }
//
//                    }
//                } else if (entity.getResult() == 1) {
//                    if (itemClickAdapter.getItemCount() > changePosition) {
//                        ClickEntity item = itemClickAdapter.getItem(changePosition);
//                        if (item != null && item.getSubscribeEntityRows() != null) {
//                            SubscribeEntity.PageInfoBean.RowsBeanXXXXX subscribeEntityRows = item.getSubscribeEntityRows();
//                            String isFollowed = "";
//                            int followedNum = 0;
//                            if (isSave == 10) {
//                                isFollowed = "1";
//                                followedNum = 1;
//                            } else if (isSave == 20) {
//                                isFollowed = "0";
//                                followedNum = -1;
//                            }
//                            switch (version) {
//                                case 0:
//                                    if (subscribeEntityRows.getPhotoOne() != null && subscribeEntityRows.getPhotoOne().getRows() != null && subscribeEntityRows.getPhotoOne().getRows().size() == 1 && subscribeEntityRows.getPhotoOne().getRows().get(0) != null) {
//                                        SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX = subscribeEntityRows.getPhotoOne().getRows().get(0);
//                                        rowsBeanXXXX.setIsFollowed(isFollowed);
//                                        rowsBeanXXXX.setFollowedNum(rowsBeanXXXX.getFollowedNum() + followedNum);
//                                    }
//                                    break;
//                                case 1:
//                                    if (subscribeEntityRows.getPhotoTwo() != null && subscribeEntityRows.getPhotoTwo().getRows() != null && subscribeEntityRows.getPhotoTwo().getRows().size() == 1 && subscribeEntityRows.getPhotoTwo().getRows().get(0) != null) {
//                                        SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX = subscribeEntityRows.getPhotoTwo().getRows().get(0);
//                                        rowsBeanXXXX.setIsFollowed(isFollowed);
//                                        rowsBeanXXXX.setFollowedNum(rowsBeanXXXX.getFollowedNum() + followedNum);
//                                    }
//                                    break;
//                                case 2:
//                                    if (subscribeEntityRows.getPhotoThree() != null && subscribeEntityRows.getPhotoThree().getRows() != null && subscribeEntityRows.getPhotoThree().getRows().size() == 1 && subscribeEntityRows.getPhotoThree().getRows().get(0) != null) {
//                                        SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX = subscribeEntityRows.getPhotoThree().getRows().get(0);
//                                        rowsBeanXXXX.setIsFollowed(isFollowed);
//                                        rowsBeanXXXX.setFollowedNum(rowsBeanXXXX.getFollowedNum() + followedNum);
//                                    }
//                                    break;
//                            }
//                            itemClickAdapter.notifyItemChanged(changePosition);
//                        }
//                    }
//                    //                    onRefresh();
//                }
                break;
            case "newest":
                break;
            case "notice":
            case "interactive":

                if (entity instanceof MsgEntity) {
                    itemClickAdapter.loadMoreComplete();
                    if (itemClickAdapter == null || ((MsgEntity) entity).getPageInfo() == null || ((MsgEntity) entity).getPageInfo().getRows() == null) {
                        itemClickAdapter.loadMoreEnd(false);
                        return;
                    }
                    if (page == 1) {
                        itemClickAdapter.setNewData(null);
                    }
                    MsgEntity.PageInfoBean pageInfo = ((MsgEntity) entity).getPageInfo();
                    if (pageInfo != null) {
                        List<MsgEntity.PageInfoBean.RowsBean> rows = pageInfo.getRows();
                        if (rows != null && rows.size() > 0) {
                            for (MsgEntity.PageInfoBean.RowsBean row : rows) {
                                itemClickAdapter.addData(new ClickEntity(row));
                            }
                            if (itemClickAdapter.getItemCount() < pageInfo.getTotal()) {
                                page++;
                            } else {
                                itemClickAdapter.loadMoreEnd(false);
                            }
                        } else {
                            itemClickAdapter.loadMoreEnd(false);
                        }


                    }
                }
                break;
            case "answer":
                if (entity instanceof AnswerEntity) {
                    itemClickAdapter.loadMoreComplete();
                    if (itemClickAdapter == null || ((AnswerEntity) entity).getAnswer() == null || ((AnswerEntity) entity).getAnswer().getRows() == null) {
                        itemClickAdapter.loadMoreEnd(false);
                        return;
                    }

                    AnswerBean answer = ((AnswerEntity) entity).getAnswer();
                    if (answer != null) {
                        if (page == 1) {
                            itemClickAdapter.setNewData(null);
                        }
                        List<AnswerQuizRowsBean> rows = answer.getRows();
                        if (rows != null && rows.size() > 0) {
                            for (AnswerQuizRowsBean row : rows) {
                                itemClickAdapter.addData(new ClickEntity(row));
                            }
                            if (itemClickAdapter.getItemCount() < answer.getTotal()) {
                                page++;
                            } else {
                                itemClickAdapter.loadMoreEnd(false);
                            }
                        } else {
                            itemClickAdapter.loadMoreEnd(false);
                        }
                    }
                }
                break;
            case "questions":
                if (entity instanceof QuizEntity) {
                    itemClickAdapter.loadMoreComplete();
                    if (itemClickAdapter == null || ((QuizEntity) entity).getQuiz() == null || ((QuizEntity) entity).getQuiz().getRows() == null) {
                        itemClickAdapter.loadMoreEnd(false);
                        return;
                    }
                    QuizBean answer = ((QuizEntity) entity).getQuiz();
                    if (answer != null) {
                        if (page == 1) {
                            itemClickAdapter.setNewData(null);
                        }
                        List<AnswerQUizXBean> rows = answer.getRows();
                        if (rows != null && rows.size() > 0) {
                            for (AnswerQUizXBean row : rows) {
                                itemClickAdapter.addData(new ClickEntity(row));
                            }
                            if (itemClickAdapter.getItemCount() < answer.getTotal()) {
                                page++;
                            } else {
                                itemClickAdapter.loadMoreEnd(false);
                            }
                        } else {
                            itemClickAdapter.loadMoreEnd(false);
                        }

                    }
                }
                break;
            case "search_hint":
                if (entity instanceof SearchHistoryEntity) {

                    itemClickAdapter.setNewData(null);

                    SearchHistoryEntity.MySearchBean mySearch = ((SearchHistoryEntity) entity).getMySearch();
                    List<SearchHistoryEntity.MySearchBean.RowsBeanX> mySearchRows = mySearch.getRows();
                    SearchHistoryEntity.OtherSearchBean otherSearch = ((SearchHistoryEntity) entity).getOtherSearch();
                    List<SearchHistoryEntity.OtherSearchBean.RowsBean> otherRows = otherSearch.getRows();
                    List<String> stringList = new ArrayList<>();
                    for (SearchHistoryEntity.MySearchBean.RowsBeanX mySearchRow : mySearchRows) {
                        stringList.add(mySearchRow.getContent());
                    }
                    if (stringList.size() > 0)
                        itemClickAdapter.addData(new ClickEntity(getString(R.string.you_search), getString(R.string.empty), stringList));
                    List<String> stringList2 = new ArrayList<>();
                    for (SearchHistoryEntity.OtherSearchBean.RowsBean otherRow : otherRows) {
                        stringList2.add(otherRow.getContent());
                    }
                    if (stringList2.size() > 0)
                        this.itemClickAdapter.addData(new ClickEntity(getString(R.string.everyone_search), getString(R.string.in_a_batch), stringList2));


                } else {
                    Utils.showToast(entity.getInfo());
                    itemClickAdapter.remove(0);
                }
                break;
            case "search_result":
                if (entity instanceof HomeSearchEntity) {
                    itemClickAdapter.setNewData(null);
                    HomeSearchEntity.PaperBean paper = ((HomeSearchEntity) entity).getPaper();
                    HomeSearchEntity.ScholarBean scholar = ((HomeSearchEntity) entity).getScholar();
                    if (scholar != null && scholar.getRows() != null && scholar.getRows().size() > 0) {
                        ClickEntity clickEntity = new ClickEntity(getString(R.string.list_of_scholars), getString(R.string.more_scholars));
                        clickEntity.setScholarBean(scholar);
                        itemClickAdapter.addData(clickEntity);
                    }
                    if (paper != null && paper.getRows() != null && paper.getRows().size() > 0) {
                        ClickEntity clickEntity = new ClickEntity(getString(R.string.paper_list), getString(R.string.more_papers));
                        clickEntity.setPaperBean(paper);
                        itemClickAdapter.addData(clickEntity);
                    }

                } else {
                    onRefresh();
                }
                break;
            case "search_scholar":
                break;

            case "search_paper":
                break;
            case "mine_scholar":
            case "mine_follow":
                if (entity instanceof FansConcernedEntity) {
                    itemClickAdapter.loadMoreComplete();
                    if (((FansConcernedEntity) entity).getPageInfo() == null) {
                        itemClickAdapter.loadMoreEnd(false);
                        return;
                    }
                    if (page == 1) {
                        itemClickAdapter.setNewData(null);
                    }
                    List<FansConcernedEntity.PageInfoBean.RowsBean> rows = ((FansConcernedEntity) entity).getPageInfo().getRows();

                    if (rows != null && rows.size() > 0) {
                        for (FansConcernedEntity.PageInfoBean.RowsBean row : rows) {
                            itemClickAdapter.addData(new ClickEntity(row));
                        }
                        if (itemClickAdapter.getItemCount() < ((FansConcernedEntity) entity).getPageInfo().getTotal()) {
                            page++;
                        } else {
                            itemClickAdapter.loadMoreEnd(false);
                        }
                    } else {
                        itemClickAdapter.loadMoreEnd(false);
                    }


                } else {
                    //
                    onRefresh();
                }
                break;
            case "scholar_list":

                if (entity instanceof ScholarEntity) {
                    itemClickAdapter.loadMoreComplete();
                    List<MyScholarBean> scholar = ((ScholarEntity) entity).getScholar();
                    if (scholar != null && scholar.size() > 0) {
                        if (page == 1) {
                            itemClickAdapter.setNewData(null);
                        }
                        for (MyScholarBean myScholarBean : scholar) {
                            itemClickAdapter.addData(new ClickEntity(myScholarBean));
                        }
                        if (((ScholarEntity) entity).getTotal() > itemClickAdapter.getItemCount()) {
                            page++;
                        } else {
                            itemClickAdapter.loadMoreEnd(false);
                        }
                    } else {
                        itemClickAdapter.loadMoreEnd(false);
                    }


                }
                break;
            default:
                break;

        }
    }

    private void initEntity(SubscribeEntity.PageInfoBean.RowsBeanXXXXX subscribeEntityRows, ClickEntity clickEntity0, List<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX> photoThreeRows, SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX rowsBeanXXXX, String version) {
        if (rowsBeanXXXX != null && rowsBeanXXXX.getRecordOne() != null && rowsBeanXXXX.getRecordTwo() != null) {
            ClickEntity clickEntityCN = new ClickEntity();
            clickEntityCN.setPhotoRecord(subscribeEntityRows.getTitle());
            clickEntityCN.setPhotoOrder(subscribeEntityRows.getCreateTime());
            clickEntityCN.setTitle(subscribeEntityRows.getTitle());
            clickEntityCN.setTime(photoThreeRows.get(0).getCreateTime());
            clickEntityCN.setVersion(version);
            if (rowsBeanXXXX.getPhotos() != null && rowsBeanXXXX.getPhotos().getRows() != null)
                clickEntityCN.setPhotoNumber(rowsBeanXXXX.getPhotos().getRows().size());
            if (rowsBeanXXXX.getRecordOne() != null && rowsBeanXXXX.getRecordOne().getRows() != null)
                clickEntityCN.setRecordNumber(rowsBeanXXXX.getRecordOne().getRows().size());
            if (rowsBeanXXXX.getRecordTwo() != null && rowsBeanXXXX.getRecordTwo().getRows() != null)
                clickEntityCN.setRecordTwoNumber(rowsBeanXXXX.getRecordTwo().getRows().size());
            clickEntityCN.setItemType(1);
            clickEntityCN.setLevel(1);
            clickEntityCN.setPaperBan(rowsBeanXXXX);
            clickEntityCN.setUrl(rowsBeanXXXX.getFirstPic());
            clickEntityCN.setLanguage("CN");
            clickEntityCN.setPaperId(subscribeEntityRows.getId() + "");
            clickEntity0.addSubItem(clickEntityCN);


            ClickEntity clickEntityEN = new ClickEntity();
            clickEntityEN.setPhotoRecord(subscribeEntityRows.getTitle());
            clickEntityEN.setPhotoOrder(subscribeEntityRows.getCreateTime());
            clickEntityEN.setTitle(subscribeEntityRows.getTitle());
            clickEntityEN.setTime(photoThreeRows.get(0).getCreateTime());
            if (rowsBeanXXXX.getPhotos() != null && rowsBeanXXXX.getPhotos().getRows() != null)
                clickEntityEN.setPhotoNumber(rowsBeanXXXX.getPhotos().getRows().size());
            if (rowsBeanXXXX.getRecordOne() != null && rowsBeanXXXX.getRecordOne().getRows() != null)
                clickEntityEN.setRecordTwoNumber(rowsBeanXXXX.getRecordOne().getRows().size());
            if (rowsBeanXXXX.getRecordTwo() != null && rowsBeanXXXX.getRecordTwo().getRows() != null)
                clickEntityEN.setRecordNumber(rowsBeanXXXX.getRecordTwo().getRows().size());
            clickEntityEN.setItemType(1);
            clickEntityEN.setVersion(version);
            clickEntityEN.setLevel(1);
            clickEntityEN.setPaperBan(rowsBeanXXXX);
            clickEntityEN.setUrl(rowsBeanXXXX.getFirstPic());
            clickEntityEN.setLanguage("EN");
            clickEntityEN.setPaperId(subscribeEntityRows.getId() + "");
            clickEntity0.addSubItem(clickEntityEN);
        }
    }

    @Override
    public void onFailure(String msg, Exception ex) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        if (itemClickAdapter != null)
            itemClickAdapter.loadMoreFail();
        if (dubbingAdapter != null)
            dubbingAdapter.loadMoreFail();
        super.onFailure(msg, ex);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (type) {

            case "he":
                break;
            case "scholar":
                FragmentActivity.actionActivity(getContext(), "he");
                break;
            case "course":
                CoursePlayActivity.newInstance(getActivity(),
                        ToolString.getUrl(itemClickAdapter.getItem(position).getCourseVO().getFileurl()),
                        itemClickAdapter.getItem(position).getCourseVO().getTitle());
//                ToastUtils.showShort("点击了：" + ToolString.getUrl(itemClickAdapter.getItem(position).getCourseVO().getFileurl()));
                break;
            case "plays":
            case "play":
                PlayNoteVo playNoteVo = itemClickAdapter.getItem(position).getPlayNoteVo();
                if (playNoteVo != null) {
                    String id = playNoteVo.getId();
                    String version = playNoteVo.getVersion();
                    if (ObjectUtils.isNotEmpty(id) && ObjectUtils.isNotEmpty(version)) {
                        PaperDetailsActivity.actionActivityNew(getContext(), id,
                                version, type, ObjectUtils.isNotEmpty(playNoteVo.getLanguage())
                                        ? playNoteVo.getLanguage() : "1", getActivity().getClass().getName());
                    }
                }
                break;
            case "paper_qa":

                break;
            case "paper_literature":
                break;
            case "paper_label":
                break;

            case "scholar_list":
                ClickEntity scholarList = itemClickAdapter.getItem(position);
                if (scholarList != null) {
                    String oid = scholarList.getMyScholarBean().getId() + "";
                    if (!TextUtils.isEmpty(oid))
                        FragmentActivity.actionActivity(getContext(), "he", oid);
                }
                break;
            case "paper_download":
                ClickEntity paperDownloadItem = mDragAdapter.getItem(position);
                if (paperDownloadItem != null) {
                    String title = paperDownloadItem.getTitle();
                    List<ClickEntity> subItems = paperDownloadItem.getSubItems();
                    ArrayList<String> recordOneRows = new ArrayList<>();
                    ArrayList<String> recordTwoRows = new ArrayList<>();
                    ArrayList<String> photoRows = new ArrayList<>();
                    for (ClickEntity subItem : subItems) {
                        List<ClickEntity> subItems1 = subItem.getSubItems();
                        for (ClickEntity clickEntity : subItems1) {
                            if (clickEntity.getPhotoRecord().equals("appInfo")) {
                                PackageUtil.installApkNormal(getActivity(), clickEntity.record.getSavePath() + "/" + clickEntity.record.getSaveName());
                            } else if (clickEntity.getPhotoRecord().equals("img")) {
                                photoRows.add(clickEntity.record.getSavePath() + "/" + clickEntity.record.getSaveName());
                            } else {
                                if (clickEntity.record.getExtra5().equals(CN)) {
                                    recordOneRows.add(clickEntity.record.getSavePath() + "/" + clickEntity.record.getSaveName());
                                } else if ((clickEntity.record.getExtra5().equals(EN))) {
                                    recordTwoRows.add(clickEntity.record.getSavePath() + "/" + clickEntity.record.getSaveName());
                                }
                            }
                        }

                    }
                    if (paperDownloadItem.getPaperId() != null && photoRows.size() > 0 && (recordOneRows.size() > 0 || recordTwoRows.size() > 0 || subItems.size() > 0)) {
                        PaperDetailsActivity.actionActivity(getContext(), paperDownloadItem.getPaperId(), recordOneRows, recordTwoRows, photoRows, "download_paper", title, subItems.get(0).getVersion());
                    }
                }

                break;
            case "summary_download":
                //                PaperDetailsActivity.actionActivity(getContext());
                break;
            case "download":
                if (expandableItemAdapter.getItemCount() > position) {
                    ClickEntity clickEntity = expandableItemAdapter.getData().get(position);
                    final TextView size = (TextView) view.findViewById(R.id.tv_download_size);
                    final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_download_progress);
                    if (clickEntity.record == null)
                        return;
                    final String url = clickEntity.record.getUrl();
                    if (TextUtils.isEmpty(url))
                        return;
                    Log.i("--->>", "onItemClick: " + clickEntity.record.getUrl());
                    DownloadController mDownloadController = clickEntity.getDownloadController();
                    Log.i("--->>", "onItemClick: " + mDownloadController);
                    if (mDownloadController != null)
                        mDownloadController.handleClick(new DownloadController.Callback() {
                            @Override
                            public void startDownload() {
                                start(url);
                            }

                            @Override
                            public void pauseDownload() {
                                pause(url);
                                Utils.showToast(R.string.paused);
                            }

                            @Override
                            public void install() {
                                //                                        loadDownloadData();
                                //                        installApk();
                                Utils.showToast(R.string.download_completes);
                            }
                        });
                }
                break;

            case "release_paper":
                //                PaperDetailsActivity.actionActivity(getContext());
                break;
    /*        case "collection_paper":
            case "collection_summary":
                ClickEntity itemCollection = itemClickAdapter.getItem(position);
                if (itemCollection != null) {
                    CollectionEntity.PageInfoBean.RowsBean collectionEntity = itemCollection.getCollectionEntity();
                    if (collectionEntity != null)
                        PaperDetailsActivity.actionActivity(getContext(), collectionEntity.getId(), type);

                }

                break;*/
            case "popular":
                //                PaperDetailsActivity.actionActivity(getContext());
                break;
            case "wait_dubbing"://配音列表点击事件，这里应该在子布局的点击事件中处理
//                ClickEntity dubbing = dubbingAdapter.getItem(position);
//                if (dubbing != null) {
//                    SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX paperBan = dubbing.getPaperBan();
//                    if (paperBan != null) {
//                        ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX> photoRows = (ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX>) paperBan.getPhotos().getRows();
//                        ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX> recordOneRows = (ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX>) paperBan.getRecordOne().getRows();
//                        ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX> recordTwoRows = (ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX>) paperBan.getRecordTwo().getRows();
//                        switch (dubbing.getLanguage()) {
//                            case "CN":
//                                if (photoRows != null && recordOneRows != null && photoRows.size() > 0 && !TextUtils.isEmpty(paperBan.getId())) {
//                                    if (photoRows.size() != recordOneRows.size()) {
//                                        DubbingActivity.actionActivity(getActivity(), paperBan.getId(), photoRows, recordOneRows, 0, type);
//                                    }
//                                }
//                                break;
//                            case "EN":
//                                if (photoRows != null && recordTwoRows != null) {
//                                    if (photoRows.size() != recordTwoRows.size() && photoRows.size() > 0 && !TextUtils.isEmpty(paperBan.getId())) {
//                                        DubbingActivity.actionActivity(getActivity(), paperBan.getId(), photoRows, recordTwoRows, 1, type);
//                                    }
//                                }
//                                break;
//                        }
//
//                    }
//                }

                break;
            case "collection":
                //                PaperDetailsActivity.actionActivity(getContext());
                break;
            case "subscribe":
            case "paper_list":
            case "summary_list":
            case "my_review":
            case "my_paper":
                PaperMainVO vo = itemClickAdapter.getItem(position).getPaperMainVO();
                PaperDetailsActivity.actionActivityNew(getContext(), vo.getId(),
                        "1", "online_paper", vo.getMyLanguage(), getActivity().getClass().getName());
                break;
            case "collection_paper":
            case "collection_summary":
                PaperMainVO vo1 = itemClickAdapter.getItem(position).getPaperMainVO();
                PaperDetailsActivity.actionActivityNew(getContext(), vo1.getId(),
                        vo1.getVersion(), "online_paper", vo1.getMyLanguage(), getActivity().getClass().getName());


//                ClickEntity item = itemClickAdapter.getItem(position);
//                if (item != null && item.getSubscribeEntityRows() != null) {
//                    SubscribeEntity.PageInfoBean.RowsBeanXXXXX subscribeEntityRows = item.getSubscribeEntityRows();
//                    String id = subscribeEntityRows.getId();
//                    SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean photoOne = subscribeEntityRows.getPhotoOne();
//                    SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean photoTwo = subscribeEntityRows.getPhotoTwo();
//                    SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean photoThree = subscribeEntityRows.getPhotoThree();
//                    ArrayList<String> version = new ArrayList<>();
//
//                    if (photoOne != null && photoOne.getRows() != null && photoOne.getRows().size() > 0) {
//                        version.add("0");
//                    }
//                    if (photoTwo != null && photoTwo.getRows() != null && photoTwo.getRows().size() > 0) {
//                        version.add("1");
//                    }
//                    if (photoThree != null && photoThree.getRows() != null && photoThree.getRows().size() > 0) {
//                        version.add("2");
//                    }
//                    if (version.size() > 0 && !TextUtils.isEmpty(id)) {
//                        if (null != subscribeEntityRows.getIsEncrypt() && subscribeEntityRows.getIsEncrypt().equals("0")) { // TODO checkpwd
//                            PaperDetailsActivity.checkPwd(getContext(), id, version, "online_paper");
//                        } else {
//                            PaperDetailsActivity.actionActivity(getContext(), id, version, "online_paper");
//                        }
//                    }
//
//                }
                break;
            case "newest":
                //                PaperDetailsActivity.actionActivity(getContext());
                break;
            case "notice":


                break;
            case "interactive":

                break;
            case "answer":
                MyAnswerVO answerVO = itemClickAdapter.getItem(position).getMyAnswerVO();
                if (answerVO != null) {
                    PaperDetailsActivity.actionActivityNew(getContext(), answerVO.getQid(),
                            "1", type, getLanguage(), getActivity().getClass().getName());
                }
                break;
            case "questions":
                MyAnswerVO answerVO1 = itemClickAdapter.getItem(position).getMyAnswerVO();
                if (answerVO1 != null) {
                    PaperDetailsActivity.actionActivityNew(getContext(), answerVO1.getQid(),
                            "1", type, getLanguage(), getActivity().getClass().getName());
                }
                break;
            case "search_hint":
                break;
            case "search_result":

                break;
            case "search_scholar":
                break;
            case "search_paper":
                //                if ()
                //                PaperDetailsActivity.actionActivity(getContext());
                break;
            default:
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final ArrayMap<String, Object> map = new ArrayMap<>();
        HashMap<String, Object> hMap = new HashMap<>();
//        map.put("token", token);
        switch (view.getId()) {
            case R.id.tv_paper_collected:
                ClickEntity clickEntityCollected = itemClickAdapter.getItem(position);
                if (clickEntityCollected != null && clickEntityCollected.getSubscribeEntityRows() != null) {
                    SubscribeEntity.PageInfoBean.RowsBeanXXXXX subscribeEntityRows = clickEntityCollected.getSubscribeEntityRows();
                    String id = subscribeEntityRows.getId();
                    if (TextUtils.isEmpty(id)) {


                     /*   ArrayMap<String, Object> mapCancel = new ArrayMap<>();
                        mapCancel.put("", );
                        mapCancel.put("", );
                        mapCancel.put("", );
                        mapCancel.put("", );
                        presenter.getCancelFollowPaperPictureData();*/
                    }
                }
                //
                break;
            case R.id.tv_voice:
                ClickEntity clickEntity = itemClickAdapter.getItem(position);
                if (clickEntity != null) {
                    QAEntity.QuizInfoBean.RowsBeanXXXX quizInfoBean = clickEntity.getQuizInfoBean();
                    if (quizInfoBean != null && quizInfoBean.getAnswer() != null && quizInfoBean.getAnswer().getRows() != null && quizInfoBean.getAnswer().getRows().size() > 0) {
                        filePath = quizInfoBean.getAnswer().getRows().get(0).getRecord();
                        tvVoice = (TextView) view;
                        togglePlaying(view);
                    }
                }
                break;
            case R.id.tv_voice_two:
                ClickEntity clickEntityTwo = itemClickAdapter.getItem(position);
                if (clickEntityTwo != null) {
                    QAEntity.QuizInfoBean.RowsBeanXXXX quizInfoBean = clickEntityTwo.getQuizInfoBean();
                    if (quizInfoBean != null && quizInfoBean.getAnswerTwo() != null && quizInfoBean.getAnswerTwo().getRows() != null && quizInfoBean.getAnswerTwo().getRows().size() > 0) {
                        filePath = quizInfoBean.getAnswerTwo().getRows().get(0).getRecord();
                        tvVoice = (TextView) view;
                        togglePlaying(view);
                    }
                }
                break;
            case R.id.tv_more:
                //                           itemClickAdapter.getItem()
                break;

            case R.id.tv_release_dubbing:  //  发布
                if (dubbingAdapter != null) {
                    String id = dubbingAdapter.getItem(position).getDubbingListVO().getId();
                    hMap.put("pid", id);
                    hMap.put("language", 1);
                    presenter.getDataAll("318", hMap);
                } else {
                    ToastUtils.showShort("发布失败！");
                }
                break;
            case R.id.tv_release_dubbing_EN:  //  英文发布
                if (dubbingAdapter != null) {
                    String id = dubbingAdapter.getItem(position).getDubbingListVO().getId();
                    hMap.put("pid", id);
                    hMap.put("language", 2);
                    presenter.getDataAll("318", hMap);
                } else {
                    ToastUtils.showShort("发布失败！");
                }
                break;
            case R.id.tv_preview_dubbing: //  预览按钮
                DubbingListVO vo6 = dubbingAdapter.getItem(position).getDubbingListVO();
                PaperDetailsActivity.actionActivityNew(getContext(), vo6.getId(),
                        vo6.getVersion(), type, "1", getActivity().getClass().getName());
                break;
            case R.id.tv_preview_dubbing_EN: //  预览按钮
                DubbingListVO vo5 = dubbingAdapter.getItem(position).getDubbingListVO();
                PaperDetailsActivity.actionActivityNew(getContext(), vo5.getId(),
                        vo5.getVersion(), type, "2", getActivity().getClass().getName());
                break;
            case R.id.tv_edit_dubbing_EN: //  编辑
                DubbingListVO vo4 = dubbingAdapter.getItem(position).getDubbingListVO();
                DubbingActivity.actionActivity(getActivity(), vo4.getId(), 2, "edit_dubbing");
                break;
            case R.id.tv_edit_dubbing: //  编辑
                DubbingListVO vo3 = dubbingAdapter.getItem(position).getDubbingListVO();
                DubbingActivity.actionActivity(getActivity(), vo3.getId(), 1, "edit_dubbing");
                break;
            case R.id.ll_waiting_dubbing:
                DubbingListVO vo2 = dubbingAdapter.getItem(position).getDubbingListVO();
                DubbingActivity.actionActivity(getActivity(), vo2.getId(), 1, type);
                break;
//            case R.id.ll_dubbing_complete:
//                break;
            case R.id.ll_waiting_dubbing_EN:
                DubbingListVO vo1 = dubbingAdapter.getItem(position).getDubbingListVO();
                DubbingActivity.actionActivity(getActivity(), vo1.getId(), 2, type);
                break;
//            case R.id.ll_dubbing_complete_EN:
//                break;


            case R.id.tv_empty:
                ClickEntity item = itemClickAdapter.getItem(position);

                if (item != null) {
                    if (Objects.equals(item.getString(), getString(R.string.you_search))) {
                        presenter.getCleanHistoryData(map);
                    } else if (Objects.equals(item.getString(), getString(R.string.everyone_search))) {
                        map.put("uid", uid);
                        map.put("page", 1);
                        presenter.getSearchHistoryData(map);
                    }

                }
                break;
            case R.id.tv_paper_number:
            case R.id.tv_paper:
            case R.id.tv_review_number:
            case R.id.tv_review:
            case R.id.tv_follow_number:
            case R.id.tv_follow:
            case R.id.tv_fans_number:
            case R.id.tv_fans:
                ClickEntity itemHe = multipleItemQuickAdapter.getItem(position);
                if (itemHe != null) {
                    UserInfoEntity userInfoEntity = itemHe.getUserInfoEntity();
                    if (userInfoEntity != null && userInfoEntity.getIsFollowed() != null && userInfoEntity.getUserInfo() != null) {
                        UserInfoEntity.UserInfoBean userInfo = userInfoEntity.getUserInfo();
                        final String id = userInfo.getId();
                        if ("".equals(id))
                            return;
                        switch (view.getId()) {
                            case R.id.tv_paper_number:
                            case R.id.tv_paper:
                                Utils.showToast(R.string.temporarily_not_opened);
                                //                                FragmentActivity.actionActivity(getContext(), "my_paper", id + "");
                                break;
                            case R.id.tv_review_number:
                            case R.id.tv_review:
                                Utils.showToast(R.string.temporarily_not_opened);
                                //                                FragmentActivity.actionActivity(getContext(), "my_review", id + "");
                                break;
                            case R.id.tv_follow_number:
                            case R.id.tv_follow:
                                FragmentActivity.actionActivity(getContext(), "mine_follow", id + "");
                                break;
                            case R.id.tv_fans_number:
                            case R.id.tv_fans:
                                FragmentActivity.actionActivity(getContext(), "mine_scholar", id + "");
                        }
                    }
                }

                break;
            case R.id.iv_attention: // TODO  iv_attention
                ClickEntity item1 = multipleItemQuickAdapter.getItem(position);
                if (ObjectUtils.isEmpty(BaseActivity.uid)) {
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(getActivity());
                    normalDialog.setIcon(null);
                    normalDialog.setTitle(R.string.need_login);
                    normalDialog.setPositiveButton(R.string.register,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LogInActivity.actionActivity(getActivity());
                                }
                            });
                    normalDialog.setNegativeButton(R.string.cancel, null);
                    // 显示
                    normalDialog.show();
                } else if (item1 != null) {
                    UserInfoEntity userInfoEntity = item1.getUserInfoEntity();
                    if (userInfoEntity != null && userInfoEntity.getIsFollowed() != null && userInfoEntity.getUserInfo() != null) {
                        UserInfoEntity.UserInfoBean userInfo = userInfoEntity.getUserInfo();
                        final String id = userInfo.getId();
//                        map.put("token", token);
                        map.put("oid", id);
                        map.put("uid", uid);
                        switch (userInfoEntity.getIsFollowed()) { // TODO getIsFollowed 1 未关注。0以关注
                            case "1":
                                presenter.getSaveFollowUserData(map);
                                break;
                            case "0":
                                Utils.showDialog((BaseActivity) getActivity(), getString(R.string.determine_cancel_attention), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ArrayMap<String, Object> map = new ArrayMap<>();
//                                        map.put("token", token);
                                        map.put("uid", uid);
                                        map.put("oid", id);
                                        presenter.getCancelFollowUserData(map);
                                    }
                                });

                                break;

                        }


                    }

                }

                //                                presenter.getCancelFollowUserData(map);
                break;
        }
    }

    @Override
    public void addDownload() {
        Log.i("--->>", "addDownload: ");
        String text = tvDownloadNumber.getText().toString();
        Integer integer;
        if (TextUtils.isEmpty(text)) {
            integer = 1;
        } else {
            integer = Integer.valueOf(text) + 1;
        }
        tvDownloadNumber.setText(String.valueOf(integer));
        super.addDownload();
    }

    @Override
    public void downloadCompleted() {
        super.downloadCompleted();
        loadDownloadData();
        loadDownloadNunber();
    }

    private void showNormalDialog(final String id, final int position) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题 Attempt to invoke virtual method 'android.content.res.Resources$Theme' on a null object reference
	at android.app.AlertDialog.resolveDialogTheme(AlertDialog.java:225)
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        if (getContext() == null)
            return;
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
        normalDialog.setIcon(null);
        normalDialog.setTitle(R.string.confirm_delete_this_entry);
        normalDialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DaoSession daoSessions = BaseApp.app.getDaoSession();
                        NoteDao noteDaos = daoSessions.getNoteDao();
//                        noteDaos.deleteByKey(id);
                        loadDownloadData();
                        itemClickAdapter.remove(position);
                    }
                });
        normalDialog.setNegativeButton(R.string.cancel, null);
        // 显示
        normalDialog.show();
    }

    private void showDownloadDialog(final ClickEntity itemDownload, final int position) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题 Attempt to invoke virtual method 'android.content.res.Resources$Theme' on a null object reference
	at android.app.AlertDialog.resolveDialogTheme(AlertDialog.java:225)
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        if (getContext() == null)
            return;

        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getContext());
        normalDialog.setIcon(null);
        normalDialog.setTitle(R.string.confirm_delete_this_entry);
        normalDialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String url = itemDownload.getPaperId();


                        mRxDownload.deleteAll(url, true).subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                //                            Utils.showToast(url);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        });
                        List<ClickEntity> subItems = itemDownload.getSubItems();
                        if (subItems != null && subItems.size() > 0) {
                            for (ClickEntity subItem : subItems) {

                                mRxDownload.deleteAll(subItem.getString(), true).subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(@NonNull Object o) throws Exception {
                                        //                                Utils.showToast(url);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                    }
                                });
                            }
                        }
                        mDragAdapter.remove(position);

                    }
                });
        normalDialog.setNegativeButton(R.string.cancel, null);
        // 显示
        normalDialog.show();
    }

    private void showPlayDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题 Attempt to invoke virtual method 'android.content.res.Resources$Theme' on a null object reference
	at android.app.AlertDialog.resolveDialogTheme(AlertDialog.java:225)
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        if (getContext() == null)
            return;

        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getContext());
        normalDialog.setIcon(null);
        normalDialog.setTitle(R.string.sure_delete_alldata);
        normalDialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DaoSession daoSessions = BaseApp.app.getDaoSession();
                        NoteDao noteDaos = daoSessions.getNoteDao();
                        Query<Note> notesQuery = noteDaos.queryBuilder().where(NoteDao.Properties.Type.eq(type)).orderAsc(NoteDao.Properties.Id).build();
                        List<Note> listnote = notesQuery.list();
                        for (Note note : listnote) {
                            noteDaos.delete(note);
                        }
                        initNetwork();
                    }
                });
        normalDialog.setNegativeButton(R.string.cancel, null);
        // 显示
        normalDialog.show();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        switch (type) {
            case "plays":
            case "play":
                ClickEntity item = (ClickEntity) adapter.getItem(position);
                if (item != null) {
                    Note note = item.getNote();
                    if (note != null) {
                        String id = note.getId();
                        showNormalDialog(id, position);
                    }
                }
                break;
            case "paper_download":
                ClickEntity itemDownload = mDragAdapter.getItem(position);
                if (itemDownload != null) {
                    showDownloadDialog(itemDownload, position);

                }
                break;
        }

        return false;
    }

    @Override
    public void onRefresh() {
        if (itemClickAdapter != null)
            itemClickAdapter.setEnableLoadMore(false);
        if (dubbingAdapter != null)
            dubbingAdapter.setEnableLoadMore(false);
        page = 0;
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
        initNetwork();
    }

    @Override
    public void onLoadMoreRequested() {
        initNetwork();
    }

    protected void initpaly() {
        audioPlayer = new AudioPlayer(getContext(), new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case AudioPlayer.HANDLER_CUR_TIME://更新的时间
                        tvVoice.setText(Util.formatSecond((int) msg.obj / 1000));
                        break;
                    case AudioPlayer.HANDLER_COMPLETE://播放结束
                        break;
                    case AudioPlayer.HANDLER_PREPARED://播放开始
                        break;
                    case AudioPlayer.HANDLER_ERROR://播放错误
                        //                        resolveResetPlay();
                        break;
                }
                return false;
            }
        }));
    }

    /**
     * 播放
     */
    protected void resolvePlayRecord() {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        if (isPause) {
            mIsPlay = true;
            audioPlayer.play();
        } else {
            mIsPlay = true;
            android.util.Log.i("--->>", "resolvePlayRecord: " + filePath);
            audioPlayer.playUrl(filePath);
        }
    }

    protected void resolvePausePlayRecord() {

        mIsPlay = false;
        isPause = true;
        audioPlayer.pause();
    }

    public void togglePlaying(View v) {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (mIsPlay) {
                    if (Objects.equals(filePath, filePathTwo)) {
                        resolvePausePlayRecord();
                    } else {
                        resolvePlayRecord();
                    }
                    //                    tvDubbingDudition.setSelected(false);
                } else {
                    if (Objects.equals(filePath, filePathTwo)) {
                        resolvePlayRecord();
                    } else {
                        isPause = false;
                        resolvePlayRecord();
                    }
                    //                    tvDubbingDudition.setSelected(true);
                }
                filePathTwo = filePath;
            }
        });
    }

    public void submitFollow(String isFollowed, String id) {
        ArrayMap<String, Object> map = new ArrayMap<>();
//        map.put("token", token);
        map.put("uid", uid);
        map.put("oid", id);
        switch (isFollowed) {
            case "0":
                presenter.getSaveFollowUserData(map);
                break;
            case "1":
            case "2":
                presenter.getCancelFollowUserData(map);
                break;
        }
    }

    //收藏
    @Override
    public void collection(int id, int integer, final String type, String isFollowed,
                           int parentPosition, int position) {
        changePosition = parentPosition;
        version = integer;
        final ArrayMap<String, Object> map = new ArrayMap<>();
        map.put("pbid", id);
        map.put("flag", 1);
        map.put("tabFlag", 1);
        map.put("uid", uid);
        map.put("version", integer);
      /*  Log.i(TAG, "collection: " + type);
        switch (type) {
            case "collection_paper":
            case "collection_summary":

                //pbid=1&flag=1&tabFlag=1
                presenter.getCancelFollowPaperPictureData(map);
                break;
            default:
                map.put("pbid", id);
                map.put("flag", 1);
                map.put("tabFlag", 1);
                map.put("uid", uid);
                map.put("version", integer);
                //pbid=1&flag=1&tabFlag=1
                presenter.getSaveFollowPaperPictureData(map);
                break;
        }*/
        switch (isFollowed) {
            case "0":
                isSave = 10;
                presenter.getSaveFollowPaperPictureData(map);
                break;
            case "1":
                Utils.showDialog((BaseActivity) getActivity(), getString(R.string.determine_cancel_collection), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isSave = 20;
                        presenter.getCancelFollowPaperPictureData(map);

                    }
                });
                break;
        }
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
        switch (url) {
            case "203":
                PaperBean bean = (PaperBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (itemClickAdapter == null || bean == null
                        || bean.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }
                ClickEntity entity = new ClickEntity();
                entity.setPaperBean(bean);
                itemClickAdapter.addData(entity);


                break;
            case "209"://首页的论文和综述
            case "212"://收藏的论文和综述
            case "312"://我的论文和综述
                PaperMainBean mainBean = (PaperMainBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (itemClickAdapter == null || mainBean == null
                        || mainBean.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }
                for (PaperMainVO vo : mainBean.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setPaperMainVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }
//                if (mainBean.getList().size() == 0) {
//                    if (page != 0)
//                        ToastUtils.showShort("数据已经全部加载了");
//                    else
//                        ToastUtils.showShort("暂无数据");
//                }
                if (mainBean.getList().size() >= pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                break;
            case "206"://论文详情问答列表
                PaperQaBean qaBean = (PaperQaBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                itemClickAdapter.setPid(string);
                itemClickAdapter.setImgId(imageId);
                if (itemClickAdapter == null || qaBean == null
                        || qaBean.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }
                for (PaperQaVO vo : qaBean.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setPaperQaVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }
//                if (qaBean.getList().size() == 0) {
//                    if (page != 0)
//                        ToastUtils.showShort("数据已经全部加载了");
//                    else
//                        ToastUtils.showShort("暂无数据");
//                }
                if (qaBean.getList().size() >= pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                break;
            case "208":
                PaperLiteratureBean bean1 = (PaperLiteratureBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (itemClickAdapter == null || bean1 == null
                        || bean1.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }

                for (PaperLiteratureVO vo : bean1.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setPaperLiteratureVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }

                if (bean1.getList().size() >= pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                break;
            case "213":
                ToastUtils.showShort("收藏成功！");
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
            case "217":
                ToastUtils.showShort("取消收藏成功！");
                break;
            case "306":
                MIneInfoBean mIneInfoBean = (MIneInfoBean) baseBean;
                ClickEntity clickEntity = new ClickEntity(R.layout.item_user_info, "head");
                clickEntity.setMIneInfoBean(mIneInfoBean);
                clickEntity.setPaperId(string);
                multipleItemQuickAdapter.addData(clickEntity);
                break;
            case "307":
                FansBean bean2 = (FansBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (itemClickAdapter == null || bean2 == null
                        || bean2.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }

                for (FansVO vo : bean2.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setFansVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }

//                if (bean2.getList().size() == 0) {
//                    if (page != 0)
//                        ToastUtils.showShort("数据已经全部加载了");
//                    else
//                        ToastUtils.showShort("暂无数据");
//                }

                if (bean2.getList().size() >= pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                break;
            case "308":
                FollowBean bean3 = (FollowBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (itemClickAdapter == null || bean3 == null
                        || bean3.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }

                for (FollowVO vo : bean3.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setFollowVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }

//                if (bean3.getList().size() == 0) {
//                    if (page != 0)
//                        ToastUtils.showShort("数据已经全部加载了");
//                    else
//                        ToastUtils.showShort("暂无数据");
//                }

                if (bean3.getList().size() >= pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                break;
            case "316"://待配音列表
                DubbingListBean bean4 = (DubbingListBean) baseBean;
                dubbingAdapter.loadMoreComplete();
                if (bean4.getList() == null) {
                    dubbingAdapter.loadMoreEnd(false);
                    return;
                }
                List<ClickEntity> list = new ArrayList<>();
                for (DubbingListVO vo :
                        bean4.getList()) {
                    ClickEntity entity1 = new ClickEntity();
                    entity1.setDubbingListVO(vo);
                    list.add(entity1);
                }

                if (page == 0) {
                    dubbingAdapter.setNewData(list);
                } else {
                    dubbingAdapter.addData(list);
                }

                if (bean4.getList().size() == pageSize) {
                    page++;
                } else {
                    dubbingAdapter.loadMoreEnd(false);
                }
                dubbingAdapter.expandAll();
                break;
            case "313"://我的提问
            case "314"://我的回答

                MyAnswerBean bean5 = (MyAnswerBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (bean5.getList() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }

                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }

                for (MyAnswerVO vo : bean5.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setMyAnswerVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }

                if (bean5.getList().size() == pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                itemClickAdapter.expandAll();
                break;
            case "218"://学者
                PeopleBean bean6 = (PeopleBean) baseBean;
                itemClickAdapter.loadMoreComplete();
                if (bean6.getUlist() == null) {
                    itemClickAdapter.loadMoreEnd(false);
                    return;
                }

                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }

                for (PeopleVO vo : bean6.getUlist()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setPeopleVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }

                if (bean6.getUlist().size() == pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                itemClickAdapter.expandAll();
                break;
            case "318"://发布论文
                ToastUtils.showShort("发布成功！");
                page = 0;
                HashMap<String, Object> map = new HashMap<>();
                map.put("uid", getUserID());
                map.put("pageno", page);
                presenter.getDataAll("316", map);
                break;
            case "214":
                PlayNoteBean bean7 = (PlayNoteBean) baseBean;
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }
                for (PlayNoteVo vo : bean7.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setPlayNoteVo(vo);
                    itemClickAdapter.addData(mainEntity);
                }
                if (bean7.getList().size() == pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                itemClickAdapter.expandAll();
                swipeRefreshLayout.setRefreshing(false);
                break;
            case "321":
                CourseBean bean8 = (CourseBean) baseBean;
                if (page == 0) {
                    itemClickAdapter.setNewData(null);
                }
                for (CourseVO vo : bean8.getList()) {
                    ClickEntity mainEntity = new ClickEntity();
                    mainEntity.setCourseVO(vo);
                    itemClickAdapter.addData(mainEntity);
                }
                if (bean8.getList().size() == pageSize) {
                    page++;
                } else {
                    itemClickAdapter.loadMoreEnd(false);
                }
                itemClickAdapter.expandAll();
                swipeRefreshLayout.setRefreshing(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailNew(String url, String msg) {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
        super.onFailNew(url, msg);
    }

    @Override
    public void listClick(String type, String pid, String version, String language) {
        HashMap<String, Object> map = new HashMap<>();
        switch (type) {
            case "collect":
                map.put("uid", getUserID());
                map.put("pid", pid);
                map.put("picid", "");
                map.put("type", "2");
                map.put("version", version);
                map.put("language", getLanguage());
                presenter.getDataAll("213", map);
                break;
            case "nocollect":
                map.put("uid", getUserID());
                map.put("pid", pid);
                map.put("picid", "");
                map.put("type", "2");
                map.put("version", version);
                map.put("language", getLanguage());
                presenter.getDataAll("217", map);
                break;
            case "download":
                map.put("uid", getUserID());
                map.put("pid", pid);
                map.put("version", version);
                map.put("language", language);
                presenter.getDataAll("216", map);
                break;
            case "add":
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
