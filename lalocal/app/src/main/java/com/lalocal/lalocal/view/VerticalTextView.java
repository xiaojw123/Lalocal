package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;

/**
 * Created by wangjie on 2016/10/18.
 */
public class VerticalTextView extends View {

    // -默认值
    // 默认字体大小: 12sp
    private static final float DEFAULT_TEXT_SIZE = 12;
    // 默认字体颜色：#FF212121
    private static final int DEFAULT_TEXT_COLOR = 0xFF212121;
    // 默认字间距
    private static final int DEFAULT_LETTER_SPACING = 0;
    // 默认行倍数
    private static final float DEFAULT_LINE_SPACING_MULTIPLIER = 0.5f;
    // 默认文本内容
    private static final String DEFAULT_TEXT = "";
    // 默认最大行数
    private static final int DEFAULT_MAX_LINES = Integer.MAX_VALUE;
    // 其他默认
    private static final int DEFAULT_GLOBAL = Integer.MIN_VALUE;

    // -位置
    // 笔刷所在位置 X
    private int mPosX = DEFAULT_GLOBAL;
    // 笔刷所在位置 Y
    private int mPosY = DEFAULT_GLOBAL;
    // 初始y轴位置
    private static final int INTIAL_Y = 0;

    // -文本
    // 文本内容
    private String mText = DEFAULT_TEXT;
    // 字体大小
    private float mTextSize = DEFAULT_TEXT_SIZE;
    // 字体颜色
    private int mTextColor = DEFAULT_TEXT_COLOR;
    // 最大字符数
    private int mMaxLength = DEFAULT_GLOBAL;

    // -间距
    // 字间距
    private float mCharSpacing = DEFAULT_LETTER_SPACING;
    // 行间距尺寸
    private float mLineSpacingExtra = DEFAULT_GLOBAL;
    // 行间距倍数
    private float mLineSpacingMultiplier = DEFAULT_LINE_SPACING_MULTIPLIER;

    // -整体
    // 视图宽度 px
    private float mGlobalWidth = DEFAULT_GLOBAL;
    // 视图高度 px
    private float mGlobalHeight = DEFAULT_GLOBAL;
    // 最大行数
    private int mMaxLines = DEFAULT_MAX_LINES;

    // -非对外属性变量
    // 单个文字尺寸
    private int mTextDimen;
    // 一行显示的字符数
    private int mLengthPerLine = DEFAULT_GLOBAL;
    // 临时行号
    private int mTmpLines;
    // 临时宽度
    private float mTmpWidth;

    // -工具
    // 绘制文本的笔刷
    private Paint mPaint;
    // 矩阵
    private Matrix mMatrix;
    // 背景
    private BitmapDrawable mBackground;

    private boolean isFirst = true;

    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // -获取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        mText = ta.getString(R.styleable.VerticalTextView_text);
        mTextColor = ta.getColor(R.styleable.VerticalTextView_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = ta.getDimension(R.styleable.VerticalTextView_textSize, DEFAULT_TEXT_SIZE);
        mLineSpacingMultiplier = ta.getFloat(R.styleable.VerticalTextView_lineSpacingMultiplier, DEFAULT_LINE_SPACING_MULTIPLIER);
        mLineSpacingExtra = ta.getDimension(R.styleable.VerticalTextView_lineSpacingExtra, DEFAULT_GLOBAL);
        mMaxLines = ta.getInteger(R.styleable.VerticalTextView_maxLines, DEFAULT_MAX_LINES);


        //初始化
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        // 初始化工具
        initTools();
    }

    /**
     * 初始化工具
     */
    private void initTools() {
        // 初始化矩阵
        mMatrix = new Matrix();
        // 初始化背景
        mBackground = (BitmapDrawable) getBackground();
        //初始化笔刷
        mPaint = new Paint();
        // 文字居中
//        mPaint.setTextAlign(Paint.Align.CENTER);
        // 去锯齿
        mPaint.setAntiAlias(true);
    }

    /**
     * Very Important Method: 同步参数
     */
    private void syncParams() {
        // 设置笔刷颜色
        mPaint.setColor(mTextColor);
        
        // 计算单个字符覆盖边长
        measureCharDimen();

        // 计算行间距
        measureLineSpacing();

        // 计算文本宽度
        mesureWidth();

    }

