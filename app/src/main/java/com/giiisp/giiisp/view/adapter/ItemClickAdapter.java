package com.giiisp.giiisp.view.adapter;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.common.MyDialog;
import com.giiisp.giiisp.dto.DubbingVO;
import com.giiisp.giiisp.dto.FansVO;
import com.giiisp.giiisp.dto.FollowVO;
import com.giiisp.giiisp.dto.MyAnswerVO;
import com.giiisp.giiisp.dto.PaperInfoVO;
import com.giiisp.giiisp.dto.PaperMainVO;
import com.giiisp.giiisp.dto.PaperQaVO;
import com.giiisp.giiisp.entity.CollectionEntity;
import com.giiisp.giiisp.entity.DownloadController;
import com.giiisp.giiisp.entity.HomeSearchEntity;
import com.giiisp.giiisp.entity.MsgEntity;
import com.giiisp.giiisp.entity.MyScholarBean;
import com.giiisp.giiisp.entity.QAEntity;
import com.giiisp.giiisp.entity.UserInfoEntity;
import com.giiisp.giiisp.model.GlideApp;
import com.giiisp.giiisp.utils.FileUtils;
import com.giiisp.giiisp.utils.ImageLoader;
import com.giiisp.giiisp.utils.MyRecyclerView;
import com.giiisp.giiisp.utils.PlayerUtil;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.ExperienceActivity;
import com.giiisp.giiisp.view.activity.FragmentActivity;
import com.giiisp.giiisp.view.activity.PaperDetailsActivity;
import com.giiisp.giiisp.view.activity.ProblemActivity;
import com.giiisp.giiisp.view.activity.SearchActivity;
import com.giiisp.giiisp.view.fragment.ListItemClick;
import com.giiisp.giiisp.widget.recording.Util;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadStatus;

import static com.giiisp.giiisp.R.id.linear_layout_one;
import static com.giiisp.giiisp.R.id.linear_layout_two;
import static com.giiisp.giiisp.R.id.tv_answer_name;
import static com.giiisp.giiisp.R.id.tv_answer_reply;
import static com.giiisp.giiisp.R.id.tv_problem;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;
import static com.giiisp.giiisp.base.BaseActivity.uid;
import static com.giiisp.giiisp.view.activity.PaperDetailsActivity.CN;

/**
 * 重用的适配器
 */
public class ItemClickAdapter extends BaseQuickAdapter<ClickEntity, BaseViewHolder> {
    private int layoutResId;
    private BaseActivity activity;
    private RxDownload mRxDownload;
    private String type = "";
    private int selectedPosition = 0;
    private ListItemClick mListItemClick;
    private String pid, imgId;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemClickAdapter(BaseActivity activity, int layoutResId, List<ClickEntity> data) {
        super(layoutResId, data);
        this.layoutResId = layoutResId;
        this.activity = activity;
        mRxDownload = RxDownload.getInstance(activity);
    }

    public ItemClickAdapter(BaseActivity activity, int layoutResId, List<ClickEntity> data, ListItemClick listItemClick) {
        super(layoutResId, data);
        this.layoutResId = layoutResId;
        this.activity = activity;
        this.mListItemClick = listItemClick;
        mRxDownload = RxDownload.getInstance(activity);
    }

    public ItemClickAdapter(BaseActivity activity, int layoutResId, List<ClickEntity> data, String type) {
        super(layoutResId, data);
        this.layoutResId = layoutResId;
        this.activity = activity;
        this.type = type;
        mRxDownload = RxDownload.getInstance(activity);
    }

    public ItemClickAdapter(BaseActivity activity, int layoutResId, List<ClickEntity> data, String type, ListItemClick listItemClick) {
        super(layoutResId, data);
        this.layoutResId = layoutResId;
        this.activity = activity;
        this.type = type;
        this.mListItemClick = listItemClick;
        mRxDownload = RxDownload.getInstance(activity);
    }

    public ItemClickAdapter(BaseActivity activity, List<ClickEntity> data) {
        super(data);
        this.activity = activity;
        mRxDownload = RxDownload.getInstance(activity);
    }

