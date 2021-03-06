package com.giiisp.giiisp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.giiisp.giiisp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCustomView extends View {

    private Context mContext;
    private List<Map<String, Float>> mPointList = new ArrayList<>();
    private long time;
    private boolean canMark = false;
    private DrawListen mDrawListen;

    public void setDrawListen(DrawListen drawListen) {
        mDrawListen = drawListen;
    }

    public void setCanMark(boolean canMark) {
        this.canMark = canMark;
    }

    public MyCustomView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }

    public void addPoint(float x, float y) {
        Map<String, Float> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        mPointList.add(map);
        invalidate();
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    //清空数据并且清空界面
    public void clearData() {
        mPointList.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.point1);
        for (Map<String, Float> map : mPointList) {
            canvas.drawBitmap(bitmap,
                    map.get("x") - (bitmap.getWidth() / 2),
                    map.get("y") - (bitmap.getHeight() / 2),
                    null);
//            canvas.drawCircle(map.get("x"), map.get("y"), 25, mPaint);
        }
        super.onDraw(canvas);
    }

    private Paint mPaint;

    private void initPaint() {
        mPaint = new Paint();
        //设置画笔颜色
        mPaint.setColor(mContext.getResources().getColor(R.color.green));
        //STROKE                //描边
        //FILL                  //填充
        //FILL_AND_STROKE       //描边加填充
        //设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔宽度为30px
        mPaint.setStrokeWidth(15f);
        mPointList = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("MyCustomView", "event.getDownTime():" + event.getDownTime());
        Log.d("MyCustomView", "event.getEventTime():" + event.getEventTime());
        Log.d("MyCustomView", "时间差:" + TimeUtils.getTimeSpan(event.getDownTime(), event.getEventTime(), 1000));
        //可以画图 = 点击了开始标记+手指已经抬起+时间间隔大于等于2秒
//        if (canMark && time != event.getDownTime() &&
//                TimeUtils.getTimeSpan(event.getDownTime(), event.getEventTime(), 1000) >= 2) {
        if (canMark) {
            time = event.getDownTime();
            mPointList.clear();//每次只显示一个点
            HashMap<String, Float> map = new HashMap<>();
            map.put("x", event.getX());
            map.put("y", event.getY());
            mPointList.add(map);
            invalidate();
            mDrawListen.drawListen(event.getX(), event.getY());
        }
        return super.onTouchEvent(event);
    }

    public boolean hasPoint() {
        return mPointList.size() > 0;
    }

    public interface DrawListen {
        void drawListen(float x, float y);
    }

}