    /**
     * 计算行间距
     */
    private void measureLineSpacing() {
        // 如果行间距未设定，则以行间距倍数来计算
        if (mLineSpacingExtra == DEFAULT_GLOBAL) {
            mLineSpacingExtra = mLineSpacingMultiplier * mTextDimen;
        }
    }

    /**
     * 以汉字“汉”为例，计算一个汉字覆盖的边长
     */
    private void measureCharDimen() {
        // 设置字体大小
        mPaint.setTextSize(mTextSize);
        // 初始化字体矩阵
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        // 获取字体高度
        mTextDimen = (int) (fontMetrics.descent - fontMetrics.ascent);
    }

    /**
     * 计算宽度
     *
     * @return
     */
    private void mesureWidth() {
        if (TextUtils.isEmpty(mText)) {
            // 临时标记文本宽度
            mTmpWidth = 0;
            mTmpLines = 0;
            return;
        }
        // 初始化第一行文本的宽度
        mTmpWidth = mTextDimen + mLineSpacingExtra;
        // 临时标记行数
        mTmpLines = 0;
        // 如果高度未设置，则说明高度是wrap_content
        if (mGlobalHeight == DEFAULT_GLOBAL) {

            // -此时文本的高度取决于换行符的个数
            // 计算换行符的个数
            int num = containNum(mText, "\n");
            // 计算行号
            mTmpLines = num + 1;
            // 计算文本宽度
            mTmpWidth = (mTextDimen + mLineSpacingExtra) * mTmpLines;
        } else {
            // 如果高度有设定，则预先试试每个字排版，测得宽度，先不考虑最大行数和宽度
            // 计算每一列可以排版的字数
            mLengthPerLine = (int) ((mGlobalHeight - 0) / mTextDimen); // 2 * INTIAL_Y
            // 每行的第j个字，标记
            int j = 0;
            // 遍历文本内容
            for (int i = 0; i < mText.length(); i++) {
                // 获取单字符
                char ch = mText.charAt(i);
                // 如果遇到换行符
                if (ch == '\n') {
                    // 且不是第一个字符就是换行符
                    if (j > 0) {
                        // 重置标记，改行第0个字
                        j = 0;
                        // 宽度增加(一个文字宽度+行间距)
                        mTmpWidth += mTextDimen + mLineSpacingExtra;
                        // 行号+1
                        mTmpLines++;
                    }
                } else {
                    // 新增加一个字
                    j++;
                    // 如果该行满了，则换行
                    if (j == mLengthPerLine) {
                        // 换行，重置标记
                        j = 0;
                        // 宽度增加(一个文字宽度+行间距)
                        mTmpWidth += mTextDimen + mLineSpacingExtra;
                        // 行数+1
                        mTmpLines++;
                    }
                }
            }
        }

        // -计算最终可以显示的行数，去最终的文本宽度
        // 与最大行数比较，取较小值
        if (mTmpLines >= mMaxLines) {
            // 如果行数超过最大行数，则以最大行数为准
            mTmpLines = mMaxLines;
            // 计算相应的宽度
            mTmpWidth = (mTextDimen + mLineSpacingExtra) * mTmpLines;
        }

        // 与设定的高度相比较，取较小值
        if (mGlobalWidth > 0) { // 排除match_parent(-1)，match_parent的值在onMeasure中计算
            // 以文字宽度+行高作为一行的宽度，取得完整行宽的行数
            int l = (int) (mGlobalWidth / (mTextDimen + mLineSpacingExtra));
            // 最终剩余的宽度与（文字宽度+一般的行高）比较，即看是否可以再完整显示一行
            float remain = mGlobalWidth % (mTextDimen + mLineSpacingExtra);
            // 如果剩余宽度可以容纳一行文字，则多加一行
            if (remain > mTextDimen + mLineSpacingExtra / 2) {
                l++;
            }

            // 如果行数超过设定的宽度计算得到的行数
            if (mTmpLines > l) {
                // 则以设定宽度下得到的行数为准
                mTmpLines = l;
                // 文本宽度以设定的宽度为准
                mTmpWidth = mGlobalWidth;
            }
        }

        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = (int)mTmpWidth;
        setLayoutParams(lp);
    }

