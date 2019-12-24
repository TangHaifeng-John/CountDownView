好久没有写博客了，趁着年末，总结了下最近一年所遇到的一些技术问题，还有一些自定义控件，比如倒计时功能

首先倒计时的实现方式

1.Handler
2.Timer
3.RxJava
4.ValueAnimator
5.其他

这些方式中，我选择了ValueAnimator,主要是它的API比较友好，不需要我们去封装太多东西,具体的使用方式我就不单独写了，下面的代码都有备注

[项目地址](https://github.com/TangHaifeng-John/CountDownView)

代码实现：

```
package com.example.countdownview;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


public class CountDownView extends View {

    //圆轮颜色
    private int mRingColor;
    //圆轮宽度
    private float mRingWidth;

    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    private Paint mPaint;
    //圆环的矩形区域
    private RectF mRectF;
    //
    private int mCountdownTime;
    private float mCurrentProgress;
    private OnCountDownFinishListener mListener;
    ValueAnimator valueAnimator;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mRingColor = a.getColor(R.styleable.CountDownView_ringColor, Color.RED);
        mCountdownTime = a.getInteger(R.styleable.CountDownView_countdownTime, 10);
        mRingWidth=a.getDimension(R.styleable.CountDownView_ringWidth,2);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /**
         *圆环
         */
        //颜色
        mPaint.setColor(mRingColor);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true); // 消除锯齿
        //宽度
        mPaint.setStrokeWidth(mRingWidth);
    }

    public void setCountdownTime(int mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRectF = new RectF(0 + mRingWidth / 2, 0 + mRingWidth / 2,
                mWidth - mRingWidth / 2, mHeight - mRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mRectF, -90, mCurrentProgress, false, mPaint);


    }

    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        setClickable(false);
        valueAnimator = getValA(mCountdownTime * 1000);
        //状态更新监听
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                mCurrentProgress = (int) (360 * (i / 100f));
                invalidate();
            }
        });
        valueAnimator.start();
        //状态变化结束监听
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //倒计时结束回调
                if (mListener != null) {
                    mListener.countDownFinished();
                }
                setClickable(true);
            }

        });
    }


    /**
     * 恢复
     */
    public void  resumeCountDown(){
        if (valueAnimator!=null){
            valueAnimator.resume();
        }
    }

    /**
     * 暂停
     */
    public void  pauseCountDown(){
        if (valueAnimator!=null){
                valueAnimator.pause();
        }
    }

    /**
     * 停止倒计时
     */
    public void  stopCountDown(){
        if (valueAnimator!=null){
            valueAnimator.cancel();
        }
    }

    public void setCountDownFinishListener(OnCountDownFinishListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCountDownFinishListener {
        void countDownFinished();
    }
}

```
