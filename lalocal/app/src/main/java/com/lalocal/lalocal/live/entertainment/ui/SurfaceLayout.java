package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by android on 2016/9/18.
 */
public class SurfaceLayout extends FrameLayout {

    private int mL,mT,mR,mB;
    private boolean isFist = true;

    public SurfaceLayout(Context context) {
        super(context);
    }

    public SurfaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SurfaceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isFist){
            mL = l;
            mT=t;
            mR=r;
            mB=b;
            isFist =false;
        }
        getChildAt(0).layout(mL, mT, mR, mB);
    }
}

