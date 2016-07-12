package com.lalocal.lalocal.activity.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.RecommendResultBean;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.ViewFactory;
import com.lalocal.lalocal.view.adapter.XListviewAdapter;
import com.lalocal.lalocal.view.viewpager.CycleViewPager;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiaojw on 2016/6/3.
 */
public class RecommendFragment extends Fragment implements XListView.IXListViewListener {
    private int pageNumber;
    private int pageSize;
    private XListView xlistview;
    private CycleViewPager cycleViewPager;
    private XListviewAdapter xListviewAdapter;
    private List<RecommendRowsBean> rows;
    private List<RecommendRowsBean> allRows = new ArrayList<>();
    private int totalPages = 0;
    private int totalRows = 0;
    private int itemCount = 0;
    boolean firstRefresh = true;
    private int page = 2;
    private ContentLoader contentService;
    private View header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recommend_layout, container, false);
        contentService = new ContentLoader(getActivity());
        contentService.setCallBack(new MyCallBack());
        contentService.recommendAd();
        contentService.recommentList(10, 1);
        xlistview = (XListView) view.findViewById(R.id.recommend_xlv);
        xlistview.setPullLoadEnable(true);
        xlistview.setPullRefreshEnable(true);
        xlistview.setXListViewListener(this);
        cycleViewPager = new CycleViewPager();
        header = View.inflate(getActivity(), R.layout.viewpager, null);
        View viewById = header.findViewById(R.id.lunbotu_content);
        viewById.setVisibility(View.GONE);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_cycle_viewpager_content, cycleViewPager).commit();
        xlistview.addHeaderView(header);
        xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (allRows != null) {
                    RecommendRowsBean recommendRowsBean = allRows.get(position - 2);
                    int rowId = recommendRowsBean.getId();
                    AppLog.i("TAG", rowId + "");
                    Intent intent = new Intent(getActivity(), SpecialDetailsActivity.class);
                    intent.putExtra("rowId", rowId + "");
                    startActivity(intent);

                }
            }
        });
        return view;
    }

    public class MyCallBack extends ICallBack {
        @Override
        public void onRecommend(RecommendDataResp recommendDataResp) {
            super.onRecommend(recommendDataResp);
            RecommendResultBean result = recommendDataResp.getResult();
            if (result == null) {
                return;
            }
            List<RecommendRowsBean> rows = result.getRows();
            if (recommendDataResp.getReturnCode() == 0 && rows != null) {
                totalPages = recommendDataResp.getResult().getTotalPages();
                totalRows = recommendDataResp.getResult().getTotalRows();
                if (!isRefresh) {
                    allRows.clear();
                }
                if (allRows.size() == 0) {
                    allRows.addAll(0, rows);
                } else {
                    itemCount = allRows.size();
                    allRows.addAll(allRows.size(), rows);
                }
                Message message = new Message();
                message.what = NOTIFI;
                handler.sendMessage(message);
                xListviewAdapter = new XListviewAdapter(getActivity(), allRows);
                if (firstRefresh) {
                    xlistview.setAdapter(xListviewAdapter);
                    firstRefresh = false;
                }
                if (!firstRefresh) {
                    xListviewAdapter.refresh(allRows);
                }
            }
        }

        @Override
        public void onRecommendAd(RecommendAdResp recommendAdResp) {
            super.onRecommendAd(recommendAdResp);
            try {
                if (recommendAdResp.getReturnCode() == 0) {
                    List<RecommendAdResultBean> result = recommendAdResp.getResult();
                    ViewFactory.initialize(getActivity(), header, cycleViewPager, result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH:
                    xlistview.stopRefresh();
                    break;
                case NOTIFI:
                    xListviewAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };
    public static final int REFRESH = 0;
    public static final int NOTIFI = 1;
    boolean isRefresh;

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = false;
                contentService.recommentList(10, 1);
                page = 2;
                Message message = new Message();
                message.what = REFRESH;
                //  handler.sendEmptyMessage(0);
                handler.sendMessage(message);
            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {
        isRefresh = true;
        if (page <= totalPages) {
            contentService.recommentList(10, page);
            page = page + 1;

        } else {
            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
        }
        xlistview.stopLoadMore();
    }


}