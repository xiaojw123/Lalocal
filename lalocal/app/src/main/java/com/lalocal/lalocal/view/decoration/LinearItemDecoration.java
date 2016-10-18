package com.lalocal.lalocal.view.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lalocal.lalocal.R;

/**
 * Created by yjwfn on 15-9-14.
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration{

    private Paint   mPaint;
    private int     mColor;
    int height;
    int leftMargin;
    Context context;
    public LinearItemDecoration(Context context) {
        height= (int) context.getResources().getDimension(R.dimen.dimen_size_half_dp);
        mPaint = new Paint();
        mPaint.setColor( context.getResources().getColor(R.color.color_de));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }
    public void setLefMargin(int leftMargin){
        this.leftMargin=leftMargin;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager  layoutManager = parent.getLayoutManager();
        View childView ;
        RecyclerView.LayoutParams   layoutParams;
        int childCount = layoutManager.getChildCount();
        Rect  drawRect = new Rect();
        int top,left,right,bottom;

        for(int childIndex = 0 ; childIndex < childCount-1; childIndex++){
            childView = layoutManager.getChildAt(childIndex);
            layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            top = childView.getBottom() + layoutParams.bottomMargin;
            left = childView.getLeft() + layoutParams.leftMargin + childView.getPaddingLeft();
            right = childView.getRight() - childView.getPaddingRight() - layoutParams.rightMargin;
            bottom = top + height;
            if (leftMargin>0){
                left=leftMargin;
            }
            drawRect.set(left,top,  right, bottom);
            c.drawRect(drawRect, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0,  height);
    }
}
