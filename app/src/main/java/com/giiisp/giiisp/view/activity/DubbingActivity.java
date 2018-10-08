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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.DubbingBean;
import com.giiisp.giiisp.dto.DubbingListVO;
import com.giiisp.giiisp.dto.DubbingVO;
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
import com.giiisp.giiisp.widget.WrapVideoView;
import com.giiisp.giiisp.widget.recording.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;

/**
 * 配音的页面
 * Created by Thinkpad on 2017/6/5.
 */
public class DubbingActivity extends DubbingPermissionActivity implements
        BaseQuickAdapter.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        MyCustomView.DrawListen,
        MyOnCompletion {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_dubbing_audition)
    TextView tvDubbingDudition;
    @BindView(R.id.tv_dubbing_determine)
    TextView tvFinish;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.linear_layout)
    RelativeLayout linearLayout;
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


    String typeActivity;
    //    private ItemClickAdapter itemClickAdapter;
    private PlayEvent playEvent = new PlayEvent();
    private ItemClickAdapter itemClickAdapte;
    private ImageAdapter mImageAdapter;

    private int dubbingPosition = 0;
    private int abc = -1;
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
    private boolean isDubbing = false;
    private int videoAllTime = 0;
    private boolean isFinish = true;

    /*** 页面的视频控件集合,Integer所处位置 ***/
    static Map<Integer, WrapVideoView> mVideoViewMap = new HashMap<>();
    static Map<Integer, View> mVideoBgViewMap;

    /*** 记录每个page页面视频播放的进度 ***/
    static Map<Integer, Integer> mCurrentPositions;

    /*** 记录当前page页面是否为视频 ***/
    static Map<Integer, Boolean> mIsVideo;


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
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.initData();
        language = getIntent().getIntExtra("language", 0);
        typeActivity = getIntent().getStringExtra("type");
        pid = getIntent().getStringExtra("id");
//        recordRows = getIntent().getParcelableArrayListExtra("recordRows");
//        photoRows = getIntent().getParcelableArrayListExtra("photoRows");
        mMyCustomView.setDrawListen(this);
        getImageData();
    }

    @Override
    public void initView() {
        super.initView();
        tvTime = findViewById(R.id.tv_time);
//        List<String> list_url = new ArrayList<>();
//        List<ClickEntity> list = new ArrayList<>();
//        if (photoRows != null) {
//            for (SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX photoRow : photoRows) {
//                list_url.add(photoRow.getPath());
//                list.add(new ClickEntity(photoRow.getPath()));
//            }
//            if (recordRows != null && photoRows.size() > recordRows.size()) {
//                position = recordRows.size();
//            }
//        }

//        viewPager.setAdapter(new ImageAdapter(this, list_url));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        itemClickAdapte = new ItemClickAdapter(this, R.layout.item_paper_pic, list, "paper_pic");
//        recyclerView.setAdapter(itemClickAdapte);
//        itemClickAdapte.setOnItemClickListener(this);
//        viewPager.addOnPageChangeListener(this);
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
                            && !isFinish) {
                        ToastUtils.showShort("录音时长需大于或等于视频时长");
                        return;
                    }
                    resolvePause();
                } else {//开始
                    resolveRecord();
                    tvHint.setText(R.string.is_dubbing);
                    linearLayout.setVisibility(View.GONE);
                    if (isVideo(dubbingPosition)) {
                        mImageAdapter.setVolume0();
                        if (mVideoViewMap != null && mVideoViewMap.get(dubbingPosition) != null)
                            mVideoViewMap.get(dubbingPosition).start();
                        isFinish = false;
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
                    tvDubbingDudition.setSelected(false);
                } else {
                    resolvePlayRecord();
                    tvDubbingDudition.setSelected(true);
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
        ivBtn.setImageResource(R.mipmap.in_recording);
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
            linearLayout.setVisibility(View.GONE);
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
            ivBtn.setImageResource(R.mipmap.in_recording_spot);
            linearLayout.setVisibility(View.VISIBLE);
            tvFinish.setVisibility(View.VISIBLE);
            tvDubbingDudition.setVisibility(View.VISIBLE);
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
//                        mTvUse.setText(showDiaoYong ? "调用" : "返回");
                    } else if ("返回".equals(ToolString.getString(mTvUse))) {
                        sendData304(0, 0, 5);
                        // 这里需要跳到录音的图片页
                        setImageStatus(dubbingPosition);
//                        mTvUse.setText("调用");
//                        mRlBig.setVisibility(View.GONE);
//                        mBtnUse.setVisibility(View.GONE);
//                        mTvUse.setVisibility(View.GONE);
//                        mCbMark.setVisibility(View.VISIBLE);
//                        mTvMark.setVisibility(View.VISIBLE);
                    }
                } else {//正在显示调用弹窗，不做操作

                }
                break;
            case R.id.btn_yes://调用-确定
                sendData304(0, 0, 4);
                showDiaoYong = false;
                mRlBig.setVisibility(View.GONE);
                mTvUse.setText("返回");
                break;
            case R.id.btn_no://调用-取消
