package com.giiisp.giiisp.view.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.LoginBean;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.CountDownTimerUtils;
import com.giiisp.giiisp.utils.KeyBoardUtils;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.WelcomeSelectActivity;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giiisp.giiisp.base.BaseActivity.uid;


/**
 * 注册页面
 * Created by Android on 2016/11/29.
 */


public class RegisterFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_enter_name)
    EditText edEnterName;
    @BindView(R.id.ed_enter_phone)
    EditText edEnterPhone;
    @BindView(R.id.ed_enter_email)
    EditText edEnterEmail;
    @BindView(R.id.ed_enter_code)
    EditText edEnterCode;
    @BindView(R.id.ed_enter_password)
    EditText edEnterPassword;
    @BindView(R.id.ed_second_password)
    EditText edSecondPassword;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    private String userName;
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
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("loginname", userName);
                    map.put("name", ToolString.getString(edEnterName));
                    map.put("password", ToolString.getString(edEnterPassword));
                    map.put("code", ToolString.getString(edEnterCode));
                    presenter.getDataAll("104", map);
                }
                break;
            case R.id.tv_user_agreement://用户协议
                getLogInActivity().setAgreement(2);
                getLogInActivity().getVpLogin().setCurrentItem(4);
                break;
            case R.id.tv_verification_code:
                if (registerType == 1)
                    userName = ToolString.getString(edEnterPhone);
                if (registerType == 2)
                    userName = ToolString.getString(edEnterEmail);
                if (checkInfoCode()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("loginname", userName);
                    map.put("sendtype", registerType);
                    map.put("type", 1);
                    presenter.getDataAll("101", map);
                }
                break;
            case R.id.rb_phone://手机号
                registerType = 1;
                edEnterPhone.setVisibility(View.VISIBLE);
                edEnterEmail.setVisibility(View.GONE);
                edEnterCode.setHint("请输入验证码");
                break;
            case R.id.rb_email://邮箱
                registerType = 2;
                edEnterEmail.setVisibility(View.VISIBLE);
                edEnterPhone.setVisibility(View.GONE);
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
        String email = ToolString.getString(edEnterEmail);
        if (registerType == 1 && TextUtils.isEmpty(mobile)) {
            Utils.showToast(R.string.input_phone);
            return false;
        }
        if (registerType == 1 && !Utils.checkMobileNumber(mobile)) {
            Utils.showToast(R.string.format_not_correct);
            return false;
        }
        if (registerType == 2 && TextUtils.isEmpty(email)) {
            Utils.showToast("请输入邮箱账号");
            return false;
        }
        if (registerType == 2 && !Utils.checkEmail(email)) {
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
        if (registerType == 1 && !Objects.equals(mobile, userName)) {
            Utils.showToast(R.string.enter_the_phone);
            return false;
        }
        if (registerType == 2 && !Objects.equals(email, userName)) {
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
        if (pwd.length() < 6) {
            Utils.showToast(R.string.password_can_only);
            return false;
        }
        return true;
    }

    private boolean checkInfoCode() {
        if (registerType == 1 && TextUtils.isEmpty(userName)) {
            Utils.showToast(R.string.input_phone);
            return false;
        }
        if (registerType == 1 && !Utils.checkMobileNumber(userName)) {
            Utils.showToast(R.string.format_not_correct);
            return false;
        }
        if (registerType == 2 && TextUtils.isEmpty(userName)) {
            Utils.showToast("请输入邮箱账号");
            return false;
        }
        if (registerType == 2 && !Utils.checkEmail(userName)) {
            Utils.showToast("邮箱格式不正确");
            return false;
        }
        return true;
    }

    @Override
    public void onSuccessNew(String url, BaseBean entity) {
        switch (url) {
            case "101":
                //发送验证码成功！
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvVerificationCode, 60000, 1000);
                mCountDownTimerUtils.start();
                ToastUtils.showShort("验证码发送成功！");
                break;
            case "104":
                LoginBean bean = (LoginBean) entity;
                //注册并登录成功！
                uid = bean.getId();
                SPUtils.getInstance().put(UrlConstants.UID, bean.getId());
                SPUtils.getInstance().put(UrlConstants.UNAME, userName);
                Utils.showToast("注册成功！");
                //选择领域
                WelcomeSelectActivity.intentActivity(getActivity());
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(BaseEntity entity) {

    }
}
