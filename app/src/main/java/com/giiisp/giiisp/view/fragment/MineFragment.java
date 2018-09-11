package com.giiisp.giiisp.view.fragment;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.MIneInfoBean;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.UserInfoEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ImageLoader;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.AttentionActivity;
import com.giiisp.giiisp.view.activity.FragmentActivity;
import com.giiisp.giiisp.view.activity.GiiispActivity;
import com.giiisp.giiisp.view.activity.SettingActivity;
import com.giiisp.giiisp.view.activity.VerifiedActivity;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static com.giiisp.giiisp.base.BaseActivity.emailauthen;
import static com.giiisp.giiisp.base.BaseActivity.isVip;
import static com.giiisp.giiisp.base.BaseActivity.uid;

/**
 * '我的'页面
 * Created by Thinkpad on 2017/5/4.
 */

public class MineFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements BaseImpl, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_user_icon)
    ImageView ivUserIcon;
    //    @BindView(R.id.iv_attention)
//    ImageView ivAttention;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.tv_user_position)
    TextView tvUserPosition;
    //    @BindView(R.id.tv_user_phone)
//    TextView tvUserPhone;
    @BindView(R.id.tv_recording_authentication)
    TextView tvRecordinAuthentication;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    //    @BindView(R.id.tv_paper_number)
//    TextView tvPaperNumber;
    @BindView(R.id.tv_mine_download)
    TextView tvMineDownload;
    @BindView(R.id.tv_follow_number)
    TextView tvFollowNumber;
    //    @BindView(R.id.tv_review_number)
//    TextView tvReviewNumber;
    @BindView(R.id.tv_fans_number)
    TextView tvFansNumber;
    @BindView(R.id.tv_arrow)
    TextView tvArrow;
    //    @BindView(R.id.fl_mine_qa)
//    FrameLayout flMineQa;
//    @BindView(R.id.fl_mine_download)
//    FrameLayout flMineDownload;
//    @BindView(R.id.fl_mine_subscribe)
//    FrameLayout flMineSubscribe;
//    @BindView(R.id.fl_mine_news)
//    FrameLayout flMineNews;
//    @BindView(R.id.fl_mine_contacts)
//    FrameLayout flMineContacts;
//    @BindView(R.id.fl_mine_collection)
//    FrameLayout flMineCollection;
//    @BindView(R.id.fl_mine_setting)
//    FrameLayout flMineSetting;
    @BindView(R.id.ll_empty_view)
    LinearLayout emptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    //    @BindView(R.id.tv_user_web)