    /**
     * 一个字符串中包含另一个字符串的个数
     *
     * @param container
     * @param str
     * @return
     */
    private int containNum(String container, String str) {
        // 子字符串长度
        int lenth = str.length();
        int index = container.indexOf(str);
        // 如果不存在，则返回0
        if (index == -1) {
            return 0;
        } else {
            // 如果存在，则迭代
            return 1 + containNum(container.substring(index + lenth), str);
        }
    }


    /**
     * 视图大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // 如果高度未设置
        if (this.mGlobalHeight == DEFAULT_GLOBAL) {
            this.mGlobalHeight = height;
        }
        // 如果宽度未设置
        if (this.mGlobalWidth == DEFAULT_GLOBAL) {
            this.mGlobalWidth = width;
        }
    }

    /**
     * 视图渲染
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景
        if (mBackground != null) {
            // 画背景
            Bitmap bitmap = Bitmap.createBitmap(mBackground.getBitmap(),
                    0, 0, mTextDimen, mTextDimen);
            canvas.drawBitmap(bitmap, mMatrix, mPaint);
        }
        // 同步参数
        syncParams();
        // 画文字
        draw(canvas, this.mText);
    }

    /**
     * 画文字
     *
     * @param canvas
     * @param text
     */
    private void draw(Canvas canvas, String text) {

        // 得到最终的宽度
        // TODO: 对MATCH_PARENT、WRAP_CONTENT进行分析
        if (mGlobalWidth < mTmpWidth) {
            mGlobalWidth = mTmpWidth;
        }

        if (mTmpLines >= mMaxLines) {
            // 得到最终行数
            mMaxLines = mTmpLines;
        }

        // 当前字符
        char ch;
        // 当前行号
        int curLine = 1;
        // 行间距的一般
        float halfLineSpacing = mLineSpacingExtra / 2;
        // 初始化笔刷X轴坐标
        mPosX = (int) (mGlobalWidth - mTextDimen); // - halfLineSpacing
        // 初始化笔刷Y轴坐标
        mPosY = INTIAL_Y;

        // -绘制文字
        // 计算字符串长度
        int length = mText.length();
        // 如果高度未设定
        if (mGlobalHeight == DEFAULT_GLOBAL) {
            // 遍历字符串
            for (int i = 0; i < length; i++) {
                // 获取当前字符
                ch = mText.charAt(i);

                // 如果遇到换行符
                if (ch == '\n') {
                    // 笔刷左移
                    mPosX -= (mTextDimen + mLineSpacingExtra);
                    // 高度重置
                    mPosY = INTIAL_Y;
                    // 行号+1
                    curLine++;
                } else {
                    // 绘制当前文字
                    canvas.drawText(String.valueOf(ch), mPosX, mPosY + mTextDimen, mPaint);
                    // 笔刷下移
                    mPosY += mTextDimen;
                }
            }

        } else { // 如果高度设定
            // 当前行的第几个字符
            int j = 0;
            // 遍历字符串
            for (int i = 0; i < length; i++) {
                // 获取当前字符
                ch = mText.charAt(i);
                // 如果遇到换行符
                if (ch == '\n') {
                    // 且当前行有文字内容
                    if (j > 0) {
                        // 行号+1
                        curLine++;
                        // 字符+1
                        j++;
                        // -根据是不是最后一行，判断要不要加省略号
                        // 如果是最后一行
                        if (curLine >= mMaxLines) {
                            /* 换行符不会出现在文本结尾，说明如果遇到换行符，则文本内容没有结束，
                            所以需要绘制省略号
                             */
                            canvas.drawText("...", mPosX, mPosY + mTextDimen, mPaint);
                            // 跳出循环
                            break;
                        } else { // 如果不是最后一行
                            // 重置位置标记为0
                            j = 0;
                            // 笔刷左移
                            mPosX -= (mTextDimen + mLineSpacingExtra);
                            // 高度重置
                            mPosY = INTIAL_Y;
                        }
                    } else { // 不做任何操作
                        // 继续遍历
                    }
                } else { // 如果没有遇到换行符
                    // 新增一个文字
                    j++;
                    // -判断是不是最后一行
                    // 如果是最后一行
                    if (curLine >= mMaxLines) {
                        // -判断有没有到行尾
                        // 如果到行尾
                        if (j == mLengthPerLine) {
                            // -判断文本内容有没有结束
                            // 如果文本内容结束
                            if (i == length - 1) {
                                // 绘制完内容并退出
                                canvas.drawText(String.valueOf(ch), mPosX, mPosY + mTextDimen, mPaint);
                                break;
                            } else { // 如果文本内容没有结束
                                // 绘制省略号并退出
                                canvas.drawText("...", mPosX, mPosY + mTextDimen, mPaint);
                                break;
                            }
                        } else { // 如果不是行尾
                            // 继续绘制
                            canvas.drawText(String.valueOf(ch), mPosX, mPosY + mTextDimen, mPaint);
                            // 笔刷下移
                            mPosY += mTextDimen;
                        }
                    } else { // 如果不是最后一行
                        // -判断有没有达到行尾
                        // 如果到达行尾
                        if (j == mLengthPerLine) {
                            // 绘制文字
                            canvas.drawText(String.valueOf(ch), mPosX, mPosY + mTextDimen, mPaint);
                            // 笔刷左移
                            mPosX -= (mTextDimen + mLineSpacingExtra);
                            // 高度重置
                            mPosY = INTIAL_Y;
                            // 重置位置标记
                            j = 0;
                            // 行号+1
                            curLine++;
                        } else { // 如果未到达行尾
                            // 继续绘制
                            canvas.drawText(String.valueOf(ch), mPosX, mPosY + mTextDimen, mPaint);
                            // 笔刷下移
                            mPosY += mTextDimen;
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置文本内容
     *
     * @param text
     */
    public void setText(String text) {
        if (!TextUtils.equals(text, mText)) {
            this.mText = text;
        }
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        if (mTextColor != color) {
            this.mTextColor = color;
        }
    }

    /**
     * 设置文字大小，单位：px
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        if (mTextSize != textSize && textSize > 0) {
            this.mTextSize = textSize;
        }
    }

    /**
     * 设置文本最大长度
     * 状态：未开放
     * @param length
     */
//    public void setMaxLenth(int length) {
//        if (mMaxLength != length && length > 0) {
//            this.mMaxLength = length;
//        }
//    }

    /**
     * 设置字符间距，单位:dp
     * 状态：未开放
     * @param spacing
     */
//    public void setLetterSpacing(float spacing) {
//        if (mCharSpacing != spacing && spacing > 0) {
//            this.mCharSpacing = spacing;
//        }
//    }

    /**
     * 设置行间距，单位：px
     *
     * @param spacing
     */
    public void setLineSpacingExtra(float spacing) {
        if (mLineSpacingExtra != spacing && spacing > 0) {
            this.mLineSpacingExtra = spacing;
        }
    }

    /**
     * 设置行间距倍数
     *
     * @param multiplier
     */
    public void setLineSpacingMultiplier(float multiplier) {
        if (mLineSpacingMultiplier != multiplier && multiplier > 0) {
            this.mLineSpacingMultiplier = multiplier;
            this.mLineSpacingExtra = DEFAULT_GLOBAL;
        }
    }

    /**
     * 设置视图宽度，单位：px
     *
     * @param width
     */
    public void setWidth(float width) {
        if (mGlobalWidth != width && width > 0) {
            this.mGlobalWidth = width;
        }
        // 刷新视图
        refreshView();
    }

    /**
     * 设置视图高度，单位：px
     *
     * @param height
     */
    public void setHeight(float height) {
        if (mGlobalHeight != height && height > 0) {
            this.mGlobalHeight = height;
        }
        // 刷新视图
        refreshView();
    }

    /**
     * 设置最大行数
     *
     * @param lines
     */
    public void setMaxLines(int lines) {
        if (mMaxLines != lines && lines > 0) {
            this.mMaxLines = lines;
        }
    }

    /**
     * 刷新视图
     */
    public void refreshView() {
        // 设置空间尺寸
        setMeasuredDimension((int)mGlobalWidth, (int)mGlobalHeight);
        // 同步参数
        syncParams();
        // 刷新
        invalidate();
    }

    @Override
    public String toString() {
        String output = "width:" + mGlobalWidth + "; height:" + mGlobalHeight + ";text size:" + mTextSize
                + ";text color:" + mTextColor + ";text dimen:" + mTextDimen + ";max lines:" + mMaxLines
                + ";x:" + mPosX + ";y:" + mPosY + ";length per line:" + mLengthPerLine + ";line spacing:"
                + mLineSpacingExtra + ";text:" + mText;
        return output;
    }
}
