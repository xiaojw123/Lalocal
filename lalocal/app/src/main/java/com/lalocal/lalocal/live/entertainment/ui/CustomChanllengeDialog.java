package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by android on 2016/10/4.
 */
public class CustomChanllengeDialog extends BaseDialog{
    @BindView(R.id.chanllage_dialog_close_iv)
    ImageView chanllageDialogCloseIv;
    @BindView(R.id.chanllenge_start)
    TextView chanllengeStart;
    private Context mContext;
    ChanllengeInitiateDialogListener chanllageStartListener;
    public CustomChanllengeDialog(Context context) {
        super(context);
    }
    public void startChanllageClikListener(ChanllengeInitiateDialogListener listener){
        this.chanllageStartListener=listener;
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.challenge_dialog_initiate_layout;
    }

    @OnClick({R.id.chanllage_dialog_close_iv, R.id.chanllenge_start})
    public  void btnClick(View view){
        switch (view.getId()){
            case R.id.chanllage_dialog_close_iv:
                dismiss();
                break;
            case R.id.chanllenge_start:
                if(chanllageStartListener!=null){
                    chanllageStartListener.onChanllengeInitiateDialogListener(200,"主播去把刚刚送给妹子的棒棒糖要回来!");
                }
                dismiss();
                break;

        }
    }


    public static interface ChanllengeInitiateDialogListener {
        void onChanllengeInitiateDialogListener(int gold, String content);
    }


}
