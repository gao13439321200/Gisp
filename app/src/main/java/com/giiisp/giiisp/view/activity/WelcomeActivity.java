package com.giiisp.giiisp.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.api.UrlConstants;
import com.giiisp.giiisp.base.BaseActivity;
import com.giiisp.giiisp.widget.recording.AppCache;
import com.giiisp.giiisp.widget.recording.PlayService;

import butterknife.BindView;

import static com.giiisp.giiisp.api.UrlConstants.LOGINTIME;

/**
 * 欢迎引导页
 * Created by Administrator on 2017/04/28.
 */

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.iv_welcome)
    ImageView ivWelcomeImg;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    private PlayServiceConnection mPlayServiceConnection;

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_welcome;
    }

    @Override
    public void initView() {
        hideImg();
//        ivWelcomeImg.setImageResource(R.mipmap.welcome_bg);
     /*   ivWelcomeImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                intentMethod();
            }
        }, 4000);
*/
    }

    private void intentMethod() {
//        String tokens = SharedPreferencesHelper.getInstance(WelcomeActivity.this).getStringValue("token");
        String uid = SPUtils.getInstance().getString(UrlConstants.UID, "");
        long loginTime = SPUtils.getInstance().getLong(LOGINTIME, 0);

        if (TextUtils.isEmpty(uid)) {
            LogInActivity.actionActivity(this);
        } else {
            if (TimeUtils.getTimeSpanByNow(loginTime, TimeConstants.DAY) > 10) {//登录信息大于10天，登录信息已过期
                ToastUtils.showShort("登录信息已失效，请重新登录");
                LogInActivity.actionActivity(this);
            } else {
                SPUtils.getInstance().put(LOGINTIME, TimeUtils.getNowMills());
                GiiispActivity.actionActivity(this, getIntent());
            }
        }
        finish();

    }

    @Override
    protected void onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
    }

    @Override
    public void initData() {
        super.initData();
        checkService();
    }

    private void checkService() {
        if (AppCache.getPlayService() == null) {
            startService();
//            showSplash();
//            updateSplash();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            }, 3000);
        } else {
            intentMethod();
        }
    }

    private void startService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            AppCache.setPlayService(playService);
            intentMethod();
/*            PermissionReq.with(WelcomeActivity.this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .result(new PermissionResult() {
                        @Override
                        public void onGranted() {
                            scanMusic(playService);
                        }

                        @Override
                        public void onDenied() {
                            ToastUtils.show(getString(R.string.no_permission, "存储空间", "扫描本地歌曲"));
                            finish();
                            playService.stop();
                        }
                    })
                    .request();*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

}