    private void updateProgressStatus(ProgressBar mProgress, DownloadStatus status, TextView size) {
        mProgress.setIndeterminate(status.isChunked);
        mProgress.setMax((int) status.getTotalSize());
        mProgress.setProgress((int) status.getDownloadSize());
        //mPercent.setText(status.getPercent());
        size.setText(status.getFormatStatusString());
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ClickEntity item) {
        if (item != null) {
            switch (layoutResId) {
                case R.layout.item_witness_people:
                    if (item.getUserInfo() != null) {
                        helper.setText(R.id.tv_user_name, item.getUserInfo().getRealName());
                        helper.setText(R.id.tv_user_position, item.getUserInfo().getPosition());
                        if (item.getUserInfo().getId().equals("admin")) { //   admin
                            helper.setImageResource(R.id.iv_user_icon, R.mipmap.about_logo);
//                          ((ImageView) helper.getView(R.id.iv_user_icon)).setImageResource(R.mipmap.about_logo);
                            //setBackgroundResource(R.mipmap.about_logo);
                        } else {
                            ImageLoader.getInstance().displayCricleImage(activity, item.getUserInfo().getAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));
                        }

                        CheckBox checkBox = helper.getView(R.id.checkbox);
                        checkBox.setChecked(false);
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                item.getUserInfo().setCheck(b);
                            }
                        });
                    }
                    break;
                case R.layout.item_questions_answers_new://论文详情问答列表(新)
                    PaperQaVO qaVO = item.getPaperQaVO();
                    GlideApp.with(mContext).load(BASE_IMG_URL + qaVO.getAvatar()).into((ImageView) helper.getView(R.id.img_head));
                    helper.setText(R.id.tv_name, qaVO.getUsername());
                    helper.setText(R.id.tv_time, qaVO.getCreatetime());
                    helper.setVisible(R.id.img_photo, ObjectUtils.isNotEmpty(qaVO.getImgurl()));
                    helper.setText(R.id.tv_qa_title, "Q：" + qaVO.getQuiz());//首问问题
                    helper.setText(R.id.tv_ask_time_1, qaVO.getQtime());//首问时长

                    if ("1".equals(qaVO.getHasanswer())) {
                        helper.setText(R.id.tv_answer_1, "A:" + qaVO.getAusername() + qaVO.getAnswer());//首答答案
                        helper.setVisible(R.id.ll_answer_1, true);
                        helper.setText(R.id.tv_answer_time_1, qaVO.getAtime());//首答时长
                    } else {
                        helper.setText(R.id.tv_answer_1, "我要回答");
                        helper.setVisible(R.id.ll_answer_1, false);
                    }

                    if (qaVO.getNextQuiz() != null && qaVO.getNextQuiz().size() != 0 &&
                            ObjectUtils.isNotEmpty(qaVO.getNextQuiz().get(0).getQuiz())) {//有追答
                        helper.setText(R.id.tv_ask_2, qaVO.getNextQuiz().get(0).getQuiz());
                        helper.setVisible(R.id.ll_ask_hint, true);
                        helper.setText(R.id.tv_ask_time_2, qaVO.getNextQuiz().get(0).getQtime());
                        if ("1".equals(qaVO.getNextQuiz().get(0).getHasanswer())) {
                            helper.setText(R.id.tv_answer_2, "A:" + qaVO.getNextQuiz().get(0).getAusername()
                                    + qaVO.getNextQuiz().get(0).getAnswer());
                            helper.setText(R.id.tv_answer_time_2, qaVO.getNextQuiz().get(0).getAtime());//追答时长
                            helper.setVisible(R.id.ll_answer_2_all, true);
                        } else {
                            helper.setText(R.id.tv_answer_2, "我要追答");
                            helper.setVisible(R.id.ll_answer_2_all, false);
                        }
                    } else {
                        helper.setText(R.id.tv_ask_2, "我要追问");//追问问题
                        helper.setVisible(R.id.ll_ask_hint, false);
                        helper.setVisible(R.id.tv_answer_2, false);
                        helper.setVisible(R.id.ll_answer_2_all, false);
                    }

                    helper.getView(R.id.tv_answer_1).setOnClickListener(view -> {
                        if ("2".equals(qaVO.getHasanswer())) {
                            ProblemActivity.actionActivity(activity, "answer", imgId, pid, qaVO.getQid(), qaVO.getImgurl());
                        }
                    });
                    helper.getView(R.id.tv_answer_2).setOnClickListener(view -> {
                        if ("2".equals(qaVO.getHasanswer())) {
                            ProblemActivity.actionActivity(activity, "answer_again", imgId, pid, qaVO.getQid(), qaVO.getImgurl());
                        }
                    });
                    helper.getView(R.id.tv_ask_2).setOnClickListener(view -> {
                        if ("2".equals(qaVO.getHasanswer())) {
                            ProblemActivity.actionActivity(activity, "examineMinutely", imgId, pid, qaVO.getQid(), qaVO.getImgurl());
                        }
                    });


                    helper.setVisible(R.id.tv_open, "1".equals(qaVO.getHasanswer()));
                    helper.getView(R.id.tv_open).setOnClickListener(v -> {
                        helper.setVisible(R.id.ll_hint, "展开".equals(((TextView) helper.getView(R.id.tv_open)).getText()));
                        helper.setText(R.id.tv_open, "展开".equals(((TextView) helper.getView(R.id.tv_open)).getText()) ? "收起" : "展开");
                    });


                    helper.getView(R.id.img_photo).setOnClickListener(v -> {
                        MyDialog dialog = new MyDialog(mContext, new MyDialog.MyDialogOnClick() {
                            @Override
                            public void onOKClick() {

                            }

                            @Override
                            public void onCancelClick() {

                            }
                        }, 1);
                        dialog.setImgView(qaVO.getImgurl());
                        dialog.setCancelable(true);//点击外部是否可以取消
                        dialog.show();
                    });
                    helper.getView(R.id.ll_ask_1).setOnClickListener(v -> PlayerUtil.getInstance().playUrl(BASE_IMG_URL + qaVO.getQrecord()));
                    helper.getView(R.id.ll_ask_2).setOnClickListener(v -> PlayerUtil.getInstance().playUrl(BASE_IMG_URL + qaVO.getNextQuiz().get(0).getQrecord()));
                    helper.getView(R.id.ll_answer_1).setOnClickListener(v -> PlayerUtil.getInstance().playUrl(BASE_IMG_URL + qaVO.getArecord()));
                    helper.getView(R.id.ll_answer_2).setOnClickListener(v -> PlayerUtil.getInstance().playUrl(BASE_IMG_URL + qaVO.getNextQuiz().get(0).getArecord()));
                    break;
                case R.layout.item_questions_answers://论文详情问答列表(旧)
                    helper.getView(R.id.img_photo).setOnClickListener(view ->
                            ToastUtils.showShort("点开了一张图片"));

                    if (item.getQuizInfoBean() != null) {
                        QAEntity.QuizInfoBean.RowsBeanXXXX quizInfoBean = item.getQuizInfoBean();
                        final String pcid = quizInfoBean.getPcid();
                        final String quid = quizInfoBean.getUid();
                        helper.setText(R.id.tv_user_name, quizInfoBean.getRealName());
                        helper.setText(R.id.tv_user_time, quizInfoBean.getCreateTime());
                        ImageLoader.getInstance().displayCricleImage(activity, quizInfoBean.getAAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));
                        QAEntity.QuizInfoBean.RowsBeanXXXX.AnswerBean answers = quizInfoBean.getAnswer();
                        QAEntity.QuizInfoBean.RowsBeanXXXX.QuizBean quizs = quizInfoBean.getQuiz();
                        if (quizs != null) {
                            List<QAEntity.QuizInfoBean.RowsBeanXXXX.QuizBean.RowsBeanX> rows = quizs.getRows();
                            if (rows != null && rows.size() > 0) {
                                QAEntity.QuizInfoBean.RowsBeanXXXX.QuizBean.RowsBeanX rowsBeanX = rows.get(0);
                                helper.setText(tv_problem, "Q: " + rowsBeanX.getContent());
                                final String qid = rowsBeanX.getId();
                                if (answers != null) {
                                    List<QAEntity.QuizInfoBean.RowsBeanXXXX.AnswerBean.RowsBean> answerRows = answers.getRows();
                                    if (answerRows != null && answerRows.size() > 0) {
                                        helper.setVisible(tv_answer_reply, false);
                                        QAEntity.QuizInfoBean.RowsBeanXXXX.AnswerBean.RowsBean answerBean = answerRows.get(0);
                                        helper.setText(tv_answer_name, answerBean.getContent())
                                                .setVisible(tv_answer_reply, true)
                                                .setVisible(tv_answer_name, true)
                                                .setText(tv_answer_reply, "A: " + answerBean.getRealName());

                                        if (!TextUtils.isEmpty(answerBean.getRecord())) {
                                            helper.setText(R.id.tv_voice, Util.formatSecond(Integer.parseInt(answerBean.getTiming()))).setVisible(R.id.tv_voice, true).addOnClickListener(R.id.tv_voice);
                                        } else {
                                            helper.setVisible(R.id.tv_voice, false);
                                        }

                                        QAEntity.QuizInfoBean.RowsBeanXXXX.AnswerBean answerTwos = quizInfoBean.getAnswerTwo();
                                        QAEntity.QuizInfoBean.RowsBeanXXXX.QuizBean quizTwos = quizInfoBean.getQuizTwo();
                                        if (quizTwos != null) {
                                            List<QAEntity.QuizInfoBean.RowsBeanXXXX.QuizBean.RowsBeanX> quizTwoRows = quizTwos.getRows();
                                            if (quizTwoRows != null && quizTwoRows.size() > 0) {
                                                helper.setVisible(linear_layout_one, true);
                                                helper.setVisible(R.id.tv_ask_btn, false);
                                                QAEntity.QuizInfoBean.RowsBeanXXXX.QuizBean.RowsBeanX rowsBeanXX = quizTwoRows.get(0);
                                                helper.setText(R.id.tv_inquiry_text, rowsBeanXX.getContent());

                                                if (answerTwos != null) {
                                                    List<QAEntity.QuizInfoBean.RowsBeanXXXX.AnswerBean.RowsBean> answerTwosRows = answerTwos.getRows();
                                                    if (answerTwosRows != null && answerTwosRows.size() > 0) {
                                                        helper.setVisible(linear_layout_two, true);
                                                        helper.setVisible(R.id.tv_answer_two, false);
                                                        QAEntity.QuizInfoBean.RowsBeanXXXX.AnswerBean.RowsBean rowsBeanXXX = answerTwosRows.get(0);
                                                        helper.setText(R.id.tv_answer_text, rowsBeanXXX.getContent());

                                                        if (!TextUtils.isEmpty(rowsBeanXXX.getRecord())) {
                                                            helper.setText(R.id.tv_voice_two, Util.formatSecond(Integer.parseInt(rowsBeanXXX.getTiming()))).setVisible(R.id.tv_voice_two, true).addOnClickListener(R.id.tv_voice_two);
                                                        } else {
                                                            helper.setVisible(R.id.tv_voice_two, false);
                                                        }

                                                    } else {
                                                        if (uid.equals(item.getPaperId())) {
                                                            helper.setVisible(R.id.tv_answer_two, true);
                                                        } else {
                                                            helper.setVisible(R.id.tv_answer_two, false);
                                                        }
                                                        helper.setVisible(linear_layout_two, false);
                                                        helper.setVisible(R.id.tv_voice_two, false);
                                                        helper.setVisible(R.id.tv_ask_btn, false);
                                                        final String id = rowsBeanXX.getId();
                                                        helper.getView(R.id.tv_answer_two).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ProblemActivity.actionActivity(activity, "answer", id, uid);
                                                            }
                                                        });
                                                    }

                                                } else {
                                                    if (uid.equals(item.getPaperId())) {
                                                        helper.setVisible(R.id.tv_answer_two, true);
                                                    } else {
                                                        helper.setVisible(R.id.tv_answer_two, false);
                                                    }
                                                    helper.setVisible(linear_layout_two, false);
                                                    helper.setVisible(R.id.tv_voice_two, false);
                                                    helper.setVisible(R.id.tv_ask_btn, false);
                                                    final String id = rowsBeanXX.getId();
                                                    helper.getView(R.id.tv_answer_two).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            ProblemActivity.actionActivity(activity, "answer", id, uid);
                                                        }
                                                    });
                                                }
                                            } else {
                                                helper.setVisible(linear_layout_one, false);
                                                helper.setVisible(linear_layout_two, false);
                                                helper.setVisible(R.id.tv_ask_btn, false);
                                                helper.setVisible(R.id.tv_answer_two, false);
                                                helper.setVisible(R.id.tv_voice_two, false);
                                                if (quizInfoBean.getUid().equals(uid)) {
                                                    helper.setVisible(R.id.tv_ask_btn, true);
                                                }
                                                helper.getView(R.id.tv_ask_btn).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ProblemActivity.actionActivity(activity, "examineMinutely", qid, uid);
                                                    }
                                                });

                                            }

                                        } else {
                                            helper.setVisible(R.id.tv_voice_two, false);
                                            helper.setVisible(linear_layout_one, false);
                                            helper.setVisible(linear_layout_two, false);
                                            helper.setVisible(R.id.tv_ask_btn, false);
                                            helper.setVisible(R.id.tv_answer_two, false);
                                            if (quizInfoBean.getUid() == uid) {
                                                helper.setVisible(R.id.tv_ask_btn, true);
                                            }
                                            helper.getView(R.id.tv_ask_btn).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    ProblemActivity.actionActivity(activity, "examineMinutely", qid, uid);
                                                }
                                            });

                                        }

                                    } else {
                                        if (uid.equals(item.getPaperId())) {
                                            helper.setText(tv_answer_reply, "回复");
                                            helper.setVisible(tv_answer_reply, true);
                                        } else {
                                            helper.setVisible(tv_answer_reply, false);

                                        }
                                        helper.setVisible(tv_answer_name, false);
                                        helper.setVisible(linear_layout_two, false);
                                        helper.setVisible(linear_layout_one, false);
                                        helper.setVisible(R.id.tv_voice_two, false);
                                        helper.setVisible(R.id.tv_voice, false);
                                        helper.setVisible(R.id.tv_ask_btn, false);
                                        helper.setVisible(R.id.tv_answer_two, false);
                                        helper.getView(tv_answer_reply).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ProblemActivity.actionActivity(activity, "answer", qid, uid);
                                            }
                                        });
                                    }

                                } else {
                                    if (uid.equals(item.getPaperId())) {
                                        helper.setText(tv_answer_reply, "回复");
                                        helper.setVisible(tv_answer_reply, true);
                                    } else {
                                        helper.setVisible(tv_answer_reply, false);
                                    }
                                    helper.setVisible(tv_answer_name, false);
                                    helper.setVisible(linear_layout_two, false);
                                    helper.setVisible(linear_layout_one, false);
                                    helper.setVisible(R.id.tv_voice_two, false);
                                    helper.setVisible(R.id.tv_voice, false);
                                    helper.setVisible(R.id.tv_ask_btn, false);
                                    helper.setVisible(R.id.tv_answer_two, false);
                                    helper.getView(tv_answer_reply).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ProblemActivity.actionActivity(activity, "answer", qid, uid);
                                        }
                                    });
                                }
                            }
                        }


                    }

                    TextView viewSwitch = helper.getView(R.id.tv_switch);

                    TextView textViewDescription = helper.getView(R.id.tv_answer_name);
                    TextView tvProblem = helper.getView(R.id.tv_problem);
                    TextView answerOne = helper.getView(R.id.tv_answer_text);
                    TextView answerTwo = helper.getView(R.id.tv_inquiry_text);
                    initSwitch(viewSwitch, tvProblem, textViewDescription, answerOne, answerTwo);
                    break;
                case R.layout.item_paper_label:
//                    final TagFlowLayout tagLabel = helper.getView(R.id.tag_flowlayout);
//                    final TextView tvSwitch = helper.getView(R.id.tv_switch);
//                    ViewGroup.LayoutParams layoutParam = tagLabel.getLayoutParams();
//                    helper.setVisible(R.id.tv_switch, DensityUtils.px2dp(activity, layoutParam.height) > 45);
//
//                    tvSwitch.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (tvSwitch.isSelected()) {
//                                tvSwitch.setText("收起");
//                                ViewGroup.LayoutParams layoutParam = tagLabel.getLayoutParams();
//                                if (DensityUtils.px2dp(activity, layoutParam.height) > 45) {
//                                    layoutParam.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                                    tagLabel.setLayoutParams(layoutParam);
//                                }
//
//                            } else {
//                                tvSwitch.setText("展开");
//                                ViewGroup.LayoutParams layoutParams = tagLabel.getLayoutParams();
//                                layoutParams.height = DensityUtils.dp2px(activity, 45);
//                                tagLabel.setLayoutParams(layoutParams);
//                            }
//                            tvSwitch.setSelected(!tvSwitch.isSelected());
//
//                        }
//                    });
//                    helper.setText(R.id.tv_label, item.getString());
//                    tagLabel.setAdapter(new TagAdapter<String>(item.getList()) {
//                        @Override
//                        public View getView(FlowLayout parent, int position, String s) {
//                            LayoutInflater mInflater = LayoutInflater.from(activity);
//                            TextView tv = (TextView) mInflater.inflate(R.layout.item_paper_tag, tagLabel, false);
//                            tv.setText(s);
//                            return tv;
//                        }
//                    });
                    break;
                case R.layout.item_message_notification:
                    if (item.getMsgEntity() != null) {
                        MsgEntity.PageInfoBean.RowsBean msgEntity = item.getMsgEntity();
                        helper.setText(R.id.tv_time, msgEntity.getMsgTime());
                        helper.setText(R.id.tv_content, msgEntity.getMsg());
                        ImageLoader.getInstance().displayCricleImage(activity, msgEntity.getAAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));
                    }
                    break;
                case R.layout.item_message_interaction:
                    if (item.getMsgEntity() != null) {
                        MsgEntity.PageInfoBean.RowsBean msgEntity = item.getMsgEntity();
                        helper.setText(R.id.tv_time, msgEntity.getMsgTime());
                        helper.setText(R.id.tv_content, msgEntity.getMsg());
                        helper.setText(R.id.tv_user_name, msgEntity.getRealName());
                        ImageLoader.getInstance().displayCricleImage(activity, msgEntity.getAAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));
                    }
                    break;
                case R.layout.item_message_answers://我的回答
                    MyAnswerVO myAnswerVO = item.getMyAnswerVO();
                    if (myAnswerVO == null)
                        return;
                    helper.setText(R.id.tv_answers_content, myAnswerVO.getAnswer());
                    helper.setText(R.id.tv_time, myAnswerVO.getCreatetime());
                    helper.setText(R.id.tv_problem_title, myAnswerVO.getQuiz());
                    helper.setText(R.id.tv_user_name, myAnswerVO.getUsername());
                    helper.setVisible(R.id.tv_answers_content, !TextUtils.isEmpty(myAnswerVO.getAnswer()));
                    helper.setVisible(R.id.tv_answers, !TextUtils.isEmpty(myAnswerVO.getAnswer()));
                    ImageLoader.getInstance().displayCricleImage(activity, myAnswerVO.getAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));


                    break;
                case R.layout.item_message_questions://我的提问
                    MyAnswerVO myQaVO = item.getMyAnswerVO();
                    helper.setText(R.id.tv_time, myQaVO.getCreatetime());
                    helper.setText(R.id.tv_problem_title, myQaVO.getQuiz());
                    if ("1".equals(myQaVO.getHasanswer())) {
                        helper.setVisible(R.id.tv_answers_content, true);
                        helper.setVisible(R.id.tv_answers, true);
                        helper.setText(R.id.tv_answers_content, myQaVO.getAnswer());
                        helper.setText(R.id.tv_answers, myQaVO.getAusername() + " 答");
                    } else {
                        helper.setVisible(R.id.tv_answers_content, false);
                        helper.setVisible(R.id.tv_answers, false);
                    }
                    break;
                case R.layout.item_collectionchild://收藏论文、收藏综述
                    if (item.getSubscribeEntityRows() == null)
                        return;
                    PaperMainVO paperMainVO = item.getPaperMainVO();
                    helper.setText(R.id.tv_title, paperMainVO.getTitle());
                    initReclerView(helper, paperMainVO);
                    break;
                case R.layout.item_collection:
                    switch (type) {
                        case "popular":
                            if (item.getNote() == null)
                                return;
                            helper.setText(R.id.tv_title, item.getNote().getTitle() + "");
                            helper.setText(R.id.tv_time, item.getNote().getCreateTime() + "");
                            helper.setText(R.id.tv_paper_browse, item.getNote().getReadNum() + "");
                            helper.setText(R.id.tv_paper_collected, item.getNote().getFollowedNum() + "");
                            //                            helper.getView(R.id.tv_paper_collected).setSelected(true);
                            helper.setText(R.id.tv_paper_download, /*item.getNote().getLikedNum() + */"");
                            helper.setText(R.id.tv_paper_problem, item.getNote().getCommentNum() + "");
                            helper.setText(R.id.tv_time, item.getNote().getCreateTime() + "");
                            helper.setVisible(R.id.tv_progress, true);
                            int playPosition = item.getNote().getPlayPosition();
                            int songsSize = item.getNote().getSongsSize();
//                            helper.getView(R.id.iv_frame).setSelected(item.getNote().getId() == paperId);
                            if (songsSize != 0) {
                                double rint = Math.rint((playPosition + 1) / (float) songsSize * 100);
                                helper.setText(R.id.tv_progress, rint + "%");
                            }

                            ImageLoader.getInstance().displayCricleImage(activity, item.getNote().getPath(), (ImageView) helper.getView(R.id.iv_icon));

                            switch (item.getNote().getVersions()) {
                                case "0":
                                    helper.setText(R.id.tv_version, "完整版");
                                    break;
                                case "1":
                                    helper.setText(R.id.tv_version, "精华版");
                                    break;
                                case "2":
                                    helper.setText(R.id.tv_version, "摘要版");
                                    break;
                            }

                            break;
                        case "plays":
                        case "play":
                            if (item.getPlayNoteVo() == null)
                                return;
                            helper.setText(R.id.tv_title, item.getPlayNoteVo().getTitle());
                            helper.setText(R.id.tv_time, item.getPlayNoteVo().getCreateTime());
                            helper.setText(R.id.tv_paper_browse, item.getPlayNoteVo().getReadnum());
                            helper.setText(R.id.tv_paper_collected, item.getPlayNoteVo().getCollectnum());
                            //                            helper.getView(R.id.tv_paper_collected).setSelected(true);
                            helper.setText(R.id.tv_paper_download, item.getPlayNoteVo().getDownloadnum());
                            helper.setText(R.id.tv_paper_problem, item.getPlayNoteVo().getQuiznum());
                            helper.setText(R.id.tv_time, item.getPlayNoteVo().getCreateTime());
//                            helper.setVisible(R.id.tv_progress, true);
//                            int playPosition = item.getPlayNoteVo().getPlayPosition();
//                            int songsSize = item.getPlayNoteVo().getSongsSize();
//                            helper.getView(R.id.iv_frame).setSelected(item.getPlayNoteVo().getId() == paperId);
//                            if (songsSize != 0) {
//                                double rint = Math.rint((playPosition + 1) / (float) songsSize * 100);
//                                helper.setText(R.id.tv_progress, rint + "%");
//                            }

//                            ImageLoader.getInstance().displayCricleImage(activity,
//                                    item.getPlayNoteVo().getPath(), (ImageView) helper.getView(R.id.iv_icon));

                            switch (item.getPlayNoteVo().getVersion()) {
                                case "2":
                                    helper.setText(R.id.tv_version, "完整版");
                                    break;
                                case "4":
                                    helper.setText(R.id.tv_version, "精华版");
                                    break;
                                case "3":
                                    helper.setText(R.id.tv_version, "摘要版");
                                    break;
                            }

                            break;
                        case "collection_summary":
                        case "collection_paper":
                           /* if (item.getSubscribeEntityRows() == null)
                                return;
                            SubscribeEntity.PageInfoBean.RowsBeanXXXXX collectionEntity = item.getSubscribeEntityRows();
                            helper.setText(R.id.tv_title, collectionEntity.getTitle());
                            helper.setText(R.id.tv_time, collectionEntity.getCreateTime());
                            //                            String firstPic = collectionEntity.getFirstPic();
                            helper.setText(R.id.tv_paper_browse, collectionEntity.getReadNum() + "");
                            helper.setText(R.id.tv_paper_collected, collectionEntity.getFollowedNum() + "");
                            helper.getView(R.id.tv_paper_collected).setSelected(true);
                            helper.addOnClickListener(R.id.tv_paper_collected);
                            helper.setText(R.id.tv_paper_download, collectionEntity.getDownloadNum() + "");
                            helper.setText(R.id.tv_paper_problem, collectionEntity.getCommentNum() + "");
                            helper.setText(R.id.tv_time, collectionEntity.getCreateTime() + "");*/
                            //                            ImageLoader.getInstance().displayCricleImage(activity, firstPic, (ImageView) helper.getView(R.id.iv_icon));
                         /*   if (collectionEntity.getPhotoOne() != null&&collectionEntity.getPhotoOne().s) {

                            }*/
                            //                            initCollection();
                            break;
                    }
                    break;
                case R.layout.item_scholar_education:
                    final UserInfoEntity.IntroductionBean introductionBean = item.getIntroduction(); // Todo 学者详情需要更改
                    helper.setText(R.id.tv_description, introductionBean.getSchool());//introductionBean.getSchool()
                    String start = introductionBean.getTimeStart().substring(0, 4);
                    String end = introductionBean.getTimeEnd().substring(0, 4);
                    String major = introductionBean.getMajor();
                    String degree = introductionBean.getDegree();
                    helper.setText(R.id.tv_university_name, start + "~" + end + ", " + major + ", " + degree);
