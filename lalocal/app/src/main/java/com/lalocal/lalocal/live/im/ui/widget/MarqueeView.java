package com.lalocal.lalocal.live.im.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by android on 2016/11/15.
 * 聊天室消息通知滚动效果
 */
public class MarqueeView extends ViewFlipper {
    private Context mContext;
    private List<String> notices;
    private boolean isSetAnimDuration = false;
    private int interval = 1000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;
    private Queue<String> content;
    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if(content==null){
            content = new LinkedList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DensityUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        typedArray.recycle();

        setFlipInterval(interval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }
    public void start(String conten) {
        content.add(conten);
        showRoll();
    }

    boolean isRoll=true;
    private void showRoll() {
        if(isRoll){
            String poll = content.poll();
            if(poll!=null){
                isRoll=false;
                removeAllViews();
                addView(createTextView(poll));

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }, 1000);
            }else{
                stopFlipping();

                AppLog.i("TAG","消息轮播停止了");
            }
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                isRoll=true;
                showRoll();
            }
        }
    };

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text) {

        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        tv.setText(text);
        if(text.equals("欢迎你进入直播间")||text.equals("登陆聊天室成功...")){
            tv.setTextColor(Color.parseColor("#e4f6cf4e"));
        }else{
            tv.setTextColor(textColor);
        }
        tv.setTextSize(textSize);
        return tv;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }
}
