package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.QuizHintEntity;
import com.giiisp.giiisp.model.GlideApp;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.KeyBoardUtils;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;

/**
 * 回答 提问 的 页面
 * Created by Thinkpad on 2017/5/9.
 */

public class ProblemActivity extends BaseMvpActivity<BaseImpl, WholePresenter> implements BaseImpl {


    @BindView(R.id.tv_put_question)
    TextView tvPutQuestion;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_examine_minutely)
    TextView tvExamineMinutely;
    @BindView(R.id.editText_answer)
    AutoCompleteTextView editTextAnswer;
    @BindView(R.id.tv_text_number)
    TextView tvTextNumber;
    @BindView(R.id.iv_answer_at)
    ImageView ivAnswerAt;
    @BindView(R.id.iv_answer_link)
    ImageView ivAnswerLink;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.fl_edit)
    FrameLayout flEdit;
    @BindView(R.id.img_photo_big)
    ImageView mImgBig;
    @BindView(R.id.rl_img_big)
    RelativeLayout mRlImgBIg;
    private String type;
    private String imgId;
    private String uid;
    private String pid;
    private String questionid;
    private String imgUrl;

    public static void actionActivity(Activity context, String type, String imgId, String uid) {
        Intent sIntent = new Intent(context, ProblemActivity.class);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        sIntent.putExtra("type", type);
        sIntent.putExtra("imgId", imgId);
        sIntent.putExtra("uid", uid);
        context.startActivityForResult(sIntent, 1000);
    }

    public static void actionActivity(Activity context,
                                      String type,
                                      String imgId,
                                      String pid,
                                      String questionid,
                                      String imgUrl) {
        Intent sIntent = new Intent(context, ProblemActivity.class);
//        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        sIntent.putExtra("type", type);
        sIntent.putExtra("imgId", imgId);
        sIntent.putExtra("pid", pid);
        sIntent.putExtra("questionid", questionid);
        sIntent.putExtra("imgUrl", imgUrl);
        context.startActivityForResult(sIntent, 1000);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_problem;
    }

    @Override
    public void initView() {
        addStatusBarView();
        type = getIntent().getStringExtra("type");
        imgId = getIntent().getStringExtra("imgId");
        pid = getIntent().getStringExtra("pid");
        questionid = getIntent().getStringExtra("questionid");
        imgUrl = getIntent().getStringExtra("imgUrl");


        InputFilter[] filters = {new InputFilter.LengthFilter(100)};

        switch (type) {
            case "answer":
                tvPutQuestion.setText(R.string.answer);
                editTextAnswer.setHint(R.string.please_input_answered);
                tvTextNumber.setVisibility(View.GONE);
                break;
            case "answer_again":
                tvPutQuestion.setText(R.string.answer);
                editTextAnswer.setHint(R.string.please_input_answered);
                tvTextNumber.setVisibility(View.GONE);
                break;
            case "Problem":
                tvPutQuestion.setText(R.string.put_question);
                editTextAnswer.setMaxLines(100);
                editTextAnswer.setFilters(filters);
                ivAnswerLink.setVisibility(View.GONE);
                ivAnswerAt.setVisibility(View.GONE);
                tvConfirm.setText(R.string.next);
                GlideApp.with(this).load(imgUrl.contains(BASE_IMG_URL) ? imgUrl : BASE_IMG_URL + imgUrl).into(mImgBig);
                break;
            case "examineMinutely":
                tvPutQuestion.setText(R.string.examine_minutely);
                editTextAnswer.setFilters(filters);
                tvExamineMinutely.setVisibility(View.VISIBLE);
                editTextAnswer.setMaxLines(100);
                editTextAnswer.setFilters(filters);
                ivAnswerLink.setVisibility(View.GONE);
                ivAnswerAt.setVisibility(View.GONE);
                tvConfirm.setText(R.string.next);
                break;
        }
        editTextAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (type) {
                    case "answer":
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvConfirm.setVisibility(View.VISIBLE);
                        break;
                    case "Problem":
                        if (s.length() >= 0) {
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvConfirm.setVisibility(View.VISIBLE);
                            tvTextNumber.setText(String.valueOf(s.length()));
                            if (s.length() >= 100) {
                                Utils.showToast(R.string.answer_concise);
                            }
                        }

                        if (s.length() == 0) {
                            tvEmpty.setVisibility(View.GONE);
                            tvConfirm.setVisibility(View.GONE);
                        }
                        if (s.length() >= 4) {
                            ArrayMap<String, Object> map = new ArrayMap<>();
                            map.put("content", editTextAnswer.getText().toString());
                            map.put("imgId", imgId);
                            presenter.getQuizHintListData(map);

                        }

                        break;
                    case "examineMinutely":
                        break;
                }
            }
        });

    }

    private void addStatusBarView() {
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this));
        ViewGroup decorView = (ViewGroup) findViewById(android.R.id.content);
        decorView.addView(view, params);
    }

    public int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


    @OnClick({R.id.im_close, R.id.tv_publish, R.id.iv_answer_at,
            R.id.fl_edit, R.id.iv_answer_link, R.id.tv_empty, R.id.tv_confirm,
            R.id.img_photo, R.id.tv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_close:
                setResult(RESULT_OK, getIntent());
                finish();
                break;
            case R.id.tv_publish:
//                ArrayMap<String, Object> map = new ArrayMap<>();
//                String content = editTextAnswer.getText().toString();
//                if (TextUtils.isEmpty(content)) {
//                    Utils.showToast(R.string.please_input_content);
//                    return;
//                }
//                switch (type) {
//                    case "answer":
//                        map.put("uid", getUserID());
//                        map.put("qid", questionid);
//                        map.put("content", content);
//                        map.put("record", "");
//                        map.put("timing", "");
//                        presenter.getSaveAnswerData(map);
//                        break;
//                    case "Problem"://首问
//                        map.put("uid", getUserID());
//                        map.put("pid", pid);
//                        map.put("picid", imgId);
//                        map.put("content", content);
//                        map.put("firstquiz", 1);
//                        map.put("timing", "");
//                        map.put("questionid", questionid);
//                        map.put("record", "");
//                        presenter.getSaveQuizData(map);
//                        break;
//                    case "examineMinutely"://追问
//                        map.put("uid", getUserID());
//                        map.put("pid", pid);
//                        map.put("picid", imgId);
//                        map.put("content", content);
//                        map.put("firstquiz", 2);
//                        map.put("timing", "");
//                        map.put("questionid", questionid);
//                        map.put("record", "");
//                        presenter.getSaveQuizData(map);
//                        break;
//                }

                KeyBoardUtils.closeKeybord(editTextAnswer, this);

                break;
            case R.id.fl_edit:
                KeyBoardUtils.openKeybord(editTextAnswer, this);
                break;
            case R.id.iv_answer_at:
                FragmentActivity.actionActivity(this, "at");
                break;
            case R.id.iv_answer_link:
                FragmentActivity.actionActivity(this, "link");
                break;
            case R.id.tv_empty:
                editTextAnswer.setText("");
                break;
            case R.id.tv_confirm:
                KeyBoardUtils.closeKeybord(editTextAnswer, this);
                AnswerVoiceMP3Activity.actionActivity(this,
                        type,
                        editTextAnswer.getText().toString(),
                        pid,
                        questionid,
                        imgId);

//                switch (type) {
//                    case "answer":
//                        AnswerVoiceMP3Activity.actionActivity(this, editTextAnswer.getText().toString(), imgId, uid);
//                        break;
//                    case "Problem":
//
//                        break;
//                    case "examineMinutely":
//
//                        break;
//                }

                break;
            case R.id.img_photo:
                mRlImgBIg.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_close:
                mRlImgBIg.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onSuccess(BaseEntity entity) {
        if (entity instanceof QuizHintEntity) {
            List<QuizHintEntity.QuizHintListBean> quizHintList = ((QuizHintEntity) entity).getQuizHintList();
            ArrayList<String> suggest = new ArrayList<>();
            for (QuizHintEntity.QuizHintListBean quizHintListBean : quizHintList) {
                suggest.add(quizHintListBean.getContent());
            }
            final ArrayAdapter<String> sugAdapter = new ArrayAdapter<>(ProblemActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
            editTextAnswer.setAdapter(sugAdapter);
            sugAdapter.notifyDataSetChanged();

            editTextAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            editTextAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Utils.showToast(R.string.problem_exists);
                }
            });
        } else {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onFailure(String msg, Exception ex) {

    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }
}
