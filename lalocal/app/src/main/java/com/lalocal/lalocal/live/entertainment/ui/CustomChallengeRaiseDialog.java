package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/10/5.
 */
public class CustomChallengeRaiseDialog extends BaseDialog {

    @BindView(R.id.chanllage_dialog_raise_close_iv)
    ImageView chanllageDialogRaiseCloseIv;
    @BindView(R.id.challenge_raise_tv)
    TextView challengeRaiseTv;
    @BindView(R.id.challenge_raise_look_details)
    TextView challengeRaiseLookDetails;
    @BindView(R.id.challenge_raise_time)
    TextView challengeRaiseTime;
    @BindView(R.id.challenge_initiate_head)
    CircleImageView challengeInitiateHead;
    @BindView(R.id.challenge_initiate_name)
    TextView challengeInitiateName;
    @BindView(R.id.challenge_raise_money_progress)
    RoundProgress challengeRaiseMoneyProgress;
    @BindView(R.id.challenge_user_account_yue)
    TextView challengeUserAccountYue;
    @BindView(R.id.challenge_user_account_money)
    TextView challengeUserAccountMoney;
    @BindView(R.id.challenge_user_topup)
    TextView challengeUserTopup;
    @BindView(R.id.challenge_user_input_money)
    EditText challengeUserInputMoney;
    @BindView(R.id.challenge_raise_money_btn)
    TextView challengeRaiseMoneyBtn;
    private Context mContext;

    public CustomChallengeRaiseDialog(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        challengeRaiseMoneyProgress.setProgress(95);
    }

    @Override
    public int getLayoutId() {
        return R.layout.challenge_dialog_raise_layout;
    }


    @OnClick({R.id.chanllage_dialog_raise_close_iv, R.id.challenge_raise_look_details})
    public  void btnClick(View view){
        switch (view.getId()){
            case R.id.chanllage_dialog_raise_close_iv:
                break;
            case R.id.challenge_raise_look_details:
                break;
            case R.id.challenge_raise_money_btn:
                break;
            case R.id.challenge_user_topup:
                break;
        }
    }

    public static interface ChanllengeRaiseDialogListener {
        void onChanllengeRaiseDialogListener();
    }
}
