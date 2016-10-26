package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.RechargeActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/9/2.
 */
public class GiftStorePopuWindow extends PopupWindow {
    private Context mContext;
    private List<GiftDataResultBean> giftSresult;
    int clickCount = 1;
    int sendTotal = 1;
    int itemPosition = 0;
    TextView sendCount;
 //   private GiftGridViewAdpter giftGridViewAdpter;
    private int payBalance = 0;
    private GiftDataResultBean giftDataResultBean;

    public GiftStorePopuWindow(Context mContext, List<GiftDataResultBean> giftSresult) {
        this.mContext = mContext;
        this.giftSresult = giftSresult;
    }
    List<GiftDataResultBean> giftDataResultBeen=null;
    public void showGiftStorePopuWindow(int gold) {

        View giftView = View.inflate(mContext, R.layout.audience_gift_page_layout, null);
        ViewPager audienceGiftVp = (ViewPager) giftView.findViewById(R.id.audience_gift_vp);
        IndicatorView mIndicatorView = (IndicatorView) giftView.findViewById(R.id.idv_banner);
        TextView giftSend = (TextView) giftView.findViewById(R.id.audience_gift_send);
        List<GridView> list = new ArrayList<>();
        int giftCountSize = giftSresult.size();
        int viewPageCount=(giftCountSize%6)==0?(giftCountSize/6):(giftCountSize/6)+1;
        for(int i=0;i<viewPageCount;i++){
            GridView giftGridViews=new GridView(mContext);
            int j=(i==0?0:(i*6));
            if(giftSresult.size()>=((i+1)*6)+1){
                giftDataResultBeen = giftSresult.subList(j, (i+1)*6);
            }else {
                giftDataResultBeen=giftSresult.subList(j,giftSresult.size());
            }
            giftGridViews.setNumColumns(3);
            final GiftGridViewAdpter  giftGridViewAdpter = new GiftGridViewAdpter(mContext, giftDataResultBeen);
            giftGridViews.setAdapter(giftGridViewAdpter);
            giftGridViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onGridViewItemClick(giftGridViewAdpter, view,position,id);
                }
            });
            list.add(giftGridViews);
        }
        final BannerPagerAdapter mBannerPagerAdapter = new BannerPagerAdapter(mContext, list);
        audienceGiftVp.setAdapter(mBannerPagerAdapter);
        mIndicatorView.setViewPager(audienceGiftVp);
        final TextView accountBalance = (TextView) giftView.findViewById(R.id.account_balance);
        TextView recharge = (TextView) giftView.findViewById(R.id.audience_top_up_recharge);
        recharge.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        recharge.getPaint().setAntiAlias(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(giftView);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);
        accountBalance.setText(String.valueOf(gold));

        giftSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payBalance==0){
                    Toast.makeText(mContext,"您还未选中礼物!",Toast.LENGTH_SHORT).show();
                }
                if (onSendClickListener != null&&payBalance!=0) {
                    onSendClickListener.sendGiftMessage(GiftStorePopuWindow.this.giftDataResultBean, sendTotal, payBalance);
                }
            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {//充值按钮
            @Override
            public void onClick(View v) {
                if (UserHelper.isLogined(mContext)) {
                    LiveConstant.IS_FIRST_CLICK_PAGE=true;
                    charge();//跳转充值页面
                    dismiss();
                }else{
                    login();
                }
            }
        });

    }

    private void onGridViewItemClick(GiftGridViewAdpter  giftGridViewAdpter, View view, int position, long id) {
        TextView sendCount = (TextView) view.findViewById(R.id.gift_send_count);
        giftDataResultBean= (GiftDataResultBean) view.getTag(R.id.giftdatabean);
        giftGridViewAdpter.setSelectedPosition(position);
        giftGridViewAdpter.notifyDataSetChanged();
        if (itemPosition != position) {
            clickCount = 1;
        }
        itemPosition = position;
        clickCount=clickCount>=6?1:clickCount;
        switch (clickCount) {
            case 1:
                sendCount.setText("1");
                sendTotal = 1;
                sendCount.setVisibility(View.VISIBLE);
                break;
            case 2:
                sendTotal = 9;
                sendCount.setText("9");
                break;
            case 3:
                sendTotal = 99;
                sendCount.setText("99");
                break;
            case 4:
                sendTotal = 520;
                sendCount.setText("520");
                break;
            case 5:
                sendTotal = 999;
                sendCount.setText("999");
                break;
        }
        payBalance = sendTotal * (giftDataResultBean.getGold());

        clickCount++;

    }

    private void login() {
        Intent intent=new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    private void charge() {
        Intent intent = new Intent(mContext, RechargeActivity.class);
        mContext.startActivity(intent);
    }


    private OnSendClickListener onSendClickListener;

    public interface OnSendClickListener {
        void sendGiftMessage(GiftDataResultBean giftDataResultBean, int sendTotal, int payBalance);
    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

}
