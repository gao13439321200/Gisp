package com.giiisp.giiisp.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MailboxAuthResultFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public static MailboxAuthResultFragment newInstance() {
        Bundle args = new Bundle();
        MailboxAuthResultFragment fragment = new MailboxAuthResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_email_result_layout;
    }

    @Override
    public void initView() {
        tvTitle.setText(R.string.mailbox_authentication);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        getActivity().finish();
    }
}
