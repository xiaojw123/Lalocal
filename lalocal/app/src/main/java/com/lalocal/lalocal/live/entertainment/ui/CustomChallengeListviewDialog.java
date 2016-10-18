package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.adapter.ChallengeListAdapter;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by android on 2016/10/8.
 */
public class CustomChallengeListviewDialog extends BaseDialog {
    @BindView(R.id.chanllage_dialog_listview_close_iv)
    ImageView chanllageDialogListviewCloseIv;
    @BindView(R.id.challenge_list_recy)
    XRecyclerView challengeListRecy;
    private Context mContext;
    private List<ChallengeDetailsResp.ResultBean> result;
    CustomDialogListener listener;
    public CustomChallengeListviewDialog(Context mContext, List<ChallengeDetailsResp.ResultBean> result) {
        super(mContext, R.style.prompt_dialog);
        this.mContext = mContext;
        this.result = result;
    }


    @Override
    public void initView() {
        showChallengeList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.challenge_dialog_listview_layout;
    }

    private void showChallengeList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        challengeListRecy.setLayoutManager(layoutManager);
        challengeListRecy.setLoadingMoreEnabled(false);
        challengeListRecy.setPullRefreshEnabled(false);
        List<ChallengeDetailsResp.ResultBean> resultBeen = resultSort(result);

        ChallengeListAdapter challengeListAdapter = new ChallengeListAdapter(mContext, resultBeen, newChallengeNumber, completeChallengeNumber);
        challengeListRecy.setAdapter(challengeListAdapter);
        challengeListAdapter.setOnLiveItemClickListener(new ChallengeListAdapter.OnEmmceeClickListener() {
            @Override
            public void emmceeClickStatus(int status,int challengeId) {
                if(listener!=null){
                    listener.onDialogClickListener(status,challengeId);
                }
            }
        });
        chanllageDialogListviewCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    List<ChallengeDetailsResp.ResultBean> newChallengeList = new ArrayList<>();
    List<ChallengeDetailsResp.ResultBean> completeChallengeList = new ArrayList<>();
    List<ChallengeDetailsResp.ResultBean> atLastChallengeList = new ArrayList<>();
    private int newChallengeNumber = 0;
    private int completeChallengeNumber = 0;

    private List<ChallengeDetailsResp.ResultBean> resultSort(List<ChallengeDetailsResp.ResultBean> result) {
        for (ChallengeDetailsResp.ResultBean resultBean : result) {
            if (resultBean.getStatus() == 1) {
                atLastChallengeList.add(0, resultBean);
                break;
            }
        }

        for (ChallengeDetailsResp.ResultBean resultBean : result) {
            if (resultBean.getStatus() == 0) {
                atLastChallengeList.add(resultBean);
                ++newChallengeNumber;
            }
        }

        for (ChallengeDetailsResp.ResultBean resultBean : result) {
            if (resultBean.getStatus() == 2) {
                atLastChallengeList.add(resultBean);
                ++completeChallengeNumber;
            }
        }
        return atLastChallengeList;
    }

    public static interface CustomDialogListener {
        void onDialogClickListener(int status,int challengeId);
    }
    public  void setDialogClickListener(CustomDialogListener listener){
        this.listener=listener;
    }
}
