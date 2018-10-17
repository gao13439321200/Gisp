package com.giiisp.giiisp.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.giiisp.giiisp.R;

public class ScaleAttrsImageView extends ImageView
        implements ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {

    private Matrix mMatrix;
    private GestureDetector mGestureDetector1;
    private float mInitScale;
    private int much = 1;

    public ScaleAttrsImageView(Context context) {
        this(context, null);
    }

    public ScaleAttrsImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private boolean isScale = true;

    public ScaleAttrsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnTouchListener(this);

        //获取所有的自定义属性和样式
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScaleImageView, defStyleAttr, 0);
        //获取自定义属性的个数
        final int indexCount = typedArray.getIndexCount();
        //获取相关设定的值
        for (int i = 0; i < indexCount; i++) {
            final int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ScaleImageView_isScaleImage:
                    isScale = typedArray.getBoolean(attr, false);
            }
        }
        //在mGestureDetector1的方法onDoubleTap中进行相关操作
        mGestureDetector1 = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //获取当前的点击点的坐标
                float x = e.getX();
                float y = e.getY();
                switch (much) {
                    case 1:
                        much = 2;
                        break;
                    case 2:
                        much = 3;
                        break;
                    case 3:
                        much = 1;
                        mMatrix.postScale(mInitScale / 4, mInitScale / 4, x, y);
                        setImageMatrix(mMatrix);
                        return true;
                    default:
                        break;
                }
                mMatrix.postScale(2, 2, x, y);
                setImageMatrix(mMatrix);
                return true;
            }
        });
        mMatrix = new Matrix();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private boolean mOnce = false;

    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            final int width = getWidth();
            final int height = getHeight();
            final Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            final int intrinsicWidth = drawable.getIntrinsicWidth();
            final int intrinsicHeight = drawable.getIntrinsicHeight();
            float scale = 1.0f;
//            如果图片宽度大于控件宽度，图片高度小于控件高度  图片缩小
            if (intrinsicWidth > width && intrinsicHeight < height) {
                scale = intrinsicWidth * 1.0f / width;
            }
//            //如果图片的高度大于控件的高度，图片的宽度小于控件的宽度  图片缩小
            if (intrinsicHeight > height && intrinsicWidth < width) {
                scale = intrinsicHeight * 1.0f / height;
            }
//            //如果图片的宽与高都大于控件的宽与高 或者 图片的宽与高都小于控件的宽与高
            if ((intrinsicHeight > height && intrinsicWidth > width)) {
                scale = Math.min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight);
            }
            if (isScale && (intrinsicHeight < height && intrinsicWidth < width)) {
                scale = Math.min(width * 1.0f / intrinsicWidth, height * 1.0f / intrinsicHeight);
            }
//            //得到初始化时图片需要进行缩放的值
            final int moveX = getWidth() / 2 - intrinsicWidth / 2;
            final int moveY = getHeight() / 2 - intrinsicHeight / 2;
            mInitScale = scale;
            mMatrix.postTranslate(0, 0);
            mMatrix.postScale(0.5f, 0.5f, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mMatrix);
            mOnce = true;
        }
    }

    public float getInitScale() {

        final float[] floats = new float[9];
        mMatrix.getValues(floats);
        //获取当前的缩放值
        return floats[Matrix.MSCALE_X];
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mGestureDetector1.onTouchEvent(event)) {
            return true;
        }
        return true;
    }
}