//    TextView tvUserWeb;
    @BindView(R.id.tv_verified)
    TextView tvVerified;
    @BindView(R.id.tv_code_number)
    TextView mTvCodeNumber;
    @BindView(R.id.tv_time_number)
    TextView mTvTimeNumber;
    private int downloadNunber;
    private String imageUrl = "";

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_mine;
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (entity instanceof UserInfoEntity) {
//            UserInfoEntity userInfoEntity = (UserInfoEntity) entity;
//            if (userInfoEntity.getUserInfo() != null) {
//                initUser(userInfoEntity);
//            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void initUser(MIneInfoBean infoBean) {
        if (context == null)
            return;
        timeout = false;
        if (emptyView == null) {
            return;
        }
        emptyView.setVisibility(View.GONE);
//        EventBus.getDefault().post(userInfoEntity);
//        UserInfoEntity.UserInfoBean userInfo = userInfoEntity.getUserInfo();
        String avatar = infoBean.getAvatar();
        if (!avatar.equals(imageUrl))
            ImageLoader.getInstance().displayCricleImage((BaseActivity) getActivity(), avatar, ivUserIcon);
        imageUrl = avatar;
        tvUserName.setText(infoBean.getName());
        tvUserEmail.setText(infoBean.getEmail());
        String sex = infoBean.getSex();
        if ("1".equals(sex)) {
            ivSex.setImageResource(R.mipmap.ic_sex_male);
        }
        if ("2".equals(sex)) {
            ivSex.setImageResource(R.mipmap.ic_sex_female);
        }
//        if (TextUtils.isEmpty(web)) {
//            tvUserWeb.setText("未添加个人网址");
//        } else {
//            tvUserWeb.setText(web);
//        }
//        ArrayMap<String, Object> map = new ArrayMap<>();
//        map.put("uid", uid);
//        map.put("token", token);
//        map.put("mobile", userInfo.getMobile() + "");
//        map.put("loginType", 2);
//        presenter.saveClientTypeData(map);
//        if (Utils.checkMobileNumber(userInfo.getMobile())) {
//            tvUserPhone.setText(userInfo.getMobile());
//        } else {
//            tvUserPhone.setText("未绑定手机号码");
//        }
        tvUserEmail.setText(Utils.checkEmail(infoBean.getEmail()) ? infoBean.getEmail() : "未绑定邮箱");
        if (TextUtils.isEmpty(infoBean.getIsvip())) {  //  Test TextUtils.isEmpty(userInfo.getIsVIP()) 修改字段 isvip 替换
            Log.d("Presenter", "initUser: isIVP: " + infoBean.getIsvip());
            emailauthen = "0";
            tvRecordinAuthentication.setText("去认证");
            tvRecordinAuthentication.setCompoundDrawables(null, null, null, null);
        } else {
            emailauthen = infoBean.getEmailauthen(); // isvip = 1,2 认证完成 （身份认证判断），0 ：身份认证，3：认证中；
            isVip = infoBean.getIsvip();
            switch (emailauthen) { // 新认证字段 // userInfo.getEmailauthen()
                case "3":
                    tvRecordinAuthentication.setText("去认证");
                    tvRecordinAuthentication.setCompoundDrawables(null, null, null, null);
                    break;
                case "2":
                    tvRecordinAuthentication.setText("开始配音");
                    break;
                case "1":
                    tvRecordinAuthentication.setText("正在认证中");
                    break;
                default:
                    tvRecordinAuthentication.setText("未认证");
                    break;
            }
//            switch (isVip) {
//                case "0":
//                    tvVerified.setVisibility(View.VISIBLE);
//                    tvVerified.setText("身份认证");
//                    break;
//                case "1":
//                    tvVerified.setVisibility(View.VISIBLE);
//                    break;
//                case "2":
//                    tvVerified.setVisibility(View.VISIBLE);
//                    break;
//                case "3":
//                    tvVerified.setVisibility(View.VISIBLE);
//                    tvVerified.setText("认证中");
//                    break;
//            }
        }
        tvPrompt.setText(infoBean.getSchool());
        if (!TextUtils.isEmpty(infoBean.getDepartment()) && TextUtils.isEmpty(infoBean.getPosition())) {
            tvUserPosition.setText(infoBean.getDepartment() + "|" + infoBean.getPosition());
        } else {
            tvUserPosition.setVisibility(View.GONE);
        }
        tvFansNumber.setText(infoBean.getFansnum());
        tvFollowNumber.setText(infoBean.getFollownum());
        mTvCodeNumber.setText(infoBean.getScore() + "分");
        mTvTimeNumber.setText(infoBean.getStudytime() + "小时");
    }

    @Override
    public void onFailure(String msg, Exception ex) {
        super.onFailure(msg, ex);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }


    @Override
    public void initView() {
        tvBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.mine);
