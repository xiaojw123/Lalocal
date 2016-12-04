package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.DesAreaAdapter;
import com.lalocal.lalocal.view.decoration.SpaceItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.List;

public class DestinationActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private Button searchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        mRecyclerView = (RecyclerView)findViewById(R.id.home_des_areas_rlv);
        searchBtn=(Button) findViewById(R.id.destion_search_btn);
        searchBtn.setOnClickListener(this);
//        mSearchView = (FrameLayout) view.findViewById(R.id.search_view);
//        mSearchView.setOnClickListener(this);
        initLoader();


    }

    private void initLoader() {
        ContentLoader loader = new ContentLoader(this);
        loader.setCallBack(new DesCallBack());
        loader.getDestinationAreas();
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,GlobalSearchActivity.class);
        intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_DESTIATION);
        startActivity(intent);
    }

    class DesCallBack extends ICallBack implements OnItemClickListener {

        @Override
        public void onGetDestinationAreas(List<AreaItem> items) {
            try {
                DesAreaAdapter adapter = new DesAreaAdapter(DestinationActivity.this, items);
                adapter.setOnItemClickListener(this);
                LinearLayoutManager lm=new LinearLayoutManager(DestinationActivity.this);
                lm.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.addItemDecoration(new SpaceItemDecoration((int)getResources().getDimension(R.dimen.dimen_size_10_dp)));
                mRecyclerView.setLayoutManager(lm);
                mRecyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemClickListener(View view, int position) {
            AreaItem item= (AreaItem) view.getTag();
            if (item!=null){
                Intent intent=new Intent(DestinationActivity.this, DestinationAreaActivity.class);
                intent.putExtra(DestinationAreaActivity.AREA_ID,item.getId());
                intent.putExtra(DestinationAreaActivity.AREA_NAME,item.getName());
                startActivity(intent);
            }

        }
    }

}
