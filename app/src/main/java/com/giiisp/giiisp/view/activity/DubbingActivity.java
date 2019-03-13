package com.giiisp.giiisp.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.common.ImageviewDouble;
import com.giiisp.giiisp.common.MutipleTouchViewPager;
import com.giiisp.giiisp.common.MyOnCompletion;
import com.giiisp.giiisp.common.ScaleAttrsImageView;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.DubbingBean;
import com.giiisp.giiisp.dto.DubbingListVO;
import com.giiisp.giiisp.dto.DubbingVO;
import com.giiisp.giiisp.dto.MarkBean;
import com.giiisp.giiisp.dto.MarkVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.PlayEvent;
import com.giiisp.giiisp.entity.SubscribeEntity;
import com.giiisp.giiisp.model.GlideApp;
import com.giiisp.giiisp.utils.ImageLoader;
import com.giiisp.giiisp.utils.SDFileHelper;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.widget.MyCustomView;
import com.giiisp.giiisp.widget.ProgressPopupWindow;
import com.giiisp.giiisp.widget.WrapVideoView;
import com.giiisp.giiisp.widget.recording.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;
import static com.giiisp.giiisp.widget.recording.AppCache.getPlayService;

/**
 * 配音的页面
 * Created by Thinkpad on 2017/6/5.
 */
public class DubbingActivity extends DubbingPermissionActivity implements
        BaseQuickAdapter.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        MyCustomView.DrawListen,
        ImageviewDouble,
        MyOnCompletion {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewPager)
    MutipleTouchViewPager viewPager;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_dubbing_audition)
    TextView tvDubbingAudition;
    @BindView(R.id.tv_dubbing_audition_new)
    TextView tvDubbingAuditionNew;
    @BindView(R.id.tv_dubbing_re_record)
    TextView tvDubbingReRecord;
    @BindView(R.id.tv_dubbing_determine)
    TextView tvFinish;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.linear_layout)
    RelativeLayout mRlFinish;
    @BindView(R.id.recycler_view_dubbing)
    RecyclerView recyclerViewDubbing;
    @BindView(R.id.iv_dubbing)
    ImageView ivDubbing;
    @BindView(R.id.cv_mark)
    MyCustomView mMyCustomView;
    @BindView(R.id.img_mark)
    CheckBox mCbMark;
    @BindView(R.id.rl_big)
    RelativeLayout mRlBig;
    @BindView(R.id.tv_use)
    TextView mTvUse;
    @BindView(R.id.btn_use)
    ImageView mBtnUse;
    @BindView(R.id.img_full)
    ImageView mImgFull;
    @BindView(R.id.btn_small)
    Button mBtnSmall;
    @BindView(R.id.btn_full)
    Button mBtnFull;
    @BindView(R.id.btn_solo)
    Button mBtnSolo;
    @BindView(R.id.tv_mark)
    TextView mTvMark;
    @BindView(R.id.rl_full)
    RelativeLayout mRlFull;
    @BindView(R.id.ll_dubbing_all)
    LinearLayout mLlDubbingAll;//录音界面
    @BindView(R.id.ll_add_mark)
    RelativeLayout mLlAddMark;//选标签界面
    @BindView(R.id.rv_mark)
    RecyclerView mRvMark;//标签列表


    String typeActivity;
    //    private ItemClickAdapter itemClickAdapter;
    private PlayEvent playEvent = new PlayEvent();
    private ItemClickAdapter itemClickAdapte;
    private ItemClickAdapter mMarkAdapter;
    private ImageAdapter mImageAdapter;

    private int dubbingPosition = -1;
    private int abc = -1;
    private int markTime = 0;
    //    private ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX> recordRows;
//    private ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX> photoRows;
    private DubbingListVO mVO;

    private int language = 0;
    private boolean canMark = false;
    private boolean showDiaoYong = false;
    private int myType = 0;
    private String pid = "";//论文id
    private String imgId = "";//录音图片id
    private List<ClickEntity> dataList = new ArrayList<>();
    private List<ClickEntity> markList = new ArrayList<>();
    private boolean isDubbing = false;
    private int videoAllTime = 0;
    private boolean isDiaoYong = false; // 是否正在调用图片
    private List<String> imgMarkList = new ArrayList<>();
    private boolean playUrl = false;//是否播放已经上传的录音

