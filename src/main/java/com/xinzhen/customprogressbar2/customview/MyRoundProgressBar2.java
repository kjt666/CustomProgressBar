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
 * 模仿ios app store应用下载圆形进图条
 */
public class MyRoundProgressBar2 extends MyHoriztalProgressBar {

    private static final int DEFAULT_PROGRESS_RADIUS = 30;
    private int mRadius = dp2px(DEFAULT_PROGRESS_RADIUS);
    private int mInRadius;
    private RectF mRectf, mInRectf;

    public MyRoundProgressBar2(Context context) {
        this(context, null);
    }

    public MyRoundProgressBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRoundProgressBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyRoundProgressBar);
        mRadius = (int) ta.getDimension(R.styleable.MyRoundProgressBar_progressbar_radius, mRadius);
        ta.recycle();

        mReachHeight = mUnReachHeight * 2;
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true); //防抖动模式
        mPaint.setStyle(Paint.Style.STROKE);//画笔风格设置为空心
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int diameter = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mUnReachHeight * 2; //控件宽度 默认四个padding一致
        int width = resolveSize(diameter, widthMeasureSpec);
        int height = resolveSize(diameter, heightMeasureSpec);
        int realWidth = Math.min(width, height);//当宽高设置不一致，取小的那个
        //外圆的半径
        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mUnReachHeight) / 2;
        mRectf = new RectF(0, 0, mRadius * 2, mRadius * 2);
        //内圆的半径
        mInRadius = mRadius - mUnReachHeight;
        mInRectf = new RectF(0, 0, mInRadius * 2, mInRadius * 2);
        setMeasuredDimension(realWidth, realWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        //draw unreachbar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        //从圆点开始画圆
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        //draw reachbar
        //将画布移动到画内圆的位置
        canvas.translate(mUnReachHeight, mUnReachHeight);
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(mInRectf, 0, sweepAngle, false, mPaint);
//        //draw text
//        String text = getProgress() + "%";
//        int textWidth = (int) mPaint.measureText(text);
//        int textHeight = (int) ((mPaint.descent() + mPaint.ascent()) / 2);
//        mPaint.setColor(mTextColor);
//        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
        canvas.restore();
    }
}
