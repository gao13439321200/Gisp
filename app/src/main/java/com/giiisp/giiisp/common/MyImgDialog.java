package com.giiisp.giiisp.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.giiisp.giiisp.R;
import com.giiisp.giiisp.model.GlideApp;

import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;

public class MyImgDialog extends Dialog implements View.OnClickListener {

    private ImageView imgView, center;
    private Button buttonOK;
    private Button buttonCancel;

    private Context mContext;
    private MyDialogOnClick mMyDialogOnClick;
    private boolean isShow = true;

    /**
     * 构造函数
     *
     * @param context         上下文
     * @param myDialogOnClick 回调函数
     * @param buttonNum       按钮数量
     */
    public MyImgDialog(Context context, MyDialogOnClick myDialogOnClick, int buttonNum) {
        super(context, R.style.successDialog);
        mContext = context;
        mMyDialogOnClick = myDialogOnClick;
        setContentView(R.layout.dialog_white_msg_layout);
        initAlertImportDialogView(buttonNum);
    }

    /**
     * 构造函数
     *
     * @param context         上下文
     * @param myDialogOnClick 回调函数
     * @param buttonNum       按钮数量
     */
    public MyImgDialog(Context context, MyDialogOnClick myDialogOnClick, int buttonNum, boolean isshow) {
        super(context, R.style.successDialog);
        mContext = context;
        this.isShow = isshow;
        mMyDialogOnClick = myDialogOnClick;
        setContentView(R.layout.dialog_white_msg_layout);
        initAlertImportDialogView(buttonNum);
    }


    @Override
    public void show() {
        super.show();
        backgroundAlpha((Activity) mContext, 0.5f);//设置背景半透明
    }

    private void initAlertImportDialogView(int btnnum) {
        imgView = findViewById(R.id.img);
        buttonCancel =  findViewById(R.id.btn_cancel);
//        buttonOK = (Button) findViewById(R.id.btn_dialog_ok);
//        buttonOK.setOnClickListener(this);
//
//        buttonCancel = (Button) findViewById(R.id.btn_dialog_cancel);
        buttonCancel.setOnClickListener(this);
//        if (btnnum == 1) {
//            buttonCancel.setVisibility(View.GONE);
//
//        }
    }

//    /**
//     * 设置弹窗提示信息
//     *
//     * @param nameString 提示信息
//     */
//    public void setNameText(String nameString) {
//        dialogMsg.setText(nameString);
//    }


//    /**
//     * 设置确定按钮的文字
//     *
//     * @param name 名字
//     */
//    public void setButtonOK(String name) {
//        buttonOK.setText(name);
//    }

//    /**
//     * 设置确定按钮的文字颜色
//     *
//     * @param color 名字颜色
//     */
//    public void setButtonOKColor(int color) {
//        buttonOK.setTextColor(color);
//    }

//    /**
//     * 设置确定按钮的文字
//     *
//     * @param name 名字
//     */
//    public void setButtonOK(String name, int color) {
//        buttonOK.setText(name);
////        buttonOK.setTextColor(mContext.getResources().getColor(color));
//        buttonOK.setTextColor(getContext().getResources().getColor(color));
//    }

//
//    /**
//     * 获取确定按钮的文字
//     */
//    public String getButtonOK() {
//        return buttonOK.getText().toString();
//    }


//    /**
//     * 设置取消按钮的文字
//     *
//     * @param name 名字
//     */
//    public void setButtonCancel(String name) {
//        buttonCancel.setText(name);
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                mMyDialogOnClick.onCancelClick();
                dismiss();
                break;
//            case R.id.btn_dialog_ok:
//                mMyDialogOnClick.onOKClick();
//                dismiss();
//                break;
            default:
                break;
        }
    }

    public void setImgView(String url) {
        GlideApp.with(mContext).load(BASE_IMG_URL + url).into(imgView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (isShow) {
            backgroundAlpha((Activity) mContext, 1.0f);//设置背景半透明
        }
    }


    public void setShow(boolean show) {
        isShow = show;
    }
//
//    public void setCenter() {
//        center.setVisibility(View.VISIBLE);
//        dialogMsg.setGravity(Gravity.CENTER_VERTICAL);
//    }

    /**
     * 设置背景透明度
     *
     * @param bgAlpha 0.0-1.0
     */
    public void backgroundAlpha(Activity mContext, float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }

}
