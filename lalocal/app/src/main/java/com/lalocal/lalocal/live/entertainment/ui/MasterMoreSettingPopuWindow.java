package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.agora.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/11/14.
 */
public class MasterMoreSettingPopuWindow extends PopupWindow {
    @BindView(R.id.live_switchover_camera)
    TextView liveSwitchoverCamera;
    @BindView(R.id.live_beauty_switch)
    TextView liveBeautySwitch;
    @BindView(R.id.live_share_pop)
    TextView liveSharePop;
    @BindView(R.id.live_send_message_pop)
    TextView liveSendMessagePop;
    private Context context;

    public MasterMoreSettingPopuWindow(Context context) {
        this.context = context;
    }

    public void showSettingView() {
        View view = View.inflate(context, R.layout.master_more_setting_pop_layout, null);
        ButterKnife.bind(this, view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);

        if(Constant.PRP_ENABLED){
            liveBeautySwitch.setText(R.string.live_beauty_on);
            liveBeautySwitch.setTextColor(context.getResources().getColor(R.color.live_beauty_on));
        }else{
            liveBeautySwitch.setText(R.string.live_beauty_off);
        }


    }
    public  void setClickListener(MasterMoreSettingListener masterMoreSettingListener){
        this.masterMoreSettingListener=masterMoreSettingListener;
    }


    @OnClick({R.id.live_switchover_camera, R.id.live_beauty_switch, R.id.live_share_pop, R.id.live_send_message_pop})
    public  void clickBtn(View view){
        switch (view.getId()){
            case R.id.live_switchover_camera:
                if(masterMoreSettingListener!=null){
                    masterMoreSettingListener.onPopItemClickListener(R.id.live_switchover_camera,view);
                }
                dismiss();
                break;
            case R.id.live_beauty_switch:
                if(masterMoreSettingListener!=null){
                    masterMoreSettingListener.onPopItemClickListener(R.id.live_beauty_switch,view);
                }
                dismiss();
                break;
            case R.id.live_share_pop:
                if(masterMoreSettingListener!=null){
                    masterMoreSettingListener.onPopItemClickListener(R.id.live_share_pop,view);
                }
                dismiss();
                break;
            case R.id.live_send_message_pop:
                if(masterMoreSettingListener!=null){
                    masterMoreSettingListener.onPopItemClickListener(R.id.live_send_message_pop,view);
                }
                dismiss();
                break;
        }
    }
    MasterMoreSettingListener masterMoreSettingListener;
    public static interface MasterMoreSettingListener {
        void onPopItemClickListener(int viewId, View view);
    }
}
