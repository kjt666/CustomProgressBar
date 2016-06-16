package com.xinzhen.customprogressbar2.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.xinzhen.customprogressbar2.R;

/**
 * Created by C058 on 2016/5/26.
 */
public class MyRoundProgressBar extends MyHoriztalProgressBar {

    private static final int DEFAULT_PROGRESS_RADIUS = 30;
    private int mMaxPaintWidth;
    private int mRadius = dp2px(DEFAULT_PROGRESS_RADIUS);
    private RectF mRectf;

    public MyRoundProgressBar(Context context) {
        this(context, null);
    }

    public MyRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyRoundProgressBar);
        mRadius = (int) ta.getDimension(R.styleable.MyRoundProgressBar_progressbar_radius, mRadius);
        ta.recycle();

        mReachHeight = mUnReachHeight * 2;
        mMaxPaintWidth = mReachHeight;
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int diameter = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mMaxPaintWidth; //控件宽度 默认四个padding一致
        int width = resolveSize(diameter, widthMeasureSpec);
        int height = resolveSize(diameter, heightMeasureSpec);

        int realWidth = Math.min(width, height);//当宽高设置不一致，取小的那个
        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;
        mRectf = new RectF(0, 0, mRadius * 2, mRadius * 2);
        setMeasuredDimension(realWidth, realWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        // mMaxPaintWidth / 加上这个只是为了能让已完成的bar完整显示出来
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        //draw unreachbar
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        //从圆点开始画圆
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        //draw reachbar
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(mRectf, 0, sweepAngle, false, mPaint);
        //draw text
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        int textHeight = (int) ((mPaint.descent() + mPaint.ascent()) / 2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
        canvas.restore();

    }
}
