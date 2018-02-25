package com.hawk.battery.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by lan on 2018-02-02.
 */

public class BatteryView extends View {
    /**
     * 默认参数
     */
    private static final float defaultWarnValue = 15.1f;                // 低电量的默认值
    private static final float defaultRatio = 7f/13;                  // 视图默认高宽比例
    private static final float defaultWidth = 40f;                   // 视图的默认宽度
    private static final float defaultHightRatio = 2f;               // 电池与电池盖默认高比例
    private static final float defaultWidthRatio = 9f;               // 电池与电池盖默认宽比例
    private static final float defaultArch = 5f;                     // 电池默认弧度
    private static final float defaultBatteryStroke = 3f;           // 电池画笔的默认厚度
    private static final float defaultPowerStroke = 1f;             // 电量画笔的默认厚度
    private static final int defaultBatteryColor = Color.LTGRAY;     // 电池画笔的默认颜色
    private static final int defaultPowerColor = Color.RED;          // 电量画笔的默认颜色
    private static final int defaultWarnColor = Color.RED;          // 低电量画笔的默认颜色
    /**
     * 画笔信息
     */
    private Paint mBatteryPaint;
    private Paint mPowerPaint;
    /**
     * 视图高宽
     */
    private int width;
    private int measureWidth;
    private int measureHeigth;
    /**
     * 电池参数
     */
    private float batteryArch = 0;                     // 电池弧度
    private float mBatteryHeight = 0;                       // 电池的高度
    private float mBatteryWidth = 0;                        // 电池的宽度
    private float mCapHeight = 0;                           // 电池盖的高度
    private float mCapWidth = 0;                            // 电池盖的宽度
    private float mBatteryStroke = defaultBatteryStroke;         // 电池画笔厚度
    private float mPowerStroke = defaultPowerStroke;             // 电量画笔厚度
    private int mBatteryColor = 0;                          // 电池画笔颜色
    private int mPowerColor = 0;                              // 电量画笔颜色
    private int mWarnColor = 0;                              // 低电量画笔颜色
    /**
     * 电池电量
     */
    private float mPowerHeight = 0;             // 电量的总高度
    private float mPowerWidth = 0;              // 电量的总宽度
    private float mPower = 0f;                  //当前电量
    /**
     * 矩形
     */
    private RectF mBatteryRect = new RectF(0, 0, 0, 0);
    private RectF mCapRect = new RectF(0, 0, 0, 0);
    private RectF mPowerRect = new RectF(0, 0, 0, 0);

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BatteryView);

        if (typedArray != null) {
            int widthDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultWidth, getResources().getDisplayMetrics());
            width = typedArray.getDimensionPixelSize(R.styleable.BatteryView_batteryWidth, widthDip);
            batteryArch = typedArray.getFloat(R.styleable.BatteryView_batteryRadius, defaultArch);
            mBatteryColor = typedArray.getColor(R.styleable.BatteryView_batteryColor, defaultBatteryColor);
            mPowerColor = typedArray.getColor(R.styleable.BatteryView_powerColor, defaultPowerColor);
            mWarnColor = typedArray.getColor(R.styleable.BatteryView_warnColor, defaultWarnColor);
            typedArray.recycle();
        }
        /**
         * 设置电池画笔
         */
        mBatteryPaint = new Paint();
        mBatteryPaint.setColor(mBatteryColor);
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(mBatteryStroke);
        /**
         * 设置电量画笔
         */
        mPowerPaint = new Paint();
        mPowerPaint.setColor(mPowerColor);
        mPowerPaint.setAntiAlias(true);
        mPowerPaint.setStyle(Paint.Style.FILL);
        mPowerPaint.setStrokeWidth(mPowerStroke);
    }

    private void intDrawRectF() {
        mBatteryHeight = measureHeigth - getPaddingTop() - getPaddingBottom();       // 电池的高度
        mBatteryWidth = (measureWidth - getPaddingLeft() - getPaddingRight()) * defaultWidthRatio / (defaultWidthRatio + 1); // 电池的宽度
        mCapHeight = mBatteryHeight / defaultHightRatio;    // 电池盖的高度
        mCapWidth = mBatteryWidth / (defaultWidthRatio + 1);       // 电池盖的宽度
        mPowerHeight = mBatteryHeight - mBatteryStroke * 2; // 电量的总高度
        mPowerWidth = mBatteryWidth - mBatteryStroke * 2;// 电量的总宽度
        /**
         * 设置电池矩形，左对齐
         */
        mBatteryRect.left = getPaddingLeft();
        mBatteryRect.top = getPaddingTop();
        mBatteryRect.right = getPaddingLeft() + mBatteryWidth;
        mBatteryRect.bottom = getPaddingTop() + mBatteryHeight;
        /**
         * 设置电池盖矩形
         */
        mCapRect.left = mBatteryRect.right;
        mCapRect.top = getPaddingTop() + (mBatteryHeight - mCapHeight) / 2;
        mCapRect.right = mBatteryRect.right + mCapWidth;
        mCapRect.bottom = getPaddingTop() + (mBatteryHeight - mCapHeight) / 2 + mCapHeight;
        /**
         * 设置电量矩形
         */
        mPowerRect.left = getPaddingLeft() + mBatteryStroke;
        mPowerRect.top = getPaddingTop() + mBatteryStroke;
        mPowerRect.right = getPaddingLeft() + mBatteryStroke + mPowerWidth * mPower / 100f;
        mPowerRect.bottom = getPaddingTop() + mBatteryStroke + mPowerHeight;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) width;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) (width * defaultRatio);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawRoundRect(mBatteryRect, batteryArch, batteryArch, mBatteryPaint); // 画电池轮廓需要考虑 画笔的宽度
        canvas.drawRoundRect(mCapRect, batteryArch, batteryArch, mBatteryPaint);// 画电池盖
        canvas.drawRect(mPowerRect, mPowerPaint);// 画电量
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width * defaultRatio > height) {
            measureWidth = (int) (height / defaultRatio);
            measureHeigth = height;
        }
        else {
            measureWidth = width;
            measureHeigth = (int) (width * defaultRatio);
        }
        intDrawRectF();
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.min(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    /**]
     * @category 设置电池电量
     * @param power
     */
    public void setPower(float power) {
        mPower = power;
        if (mPower < 0) {
            mPower = 0;
        }
        if (mPower < defaultWarnValue) {
            mPowerPaint.setColor(mWarnColor);
        }
        else {
            mPowerPaint.setColor(mPowerColor);
        }
        mPowerRect.right = getPaddingLeft() + mBatteryStroke + mPowerWidth * mPower / 100f;
        invalidate();
    }
}
