package com.giiisp.giiisp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.HeBean;
import com.giiisp.giiisp.dto.HeEduListVO;
import com.giiisp.giiisp.dto.HePaperTitleVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.model.GlideApp;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 学者详情页
 * Created by Thinkpad on 2017/6/5.
 */

public class HeActivity extends BaseMvpActivity<BaseImpl, WholePresenter> {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_attention)
    ImageView ivAttention;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.tv_user_position)
    TextView tvUserPosition;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.tv_paper)
    TextView tvPaper;
    @BindView(R.id.tv_paper_number)
    TextView tvPaperNumber;
    @BindView(R.id.tv_review)
    TextView tvReview;
    @BindView(R.id.tv_review_number)
    TextView tvReviewNumber;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_fans_number)
    TextView tvFansNumber;
    @BindView(R.id.tv_authentication)
    TextView tvAuthentication;
    @BindView(R.id.tv_institutions)
    TextView tvInstitutions;
    @BindView(R.id.tv_institutions_info)
    TextView tvInstitutionsInfo;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_department_info)
    TextView tvDepartmentInfo;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_position_info)
    TextView tvPositionInfo;
    @BindView(R.id.tv_research_field)
    TextView tvResearchField;
    @BindView(R.id.tv_research_info)
    TextView tvResearchInfo;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    @BindView(R.id.recyclerView_introduction)
    RecyclerView recyclerViewIntroduction;
    @BindView(R.id.recyclerView_paper_list)
    RecyclerView recyclerViewPaperList;
    @BindView(R.id.recyclerView_review_list)
    RecyclerView recyclerViewReviewList;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    public static void newInstance(Context context, String scholarId) {
        Bundle bundle = new Bundle();
        bundle.putString("scholarId", scholarId);
        Intent intent = new Intent(context, HeActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_he;
    }

    @Override
    public void initView() {
        tvTitle.setText("学者详情页");
//        scrollView.fullScroll(ScrollView.FOCUS_UP);
        recyclerViewIntroduction.setNestedScrollingEnabled(false);
        recyclerViewReviewList.setNestedScrollingEnabled(false);
        recyclerViewPaperList.setNestedScrollingEnabled(false);
        recyclerViewIntroduction.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPaperList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviewList.setLayoutManager(new LinearLayoutManager(this));

        String suid = getIntent().getExtras().getString("scholarId");

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        map.put("suid", suid);
        presenter.getDataAll("222", map);

    }


    @OnClick({R.id.tv_back, R.id.tv_paper_number, R.id.tv_review_number, R.id.tv_fans_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_paper_number:
                break;
            case R.id.tv_review_number:
                break;
            case R.id.tv_fans_number:
                break;
        }
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
            case "222":
                HeBean bean = (HeBean) baseBean;
                if (bean == null)
                    return;
                GlideApp.with(this)
                        .load(bean.getUserinfo().getAvatar())
                        .into(ivUserIcon);
                tvUserName.setText(bean.getUserinfo().getName());
                ivAttention.setVisibility("1".equals(bean.getIsfollow()) ? View.INVISIBLE : View.VISIBLE);
                tvPrompt.setText("这个字段后台没有返回");
                tvUserPosition.setText(bean.getUserinfo().getPosition());
                tvUserPhone.setText(bean.getUserinfo().getPhone());
                tvUserEmail.setText(bean.getUserinfo().getEmail());
                tvPaperNumber.setText(bean.getUserinfo().getPapernum() + "");
                tvReviewNumber.setText(bean.getUserinfo().getSummarizenum() + "");
                tvFansNumber.setText(bean.getUserinfo().getFansnum() + "");
                tvInstitutionsInfo.setText(isChinese() ? bean.getUserinfo().getOcname() : bean.getUserinfo().getOename());
                tvDepartmentInfo.setText(bean.getUserinfo().getDepartment());
                tvPositionInfo.setText(bean.getUserinfo().getPosition());
                tvResearchInfo.setText(isChinese() ? bean.getUserinfo().getMajor() : bean.getUserinfo().getEmajor());

                List<ClickEntity> eduList = new ArrayList<>();
                for (HeEduListVO vo : bean.getEdulist()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setHeEduListVO(vo);
                    eduList.add(entity);
                }
                recyclerViewIntroduction.setAdapter(new ItemClickAdapter(this, R.layout.item_editinfo_education, eduList, "Introduction"));

                List<ClickEntity> paperList = new ArrayList<>();
                for (HePaperTitleVO vo : bean.getPapertitle()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setHePaperTitleVO(vo);
                    paperList.add(entity);
                }
                recyclerViewPaperList.setAdapter(new ItemClickAdapter(this, R.layout.item_paper_indexes, paperList, "PaperList"));

                List<ClickEntity> spaperList = new ArrayList<>();
                for (HePaperTitleVO vo : bean.getSpapertitle()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setHePaperTitleVO(vo);
                    spaperList.add(entity);
                }
                recyclerViewReviewList.setAdapter(new ItemClickAdapter(this, R.layout.item_paper_indexes, spaperList, "ReviewList"));


                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String msg, Exception ex) {

    }
}
