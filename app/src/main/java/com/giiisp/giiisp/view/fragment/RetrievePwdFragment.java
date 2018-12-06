package com.giiisp.giiisp.view.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.CountDownTimerUtils;
import com.giiisp.giiisp.utils.KeyBoardUtils;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码界面
 * Created by Android on 2017/5/3.
 */


public class RetrievePwdFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_enter_phones)
    EditText edEnterPhones;
    @BindView(R.id.ed_enter_code)
    EditText edEnterCode;
    @BindView(R.id.ed_enter_password)
    EditText edEnterPassword;
    @BindView(R.id.ed_second_password)
    EditText edSecondPassword;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    private String phone;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_retrievepassword;
    }

    @Override
    public void initView() {
        tvTitle.setText(R.string.retrieve_password);
    }


    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @OnClick({R.id.tv_register, R.id.tv_back, R.id.tv_verification_code, R.id.ed_enter_phones})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ed_enter_phones:
                KeyBoardUtils.openKeybord(edEnterPhones, getContext());
                break;
            case R.id.tv_back:
                getLogInActivity().getVpLogin().setCurrentItem(1);
                break;
            case R.id.tv_register:
                String pwd = edEnterPassword.getText().toString();
                String code = edEnterCode.getText().toString();
                String secondPwd = edSecondPassword.getText().toString();
                String mobile = edEnterPhones.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    Utils.showToast(R.string.verification_code_empty);
                    break;
                }
                if (!Objects.equals(mobile, phone)) {
                    Utils.showToast(R.string.enter_the_phone_email);
                    break;
                }
                if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(secondPwd)) {
                    Utils.showToast(R.string.password_cannot_empty);
                    break;
                } else if (!pwd.equals(secondPwd)) {
                    Utils.showToast(R.string.passwords_match);
                    break;
                } else if (pwd.length() > 50 | pwd.length() < 6) {
                    Utils.showToast(R.string.password_can_only);
                    break;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("loginname", mobile);
                map.put("password", pwd);
                map.put("code", code);
                presenter.getDataAll("107", map);//Retrieve
                break;

            case R.id.tv_verification_code:
                phone = edEnterPhones.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    if (!phone.contains("@") && !Utils.checkMobileNumber(phone)) {
                        Utils.showToast(R.string.format_not_correct);
                        return;
                    }
                    if (phone.contains("@") && !Utils.checkEmail(phone)) {
                        Utils.showToast(R.string.email_not_correct);
                        return;
                    }
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("loginname", phone);
                    map1.put("sendtype", phone.contains("@") ? 2 : 1);
                    map1.put("type", 3);
                    presenter.getDataAll("101", map1);

                } else {
                    Utils.showToast(R.string.input_phone_email);
                }
                break;
        }
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "101":
                ToastUtils.showShort("验证码发送成功！");
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvVerificationCode, 60000, 1000);
                mCountDownTimerUtils.start();
                break;
            case "107":
                ToastUtils.showShort("密码重置成功！");
                getLogInActivity().getVpLogin().setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}
