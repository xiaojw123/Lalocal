package com.lalocal.lalocal.live.im.ui.barrage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.live.base.util.ScreenUtil;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * 弹幕容器
 */
public class BarrageViewTest extends RelativeLayout {

    private static final String TAG = "BarrageView";

    private static final int DEFAULT_RANDOM_COLOR_NUM = 10;

    private static final boolean OUTPUT_LOG = true;

    private Random random;

    // 配置管理
    private BarrageConfig config;

    // 轨道管理
    private Set<Integer> linesUnavailable = new HashSet<>();
    private Queue<SoftReference<LinearLayout>> textViewCache = new LinkedList<>();
    private int lineCount;
    private int lineHeight;

    // 字幕管理
    private Queue<String> textCache = new LinkedList<>();


    public BarrageViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarrageViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void init(final BarrageConfig config) {
        this.random = new Random();
        int totalLineHeight = getBottom() - getTop() - getPaddingTop() - getPaddingBottom();
        this.lineHeight = ScreenUtil.sp2px(config.getMaxTextSizeSp());
        this.lineCount = totalLineHeight / lineHeight;
        AppLog.i("TAG","lineCount;"+lineCount+"getBottom:"+getBottom()+"getTop:"+getTop()+"getPaddingTop:"+getPaddingTop()+"getPaddingBottom:"+getPaddingBottom());
        // random colors
        if (config.getColors() == null || config.getColors().isEmpty()) {
            List<Integer> colors = new ArrayList<>(DEFAULT_RANDOM_COLOR_NUM);
            for (int i = 0; i < DEFAULT_RANDOM_COLOR_NUM; i++) {
                colors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            }
            config.setColors(colors);
        }
        this.config = config;

        log("barrage init, lineHeight=" + lineHeight + ", lineCount=" + lineCount);
    }

    public void addTextBarrage(String text) {
        textCache.add(text);
        checkAndRunTextBarrage();
    }

    private void checkAndRunTextBarrage() {
        if (textCache.isEmpty()) {
            return;
        }

        // line
        int availableLine = getAvailableLine();
        if (availableLine < 0) {
            AppLog.i("TAG","走了这里availableLine");
            return; // pend
        }

        // find cached text view

        LinearLayout linearLayout=null;
        SoftReference<LinearLayout> softReference;
        while (true) {
            softReference = textViewCache.poll();
            if (softReference == null || (linearLayout = softReference.get()) != null) {
                break; // queue is empty or find available cached object
            }
        }


        if (linearLayout == null) {
            linearLayout = buildTextView(textCache.poll(), availableLine);
        } else {
            linearLayout = reuseTextView(linearLayout, textCache.poll(), availableLine);
        }

        buildTranslationAnimator(linearLayout, availableLine).start();
    }

    private LinearLayout buildTextView(String text, int line) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        String[] value = text.split(":");
        String name = value[0];
        String content = value[1];
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // text content
        TextView  textViewName = new TextView(getContext());
        textViewName.setText(name+" : ");
        textViewName.setSingleLine();
        textViewName.setTextSize(14);
        textViewName.setTextColor(Color.parseColor("#ffaa2a"));
        TextView   textViewContent = new TextView((getContext()));
        textViewContent.setText(content);
        textViewContent.setSingleLine();
        textViewContent.setTextSize(14);
        textViewContent.setTextColor(Color.parseColor("#ffffff"));
        linearLayout.addView(textViewName);
        linearLayout.addView(textViewContent);
        // layout param
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = line * lineHeight;
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setBackgroundColor(Color.parseColor("#73000000"));

        addView(linearLayout);

        return linearLayout;
    }

    private LinearLayout reuseTextView(LinearLayout linearLayout, String text, int line) {
        String[] value = text.split(":");
        String name = value[0];
        String content = value[1];
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // text content
        TextView  textViewName = new TextView(getContext());
        textViewName.setText(name+" : ");
        textViewName.setSingleLine();
        textViewName.setTextSize(14);
        textViewName.setTextColor(Color.parseColor("#ffaa2a"));
        TextView   textViewContent = new TextView((getContext()));
        textViewContent.setText(content);
        textViewContent.setSingleLine();
        textViewContent.setTextSize(14);
        textViewContent.setTextColor(Color.parseColor("#ffffff"));
        linearLayout.addView(textViewName);
        linearLayout.addView(textViewContent);
        LayoutParams params = (LayoutParams) linearLayout.getLayoutParams();
        params.topMargin = line * lineHeight;
        linearLayout.setLayoutParams(params);

        // re add view to container
        addView(linearLayout);

        // log
        log("reuse text barrage");

        return linearLayout;
    }

    private int getAvailableLine() {
        int line = -1;
        for (int i = 0; i < lineCount; i++) {
            if (!linesUnavailable.contains(i)) {
                line = i;
                break;
            }
        }

        if (line >= 0 && line < lineCount) {
            linesUnavailable.add(line); // 占用
        }

        return line;
    }

    private ObjectAnimator buildTranslationAnimator(final LinearLayout target, final int line) {

        final int duration = config.getDuration() + random.nextInt() % 500;
        final int textLength =  target.getWidth();
        final int freeLineDuration = (int) ((textLength + 50.0f) / (1.0f * getWidth() / duration));

        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationX", getWidth(), -textLength).setDuration(duration);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                log("text barrage run, line=" + line + ", duration=" + duration + ", freeLineDuration=" + freeLineDuration);

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLineAvailable(line);
                    }
                }, freeLineDuration);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onTextBarrageDone(target, line);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onTextBarrageDone(target, line);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private void onLineAvailable(final int line) {
        log("free line, line=" + line);

        linesUnavailable.remove(line);
        checkAndRunTextBarrage();
    }

    private void onTextBarrageDone(final LinearLayout view, final int line) {
        log("text barrage completed, line=" + line);

        // should remove strong reference
        removeView(view);

        // add to cache for reuse
        textViewCache.add(new SoftReference<>(view));

        checkAndRunTextBarrage();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (View.GONE == visibility) {

        } else {

        }
    }

    private void log(String message) {
        if (OUTPUT_LOG) {
            Log.i(TAG, message);
        }
    }
}
