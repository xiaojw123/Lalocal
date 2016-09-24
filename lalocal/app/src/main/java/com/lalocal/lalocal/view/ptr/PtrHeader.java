package com.lalocal.lalocal.view.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cunoraz.gifview.library.GifView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.indicator.PtrTensionIndicator;

/**
 * 项目名称：lalocal
 * 模块名称：
 * 包名：com.lalocal.lalocal.view.ptr
 * 类功能：
 * 创建人：Wang Jie
 * 创建时间：2016 16/9/9 上午11:44
 * 联系邮箱: wjnovember@icloud.com
 */
public class PtrHeader extends LinearLayout implements PtrUIHandler {

    private PtrFrameLayout mPtrFrameLayout;
    private PtrTensionIndicator mPtrTensionIndicator;
    private LinearLayout mContentView;
    private GifView mGifView;

    public PtrHeader(Context context) {
        this(context, null);
    }

    public PtrHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化控件
        initView(context);
    }

    /**
     * 初始化视图
     * @param context
     */
    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
//        LayoutParams lp = new LayoutParams(
//                LayoutParams.MATCH_PARENT, 0);
        mContentView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ptr_header, null);
        mGifView = (GifView) mContentView.findViewById(R.id.ptr_loading_view);
        mContentView.setGravity(Gravity.CENTER);

        addView(mContentView);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = getResources().getDimensionPixelSize(R.dimen.ptr_header_height);
        widthSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthSpec), MeasureSpec.EXACTLY);
//        heightSpec = MeasureSpec.makeMeasureSpec(
//                MeasureSpec.getSize(heightSpec), MeasureSpec.EXACTLY);
        this.setGravity(Gravity.CENTER);
        super.onMeasure(widthSpec, heightSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        AppLog.i("onMeasuree", "the height is " + h);
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = h;
        mContentView.setLayoutParams(lp);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO: 获取header富容器的宽高
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mGifView.setGifResource(R.drawable.loading);
        mGifView.setVisibility(VISIBLE);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh ) {
            //未到达刷新线
            if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                mContentView.setVisibility(GONE);
            }
        } else if (currentPos > mOffsetToRefresh ) {
            //到达或超过刷新线
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                mGifView.setVisibility(GONE);
            }
        }
    }
}
