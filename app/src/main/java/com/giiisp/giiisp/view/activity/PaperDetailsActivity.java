package com.giiisp.giiisp.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.base.BaseApp;
import com.giiisp.giiisp.base.BaseFragment;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.DownloadImgInfoVO;
import com.giiisp.giiisp.dto.DownloadInfoBean;
import com.giiisp.giiisp.dto.DubbingBean;
import com.giiisp.giiisp.dto.DubbingVO;
import com.giiisp.giiisp.dto.ImgInfoBean;
import com.giiisp.giiisp.dto.PaperEventBean;
import com.giiisp.giiisp.dto.PaperEventVO;
import com.giiisp.giiisp.dto.PaperInfoBean;
import com.giiisp.giiisp.dto.PaperInfoVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.DaoSession;
import com.giiisp.giiisp.entity.DownloadController;
import com.giiisp.giiisp.entity.Note;
import com.giiisp.giiisp.entity.NoteDao;
import com.giiisp.giiisp.entity.Song;
import com.giiisp.giiisp.model.ModelFactory;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.FileUtils;
import com.giiisp.giiisp.utils.ImageLoader;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.view.adapter.ViewPagerAdapter;
import com.giiisp.giiisp.view.fragment.BannerRecyclerViewFragment;
import com.giiisp.giiisp.view.fragment.StatisticsFragment;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.giiisp.giiisp.widget.FloatDragView;
import com.giiisp.giiisp.widget.FullScreenPopupWindow;
import com.giiisp.giiisp.widget.MyCustomView;
import com.giiisp.giiisp.widget.ProgressPopupWindow;
import com.giiisp.giiisp.widget.WrapVideoView;
import com.giiisp.giiisp.widget.recording.AppCache;
import com.giiisp.giiisp.widget.recording.OnPlayerEventListener;
import com.giiisp.giiisp.widget.recording.PlayService;
import com.giiisp.giiisp.widget.recording.Util;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.shuyu.waveview.AudioPlayer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.giiisp.giiisp.api.UrlConstants.CN;
import static com.giiisp.giiisp.api.UrlConstants.EN;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.ACTIVITYNAME;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;
import static com.giiisp.giiisp.widget.recording.AppCache.getPlayService;


/**
 * 论文详细页面
 * Created by Thinkpad on 2017/5/10.
 */

public class PaperDetailsActivity extends
        BaseMvpActivity<BaseImpl, WholePresenter>
        implements BaseQuickAdapter.OnItemClickListener,
        BaseImpl,
        ViewPager.OnPageChangeListener,
        SeekBar.OnSeekBarChangeListener,
        OnPlayerEventListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.viewpager_paper)
    ViewPager viewpagerPaper;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.seek_bar_paper)
    SeekBar seekBarPaper;
    @BindView(R.id.iv_back_off)
    ImageView ivBackOff;
    @BindView(R.id.iv_play_stop)
    ImageView ivPlayStop;
    @BindView(R.id.iv_fast_forward)
    ImageView ivFastForward;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tab_layout_paper)
    TabLayout tabLayoutPaper;
    @BindView(R.id.et_comm_post)
    TextView etCommPost;
    @BindView(R.id.tv_liked_number)
    TextView tvLikedNumber;
    @BindView(R.id.iv_liked_icon)
    ImageView ivLikedIcon;
    @BindView(R.id.tv_download_number)
    TextView tvDownloadNumber;
    @BindView(R.id.iv_download_icon)
    ImageView ivDownloadIcon;
    @BindView(R.id.tv_share_number)
    TextView tvShareNumber;
    @BindView(R.id.iv_share_icon)
    ImageView ivShareIcon;
    @BindView(R.id.liner_bottom_comm)
    LinearLayout linerBottomComm;
    @BindView(R.id.viewpager_tab)
    ViewPager viewpagerTab;
    @BindView(R.id.line_banner)
    RelativeLayout lineBanner;
    @BindView(R.id.relative_full)
    RelativeLayout relativeFull;
    @BindView(R.id.iv_fullscreen_button)
    ImageView ivFullscreenButton;
    @BindView(R.id.tv_play_time)
    TextView tvPlayTime;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.tv_playlist_number)
    TextView tvPlaylistNumber;
    @BindView(R.id.iv_play_list)
    ImageView ivPlayList;
    @BindView(R.id.linear_layout_main)
    LinearLayout linearLayoutMain;
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.tv_paper_complete)
    TextView tvPaperComplete;
    @BindView(R.id.tv_paper_marrow)
    TextView tvPaperMarrow;
    @BindView(R.id.tv_paper_abstract)
    TextView tvPaperAbstract;
    @BindView(R.id.tv_cn)
    CheckBox tvCn;
    @BindView(R.id.tv_en)
    CheckBox tvEn;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.rl_viewpager_full)
    RelativeLayout rl_viewpager_full;
    @BindView(R.id.cv_mark)
    MyCustomView mMyCustomView;
    @BindView(R.id.rl_all)
    RelativeLayout mRlAll;