//    private boolean isFinish = true;

    /*** 页面的视频控件集合,Integer所处位置 ***/
    static Map<Integer, WrapVideoView> mVideoViewMap = new HashMap<>();
    static Map<Integer, View> mVideoBgViewMap = new HashMap<>();

    /*** 记录每个page页面视频播放的进度 ***/
    static Map<Integer, Integer> mCurrentPositions;

    /*** 记录当前page页面是否为视频 ***/
    static Map<Integer, Boolean> mIsVideo;

    private List<String> markFinishList = new ArrayList<>();


    public static void actionActivity(Context context) {
        Intent sIntent = new Intent(context, DubbingActivity.class);

        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    public static void actionActivity(Activity context, String id, ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX> photoRows, ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX> recordRows, int language, String type) {
        Intent sIntent = new Intent(context, DubbingActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putExtra("language", language);
        sIntent.putParcelableArrayListExtra("recordRows", recordRows);
        sIntent.putParcelableArrayListExtra("photoRows", photoRows);
//        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(sIntent, 3000);
    }

    public static void actionActivity(Activity context, String id, int language, String type) {
        Intent sIntent = new Intent(context, DubbingActivity.class);
        sIntent.putExtra("id", id);
        sIntent.putExtra("type", type);
        sIntent.putExtra("language", language);
        context.startActivityForResult(sIntent, 3000);
    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_dubbing;
    }

    @Override
    public void initData() {
        if (getPlayService() != null && getPlayService().isPlaying())
            getPlayService().playPause();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //屏幕不息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.initData();
        hideImg();
        language = getIntent().getIntExtra("language", 0);
        typeActivity = getIntent().getStringExtra("type");
        pid = getIntent().getStringExtra("id");
//        recordRows = getIntent().getParcelableArrayListExtra("recordRows");
//        photoRows = getIntent().getParcelableArrayListExtra("photoRows");
        mMyCustomView.setDrawListen(this);
        getImageData();
        progressPopupWindow = new ProgressPopupWindow(this);

        tvPlayOld = tvDubbingAudition;
        tvPlayNew = tvDubbingAuditionNew;
    }

    @Override
    public void initView() {
        super.initView();
        tvTime = findViewById(R.id.tv_time);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRvMark.setLayoutManager(manager);
        mMarkAdapter = new ItemClickAdapter(this, R.layout.item_mark_layout, markList);
        mRvMark.setAdapter(mMarkAdapter);
        tvHint.setText(R.string.click_start_recording);
        //        tvRight.setText("保存");
        tvTitle.setText(R.string.dubbing);
//        itemClickAdapte.setSelectedPosition(position);
//        itemClickAdapte.notifyDataSetChanged();
//        recyclerView.scrollToPosition(position);
//        viewPager.setCurrentItem(position);
        //        recyclerViewDubbing.setLayoutManager(new LinearLayoutManager(this));
        //        itemClickAdapter = new ItemClickAdapter(this, R.layout.item_dubbing_sound, null, "dubbing_sound");
        //        recyclerViewDubbing.setAdapter(itemClickAdapter);
        initpaly();
        TelephonyManager telephony = (TelephonyManager) getSystemService(
                Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
                             @Override
                             public void onCallStateChanged(int state, String incomingNumber) {
                                 Log.i("--->>", "[Listener]电话号码:" + incomingNumber);
                                 switch (state) {
                                     case TelephonyManager.CALL_STATE_RINGING:
                                         Log.i("--->>", "[Listener]等待接电话:" + incomingNumber);
                                         break;
                                     case TelephonyManager.CALL_STATE_IDLE:
                                         Log.i("--->>", "[Listener]电话挂断:" + incomingNumber);
                                         break;
                                     case TelephonyManager.CALL_STATE_OFFHOOK:
                                         Log.i("--->>", "[Listener]通话中:" + incomingNumber);
                                         break;
                                 }
                                 resolvePause();
                                 super.onCallStateChanged(state, incomingNumber);
                             }
                         },
                PhoneStateListener.LISTEN_CALL_STATE);
  /*      PlayEvent playEvent = new PlayEvent();
        playEvent.setAction(PlayEvent.Action.DUBBING);
        playEvent.setHandler(handler);
        EventBus.getDefault().post(playEvent);*/

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        presenter.getDataAll("343", map);
    }

    private void getImageData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("language", language);
        presenter.getDataAll("317", map);
    }


    public void toggleRecording(View v) {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (mIsRecord) {//暂停
                    //这里判断如果是视频的话 并且 录音时间大于视频时间 才可以暂停
                    if (isVideo(dubbingPosition)
                            && recorderSecondsElapsed < videoAllTime) {
                        ToastUtils.showShort("录音时长需大于或等于视频时长");
                        return;
                    }
                    resolvePause();
                } else {//开始
                    resolveRecord();
                    tvHint.setText(R.string.is_dubbing);
//                    mRlFinish.setVisibility(View.GONE);
                    setFinishLayout(false, false, true);
                    if (isVideo(dubbingPosition)) {
                        mImageAdapter.setVolume0();
                        if (mVideoViewMap != null && mVideoViewMap.get(dubbingPosition) != null)
                            mVideoViewMap.get(dubbingPosition).start();
                        if (mVideoBgViewMap != null && mVideoBgViewMap.get(dubbingPosition) != null)
                            mVideoBgViewMap.get(dubbingPosition).setVisibility(View.GONE);
                    }
                    //                    DubbingPermissionActivityPermissionsDispatcher.resolveRecordWithCheck(DubbingActivity.this);
                }
            }
        });
    }

    @Override
    public void togglePlaying(View v) {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (mIsPlay) {
                    resolvePausePlayRecord();
                    tvDubbingAudition.setSelected(false);
                } else {
                    resolvePlayRecord();
                    tvDubbingAudition.setSelected(true);
                }
            }
        });
        super.togglePlaying(v);
    }

    public void restartRecording(View v) {
        resolveResetPlay();
    }


    @Override
    protected void startTimer() {
        ivBtn.setImageResource(R.mipmap.btn_zanting_new);
        super.startTimer();
    }

    /**
     * 暂停
     */
    @Override
    protected void resolvePause() {
        if (!mIsRecord)
            return;
        if (mRecorder.isPause()) {//继续录音
            if (mVideoViewMap != null && mVideoViewMap.get(dubbingPosition) != null)
                mVideoViewMap.get(dubbingPosition).start();
            isDubbing = true;
            tvHint.setText(R.string.is_dubbing);
            ivBtn.setImageResource(R.mipmap.in_recording);
            mRecorder.setPause(false);
//            mRlFinish.setVisibility(View.GONE);
            setFinishLayout(false, false, true);
            resolvePausePlayRecord();
            startTimer();
//            if (isVideo(dubbingPosition)) {
//                mBtnSolo.setVisibility(View.VISIBLE);
//                //这里需要静音
//                mImageAdapter.setVolume0();
//            }
        } else {//暂停录音
            if (mVideoViewMap != null && mVideoViewMap.get(dubbingPosition) != null)
                mVideoViewMap.get(dubbingPosition).pause();
            isDubbing = false;
            stopTimer();
            mRecorder.setPause(true);
            tvHint.setText(R.string.dubbing_pause);
            isPause = false;
            ivBtn.setImageResource(R.mipmap.btn_dubbing_before);
//            mRlFinish.setVisibility(View.VISIBLE);
            setFinishLayout(true, true, true);
//            tvFinish.setVisibility(View.VISIBLE);
//            tvDubbingAudition.setVisibility(View.VISIBLE);

//            if (isVideo(dubbingPosition)) {
//                mBtnSolo.setVisibility(View.INVISIBLE);
            //这里需要播放原视频声音
//                mImageAdapter.setVolumeSystem();
//            }
        }

    }


    @OnClick({R.id.tv_back, R.id.iv_btn, R.id.tv_right, R.id.iv_dubbing,
            R.id.tv_dubbing_audition, R.id.tv_dubbing_determine,
            R.id.tv_dubbing_re_record, R.id.iv_left_slip, R.id.iv_right_slide,
            R.id.tv_mark, R.id.img_mark, R.id.btn_use, R.id.tv_use, R.id.btn_solo,
            R.id.tv_dubbing_re_record_mark, R.id.tv_dubbing_determine_mark,
            R.id.tv_dubbing_audition_new,
            R.id.btn_yes, R.id.btn_no, R.id.btn_full, R.id.img_full, R.id.btn_small})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mark://开始标记
                mCbMark.setChecked(!canMark);
            case R.id.img_mark:
                canMark = !canMark;
                mMyCustomView.setCanMark(canMark);
                break;
            case R.id.tv_use://调用
            case R.id.btn_use:
                if (!showDiaoYong) {//没显示调用弹窗
                    if ("调用".equals(ToolString.getString(mTvUse))) {
                        showDiaoYong = true;
                        mRlBig.setVisibility(View.VISIBLE);
                        //弹出调用弹窗后暂停录音
                        resolvePause();
                        viewPager.setDiaoYong(true);
//                        mTvUse.setText(showDiaoYong ? "调用" : "返回");
                    } else if ("返回".equals(ToolString.getString(mTvUse))) {
                        sendData304(0, 0, 5);
                        isDiaoYong = false;
                        viewPager.setDiaoYong(false);
                        // 这里需要跳到录音的图片页
                        setImageStatus(dubbingPosition);
                        if (isDubbing) {
                            setFinishLayout(false, false, true);
                        } else {
                            setFinishLayout(true, true, true);
                        }
                    }
                } else {//正在显示调用弹窗，不做操作

                }
                break;
            case R.id.btn_yes://调用-确定
                resolvePause();//点击调用后继续录音
                sendData304(0, 0, 4);
                showDiaoYong = false;
                isDiaoYong = true;
                mRlBig.setVisibility(View.GONE);
                mTvUse.setText("返回");
                Glide.with(this).load(R.mipmap.img_quxiao).into(mBtnUse);
                viewPager.setDiaoYong(true);
                break;
            case R.id.btn_no://调用-取消
                resolvePause();//点击不调用后继续录音
