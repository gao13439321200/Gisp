package com.giiisp.giiisp.view.fragment;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.PhoneEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.CountDownTimerUtils;
import com.giiisp.giiisp.utils.KeyBoardUtils;
import com.giiisp.giiisp.utils.Log;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册页面
 * Created by Android on 2016/11/29.
 */


public class RegisterFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements BaseImpl {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_enter_name)
    EditText edEnterName;
    @BindView(R.id.ed_enter_phone)
    EditText edEnterPhone;
    @BindView(R.id.ed_enter_code)
    EditText edEnterCode;
    @BindView(R.id.ed_enter_password)
    EditText edEnterPassword;
    @BindView(R.id.ed_second_password)
    EditText edSecondPassword;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    private String phone;
    int type = 0;
    int registerType = 1;//1是手机号注册 2是邮箱注册

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_register;
    }

    @Override
    public void initView() {
        tvTitle.setText(R.string.login);
    }


    @Override
    public void onSuccess(BaseEntity entity) {
        Log.i("--->>", entity.toString());
        if (context == null) return;
        if (entity != null && entity.getResult() == 1) {
            if (entity instanceof PhoneEntity) {
                if (((PhoneEntity) entity).getIsMobileExist() == 0) {
                    type = 1;
                    presenter.getSendCodeData(phone, "1");
                } else {
                    Utils.showToast(R.string.phone_existing);
                }
            } else if (type == 1) {
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvVerificationCode, 60000, 1000);
                mCountDownTimerUtils.start();
                Utils.showToast(entity.getInfo());
            } else if (type == 2) {
                getLogInActivity().getVpLogin().setCurrentItem(1);
                Utils.showToast(entity.getInfo());
            }
        } else if (entity != null) {
            Utils.showToast(entity.getInfo());
        }
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @OnClick({R.id.tv_register, R.id.tv_back,
            R.id.tv_verification_code, R.id.tv_user_agreement
            , R.id.rb_phone, R.id.rb_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                KeyBoardUtils.closeKeybord(edEnterName, getContext());
                KeyBoardUtils.closeKeybord(edEnterCode, getContext());
                KeyBoardUtils.closeKeybord(edEnterPhone, getContext());
                KeyBoardUtils.closeKeybord(edEnterPassword, getContext());
                KeyBoardUtils.closeKeybord(edSecondPassword, getContext());
                getLogInActivity().getVpLogin().setCurrentItem(getLogInActivity().getFrom());
                break;
            case R.id.tv_register:
                if (checkInfo()) {
                    ArrayMap<String, Object> map = new ArrayMap<>();
                    map.put("mobile", ToolString.getString(edEnterPhone));
                    map.put("realName", ToolString.getString(edEnterName));
                    map.put("pwd", ToolString.getString(edEnterPassword));
                    map.put("code", ToolString.getString(edEnterCode));
                    type = 2;
                    presenter.getEnrollData(map);
                }
                break;
            case R.id.tv_user_agreement:
                getLogInActivity().setAgreement(2);
                getLogInActivity().getVpLogin().setCurrentItem(4);
                break;
            case R.id.tv_verification_code:
                phone = ToolString.getString(edEnterPhone);
                if (checkInfoCode()) {
                    presenter.getPhoneData(phone);
                }
                break;
            case R.id.rb_phone://手机号
                registerType = 1;
                edEnterPhone.setHint("手机号码");
                edEnterCode.setHint("请输入验证码");
                break;
            case R.id.rb_email://邮箱
                registerType = 2;
                edEnterPhone.setHint("邮箱");
                edEnterCode.setHint("邮箱验证码");
                break;
        }
    }

    private boolean checkInfo() {
        String pwd = ToolString.getString(edEnterPassword);
        String code = ToolString.getString(edEnterCode);
        String secondPwd = ToolString.getString(edSecondPassword);
        String name = ToolString.getString(edEnterName);
        String mobile = ToolString.getString(edEnterPhone);
        if (registerType == 1 && TextUtils.isEmpty(phone)) {
            Utils.showToast(R.string.input_phone);
            return false;
        }
        if (registerType == 1 && !Utils.checkMobileNumber(phone)) {
            Utils.showToast(R.string.format_not_correct);
            return false;
        }
        if (registerType == 2 && TextUtils.isEmpty(phone)) {
            Utils.showToast("请输入邮箱账号");
            return false;
        }
        if (registerType == 2 && !Utils.checkEmail(phone)) {
            Utils.showToast("邮箱格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Utils.showToast(R.string.name_cannot_empty);
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            Utils.showToast(R.string.verification_code_empty);
            return false;
        }
        if (!Objects.equals(mobile, phone)) {
            if (registerType == 1)
                Utils.showToast(R.string.enter_the_phone);
            if (registerType == 2)
                Utils.showToast("输入邮箱地址不一致");
            return false;
        }
        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(secondPwd)) {
            Utils.showToast(R.string.password_cannot_empty);
            return false;
        }
        if (!pwd.equals(secondPwd)) {
            Utils.showToast(R.string.passwords_match);
            return false;
        }
        if (pwd.length() > 8 | pwd.length() < 6) {
            Utils.showToast(R.string.password_can_only);
            return false;
        }
        return true;
    }

    private boolean checkInfoCode() {
        if (registerType == 1 && TextUtils.isEmpty(phone)) {
            Utils.showToast(R.string.input_phone);
            return false;
        }
        if (registerType == 1 && !Utils.checkMobileNumber(phone)) {
            Utils.showToast(R.string.format_not_correct);
            return false;
        }
        if (registerType == 2 && TextUtils.isEmpty(phone)) {
            Utils.showToast("请输入邮箱账号");
            return false;
        }
        if (registerType == 2 && !Utils.checkEmail(phone)) {
            Utils.showToast("邮箱格式不正确");
            return false;
        }
        return true;
    }

}