//                    helper.setText(R.id.tv_university_name,item.getString()+item.getUrl());
                    helper.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ExperienceActivity.actionActivity(activity, "edit", introductionBean);
                        }
                    });
                    break;
                case R.layout.item_authentication_info:
                    helper.setText(R.id.tv_research_field, item.getString());
                    helper.setText(R.id.tv_research_info, item.getUrl());

                    break;
                case R.layout.item_duration_size:
                    helper.setText(R.id.tv_duration, Util.formatSecond(item.getDurations()));
                    helper.setText(R.id.tv_size, Math.rint(item.getSize()) + "M");
                    helper.setText(R.id.tv_language, item.getLanguage());
                    break;
                case R.layout.item_download_finished:
                    helper.setText(R.id.tv_title, item.record.getSaveName());
                    Log.i("--->>", "convert: " + item.record.getSavePath());
                    ImageLoader.getInstance().displayCricleImage(activity, new File(item.record.getSavePath() + "/" + item.record.getSaveName()), (ImageView) helper.getView(R.id.iv_icon));
                    break;
                case R.layout.item_download_download:
                    final ProgressBar progressBar = helper.getView(R.id.pb_download_progress);
                    final TextView size = helper.getView(R.id.tv_download_size);
                    final int[] flag = new int[1];
                    final DownloadController mDownloadController = new DownloadController((TextView) helper.getView(R.id.tv_download_status), (ImageView) helper.getView(R.id.tv_download_icon));
                    //                    f.setmDownloadController(mDownloadController);
              /*      helper.getView(R.id.tv_download_size).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });*/

                    item.setDownloadController(mDownloadController);
                    item.disposable = RxDownload.getInstance(activity).receiveDownloadStatus(item.record.getUrl())
                            .subscribe(new Consumer<DownloadEvent>() {
                                @Override
                                public void accept(DownloadEvent downloadEvent) throws Exception {

                                    if (flag[0] != downloadEvent.getFlag()) {
                                        flag[0] = downloadEvent.getFlag();
                                        Log.d("", flag[0] + "");
                                    }
                                    if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
                                        Throwable throwable = downloadEvent.getError();
                                        Log.w("TAG", throwable);
                                    }
                                    mDownloadController.setEvent(downloadEvent);
                                    updateProgressStatus(progressBar, downloadEvent.getDownloadStatus(), size);
                                    if (downloadEvent.getFlag() == DownloadFlag.COMPLETED)
                                        remove(helper.getLayoutPosition());
                                }
                            });
                    break;
                case R.layout.item_scholar:
                    if (item.getMyScholarBean() == null)
                        return;
                    MyScholarBean scholarBean = item.getMyScholarBean();
                    ImageLoader.getInstance().displayImage(activity, scholarBean.getAvatar(), (ImageView) helper.getView(R.id.iv_scholar_icon), false);
                    helper.setText(R.id.tv_scholar_name, scholarBean.getRealName());
                    helper.setText(R.id.tv_user_position, scholarBean.getRealName());
                    break;
                case R.layout.item_attention_text:
                    switch (type) {
                        case "1":
                            break;
                        case "2":
                            break;
                    }
                    if (item.getTypeBean() == null)
                        return;

                    helper.setText(R.id.tv_attention, item.getTypeBean().getContent());
                    helper.getView(R.id.tv_attention).setSelected(item.isSelected());
                    helper.addOnClickListener(R.id.tv_attention);
                    break;
                case R.layout.item_attention_img:
                    if (item.getMyScholarBean() == null)
                        return;
                    MyScholarBean myScholarBean = item.getMyScholarBean();
                    ImageView imageView = helper.getView(R.id.iv_scholar_icon);
                    imageView.setSelected(!item.isSelected());
                    ImageLoader.getInstance().displayCricleImage(activity, myScholarBean.getAvatar(), imageView);
                    helper.setText(R.id.tv_scholar_name, myScholarBean.getRealName()).addOnClickListener(R.id.iv_scholar_icon);
                    break;
                case R.layout.item_paper://首页综述、论文、订阅
                    if (item.getPaperMainVO() != null) {
                        PaperMainVO vo = item.getPaperMainVO();
                        helper.setText(R.id.tv_no, "[" + getParentPosition(item) + 1 + "]" + vo.getCode());
                        helper.setText(R.id.tv_naov, vo.getUsername());
                        helper.setText(R.id.tv_title, vo.getTitle());
                        helper.setText(R.id.tv_author, vo.getAuthors());
                        helper.setText(R.id.tv_subject, vo.getSubject());
                        GlideApp.with(mContext)
                                .load(vo.getUseravatar())
                                .into((ImageView) helper.getView(R.id.iv_user_icon));
                        if (vo.getVlist() != null && vo.getVlist().size() != 0) {
                            List<ClickEntity> list = new ArrayList<>();
                            for (PaperMainVO.VlistBean bean : vo.getVlist()) {
                                bean.setPid(vo.getId());
                                ClickEntity entity = new ClickEntity();
                                bean.setEnglish(false);
                                entity.setBean(bean);
                                list.add(entity);
                            }

                            ItemClickAdapter adapter = new ItemClickAdapter(activity, R.layout.item_paper_child_new, list, mListItemClick);
                            ((MyRecyclerView) helper.getView(R.id.my_recycler_view)).setLayoutManager(new LinearLayoutManager(activity));
                            ((MyRecyclerView) helper.getView(R.id.my_recycler_view)).setAdapter(adapter);
                            ((CheckBox) helper.getView(R.id.cb_menu)).setOnCheckedChangeListener((compoundButton, b) -> {
                                helper.getView(R.id.my_recycler_view).setVisibility(b ? View.VISIBLE : View.GONE);
                            });
                            ((RadioButton) helper.getView(R.id.rb_cn)).setOnCheckedChangeListener((compoundButton, b) -> {
                                if (b) {
                                    vo.setMyLanguage("1");//记录是中文
                                    list.clear();
                                    for (PaperMainVO.VlistBean bean : vo.getVlist()) {
                                        ClickEntity entity = new ClickEntity();
                                        bean.setEnglish(false);
                                        entity.setBean(bean);
                                        list.add(entity);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            ((RadioButton) helper.getView(R.id.rb_en)).setOnCheckedChangeListener((compoundButton, b) -> {
                                if (b) {
                                    vo.setMyLanguage("2");//记录是英文
                                    list.clear();
                                    for (PaperMainVO.VlistBean bean : vo.getVlist()) {
                                        ClickEntity entity = new ClickEntity();
                                        bean.setEnglish(true);
                                        entity.setBean(bean);
                                        list.add(entity);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                    break;
                case R.layout.item_paper_child_new://论文综述订阅子列表
                    if (item.getBean() != null) {
                        PaperMainVO.VlistBean vlistBean = item.getBean();
                        switch (vlistBean.getVersion()) {
                            case 2:
                                helper.setImageResource(R.id.img_icon, R.mipmap.img_wanzheng);
                                helper.setText(R.id.tv_icon, "完整");
                                break;
                            case 3:
                                helper.setImageResource(R.id.img_icon, R.mipmap.img_zhaiyao);
                                helper.setText(R.id.tv_icon, "摘要");
                                break;
                            case 4:
                                helper.setImageResource(R.id.img_icon, R.mipmap.img_jinghua);
                                helper.setText(R.id.tv_icon, "精华");
                                break;
                            default:
                                break;
                        }
                        helper.setChecked(R.id.cb_collect, "1".equals(vlistBean.getIsfollow()));
                        helper.getView(R.id.cb_collect).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (((CheckBox) helper.getView(R.id.cb_collect)).isChecked()) {
                                    mListItemClick.listClick("collect", vlistBean.getPid(), vlistBean.getVersion() + "", vlistBean.isEnglish() ? "2" : "1");
                                } else {
                                    mListItemClick.listClick("nocollect", vlistBean.getPid(), vlistBean.getVersion() + "", vlistBean.isEnglish() ? "2" : "1");
                                }
                            }
                        });
                        helper.setChecked(R.id.cb_download, "1".equals(vlistBean.getIsdownload()));
                        helper.getView(R.id.cb_download).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListItemClick.listClick("download", vlistBean.getId(), vlistBean.getVersion() + "", vlistBean.isEnglish() ? "2" : "1");
                            }
                        });
                        helper.setChecked(R.id.cb_add, "1".equals(vlistBean.getIsaddplay()));
                        helper.getView(R.id.cb_add).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListItemClick.listClick("add", vlistBean.getPid(), vlistBean.getVersion() + "", vlistBean.isEnglish() ? "2" : "1");
                            }
                        });
                        String btnString;
                        if (vlistBean.isEnglish()) {
                            btnString = "EN   ▷" + vlistBean.getEnduration() + "   " + vlistBean.getEnsize();
                        } else {
                            btnString = "CN   ▷" + vlistBean.getCnduration() + "   " + vlistBean.getCnsize();
                        }
                        helper.setText(R.id.tv_play, btnString);
//                        ((AnimDownloadProgressButton) helper.getView(R.id.anim_btn)).setText(btnString);
//                        ((AnimDownloadProgressButton) helper.getView(R.id.anim_btn)).set
                    }

                    break;
                case R.layout.item_paper_tag:
                    helper.setText(R.id.tv_tag, item.getString());
                    break;
                case R.layout.item_home_tab:
                    ImageLoader.getInstance().displayCricleImage(activity, item.getRid(), (ImageView) helper.getView(R.id.iv_icon));
                    helper.setText(R.id.tv_title, item.getString());
                    break;
                case R.layout.item_home_child:
                    helper.setText(R.id.tv_title, item.getString());
                    ImageLoader.getInstance().displayImage(activity, item.getUrl(), (ImageView) helper.getView(R.id.iv_icon));
                    break;
                case R.layout.item_paper_pic://论文详情
                    PaperInfoVO infoVO = item.getPaperInfoVO();
                    View bg = helper.getView(R.id.iv_bg);
                    if (helper.getLayoutPosition() == selectedPosition) {
                        Log.i("-->>", "convert: " + selectedPosition);
                        bg.setVisibility(View.INVISIBLE);
                    } else {
                        bg.setVisibility(View.VISIBLE);
                    }
                    if ("mp4".equals(FileUtils.parseSuffix(infoVO.getUrl()))) { // 视频
                        ImageView imageView1 = helper.getView(R.id.iv_pic);
                        imageView1.setImageBitmap(ImageLoader.getInstance().createVideoThumbnail(infoVO.getUrl(), 1));
                    } else {
                        ImageLoader.getInstance().displayImage(activity, BASE_IMG_URL + infoVO.getUrl(), (ImageView) helper.getView(R.id.iv_pic));
                    }
                    break;
                case R.layout.item_dubbing_pic://配音图片详情
                    DubbingVO dubbingVO = item.getDubbingVO();
                    if (helper.getLayoutPosition() == selectedPosition) {
                        Log.i("-->>", "convert: " + selectedPosition);
                        helper.getView(R.id.iv_bg).setVisibility(View.INVISIBLE);
                    } else {
                        helper.getView(R.id.iv_bg).setVisibility(View.VISIBLE);
                    }
                    if ("mp4".equals(FileUtils.parseSuffix(dubbingVO.getUrl()))) { // 视频
                        ImageView imageView1 = helper.getView(R.id.iv_pic);
                        imageView1.setImageBitmap(ImageLoader.getInstance().createVideoThumbnail(dubbingVO.getUrl(), 1));
                    } else {
                        ImageLoader.getInstance().displayImage(activity, BASE_IMG_URL + dubbingVO.getUrl(), (ImageView) helper.getView(R.id.iv_pic));
                    }
                    break;
                case R.layout.item_paperpull_pic: // TODO 图片+视频
                    DubbingVO vo = item.getDubbingVO();
                    View viewBg = helper.getView(R.id.iv_bg);
                    if (helper.getLayoutPosition() == selectedPosition) {
                        Log.i("-->>", "convert: " + selectedPosition);
                        viewBg.setVisibility(View.INVISIBLE);
                    } else {
                        viewBg.setVisibility(View.VISIBLE);
                    }
                    if ("mp4".equals(FileUtils.parseSuffix(vo.getUrl()))) { // 视频
                        ImageView imageView1 = helper.getView(R.id.iv_pic);
                        imageView1.setImageBitmap(ImageLoader.getInstance().createVideoThumbnail(vo.getUrl(), 1));
                    } else {
                        ImageLoader.getInstance().displayImage(activity, BASE_IMG_URL + vo.getUrl(), (ImageView) helper.getView(R.id.iv_pic));
                    }
                    break;
                case R.layout.item_search:
                    helper.addOnClickListener(R.id.tv_empty);
                    final TagFlowLayout tagFlowLayout = helper.getView(R.id.tag_flowlayout);
                    final TagAdapter<String> adapter = new TagAdapter<String>(item.getList()) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            LayoutInflater mInflater = LayoutInflater.from(activity);
                            TextView tv = (TextView) mInflater.inflate(R.layout.item_search_text,
                                    tagFlowLayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    };
                    tagFlowLayout.setAdapter(adapter);

                    tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            if (activity instanceof SearchActivity) {
                                ((SearchActivity) activity).initSearch(adapter.getItem(position));
                            }
                            return false;
                        }
                    });
                    helper.setText(R.id.tv_search_title, item.getString());
                    helper.setText(R.id.tv_empty, item.getUrl());

                    break;
                case R.layout.item_search_result://搜索结果
                    final int layoutPosition = helper.getLayoutPosition();
                    RecyclerView recyclerResult = helper.getView(R.id.recyclerview);
                    recyclerResult.setLayoutManager(new LinearLayoutManager(activity));

                    List<ClickEntity> listResult = new ArrayList<>();

                    if (item.getScholarBean() != null) {//搜索粉丝
                        List<HomeSearchEntity.ScholarBean.RowsBean> rowsX = item.getScholarBean().getRows();
                        for (HomeSearchEntity.ScholarBean.RowsBean rowsBean : rowsX) {
                            listResult.add(new ClickEntity(rowsBean));
                        }
                        ItemClickAdapter searchScholar = new ItemClickAdapter(activity, R.layout.item_search_scholar, listResult, "search_scholar");
                        recyclerResult.setAdapter(searchScholar);
                    } else if (item.getPaperBean() != null) {//搜索论文
                        List<HomeSearchEntity.PaperBean.RowsBeanX> paperBeanRows = item.getPaperBean().getRows();
                        for (HomeSearchEntity.PaperBean.RowsBeanX paperBeanRow : paperBeanRows) {
                            listResult.add(new ClickEntity(paperBeanRow));
                        }
                        ItemClickAdapter searchPaper = new ItemClickAdapter(activity, R.layout.item_paper_indexes, listResult, "search_paper");
                        recyclerResult.setAdapter(searchPaper);
                        searchPaper.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                ClickEntity item = (ClickEntity) adapter.getItem(position);
                                if (item != null && item.getPaperBeanRows() != null) {
                                    HomeSearchEntity.PaperBean.RowsBeanX paperBean = item.getPaperBeanRows();
                                    String id = paperBean.getId();
                                    String version = paperBean.getVersion();
                                    PaperDetailsActivity.actionActivityNew(activity, id, version, "online_paper", CN);
                                }
                            }
                        });
                    }
                    helper.setVisible(R.id.tv_more, false);
                    helper.setText(R.id.tv_scholar_name, item.getString());
                    helper.setText(R.id.tv_more, item.getUrl()).setOnClickListener(R.id.tv_more, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (activity instanceof SearchActivity) {
                                ((SearchActivity) activity).getViewPager().setCurrentItem(layoutPosition + 2);
                            }
                        }
                    });

                    break;
                case R.layout.item_paper_indexes://论文详情文献
                    TextView viewSwitch1 = helper.getView(R.id.tv_switch);
                    if (item.getLiteratureInfoBean() != null) {
                        helper.setVisible(R.id.tv_title, false);
                        List<UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean> list = new ArrayList<>();
                        helper.setText(R.id.tv_description, item.getLiteratureInfoBean().getContent());
                   /*     for (int i = 0; i < item.getLiteratureInfoBean().getAuthors().size(); i++) {
                            list.add(item.getLiteratureInfoBean().getAuthors().get(i));
                        }*/
                        //                        initFlow(helper, list);
                    }
                    if (item.getPaperBeanRows() != null) {
                        List<UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean> list = new ArrayList<>();

                        helper.setText(R.id.tv_title, item.getPaperBeanRows().getTitle());
                        helper.setText(R.id.tv_description, item.getPaperBeanRows().getDigest() + "");
                        for (int i = 0; i < item.getPaperBeanRows().getAuthors().getRows().size(); i++) {
                            list.add(item.getPaperBeanRows().getAuthors().getRows().get(i));
                        }
                        initFlow(helper, list);
                    }
                    if (item.getSummarizeBean() != null) {
                        List<UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean> list = new ArrayList<>();

                        UserInfoEntity.SummarizeBean summarizeBean = item.getSummarizeBean();

                        helper.setText(R.id.tv_title, summarizeBean.getTitle());
                        helper.setText(R.id.tv_description, summarizeBean.getDigest());
                        for (int i = 0; i < summarizeBean.getAuthors().getRows().size(); i++) {
                            list.add(summarizeBean.getAuthors().getRows().get(i));
                        }

                        initFlow(helper, list);
                    }
                    TextView textViewDescription1 = helper.getView(R.id.tv_description);
                    if (textViewDescription1.getLineCount() == 1) {
                        viewSwitch1.setVisibility(View.GONE);
                    } else {
                        initSwitch(viewSwitch1, new TextView(activity), textViewDescription1, new TextView(activity), new TextView(activity));

                    }
                    break;
                case R.layout.item_paper_indexes_new://论文详情文献
                    if (item.getPaperLiteratureVO() != null) {
                        helper.setText(R.id.tv_title, item.getPaperLiteratureVO().getTitle());
                        helper.setText(R.id.tv_info, item.getPaperLiteratureVO().getDigest()
                                + item.getPaperLiteratureVO().getDigest() + item.getPaperLiteratureVO().getDigest());
                        helper.setText(R.id.tv_author, item.getPaperLiteratureVO().getAuthor());
                        TextView tv = helper.getView(R.id.tv_switch);
                        TextView tvInfo = helper.getView(R.id.tv_info);
                        tv.setOnClickListener(v -> {
                            tv.setText(tvInfo.getMaxLines() == 1 ? "收起" : "展开");
                            tvInfo.setMaxLines(tvInfo.getMaxLines() == 1 ? 100 : 1);
                        });
                    }
                    break;
                case R.layout.item_search_scholar://我的粉丝，搜索粉丝，我的关注
                    if (item.getFansVO() != null) {//粉丝
                        FansVO fansVO = item.getFansVO();

//                        HomeSearchEntity.ScholarBean.RowsBean rowsBeanHomeEntity = item.getRowsBeanHomeEntity();
                        TextView textViewAttention = helper.getView(R.id.tv_attention);
                        helper.setText(R.id.tv_user_name, fansVO.getName());
                        helper.setText(R.id.tv_position, fansVO.getOrganization());
                        ImageLoader.getInstance().displayCricleImage(activity, fansVO.getAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));

                        final String isFollowed = fansVO.getIsfollow(); // todo 关注
                        final String id = fansVO.getId();
                        helper.getView(R.id.iv_user_icon).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentActivity.actionActivity(activity, "he", id + "");
                            }
                        });
                        if (id.equals(uid)) {
                            textViewAttention.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(isFollowed)) {
                            switch (isFollowed) {
                                case "0":
                                    textViewAttention.setText("+关注");
                                    textViewAttention.setBackground(null);
                                    textViewAttention.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (activity instanceof SearchActivity) {
                                                ((SearchActivity) activity).submitFollow(isFollowed, id);
                                            }
                                        }
                                    });

                                    break;
                                case "1":
                                    textViewAttention.setText("已关注");
                                    textViewAttention.setBackground(null);
                                    textViewAttention.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.showDialog(activity, "确定取消关注", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (activity instanceof SearchActivity) {
                                                        ((SearchActivity) activity).submitFollow(isFollowed, id);
                                                    } else if (activity instanceof FragmentActivity) {
                                                        ((FragmentActivity) activity).submitFollow(isFollowed, id);
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    break;
                                case "2":
                                    textViewAttention.setText("互相关注"); // TODO 关注
                                    textViewAttention.setBackground(null);
                                    textViewAttention.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.showDialog(activity, "确定取消关注", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (activity instanceof SearchActivity) {
                                                        ((SearchActivity) activity).submitFollow(isFollowed, id);
                                                    } else if (activity instanceof FragmentActivity) {
                                                        ((FragmentActivity) activity).submitFollow(isFollowed, id);
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }

                        }

                    }
                    if (item.getFollowVO() != null) {//关注
                        TextView textViewAttention = helper.getView(R.id.tv_attention);
                        FollowVO followVO = item.getFollowVO();
                        helper.setText(R.id.tv_user_name, followVO.getName());
                        helper.setText(R.id.tv_position, followVO.getOrganization());
                        ImageLoader.getInstance().displayCricleImage(activity, followVO.getAvatar(), (ImageView) helper.getView(R.id.iv_user_icon));
                        final String id = followVO.getId();
                        helper.getView(R.id.iv_user_icon).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentActivity.actionActivity(activity, "he", id + "");
                            }
                        });
                        if (!id.equals(uid)) {
                            textViewAttention.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utils.showDialog(activity, "确定取消关注", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (activity instanceof SearchActivity) {
                                                ((SearchActivity) activity).submitFollow(1 + "", id);
                                            } else if (activity instanceof FragmentActivity) {
                                                ((FragmentActivity) activity).submitFollow(1 + "", id);
                                            }
                                        }
                                    });

                                }
                            });
                            textViewAttention.setText("已关注");
                        }
                    }
                    break;
                case R.layout.item_popoupwindow_languageselect:
                    TextView paperLanguage = helper.getView(R.id.paper_language);
                    switch (item.getString()) {
                        case "CN":
                            paperLanguage.setText("CN");
                            paperLanguage.setBackgroundResource(R.mipmap.paper_cn_bg);
                            break;
                        case "EN":
                            paperLanguage.setText("EN");
                            paperLanguage.setBackgroundResource(R.mipmap.paper_en_bg);
                            break;
                    }
                    break;
                case R.layout.item_dubbing_sound:
                    /*helper.setText(R.id.tv_duration, item.getDuration());
                    final String filePath = item.getDubbingUrl();
                    helper.setText(R.id.tv_language, item.getLanguage());
                    helper.setText(R.id.tv_time, item.getTime());
                    final TextView textView = helper.getView(R.id.tv_duration);
                    final ImageView imageView = helper.getView(R.id.iv_dubbing_icon);
                    final boolean[] mIsPlay = new boolean[2];*/

                    break;
                case R.layout.item_waiting_dubbing:
                    Log.i("--->>", "convert: " + helper.getLayoutPosition());
                    if (item.getCollectionEntity() == null)
                        return;
                    CollectionEntity.PageInfoBean.RowsBean collectionEntity = item.getCollectionEntity();
                    helper.setText(R.id.tv_title, collectionEntity.getTitle());
                    helper.setText(R.id.tv_time, collectionEntity.getCreateTime());
                    ImageLoader.getInstance().displayCricleImage(activity, collectionEntity.getFirstPic(), (ImageView) helper.getView(R.id.iv_icon));

                    break;
            }
        }

    }

    private void initReclerView(BaseViewHolder helper, PaperMainVO paperMainVO) {
        List<ClickEntity> list = new ArrayList<>();
        for (PaperMainVO.VlistBean bean : paperMainVO.getVlist()) {
            ClickEntity entity = new ClickEntity();
            entity.setVlistBean(bean);
            list.add(entity);
        }
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        ExpandableItemAdapter expandableItemAdapter =
                new ExpandableItemAdapter(activity,
                        R.layout.item_paper_child,
                        R.layout.item_paper_child,
                        list, type);
        recyclerView.setAdapter(expandableItemAdapter);
        switch (type) {
            case "collection_paper":
            case "collection_summary":
                expandableItemAdapter.expandAll();
                break;
        }

        expandableItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ClickEntity clickEntity = (ClickEntity) adapter.getItem(position);
                if (clickEntity != null) {
                    if (clickEntity.getPaperId() != null)
                        PaperDetailsActivity.actionActivityNew(activity,
                                clickEntity.getPaperId(),
                                clickEntity.getVersion(),
                                "online_paper",
                                CN);
                }
            }
        });
    }

    //
    private void initFlow(BaseViewHolder helper, final List<UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean> list) {
        final TagFlowLayout flAuthor = helper.getView(R.id.fl_author);
        final TagAdapter<UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean> adapterFlAuthor = new TagAdapter<UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean>(list) {
            @Override
            public View getView(FlowLayout parent, int position, UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean s) {
                LayoutInflater mInflater = LayoutInflater.from(activity);
                TextView tv = (TextView) mInflater.inflate(R.layout.item_search_text,
                        flAuthor, false);
                if (!"0".equals(s.getIsGiiisp())) {
                    tv.setTextColor(activity.getResources().getColor(R.color.colorInterface));
                } else {
                    tv.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                }
                tv.setText(s.getAuthor());
                return tv;
            }
        };

        flAuthor.setAdapter(adapterFlAuthor);
        flAuthor.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                UserInfoEntity.SummarizeBean.AuthorsBeanX.RowsBean authorsBean = adapterFlAuthor.getItem(position);
                if (authorsBean != null) {
                    String uid = authorsBean.getIsGiiisp();
                    if (!"0".equals(uid)) {
                        FragmentActivity.actionActivity(activity, "he", uid);
                    }
                }
                return false;
            }
        });
    }

    private void initSwitch(final TextView textViewSwitch, final TextView tvProblem, final TextView textViewDescription, final TextView one, final TextView two) {
        textViewSwitch.setText("展开");
        textViewDescription.setMaxLines(1);
        tvProblem.setMaxLines(1);
        one.setMaxLines(1);
        two.setMaxLines(1);

        textViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewDescription.getMaxLines() == 1 && one.getMaxLines() == 1 && two.getMaxLines() == 1) {
                    textViewSwitch.setText("收起");
                    textViewDescription.setMaxLines(Integer.MAX_VALUE);
                    one.setMaxLines(Integer.MAX_VALUE);
                    tvProblem.setMaxLines(Integer.MAX_VALUE);
                    two.setMaxLines(Integer.MAX_VALUE);
                    textViewSwitch.setSelected(!textViewSwitch.isSelected());

                } else {
                    textViewSwitch.setText("展开");
                    textViewDescription.setMaxLines(1);
                    one.setMaxLines(1);
                    tvProblem.setMaxLines(1);
                    two.setMaxLines(1);
                    textViewSwitch.setSelected(!textViewSwitch.isSelected());
                }
            }
        });
    }


}
