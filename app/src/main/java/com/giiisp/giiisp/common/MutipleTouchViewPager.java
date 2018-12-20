package com.giiisp.giiisp.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MutipleTouchViewPager extends ViewPager {

    private float beforeX;

    public MutipleTouchViewPager(Context context) {
        super(context);
    }

    public MutipleTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mIsDisallowIntercept = false;

    private boolean isDubbing = false;
    private boolean isDiaoYong = false;
    private int dubbingPosition = 0;


    public void setDubbing(boolean dubbing) {
        isDubbing = dubbing;
        dubbingPosition = getCurrentItem();
    }

    public void setDiaoYong(boolean diaoYong) {
        isDiaoYong = diaoYong;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // keep the info about if the innerViews do
        // requestDisallowInterceptTouchEvent
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isDiaoYong) {
            return false;
        }

        //正在录音 +向右滑动 +当前位置为正在录音图片 = 不允许滑动
        if (isDubbing && getCurrentItem() == dubbingPosition) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                    beforeX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("MutipleTouchViewPager", "ev.getX():" + ev.getX());
                    Log.d("MutipleTouchViewPager", "beforeX:" + beforeX);
                    Log.d("MutipleTouchViewPager", "-------------------------------------------------------");

                    float motionValue = ev.getX() - beforeX;
                    if (motionValue < 0) {//禁止左滑
                        return false;
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
                    break;
            }
        }
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}
