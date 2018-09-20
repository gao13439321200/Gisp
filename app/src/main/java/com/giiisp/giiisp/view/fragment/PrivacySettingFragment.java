package com.giiisp.giiisp.view.fragment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 隐私设置 (1.0不用)
 * Created by Thinkpad on 2017/5/31.
 */

public class PrivacySettingFragment extends BaseMvpFragment<BaseViewHolder, WholePresenter> {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_my_data)
    TextView tvMyData;
    @BindView(R.id.tv_my_emails)
    TextView tvMyEmails;
    @BindView(R.id.tv_my_phone)
    TextView tvMyPhone;

    private ListPopupWindow emailPop;
    private ListPopupWindow phonePop;
    private List<String> emails = new ArrayList<>();
    private List<String> phones = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_privacysettings;
    }

    @Override
    public void initView() {
        tvTitle.setText(R.string.privacy_settings);

        emails.clear();
        emails.add("对所有人公开");
        emails.add("对认证用户公开");
        emails.add("不公开");
        phones.clear();
        phones.add("对认证用户公开");
        phones.add("不公开");
        String email = SPUtils.getInstance().getString(UrlConstants.EMAILPER);
        switch (email) {
            case "1":
                tvMyEmails.setText("对所有人公开");
                break;
            case "2":
                tvMyEmails.setText("对认证用户公开");
                break;
            case "3":
                tvMyEmails.setText("不公开");
                break;
            default:
                tvMyEmails.setText("");
                break;
        }
        String phone = SPUtils.getInstance().getString(UrlConstants.PHONEPER);
        switch (phone) {
            case "1":
                tvMyPhone.setText("对认证用户公开");
                break;
            case "2":
                tvMyPhone.setText("不公开");
                break;
            default:
                tvMyPhone.setText("");
                break;
        }

        emailPop = new ListPopupWindow(getContext());
        ArrayAdapter emailAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, emails);
        emailPop.setAdapter(emailAdapter);
        emailPop.setAnchorView(tvMyEmails);
        emailPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        emailPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        emailPop.setModal(true);
        emailPop.setOnItemClickListener((parent, view, position, id) -> {
            tvMyEmails.setText(emails.get(position));
            HashMap<String, Object> map = new HashMap<>();
            map.put("uid", getUserID());
            map.put("emailper", position + 1);
            presenter.getDataAll("332", map);
            SPUtils.getInstance().put(UrlConstants.EMAILPER, (position + 1) + "");
            emailPop.dismiss();
        });

        phonePop = new ListPopupWindow(getContext());
        ArrayAdapter phoneAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, phones);
        phonePop.setAdapter(phoneAdapter);
        phonePop.setAnchorView(tvMyPhone);
        phonePop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        phonePop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        phonePop.setModal(true);
        phonePop.setOnItemClickListener((parent, view, position, id) -> {
            tvMyPhone.setText(phones.get(position));
            HashMap<String, Object> map = new HashMap<>();
            map.put("uid", getUserID());
            map.put("phoneper", position + 1);
            presenter.getDataAll("331", map);
            SPUtils.getInstance().put(UrlConstants.PHONEPER, (position + 1) + "");
            phonePop.dismiss();
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_back, R.id.fl_my_data, R.id.fl_my_emails, R.id.fl_my_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                getSettingActivity().getVpLogin().setCurrentItem(0);
                pop();
                break;
            case R.id.fl_my_data:
                getSettingActivity().getVpLogin().setCurrentItem(10);
                break;
            case R.id.fl_my_emails:
                emailPop.show();
                break;
            case R.id.fl_my_phone:
                phonePop.show();
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
}