//        ivAttention.setVisibility(View.GONE);
        tvArrow.setVisibility(View.VISIBLE);
        //        emptyView.setVisibility(View.VISIBLE);
        loadDownloadNunber();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAuxiliary);
        swipeRefreshLayout.setOnRefreshListener(this);
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setBackgroundResource(R.mipmap.img_setting);
    }

    @Override
    public void initData() {
        super.initData();
        initNetwork();

    }

    @Override
    public void initNetwork() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("language", getLanguage());
        presenter.getDataAll("306", map);
        super.initNetwork();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void addDownload() {
        Log.i("--->>", "addDownload: ");
        String text = tvMineDownload.getText().toString();
        Integer integer;
        if (TextUtils.isEmpty(text)) {
            integer = 1;
        } else {
            integer = Integer.valueOf(text) + 1;
        }
        tvMineDownload.setText(String.valueOf(integer));
        super.addDownload();
    }


    @OnClick({R.id.tv_verified, R.id.ll_summary, R.id.ll_paper, R.id.iv_empty,
            R.id.tv_empty, R.id.fl_mine_history, R.id.tv_follow_number, R.id.tv_follow,
            R.id.rl_user_info, R.id.ll_dubbing, R.id.tv_fans_number,
            R.id.tv_fans, R.id.fl_mine_qa, R.id.fl_mine_download, R.id.fl_mine_subscribe,
            R.id.ll_meeting, R.id.ll_collection, R.id.iv_menu, R.id.ll_email, R.id.fl_auth,
            R.id.ll_course,
            R.id.fl_mine_news, R.id.fl_mine_contacts, R.id.fl_mine_collection, R.id.fl_mine_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_empty:
            case R.id.tv_empty:
                initNetwork();
                break;
            case R.id.rl_user_info:
            case R.id.iv_menu://设置
                SettingActivity.actionActivity(getContext());
                break;
//            case R.id.rl_user_info:
//                FragmentActivity.actionActivity(getContext(), "he", uid + "");
//                break;
            case R.id.fl_mine_history:
                FragmentActivity.actionActivity(getContext(), "plays");
                break;
            case R.id.ll_dubbing:
                //
                switch (emailauthen) { //  开始录音是否Ok  test 1 BaseActivity.emailauthen
                    case "3"://未认证
                        FragmentActivity.actionActivity(getContext(), "mailbox_authentication");
                        break;
                    case "2"://认证成功
                        FragmentActivity.actionActivity(getContext(), "wait_dubbing"); //  认证完成开始录音
                        break;
                    case "1"://认证中
                        Utils.showToast(R.string.in_authentication);
                        break;
                    default:
                        //FragmentActivity.actionActivity(getContext(), "wait_dubbing");
                        break;
                }

                break;
            case R.id.ll_paper://论文
                FragmentActivity.actionActivity(getContext(), "my_paper");
                break;
            case R.id.ll_summary://综述
                FragmentActivity.actionActivity(getContext(), "my_review");
                break;
            case R.id.tv_follow_number:
            case R.id.tv_follow:
                FragmentActivity.actionActivity(getContext(), "mine_follow", uid + "");
                break;
            case R.id.tv_fans_number:
            case R.id.tv_fans:
                FragmentActivity.actionActivity(getContext(), "mine_scholar", uid + "");
                break;
            case R.id.fl_mine_qa://问答
                FragmentActivity.actionActivity(getContext(), "qa");
                break;
            case R.id.fl_mine_download:
                FragmentActivity.actionActivity(getContext(), "download_activity");
                break;
            case R.id.fl_mine_subscribe://我的关注
                AttentionActivity.actionActivity(getContext(), "mine");
                break;
            case R.id.fl_mine_news://我的消息
                FragmentActivity.actionActivity(getContext(), "news");
                break;
            case R.id.fl_mine_contacts:
                break;
            case R.id.fl_mine_collection:
                GiiispActivity giiispActivity = getGiiispActivity();
                if (giiispActivity != null) {
                    giiispActivity.getViewPagerGiiisp().setCurrentItem(3);
                }
                break;
            case R.id.ll_meeting://会议
                ToastUtils.showShort("这里是会议");
                break;
            case R.id.ll_collection://收藏
                FragmentActivity.actionActivity(getContext(), "collection_fragment");
                break;
            case R.id.ll_course://教程列表
                FragmentActivity.actionActivity(getContext(), "course");
                break;
            case R.id.fl_mine_setting:
//                SettingActivity.actionActivity(getContext());
                break;
            case R.id.tv_verified://身份认证
                switch (isVip) {
                    case "0":
                        VerifiedActivity.actionActivity(context);
                        break;
                    case "1":
                        VerifiedActivity.actionActivity(context);
                        break;
                    case "2":
                        VerifiedActivity.actionActivity(context);
                        break;
                    case "3":
                        Utils.showToast(R.string.in_authentication);
                        break;
                }
                break;
            case R.id.ll_email://邮箱认证
                FragmentActivity.actionActivity(getContext(), "mailbox_authentication");
                break;
            case R.id.fl_auth://资料认证
                switch (emailauthen) {
                    case "3":
                        FragmentActivity.actionActivity(getContext(), "mailbox_authentication");
                        break;
                    case "2":
                        switch (isVip) {
                            case "0":
                                VerifiedActivity.actionActivity(context);
                                break;
                            case "1":
                                Utils.showToast(R.string.auth_pass);
                                break;
                            case "2":
                                Utils.showToast(R.string.auth_pass);
                                break;
                            case "3":
                                Utils.showToast(R.string.in_authentication);
                                break;
                        }
                        break;
                    case "1":
                        Utils.showToast(R.string.in_authentication);
                        break;
                    default:
                        //FragmentActivity.actionActivity(getContext(), "wait_dubbing");
                        break;
                }
                break;
        }

    }

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
                            tvMineDownload.setText("" + downloadNunber);
                        } else {
                            tvMineDownload.setText("");
                        }

                    }
                });

    }

    @Override
    public void onRefresh() {
        initNetwork();
        loadDownloadNunber();
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "306":
                swipeRefreshLayout.setRefreshing(false);
                MIneInfoBean bean = (MIneInfoBean) baseBean;
                initUser(bean);
                break;
            default:
                break;
        }
    }
}
