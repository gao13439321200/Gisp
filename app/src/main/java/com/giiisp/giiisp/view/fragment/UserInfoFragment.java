package com.giiisp.giiisp.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.base.BaseMvpFragment;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.CountryListBean;
import com.giiisp.giiisp.dto.CountryVO;
import com.giiisp.giiisp.dto.MIneInfoBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MajorVO;
import com.giiisp.giiisp.dto.ProfessionalListBean;
import com.giiisp.giiisp.dto.ProfessionalVO;
import com.giiisp.giiisp.dto.SubjectBean;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.presenter.WholePresenter;
import com.giiisp.giiisp.utils.ImageLoader;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.FragmentActivity;
import com.giiisp.giiisp.view.activity.SettingActivity;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.giiisp.giiisp.widget.ProgressPopupWindow;
import com.qiniu.android.storage.UploadManager;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.giiisp.giiisp.base.BaseActivity.uid;

/**
 * 用户的详细信息
 * Created by Thinkpad on 2017/5/4.
 */

public class UserInfoFragment extends BaseMvpFragment<BaseImpl, WholePresenter> implements BaseImpl, View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.tv_user_mechanism)
    TextView tvUserMechanism;
    @BindView(R.id.tv_user_position)
    TextView tvUserPosition;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.tv_user_jobtitle)
    TextView tvUserJobtitle;
    @BindView(R.id.tv_user_resume)
    TextView tvUserResume;
    @BindView(R.id.tv_contact_information)
    TextView tvContactInformation;
    @BindView(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @BindView(R.id.tv_user_professional)
    TextView tvUserProfessional;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_user_country)
    TextView tvUserCountry;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_web)
    TextView tvUserWeb;
    @BindView(R.id.fl_user_email)
    FrameLayout flUserEmail;

    @BindView(R.id.fl_user_phone)
    FrameLayout flUserPhone;
    //    Unbinder unbinder;
    private Dialog dialog;
    private File file;
    private Uri imageUri;
    private int type;
    private UploadManager uploadManager = new UploadManager();
    private String qNtoken;
    private String sex;
    private String changeSex;
    private String changeName;
    private String name;
    ProgressPopupWindow progressPopupWindow;
    private String imagUrl = "";
    private String PageType;
    private ListPopupWindow institutionPop;
    private ListPopupWindow majorPop;
    private ListPopupWindow countryPop;
    private List<String> institutions = new ArrayList<>();
    private List<String> majors = new ArrayList<>();
    private List<String> countrys = new ArrayList<>();
    private ArrayAdapter institutionAdapter;
    private ArrayAdapter majorAdapter;
    private ArrayAdapter countryAdapter;
    private String oid;//专业id
    private String mid;//机构id
    private String cid;//国家id
    private ProfessionalListBean listBean;
    private MajorBean majorBean;
    private CountryListBean countryListBean;

    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString("1005", param1);
        args.putString("1006", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSuccess(BaseEntity entity) {
        if (tvUserName == null)
            return;

//        if (!TextUtils.isEmpty(entity.getToken())) {
//            qNtoken = entity.getToken();
//            Log.i("--->>", "onSuccess: " + qNtoken);
//        } else
        if (progressPopupWindow != null) {
            progressPopupWindow.dismiss();
        }
        if (entity.getResult() == 1) {
            Log.i("--->>", "onSuccess: " + type);
            switch (type) {
                case 1:
                    if (getActivity() != null && getActivity() instanceof SettingActivity) {
                        ((SettingActivity) getActivity()).initView();
                    }
                    if (getActivity() != null && getActivity() instanceof FragmentActivity) {
                        Utils.showToast(entity.getInfo());
                        getActivity().setResult(203);
                        getActivity().finish();
                    }
                    break;
                case 2:
                    if (getActivity() != null && getActivity() instanceof SettingActivity) {
                        ((SettingActivity) getActivity()).initView();
                    }
                    ImageLoader.getInstance().displayCricleImage((BaseActivity) getActivity(), file.getAbsoluteFile(), ivUserIcon);
                    break;
                case 3:
//                    if (entity instanceof UserInfoEntity) {
//                        UserInfoEntity userInfoEntity = (UserInfoEntity) entity;
//                        if (userInfoEntity.getUserInfo() != null) {
//                            onMessageUserInfo(userInfoEntity);
//                        }
//                    }
                    break;
            }
        } else {
            Utils.showToast(entity.getInfo());
        }
    }

    @Override
    public void onSuccessNew(String url, BaseBean baseBean) {
        super.onSuccessNew(url, baseBean);
        switch (url) {
            case "306":
                MIneInfoBean bean = (MIneInfoBean) baseBean;
                onMessageUserInfo(bean);

                break;
            case "320":
                EditInfoFragment.newRxBus();
                ToastUtils.showShort("保存成功！");
                getActivity().finish();
                break;
            case "322":
                countryListBean = (CountryListBean) baseBean;
                for (CountryVO vo : countryListBean.getList()) {
                    countrys.add(isChinese() ? vo.getCname() : vo.getEname());
                }
                countryAdapter.notifyDataSetChanged();
                break;
            case "330":
                listBean = (ProfessionalListBean) baseBean;
                if (listBean != null) {
                    institutions.clear();
                    for (ProfessionalVO vo : listBean.getList()) {
                        institutions.add(isChinese() ? vo.getCname() : vo.getEname());
                    }
                    institutionAdapter.notifyDataSetChanged();
                }
                break;
            case "109":
                SubjectBean subjectBean = (SubjectBean) baseBean;
                if (subjectBean != null && subjectBean.getList() != null && subjectBean.getList().size() > 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("pid", subjectBean.getList().get(0).getId());
                    map.put("uid", getUserID());
                    map.put("language", getLanguage());
                    presenter.getDataAll("110", map);
                }

                break;
            case "110":
                majorBean = (MajorBean) baseBean;
                if (majorBean != null && majorBean.getMajors() != null && majorBean.getMajors().size() > 0) {
                    majors.clear();
                    for (MajorVO vo : majorBean.getMajors()) {
                        majors.add(vo.getName());
                    }
                    majorAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected WholePresenter initPresenter() {
        return new WholePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_edituserinfo;
    }

    @Override
    public void initView() {
        tvRight.setText(R.string.save);
        tvTitle.setText(R.string.set_data);
        name = tvUserName.getText() + "";
        sex = tvUserSex.getText() + "";
    }

    @Override
    public void initData() {
        super.initData();

        PageType = getArguments().getString("1005");
        if (PageType == null) {
            PageType = "";
        }
        String string = getArguments().getString("1006");
        switch (PageType) {
            case "verified_edit_info":
                type = 3;
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("uid", getUserID());
                userMap.put("language", getLanguage());
                if (presenter != null)
                    presenter.getDataAll("306", userMap);
                break;
            case "setting_edit_info":
                break;
        }
//        presenter.getQNTokenData(uid);
        progressPopupWindow = new ProgressPopupWindow((BaseActivity) getActivity());

        institutionPop = new ListPopupWindow(getContext());
        institutionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, institutions);
        institutionPop.setAdapter(institutionAdapter);
        institutionPop.setAnchorView(tvUserMechanism);
        institutionPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        institutionPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        institutionPop.setModal(true);
        institutionPop.setOnItemClickListener((parent, view, position, id) -> {
            oid = listBean.getList().get(position).getId();
            tvUserMechanism.setText(institutions.get(position));
            institutionPop.dismiss();
            countryPop.dismiss();
        });

        countryPop = new ListPopupWindow(getContext());
        countryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, countrys);
        countryPop.setAdapter(countryAdapter);
        countryPop.setAnchorView(tvUserCountry);
        countryPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        countryPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        countryPop.setModal(true);
        countryPop.setOnItemClickListener((parent, view, position, id) -> {
            cid = countryListBean.getList().get(position).getId();
            if (!institutionPop.isShowing())
                institutionPop.show();
            HashMap<String, Object> map = new HashMap<>();
            map.put("cid", cid);
            presenter.getDataAll("330", map);
        });

        majorPop = new ListPopupWindow(getContext());
        majorAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, majors);
        majorPop.setAdapter(majorAdapter);
        majorPop.setAnchorView(tvUserProfessional);
        majorPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        majorPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        majorPop.setModal(true);
        majorPop.setOnItemClickListener((parent, view, position, id) -> {
            mid = majorBean.getMajors().get(position).getId();
            tvUserProfessional.setText(majors.get(position));
            majorPop.dismiss();
        });

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUserID());
        presenter.getDataAll("322", map);

        map = new HashMap<>();
        map.put("language", getLanguage());
        map.put("pname", "");
        presenter.getDataAll("109", map);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.tv_back, R.id.fl_user_icon, R.id.tv_right,
            R.id.fl_user_professional, R.id.fl_user_name, R.id.fl_user_email,
            R.id.fl_user_phone, R.id.fl_user_sex, R.id.fl_user_mechanism,
            R.id.fl_user_position,R.id.fl_user_department,R.id.fl_user_jobtitle,
            R.id.fl_user_resume, R.id.fl_user_web})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                switch (PageType) {
                    case "verified_edit_info":
                        getActivity().setResult(201);
                        getActivity().finish();
                        break;
                    case "setting_edit_info":
                        getSettingActivity().getVpLogin().setCurrentItem(0, false);
                        break;
                }
                break;
            case R.id.fl_user_icon:
                showDialog();
                break;
            case R.id.tv_right:
                type = 1;
                changeName = tvUserName.getText() + "";
                changeSex = tvUserSex.getText() + "";

                HashMap<String, Object> map = new HashMap<>();
                if (!changeSex.equals(sex)) {
                    map.put("sex", changeSex.equals(getString(R.string.man)) ? 1 : 2);
                } else {
                    Utils.showToast("请选择性别");
                    return;
                }
                if (!changeName.equals(name)) {
                    map.put("name", changeName);
                } else {
                    Utils.showToast("请填写姓名");
                    return;
                }

                String phone = tvUserPhone.getText().toString();
                String email = tvUserEmail.getText().toString();
                String organization = tvUserMechanism.getText().toString();
                String position = tvUserPosition.getText().toString();
                String department = tvUserDepartment.getText().toString();
                String jobtitle = tvUserJobtitle.getText().toString();
