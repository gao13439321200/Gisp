package com.giiisp.giiisp.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("Registered")
public class CreateGroupActivity extends BaseMvpActivity<BaseImpl, WholePresenter> implements BaseImpl {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_detail)
    EditText mEtDetail;

    public static void newInstance(Context context) {
        Bundle args = new Bundle();
        context.startActivity(new Intent(context, CreateGroupActivity.class));
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_group_layout;
    }

    @Override
    public void initView() {
        setTitle("创建团组");
    }

    @OnClick({R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.back:
//                finish();
//                break;
            case R.id.btn_create:
                if (ObjectUtils.isEmpty(ToolString.getString(mEtName))) {
                    ToastUtils.showShort("名称不能为空");
                    return;
                }
                if (ObjectUtils.isEmpty(ToolString.getString(mEtDetail))) {
                    ToastUtils.showShort("团组介绍不能为空");
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", ToolString.getString(mEtName));
                map.put("detail", ToolString.getString(mEtDetail));
                map.put("uid", getUserID());
                presenter.getDataAll("333", map);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseEntity) {
        super.onSuccessNew(url, baseEntity);
        switch (url) {
            case "333":
                ToastUtils.showShort("创建成功！");
                finish();
                break;
            default:
                break;
        }
    }
}
