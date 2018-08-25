package com.giiisp.giiisp.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class PlayerUtil {

    private static MediaPlayer mediaPlayer; // 媒体播放器
    private static PlayerUtil sPlayerUtil;
    private String url = "";

    // 初始化播放器
    private PlayerUtil() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        }
    }

    public static PlayerUtil getInstance() {
        if (sPlayerUtil == null) {
            sPlayerUtil = new PlayerUtil();
        }
        return sPlayerUtil;
    }

    public void play() {
        mediaPlayer.start();
    }

    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        if (this.url != null && this.url.equals(url)) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url); // 设置数据源
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                this.url = url;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 暂停
    public void pause() {
        mediaPlayer.pause();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
