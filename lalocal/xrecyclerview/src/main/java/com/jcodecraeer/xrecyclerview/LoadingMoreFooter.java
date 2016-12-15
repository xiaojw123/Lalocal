package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

public class LoadingMoreFooter extends LinearLayout {

    private SimpleViewSwitcher progressCon;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TextView mText;
    private View mNoMoreView;


	public LoadingMoreFooter(Context context) {
		super(context);
		initView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LoadingMoreFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
    public void initView(){
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressCon = new SimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(getContext());
        mText.setText("正在加载...");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );

        mText.setLayoutParams(layoutParams);
        mNoMoreView= LayoutInflater.from(getContext()).inflate(R.layout.load_more_layout,null);
        addView(mText);
        addView(mNoMoreView);
    }

    public void setNoMore(){
        if (progressCon.getVisibility()==VISIBLE){
        progressCon.setVisibility(GONE);
        }
        if (mText.getVisibility()==VISIBLE){

        mText.setVisibility(GONE);
        }
        if (mNoMoreView.getVisibility()!=VISIBLE){
        mNoMoreView.setVisibility(VISIBLE);
        }
    }
    public void setOther(){
        if (progressCon.getVisibility()!=VISIBLE){
            progressCon.setVisibility(VISIBLE);
        }
        if (mText.getVisibility()!=VISIBLE){
            mText.setVisibility(VISIBLE);
        }
        if (mNoMoreView.getVisibility()==VISIBLE){
            mNoMoreView.setVisibility(GONE);
        }

    }

    public void setProgressStyle(int style) {

        if(style == ProgressStyle.SysProgress){
//            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
//            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
            style=ProgressStyle.LineSpinFadeLoader;
        }
//        else{
//            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
//            progressView.setIndicatorColor(0xffB5B5B5);
//            progressView.setIndicatorId(style);
//            progressCon.setView(progressView);
//        }
        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(style);
        progressCon.setView(progressView);
    }

    public void  setState(int state) {
        switch(state) {
            case STATE_LOADING:
                setOther();
                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                    break;
            case STATE_COMPLETE:
                setOther();
                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                setNoMore();
                break;
        }
    }
}