//                sendData304(0, 0, 5);
                showDiaoYong = false;
                mRlBig.setVisibility(View.GONE);
                mTvUse.setText("调用");
                Glide.with(this).load(R.mipmap.img_diaoyong).into(mBtnUse);
                break;
            case R.id.tv_back:
                back = true;
                finish();
                break;
            case R.id.tv_dubbing_determine://完成
                viewPager.setDiaoYong(false);//重置滑动状态
                if (recorderSecondsElapsed < 2) {
                    ToastUtils.showShort("录音时间不可少于2秒");
                    return;
                }

                if (isDubbing) {
                    //这里判断如果是视频的话 并且 录音时间大于视频时间 才可以暂停
                    if (isVideo(dubbingPosition)
                            && recorderSecondsElapsed < videoAllTime) {
                        ToastUtils.showShort("录音时长需大于或等于视频时长");
                        return;
                    }

                    if (mVideoViewMap != null && mVideoViewMap.get(dubbingPosition) != null)
                        mVideoViewMap.get(dubbingPosition).pause();
                    isDubbing = false;
                    stopTimer();
                    mRecorder.setPause(true);
                    tvHint.setText(R.string.dubbing_pause);
                    isPause = false;
                    ivBtn.setImageResource(R.mipmap.btn_dubbing_before);
                }

                progressPopupWindow.showPopupWindow();
                type = 0;
                back = false;
                resolveStopRecord();
                resolvePausePlayRecord();
                isSolo = false;
                upAudio();//上传录音

                break;
            case R.id.tv_dubbing_determine_mark://保存标签
                imgMarkList = mMarkAdapter.getMarks();
                if (imgMarkList.size() == 0) {
                    ToastUtils.showShort("至少选择一个标签");
                    break;
                }
                StringBuffer buffer = new StringBuffer();
                for (String id :
                        imgMarkList) {
                    buffer.append(id);
                    buffer.append("#");
                }
                String ids = "";
                if (buffer.length() != 0) {
                    ids = buffer.substring(0, buffer.length() - 1);
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("picid", dataList.get(viewPager.getCurrentItem()).getDubbingVO().getPcid());
                map.put("ids", ids);
                map.put("language", language);
                presenter.getDataAll("345", map);
                pauseNewAudio();//停止播放试听
                tvDubbingAudition.setSelected(false);
                tvDubbingAuditionNew.setSelected(false);
                break;
            case R.id.tv_dubbing_re_record://重录
            case R.id.tv_dubbing_re_record_mark://重录(选标签)
                viewPager.setDiaoYong(false);//重置滑动状态
                pauseNewAudio();//停止播放试听
                tvDubbingAudition.setSelected(false);
                tvDubbingAuditionNew.setSelected(false);
                dataList.get(viewPager.getCurrentItem()).getDubbingVO().setRid("");
                itemClickAdapte.notifyDataSetChanged();//为了清除图片列表中的已完成的标记
                if (isVideo(viewPager.getCurrentItem())) {
                    mBtnSolo.setVisibility(View.VISIBLE);
                }
                resolveStopRecord();
                mMyCustomView.clearData();
                tvHint.setText(R.string.click_start_voice);
                recorderSecondsElapsed = 0;
                tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
                resolvePausePlayRecord();
                //清空当前图片事件
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("imgid", getImageId());
                map1.put("language", language);
                presenter.getDataAll("315", map1);
                break;
            case R.id.tv_dubbing_audition://试听
                if (playUrl) { //播放在线录音
                    if (!tvDubbingAudition.isSelected()) {
                        String url = dataList.get(viewPager.getCurrentItem()).getDubbingVO().getRurl();
                        playNewAudio(ToolString.getUrl(url));
                        tvDubbingAudition.setSelected(true);
                    } else {
                        pauseNewAudio();
                        tvDubbingAudition.setSelected(false);
                    }
                } else //播放本地录音
                    togglePlaying(view);
                break;

            case R.id.tv_dubbing_audition_new://试听（标签）
                if (!tvDubbingAuditionNew.isSelected()) {
                    String url = dataList.get(viewPager.getCurrentItem()).getDubbingVO().getRurl();
                    playNewAudio(ToolString.getUrl(url));
                    tvDubbingAuditionNew.setSelected(true);
                } else {
                    pauseNewAudio();
                    tvDubbingAuditionNew.setSelected(false);
                }
                break;

            case R.id.iv_left_slip://上一张图片
                if (viewPager.getCurrentItem() > 1) {
                    setImageStatus(viewPager.getCurrentItem() - 1);
                } else {
                    setImageStatus(0);
                }
//                viewPager.setCurrentItem(viewPager.getCurrentItem() > 1 ? viewPager.getCurrentItem() - 1 : 0);
//                //设置是否显示标记按钮
//                mTvMark.setVisibility(viewPager.getCurrentItem() == position ? View.VISIBLE : View.GONE);
//                mCbMark.setVisibility(viewPager.getCurrentItem() == position ? View.VISIBLE : View.GONE);
//                mTvUse.setVisibility(viewPager.getCurrentItem() != position ? View.VISIBLE : View.GONE);
//                mBtnUse.setVisibility(viewPager.getCurrentItem() != position ? View.VISIBLE : View.GONE);
                break;
            case R.id.iv_right_slide://下一张图片
                if (viewPager.getCurrentItem() < viewPager.getChildCount() - 1) {
                    setImageStatus(viewPager.getCurrentItem() + 1);
                } else {
                    setImageStatus(0);
                }
//                viewPager.setCurrentItem(viewPager.getCurrentItem() < viewPager.getChildCount() ? viewPager.getCurrentItem() + 1 : 0);
//                //设置是否显示标记按钮
//                mTvMark.setVisibility(viewPager.getCurrentItem() == position ? View.VISIBLE : View.GONE);
//                mCbMark.setVisibility(viewPager.getCurrentItem() == position ? View.VISIBLE : View.GONE);
//                mTvUse.setVisibility(viewPager.getCurrentItem() != position ? View.VISIBLE : View.GONE);
//                mBtnUse.setVisibility(viewPager.getCurrentItem() != position ? View.VISIBLE : View.GONE);
                break;
            case R.id.iv_btn://录音
                if (!isDubbing && ObjectUtils.isNotEmpty(dataList.get(viewPager.getCurrentItem()).getDubbingVO().getRid())) {
                    ToastUtils.showShort("该图片或视频已有录音，请点击重录后再尝试");
                    break;
                }
                if (!isDubbing) {//开始录音
                    dubbingPosition = viewPager.getCurrentItem();
                    if (isVideo(dubbingPosition)) {
                        videoAllTime = getVideoDuration(
                                BASE_IMG_URL + dataList.get(dubbingPosition).getDubbingVO().getUrl());
                    }
                    mBtnSolo.setVisibility(View.INVISIBLE);
                    isDubbing = true;
                    imgId = getImageId();
                    viewPager.setDubbing(isDubbing);
                }
                toggleRecording(view);
                break;
            case R.id.btn_full://全屏
                GlideApp.with(this)
                        .load(BASE_IMG_URL + dataList.get(viewPager.getCurrentItem()).getDubbingVO().getUrl())
                        .into(mImgFull);
//                ScreenUtils.setLandscape(this);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mRlFull.setVisibility(View.VISIBLE);
                sendData304(0, 0, 1);
                break;
            case R.id.iv_dubbing:
//                mRlFinish.setVisibility(View.VISIBLE);
                setFinishLayout(true, true, true);
                tvHint.setText(R.string.click_start_voice);
                recorderSecondsElapsed = 0;
                tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
                break;
            case R.id.img_full://全屏时的图片
                break;
            case R.id.btn_small://缩小全屏
//                ScreenUtils.setPortrait(this);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mRlFull.setVisibility(View.GONE);
                sendData304(0, 0, 2);
                break;
            case R.id.tv_right:
     /*           if (linearLayout.getVisibility() == View.VISIBLE) {
                    resolveStopRecord();
                    if (recorderSecondsElapsed != 0)
                        itemClickAdapter.setNewData(null);
                    itemClickAdapter.addData(new ClickEntity(filePath, "中文", new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault()).format(new Date()), Util.formatSecond(recorderSecondsElapsed)));
                    linearLayout.setVisibility(View.GONE);
                } else {
                    finish();
                }*/
                break;
            case R.id.btn_solo://原音
                //直接上传原音
//                filePath = "";
                isSolo = true;
                upAudio();
//                if (viewPager.getCurrentItem() < viewPager.getChildCount()) {
//                    setImageStatus(viewPager.getCurrentItem() + 1);
//                } else {
//                    setImageStatus(0);
//                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        stopNewAudio();
        super.onDestroy();
    }

    /**
     * @param key
     */
    File file;

    @Override
    protected void keyCompete(String key) {
        super.keyCompete(key);
        ArrayMap<String, Object> map = new ArrayMap<>();
//        if (photoRows != null && photoRows.size() > position) {
//            String id = photoRows.get(position).getId();
//
//            double duration = 0;
//            try {
//                file = new File(filePath);
//                long fileSize = SDFileHelper.getFileSize(new File(filePath));
//                double rint = SDFileHelper.FormetFileSize(fileSize);
//                duration = Math.rint(rint * 100) / 100;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (recordRows != null && recordRows.size() > position) {
//                String recordId = recordRows.get(position).getId();
//                map.put("id", recordId);
//            }
////            map.put("token", token);
//            map.put("uid", getUserID());
//            map.put("pcid", id);
//            map.put("size", duration);
//            map.put("duration", recorderSecondsElapsed);
//            map.put("language", language);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
//            MultipartBody.Part part = MultipartBody.Part.createFormData("recordFile", file.getName(), requestBody);
////            map.put("path", UrlConstants.RequestUrl.MP3_URL + key);
//            presenter.getSaveRecordData(map, part);
//        }

    }


    /**
     * 上传录音
     */
    public void upAudio() {
        ArrayMap<String, Object> map = new ArrayMap<>();
//        double duration = 0;
        long fileSize = 0;
        if (!isSolo) {//录音
            if (ObjectUtils.isNotEmpty(filePath)) {
                file = new File(filePath);
                try {
                    fileSize = SDFileHelper.getFileSize(new File(filePath));
//            double rint = SDFileHelper.FormetFileSize(fileSize);
//            duration = Math.rint(rint * 100) / 100;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                file = new File(Environment.getDownloadCacheDirectory().getPath() + "/test");
            }
        }
//      if (recordRows != null && recordRows.size() > position) {
//          String recordId = recordRows.get(position).getId();
//          map.put("id", recordId);
//      } else {
        map.put("id", dataList.get(dubbingPosition).getDubbingVO().getRid());//修改录音是使用，第一次录音 传 “”
//      }
//      map.put("token", token);
        map.put("uid", getUserID());
        map.put("pcid", dataList.get(dubbingPosition).getDubbingVO().getPcid());
        map.put("useinit", isSolo ? "1" : "2");//1原音2录音
        map.put("duration", (long) recorderSecondsElapsed);
        map.put("language", language); //application/x-www-form-urlencoded ,multipart/form-data
        map.put("resolution", ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight());//手机分辨率
        MultipartBody.Part part = null;
        if (!isSolo) {//录音
            map.put("size", fileSize);
            RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
            part = MultipartBody.Part.createFormData("recordFile", file.getName(), requestBody);
        } else {//原音
            map.put("size", 0);
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc.mp3");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), f);
            part = MultipartBody.Part.createFormData("recordFile", "abc.mp3", requestBody);
        }
        presenter.getSaveRecordData(map, part);
//        }


    }

    @Override
    protected void postDubbing() {
        super.postDubbing();

    }

    @Override
    protected void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mIsRecord) {
                    if (recorderSecondsElapsed - markTime >= 2) {//2秒清除标记点
                        if (mMyCustomView != null && mMyCustomView.hasPoint())
                            mMyCustomView.clearData();
                    }
                    tvTime.setText(Util.formatSeconds(recorderSecondsElapsed++));
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("--->>", "onPageScrolled: " + position);
        Log.i("--->>", "positionOffset: " + positionOffset);
        Log.i("--->>", "positionOffsetPixels: " + positionOffsetPixels);
        if (positionOffset == 0 && position != abc) {
//            if (mVideoViewMap.get(abc) != null)
//                mVideoViewMap.get(abc).pause();
            setImageStatus(position);
            abc = position;
        }

//        // 正在录音 + 滑动 = 暂停录音
//        if (position != abc && mRecorder != null && !mRecorder.isPause() && isDubbing) {
//            if (mVideoViewMap != null && mVideoViewMap.get(dubbingPosition) != null)
//                mVideoViewMap.get(dubbingPosition).pause();
//            stopTimer();
//            mRecorder.setPause(true);
//            tvHint.setText(R.string.dubbing_pause);
//            isPause = false;
//            ivBtn.setImageResource(R.mipmap.btn_dubbing_before);
//            setFinishLayout(true, true, true);
//        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("--->>", "onPageSelected: " + position);
//        if (position != abc) {
//            setImageStatus(position);
//            abc = position;
//        }
     /*   if (itemClickAdapte.getSelectedPosition() > position) {
            recyclerView.scrollToPosition(position - 2);
        } else {
            recyclerView.scrollToPosition(position + 2);
        }
        itemClickAdapte.setSelectedPosition(position);
        itemClickAdapte.notifyDataSetChanged();*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("--->>", "onPageScrollStateChanged: " + state);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (typeActivity != null)
            switch (typeActivity) {
                case "wait_dubbing":
                    setImageStatus(position);
                    abc = position;
                    break;
                case "edit_dubbing":
//                    this.position = position;

                    viewPager.setCurrentItem(position);
                    itemClickAdapte.setSelectedPosition(position);
                    itemClickAdapte.notifyDataSetChanged();
                    back = false;
                    resolveStopRecord();
                    resolvePausePlayRecord();
                    tvHint.setText(R.string.click_start_voice);
                    recorderSecondsElapsed = 0;
                    tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
                    break;
            }
    }

    private void setImageStatus(int position) {
        if (mMyCustomView != null) {
            mMyCustomView.clearData();
        }
        if (dataList != null && position < dataList.size()) {

            if (dataList != null && isVideo(position)) {

                mBtnSolo.setVisibility(isDubbing ? View.INVISIBLE : View.VISIBLE);
            } else {
                mBtnSolo.setVisibility(View.INVISIBLE);
            }
            if (viewPager.getCurrentItem() != position) {
                viewPager.setCurrentItem(position);
                viewPager.setOffscreenPageLimit(dataList.size());
            }
            itemClickAdapte.setSelectedPosition(position);
            itemClickAdapte.notifyItemChanged(abc, "123");
            itemClickAdapte.notifyItemChanged(position, "123");
//        itemClickAdapte.notifyDataSetChanged();
            canMark = false;
            mMyCustomView.setCanMark(false);
            mCbMark.setChecked(false);
            LogUtils.v("ok-dubb:" + dubbingPosition);
            LogUtils.v("ok-position:" + position);
            if (isDubbing) {//正在录音
//                ivBtn.setVisibility(this.dubbingPosition != position ? View.GONE : View.VISIBLE);
                if (this.dubbingPosition == position) {
                    ivBtn.setImageResource(R.mipmap.btn_zanting_new);
                } else {
                    ivBtn.setImageResource(R.mipmap.btn_zanting);
                }
//                mCbMark.setVisibility(this.dubbingPosition != position ? View.GONE : View.VISIBLE);
//                mTvMark.setVisibility(this.dubbingPosition != position ? View.GONE : View.VISIBLE);
                mBtnUse.setVisibility(this.dubbingPosition > position ? View.VISIBLE : View.GONE);
                if (!isVideo(position)) {
                    mRlBig.setVisibility(this.dubbingPosition > position ? View.VISIBLE : View.GONE);
                    mTvUse.setVisibility(this.dubbingPosition > position ? View.VISIBLE : View.GONE);
                    if (this.dubbingPosition > position) {//如果弹出调用选择框，则暂停录音
                        resolvePause();
                    }
                } else {
                    mRlBig.setVisibility(View.GONE);
                    mTvUse.setVisibility(View.GONE);
                }
                //正在调用 + 回到录音图片 = 调用结束
                if (isDiaoYong && dubbingPosition == position) {
                    sendData304(0, 0, 5);
                    isDiaoYong = false;
                    viewPager.setDiaoYong(false);
                }
//                setFinishLayout(false, false, this.dubbingPosition == position);
                setFinishLayout(false, false, true);
            } else {//未录音
                if (ObjectUtils.isNotEmpty(dataList.get(position).getDubbingVO().getRid())) {//已经录过音了
                    mCbMark.setVisibility(View.GONE);
                    mTvMark.setVisibility(View.GONE);
                    setFinishLayout(true, true, false);
                    playUrl = true;

                    mBtnSolo.setVisibility(View.INVISIBLE);
                } else {
                    if (isVideo(position)) {//视频不可放大
                        mBtnFull.setVisibility(View.GONE);
                    } else {//图片
                        mBtnFull.setVisibility(View.VISIBLE);
                    }
//                    mRlFinish.setVisibility(View.GONE);
                    if (recorderSecondsElapsed == 0 || dubbingPosition != position)//暂停录音 + 调用返回 = 录音时间不为0 或者当前图片为非录音图片（不重置完成布局）
                        setFinishLayout(false, false, false);
                    else //暂停录音 + 有录音 + 回到当前录音图片位置 = 继续上传
                        setFinishLayout(true, true, true);

                    mCbMark.setVisibility(View.VISIBLE);
                    mTvMark.setVisibility(View.VISIBLE);
                }
                mRlBig.setVisibility(View.GONE);
                mBtnUse.setVisibility(View.GONE);
                mTvUse.setVisibility(View.GONE);
                if (dubbingPosition != position) {//暂停配音
                    resolveStopRecord();
                    mMyCustomView.clearData();
                    tvHint.setText(R.string.click_start_voice);
                    recorderSecondsElapsed = 0;
                    tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
                }
            }

            boolean isFinish = true;
            for (ClickEntity entity : dataList) {
                if (ObjectUtils.isEmpty(entity.getDubbingVO().getRid())) {
                    isFinish = false;
                    break;
                }
            }

            if (isFinish) {//全录完了
                mLlDubbingAll.setVisibility(View.GONE);
                mLlAddMark.setVisibility(View.VISIBLE);
                //获取图片标签
                HashMap<String, Object> map = new HashMap<>();
                map.put("picid", dataList.get(position).getDubbingVO().getPcid());
                map.put("language", language);
                presenter.getDataAll("344", map);
            } else {
                mLlDubbingAll.setVisibility(View.VISIBLE);
                mLlAddMark.setVisibility(View.GONE);
            }
        } else {
            ToastUtils.showShort("信息异常，请重试");
        }
    }

    @Override
    public void drawListen(float x, float y) {
        markTime = recorderSecondsElapsed;//标记点时间
        sendData304(x, y, 3);
    }

    @Override
    public void onMyCompletion(MediaPlayer videoView) {
//        if (isDubbing) {//如果还在录音的话重新播放视频
        videoView.start();
        videoView.setLooping(true);
//        } else {
//            这里需要上传原音
//            filePath = "";
//            upAudio(true);
//        }
    }

    @Override
    public void doubleClick(float x, float y, int scale) {
        sendData304(x, y, scale);
    }

    private class ImageAdapter extends PagerAdapter {

        private List<DubbingVO> viewlist;
        private BaseActivity activity;
        private MediaPlayer mMediaPlayer;
        private int volume;
        private MyOnCompletion mOnCompletion;
        private ImageviewDouble mDouble;
//        private WrapVideoView videoview;

        ImageAdapter(BaseActivity activity, List<DubbingVO> viewlist, MyOnCompletion onCompletion, ImageviewDouble imageviewDouble) {
            this.viewlist = viewlist;
            this.activity = activity;
            this.mDouble = imageviewDouble;
            this.mOnCompletion = onCompletion;
            AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            volume = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return viewlist.size();
        }

        public void setVolume0() {
            mMediaPlayer.setVolume(0, 0);
        }

        public void setVolumeSystem() {
            mMediaPlayer.setVolume(volume, volume);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            //Warning：不要在这里调用removeView
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项
            position %= viewlist.size();
            if (position < 0) {
                position = viewlist.size() + position;
            }
            String path = viewlist.get(position).getUrl();
//            String path = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
            if (path.contains("mp4")) {
                View videoview_layout = View.inflate(activity, R.layout.item_paper_videoview,
                        null);
                WrapVideoView videoview = videoview_layout.findViewById(R.id.videoview);
                View mVideoBgView = videoview_layout.findViewById(R.id.iv_bg);
                ImageButton imPlayBtn = videoview_layout.findViewById(R.id.imbtn_video_play);
                imPlayBtn.setVisibility(View.GONE);//暂时隐藏
//                imPlayBtn.setBackground(activity.getResources().getDrawable(R.mipmap.main_stop));
                imPlayBtn.setOnClickListener(v -> {
                    mVideoBgView.setVisibility(View.GONE);
                    if (mVideoViewMap.get(viewPager.getCurrentItem()).isPlaying()) {
                        mVideoViewMap.get(viewPager.getCurrentItem()).pause();
//                        imPlayBtn.setBackground(activity.getResources().getDrawable(R.mipmap.main_stop));
                    } else {
//                        imPlayBtn.setBackground(activity.getResources().getDrawable(R.mipmap.main_play));
                        v.setVisibility(View.GONE);
                        mVideoViewMap.get(viewPager.getCurrentItem()).start();
                        mVideoBgView.setVisibility(View.GONE);
                    }
                });
                mVideoBgView.setBackground(new BitmapDrawable(getVideoBitmap(BASE_IMG_URL + path)));
                mVideoBgView.setVisibility(View.GONE);
//                MediaController mpc = new MediaController(activity, false);
                videoview.setVideoPath(BASE_IMG_URL + path);
                videoview.setZOrderOnTop(true);
                videoview.setZOrderMediaOverlay(true);
//                videoview.setMediaController(mpc);//暂时不使用控制器
                videoview.setOnPreparedListener(mp -> mMediaPlayer = mp);
                videoview.setOnCompletionListener(mp -> mOnCompletion.onMyCompletion(mp));
                mVideoViewMap.put(position, videoview);
                mVideoBgViewMap.put(position, mVideoBgView);
                container.addView(videoview_layout);
                return videoview_layout;
            } else {
                ScaleAttrsImageView imageView = new ScaleAttrsImageView(activity,
                        BASE_IMG_URL + path,
                        viewPager.getMeasuredWidth(), viewPager.getMeasuredHeight(), mDouble);
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = imageView.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(imageView);
                }
                ImageLoader.getInstance().displayImage(activity, BASE_IMG_URL + path, imageView);
                container.addView(imageView);
                //add listeners here if necessary
                return imageView;
            }
        }

        Bitmap getVideoBitmap(String mVideoUrl) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mVideoUrl, new HashMap<>());
            Bitmap bitmap = retriever.getFrameAtTime(1000000, OPTION_CLOSEST_SYNC);
            retriever.release();
            return bitmap;
        }

    }

    @Override
    public void onSuccess(BaseEntity entity) {
        super.onSuccess(entity);
    }

    //发送事件 304
    private void sendData304(float x, float y, int type) {
        myType = type;
        HashMap<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("language", language);
        map.put("type", type + "");// 1放大 2缩小  3标记 4图片调用开始 5图片调用结束
        map.put("time", recorderSecondsElapsed);
        map.put("imgid", imgId);
        map.put("timgid", type == 3 || type == 4 || type == 5 ? getImageId() : "");
        map.put("x", x);
        map.put("y", y);
        presenter.getDataAll("304", map);
    }

    @Override
    protected void onPause() {
        resolvePause();
        super.onPause();
    }

    private int sendNum = 1;//发送次数
    private boolean isSolo = false;//是否是原音

    @Override
    public void onFailNew(String url, String msg) {

        switch (url) {
            case "sendData":
                if (sendNum == 1) {//失败的情况重新上传一次
                    sendNum++;
                    upAudio();//
                } else {
                    sendNum = 1;//重置状态
                    ToastUtils.showShort("上传失败，请重试");
                    progressPopupWindow.dismiss();
                }
                return;
            default:
                break;
        }
        super.onFailNew(url, msg);
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "304"://事件
                switch (myType) {
                    case 4:
                        ToastUtils.showShort("调用开始！");
                        break;
                    case 5:
                        ToastUtils.showShort("调用结束！");
                        break;
                    default:
                        break;
                }
                break;
            case "315"://重录
                mMyCustomView.clearData();
//                mRlFinish.setVisibility(View.GONE);
                setFinishLayout(false, false, false);
                mLlAddMark.setVisibility(View.GONE);
                mLlDubbingAll.setVisibility(View.VISIBLE);
                mCbMark.setChecked(false);
                mCbMark.setVisibility(View.VISIBLE);
                mTvMark.setVisibility(View.VISIBLE);
                canMark = false;
                mMyCustomView.setCanMark(false);
                break;
            case "317"://论文图片信息
                DubbingBean bean = (DubbingBean) baseBean;
//                for (int i = 0; i < bean.getList().size(); i++) {
//                    DubbingVO vo = bean.getList().get(i);
//                    if (i == 0)
//                        vo.setUrl("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
//                    ClickEntity clickEntity = new ClickEntity();
//                    clickEntity.setDubbingVO(vo);
//                    dataList.add(clickEntity);
//                }
                for (DubbingVO vo : bean.getList()) {
                    ClickEntity clickEntity = new ClickEntity();
                    clickEntity.setDubbingVO(vo);
                    dataList.add(clickEntity);
                }
                int position = 0;
                //确定第一个未录音的图片位置，如果全都录过了就是从修改进来的，默认也是从第0个开始
                for (int i = 0; i < bean.getList().size(); i++) {
                    if (ObjectUtils.isEmpty(bean.getList().get(i).getRid())) {
                        position = i;
                        break;
                    }
                }
                dubbingPosition = position;
//                List<DubbingVO> list = new ArrayList<>();
//                list.add(bean.getList().get(0));
                mImageAdapter = new ImageAdapter(this, bean.getList(), this, this);
                viewPager.setAdapter(mImageAdapter);
                viewPager.addOnPageChangeListener(this);
//                viewPager.setCurrentItem(position);
                itemClickAdapte = new ItemClickAdapter(this, R.layout.item_dubbing_pic, dataList, "paper_pic");
                itemClickAdapte.setOnItemClickListener(this);
//                itemClickAdapte.setSelectedPosition(position);
//                itemClickAdapte.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(itemClickAdapte);
                recyclerView.scrollToPosition(position);
                setImageStatus(position);
                break;
            case "sendData":
                sendNum = 1;//重置上传状态
                //重置标记状态
                mCbMark.setChecked(false);
                canMark = false;
                mMyCustomView.clearData();
                mMyCustomView.setCanMark(false);
                dataList.get(dubbingPosition).getDubbingVO().setRid(baseBean.getRid());
                dataList.get(dubbingPosition).getDubbingVO().setRurl(baseBean.getRurl());
                Utils.showToast(R.string.uploaded_successfully);
                tvHint.setText(R.string.click_start_voice);
                recorderSecondsElapsed = 0;
                type = 1;
                progressPopupWindow.dismiss();
                tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
//                mRlFinish.setVisibility(View.GONE);
                setFinishLayout(false, false, false);
                if (typeActivity != null)
                    switch (typeActivity) {
                        case "wait_dubbing":
                            if (dubbingPosition < dataList.size() - 1) {
                                dubbingPosition++;
                                setImageStatus(dubbingPosition);
                            } else {
                                dubbingPosition = 0;
                                setImageStatus(0);
                            }
                            boolean isFinish = true;
                            for (ClickEntity entity : dataList) {
                                if (ObjectUtils.isEmpty(entity.getDubbingVO().getRid())) {
                                    isFinish = false;
                                    break;
                                }
                            }
                            // 2018/11/22 这里需要加上自动录音 = 下一张是图片 + 该图片未录音
                            if (!isFinish && ObjectUtils.isEmpty(dataList.get(dubbingPosition).getDubbingVO().getRid())
                                    && !isVideo(dubbingPosition)) {
                                // 2018/11/22 自动录音
                                mBtnSolo.setVisibility(View.INVISIBLE);
                                isDubbing = true;
                                imgId = getImageId();
                                toggleRecording(null);
                            } else {
                                isDubbing = false;
                            }
                            viewPager.setDubbing(isDubbing);

                            break;
                        case "edit_dubbing":

                            break;
                    }
                break;
            case "343":
                MarkBean bean1 = (MarkBean) baseBean;
                for (MarkVO vo : bean1.getList()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setMarkVO(vo);
                    markList.add(entity);
                }
                mMarkAdapter.notifyDataSetChanged();
                break;
            case "344":
                MarkBean bean2 = (MarkBean) baseBean;
                imgMarkList = new ArrayList<>();
                for (MarkVO vo : bean2.getList()) {
                    imgMarkList.add(vo.getId());
                }
                //已经标记过了
                if (!markFinishList.contains(getImageId()) && imgMarkList.size() != 0) {
                    markFinishList.add(getImageId());
                }

                mMarkAdapter.setMarks(imgMarkList);
                mMarkAdapter.notifyDataSetChanged();
                break;
            case "345"://标签保存完成
                ToastUtils.showShort("标签保存成功！");
                if (!markFinishList.contains(getImageId())) {
                    markFinishList.add(getImageId());
                }
                if (markFinishList.size() == dataList.size()) {//全都标记完成后关闭当前页
                    finish();
                    break;
                }
                if (viewPager.getCurrentItem() < viewPager.getChildCount() - 1) {
                    setImageStatus(viewPager.getCurrentItem() + 1);
                } else {
                    setImageStatus(0);
                }
                break;
            default:
                break;
        }
    }

    private String getImageId() {
        return dataList.get(viewPager.getCurrentItem()).getDubbingVO().getPcid();
    }

    private boolean isVideo(int position) {
        if (dataList != null && dataList.size() > position)
            return dataList.get(position).getDubbingVO().getUrl().contains("mp4");
        else
            return false;
    }

    private int getVideoDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path, new HashMap<>());
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        mmr.release();
        return (Integer.parseInt(duration)) / 1000;
    }

    private void setFinishLayout(boolean listen, boolean again, boolean finish) {
        tvDubbingAudition.setVisibility(listen ? View.VISIBLE : View.INVISIBLE);
        tvDubbingReRecord.setVisibility(again ? View.VISIBLE : View.INVISIBLE);
        tvFinish.setVisibility(finish ? View.VISIBLE : View.INVISIBLE);
        playUrl = false;//重置播放录音的方式，默认为本地录音
    }
}
