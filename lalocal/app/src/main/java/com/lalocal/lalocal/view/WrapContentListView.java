package com.lalocal.lalocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 项目名称：lalocal
 * 模块名称：
 * 包名：com.lalocal.lalocal.view
 * 类功能：
 * 创建人：inerdstack
 * 创建时间：2016 16/9/18 上午8:29
 * 联系邮箱: wjnovember@icloud.com
 */
public class WrapContentListView extends ListView {

    public WrapContentListView(Context context) {
        super(context);
    }

    public WrapContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