//    public static final String CN = "1";
//    public static final String EN = "2";

    private boolean isFulllScreen = false;
    private FullScreenPopupWindow fullScreenPopup;
    private DownloadController mDownloadController;
    private String type;
    private String isFollowed;
    private RxDownload mRxDownload;
    private NoteDao noteDao;
    private Query<Note> notesQuery;
    public static String paperId;
    public static String pid;
    private String storageId;
    public static String downloadId = "";
    private ItemClickAdapter itemClickAdapter;
    private ArrayList<String> photoList;
    private ArrayList<String> recordOneList;
    private ArrayList<String> recordTwoList;
    BaseImpl baseImpl;
    OnPlayerEventListener onPlayerEventListener;
    private int position;
    private int positionPic;
    List<String> imageId = new ArrayList<>();
    private BannerRecyclerViewFragment paperQA;
    Note note = new Note();
    //    private ArrayList<String> version = new ArrayList<>();
    private String myVersionNo = "";
    private List<Song> queueCN = new ArrayList<>();
    private List<Song> queueEN = new ArrayList<>();
    private Song songCN;
    private Song songEN;

    private String language = CN;
    private String title = "";
    private String firstPic = "";
    private String realName = "";
    private ProgressPopupWindow progressPopupWindow;
    private boolean isMove = false;
    /*** viewpager的根视图数据集合 ***/
    List<View> mViewList;

    /*** 当前页面索引 ***/
    int currentViewPagerItem = 0;

    /*** 上一个页面索引 ***/
    int lastItem = 0;

    /*** 页面的视频控件集合,Integer所处位置 ***/
    static Map<Integer, WrapVideoView> mVideoViewMap;
    static Map<Integer, View> mVideoBgViewMap;
    /*** 页面播放进度控制器集合 ***/
    static Map<Integer, MediaController> mMediaControllerMap;

    /*** 页面视频缓冲图集合 ***/
    static List<View> mCacheViewList;

    /*** 记录每个page页面视频播放的进度 ***/
    static Map<Integer, Integer> mCurrentPositions;

    /*** 记录当前page页面是否为视频 ***/
    static Map<Integer, Boolean> mIsVideo;

    private int nowProgress = 0;//播放进度
    private Map<String, PaperEventVO> mBeanMap = new HashMap<>();
    private List<String> timeString = new ArrayList<>();
    private boolean isEvent = false;
    private Map<String, String> mPidMap = new HashMap<>();
    private String twoId = "";
    private String threeId = "";
    private String fourId = "";
    private String lastPlayId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.back_right_in, R.anim.push_left_out);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.back_right_in, R.anim.push_left_out);
        super.onResume();
        switch (type) {
            case "online_paper":
            case "collection_paper":
            case "collection_summary":
            case "play":
            case "plays":
            case "home":
            case "answer":
            case "questions":
                if (imageId.size() > position && position >= 0) {
                    paperQA.setImageId(imageId.get(position));
                    paperQA.initNetwork();
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        getPlayService().setPlayType(PlayService.BACK);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.push_left_in, R.anim.back_right_out);
        super.onPause();
    }

    @Override
    protected void onStop() {
        sendPlayNote();
        super.onStop();
    }

    public OnPlayerEventListener getOnPlayerEventListener() {
        return onPlayerEventListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.onPlayerEventListener = onPlayerEventListener;
    }

    public BaseImpl getBaseImpl() {
        return baseImpl;
    }

    public void setBaseImpl(BaseImpl baseImpl) {
        this.baseImpl = baseImpl;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_paperdetails;
    }

    //所有入口
    public static void actionActivity(Context context, String id, ArrayList<String> version, String type) {
        Intent sIntent = new Intent(context, PaperDetailsActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putStringArrayListExtra("version", version);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    //所有入口
    public static void actionActivityNew(Context context, String id, String version,
                                         String type, String language) {
        Intent sIntent = new Intent(context, PaperDetailsActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putExtra("language", language);
        sIntent.putExtra("version", version);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    //所有入口
    public static void actionActivityBase(Context context, String id, String version,
                                          String type, String language, String activityName) {
        ACTIVITYNAME = activityName;
        Intent sIntent = new Intent(context, PaperDetailsActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putExtra("language", language);
        sIntent.putExtra("version", version);
        sIntent.putExtra("activityName", activityName);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    //所有入口
    public static void actionActivityNew(Context context, String id, String version,
                                         String type, String language, String activityName) {
        ACTIVITYNAME = activityName;
        Intent sIntent = new Intent(context, PaperDetailsActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putExtra("language", language);
        sIntent.putExtra("version", version);
        sIntent.addCategory(Math.random() + "");
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    //下载页
    public static void actionActivity(Context context,
                                      String id,
                                      ArrayList<String> recordOneRows,
                                      ArrayList<String> recordTwoRows,
                                      ArrayList<String> photoRows,
                                      String type,
                                      String title,
                                      String version) {
        Intent sIntent = new Intent(context, PaperDetailsActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putStringArrayListExtra("recordOneRows", recordOneRows);
        sIntent.putStringArrayListExtra("recordTwoRows", recordTwoRows);
        sIntent.putStringArrayListExtra("photoRows", photoRows);
        sIntent.putExtra("title", title);
        sIntent.putExtra("version", version);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    @Override
    public void initData() {
        pid = getIntent().getStringExtra("id");
        //        downloadId = getIntent().getIntExtra("downloadId", -1);
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        if (ObjectUtils.isNotEmpty(getIntent().getStringExtra("language")))
            language = getIntent().getStringExtra("language");
        tvCn.setChecked(CN.equals(language));
        tvEn.setChecked(EN.equals(language));
        myVersionNo = getIntent().getStringExtra("version");

        SPUtils.getInstance().put(UrlConstants.PID, pid);
        SPUtils.getInstance().put(UrlConstants.PAPERTYPE, type);
        SPUtils.getInstance().put(UrlConstants.LANGUAGE, language);
        SPUtils.getInstance().put(UrlConstants.PAPERVERSION, myVersionNo);

        hideImg();
        storageId = pid;
        photoList = getIntent().getStringArrayListExtra("photoRows");
        recordOneList = getIntent().getStringArrayListExtra("recordOneRows");
        recordTwoList = getIntent().getStringArrayListExtra("recordTwoRows");

        if (type == null)
            type = "";

        if (photoList == null) {
            photoList = new ArrayList<>();
        }

        DaoSession daoSession = BaseApp.app.getDaoSession();
        noteDao = daoSession.getNoteDao();
        Query<Note> notesId = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(storageId)).build();
        List<Note> list = notesId.list();
        for (Note note1 : list) {
            if (note1.getId().equals(storageId)) {
                language = note1.getLanguage();
                position = note1.getPlayPosition();
            }
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mMyCustomView.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth();
        mMyCustomView.setLayoutParams(params);
    }

    @Override
    public void initView() {
        fullScreenPopup = new FullScreenPopupWindow(this);

        progressPopupWindow = new ProgressPopupWindow(this);
//                showDialog();
        loadDownloadNunber();
//        if (photoList != null && photoList.size() > 0) {
//            fullScreenPopup.setStringArrayList(photoList);
//            fullScreenPopup.initDownload();
//        }

        tvTitle.setText("P1");
        viewpagerPaper.addOnPageChangeListener(this);
        List<ClickEntity> list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        itemClickAdapter = new ItemClickAdapter(this, R.layout.item_paper_pic, list, "");
        recyclerView.setAdapter(itemClickAdapter);
        itemClickAdapter.setOnItemClickListener(this);
        //        coordinatorLayout.addOnAttachStateChangeListener();

        fullScreenPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PaperDetailsActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        mDownloadController = new DownloadController(new TextView(this), new ImageView(this));
        //        fullScreenPopup.setDismissWhenTouchOuside();
        mRxDownload = RxDownload.getInstance(this);
        seekBarPaper.setOnSeekBarChangeListener(this);
        if (getPlayService() != null) {
            PlayService playService = getPlayService();
            playService.setOnPlayEventListener(this);
            if (storageId.equals(pid)) {
                ivPlayStop.setSelected(playService.isPlaying());

                if (playService.getPlayingMusic() != null) {
                    seekBarPaper.setMax((playService.getPlayingMusic().getDuration() - 1) * 1000);
                    tvDuration.setText("/ " + Util.formatSeconds(playService.getPlayingMusic().getDuration()));
                }
//                switch (language) {
//                    case CN:
//                        tvCn.setChecked(false);
//                        tvEn.setChecked(true);
//                        break;
//                    case EN:
//                        tvCn.setChecked(true);
//                        tvEn.setChecked(false);
//                        break;
//                }

                seekBarPaper.setProgress(playService.getmPlayer().getCurrentPosition());
            }
        }

        switch (type) {

            case "collection_paper":
            case "collection_summary":
            case "online_paper":
            case "play":
            case "plays":
            case "home":
            case "answer":
            case "questions":
                getPlayService().setPlayType(PlayService.ONLINE);
                List<BaseFragment> fragments = new ArrayList<>();
                //问答、文献索引、统计
                paperQA = BannerRecyclerViewFragment.newInstance("paper_qa", pid);
                fragments.add(paperQA);
                fragments.add(BannerRecyclerViewFragment.newInstance("paper_literature", pid));
                fragments.add(StatisticsFragment.newInstance());
                List<String> mTitles = new ArrayList<>();
                mTitles.add("问答");
                mTitles.add("文献索引");
                mTitles.add("统计");
                viewpagerTab.setOffscreenPageLimit(2);
                tabLayoutPaper.setupWithViewPager(viewpagerTab);
                viewpagerTab.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
                viewpagerTab.setCurrentItem(0);
                TabLayout.Tab tabAt0 = tabLayoutPaper.getTabAt(0);
                TabLayout.Tab tabAt1 = tabLayoutPaper.getTabAt(1);
                TabLayout.Tab tabAt2 = tabLayoutPaper.getTabAt(2);
                if (tabAt0 != null) {
                    tabAt0.setIcon(R.drawable.bg_tab_mine);
                }
                if (tabAt1 != null)
                    tabAt1.setIcon(R.drawable.bg_tab_center);
                if (tabAt2 != null)
                    tabAt2.setIcon(R.drawable.bg_tab_tag);
                //                initTab();
                ivEmpty.setVisibility(View.GONE);
                progressWheel.setVisibility(View.VISIBLE);
                initNetwork();

                break;
            case "wait_dubbing"://待配音
                getPlayService().setPlayType(PlayService.ONLINE);
                linerBottomComm.setVisibility(View.GONE);
                HashMap<String, Object> map = new HashMap<>();
                map.put("pid", pid);
                map.put("language", language);
                presenter.getDataAll("317", map);
                llEmptyView.setVisibility(View.GONE);
                break;
            case "download_paper": {//下载
                getPlayService().setPlayType(PlayService.DOWN);
                linerBottomComm.setVisibility(View.GONE);
                llEmptyView.setVisibility(View.GONE);
                if (getPlayService() != null) {
                    PlayService playService = getPlayService();

                    playService.setOnPlayEventListener(this);
                    if (downloadId.equals(storageId)) {
                        Log.i("--->>", "initData: " + playService.getPlayingMusic().getDuration());

                        if (playService.getImageList().size() > 0) {
                            if (photoList != null) photoList.clear();
                            photoList = (ArrayList<String>) playService.getImageList();
                            for (String photo : photoList) {
                                itemClickAdapter.addData(new ClickEntity(photo));
                            }
                            viewpagerPaper.setAdapter(new ImageAdapter(this, photoList));
                        }
                        viewpagerPaper.setCurrentItem(playService.getPlayingPosition());
                        recyclerView.scrollToPosition(playService.getPlayingPosition());
                        //                        Song playingMusic = playService.getPlayingMusic();
                        ivPlayStop.setSelected(playService.isPlaying());

                        if (playService.getPlayingMusic() != null) {
                            seekBarPaper.setMax((playService.getPlayingMusic().getDuration() - 1) * 1000);
                            tvDuration.setText("/ " + Util.formatSeconds(playService.getPlayingMusic().getDuration()));
                        }
                        itemClickAdapter.setSelectedPosition(playService.getPlayingPosition());
                        itemClickAdapter.notifyDataSetChanged();

                    } else {
                        if (photoList != null) {
                            for (String photo : photoList) {
                                itemClickAdapter.addData(new ClickEntity(photo));
                            }
                            viewpagerPaper.setAdapter(new ImageAdapter(this, photoList));
                            viewpagerPaper.setCurrentItem(0);
                            for (String url : photoList) {
                                ClickEntity entity = new ClickEntity();
                                PaperInfoVO vo = new PaperInfoVO();
                                vo.setUrl(url);
                                entity.setPaperInfoVO(vo);
                                itemClickAdapter.addData(entity);
                            }
                        }

                        if (recordOneList != null) {
                            for (int i = 0; i < recordOneList.size(); i++) {
                                Song song = new Song();
                                song.setPosition(i);
                                if (photoList != null && photoList.size() > i)
                                    song.setPhotoPath(photoList.get(i));
                                song.setPath(recordOneList.get(i));
                                song.setTitle(title);
                                song.setDuration((int) AudioPlayer.getDurationLocation(this, recordOneList.get(i)) / 1000);
                                queueCN.add(song);
                            }
                        }
                        if (recordTwoList != null) {
                            for (int i = 0; i < recordTwoList.size(); i++) {
                                Song song = new Song();
                                song.setPosition(i);
                                song.setTitle(title);
                                if (photoList != null && photoList.size() > i)
                                    song.setPhotoPath(photoList.get(i));
                                song.setPath(recordTwoList.get(i));
                                song.setDuration((int) AudioPlayer.getDurationLocation(this, recordTwoList.get(i)) / 1000);
                                queueEN.add(song);
                            }
                        }
                        if (queueEN.size() > 0 && queueCN.size() > 0) {
                            tvCn.setVisibility(View.VISIBLE);
                            tvEn.setVisibility(View.VISIBLE);
                            tvCn.setChecked(false);
                            tvEn.setChecked(true);
                        }
                        playService.setmMusicList(queueCN);
                        playService.setImageList(photoList);
                        playService.play(position);
                    }
                    downloadId = myVersionNo;
                    paperId = "";
                }
            }
            break;
        }

        //添加可拖动悬浮按钮
        FloatDragView.addFloatDragView(this, mRlAll, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpagerPaper.setCurrentItem(getPlayService().getPlayingPosition());
            }
        }, new FloatDragView.OnMyListening() {
            @Override
            public void myListening(int action) {
                //在播放的时候，拖动按钮有问题，暂时这样：在拖动的时候暂停，结束在播放
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (getPlayService().isPlaying()) {
                            getPlayService().playPause();
                            isMove = true;
                        }
                        toolbarLayout.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (getPlayService().isPlaying()) {
                            getPlayService().playPause();
                            isMove = true;
                        }
                        toolbarLayout.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (getPlayService().isPausing() && isMove) {
                            getPlayService().playPause();
                            isMove = false;
                        }
                        toolbarLayout.requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
        });

    }

    @Override
    protected void initNetwork() {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("token", token);
        map.put("pid", pid);
        map.put("version", myVersionNo);
        presenter.getDataAll("204", map);
        super.initNetwork();
    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.tv_back, R.id.iv_fullscreen_button, R.id.iv_empty, R.id.tv_empty,
            R.id.tv_paper_marrow, R.id.tv_paper_complete, R.id.tv_paper_abstract,
            R.id.tv_cn, R.id.tv_en, R.id.tv_liked_number, R.id.fl_paper_play,
            R.id.iv_fast_forward, R.id.iv_back_off, R.id.iv_play_stop,
            R.id.et_comm_post, R.id.fl_download, R.id.fl_collection, R.id.fl_share,
            R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_empty:
            case R.id.iv_empty:
                initNetwork();
                break;
            case R.id.tv_back:
//                if (getPlayService() != null && getPlayService().isPlaying())
//                    getPlayService().playPause();
//                finish();
                try {
                    if (ObjectUtils.isNotEmpty(ACTIVITYNAME))
                        startActivity(new Intent(this, Class.forName(ACTIVITYNAME)));
                    else
                        startActivity(new Intent(this, Class.forName("GiiispActivity")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_paper_complete://完整
                PaperDetailsActivity.actionActivityNew(this, twoId, "2", "online_paper", language, ACTIVITYNAME);
                break;
            case R.id.tv_paper_marrow://精华
                PaperDetailsActivity.actionActivityNew(this, fourId, "4", "online_paper", language, ACTIVITYNAME);
                break;
            case R.id.tv_paper_abstract://摘要
                PaperDetailsActivity.actionActivityNew(this, threeId, "3", "online_paper", language, ACTIVITYNAME);
                break;
            case R.id.tv_cn:
                if (getPlayService() != null && songCN != null) {
                    sendPlayNote();
                    changeLanguage(true);
                } else {
                    tvCn.setChecked(false);
                    tvEn.setChecked(true);
                    ToastUtils.showShort("暂无中文版录音");
                }
                Log.i("--->>", "onViewClicked: tv_cn");
                break;
            case R.id.tv_en:
                Log.i("--->>", "onViewClicked: tv_en");
                if (getPlayService() != null && songEN != null) {
                    sendPlayNote();
                    changeLanguage(false);
                } else {
                    tvCn.setChecked(true);
                    tvEn.setChecked(false);
                    ToastUtils.showShort("暂无英文版录音");
                }
                break;
            case R.id.iv_fast_forward://下一页
                if (getPlayService() != null) {
                    if (position < imageId.size() - 1) {
                        sendPlayNote();
                        position++;
                        getImageInfo();
                    } else {
                        ToastUtils.showShort("已经是最后一张了");
                    }
                }

                break;
            case R.id.iv_back_off://上一页
                if (getPlayService() != null) {
                    if (position > 0) {
                        sendPlayNote();
                        position--;
                        getImageInfo();
                    } else {
                        ToastUtils.showShort("已经是第一张了");
                    }
                }

                break;
            case R.id.iv_play_stop://暂停
                if (getPlayService() != null)
                    getPlayService().playPause();

                break;
            case R.id.iv_fullscreen_button:
                //                FullscreenActivity.actionActivity(this);
//                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
//                    //切换竖屏
//                    fullScreenPopup.dismiss();
//                    //                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                    PaperDetailsActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//                } else {
//                    //切换横屏
//                    PaperDetailsActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                    fullScreenPopup.showPopupWindow();
//                    //                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                }
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                isFulllScreen = !isFulllScreen;
                break;
            case R.id.et_comm_post://提问
//                if (photosBeanRows == null || photosBeanRows.size() <= position)
//                    return;
                if (ObjectUtils.isEmpty(getUserID())) {
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(this);
                    normalDialog.setIcon(null);
                    normalDialog.setTitle("需要登录才能执行此操作");
                    normalDialog.setPositiveButton("登录",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LogInActivity.actionActivity(PaperDetailsActivity.this);
                                }
                            });
                    normalDialog.setNegativeButton("取消", null);
                    // 显示
                    normalDialog.show();
                } else
                    switch (BaseActivity.emailauthen) { //  按照 emailauthen 判断
                        case "3":
                            Utils.showToast("      认证请联系：\n" +
                                    "+86 185 0101 0114 \n" +
                                    " service@giiisp.com");
                            break;
                        case "2":
                            ProblemActivity.actionActivity(this, "Problem", imageId.get(position),
                                    pid, "", photoList.get(position));
                            break;
                        case "1":
                            Utils.showToast("等待认证完成");
                            break;
                    }

                break;
            case R.id.fl_collection://收藏
                if (ObjectUtils.isEmpty(BaseActivity.uid)) {
                    AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
                    normalDialog.setIcon(null);
                    normalDialog.setTitle("需要登录才能执行此操作");
                    normalDialog.setPositiveButton("登录",
                            (dialogInterface, i) -> LogInActivity.actionActivity(PaperDetailsActivity.this));
                    normalDialog.setNegativeButton("取消", null);
                    // 显示
                    normalDialog.show();
                } else {
                    collection();
                }
                break;
            case R.id.fl_paper_play://播放记录
                FragmentActivity.actionActivity(this, "play");
                break;
            case R.id.fl_download://下载
                if (ObjectUtils.isEmpty(BaseActivity.uid)) {
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(this);
                    normalDialog.setIcon(null);
                    normalDialog.setTitle("需要登录才能执行此操作");
                    normalDialog.setPositiveButton("登录",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LogInActivity.actionActivity(PaperDetailsActivity.this);
                                }
                            });
                    normalDialog.setNegativeButton("取消", null);
                    // 显示
                    normalDialog.show();
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uid", getUserID());
                    map.put("pid", pid);
                    map.put("picid", imageId.get(position));
                    map.put("version", myVersionNo);
                    map.put("language", language);
                    map.put("type", "1");
                    presenter.getDataAll("216", map);
                }
                //                presenter.downloadFileWithDynamicUrlSync();
                break;
            case R.id.fl_share:
                new RxPermissions(this).requestEach(WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(@NonNull Permission permission) throws Exception {
                                if (permission.granted) {
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // Denied permission without ask never again
                                    Utils.showToast("取消存储授权,不能存储文件");
                                } else {
                                    // Denied permission with ask never again
                                    // Need to go to the
                                    Utils.showToast("您已经禁止弹出存储的授权操作,请在设置中手动开启");
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.i("--->>", "onError", throwable);
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {

                                UMWeb web = new UMWeb("http://giiisp.com/paper_play.php?id=" + pid);
                                UMImage thumb = new UMImage(PaperDetailsActivity.this, firstPic);
                                web.setTitle(title);//标题
                                web.setThumb(thumb);  //缩略图
                                web.setDescription(realName);//描述
                                new ShareAction(PaperDetailsActivity.this).withMedia(web)
                                        .setDisplayList(/*SHARE_MEDIA.QQ, */SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                                        .setCallback(umShareListener).open(new ShareBoardConfig().setIndicatorVisibility(false));
                            }
                        });

                break;
            case R.id.tv_left://快退
                if (nowProgress < 15000) {
                    seekBarPaper.setProgress(0);//如果已经播放的时间少于15秒，则从头开始播放
                    getPlayService().seekTo(0);
                } else {
                    int time = nowProgress - 15000;
                    seekBarPaper.setProgress(time);
                    getPlayService().seekTo(time);
                }
                break;
            case R.id.tv_right://快进
                Log.d("PaperDetailsActivity", "seekBarPaper.getMax():" + seekBarPaper.getMax());
                if (seekBarPaper.getMax() - nowProgress < 15000) {
                    seekBarPaper.setProgress(seekBarPaper.getMax() - 1000);//如果未播放的时间少于15秒，则直接到最后一秒
                    getPlayService().seekTo(seekBarPaper.getMax() - 1000);
                } else {
                    int time = nowProgress + 15000;
                    seekBarPaper.setProgress(time);
                    getPlayService().seekTo(time);
                }
                break;
        }
    }

    //切换语言
    private void changeLanguage(boolean isCN) {
        language = isCN ? CN : EN;
        getPlayService().setLanguage(language);
        getPlayService().play(isCN ? songCN : songEN);
        SPUtils.getInstance().put(UrlConstants.LANGUAGE, language);
        tvCn.setChecked(isCN);
        tvEn.setChecked(!isCN);
    }

    private void collection() {
/*        ArrayMap<String, Object> map = new ArrayMap<>();
        map.put("pbid", PaperDetailsActivity.id);
        map.put("flag", 1);
        map.put("tabFlag", 1);
        map.put("uid", uid);
        map.put("version", integer);
        //pbid=1&flag=1&tabFlag=1
        presenter.getSaveFollowPaperPictureData(map);*/
//        final ArrayMap<String, Object> map = new ArrayMap<>();
//        map.put("pbid", id);
//        map.put("flag", 1);
//        map.put("tabFlag", 1);
//        map.put("uid", uid);
//        map.put("version", myVersionNo);

        HashMap<String, Object> hMap = new HashMap<>();
        hMap.put("uid", uid);
        hMap.put("pid", pid);
        hMap.put("version", myVersionNo);
        hMap.put("picid", imageId.get(position));
        hMap.put("language", getLanguage());
        hMap.put("type", "1");
        switch (isFollowed) {
            case "2":
                presenter.getDataAll("213", hMap);
                break;
            case "1":
                Utils.showDialog(this, "确定取消收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.getDataAll("217", hMap);
                    }
                });
                break;
        }

    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
            progressPopupWindow.showPopupWindow();
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Utils.showToast(getString(R.string.share_result));
            //uId=1&pid=5&ptype=1&ttype=1
            ArrayMap<String, Object> map = new ArrayMap<>();
            map.put("uid", uid);
            map.put("pid", pid);
            map.put("ttype", 1);
            map.put("ptype", 1);
            presenter.getSaveShareData(map);
            progressPopupWindow.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Utils.showToast(getString(R.string.share_error));
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
            progressPopupWindow.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Utils.showToast(getString(R.string.share_cancel));
            progressPopupWindow.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("--->>", "onActivityResult: " + requestCode);
        if (requestCode == 1000) {
            switch (type) {
                case "online_paper":
                case "collection_paper":
                case "collection_summary":
                case "play":
                case "plays":
                case "home":
                case "answer":
                case "questions":
                    if (imageId.size() > position) {
                        paperQA.setImageId(imageId.get(position));
                        paperQA.initNetwork();
                    }
                    break;

            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @SuppressLint("CheckResult")
    private void start(DownloadInfoBean infoBean) {
        ArrayList<DownloadBean> list = new ArrayList<>();
        for (DownloadImgInfoVO vo : infoBean.getPics()) {
            String imgPath = BASE_IMG_URL + vo.getPicurl();
            String mp3Path = BASE_IMG_URL + vo.getRurl();
            if (DataBaseHelper.getSingleton(this).recordNotExists(imgPath)) {
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
            if (DataBaseHelper.getSingleton(this).recordNotExists(mp3Path)) {
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
            new RxPermissions(this)
                    .request(WRITE_EXTERNAL_STORAGE)
                    .doOnNext(granted -> {
                        if (!granted) {
                            throw new RuntimeException("no permission");
                        }
                    })
                    .compose(mRxDownload.transformMulti(list, pid))
                    .subscribe(o -> Utils.showToast("下载开始"), throwable -> {
                        Log.w("--->>", throwable);
                        Utils.showToast("下载中");
                    });
        } else {
            Utils.showToast("下载完成");
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setViewState(newConfig);
    }

    /**
     * 设置全屏
     *
     * @param newConfig
     */
    public void setViewState(Configuration newConfig) {
        if (relativeFull != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                rl_viewpager_full.setVisibility(View.GONE);
                rl_viewpager_full.removeAllViews();
                toolbarLayout.addView(relativeFull);
                mRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                ViewGroup viewGroup = (ViewGroup) relativeFull.getParent();
                if (viewGroup == null)
                    return;
                viewGroup.removeAllViews();
                rl_viewpager_full.addView(relativeFull);
                rl_viewpager_full.setVisibility(View.VISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                int mHideFlags =
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                rl_viewpager_full.setSystemUiVisibility(mHideFlags);
            }
        } else {
            rl_viewpager_full.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (this.position != position) {
            sendPlayNote();
            this.position = position;
//            if (getPlayService() != null)
//                getPlayService().play(position);
            getImageInfo();
        }


        viewpagerPaper.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        if (!isEvent) {
            this.position = position;
            lastItem = currentViewPagerItem;
            currentViewPagerItem = position;
            tvTitle.setText("P" + (position + 1));
            viewpagerPaper.setCurrentItem(position);
//        recyclerView.scrollToPosition(position);
//        itemClickAdapter.setSelectedPosition(position);
//        itemClickAdapter.notifyDataSetChanged();
            if (mIsVideo.get(currentViewPagerItem)) { // 当前是视频
                seekBarPaper.setVisibility(View.INVISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                mMediaControllerMap.get(currentViewPagerItem).setVisibility(View.VISIBLE);
            } else {
                seekBarPaper.setVisibility(View.VISIBLE);
                if (!isFulllScreen) {
                    mRelativeLayout.setVisibility(View.VISIBLE);
                }
                if (mIsVideo.get(lastItem)) { // 上一个为视频时
                    if (mVideoViewMap.get(lastItem).isPlaying()) {
                        mVideoViewMap.get(lastItem).pause();
                    }
                    if (mMediaControllerMap.get(lastItem).isShowing()) {
                        mMediaControllerMap.get(lastItem).setVisibility(View.INVISIBLE);
                    }
                }
            }
            switch (type) {
                case "online_paper":
                case "collection_paper":
                case "collection_summary":
                case "play":
                case "plays":
                case "home":
                case "answer":
                case "questions":
                    if (imageId.size() > position) {
                        paperQA.setImageId(imageId.get(position));
                        paperQA.initNetwork();
                    }
                    break;

            }
        } else {//是事件的只是显示图片，不做其他处理
            viewpagerPaper.setCurrentItem(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSuccess(BaseEntity entity) {
//        if (viewpagerPaper == null)
//            return;
//
//        if (entity instanceof PaperEntity) {
//        } else if (entity instanceof LiteratureEntity) {
//
//        } else if (entity instanceof PaperDatailEntity) {
//            if (baseImpl != null)
//                baseImpl.onSuccess(entity);
//            //            progressPopupWindow.dismiss();
//            llEmptyView.setVisibility(View.GONE);
//            PaperDatailEntity.PaperBaseBean paperBase = ((PaperDatailEntity) entity).getPaperBase();
//            if (paperBase == null)
//                return;
//            if (!TextUtils.isEmpty(paperBase.getTitle()))
//                title = paperBase.getTitle();
//            if (!TextUtils.isEmpty(paperBase.getRealName()))
//                realName = paperBase.getRealName();
//            PaperDatailEntity.PaperBaseBean.PhotoOneBean photo = null;
//            switch (myVersionNo) {
//                case "0":
//                    photo = paperBase.getPhotoOne();
//                    break;
//                case "1":
//                    photo = paperBase.getPhotoTwo();
//                    break;
//                case "2":
//                    photo = paperBase.getPhotoThree();
//                    break;
//            }
//
//            if (photo != null && photo.getRows() != null && photo.getRows().size() > 0) {
//                PaperDatailEntity.PaperBaseBean.PhotoOneBean.RowsBeanXX rowsBeanXX = photo.getRows().get(0);
//                firstPic = rowsBeanXX.getFirstPic();
//                isFollowed = rowsBeanXX.getIsFollowed();
//                ivLikedIcon.setSelected("1".equals(isFollowed));
//                PaperDatailEntity.PaperBaseBean.PhotoOneBean.RowsBeanXX.PhotosBean photos = rowsBeanXX.getPhotos();//  add type 1,png ,2 mp4, 3 gif
//                PaperDatailEntity.PaperBaseBean.PhotoOneBean.RowsBeanXX.RecordOneBean recordOne = rowsBeanXX.getRecordOne();
//                PaperDatailEntity.PaperBaseBean.PhotoOneBean.RowsBeanXX.RecordOneBean recordTwo = rowsBeanXX.getRecordTwo();
//                if (photos != null && photos.getRows() != null && photos.getRows().size() > 0) {
//                    photosBeanRows = photos.getRows();
//                    List<String> images = new ArrayList<>();
//                    if (photoList != null) {
//                        photoList.clear();
//                    } else {
//                        photoList = new ArrayList<>();
//                    }
//                    for (PaperDatailEntity.PaperBaseBean.PhotoOneBean.RowsBeanXX.PhotosBean.RowsBean photosBeanRow : photosBeanRows) {
//                        itemClickAdapter.addData(new ClickEntity(photosBeanRow.getPath(), photosBeanRow.getId()));
//                        photoList.add(photosBeanRow.getPath());
//                        imageId.add(photosBeanRow.getId());
//                    }
//                    if (imageId.size() > position && paperQA != null) {
//                        paperQA.setImageId(imageId.get(position));
//                        paperQA.initNetwork();
//
//                    }
//                    note.setPath(photos.getRows().get(0).getPath());
////                    long time3 = System.currentTimeMillis(); //  time test 3
//                    viewpagerPaper.setAdapter(new ImageAdapter(this, photoList));
//                    viewpagerPaper.setCurrentItem(position);
//                    recyclerView.scrollToPosition(position);
//                    itemClickAdapter.setSelectedPosition(position);
//                    itemClickAdapter.notifyDataSetChanged();
////                    long result3 = (System.currentTimeMillis()-time3);
////                    Log.e("time","result3= "+result3);
//                }
//
//                if (recordOne != null && recordOne.getRows() != null && recordOne.getRows().size() > 0) {
//                    recordsBeanOneRows = recordOne.getRows();
//                    for (int i = 0; i < recordsBeanOneRows.size(); i++) {
//                        Song song = new Song();
//                        song.setPosition(i);
//                        song.setTitle(paperBase.getTitle());
//                        if (photosBeanRows != null && photosBeanRows.size() > i) {
//                            song.setPhotoPath(photosBeanRows.get(i).getPath());
//                        }
//                        song.setPath(recordsBeanOneRows.get(i).getPath());
//                        song.setType(recordsBeanOneRows.get(i).getType());
//                        song.setDuration(Integer.parseInt(recordsBeanOneRows.get(i).getDuration()));
//                        queueCN.add(song);
//                    }
//                }
//                if (recordTwo != null && recordTwo.getRows() != null && recordTwo.getRows().size() > 0) {
//                    recordsBeanTwoRows = recordTwo.getRows();
//                    for (int i = 0; i < recordsBeanTwoRows.size(); i++) {
//                        Song song = new Song();
//                        song.setPosition(i);
//                        song.setTitle(paperBase.getTitle());
//                        if (photosBeanRows != null && photosBeanRows.size() > i) {
//                            song.setPhotoPath(photosBeanRows.get(i).getPath());
//                        }
//                        song.setPath(recordsBeanTwoRows.get(i).getPath());
//                        song.setType(recordsBeanOneRows.get(i).getType());
//                        song.setDuration(Integer.parseInt(recordsBeanTwoRows.get(i).getDuration()));
//                        queueEN.add(song);
//                    }
//                }
//                if (queueEN.size() > 0 && queueCN.size() > 0) {
//                    tvCn.setVisibility(View.VISIBLE);
//                    tvEn.setVisibility(View.VISIBLE);
//                }
//
//            /*    if (recordTwo != null && recordTwo.getRows() != null && recordTwo.getRows().size() > 0) {
//                    recordsBeanTwoRows = recordTwo.getRows();
//                    for (int i = 0; i < recordsBeanTwoRows.size(); i++) {
//                        Song song = new Song();
//                        song.setPosition(i);
//                        song.setPath(recordsBeanTwoRows.get(i).getPath());
//                        song.setDuration(recordsBeanTwoRows.get(i).getDuration());
//                        queueEN.add(song);
//                    }
//                }*/
//                note.setTitle(paperBase.getTitle());
//
//                note.setType("play");
//                note.setId(storageId);
//                note.setCommentNum(paperBase.getCommentNum() + "");
//                note.setFollowedNum(paperBase.getFollowedNum() + "");
//                note.setCreateTime(paperBase.getUpdateTime());
//                note.setReadNum(paperBase.getReadNum() + "");
//                note.setLikedNum(paperBase.getLikedNum() + "");
//                note.setVersions(myVersionNo);
//
//                if (getPlayService() != null) {
//                    PlayService playService = getPlayService();
//                    if (storageId.equals(pid)) {
//                        switch (language) {
//                            case CN:
//                                tvCn.setChecked(false);
//                                tvEn.setChecked(true);
//                                playService.setmMusicList(queueCN);
//                                break;
//                            case EN:
//                                tvCn.setChecked(true);
//                                tvEn.setChecked(false);
//                                playService.setmMusicList(queueEN);
//                                break;
//                            default:
//                                if (queueCN.size() > 0) {
//                                    tvCn.setChecked(false);
//                                    tvEn.setChecked(true);
//                                    playService.setmMusicList(queueCN);
//                                } else if (queueEN.size() > 0) {
//                                    tvCn.setChecked(true);
//                                    tvEn.setChecked(false);
//                                    playService.setmMusicList(queueEN);
//                                }
//                                break;
//                        }
//                        playService.play(position);
//                    }
//                    switch (language) {
//                        case CN:
//                            tvCn.setChecked(false);
//                            tvEn.setChecked(true);
//                            break;
//                        case EN:
//                            tvCn.setChecked(true);
//                            tvEn.setChecked(false);
//                            break;
//                        default:
//                            if (queueCN.size() > 0) {
//                                tvCn.setChecked(false);
//                                tvEn.setChecked(true);
//                            } else if (queueEN.size() > 0) {
//                                tvCn.setChecked(true);
//                                tvEn.setChecked(false);
//                            }
//                            break;
//                    }
//                    paperId = myVersionNo;
//                    downloadId = "";
//                }
//            }
//            //            isFollowed = ((PaperDatailEntity) entity).getIsFollowed();
//
//            tvShareNumber.setText(String.valueOf(paperBase.getShareNum()));
//            tvLikedNumber.setText(String.valueOf(paperBase.getLikedNum()));
//
//
//        } else {
////            Utils.showToast(entity.getInfo());
////            if (isSave == 10) {
////                isFollowed = "1";
////            } else if (isSave == 20) {
////                isFollowed = "0";
////            }
////            ivLikedIcon.setSelected("1".equals(isFollowed));
//        }
    }

    @Override
    public void onFailure(String msg, Exception ex) {
        if (ivEmpty == null)
            return;
        ivEmpty.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);

    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("PaperDetailsActivity", "progress:" + progress);
        this.nowProgress = progress;
        tvPlayTime.setText(Util.formatSeconds((progress + 1000) / 1000));
        if (timeString.contains(Util.formatSeconds((progress + 1000) / 1000))) {
            PaperEventVO vo = mBeanMap.get(Util.formatSeconds((progress + 1000) / 1000));
            switch (vo.getType()) {
                case "1"://放大
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    isFulllScreen = true;
                    break;
                case "2"://缩小
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    isFulllScreen = false;
                    break;
                case "3"://标记
                    mMyCustomView.clearData();
                    mMyCustomView.addPoint(Float.valueOf(vo.getX()), Float.valueOf(vo.getY()));
                    break;
                case "4"://调用开始
                    if (imageId.contains(vo.getTimgid())) {
                        isEvent = true;
                        int timgPositon = imageId.indexOf(vo.getTimgid());
                        viewpagerPaper.setCurrentItem(timgPositon);
                    } else {
                        LogUtils.v("图片事件信息异常，无调用id");
                    }
                    break;
                case "5"://调用结束
                    viewpagerPaper.setCurrentItem(position);
                    isEvent = false;
                    break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //手动调节进度
        //seekbar的拖动位置
        int dest = seekBar.getProgress();
        //seekbar的最大值
        int max = seekBar.getMax();
        //调用service调节播放进度
 /*       PlayEvent playEvent = new PlayEvent();
        playEvent.setAction(PlayEvent.Action.SEEK);
        Log.i("--->>", "onStopTrackingTouch: max" + max + "  dest" + dest);
        playEvent.setMax(max);
        playEvent.setDest(dest);
        EventBus.getDefault().post(playEvent);*/
        Log.i("--->>", "onStopTrackingTouch: " + max + "    " + dest);
        if (getPlayService() != null)
            getPlayService().seekTo(dest);
    }

    @Override
    public void onPublish(int progress) {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onPublish(progress);
        tvPlayTime.setText(Util.formatSeconds((progress + 1000) / 1000));
        seekBarPaper.setProgress(progress);
    }

    @Override
    public void onDuration(int duration) {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onDuration(duration);
        //        seekBarPaper.setMax(duration);
        Log.i("--->>", "onDuration: " + duration);
    }

    //播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
//        if (onPlayerEventListener != null)
//            onPlayerEventListener.onCompletion(mp);

//        List<Song> songs = AppCache.getPlayService().getmMusicList();
//        Log.i("--->>", "2onCompletion: " + songs.size() + "   " + position);
//        if (songs.size() == (position + 1)) {
//            Log.i("--->>", "1onCompletion: " + songs.size() + "   " + position);
//            note.setType("plays");
//            note.setId(storageId);
//            note.setPlayPosition(position);
//            note.setSongsSize(songs.size());
//            noteDao.insertOrReplace(note);
//        }
        sendPlayNote();

        if (getPlayService() != null) {
            if (position < imageId.size() - 1) {
                position++;
                getImageInfo();
            } else {
                if (imageId.size() > 0) {
                    position = 0;
                    getImageInfo();
                }
            }
        }

    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onBufferingUpdate(percent);
        Log.i("--->>", "onBufferingUpdate: " + percent);
        int max = seekBarPaper.getMax();
        seekBarPaper.setSecondaryProgress(max / 100 * percent);
    }

    @Override
    public void onChange(Song music) {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onChange(music);

        if (music == null) {
            return;
        }
        if (getPlayService() != null) {
            progressPopupWindow.showPopupWindow();
            if (AppCache.getPlayService().isPlaying() || AppCache.getPlayService().isPreparing()) {
                ivPlayStop.setSelected(true);
            } else {
                ivPlayStop.setSelected(false);
            }
            int position = music.getPosition();
            viewpagerPaper.setCurrentItem(position);
            tvDuration.setText("/ " + Util.formatSeconds(music.getDuration()));

            recyclerView.scrollToPosition(position);
            itemClickAdapter.setSelectedPosition(position);
            itemClickAdapter.notifyDataSetChanged();
            seekBarPaper.setMax((music.getDuration() - 1) * 1000);
            seekBarPaper.setProgress(0);
            List<Song> songs = AppCache.getPlayService().getmMusicList();
            note.setType("play");
            note.setId(storageId);
            note.setLanguage(language);
            Log.i("--->>", "onChange: " + position);
            Log.i("--->>", "onChange: " + language);
            note.setPlayPosition(position);
            note.setSongsSize(songs.size());
            note.setTime(String.valueOf(System.currentTimeMillis()));
            if (note.getTitle() != null)
                noteDao.insertOrReplace(note);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        progressPopupWindow.dismiss();
        if (onPlayerEventListener != null)
            onPlayerEventListener.onPrepared(mp);
    }

    @Override
    public void onPlayerPause() {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onPlayerPause();
        ivPlayStop.setSelected(false);
    }

    @Override
    public void onPlayerResume() {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onPlayerResume();
        ivPlayStop.setSelected(true);
    }

    @Override
    public void onTimer(long remain) {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onTimer(remain);
    }

    @Override
    public void onMusicListUpdate() {
        if (onPlayerEventListener != null)
            onPlayerEventListener.onMusicListUpdate();
    }


    private class ImageAdapter extends PagerAdapter {

        private List<String> viewpathlist;
        private Activity activity;
        private View view;
        private VideoView videoView;

        public ImageAdapter(Activity activity, ArrayList<String> viewpathlist) {
            this.viewpathlist = viewpathlist;
            this.activity = activity;
            initImageView();
        }

        @SuppressLint("ClickableViewAccessibility")
        private void initImageView() {
            mViewList = new ArrayList<View>();
            mVideoViewMap = new HashMap<Integer, WrapVideoView>();
            mVideoBgViewMap = new HashMap<Integer, View>();
            mMediaControllerMap = new HashMap<Integer, MediaController>();
            mCacheViewList = new ArrayList<View>();

            mCurrentPositions = new HashMap<>();
            mIsVideo = new HashMap<>();
            // mIsPageFirstAvaliable = new HashMap<Integer, Boolean>();

            for (int i = 0; i < viewpathlist.size(); i++) {
                String path = viewpathlist.get(i);
                if ("mp4".equals(FileUtils.parseSuffix(path))) {
                    path = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
                    View videoview_layout = (View) View.inflate(activity, R.layout.item_paper_videoview,
                            null);
                    WrapVideoView videoview = (WrapVideoView) videoview_layout.findViewById(R.id.videoview);
                    View mVideoBgView = (View) videoview_layout.findViewById(R.id.iv_bg);
                    ImageButton imPlayBtn = videoview_layout.findViewById(R.id.imbtn_video_play);
                    imPlayBtn.setOnClickListener(new ImgBtnClickLister());
                    mVideoBgView.setBackground(new BitmapDrawable(getVideoBitmap(path)));

                    MediaController mpc = new MediaController(activity);
                    videoview.setVideoPath(path);
                    videoview.setMediaController(mpc);
                    videoview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (videoview.isPlaying()) {
                                mpc.show();
                            } else {
                                mpc.hide();
                                imPlayBtn.setVisibility(View.VISIBLE);
                            }
                        }
                    });
//                    setListener(vv);
                    mViewList.add(videoview_layout);
                    mVideoViewMap.put(i, videoview);
                    mMediaControllerMap.put(i, mpc);
                    mVideoBgViewMap.put(i, mVideoBgView);
                    mCurrentPositions.put(i, 0);// 每个页面的初始播放进度为0
                    mIsVideo.put(i, true);// 每个页面的初始播放状态false
                } else {
                    mIsVideo.put(i, false);
                    ImageView imageView = new ImageView(activity);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLoader.getInstance().displayImage((BaseActivity) activity, path, imageView);
                    ViewParent vp = imageView.getParent();
                    if (vp != null) {
                        ViewGroup parent = (ViewGroup) vp;
                        parent.removeView(imageView);
                    }
                    mViewList.add(imageView);
                    imageView.setOnTouchListener((view, motionEvent) -> {
                        mGestureDetector.onTouchEvent(motionEvent);
                        return true;
                    });
                }
            }
        }

        class ImgBtnClickLister implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getVisibility() == View.VISIBLE) {
                    mVideoViewMap.get(currentViewPagerItem).start();
                    v.setVisibility(View.GONE);
                    mVideoBgViewMap.get(currentViewPagerItem).setVisibility(View.GONE);
                }
            }
        }

        public Bitmap getVideoBitmap(String mVideoUrl) {
            Bitmap bitmap = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            int kind = MediaStore.Video.Thumbnails.MINI_KIND;
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(mVideoUrl, new HashMap<String, String>());
            } else {
                retriever.setDataSource(mVideoUrl);
            }
            bitmap = retriever.getFrameAtTime();
            retriever.release();

            return bitmap;
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(mViewList.get(position % mViewList.size()));
            //Warning：不要在这里调用removeView
//            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        GestureDetector mGestureDetector = new GestureDetector(PaperDetailsActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (getPlayService() != null)
                    getPlayService().play(position);
                return true;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("--->>", "onKeyDown: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (ObjectUtils.isNotEmpty(ACTIVITYNAME))
                    startActivity(new Intent(this, Class.forName(ACTIVITYNAME)));
                else
                    startActivity(new Intent(this, Class.forName("GiiispActivity")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    @SuppressLint("CheckResult")
    public void loadDownloadNunber() {
        RxDownload.getInstance(this).getTotalDownloadRecords()
                .map(downloadRecords -> {
                    List<String> missionIds = new ArrayList<>();
                    for (DownloadRecord each : downloadRecords) {
                        if (each.getFlag() != DownloadFlag.COMPLETED
                                && each.getExtra1() != null
                                && !missionIds.contains(each.getExtra1())) {
                            missionIds.add(each.getExtra1());
                        }
                    }
                    return missionIds;
                })
                .subscribe(downloadBeen -> {
                    downloadNunber = downloadBeen.size();
                    tvDownloadNumber.setText(downloadNunber + "");
                });
    }
  /*  public View getTabItemView(int position) {//                mTitles.add("问答");
        TabLayout.Tab tabAt0 = tabLayoutPaper.getTabAt(0);
        TabLayout.Tab tabAt1 = tabLayoutPaper.getTabAt(1);
        TabLayout.Tab tabAt2 = tabLayoutPaper.getTabAt(2);
        if (tabAt0 != null) {
            tabAt0.setIcon(R.drawable.bg_tab_mine);
        }
        if (tabAt1 != null)
            tabAt1.setIcon(R.drawable.bg_tab_center);
        if (tabAt2 != null)
            tabAt2.setIcon(R.drawable.bg_tab_tag);
        String[] tabTitle = {"问答", "文献索引","标签"};
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab_paper, tabLayoutPaper, false);
        TextView textView = view.findViewById(R.id.tv_tab_text);
        textView.setText(tabTitle[position]);
        textView.setCompoundDrawables(null,getDrawable());
        return view;
    }*/

    /**
     * 设置Tablayout上的标题的角标
     */
    private void initTab() {

        // 1. 最简单
        //        for (int i = 0; i < mFragmentList.size(); i++) {
        //            mBadgeViews.get(i).setTargetView(((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(i));
        //            mBadgeViews.get(i).setText(formatBadgeNumber(mBadgeCountList.get(i)));
        //        }

        // 2. 最实用
        for (int i = 0; i < tabLayoutPaper.getChildCount(); i++) {
            TabLayout.Tab tab = tabLayoutPaper.getTabAt(i);

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = null;
            if (tab == null) {
                return;
            }

            customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(getTabItemView(i));
        }

        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        //        mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }


    public View getTabItemView(int position) {
        String[] tabTitle = {getString(R.string.questions_answers), getString(R.string.literature_index), getString(R.string.label)};
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab_paper, tabLayoutPaper, false);
        TextView textView = view.findViewById(R.id.tv_tab_text);
        textView.setText(tabTitle[position]);

        return view;
    }


    public static void checkPwd(Context context, String pid, ArrayList<String> v, String type) {
        final EditText inputServer = new EditText(context);
        inputServer.setPadding(50, 50, 50, 50);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_checkpwd)).setView(inputServer).setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        ArrayMap<String, Object> map = new ArrayMap<>();
                        map.put("pwd", inputName);
                        map.put("pid", pid);
                        ModelFactory.getBaseModel().checkPaperPwd(map, new Callback<BaseEntity>() {
                            @Override
                            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                                if (response.isSuccessful()) {
                                    // response.body() 返回 ResponseBody
                                    BaseEntity entity = response.body();
                                    if (entity.getResult() == 1) {
                                        String version = "1";
                                        if (v != null && v.size() > 0) {
                                            version = v.get(0);
                                        }
                                        PaperDetailsActivity.actionActivityNew(context, pid, version, type, CN, ACTIVITYNAME);
                                    } else {
                                        Utils.showToast(entity.getInfo());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseEntity> call, Throwable t) {

                            }
                        });
                    }
                });
        builder.show();
    }


    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "204"://获取论文信息
                PaperInfoBean bean = (PaperInfoBean) baseBean;
                if (bean == null) {
                    return;
                }
                if (getPlayService() != null)
                    getPlayService().setPaperInfoBean(bean);
                imageId.clear();
                myVersionNo = bean.getVersion();
                pid = bean.getId();
                //完整
                tvPaperComplete.setVisibility(!"2".equals(myVersionNo) && "1".equals(bean.getTwo())
                        ? View.VISIBLE : View.GONE);
                //摘要
                tvPaperAbstract.setVisibility(!"3".equals(myVersionNo) && "1".equals(bean.getThree())
                        ? View.VISIBLE : View.GONE);
                //精华
                tvPaperMarrow.setVisibility(!"4".equals(myVersionNo) && "1".equals(bean.getFour())
                        ? View.VISIBLE : View.GONE);
                twoId = bean.getTwoid();
                threeId = bean.getThreeid();
                fourId = bean.getFourid();

                llEmptyView.setVisibility(View.GONE);
                if (photoList != null) {
                    photoList.clear();
                } else {
                    photoList = new ArrayList<>();
                }
                for (PaperInfoVO vo : bean.getImglist()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setPaperInfoVO(vo);
                    itemClickAdapter.addData(entity);
                    photoList.add(BASE_IMG_URL + vo.getUrl());
//                    photoList.add("abc.mp4");
                    imageId.add(vo.getId());
                }

                getPlayService().setImageList(photoList);

                if (!TextUtils.isEmpty(bean.getDigest()))
                    title = bean.getDigest();

                viewpagerPaper.setAdapter(new ImageAdapter(this, photoList));
                if (bean.getImglist() != null && bean.getImglist().size() != 0) {
                    position = 0;
                    getImageInfo();
                    viewpagerPaper.setCurrentItem(0);
                    paperQA.setImageId(imageId.get(position));
                    paperQA.initNetwork();
                }
                isFollowed = bean.getIsfollow();
                ivLikedIcon.setSelected("1".equals(isFollowed));
                break;
            case "317"://获取待配音预览论文信息
                DubbingBean dubbingBean = (DubbingBean) baseBean;
                if (dubbingBean == null) {
                    return;
                }
                llEmptyView.setVisibility(View.GONE);
                if (photoList != null) {
                    photoList.clear();
                } else {
                    photoList = new ArrayList<>();
                }
                for (DubbingVO vo : dubbingBean.getList()) {
                    PaperInfoVO infoVO = new PaperInfoVO();
                    infoVO.setId(vo.getPcid());
                    infoVO.setUrl(vo.getUrl());
                    ClickEntity entity = new ClickEntity();
                    entity.setPaperInfoVO(infoVO);
                    itemClickAdapter.addData(entity);
                    photoList.add(BASE_IMG_URL + vo.getUrl());
                    imageId.add(vo.getPcid());
                }
//                if (!TextUtils.isEmpty(bean.getDigest()))
//                    title = bean.getDigest();

                viewpagerPaper.setAdapter(new ImageAdapter(this, photoList));
                if (dubbingBean.getList() != null && dubbingBean.getList().size() != 0) {
                    position = 0;
                    getImageInfo();
                    viewpagerPaper.setCurrentItem(0);
                }
                break;
            case "205"://获取论文语音
                ImgInfoBean bean1 = (ImgInfoBean) baseBean;
                if (bean1.getCnrecord() != null) {
                    songCN = new Song();
                    songCN.setPath(BASE_IMG_URL + bean1.getCnrecord().getUrl());
                    songCN.setDuration(Integer.parseInt(bean1.getCnrecord().getDuration()));
                    songCN.setTitle(bean1.getCnrecord().getId() + "音频");
                    songCN.setPhotoPath(photoList.get(position));
                    songCN.setPosition(position);
                } else {
                    songCN = null;
                }

                if (bean1.getEnrecord() != null) {
                    songEN = new Song();
                    songEN.setPath(BASE_IMG_URL + bean1.getEnrecord().getUrl());
                    songEN.setDuration(Integer.parseInt(bean1.getEnrecord().getDuration()));
                    songEN.setTitle(bean1.getEnrecord().getId() + "音频");
                    songEN.setPhotoPath(photoList.get(position));
                    songEN.setPosition(position);
                } else {
                    songEN = null;
                }
                Log.d("====", "准备播放了");
                if (songCN != null && songEN != null) {
                    if (CN.equals(language)) {
                        getPlayService().play(songCN);
                    } else if (EN.equals(language)) {
                        getPlayService().play(songEN);
                    } else {
                        getPlayService().play(songCN);
                    }
                } else if (songCN == null && songEN != null) {
                    changeLanguage(false);
                } else if (songEN == null && songCN != null) {
                    changeLanguage(true);
                } else {
                    ToastUtils.showShort("音频获取失败");
                }
                break;
            case "213":
                isFollowed = "1";
                ToastUtils.showShort("收藏成功！");
                ivLikedIcon.setSelected(true);
                break;
            case "216":
                DownloadInfoBean infoBean = (DownloadInfoBean) baseBean;
                if (infoBean == null) {
                    ToastUtils.showShort("下载信息异常，请重试");
                    break;
                }
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
                loadDownloadNunber();

                break;
            case "217":
                isFollowed = "2";
                ToastUtils.showShort("取消收藏成功！");
                ivLikedIcon.setSelected(false);
                break;
            case "305":
                PaperEventBean bean2 = (PaperEventBean) baseBean;
                Log.v("====", "事件获取完成,个数" + bean2.getList().size());
                mBeanMap = new HashMap<>();
                timeString = new ArrayList<>();
                for (PaperEventVO vo : bean2.getList()) {
                    mBeanMap.put(Util.formatSeconds(Integer.parseInt(vo.getTime())), vo);
                    timeString.add(Util.formatSeconds(Integer.parseInt(vo.getTime())));
                }
            default:
                break;
        }

    }

    @Override
    public void onFailNew(String url, String msg) {

    }

    //获取图片地址及事件
    private void getImageInfo() {
        mMyCustomView.clearData();
        mBeanMap = new HashMap<>();
        timeString = new ArrayList<>();
        isEvent = false;
        getPlayService().setPlayPosition(position);
        HashMap<String, Object> map = new HashMap<>();
        map.put("imgid", imageId.get(position));
        map.put("pid", pid);
        map.put("language", language);
        presenter.getDataAll("305", map);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map = new HashMap<>();
        map.put("iid", imageId.get(position));
        presenter.getDataAll("205", map);
    }

    //保存播放记录
    private void sendPlayNote() {
        if ((nowProgress / 1000) != 0 && !"wait_dubbing".equals(type) && !"download_paper".equals(type)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uid", getUserID());
            map.put("pid", pid);
            map.put("picid", imageId.get(position));
            map.put("language", language);
            map.put("stoptime", (nowProgress / 1000) + 1);
            map.put("duration", (nowProgress / 1000) + 1);
            presenter.getDataAll("215", map);
        }
    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

}
