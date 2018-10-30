package com.giiisp.giiisp.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.base.BaseView;
import com.giiisp.giiisp.common.MyDialog;
import com.giiisp.giiisp.common.MyDialogOnClick;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.GroupInfoBean;
import com.giiisp.giiisp.dto.GroupMemberInfo;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.adapter.ClickEntity;
import com.giiisp.giiisp.view.adapter.ItemClickAdapter;
import com.giiisp.giiisp.view.impl.BaseImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupInfoActivity extends BaseMvpActivity<BaseView, WholePresenter> implements BaseImpl {
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_notice)
    TextView mTvNotice;
    @BindView(R.id.tv_owner)
    TextView mTvOwner;
    @BindView(R.id.rv_member)
    RecyclerView mRvMember;

    private String ownerId = "";
    private String gid = "";
    private List<ClickEntity> mEntityList = new ArrayList<>();
    private ItemClickAdapter mAdapter;

    public static void newInstance(Context context, String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        context.startActivity(new Intent(context, GroupInfoActivity.class).putExtras(args));
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
        return R.layout.activity_group_info_layout;
    }

    @Override
    public void initView() {
        tvTitle.setText("团组信息");
        mTvRight.setText("退出");

        mAdapter = new ItemClickAdapter(this, R.layout.item_member_info_layout, mEntityList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!ObjectUtils.equals(mEntityList.get(position).getGroupMemberInfo().getUserid(), getUserID())) {
                    inputTitleDialog(view.findViewById(R.id.tv_position), "职务",
                            mEntityList.get(position).getGroupMemberInfo().getUserid());
                } else {
                    ToastUtils.showShort("团长的职务不可修改");
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvMember.setLayoutManager(linearLayoutManager);
        mRvMember.setAdapter(mAdapter);


        gid = getIntent().getExtras().getString("id");
        HashMap<String, Object> map = new HashMap<>();
        map.put("gid", gid);
        presenter.getDataAll("338", map);
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "334":
                ToastUtils.showShort("删除成功！");
                finish();
                break;
            case "338":
                GroupInfoBean bean = (GroupInfoBean) baseBean;
                if (bean == null) {
                    ToastUtils.showShort("信息异常，请重试");
                    return;
                }
                ownerId = bean.getGroup().getUserid();
                mTvName.setText(bean.getGroup().getTitle());
                mTvNotice.setText(bean.getGroup().getDetail());
                mTvOwner.setText(bean.getGroup().getUsername());
                for (GroupMemberInfo info : bean.getGroupusers()) {
                    ClickEntity entity = new ClickEntity();
                    entity.setGroupMemberInfo(info);
                    mAdapter.addData(entity);
                }
                break;
            case "335":
            case "339":
                ToastUtils.showShort("修改成功！");
                break;
            case "336":
                ToastUtils.showShort("邀请信息已发送");
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("gid", gid);
//                presenter.getDataAll("338", map);
                break;
            case "340":
                ToastUtils.showShort("退出成功！");
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onFailNew(String url, String msg) {
        super.onFailNew(url, msg);
    }

    @OnClick({R.id.tv_back, R.id.tv_right, R.id.fl_name, R.id.fl_notice,
            R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_add:
                inputTitleDialog(mTvName, "被邀请人用户名", "");
                break;
            case R.id.tv_right:
                MyDialog dialog = new MyDialog(this, new MyDialogOnClick() {
                    @Override
                    public void onOKClick() {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("gid", gid);
                        map.put("uid", getUserID());
                        if (isOwner()) {
                            presenter.getDataAll("334", map);
                        } else {
                            presenter.getDataAll("340", map);
                        }

                    }

                    @Override
                    public void onCancelClick() {

                    }
                }, 2);
                dialog.setNameText(isOwner() ? "确认删除该团组吗？" : "确认退出该团组吗？");//内容
                dialog.setButtonOK("确定");//确定的按钮
                dialog.setButtonCancel("取消");//取消的按钮
                dialog.setCancelable(true);//点击外部是否可以取消
                dialog.show();
                break;
            case R.id.fl_name:
                inputTitleDialog(mTvName, "名称", "");
                break;
            case R.id.fl_notice:
                inputTitleDialog(mTvNotice, "公告", "");
                break;
        }
    }

    private void inputTitleDialog(final TextView view, final String name, final String id) {

        final EditText inputServer = new EditText(this);
        inputServer.setPadding(50, 50, 50, 50);
        inputServer.setFocusable(true);
        switch (name) {
            case "名称":
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                break;
            case "公告":
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
                break;
            case "职务":
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                break;
            case "被邀请人用户名":
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                break;
            default:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.input) + name).setIcon(
                R.mipmap.ic_launcher).setView(inputServer).setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        HashMap<String, Object> map = new HashMap<>();
                        switch (name) {
                            case "名称":
                                if (ObjectUtils.isNotEmpty(inputName)) {
                                    view.setText(inputName);
                                } else {
                                    Utils.showToast("请输入名称");
                                    break;
                                }
                                map.put("gid", gid);
                                map.put("title", inputName);
                                presenter.getDataAll("335", map);
                                break;
                            case "公告":
                                if (ObjectUtils.isNotEmpty(inputName)) {
                                    view.setText(inputName);
                                } else {
                                    Utils.showToast("请输入公告");
                                    break;
                                }
                                map.put("gid", gid);
                                map.put("detail", inputName);
                                presenter.getDataAll("335", map);
                                break;
                            case "职务":
                                if (ObjectUtils.isNotEmpty(inputName)) {
                                    view.setText(inputName);
                                } else {
                                    Utils.showToast("请输入职务");
                                    break;
                                }
                                map.put("gid", gid);
                                map.put("uid", id);
                                map.put("job", inputName);
                                presenter.getDataAll("339", map);
                                break;
                            case "被邀请人用户名":
                                if (ObjectUtils.isEmpty(inputName)) {
                                    Utils.showToast("请输入用户名");
                                    break;
                                }
                                map.put("gid", gid);
                                map.put("loginname", inputName);
                                presenter.getDataAll("336", map);
                                break;
                        }
                    }
                });
        builder.show();
    }

    private boolean isOwner() {
        return ownerId.equals(getUserID());
    }
}
