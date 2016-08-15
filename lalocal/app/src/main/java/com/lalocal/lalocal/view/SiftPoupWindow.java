package com.lalocal.lalocal.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SiftModle;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.SiftMenuAdpater;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/22.
 */
public class SiftPoupWindow extends PopupWindow implements View.OnClickListener, View.OnTouchListener {

    RecyclerView siftRlv;
    ContentLoader loader;
    SiftMenuAdpater adpater;
    Button closeBtn;
    Context context;
    OnItemClickListener listener;

    public SiftPoupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        initView(contentView);
    }

    public void setSiftItemClickListener(OnItemClickListener listener){
        this.listener=listener;

    }
    private void initView(View contentView) {
        context = contentView.getContext();
        contentView.setOnTouchListener(this);
        siftRlv = (RecyclerView) contentView.findViewById(R.id.sift_menu_rlv);
        closeBtn = (Button) contentView.findViewById(R.id.sifit_menu_close_btn);
        closeBtn.setOnClickListener(this);
        loader = new ContentLoader(context);
        loader.setCallBack(new SiftCallBack());
        loader.getDestinationCollections();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isShowing()) {
            dismiss();
        }
        return false;
    }

    class SiftCallBack extends ICallBack {
        @Override
        public void onGetDestinationCollections(List<SiftModle> items) {
            if (adpater == null) {
                adpater = new SiftMenuAdpater(context, items);
                GridLayoutManager gm = new GridLayoutManager(context, 3);
                gm.setOrientation(GridLayoutManager.VERTICAL);
                siftRlv.addItemDecoration(new SiftDecoration((int) context.getResources().getDimension(R.dimen.sift_menu_item_left), (int) context.getResources().getDimension(R.dimen.sift_menu_item_top)));
                siftRlv.setLayoutManager(gm);
                adpater.setOnItemClickListener(listener);
                siftRlv.setAdapter(adpater);
            } else {
                adpater.updateItems(items);
            }


        }

    }

    class SiftDecoration extends RecyclerView.ItemDecoration {
        int top;
        int left;

        public SiftDecoration(int left, int top) {
            this.top = top;
            this.left = left;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildLayoutPosition(view);
            if (pos > 2) {
                outRect.top = top;
            } else {
                outRect.top = 0;
            }
            outRect.left = left;
            outRect.right = left;
        }
    }
}
