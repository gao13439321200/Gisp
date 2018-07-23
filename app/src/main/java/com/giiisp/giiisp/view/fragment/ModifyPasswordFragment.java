package com.giiisp.giiisp.view.fragment;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

//import static com.giiisp.giiisp.base.BaseActivity.token;

/**
 * 修改密码页面
 * Created by Thinkpad on 2017/5/4.
 */

public class ModifyPasswordFragment extends BaseMvpFragment<BaseImpl, WholePresenter> {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_current_password)
    EditText etCurrentPassword;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.switch_notice_news)
    Switch switchNoticeNews;
    @BindView(R.id.tv_info)
    TextView tvInfo;

//    @Override
//    public void onSuccess(BaseEntity entity) {
//        if (context == null)
//            return;
//        Utils.showToast(entity.getInfo());
//        if (entity.getResult() == 1) {
//            getSettingActivity().getVpLogin().setCurrentItem(2, false);
//            uid = entity.getUid();
//            SharedPreferencesHelper.getInstance(context).putStringValue("uid", entity.getUid());
//        }
//    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_changepwd;
    }

    @Override
    public void initView() {

        switchNoticeNews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etCurrentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //                    etConfirmPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etCurrentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etCurrentPassword.setSelection(etCurrentPassword.length());
                etNewPwd.setSelection(etNewPwd.length());
                etConfirmPwd.setSelection(etConfirmPwd.length());
            }
        });


        etConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = etNewPwd.getText() + "";
                String confirmPwd = etConfirmPwd.getText() + "";
                if (!confirmPwd.equals(pwd)) {
                    tvInfo.setText(R.string.enter_the_password);
                    tvInfo.setTextColor(Color.RED);
                } else {
                    tvInfo.setText(R.string.enter_password_consistent);
                    tvInfo.setTextColor(Color.BLACK);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_back, R.id.tv_confirm, R.id.tv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
            case R.id.tv_close:
                getSettingActivity().getVpLogin().setCurrentItem(2, false);
                break;
            case R.id.tv_confirm:
                String opwd = ToolString.getString(etCurrentPassword);
                String pwd = ToolString.getString(etNewPwd);
                String confirmPwd = ToolString.getString(etConfirmPwd);

                if (pwd.equals(confirmPwd)) {
                    if (pwd.length() > 8 | pwd.length() < 6) {
                        Utils.showToast(R.string.password_can_only);
                        break;
                    } else if (opwd.equals(pwd)) {
                        Utils.showToast(R.string.newpassword_oldpassword_same);
                    } else {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("loginname", SPUtils.getInstance().getString(UrlConstants.UNAME));
                        map.put("oldpwd", opwd);
                        map.put("newpwd", pwd);
                        presenter.getDataAll("106", map);
                    }
                } else {
                    Utils.showToast(R.string.passwords_match);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String url, BaseBean baseBean) {
        super.onSuccess(url, baseBean);
        switch(url){
            case "106":
                ToastUtils.showShort("密码修改成功！");
                getSettingActivity().getVpLogin().setCurrentItem(2, false);
                break;
            default:
                break;
        }
    }
}
