package com.giiisp.giiisp.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import butterknife.ButterKnife;

/**
 * Created by Gao on 2017/3/23.
 * 弹窗基类
 */

public class BasePopupWindow extends PopupWindow {

    public View mMenuView;
    public Context mContext;
    private boolean focusable = true;

    /**
     * @param context
     * @param layoutID 布局文件
     */
    public BasePopupWindow(Context context, int layoutID, final int style) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(layoutID, null);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(focusable);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimDialogBottom);
        if (style != 0)
            this.setAnimationStyle(style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        setOutsideTouchable(true);
        //让pop可以点击外面消失掉
        ButterKnife.bind(this, mMenuView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        ToolWindow.backgroundAlpha((BaseActivity) mContext, 1f);//设置背景透明
    }

    //设置SelectPicPopupWindow弹出窗体可点击
    public void setFocus(boolean focus) {
        this.setFocusable(focus);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
//        ToolWindow.backgroundAlpha((BaseActivity) mContext, 0.5f);//设置背景不透明
    }

    /**
     * 显示弹窗
     *
     * @param parent 定位
     */
    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public int getWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return mMenuView.getMinimumWidth();
        } else {
            return mMenuView.getWidth();
        }
    }

    public int getHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return mMenuView.getMinimumHeight();
        } else {
            return mMenuView.getHeight();
        }
    }

}
/*
* public class PhotoPopupWindow extends BasePopupWindow {
    private Button btn_take_photo, btn_pick_photo, btn_cancel;

    public PhotoPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context, R.layout.dialog_white_bottom_layout, R.style.AnimDialogBottom);
        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
    }


    public void hide() {
        btn_take_photo.setVisibility(View.GONE);
        btn_pick_photo.setText("解除绑定");
    }


}






photoPopupWindow = new PhotoPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_take_photo://拍照
                            if (ToolApp.checkPermission(PhotoActivity.this, "android.permission.CAMERA")) {
                                ToolImage.takePhoto(PhotoActivity.this);
                            } else {
                                MyDialog myDialog = showPermissionDialog(ToolString.str_j, true);
                                myDialog.show();
                            }
                            photoPopupWindow.dismiss();
                            break;
                        case R.id.btn_pick_photo://相册选取
                            if (ToolApp.checkPermission(PhotoActivity.this, "android.permission.READ_EXTERNAL_STORAGE")
                                    && ToolApp.checkPermission(PhotoActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpg");
                                startActivityForResult(intent1, 1);
                            } else {
                                MyDialog myDialog = showPermissionDialog(ToolString.str_k, true);
                                myDialog.show();
                            }
                            photoPopupWindow.dismiss();
                            break;
                        case R.id.btn_cancel://取消
                            photoPopupWindow.dismiss();
                            break;
                    }
                }


            });
            photoPopupWindow.showAtLocation(relativelayout, Gravity.BOTTOM, 0, 0);
        }
* */