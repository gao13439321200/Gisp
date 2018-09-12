package com.giiisp.giiisp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.LogUtils;
import com.giiisp.giiisp.R;
import com.giiisp.giiisp.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CoursePlayActivity extends BaseActivity {
    @BindView(R.id.video)
    VideoView mVideoView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public static void newInstance(Context context, String url, String name) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("name", name);
        Intent intent = new Intent(context, CoursePlayActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    @Override
    public String getNowActivityName() {
        return this.getClass().getName();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment_course;
    }

    @Override
    public void initView() {
        tvTitle.setText(getIntent().getExtras().getString("name"));
        String path = getIntent().getExtras().getString("url");
        LogUtils.v("路径：" + path);
//        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");//将路径转换成uri
        Uri uri = Uri.parse(path);//将路径转换成uri
        mVideoView.setVideoURI(uri);//为视频播放器设置视频路径
        mVideoView.setMediaController(new MediaController(this));//显示控制栏
//        mVideoView.setOnPreparedListener(mp -> {
        mVideoView.start();//开始播放视频
//        });
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