//                String web = tvUserWeb.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    map.put("phone", phone);
                } else {
                    Utils.showToast(R.string.phone_verified);
                    return;
                }
                if (!TextUtils.isEmpty(oid)) {
                    map.put("oid", oid);
                } else {
                    Utils.showToast("请选择领域或专业");
                    return;
                }
                if (!TextUtils.isEmpty(email)) {
                    map.put("email", email);
                } else {
                    Utils.showToast("请填写邮箱");
                    return;
                }
                if (!TextUtils.isEmpty(mid)) {
                    map.put("mid", mid);
                } else {
                    Utils.showToast("请选择机构");
                    return;
                }
                if (!TextUtils.isEmpty(position)) {
                    map.put("position", position);
                } else {
                    Utils.showToast("请填写职称");
                    return;
                }
                if (!TextUtils.isEmpty(department)) {
                    map.put("department", department);
                }
                if (!TextUtils.isEmpty(jobtitle)) {
                    map.put("jobtitle", jobtitle);
                }
                if (map.size() > 0) {
                    map.put("uid", getUserID());
                    progressPopupWindow.showPopupWindow();
                    presenter.getDataAll("320", map);
                }

                break;
            case R.id.fl_user_sex:
                inputSexDialog();
                break;
            case R.id.fl_user_name:
                inputTitleDialog(tvUserName, getString(R.string.real_name));
                break;
            case R.id.fl_user_mechanism:
