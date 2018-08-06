package com.giiisp.giiisp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.giiisp.giiisp.R;

public class MyCustomView extends View {

    private Context mContext;

    public MyCustomView(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private Paint mPaint;
    private Canvas mCanvas;

    private void initPaint() {
        mPaint = new Paint();
        //设置画笔颜色
        mPaint.setColor(mContext.getResources().getColor(R.color.blue));
        //STROKE                //描边
        //FILL                  //填充
        //FILL_AND_STROKE       //描边加填充
        //设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔宽度为30px
        mPaint.setStrokeWidth(30f);
        mCanvas = new Canvas();
    }

    private void drawMark(int x, int y) {
        mCanvas.drawCircle(x, y, 100, mPaint);
    }

}