//                sendData304(0, 0, 5);
                showDiaoYong = false;
                mRlBig.setVisibility(View.GONE);
                mTvUse.setText("调用");
                break;
            case R.id.tv_back:
                back = true;
                finish();
                break;
            case R.id.tv_dubbing_determine://完成

                type = 0;
                back = false;
                resolveStopRecord();
                resolvePausePlayRecord();
                upAudio();//上传录音

                break;
            case R.id.tv_dubbing_re_record://重录
                dataList.get(viewPager.getCurrentItem()).getDubbingVO().setRid("");
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
                HashMap<String, Object> map = new HashMap<>();
                map.put("imgid", imgId);
                map.put("language", language);
                presenter.getDataAll("315", map);
                break;
            case R.id.tv_dubbing_audition://试听
                togglePlaying(view);
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
                if (viewPager.getCurrentItem() < viewPager.getChildCount()) {
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
                    mBtnSolo.setVisibility(View.INVISIBLE);
                    isDubbing = true;
                    imgId = getImageId();
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
                linearLayout.setVisibility(View.VISIBLE);
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
                filePath = "";
                upAudio();
//                if (viewPager.getCurrentItem() < viewPager.getChildCount()) {
//                    setImageStatus(viewPager.getCurrentItem() + 1);
//                } else {
//                    setImageStatus(0);
//                }
                break;
        }
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
//            map.put("uid", uid);
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

    /*
     * 传录音
     * */
    public void upAudio() {
        ArrayMap<String, Object> map = new ArrayMap<>();
//        double duration = 0;
        long fileSize = 0;
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
//      if (recordRows != null && recordRows.size() > position) {
//          String recordId = recordRows.get(position).getId();
//          map.put("id", recordId);
//      } else {
        map.put("id", dataList.get(dubbingPosition).getDubbingVO().getRid());//修改录音是使用，第一次录音 传 “”
//      }
//      map.put("token", token);
        map.put("uid", uid);
        map.put("pcid", dataList.get(dubbingPosition).getDubbingVO().getPcid());
        map.put("size", fileSize);
        map.put("useinit", ObjectUtils.isNotEmpty(filePath) ? "2" : "1");
        map.put("duration", (long) recorderSecondsElapsed);
        map.put("language", language); //application/x-www-form-urlencoded ,multipart/form-data
        map.put("resolution", ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight());//手机分辨率
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("recordFile", file.getName(), requestBody);
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
            if (mVideoViewMap.get(abc) != null)
                mVideoViewMap.get(abc).pause();
            setImageStatus(position);
            abc = position;
        }
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
        //        viewPager.setCurrentItem(position);
        //        itemClickAdapte.setSelectedPosition(position);
        //        itemClickAdapte.notifyDataSetChanged();
        //        ClickEntity item = (ClickEntity) adapter.getItem(position);
        //        Utils.showToast(item.getString());
        //        Log.i("--->>", "onItemClick: " + item.getString());
    }

    private void setImageStatus(int position) {
        if (dataList != null && isVideo(position)) {
            if (this.dubbingPosition == position) {
                videoAllTime = mImageAdapter.getVideoDuration(dubbingPosition);//这里记录视频的总时间
//                if (!isDubbing)
//                    mBtnSolo.setVisibility(View.VISIBLE);
//            } else {
//                mBtnSolo.setVisibility(View.INVISIBLE);
            }
            mBtnSolo.setVisibility(isDubbing ? View.INVISIBLE : View.VISIBLE);
        } else {
            mBtnSolo.setVisibility(View.INVISIBLE);
        }
        viewPager.setCurrentItem(position);
        itemClickAdapte.setSelectedPosition(position);
        itemClickAdapte.notifyDataSetChanged();
        canMark = false;
        mMyCustomView.setCanMark(false);
        mCbMark.setChecked(false);
        LogUtils.v("ok-dubb:" + dubbingPosition);
        LogUtils.v("ok-position:" + position);
        if (isDubbing) {//正在录音
            ivBtn.setVisibility(this.dubbingPosition != position ? View.GONE : View.VISIBLE);
            mCbMark.setVisibility(this.dubbingPosition != position ? View.GONE : View.VISIBLE);
            mTvMark.setVisibility(this.dubbingPosition != position ? View.GONE : View.VISIBLE);
            mBtnUse.setVisibility(this.dubbingPosition != position ? View.VISIBLE : View.GONE);
            mRlBig.setVisibility(this.dubbingPosition != position ? View.VISIBLE : View.GONE);
            mTvUse.setVisibility(this.dubbingPosition != position ? View.VISIBLE : View.GONE);
        } else {//未录音
            if (ObjectUtils.isNotEmpty(dataList.get(position).getDubbingVO().getRid())) {//已经录过音了
                mCbMark.setVisibility(View.GONE);
                mTvMark.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                tvDubbingDudition.setVisibility(View.INVISIBLE);
                tvFinish.setVisibility(View.INVISIBLE);
                mBtnSolo.setVisibility(View.INVISIBLE);
            } else {
                if (isVideo(position)) {//视频不可放大
                    mBtnFull.setVisibility(View.GONE);
                } else {//图片
                    mBtnFull.setVisibility(View.VISIBLE);
                }
                linearLayout.setVisibility(View.GONE);
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
    }

    @Override
    public void drawListen(float x, float y) {
        sendData304(x, y, 3);
    }

    @Override
    public void onCompletion(MediaPlayer videoView) {
        isFinish = true;
        if (isDubbing) {//如果还在录音的话重新播放视频
            videoView.start();
            videoView.setLooping(true);
        } else {
            //这里需要上传原音
            filePath = "";
            upAudio();
        }
    }

    private class ImageAdapter extends PagerAdapter {

        private List<DubbingVO> viewlist;
        private BaseActivity activity;
        private MediaPlayer mMediaPlayer;
        private int volume;
        private MyOnCompletion mOnCompletion;
//        private WrapVideoView videoview;

        ImageAdapter(BaseActivity activity, List<DubbingVO> viewlist, MyOnCompletion onCompletion) {
            this.viewlist = viewlist;
            this.activity = activity;
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
                mVideoBgView.setBackground(new BitmapDrawable(getVideoBitmap(path)));

                MediaController mpc = new MediaController(activity, false);
                videoview.setVideoPath(path);
                videoview.setZOrderOnTop(true);
                videoview.setMediaController(mpc);
                videoview.setOnPreparedListener(mp -> mMediaPlayer = mp);
                videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mOnCompletion.onCompletion(mp);
                    }
                });
                mVideoViewMap.put(position, videoview);
                container.addView(videoview_layout);
                return videoview_layout;
            } else {
                ImageView imageView = new ImageView(activity);
                //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = imageView.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(imageView);
                }
                ImageLoader.getInstance().displayImage(activity, BASE_IMG_URL + path, imageView);
                //            view.setImageURI(Uri.parse(path));
                container.addView(imageView);
                //add listeners here if necessary
                return imageView;
            }
        }

        public Bitmap getVideoBitmap(String mVideoUrl) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mVideoUrl, new HashMap<>());
            Bitmap bitmap = retriever.getFrameAtTime();
            retriever.release();
            return bitmap;
        }

        public int getVideoDuration(int position) {
            return mVideoViewMap.get(position) != null ? mVideoViewMap.get(position).getDuration() : 0;
        }
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        super.onSuccess(entity);
//        if (entity.getResult() != 1) {
//            Utils.showToast(entity.getInfo());
//        } else {
//            //重置标记状态
//            mCbMark.setChecked(false);
//            canMark = false;
//            mMyCustomView.clearData();
//            mMyCustomView.setCanMark(false);
//
//            Utils.showToast(R.string.uploaded_successfully);
//            tvHint.setText(R.string.click_start_voice);
//            recorderSecondsElapsed = 0;
//            type = 1;
//            progressPopupWindow.dismiss();
//            tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
//            linearLayout.setVisibility(View.GONE);
//            if (typeActivity != null)
//                switch (typeActivity) {
//                    case "wait_dubbing":
//                        position++;
////                        if (photoRows != null)
////                            if (position == photoRows.size()) {
////                                Utils.showToast(R.string.complete_dubbing);
////                                setResult(3000);
////                                finish();
////                            }
//                        if (position < itemClickAdapte.getItemCount()) {
//                            itemClickAdapte.setSelectedPosition(position);
//                            itemClickAdapte.notifyDataSetChanged();
//                            recyclerView.scrollToPosition(position);
//                            viewPager.setCurrentItem(position);
//                        }
//
//                        break;
//                    case "edit_dubbing":
//
//                        break;
//                }
//        }
    }

    private void sendData304(float x, float y, int type) {
        myType = type;
        HashMap<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("language", language);
        map.put("type", type + "");// 1放大 2缩小  3标记 4图片调用开始 5图片调用结束
        map.put("time", recorderSecondsElapsed);
        map.put("imgid", imgId);
        map.put("timgid", type == 4 || type == 5 ? getImageId() : "");
        map.put("x", x);
        map.put("y", y);
        presenter.getDataAll("304", map);
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "304":
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
                linearLayout.setVisibility(View.GONE);
                mCbMark.setChecked(false);
                mCbMark.setVisibility(View.VISIBLE);
                mTvMark.setVisibility(View.VISIBLE);
                canMark = false;
                mMyCustomView.setCanMark(false);
                break;
            case "317"://论文图片信息
                DubbingBean bean = (DubbingBean) baseBean;
                for (int i = 0; i < bean.getList().size(); i++) {
                    DubbingVO vo = bean.getList().get(i);
                    if (i == 0)
                        vo.setUrl("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
                    ClickEntity clickEntity = new ClickEntity();
                    clickEntity.setDubbingVO(vo);
                    dataList.add(clickEntity);
                }
//                for (DubbingVO vo : bean.getList()) {
//                    ClickEntity clickEntity = new ClickEntity();
//                    clickEntity.setDubbingVO(vo);
//                    dataList.add(clickEntity);
//                }
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
                mImageAdapter = new ImageAdapter(this, bean.getList(), this);
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
                //重置标记状态
                mCbMark.setChecked(false);
                canMark = false;
                mMyCustomView.clearData();
                mMyCustomView.setCanMark(false);
                dataList.get(dubbingPosition).getDubbingVO().setRid(baseBean.getRid());
                Utils.showToast(R.string.uploaded_successfully);
                tvHint.setText(R.string.click_start_voice);
                recorderSecondsElapsed = 0;
                type = 1;
                progressPopupWindow.dismiss();
                tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
                linearLayout.setVisibility(View.GONE);
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
//                            position++;
//                        if (photoRows != null)
//                            if (position == photoRows.size()) {
//                                Utils.showToast(R.string.complete_dubbing);
//                                setResult(3000);
//                                finish();
//                            }
//                            if (position < itemClickAdapte.getItemCount()) {
//                                itemClickAdapte.setSelectedPosition(position);
//                                itemClickAdapte.notifyDataSetChanged();
//                                recyclerView.scrollToPosition(position);
//                                viewPager.setCurrentItem(position);
//                            }


                            break;
                        case "edit_dubbing":

                            break;
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
        return dataList.get(position).getDubbingVO().getUrl().contains("mp4");
    }
}

interface MyOnCompletion {
    void onCompletion(MediaPlayer mediaPlayer);
}