//                inputTitleDialog(tvUserMechanism, getString(R.string.subordinate_institution));
//                institutionPop.show();
                countryPop.show();
                break;
            case R.id.fl_user_position:
                inputTitleDialog(tvUserPosition, getString(R.string.user_position));
                break;
            case R.id.fl_user_department:
                inputTitleDialog(tvUserDepartment, getString(R.string.user_department));
                break;
            case R.id.fl_user_jobtitle:
                inputTitleDialog(tvUserJobtitle, getString(R.string.user_jobtitle));
                break;
            case R.id.fl_user_resume:
                inputTitleDialog(tvUserResume, getString(R.string.user_resume));
                break;
            case R.id.fl_user_phone:
                inputTitleDialog(tvUserPhone, getString(R.string.enter_phone));
                break;
            case R.id.fl_user_email:
                inputTitleDialog(tvUserEmail, getString(R.string.email));
                break;
            case R.id.fl_user_professional://机构
                majorPop.show();
//                Utils.showToast(R.string.web_editing_data);
                break;
            case R.id.fl_user_web:
                inputTitleDialog(tvUserWeb, getString(R.string.edit_user_web));
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageUserInfo(MIneInfoBean userInfoEntity) {
        if (tvUserName == null && userInfoEntity == null)
            return;
        Log.i("--->>", "onMessageUserInfo: " + userInfoEntity);
        String avatar = userInfoEntity.getAvatar();
        if (!imagUrl.equals(avatar))
            ImageLoader.getInstance().displayCricleImage((BaseActivity) getActivity(), avatar, ivUserIcon);
        imagUrl = avatar + "";
        if (!TextUtils.isEmpty(userInfoEntity.getName())) {
            tvUserName.setText(userInfoEntity.getName());
        }
        switch (userInfoEntity.getSex()) {
            case "1":
                tvUserSex.setText(getString(R.string.man));
                break;
            case "2":
                tvUserSex.setText(getString(R.string.gril));
                break;
            default:
                tvUserSex.setText(getString(R.string.unknown));
                break;
        }
        if (Utils.checkMobileNumber(userInfoEntity.getPhone())) {
            tvUserPhone.setText(userInfoEntity.getPhone());
        }
        if (Utils.checkEmail(userInfoEntity.getEmail())) {
            tvUserEmail.setText(userInfoEntity.getEmail().trim());
        }
        if (userInfoEntity.getEmailauthen() == null) {
            return;
        }
        if (!TextUtils.isEmpty(userInfoEntity.getOrganization())) {
            tvUserMechanism.setText(userInfoEntity.getOrganization());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getPosition())) {
            tvUserPosition.setText(userInfoEntity.getPosition());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getDepartment())) {
            tvUserDepartment.setText(userInfoEntity.getDepartment());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getJobtitle())) {
            tvUserJobtitle.setText(userInfoEntity.getJobtitle());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getDepartment())) {
            tvUserResume.setText(userInfoEntity.getDepartment());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getUserweb())) {
            tvUserWeb.setText(userInfoEntity.getUserweb());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getMajor())) {
            tvUserProfessional.setText(userInfoEntity.getMajor());
        }
        if (!TextUtils.isEmpty(userInfoEntity.getMid())) {
            mid = userInfoEntity.getMid();
        }
        if (!TextUtils.isEmpty(userInfoEntity.getOid())) {
            oid = userInfoEntity.getOid();
        }
        if (!TextUtils.isEmpty(userInfoEntity.getOcid())) {
            cid = userInfoEntity.getOcid();
        }
        if (!TextUtils.isEmpty(userInfoEntity.getPosition())) {
            tvUserPosition.setText(userInfoEntity.getPosition());
        }
    }

    private void inputTitleDialog(final TextView view, final String name) {

        final EditText inputServer = new EditText(getContext());
        inputServer.setPadding(50, 50, 50, 50);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.input) + name).setIcon(
                R.mipmap.ic_launcher).setView(inputServer).setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        switch (name) {
                            case "手机号码":
                                if (Utils.checkMobileNumber(inputName)) {
                                    view.setText(inputName);
                                } else {
                                    Utils.showToast("请输入正确的手机号码");
                                }

                                break;
                            case "邮箱":
                                if (Utils.checkEmail(inputName)) {
                                    view.setText(inputName);
                                } else {
                                    Utils.showToast("请输入正确的邮箱");
                                }
                                break;
                            case "真实姓名":
                                view.setText(inputName);
                                break;
                            case "所属机构":
                                view.setText(inputName);
                                break;
                            case "职称":
                                view.setText(inputName);
                                break;
                            case "履历":
                                view.setText(inputName);
                                break;
                            case "个人网址":
                                view.setText(inputName);
                                break;
                        }


                    }
                });
        builder.show();
    }

    private void inputSexDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.sex_info)).setIcon(
                R.mipmap.ic_launcher).setNegativeButton(getString(R.string.man), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvUserSex.setText(getString(R.string.man));
            }
        });
        builder.setPositiveButton(getString(R.string.gril),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tvUserSex.setText(getString(R.string.gril));
                    }
                });
        builder.show();
    }

    private void showDialog() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        Button butCamera = (Button) view.findViewById(R.id.but_camera);
        Button butAlbum = (Button) view.findViewById(R.id.but_album);
        Button butCancel = (Button) view.findViewById(R.id.but_cancel);
        butCamera.setOnClickListener(this);
        butAlbum.setOnClickListener(this);
        butCancel.setOnClickListener(this);
        dialog = new Dialog(getContext(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        if (window != null) {
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // 设置显示位置
            dialog.onWindowAttributesChanged(wl);
        }
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("--->>", "onActivityResult:requestCode: " + requestCode + "--resultCode:" + resultCode);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Giiisp/download",
                            "GiiispHead.jpg");
                    if (temp.exists()) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            cropPhoto(FileProvider.getUriForFile(context, "com.giiisp.giiisp.fileprovider", temp));//裁剪图片
                        } else {

                            cropPhoto(Uri.fromFile(temp));//裁剪图片
                        }
                    } else {
                        Utils.showToast("图片可能已经移位或删除");
                    }
                }
                break;
            case 3:
                if (file == null)
                    return;
                if (file.exists()) {
                    postPic(file);
                } else {
                    Utils.showToast("图片可能已经移位或删除");
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传头像
     *
     * @param file
     */
    public void postPic(File file) {
        if (TextUtils.isEmpty(file.getAbsolutePath()))
            return;
        progressPopupWindow.showPopupWindow();
//        String simpe = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        String upkey = UUID.randomUUID().toString() + simpe + "_" + Utils.fileName(file.getAbsolutePath());
//        uploadManager.put(file, upkey, qNtoken, new UpCompletionHandler() {
//            public void complete(String key1, ResponseInfo rinfo, JSONObject response) {
//                Log.i("qiniu", key1 + ",\r\n " + rinfo + ",\r\n " + response);
//
//                if (response == null) {
//                    progressPopupWindow.dismiss();
//                    return;
//                }
//                String key = response.optString("key");
//                if (TextUtils.isEmpty(key)) {
//                    progressPopupWindow.dismiss();
//                    return;
//                }
//                type = 2;
//                ArrayMap<String, Object> map = new ArrayMap<>();
//                map.put("path", UrlConstants.RequestUrl.QN_ADDRESS + key);
////                map.put("token", token);
//                map.put("uid", uid);
//                presenter.getUpdatePortraitData(map);
//                Log.i("--->>", "complete: " + key);
//            }
//
//        }, null);
        type = 2;
//        ArrayMap<String, Object> map = new ArrayMap<>();
//        map.put("uid", uid);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);

        presenter.getUpdatePortraitData(uid, imageBodyPart);

    }


    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Giiisp/download",
                "GiiispCropHead.jpg");

        imageUri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);
    }

    /**
     * 设置图片保存路径
     *
     * @return
     */
    private File getImageStoragePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            long sTime = System.currentTimeMillis();
            SimpleDateFormat simpe = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String format = simpe.format(new Date());
            return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), format + sTime + "GiiispHead.jpg");
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_camera:
                new RxPermissions(getActivity()).requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(@NonNull Permission permission) throws Exception {
                                if (permission.granted) {
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // Denied permission without ask never again
                                    Utils.showToast("取消照相机授权");
                                } else {
                                    // Denied permission with ask never again
                                    // Need to go to the
                                    Utils.showToast("您已经禁止弹出照相机的授权操作,请在设置中手动开启");
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.i("--->>", "onError", throwable);
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Giiisp/download");
                                    if (!folder.exists()) {
                                        folder.mkdirs();
                                    }
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        Uri photoUri = FileProvider.getUriForFile(context, "com.giiisp.giiisp.fileprovider", new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Giiisp/download",
                                                "GiiispHead.jpg"));
                                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                        startActivityForResult(takeIntent, 2);
                                    } else {
                                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Giiisp/download",
                                                "GiiispHead.jpg")));
                                        //指定照片存储路径
                                        //                intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(intent2, 2);//采用ForResult打开
                                    }

                                    dialog.dismiss();
                                } else {
                                    Utils.showToast("外部储存没有挂载");
                                }
                            }
                        });

                break;
            case R.id.but_album:
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
                break;
            case R.id.but_cancel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onFailure(String msg, Exception ex) {
        super.onFailure(msg, ex);
        if (progressPopupWindow != null) {
            progressPopupWindow.dismiss();
        }
    }

}
