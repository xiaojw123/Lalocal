package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.DestinationFragment;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.DesAreaAdapter;
import com.lalocal.lalocal.view.decoration.SpaceItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DestinationActivity extends BaseActivity {

    @BindView(R.id.home_des_areas_rlv)
    RecyclerView mRVDest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        // 使用ButterKnife框架
        ButterKnife.bind(this);
        // 初始化ContentLoader
        initLoader();
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        ContentLoader loader = new ContentLoader(DestinationActivity.this);
        loader.setCallBack(new DesCallBack());
        loader.getDestinationAreas();
    }

    @OnClick({R.id.img_back, R.id.img_search})
    void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                DestinationActivity.this.finish();
                break;
            case R.id.img_search:
                MobHelper.sendEevent(DestinationActivity.this, MobEvent.DESTINATION_SEARCH);
                Intent intent = new Intent(DestinationActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    class DesCallBack extends ICallBack implements OnItemClickListener {

        @Override
        public void onGetDestinationAreas(List<AreaItem> items) {
            try {
                DesAreaAdapter adapter = new DesAreaAdapter(DestinationActivity.this, items);
                adapter.setOnItemClickListener(this);
                LinearLayoutManager lm=new LinearLayoutManager(DestinationActivity.this);
                lm.setOrientation(LinearLayoutManager.VERTICAL);
                mRVDest.addItemDecoration(new SpaceItemDecoration((int)getResources().getDimension(R.dimen.dimen_size_10_dp)));
                mRVDest.setLayoutManager(lm);
                mRVDest.setAdapter(adapter);
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
