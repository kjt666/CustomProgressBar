package com.xinzhen.customprogressbar2.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.xinzhen.customprogressbar2.R;

/**
 * Created by C058 on 2016/5/25.
 */
public class MyHoriztalProgressBar extends ProgressBar {

    private static final int DEFAULT_REACH_COLOR = 0xff24F569;
    private static final int DEFAULT_UNREACH_COLOR = 0xffC0C0C0;
    private static final int DEFAULT_REACH_HEIGHT = 2;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;
    private static final int DEFAULT_TEXT_COLOR = DEFAULT_REACH_COLOR;
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_OFFSET = 5;

    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mUnReachColor = DEFAULT_UNREACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();
    private int mRealWidth;

    public MyHoriztalProgressBar(Context context) {
        this(context, null);
    }

    public MyHoriztalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHoriztalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyHoriztalProgressBar);
        mReachColor = ta.getColor(R.styleable.MyHoriztalProgressBar_progressbar_reach_color, mReachColor);
        mUnReachColor = ta.getColor(R.styleable.MyHoriztalProgressBar_progressbar_unreach_color, mUnReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar_progressbar_reach_height, mReachHeight);
        mUnReachHeight = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar_progressbar_unreach_height, mUnReachHeight);
        mTextColor = ta.getColor(R.styleable.MyHoriztalProgressBar_progressbar_text_color, mTextColor);
        mTextSize = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar_progressbar_text_size, mTextSize);
        mTextOffset = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar_progressbar_text_offset, mTextOffset);
        ta.recycle();
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int heightVal = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, heightVal);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //draw reachBar
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        boolean noNeedUnrechBar = false;

        canvas.save();
        canvas.translate(getPaddingLeft(), getMeasuredHeight() / 2);
        float radio = getProgress() * 1.0f / getMax();
        float progressX = mRealWidth * radio;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnrechBar = true;
        }
        //draw reachbar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float endX = progressX - mTextOffset / 2;
        canvas.drawLine(0, 0, endX, 0, mPaint);
        //draw text
        mPaint.setColor(mTextColor);
        float y = -(mPaint.descent() + mPaint.ascent())/2;
        canvas.drawText(text, progressX,y, mPaint);
        //draw unreachbar
        if (!noNeedUnrechBar) {
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            float startX = progressX + textWidth + mTextOffset / 2;
            canvas.drawLine(startX, 0, mRealWidth, 0, mPaint);
        }
        canvas.restore();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = height;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachHeight, mUnReachHeight), textHeight);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, height);
            }
        }
        return result;
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    protected int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
