package com.xinzhen.customprogressbar2.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.xinzhen.customprogressbar2.R;

/**
 * Created by C058 on 2016/6/14.
 */
public class MyHoriztalProgressBar2 extends ProgressBar {

    private static final int DEFAULT_PROGRESSBAR_HEIGHT = 10;
    private static final int DEFAULT_REACH_COLOR = 0xfff456c5;
    private static final int DEFAULT_UNREACH_COLOR = 0xFFCECECE;
    private static final int DEFAULT_PROGRESSBAR_RADIUS = DEFAULT_PROGRESSBAR_HEIGHT / 2;

    private int mProgressbarHeight = dp2px(DEFAULT_PROGRESSBAR_HEIGHT);
    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mUnReachColor = DEFAULT_UNREACH_COLOR;
    private int mProgressbarRadius = dp2px(DEFAULT_PROGRESSBAR_RADIUS);

    private int mRealWidth,mRealHeight;
    private Paint mPaint;
    private RectF mRectf;

    private Bitmap mDstBmp, mSrcBmp;

    public MyHoriztalProgressBar2(Context context) {
        this(context, null);
    }

    public MyHoriztalProgressBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHoriztalProgressBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyHoriztalProgressBar2);
        mProgressbarHeight = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar2_progressbar_height, mProgressbarHeight);
        mReachColor = ta.getColor(R.styleable.MyHoriztalProgressBar2_reach_color, mReachColor);
        mUnReachColor = ta.getColor(R.styleable.MyHoriztalProgressBar2_unreach_color, mUnReachColor);
        mProgressbarRadius = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar2_radius, mProgressbarRadius);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mRectf = new RectF(0, 0, mProgressbarRadius * 2, mProgressbarRadius * 2);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int heightVal = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, heightVal);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mRealHeight= getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radio = getProgress() * 1.0f / getMax();
        float finshedX = mRealWidth * radio;
        mDstBmp = getBackGround();
        mSrcBmp = getProgressX(finshedX);
        int layerID1 = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.translate(getPaddingLeft(), 0);
        canvas.drawBitmap(mDstBmp, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mSrcBmp, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerID1);
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = height;
        } else {
            result = getPaddingTop() + getPaddingBottom() + mProgressbarHeight;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, height);
            }
        }
        return result;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private Bitmap getBackGround() {
        Bitmap bitmap = Bitmap.createBitmap(mRealWidth, mRealHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(mUnReachColor);
        paint.setStrokeWidth(2 * mProgressbarHeight);
        //开头的半圆
        canvas.drawArc(mRectf, 90, 180, false, paint);
        //直线
        canvas.drawLine(mProgressbarRadius, 0, mRealWidth - mProgressbarRadius, 0, paint);
        //结尾的半圆
        canvas.translate(mRealWidth - mProgressbarHeight, 0);
        canvas.drawCircle(mProgressbarRadius, mProgressbarRadius, mProgressbarRadius, paint);
        return bitmap;
    }

    private Bitmap getProgressX(float progress) {
        Bitmap bitmap = Bitmap.createBitmap((int) progress, mRealHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(mReachColor);
        paint.setStrokeWidth(2 * mProgressbarHeight);
        canvas.drawLine(0, 0, progress, 0, paint);
        return bitmap;
    }
}
