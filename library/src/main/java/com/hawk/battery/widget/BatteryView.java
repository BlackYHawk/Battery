package com.hawk.battery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lan on 2018-02-02.
 */

public class BatteryView extends View {
    /**
     * 默认参数
     */
    private static final float defaultRatio = 7f/13;                  // 电池默认高宽比例
    private static final float defaultHightRatio = 2f;               // 电池与电池盖默认高比例
    private static final float defaultWidthRatio = 9f;               // 电池与电池盖默认宽比例
    private static final float defaultHeight = 35f;                  // 视图的默认高度
    private static final float defaultWidth = 65f;                   // 视图的默认宽度
    private static final float defaultStroke = 3f;                   // 画笔的默认厚度
    /**
     * 画笔信息
     */
    private Paint mBatteryPaint;
    private Paint mPowerPaint;
    /**
     * 视图高宽
     */
    private int measureWidth;
    private int measureHeigth;
    /**
     * 电池参数
     */
    private float mBatteryHeight = defaultHeight;                       // 电池的高度
    private float mBatteryWidth = defaultWidth * defaultWidthRatio / (defaultWidthRatio + 1); // 电池的宽度
    private float mCapHeight = defaultHeight / defaultHightRatio;    // 电池盖的高度
    private float mCapWidth = defaultWidth / (defaultWidthRatio + 1);       // 电池盖的宽度
    private float mBatteryStroke = defaultStroke;         // 画笔厚度
    /**
     * 电池电量
     */
    private float mPowerPadding = 1;
    private float mPowerHeight = 0;             // 电量的总高度
    private float mPowerWidth = 0;              // 电量的总宽度
    private float mPower = 80f;                  //当前电量
    /**
     * 矩形
     */
    private RectF mBatteryRect = new RectF(0, 0, 0, 0);
    private RectF mCapRect = new RectF(0, 0, 0, 0);
    private RectF mPowerRect = new RectF(0, 0, 0, 0);

    public BatteryView(Context context) {
        super(context);
        initView();
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        /**
         * 设置电池画笔
         */
        mBatteryPaint = new Paint();
        mBatteryPaint.setColor(Color.GRAY);
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(mBatteryStroke);
        /**
         * 设置电量画笔
         */
        mPowerPaint = new Paint();
        mPowerPaint.setColor(Color.RED);
        mPowerPaint.setAntiAlias(true);
        mPowerPaint.setStyle(Paint.Style.FILL);
        mPowerPaint.setStrokeWidth(mBatteryStroke);
    }

    private void intDrawRectF() {
        mBatteryHeight = measureHeigth;                       // 电池的高度
        mBatteryWidth = measureWidth * defaultWidthRatio / (defaultWidthRatio + 1); // 电池的宽度
        mCapHeight = measureHeigth / defaultHightRatio;    // 电池盖的高度
        mCapWidth = measureWidth / (defaultWidthRatio + 1);       // 电池盖的宽度

        mPowerHeight = mBatteryHeight - mBatteryStroke - mPowerPadding * 2; // 电池身体的高度
        mPowerWidth = mBatteryWidth - mBatteryStroke - mPowerPadding * 2;// 电池身体的总宽度
        /**
         * 设置电池矩形
         */
        mBatteryRect.left = getPaddingLeft();
        mBatteryRect.top = 0;
        mBatteryRect.right = (getWidth() - getPaddingRight()) + mPowerWidth;
        mBatteryRect.bottom = mBatteryHeight;
        /**
         * 设置电池盖矩形
         */
        mCapRect.left = mBatteryRect.right - mBatteryRect.left;
        mCapRect.top = (mBatteryHeight - mCapHeight) / 2;
        mCapRect.right = mBatteryRect.right - mBatteryRect.left + mCapWidth;
        mCapRect.bottom = (mBatteryHeight - mCapHeight) / 2 + mCapHeight;
        /**
         * 设置电量矩形
         */
        mPowerRect.left = mBatteryStroke / 2 + mPowerPadding
                + mPowerWidth * ((100f - mPower) / 100f);
        mPowerRect.top = mPowerPadding + mBatteryStroke / 2;
        mPowerRect.right = mBatteryStroke / 2 + mPowerPadding
                + mPowerWidth * ((100f - mPower) / 100f);
        mPowerRect.bottom = mBatteryStroke / 2 + mPowerPadding + mPowerHeight;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mBatteryWidth;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) mBatteryHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
    //    canvas.translate(measureWidth / 2, measureHeigth / 2);
        canvas.drawRoundRect(mBatteryRect, 2f, 2f, mBatteryPaint); // 画电池轮廓需要考虑 画笔的宽度
        canvas.drawRoundRect(mCapRect, 2f, 2f, mBatteryPaint);// 画电池盖
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
        mPowerRect.right = mBatteryStroke / 2 + mPowerPadding
                + mPowerWidth * ((100f - mPower) / 100f);
        invalidate();
    }
}
