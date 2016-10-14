package com.lalocal.lalocal.live.entertainment.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.live.entertainment.adapter.LiveAttentionDemoAdapter;
import com.lalocal.lalocal.live.entertainment.model.LiveAttentionBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/10/10.
 */
public class LiveAttentionDemo extends BaseActivity {
    @BindView(R.id.live_attention_xrecy)
    XRecyclerView liveAttentionXrecy;
    private List<LiveAttentionBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_attention_demo_layout);
        ButterKnife.bind(this);
        list = new ArrayList<>();
         initData();
        initRecyclerView();

    }

    private void initData() {
        LiveAttentionBean liveAttentionBean = new LiveAttentionBean();
        liveAttentionBean.setType(0);
        liveAttentionBean.setLiveContent("意大利风情小吃街");
        list.add(liveAttentionBean);

        LiveAttentionBean liveAttentionBean1 = new LiveAttentionBean();
        liveAttentionBean1.setType(0);
        liveAttentionBean1.setLiveContent("户部巷风情小吃街");
        list.add(liveAttentionBean1);


        LiveAttentionBean liveAttentionBean2 = new LiveAttentionBean();
        liveAttentionBean2.setType(1);
        liveAttentionBean2.setLiveContent("意大利风情小吃街回放");
        list.add(liveAttentionBean2);

        LiveAttentionBean liveAttentionBean3 = new LiveAttentionBean();
        liveAttentionBean3.setType(1);
        liveAttentionBean3.setLiveContent("户部巷风情小吃街回放");
        list.add(liveAttentionBean3);

        LiveAttentionBean liveAttentionBean4 = new LiveAttentionBean();
        liveAttentionBean4.setType(2);
        liveAttentionBean4.setLiveContent("意大利风情小吃街推荐");
        list.add(liveAttentionBean4);

        LiveAttentionBean liveAttentionBean5 = new LiveAttentionBean();
        liveAttentionBean5.setType(2);
        liveAttentionBean5.setLiveContent("意大利风情小吃街推荐");
        list.add(liveAttentionBean5);


    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        liveAttentionXrecy.setLayoutManager(layoutManager);
        liveAttentionXrecy.setAdapter(new LiveAttentionDemoAdapter(this,list));

    }
}
