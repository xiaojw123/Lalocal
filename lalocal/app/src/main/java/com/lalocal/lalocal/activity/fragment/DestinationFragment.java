package com.lalocal.lalocal.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.DestinationAreaActivity;
import com.lalocal.lalocal.activity.SearchActivity;
import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.DesAreaAdapter;
import com.lalocal.lalocal.view.decoration.SpaceItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 */
public class DestinationFragment extends Fragment implements View.OnClickListener {
    private static final String PAGE_NAME = "DestinationFragment";
    private RecyclerView mRecyclerView;
    private FrameLayout mSearchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_destination_layout, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_des_areas_rlv);
        mSearchView = (FrameLayout) view.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        initLoader();
        return view;
    }

    private void initLoader() {
        ContentLoader loader = new ContentLoader(getActivity());
        loader.setCallBack(new DesCallBack());
        loader.getDestinationAreas();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    class DesCallBack extends ICallBack implements OnItemClickListener {

        @Override
        public void onGetDestinationAreas(List<AreaItem> items) {
            DesAreaAdapter adapter = new DesAreaAdapter(getActivity(), items);
            adapter.setOnItemClickListener(this);
            LinearLayoutManager lm=new LinearLayoutManager(getActivity());
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration((int)getResources().getDimension(R.dimen.dimen_size_10_dp)));
            mRecyclerView.setLayoutManager(lm);
            mRecyclerView.setAdapter(adapter);
        }

        @Override
        public void onItemClickListener(View view, int position) {
            AreaItem item= (AreaItem) view.getTag();
            if (item!=null){
            Intent intent=new Intent(getActivity(), DestinationAreaActivity.class);
            intent.putExtra(DestinationAreaActivity.AREA_ID,item.getId());
            intent.putExtra(DestinationAreaActivity.AREA_NAME,item.getName());
            startActivity(intent);
            }

        }
    }


}