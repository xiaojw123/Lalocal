package com.lalocal.lalocal.live.im.ui.barrage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.ScreenUtil;
import com.lalocal.lalocal.live.im.session.BarrageViewBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 弹幕容器
 */
public class BarrageView extends RelativeLayout{

    private static final String TAG = "BarrageView";
    private static final int DEFAULT_RANDOM_COLOR_NUM = 10;
    private static final boolean OUTPUT_LOG = true;
    private  int padding;

    private Random random;

    // 配置管理
    private BarrageConfig config;

    // 轨道管理
    private Set<Integer> linesUnavailable = new HashSet<>();
    private Queue<SoftReference<LinearLayout>> textViewCache = new LinkedList<>();
    private int lineCount;
    private int lineHeight;

    // 字幕管理
    private Queue<BarrageViewBean> textCache = new LinkedList<>();
    private BarrageViewBean poll;

    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(final BarrageConfig config) {
        this.random = new Random();
        int totalLineHeight = getBottom() - getTop() - getPaddingTop() - getPaddingBottom();
        this.lineHeight = ScreenUtil.sp2px(config.getMaxTextSizeSp());
        this.lineCount = totalLineHeight / lineHeight;
        this.padding= DensityUtil.dip2px(getContext(), 3);
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

    public void addTextBarrage(BarrageViewBean text) {
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
            return; // pend
        }

        LinearLayout linearLayout=null;
        SoftReference<LinearLayout> softReference;
        while (true) {
            softReference = textViewCache.poll();
            if (softReference == null || (linearLayout = softReference.get()) != null) {
                break; // queue is empty or find available cached object
            }
        }
        // create text view
        poll = textCache.poll();
        if (linearLayout == null) {
            linearLayout = buildTextView(poll, availableLine);

        } else {
            linearLayout = reuseTextView(linearLayout, poll, availableLine);
        }

        // run
        buildTranslationAnimator(linearLayout, availableLine,poll).start();
    }

    private LinearLayout buildTextView(final BarrageViewBean barrageBean, int line) {
        if (TextUtils.isEmpty(barrageBean.getContent())) {
            return null;
        }
        LinearLayout linearLayoutParent=new LinearLayout(getContext());
        linearLayoutParent.setOrientation(LinearLayout.HORIZONTAL);
        TextView textNull=new TextView(getContext());
        textNull.setText("     ");

        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setPadding(padding,padding,padding,padding);
        linearLayout.setBackgroundResource(R.drawable.barrage_view_bg);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);


        linearLayoutParent.addView(linearLayout);
        linearLayoutParent.addView(textNull);
        CircleImageView imageView=new CircleImageView(getContext());
        if(barrageBean.getAvator()!=null){
            DrawableUtils.displayImg(getContext(),imageView,barrageBean.getAvator());
        }else {
            imageView.setImageResource(R.drawable.androidloading);
        }

        linearLayout.addView(imageView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height= DensityUtil.dip2px(getContext(),22);
        layoutParams.width=DensityUtil.dip2px(getContext(),22);
        layoutParams.gravity= Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        TextView textName = new TextView(getContext());
        textName.setText(barrageBean.getSenderName());
        textName.setSingleLine();
        textName.setMaxEms(7);
        textName.setTextColor(Color.parseColor("#ffaa2a"));
        textName.setPadding(padding,0,padding,0);
        textName.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textName);

        TextView textContent = new TextView(getContext());
        textContent.setText(barrageBean.getContent());
        textContent.setSingleLine();
        textContent.setMaxEms(30);
        textContent.setTextColor(Color.WHITE);
        textContent.setPadding(padding,0,padding,0);
        textContent.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textContent);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = line * lineHeight;
        linearLayoutParent.setLayoutParams(params);

        addView(linearLayoutParent);

        return linearLayoutParent;
    }

    private LinearLayout reuseTextView(LinearLayout  linearLayoutParent,final BarrageViewBean barrageBean, int line) {
        linearLayoutParent.removeAllViews();
        CircleImageView imageView=new CircleImageView(getContext());
        if(barrageBean.getAvator()!=null){
            DrawableUtils.displayImg(getContext(),imageView,barrageBean.getAvator());
        }else{
            imageView.setImageResource(R.drawable.androidloading);
        }
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textNull=new TextView(getContext());
        textNull.setText("     ");
        linearLayoutParent.addView(linearLayout);
        linearLayoutParent.addView(textNull);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(padding,padding,padding,padding);
        linearLayout.setBackgroundResource(R.drawable.barrage_view_bg);
        linearLayout.addView(imageView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height=DensityUtil.dip2px(getContext(),22);
        layoutParams.width=DensityUtil.dip2px(getContext(),22);
        layoutParams.gravity=Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);

        TextView textName = new TextView(getContext());
        textName.setText(barrageBean.getSenderName());
        textName.setSingleLine();
        textName.setMaxEms(7);
        textName.setTextColor(Color.parseColor("#ffaa2a"));
        textName.setPadding(padding,0,padding,0);
        textName.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textName);

        TextView textContent = new TextView(getContext());
        textContent.setText(barrageBean.getContent());
        textContent.setSingleLine();
        textContent.setMaxEms(30);
        textContent.setTextColor(Color.WHITE);
        textContent.setPadding(padding,0,padding,0);
        textContent.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textContent);
        LayoutParams params = (LayoutParams) linearLayoutParent.getLayoutParams();
        params.topMargin = line * lineHeight;
        linearLayoutParent.setLayoutParams(params);
        addView(linearLayoutParent);
        // log
        log("reuse text barrage");
        return linearLayoutParent;
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

    private  BarrageViewBean barrageBean;
    private ObjectAnimator buildTranslationAnimator(final LinearLayout target, final int line,final BarrageViewBean barrageBean) {
        target.measure(0,0);
        this.barrageBean=barrageBean;
        final int textLength =target.getMeasuredWidth();
        final int duration = config.getDuration() +( random.nextInt() % 500);
        final int freeLineDuration = (int) ((textLength + 50.0f) / (1.0f * getWidth() / duration));
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationX", getWidth(), -textLength).setDuration(duration);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                AppLog.i("TAG","text barrage run, line=" + line + ", duration=" + duration + ", freeLineDuration=" + freeLineDuration);
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
        target.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBarrageClickListener!=null){
                    onBarrageClickListener.getUserId(barrageBean.getUserId());
                }
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
        AppLog.i("TAG","弹幕动画结束，移除view");
        removeView(view);
        textViewCache.add(new SoftReference<>(view));

        checkAndRunTextBarrage();
    }


    private void log(String message) {
        if (OUTPUT_LOG) {
            Log.i(TAG, message);
        }
    }

    private OnBarrageClickListener onBarrageClickListener;

    public interface OnBarrageClickListener {
        void getUserId(String userId);
    }

    public void setOnBarrageClickListener( OnBarrageClickListener onBarrageClickListener) {
        this.onBarrageClickListener = onBarrageClickListener;
    }
}
