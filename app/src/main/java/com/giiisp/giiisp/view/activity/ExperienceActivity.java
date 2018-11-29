package com.giiisp.giiisp.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseMvpActivity;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.CountryListBean;
import com.giiisp.giiisp.dto.CountryVO;
import com.giiisp.giiisp.dto.EditInfoVo;
import com.giiisp.giiisp.dto.SchoolListBean;
import com.giiisp.giiisp.dto.SchoolVO;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ToolString;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.fragment.EditInfoFragment;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.giiisp.giiisp.widget.ProgressPopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.tv_user_country)
    TextView tvUserCountry;
    @BindView(R.id.tv_user_degree)
    TextView tvUserDagree;
    @BindView(R.id.tv_user_starttime)
    TextView tvUserStarttime;
    @BindView(R.id.tv_user_endtime)
    TextView tvUserEndtime;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    int pType;
    ProgressPopupWindow progressPopupWindow;
    EditInfoVo introductionBean;
    private String cId = "";
    private String nId = "";
    private String mId = "";
    private String eId = "";
    private String degree = "";
    private ListPopupWindow namePop;
    private ListPopupWindow majorPop;
    private ListPopupWindow eduPop;
    private ListPopupWindow countryPop;
    private List<String> names = new ArrayList<>();
    private List<String> majors = new ArrayList<>();
    private List<String> edus = new ArrayList<>();
    private List<String> countrys = new ArrayList<>();
    private ArrayAdapter nameAdapter;
    private ArrayAdapter majorAdapter;
    private ArrayAdapter eduAdapter;
    private ArrayAdapter countryAdapter;
    private SchoolListBean nameListBean;
    private SchoolListBean majorListBean;
    private SchoolListBean eduListBean;
    private CountryListBean countryListBean;
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
                    break;
                case "edit":
                    introductionBean = (EditInfoVo) getIntent().getSerializableExtra("introductionBean");
                    tvUserSchool.setText(TextUtils.isEmpty(introductionBean.getUcname()) ? "" : introductionBean.getUcname());
                    tvUserMajor.setText(TextUtils.isEmpty(introductionBean.getMcname()) ? "" : introductionBean.getMcname());
                    tvUserEdubackground.setText(introductionBean.getEcname());
                    tvUserDagree.setText(introductionBean.getEcname());
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

        countryPop = new ListPopupWindow(this);
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countrys);
        countryPop.setAdapter(countryAdapter);
        countryPop.setAnchorView(tvUserCountry);
        countryPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        countryPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        countryPop.setModal(true);
        countryPop.setOnItemClickListener((parent, view, position, id) -> {
            cId = countryListBean.getList().get(position).getId();
            if (!namePop.isShowing())
                namePop.show();
            getSchoolList();
        });

        namePop = new ListPopupWindow(this);
        nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        namePop.setAdapter(nameAdapter);
        namePop.setAnchorView(tvUserSchool);
        namePop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        namePop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        namePop.setModal(true);
        namePop.setOnItemClickListener((parent, view, position, id) -> {
            if (!ObjectUtils.equals(nId, nameListBean.getList().get(position).getId())) {
                tvUserSchool.setText(names.get(position));
                nId = nameListBean.getList().get(position).getId();
                mId = "";
                eId = "";
                tvUserMajor.setText("");
                tvUserEdubackground.setText("");
                getMajorList();
                getEduList();
            }
            namePop.dismiss();
            countryPop.dismiss();
        });


        majorPop = new ListPopupWindow(this);
        majorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, majors);
        majorPop.setAdapter(majorAdapter);
        majorPop.setAnchorView(tvUserMajor);
        majorPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        majorPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        majorPop.setModal(true);
        majorPop.setOnItemClickListener((parent, view, position, id) -> {
            tvUserMajor.setText(majors.get(position));
            mId = majorListBean.getList().get(position).getId();
            majorPop.dismiss();
        });


        eduPop = new ListPopupWindow(this);
        eduAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, edus);
        eduPop.setAdapter(eduAdapter);
        eduPop.setAnchorView(tvUserEdubackground);
        eduPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        eduPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        eduPop.setModal(true);
        eduPop.setOnItemClickListener((parent, view, position, id) -> {
            tvUserEdubackground.setText(edus.get(position));
            eId = eduListBean.getList().get(position).getId();
            eduPop.dismiss();
        });
        switch (type) {
            case "add":
                getCountryList();
                break;
            case "edit":
                getCountryList();
//                getSchoolList();
                getMajorList();
                getEduList();
                break;
        }

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
                countryPop.show();
