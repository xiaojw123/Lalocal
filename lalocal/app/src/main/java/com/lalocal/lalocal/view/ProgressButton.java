package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;


public class ProgressButton extends ImageButton {

    private boolean _shouldDisplayLoadingAnimation = false;
    private Drawable _loadingAnimation;
    private TextPaint _textPaint;
    private Rect _textBounds;
    private String _defaultText;
    int defaultColor = -1;

    /**
     * {@link ProgressButton} can be used to display a simple rotating {@link Drawable} to give the user
     * the effect of a loading button. The {@link Drawable} will be displayed once the user clicks the button and will have to be
     * manually dismissed using the {@link #stopLoadingAnimation()} method.
     *
     * @param context_  the current {@link Context}
     * @param attrs_    the {@link AttributeSet} to retrieve data from compiled xml files
     * @param defStyle_ the default style to apply
     */
    public ProgressButton(Context context_, AttributeSet attrs_, int defStyle_) {
        super(context_, attrs_, defStyle_);
//        TypedArray a = context_.obtainStyledAttributes(attrs_, R.styleable.ProgressButton,
//                R.attr.progressButtonStyle, defStyle_);
        final TypedArray a = context_.obtainStyledAttributes(
                attrs_, R.styleable.ProgressButton, defStyle_, 0);
        boolean isOrange = a.getBoolean(R.styleable.ProgressButton_isOrangeBg, true);
        int textSize = (int) a.getDimension(R.styleable.ProgressButton_pb_TextSize, getResources().getDimension(R.dimen.text_size_18_sp));
        int bgColor = a.getColor(R.styleable.ProgressButton_pb_BackgroundColor, defaultColor);
        if (isOrange) {
            if (bgColor == defaultColor) {
                setBackgroundDrawable(getResources().getDrawable(R.drawable.orange_corner_btn_bg));
            } else {
                setBackgroundColor(bgColor);
            }
        } else {
            setBackgroundColor(getResources().getColor(R.color.black_99));
        }
        int padding = (int) getResources().getDimension(R.dimen.dimen_size_13_dp);
        setPadding(padding, padding, padding, padding);
        setImageResource(R.drawable.progress_view);
//        <item name="android:layout_width">match_parent</item>
//        <item name="android:layout_height">@dimen/dimen_size_50_dp</item>
//        <item name="android:clickable">true</item>
//        <item name="android:src">@drawable/progress_view</item>
//        <item name="android:padding">@dimen/text_size_16_sp</item>


//        <style name="LoginNextStyle">
//        <item name="android:layout_width">@dimen/login_phone_edit_width</item>
//        <item name="android:layout_height">wrap_content</item>
//        <item name="android:layout_marginTop">@dimen/dimen_size_10_dp</item>
//        <item name="android:background">@color/black_99</item>
//        <item name="android:textColor">@color/color_f0a028</item>
//        <item name="android:textSize">@dimen/text_size_18_sp</item>
//        </style>
        _loadingAnimation = getDrawable();
        _loadingAnimation.setAlpha(0);
        _defaultText = a.getString(R.styleable.ProgressButton_pb_text);
        _textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        _textPaint.density = getResources().getDisplayMetrics().density;
        if (isOrange) {
            _textPaint.setColor(getResources().getColor(R.color.white));
        } else {
            _textPaint.setColor(getResources().getColor(R.color.color_f0a028));
        }
        _textPaint.setTextAlign(Align.CENTER);
//        _textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_18_sp));
        _textPaint.setTextSize(textSize);
//        _textPaint.setFakeBoldText(true);
        _textBounds = new Rect();

        a.recycle();
    }

    public ProgressButton(Context context_, AttributeSet attrs_) {
        this(context_, attrs_, 0);
    }

    public ProgressButton(Context context_) {
        this(context_, null);
    }

    /**
     * Ensures that the loading animation will be displayed when the user clicks the button
     */
//    @Override
//    public boolean performClick() {
//        boolean isClicked = super.performClick();
//        AppLog.print("performClick____isClicked__" + isClicked);
//        if (isClicked) {
//            _shouldDisplayLoadingAnimation = true;
//            this.invalidate();
//        }
//
//        return isClicked;
//    }

    ;

    public void setText(String text) {
        _defaultText = text;
        this.invalidate();
    }


    public void startLoadingAnimation() {
        _shouldDisplayLoadingAnimation = true;
        this.invalidate();
    }

    public void stopLoadingAnimation() {
        _shouldDisplayLoadingAnimation = false;
        this.invalidate();
    }

    /**
     * Display a loading animation if the user has clicked the button or hide it if {@link #stopLoadingAnimation()}
     * has been called.
     */
    @Override
    protected void onDraw(Canvas canvas_) {
        if (_shouldDisplayLoadingAnimation) {
            shouldShowAnimation(true);
        } else {
            if (!TextUtils.isEmpty(_defaultText)) {
                _textPaint.getTextBounds(_defaultText, 0, _defaultText.length(), _textBounds);
                canvas_.drawText(_defaultText, getWidth() / 2, (getHeight() / 2) + ((_textBounds.bottom - _textBounds.top) / 2), _textPaint);
            }
            shouldShowAnimation(false);
            _loadingAnimation.setVisible(false, false);
        }
        super.onDraw(canvas_);

    }

    /**
     * Start or stop the current {@link Animatable} instance
     *
     * @param shouldShow_ to indicate whether {@link Animatable#stop()} or
     *                    {@link Animatable#start()} should be invoked on the current {@link Animatable} instance
     */
    private void shouldShowAnimation(boolean shouldShow_) {
        AppLog.print("shouldShowAnimation____");
        if (_loadingAnimation instanceof Animatable) {
            if (shouldShow_) {
                _loadingAnimation.setAlpha(255);
                AppLog.print("Animation__start__");
                ((Animatable) _loadingAnimation).start();
            } else {
                _loadingAnimation.setAlpha(0);
                AppLog.print("Animation__stop__");
                ((Animatable) _loadingAnimation).stop();
            }
        }
    }

}
