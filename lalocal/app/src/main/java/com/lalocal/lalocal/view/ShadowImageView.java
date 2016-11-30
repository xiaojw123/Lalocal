package com.lalocal.lalocal.view;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;

/**
 * Created by wangjie on 2016/11/4.
 */
public class ShadowImageView extends ImageView {

    // 默认阴影颜色
    private static final int DEFAULT_SHADOW_COLOR = 0x000000;

    private Paint mPaint;

    // 阴影宽度
    private int mShadowWidth;
    // 阴影颜色
    private int mShadowColor = 0x000000;
    // 阴影透明度
    private float mShadowAlpha = 1;

    // 添加上阴影后的padding
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;

    // 原始padding
    private int mOrgPaddingRight = 0;
    private int mOrgPaddingBottom = 0;

    public ShadowImageView(Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 默认阴影宽度
        int defaultWidth = DensityUtil.dip2px(getContext(), 2);
        mShadowWidth = defaultWidth;

        // 获取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyShadow);
        this.mShadowWidth = (int) ta.getDimension(R.styleable.MyShadow_shadow_width, mShadowWidth);
        this.mShadowColor = ta.getColor(R.styleable.MyShadow_shadow_color, DEFAULT_SHADOW_COLOR);
        this.mShadowAlpha = ta.getFloat(R.styleable.MyShadow_shadow_alpha, 1.0f);

        // 获取默认的padding设置
        mOrgPaddingRight = this.getPaddingRight();
        mOrgPaddingBottom = this.getPaddingBottom();

        // 添加阴影后的边距
        mPaddingRight = mOrgPaddingRight + mShadowWidth;
        mPaddingBottom = mOrgPaddingBottom + mShadowWidth;

        // 重新设置padding，使之适应阴影宽度
        this.setPadding(getPaddingLeft(), getPaddingTop(), mPaddingRight, mPaddingBottom);
        AppLog.i("shj", "setPadding:" + getPaddingLeft() + "--" + getPaddingTop() + "--" + mPaddingRight + "--" + mPaddingBottom);
    }

    @Override
    public int getPaddingBottom() {
        return mOrgPaddingBottom;
    }

    @Override
    public int getPaddingRight() {
        return mOrgPaddingRight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

        AppLog.i("shj", "mShadowWidth--" + mShadowWidth + ";mShadowColor--" + mShadowColor + ";mShadowAlpha--" + mShadowAlpha);


        // 绘制阴影
        drawShadow(canvas, mPaint, mShadowWidth, mShadowColor, mShadowAlpha);
    }

    /**
     *
     * @param canvas
     * @param paint 0~1
     * @param shadowWidth
     * @param shadowColor
     * @param alpha
     */
    private void drawShadow(Canvas canvas, Paint paint, int shadowWidth, int shadowColor, float alpha) {
        AppLog.i("shj", "drawShadow()");
        // 画布的alpha
        int painAlpha = (int) (255 * alpha);
        AppLog.i("shj", "painAlpha--" + painAlpha);
        // 设置画笔颜色
        paint.setColor(shadowColor);
        AppLog.i("shj", "setColor" + shadowColor);
        // 使用drawLine绘制阴影
        for (int i = 1; i <= shadowWidth; i++) {
            AppLog.i("shj", "i--" + i);
            paint.setAlpha(painAlpha / shadowWidth * (shadowWidth - i));
            // 竖直方向
            canvas.drawLine(canvas.getWidth() - getPaddingRight() + i,
                    getPaddingTop() + i,
                    canvas.getWidth() - getPaddingRight() + i,
                    canvas.getHeight() - getPaddingBottom()+ i,
                    paint);
            // 水平方向
            canvas.drawLine(getPaddingLeft() + i,
                    canvas.getHeight() - getPaddingBottom() + i,
                    canvas.getWidth() - getPaddingRight() + i,
                    canvas.getHeight() - getPaddingBottom() + i,
                    paint);
        }
    }

    /**
     * 阴影宽度
     * @param width
     */
    public void setShadowWidth(int width) {
        this.mShadowWidth = width;

        this.invalidate();
    }

    /**
     * 设置阴影颜色
     * @param color
     */
    public void setShadowColor(int color) {
        this.mShadowColor = color;

        this.invalidate();
    }

    /**
     * 设置阴影透明度
     * @param alpha
     */
    public void setShadowAlpha(float alpha) {
        this.mShadowAlpha = alpha;

        this.invalidate();
    }
}
