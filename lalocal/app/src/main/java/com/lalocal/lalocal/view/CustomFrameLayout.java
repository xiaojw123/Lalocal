package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by xiaojw on 2017/1/6.
 */

public class CustomFrameLayout extends FrameLayout {
    ViewHelperListener listner;

    public CustomFrameLayout(Context context) {
        super(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnViewHeplerListner(ViewHelperListener listner) {
        this.listner = listner;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (listner!=null){
            listner.onVisibilityChanged(visibility);
        }
    }

    public interface ViewHelperListener {
        void onVisibilityChanged(int visibility);
    }

}
