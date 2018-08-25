package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.utils.DensityUtils;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.widget.recording.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 语音回答页面
 * Created by Thinkpad on 2017/5/24.
 */
public class AnswerVoiceMP3Activity extends DubbingPermissionActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_start_hint)
    TextView tvStartHint;


    @BindView(R.id.tv_record)
    ImageView tvRecord;

    private Dialog dialog;
    private String questionid;
    private String pid;
    private String imgid;
    private String answer;
    private String type;

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_qarecording;
    }

    public static void actionActivity(Activity context, String type, String answer,
                                      String pid, String questionid, String imgid) {
        Intent sIntent = new Intent(context, AnswerVoiceMP3Activity.class);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        sIntent.putExtra("Answer", answer);
        sIntent.putExtra("type", type);
        sIntent.putExtra("pid", pid);
        sIntent.putExtra("imgid", imgid);
        sIntent.putExtra("questionid", questionid);
        context.startActivityForResult(sIntent, 2000);
    }

    @Override
    public void initView() {
        super.initView();
        initDialog();
        answer = getIntent().getStringExtra("Answer");
        pid = getIntent().getStringExtra("pid");
        imgid = getIntent().getStringExtra("imgid");
        questionid = getIntent().getStringExtra("questionid");
        type = getIntent().getStringExtra("type");
        //        setupRecorder();
        tvAnswer.setText(answer);
        tvAnswer.setMovementMethod(new ScrollingMovementMethod());
        initpaly();

    }

    public File getFilePath() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Giiisp", System.currentTimeMillis() + "ecorded_audio.wav");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file;

    }

    @Override
    public void toggleRecording(View v) {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (mIsRecord) {
                    resolveStopRecord();
                    showDialog();
                    //                    pauseRecording();
                } else {
                    tvStartHint.setText(R.string.dubbing_consistent_with_text);
                    resolveRecord();
                    //                    DubbingPermissionActivityPermissionsDispatcher.resolveRecordWithCheck(AnswerVoiceMP3Activity.this);
                }
            }
        });
    }

    @Override
    protected void resolveStopRecord() {
        super.resolveStopRecord();
        tvRecord.setImageResource(R.mipmap.recording_icon);
    }

    @Override
    public void togglePlaying(View v) {
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (mIsPlay) {
                    resolvePausePlayRecord();
                } else {
                    resolvePlayRecord();
                }
            }
        });
    }

    public void restartRecording(View v) {
        resolveResetPlay();
    }

    public void showDialog() {
        dialog.show();
    }

    @Override
    protected void startTimer() {
        tvRecord.setImageResource(R.mipmap.answer_dubbing);
        super.startTimer();
    }

    @OnClick({R.id.tv_back, R.id.tv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tv_record:
                toggleRecording(view);
                break;
            case R.id.tv_audition:
                break;
            case R.id.tv_reset:
                dialog.dismiss();
                recorderSecondsElapsed = 0;
                break;
            case R.id.tv_publish:
                finish();
                break;
        }
    }


    private void initDialog() {
        View view = getLayoutInflater().inflate(R.layout.answer_recorder_dialog, null);
        TextView backEdit = (TextView) view.findViewById(R.id.tv_back_edit);
        tvTime = (TextView) view.findViewById(R.id.tv_recording);
        TextView audition = (TextView) view.findViewById(R.id.tv_audition);
        TextView reset = (TextView) view.findViewById(R.id.tv_reset);
        TextView publish = (TextView) view.findViewById(R.id.tv_publish);
        backEdit.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        audition.setOnClickListener(this);
        reset.setOnClickListener(this);
        publish.setOnClickListener(this);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        if (window != null) {
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = DensityUtils.dp2px(this, 214f);
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // 设置显示位置
            dialog.onWindowAttributesChanged(wl);
        }
        dialog.setOnKeyListener((dialog, keyCode, event) -> true);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back_edit:

                finish();
                break;
            case R.id.tv_recording:
                togglePlaying(v);
                break;
            case R.id.tv_audition:
                break;
            case R.id.tv_reset://重录
                restartRecording(v);
                isPause = false;
                dialog.dismiss();
                break;
            case R.id.tv_publish://发表
//                postDubbing();

                ArrayMap<String, Object> map = new ArrayMap<>();
                map.put("uid", getUserID());
                map.put("content", answer);
                map.put("qid", questionid);
//                map.put("record", UrlConstants.RequestUrl.MP3_URL + key);
                File file = new File(filePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("record", file.getName(), requestBody);
                map.put("timing", recorderSecondsElapsed);
                switch (type) {
                    case "answer":
                        presenter.getSaveAnswerData(map);
                        break;
                    case "answer_again":
                        presenter.getSaveAnswerData(map);
                        break;
                    case "Problem"://首问
                        map.put("pid",pid );
                        map.put("picid", imgid);
                        map.put("firstquiz", 1);
                        map.put("pqid", questionid);
                        presenter.getSaveQuizData(map,part);
                        break;
                    case "examineMinutely"://追问
                        map.put("pid", pid);
                        map.put("picid", imgid);
                        map.put("firstquiz", 2);
                        map.put("pqid", questionid);
                        presenter.getSaveQuizData(map,part);
                        break;
                }


                break;

        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        super.onSuccess(entity);
        if (!TextUtils.isEmpty(entity.getUid())) {
//            QnToken = entity.getToken();
//            Log.i("--->>", "onSuccess: " + QnToken);
        } else {
            Utils.showToast(entity.getInfo());
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void keyCompete(String key) {
        super.keyCompete(key);
        ArrayMap<String, Object> map = new ArrayMap<>();
        map.put("uid", getUserID());
        map.put("content", answer);
        map.put("qid", questionid);
        map.put("record", UrlConstants.RequestUrl.MP3_URL + key);
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("record", file.getName(), requestBody);
        map.put("timing", recorderSecondsElapsed);
        switch (type) {
            case "answer":
                presenter.getSaveAnswerData(map);
                break;
            case "answer_again":
                presenter.getSaveAnswerData(map);
                break;
            case "Problem"://首问
                map.put("pid",pid );
                map.put("picid", imgid);
                map.put("firstquiz", 1);
                map.put("pqid", questionid);
                presenter.getSaveQuizData(map,part);
                break;
            case "examineMinutely"://追问
                map.put("pid", pid);
                map.put("picid", imgid);
                map.put("firstquiz", 2);
                map.put("pqid", questionid);
                presenter.getSaveQuizData(map,part);
                break;
        }

    }

    @Override
    protected void postDubbing() {
        super.postDubbing();
    }
}
