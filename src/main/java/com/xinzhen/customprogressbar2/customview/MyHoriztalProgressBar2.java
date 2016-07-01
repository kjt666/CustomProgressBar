package com.xinzhen.customprogressbar2.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.xinzhen.customprogressbar2.R;


/**
 * Created by C058 on 2016/7/1.
 */
public class MyHoriztalProgressBar2 extends ProgressBar {

    private final int PROGRESSBAR_DEFAULT_HEIGHT = dp2px(10);//px
    private final int PROGRESSBAR_DEFAULT_REACH_COLOR = 0xfff456c5;
    private final int PROGRESSBAR_DEFAULT_UNREACH_COLOR = 0xFFCECECE;

    private int mProgressbarHeight;
    private int mReachColor;
    private int mUnReachColor;
    private int mRealWidth;
    private Paint mPaint;

    private Bitmap mDstBmp, mSrcBmp;
    private Canvas mSrcCanvas;
    float progressX;

    public MyHoriztalProgressBar2(Context context) {
        this(context, null);
    }

    public MyHoriztalProgressBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHoriztalProgressBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyHoriztalProgressBar2);
        mProgressbarHeight = (int) ta.getDimension(R.styleable.MyHoriztalProgressBar2_progressbar_height, PROGRESSBAR_DEFAULT_HEIGHT);
        mReachColor = ta.getColor(R.styleable.MyHoriztalProgressBar2_reach_color, PROGRESSBAR_DEFAULT_REACH_COLOR);
        mUnReachColor = ta.getColor(R.styleable.MyHoriztalProgressBar2_unreach_color, PROGRESSBAR_DEFAULT_UNREACH_COLOR);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mProgressbarHeight);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int heightVal = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, heightVal);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mDstBmp = getDstPic();//完成测量后再创建图片\
        mSrcBmp = Bitmap.createBitmap(getMeasuredWidth(), mProgressbarHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        progressX = mRealWidth * getProgress() / 100;
        mSrcBmp = getSrcPic(progressX);
        int layer = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.translate(getPaddingLeft(), (getMeasuredHeight() - mProgressbarHeight) / 2); //这个高度上的位移让我想了很久。画图和普通的画线不一样。画图是从图片的（0,0）点开始画的，画线是从（0，线高度的一半）开始画的。
        canvas.drawBitmap(mDstBmp, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(mSrcBmp, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layer);
    }

    /**
     * 测量控件高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = height;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = getPaddingTop() + getPaddingBottom() + mProgressbarHeight * 2;
        }
        return result;
    }

    /**
     * 获取源图像，因为使用次数较多，所以将一些资源抽了出来，避免频繁gc操作，耗费内存
     *
     * @param progress
     * @return
     */
    private Bitmap getSrcPic(float progress) {
        mSrcCanvas = new Canvas(mSrcBmp);
        mPaint.setColor(mReachColor);
        //canvas.drawLine(mProgressbarHeight, mProgressbarHeight / 2, progress - mProgressbarHeight, mProgressbarHeight / 2, paint);
        if (getProgress() <= 50) {
            mSrcCanvas.drawLine(0, mProgressbarHeight / 2, progress, mProgressbarHeight / 2, mPaint);
        } else {
            mSrcCanvas.drawLine(0, mProgressbarHeight / 2, progress - mProgressbarHeight / 2, mProgressbarHeight / 2, mPaint);
        }
        return mSrcBmp;
    }

    /**
     * 获取目标图层图像，初始一次就行了。用临时变量
     *
     * @return
     */
    private Bitmap getDstPic() {
        //创建画布，不用mRealWidth的原因是加入给控件设定10dp宽度，在设定10dp的pading，那么这个mReaalWidth则为负数，创建bitmap会报错，同理，高度我们也是限定了的
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), mProgressbarHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mPaint.setColor(mUnReachColor);
        //因为线帽是线段两头的样式，是多出来的一部分，如果不设置线帽从0开始就可以，线帽长度一半为线的高度。
        canvas.drawLine(mProgressbarHeight, mProgressbarHeight / 2, mRealWidth - mProgressbarHeight, mProgressbarHeight / 2, mPaint);
        return bitmap;
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
