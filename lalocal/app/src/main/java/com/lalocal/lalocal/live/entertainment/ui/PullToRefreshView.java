package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;

import com.lalocal.lalocal.R;

/**
 * Created by android on 2016/8/24.
 */
public class PullToRefreshView extends ListView implements AbsListView.OnScrollListener{
    private View headerView;
    private int headerHeight;
    private View arrow;

    public PullToRefreshView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeader();
        setListener();
    }

    private void setListener() {
        setOnScrollListener(this);
    }

    private void initHeader() {
        headerView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
        headerView.measure(0, 0);
        headerHeight = headerView.getMeasuredHeight();
        arrow = headerView.findViewById(R.id.header_view);
        finishLoading(true, true);
        this.addHeaderView(headerView);
    }
    private enum PullState {
        PULL_TO_REFRESH, RELEASE_REFRESH, LOADING
    }
    private int downY;
    private int firstVisibleItem;
    private Enum<PullState> state;
    private float k = 3.0f;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int dY = moveY - downY;
                if (firstVisibleItem == 0 && dY > 0) {
                    int top = -headerHeight + dY;
                    float value = -headerHeight + dY * 1.0f / k;
                    headerView.setPadding(0, (int) value, 0, 0);
                    if (top >= 0 && state == PullState.PULL_TO_REFRESH) {

                        state = PullState.RELEASE_REFRESH;
                        animation(true);
                    } else if (top < 0 && state == PullState.RELEASE_REFRESH) {

                        state = PullState.PULL_TO_REFRESH;
                        animation(false);
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == PullState.PULL_TO_REFRESH) {
                    headerView.setPadding(0, -headerHeight, 0, 0);
                } else if (state == PullState.RELEASE_REFRESH) {
                    state = PullState.LOADING;
                    headerView.setPadding(0, 0, 0, 0);
                    arrow.clearAnimation();
                    arrow.setVisibility(View.INVISIBLE);

                    if (listener != null) {
                        listener.onRefresh();
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void animation(boolean isChangeToReleaseRefresh) {
        RotateAnimation ra = new RotateAnimation(isChangeToReleaseRefresh ? 0
                : -180, isChangeToReleaseRefresh ? -180 : -360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(200);
        ra.setFillAfter(true);
        arrow.startAnimation(ra);
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (this.getLastVisiblePosition() == this.getCount() - 1) {
                if (!isBottom) {
                    isBottom = true;
                    setSelection(getCount());

                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem  = firstVisibleItem;
    }




    private OnRefreshListener listener;
    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }
    private boolean isBottom;
    public void finishLoading(boolean isSuccessLoading, boolean isPullToRefresh) {
        if (isPullToRefresh) {
            state = PullState.PULL_TO_REFRESH;
            // 隐藏头部
            headerView.setPadding(0, -headerHeight, 0, 0);
            arrow.setVisibility(View.VISIBLE);

        }else{
            isBottom=false;

        }
    }
}