//                namePop.show();
                break;
            case R.id.fl_user_major:
                majorPop.show();
                break;
            case R.id.fl_user_edubackground:
                eduPop.show();
                break;
            case R.id.fl_user_degree:
                inputTitleDialog(tvUserDagree, getString(R.string.degree));
                break;
            case R.id.fl_user_starttime:
                showDate(tvUserStarttime, getString(R.string.starttime));
                break;
            case R.id.fl_user_endtime:
                showDate(tvUserEndtime, getString(R.string.endtime));
                break;
            case R.id.tv_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.confirm_delete)).setIcon(
                        R.mipmap.ic_launcher).setNegativeButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> dmap = new HashMap<>();
                        dmap.put("rid", rid);
                        dmap.put("uid", getUserID());
                        progressPopupWindow.showPopupWindow();
                        presenter.getDataAll("329", dmap);
                    }
                });
                builder.setPositiveButton(getString(R.string.cancel),
                        (dialog, which) -> {
                        });
                builder.show();
                break;
            case R.id.tv_right:
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
                if (TextUtils.isEmpty(degree)) {
                    Utils.showToast(R.string.degree_verified);
                    return;
                } else {
                    map.put("degree", degree);
                }
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
                if (presenter != null) {
                    progressPopupWindow.showPopupWindow();
                    if (type.equals("add")) {
                        presenter.getDataAll("327", map);
                    } else if (type.equals("edit")) {
                        map.put("rid", rid);
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
                (dialog, which) -> {
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
                            degree = inputName;
                            break;
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
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText(getString(R.string.cancel))//取消按钮文字
                .setSubmitText(getString(R.string.confirm))//确认按钮文字
                .setTitleText(name)//标题文字
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
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
                countryListBean = (CountryListBean) baseBean;
                for (CountryVO vo : countryListBean.getList()) {
                    countrys.add(isChinese() ? vo.getCname() : vo.getEname());
                }
                countryAdapter.notifyDataSetChanged();
//                cId = countryListBean.getList().get(0).getId();
//                getSchoolList();
                break;
            case "323"://学校
                names.clear();
                nameListBean = (SchoolListBean) baseBean;
                for (SchoolVO vo : nameListBean.getList()) {
                    names.add(isChinese() ? vo.getCname() : vo.getEname());
                }
                nameAdapter.notifyDataSetChanged();
                break;
            case "324"://专业
                majors.clear();
                majorListBean = (SchoolListBean) baseBean;
                for (SchoolVO vo : majorListBean.getList()) {
                    majors.add(isChinese() ? vo.getCname() : vo.getEname());
                }
                majorAdapter.notifyDataSetChanged();
                break;
            case "325"://学历
                edus.clear();
                eduListBean = (SchoolListBean) baseBean;
                for (SchoolVO vo : eduListBean.getList()) {
                    edus.add(isChinese() ? vo.getCname() : vo.getEname());
                }
                eduAdapter.notifyDataSetChanged();
                break;
            case "327":
            case "328":
                EditInfoFragment.newRxBus();
                ToastUtils.showShort("保存成功！");
                finish();
                break;
            case "329":
                EditInfoFragment.newRxBus();
                ToastUtils.showShort("删除成功！");
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailNew(String url, String msg) {
        super.onFailNew(url, msg);
        switch (url) {
            case "327":
            case "328":
            case "329":
                progressPopupWindow.dismiss();
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

    public boolean isChinese() {
        return Locale.getDefault().getLanguage().endsWith("zh");
    }
}
