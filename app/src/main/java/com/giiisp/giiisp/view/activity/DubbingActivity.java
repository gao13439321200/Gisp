package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.DubbingListVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.PlayEvent;
import com.giiisp.giiisp.entity.SubscribeEntity;
import com.giiisp.giiisp.model.GlideApp;
import com.giiisp.giiisp.utils.ImageLoader;
import com.giiisp.giiisp.utils.SDFileHelper;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.widget.MyCustomView;
import com.giiisp.giiisp.widget.recording.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 配音的页面
 * Created by Thinkpad on 2017/6/5.
 */
public class DubbingActivity extends DubbingPermissionActivity implements BaseQuickAdapter.OnItemClickListener, ViewPager.OnPageChangeListener, MyCustomView.DrawListen {
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
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
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
    @BindView(R.id.img_full)
    ImageView mImgFull;
    @BindView(R.id.btn_small)
    Button mBtnSmall;


    String typeActivity;
    //    private ItemClickAdapter itemClickAdapter;
    private PlayEvent playEvent = new PlayEvent();
    private ItemClickAdapter itemClickAdapte;

    private int position = 0;
    private ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.RecordOneBean.RowsBeanXXX> recordRows;
    private ArrayList<SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX> photoRows;
    private DubbingListVO mVO;

    private int language = 0;
    private boolean canMark = false;
    private boolean showDiaoYong = false;
    private int myType = 0;

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

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_dubbing;
    }

    @Override
    public void initData() {
        super.initData();
        language = getIntent().getIntExtra("language", 0);
        typeActivity = getIntent().getStringExtra("type");
        recordRows = getIntent().getParcelableArrayListExtra("recordRows");
        photoRows = getIntent().getParcelableArrayListExtra("photoRows");
        mMyCustomView.setDrawListen(this);
    }

    @Override
    public void initView() {
        super.initView();
        ScreenUtils.setPortrait(this);
        tvTime = (TextView) findViewById(R.id.tv_time);
        List<String> list_url = new ArrayList<>();
        List<ClickEntity> list = new ArrayList<>();
        if (photoRows != null) {
            for (SubscribeEntity.PageInfoBean.RowsBeanXXXXX.PhotoOneBean.RowsBeanXXXX.PhotosBean.RowsBeanXX photoRow : photoRows) {
                list_url.add(photoRow.getPath());
                list.add(new ClickEntity(photoRow.getPath()));
            }
            if (recordRows != null && photoRows.size() > recordRows.size()) {
                position = recordRows.size();
            }
        }

        viewPager.setAdapter(new ImageAdapter(this, list_url));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        itemClickAdapte = new ItemClickAdapter(this, R.layout.item_paper_pic, list, "paper_pic");
        recyclerView.setAdapter(itemClickAdapte);
        itemClickAdapte.setOnItemClickListener(this);
        viewPager.addOnPageChangeListener(this);
        tvHint.setText(R.string.click_start_recording);
        //        tvRight.setText("保存");
        tvTitle.setText(R.string.dubbing);
        itemClickAdapte.setSelectedPosition(position);
        itemClickAdapte.notifyDataSetChanged();
        recyclerView.scrollToPosition(position);
        viewPager.setCurrentItem(position);
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


    public void toggleRecording(View v) {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (mIsRecord) {
                    resolvePause();
                } else {
                    resolveRecord();
                    tvHint.setText(R.string.is_dubbing);
                    linearLayout.setVisibility(View.GONE);
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
        if (mRecorder.isPause()) {
            tvHint.setText(R.string.is_dubbing);
            ivBtn.setImageResource(R.mipmap.in_recording);
            mRecorder.setPause(false);
            linearLayout.setVisibility(View.GONE);
            resolvePausePlayRecord();
            startTimer();
        } else {
            stopTimer();
            mRecorder.setPause(true);
            tvHint.setText(R.string.dubbing_pause);
            isPause = false;
            ivBtn.setImageResource(R.mipmap.in_recording_spot);
            linearLayout.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.tv_back, R.id.iv_btn, R.id.tv_right, R.id.iv_dubbing,
            R.id.tv_dubbing_audition, R.id.tv_dubbing_determine,
            R.id.tv_dubbing_re_record, R.id.iv_left_slip, R.id.iv_right_slide,
            R.id.tv_mark, R.id.img_mark, R.id.btn_use, R.id.tv_use,
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
                showDiaoYong = !showDiaoYong;
                mRlBig.setVisibility(showDiaoYong ? View.VISIBLE : View.GONE);
                mTvUse.setText(showDiaoYong ? "调用" : "返回");
                break;
            case R.id.btn_yes://调用-确定
                sendData304(0, 0, 4);
                showDiaoYong = !showDiaoYong;
                mRlBig.setVisibility(showDiaoYong ? View.VISIBLE : View.GONE);
                mTvUse.setText(showDiaoYong ? "调用" : "返回");
                break;
            case R.id.btn_no://调用-取消
                sendData304(0, 0, 5);
                showDiaoYong = !showDiaoYong;
                mRlBig.setVisibility(showDiaoYong ? View.VISIBLE : View.GONE);
                mTvUse.setText(showDiaoYong ? "调用" : "返回");
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
                resolveStopRecord();
                mMyCustomView.clearData();
                tvHint.setText(R.string.click_start_voice);
                recorderSecondsElapsed = 0;
                tvTime.setText(Util.formatSeconds(recorderSecondsElapsed));
                resolvePausePlayRecord();
                //清空当前图片事件
                HashMap<String, Object> map = new HashMap<>();
                map.put("imgid", "");
                map.put("language", language);
                presenter.getDataAll("135", map);
                break;
            case R.id.tv_dubbing_audition://试听
                togglePlaying(view);
                break;
            case R.id.iv_left_slip://上一张图片
                viewPager.setCurrentItem(viewPager.getCurrentItem() > 1 ? viewPager.getCurrentItem() - 1 : 0);
                break;
            case R.id.iv_right_slide://下一张图片
                viewPager.setCurrentItem(viewPager.getCurrentItem() < viewPager.getChildCount() ? viewPager.getCurrentItem() + 1 : 0);
                break;
            case R.id.iv_btn://录音
                toggleRecording(view);
                break;
            case R.id.btn_full://全屏
                // TODO: 2018/8/7 高鹏 这里需要设置图片的地址
                GlideApp.with(this).load("图片地址").into(mImgFull);
                ScreenUtils.setLandscape(this);
                mImgFull.setVisibility(View.VISIBLE);
                mBtnSmall.setVisibility(View.VISIBLE);
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
                ScreenUtils.setPortrait(this);
                mImgFull.setVisibility(View.GONE);
                mBtnSmall.setVisibility(View.GONE);
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
        if (photoRows != null && photoRows.size() > position) {
            String id = photoRows.get(position).getId();

            double duration = 0;
            try {
                file = new File(filePath);
                long fileSize = SDFileHelper.getFileSize(new File(filePath));
                double rint = SDFileHelper.FormetFileSize(fileSize);
                duration = Math.rint(rint * 100) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (recordRows != null && recordRows.size() > position) {
                String recordId = recordRows.get(position).getId();
                map.put("id", recordId);
            }
//            map.put("token", token);
            map.put("uid", uid);
            map.put("pcid", id);
            map.put("size", duration);
            map.put("duration", recorderSecondsElapsed);
            map.put("language", language);
            RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("recordFile", file.getName(), requestBody);
//            map.put("path", UrlConstants.RequestUrl.MP3_URL + key);
            presenter.getSaveRecordData(map, part);
        }

    }

    /*
     * 传录音
     * */
    public void upAudio() {
        ArrayMap<String, Object> map = new ArrayMap<>();
        if (photoRows != null && photoRows.size() > position) {
            String id = photoRows.get(position).getId();

            double duration = 0;
            long fileSize = 0;
            try {
                file = new File(filePath);
                fileSize = SDFileHelper.getFileSize(new File(filePath));
                double rint = SDFileHelper.FormetFileSize(fileSize);
                duration = Math.rint(rint * 100) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (recordRows != null && recordRows.size() > position) {
                String recordId = recordRows.get(position).getId();
                map.put("id", recordId);
            } else {
                map.put("id", "0");
            }
//            map.put("token", token);
            map.put("uid", uid);
            map.put("size", fileSize);
            map.put("duration", (long) recorderSecondsElapsed);
            map.put("language", language); //application/x-www-form-urlencoded ,multipart/form-data
            map.put("resolution", ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight());//手机分辨率
            RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("recordFile", file.getName(), requestBody);
            presenter.getSaveRecordData(map, part);
        }


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
        Log.i("--->>", "onPageScrolled: ");
    }

    @Override
    public void onPageSelected(int position) {
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

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (typeActivity != null)
            switch (typeActivity) {
                case "wait_dubbing":
                    break;
                case "edit_dubbing":
                    this.position = position;

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

    @Override
    public void drawListen(float x, float y) {
        sendData304(x, y, 3);
    }

    private static class ImageAdapter extends PagerAdapter {

        private List<String> viewlist;
        private BaseActivity activity;


        public ImageAdapter(BaseActivity activity, List<String> viewlist) {
            this.viewlist = viewlist;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return viewlist.size();
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项
            ImageView imageView = new ImageView(activity);
            position %= viewlist.size();
            if (position < 0) {
                position = viewlist.size() + position;
            }
            String path = viewlist.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = imageView.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(imageView);
            }
            ImageLoader.getInstance().displayImage(activity, path, imageView);
            //            view.setImageURI(Uri.parse(path));
            container.addView(imageView);
            //add listeners here if necessary
            return imageView;
        }
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        super.onSuccess(entity);
        if (entity.getResult() != 1) {
            Utils.showToast(entity.getInfo());
        } else {
            //重置标记状态
            mCbMark.setChecked(false);
            canMark = false;
            mMyCustomView.clearData();
            mMyCustomView.setCanMark(false);

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
                        position++;
                        if (photoRows != null)
                            if (position == photoRows.size()) {
                                Utils.showToast(R.string.complete_dubbing);
                                setResult(3000);
                                finish();
                            }
                        if (position < itemClickAdapte.getItemCount()) {
                            itemClickAdapte.setSelectedPosition(position);
                            itemClickAdapte.notifyDataSetChanged();
                            recyclerView.scrollToPosition(position);
                            viewPager.setCurrentItem(position);
                        }

                        break;
                    case "edit_dubbing":

                        break;
                }
        }
    }

    private void sendData304(float x, float y, int type) {
        myType = type;
        HashMap<String, Object> map = new HashMap<>();
        map.put("pid", "");
        map.put("imgid", "");
        map.put("language", "中英文？");
        map.put("type", type + "");// 1放大 2缩小  3标记 4图片调用开始 5图片调用结束
        map.put("time", recorderSecondsElapsed + "");
        map.put("x", x);
        map.put("y", y);
        presenter.getDataAll("304", map);
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseEntity) {
        super.onSuccessNew(url, baseEntity);
        switch (url) {
            case "304":
                switch (type) {
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
            default:
                break;
        }
    }
}
