package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.CountryListBean;
import com.giiisp.giiisp.dto.EditInfoVo;
import com.giiisp.giiisp.dto.SchoolListBean;
import com.giiisp.giiisp.dto.SchoolVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.giiisp.giiisp.widget.ProgressPopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的教育经历页面
 * Created by Thinkpad on 2017/6/1.
 */

public class ExperienceActivity extends BaseMvpActivity<BaseImpl, WholePresenter> implements BaseImpl {
    public static int EDU_SUCCESS = 203;
    public static int EDU_BACK = 202;

    String type;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_user_school)
    TextView tvUserSchool;
    @BindView(R.id.tv_user_major)
    TextView tvUserMajor;
    @BindView(R.id.tv_user_edubackground)
    TextView tvUserEdubackground;
    //    @BindView(R.id.tv_user_degree)
//    TextView tvUserDagree;
    @BindView(R.id.tv_user_starttime)
    TextView tvUserStarttime;
    @BindView(R.id.tv_user_endtime)
    TextView tvUserEndtime;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.spin_name)
    Spinner mNameSpin;
    @BindView(R.id.spin_major)
    Spinner mMajorSpin;
    @BindView(R.id.spin_edu)
    Spinner mEduSpin;
    int pType;
    ProgressPopupWindow progressPopupWindow;
    EditInfoVo introductionBean;
    SimpleAdapter nameAdapter;
    SimpleAdapter majorAdapter;
    SimpleAdapter eduAdapter;
    List<Map<String, String>> nameList = new ArrayList<>();
    List<Map<String, String>> majorList = new ArrayList<>();
    List<Map<String, String>> eduList = new ArrayList<>();
    private String cId = "";
    private String nId = "";
    private String mId = "";
    private String eId = "";


    String rid = "";

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_experience;
    }

    public static void actionActivity(Activity context, String type, EditInfoVo vo) {
        Intent sIntent = new Intent(context, ExperienceActivity.class);
        sIntent.putExtra("type", type);
        sIntent.putExtra("introductionBean", vo);
//        sIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(sIntent, 1002);
    }

    @Override
    public void initData() {
        super.initData();
        type = getIntent().getStringExtra("type");
        progressPopupWindow = new ProgressPopupWindow(this);
    }

    @Override
    public void initView() {

        tvTitle.setText(R.string.my_education_experience);
        tvRight.setText(R.string.save);
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case "add":
                    tvDelete.setVisibility(View.GONE);
                    getCountryList();
                    break;
                case "edit":
                    introductionBean = (EditInfoVo) getIntent().getSerializableExtra("introductionBean");
                    tvUserSchool.setText(TextUtils.isEmpty(introductionBean.getUcname()) ? "" : introductionBean.getUcname());
                    tvUserMajor.setText(TextUtils.isEmpty(introductionBean.getMcname()) ? "" : introductionBean.getMcname());
                    tvUserEdubackground.setText(introductionBean.getEcname());
//                    tvUserDagree.setText(introductionBean.getEcname());
                    tvUserStarttime.setText(introductionBean.getTimestart());
                    tvUserEndtime.setText(introductionBean.getTimeend());
                    rid = introductionBean.getId();
                    cId = introductionBean.getCid();
                    nId = introductionBean.getUnid();
                    mId = introductionBean.getUmid();
                    eId = introductionBean.getEid();
                    break;
            }
        }

        nameAdapter = new SimpleAdapter(this, nameList, R.layout.item_spinner, new String[]{"text", "id"}, new int[]{R.id.tv_name});
        mNameSpin.setAdapter(nameAdapter);
        mNameSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nId = nameList.get(i).get("id");
                getMajorList();
                getEduList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        majorAdapter = new SimpleAdapter(this, majorList, R.layout.item_spinner, new String[]{"text", "id"}, new int[]{R.id.tv_name});
        mMajorSpin.setAdapter(majorAdapter);
        mMajorSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mId = majorList.get(i).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        eduAdapter = new SimpleAdapter(this, eduList, R.layout.item_spinner, new String[]{"text", "id"}, new int[]{R.id.tv_name});
        mEduSpin.setAdapter(eduAdapter);
        mEduSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eId = eduList.get(i).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(ExperienceActivity.EDU_BACK, getIntent());
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.tv_back, R.id.fl_user_school, R.id.fl_user_major, R.id.fl_user_edubackground, R.id.fl_user_degree, R.id.fl_user_starttime, R.id.fl_user_endtime, R.id.tv_delete, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                setResult(ExperienceActivity.EDU_BACK);
                finish();
                break;
            case R.id.fl_user_school:
//                inputTitleDialog(tvUserSchool, getString(R.string.school));
                break;
            case R.id.fl_user_major:
//                inputTitleDialog(tvUserMajor, getString(R.string.major));
                break;
            case R.id.fl_user_edubackground:
//                inputTitleDialog(tvUserEdubackground, getString(R.string.edubackground));
                break;
            case R.id.fl_user_degree:
//                inputTitleDialog(tvUserDagree, getString(R.string.degree));
                break;
            case R.id.fl_user_starttime:
                showDate(tvUserStarttime, getString(R.string.starttime));
                break;
            case R.id.fl_user_endtime:
                showDate(tvUserEndtime, getString(R.string.endtime));
                break;
            case R.id.tv_delete:
                HashMap<String, Object> dmap = new HashMap<>();
                dmap.put("rid", rid);
                progressPopupWindow.showPopupWindow();
                presenter.getDataAll("329", dmap);
                break;
            case R.id.tv_right:
//                String school = tvUserSchool.getText().toString();
//                String major = tvUserMajor.getText().toString();
//                String edubackground = tvUserEdubackground.getText().toString();
////                String degree = tvUserDagree.getText().toString();
//                String timeStart = tvUserStarttime.getText().toString();
//                String timeEnd = tvUserEndtime.getText().toString();

                HashMap<String, Object> map = new HashMap<>();
                map.put("uid", uid);
                if (!TextUtils.isEmpty(nId)) {
                    map.put("unid", nId);
                }
                if (!TextUtils.isEmpty(mId)) {
                    map.put("umid", mId);
                }
                if (TextUtils.isEmpty(eId)) {
                    Utils.showToast(R.string.edubackground_verified);
                } else {
                    map.put("eid", eId);
                }
//                if (TextUtils.isEmpty(degree)) {
//                    Utils.showToast(R.string.degree_verified);
//                    return;
//                } else {
//                    map.put("degree", degree);
//                }
                if (TextUtils.isEmpty(ToolString.getString(tvUserStarttime))) {
                    Utils.showToast(R.string.time_start_verified);
                    return;
                } else {
                    map.put("timestart", ToolString.getString(tvUserStarttime));
                }
                if (TextUtils.isEmpty(ToolString.getString(tvUserEndtime))) {
                    Utils.showToast(R.string.time_end_verified);
                    return;
                } else {
                    map.put("timeend", ToolString.getString(tvUserEndtime));
                }
                map.put("rid", rid);
                if (presenter != null) {
                    progressPopupWindow.showPopupWindow();
                    if (type.equals("add")) {
                        presenter.getDataAll("327", map);
                    } else if (type.equals("edit")) {
                        presenter.getDataAll("328", map);
                    }
                }

                break;
        }
    }

    private void inputTitleDialog(final TextView view, final String name) {

        final EditText inputServer = new EditText(this);
        inputServer.setPadding(50, 50, 50, 50);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.input) + name).setIcon(
                R.mipmap.ic_launcher).setView(inputServer).setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        switch (name) {
                            case "学校":
                                view.setText(inputName);
                                break;
                            case "专业":
                                view.setText(inputName);
                                break;
                            case "学历":
                                view.setText(inputName);
                                break;
                            case "学位":
                                view.setText(inputName);
                                break;
                        }


                    }
                });
        builder.show();
    }


    private void showDate(final TextView view, final String name) {
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                view.setText(getTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText(getString(R.string.cancel))//取消按钮文字
                .setSubmitText(getString(R.string.confirm))//确认按钮文字
                .setTitleText(name)//标题文字
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        if (progressPopupWindow != null) {
            progressPopupWindow.dismiss();
        }
        Utils.showToast(entity.getInfo());
        if (pType == 1) {
            if (entity.getResult() == 1) {
                setResult(ExperienceActivity.EDU_SUCCESS);
                finish();
            }
        }
        if (pType == 2) {
            if (entity.getResult() == 1) {
                setResult(ExperienceActivity.EDU_SUCCESS);
                finish();
            }
        }
        if (pType == 3) {
            if (entity.getResult() == 1) {
                setResult(ExperienceActivity.EDU_SUCCESS);
                finish();
            }
        }
    }

    @Override
    public void onFailure(String msg, Exception ex) {

    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }


    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "322":
                CountryListBean bean = (CountryListBean) baseBean;

                HashMap<String, Object> map = new HashMap<>();
                map.put("id", bean.getList().get(0).getId());
                presenter.getDataAll("323", map);
                break;
            case "323"://学校
                SchoolListBean schoolListBean = (SchoolListBean) baseBean;
                for (SchoolVO vo : schoolListBean.getList()) {
                    Map<String, String> sMap = new HashMap<>();
                    sMap.put("text", vo.getCname());
                    sMap.put("id", vo.getId());
                    nameList.add(sMap);
                }
                nameAdapter.notifyDataSetChanged();
                break;
            case "324"://专业
                SchoolListBean majorBean = (SchoolListBean) baseBean;
                for (SchoolVO vo : majorBean.getList()) {
                    Map<String, String> sMap = new HashMap<>();
                    sMap.put("text", vo.getCname());
                    sMap.put("id", vo.getId());
                    majorList.add(sMap);
                }
                majorAdapter.notifyDataSetChanged();
                break;
            case "325"://学历
                SchoolListBean eduBean = (SchoolListBean) baseBean;
                for (SchoolVO vo : eduBean.getList()) {
                    Map<String, String> sMap = new HashMap<>();
                    sMap.put("text", vo.getCname());
                    sMap.put("id", vo.getId());
                    eduList.add(sMap);
                }
                eduAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    //国家
    private void getCountryList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        presenter.getDataAll("322", map);
    }

    //学校
    private void getSchoolList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cid", cId);
        presenter.getDataAll("323", map);
    }

    //专业
    private void getMajorList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("unid", nId);
        presenter.getDataAll("324", map);
    }

    //学历
    private void getEduList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("unid", nId);
        presenter.getDataAll("325", map);
    }
}
