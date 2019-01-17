package com.giiisp.giiisp.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.net.NetChangeObserver;
import com.giiisp.giiisp.net.NetWorkUtil;
import com.giiisp.giiisp.net.NetworkStateReceiver;
import com.giiisp.giiisp.utils.AppManager;
import com.giiisp.giiisp.utils.SharedPreferencesHelper;
import com.giiisp.giiisp.utils.Utils;
import com.giiisp.giiisp.view.activity.PaperDetailsActivity;
import com.giiisp.giiisp.widget.FloatDragView;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

import static com.giiisp.giiisp.api.UrlConstants.CN;
import static com.giiisp.giiisp.api.UrlConstants.UID;


public abstract class BaseActivity extends SupportActivity implements NetChangeObserver {

    private boolean isNetworkConnect;
    protected boolean mLastNetworkConnect; // 上次网络连接状态
    protected boolean isResume = false;
    //    public static String token = "";
    public static String emailauthen = ""; //  替代原先 isVIP 的功能
    public static String isVip = "";  // 用作身份认证按钮 的判断
    public Unbinder unbinder;
    public int downloadNunber;
    private ImageView baseImg;

    public void changeAppLanguage() {
        String sta = SharedPreferencesHelper.getInstance(this).getStringValue("VoiceSelection");//这是SharedPreferences工具类，用于保存设置，代码很简单，自己实现吧
        // 本地语言设置
        Log.i("--->>", "changeAppLanguage: " + sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        switch (sta) {
            case "English":
                conf.setLocale(Locale.ENGLISH);
                break;
            case "简体中文":
                conf.setLocale(Locale.SIMPLIFIED_CHINESE);
                break;
        }
        res.updateConfiguration(conf, dm);
        recreate();//刷新界面
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLastNetworkConnect = NetWorkUtil.isNetworkAvailable(this);
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //透明导航栏
            //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);stateHidden
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/

        if (savedInstanceState != null) {
//            token = savedInstanceState.getString("token");
            String stateUid = savedInstanceState.getString("uid");
            assert stateUid != null;
            SPUtils.getInstance().put(UID, stateUid);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FlymeSetStatusBarLightMode(getWindow(), true);
        MIUISetStatusBarLightMode(getWindow(), true);
        translucentStatusBar();

   /*     //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
*/
        setContentView(R.layout.activity_base_layout);
        RelativeLayout mBaseRl = findViewById(R.id.base_rl);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(getLayoutId(), null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        if (view != null)
            mBaseRl.addView(view, layoutParams);

        //添加可拖动悬浮按钮
        baseImg = FloatDragView.addFloatDragView(this, mBaseRl, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ObjectUtils.isNotEmpty(SPUtils.getInstance().getString(UrlConstants.PID))
                        && ObjectUtils.isNotEmpty(SPUtils.getInstance().getString(UrlConstants.PAPERVERSION))
                        && ObjectUtils.isNotEmpty(SPUtils.getInstance().getString(UrlConstants.PAPERTYPE))) {
                    PaperDetailsActivity.actionActivityBase(BaseActivity.this
                            , SPUtils.getInstance().getString(UrlConstants.PID)
                            , SPUtils.getInstance().getString(UrlConstants.PAPERVERSION)
                            , SPUtils.getInstance().getString(UrlConstants.PAPERTYPE)
                            , SPUtils.getInstance().getString(UrlConstants.LANGUAGE), getNowActivityName()
                    );
                } else {
                    ToastUtils.showShort("暂无播放信息");
                }
            }
        }, action -> {
            //在播放的时候，拖动按钮有问题，暂时这样：在拖动的时候暂停，结束在播放
            switch (action) {
                case MotionEvent.ACTION_DOWN:
//                        toolbarLayout.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
//                        toolbarLayout.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
//                        toolbarLayout.requestDisallowInterceptTouchEvent(false);
                    break;
            }
        });

        unbinder = ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        init();
    }


    public abstract String getNowActivityName();

    public abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        NetworkStateReceiver.registerObserver(this);
        MobclickAgent.onResume(this);
        isNetworkConnect = NetWorkUtil.isNetworkAvailable(this);
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
        MobclickAgent.onPause(this);
        NetworkStateReceiver.removeRegisterObserver(this);
    }

    private final void init() {
        initData();
        initView();
    }

    public abstract void initView();

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        AppManager.getAppManager().finishActivity();
    }

    public void initData() {
    }

    @Override
    public void onConnect(NetWorkUtil.NetType type) {
        Log.i("--->>", "onConnect: " + type + mLastNetworkConnect);
        if (!isResume)
            return;
        if (!mLastNetworkConnect) {
            initNetwork();
            mLastNetworkConnect = !mLastNetworkConnect;
        }
    }

    protected void initNetwork() {
        Log.i("--->>", "initNetwork: ");
    }

    /**
     * 当前没有网络连接
     */
    @Override
    public void onDisConnect() {
        //        mLastNetworkConnect = false;
        Utils.showToast("断开连接");
        Log.i("--->>", "onDisConnect: ");
    }

    private void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString("token", token);
        outState.putString("uid", getUserID());
        super.onSaveInstanceState(outState);
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public void addDownload() {

    }

    public void downloadCompleted() {
    }

    public void collection(int id, int integer, String type, String isFollowed, int parentPosition, int position) {

    }

    public String getUserID() {
        return SPUtils.getInstance().getString(UrlConstants.UID, "");
    }

    public String getLanguage() {
        return SPUtils.getInstance().getString(UrlConstants.LANGUAGE, CN);
    }


    public void hideImg() {
        baseImg.setVisibility(View.GONE);
    }

    public boolean isChinese() {
        return CN.equals(SPUtils.getInstance().getString(UrlConstants.LANGUAGE, CN));
    }
}
