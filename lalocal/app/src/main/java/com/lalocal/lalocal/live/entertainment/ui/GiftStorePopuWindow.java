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
    private GiftGridViewAdpter giftGridViewAdpter;
    private int payBalance = 0;

    public GiftStorePopuWindow(Context mContext, List<GiftDataResultBean> giftSresult) {
        this.mContext = mContext;
        this.giftSresult = giftSresult;
    }

    public void showGiftStorePopuWindow(int gold) {
        View giftView = View.inflate(mContext, R.layout.audience_gift_page_layout, null);
        ViewPager audienceGiftVp = (ViewPager) giftView.findViewById(R.id.audience_gift_vp);
        IndicatorView mIndicatorView = (IndicatorView) giftView.findViewById(R.id.idv_banner);
        TextView giftSend = (TextView) giftView.findViewById(R.id.audience_gift_send);
        List<GridView> list = new ArrayList<>();
        View inflate = View.inflate(mContext, R.layout.audience_gift_viewpager_layout, null);
        GridView giftGridView = (GridView) inflate.findViewById(R.id.audience_gift_list);
   /*     int size = giftSresult.size();
        int i = size % 6;
        int pages=0;
        if(i!=0){
            pages=1+(size/6);
        }else {
            pages=size/6;
        }
        AppLog.i("TAG","礼物种类数量:pages:"+pages);
        for(int page=0;page<pages;page++){
            List<GiftDataResultBean> giftDataResultBeen = giftSresult.subList(page, page*6);
            giftGridViewAdpter = new GiftGridViewAdpter(mContext, giftDataResultBeen);
            giftGridView.setAdapter(giftGridViewAdpter);
            list.add(giftGridView);
            page=page*6;
            AppLog.i("TAG","礼物gridview 显示");
        }
      */

          giftGridViewAdpter = new GiftGridViewAdpter(mContext, giftSresult);
        giftGridView.setAdapter(giftGridViewAdpter);
        list.add(giftGridView);
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
                    onSendClickListener.sendGiftMessage(itemPosition, sendTotal, payBalance);
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

        giftGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView sendCount = (TextView) view.findViewById(R.id.gift_send_count);
                GiftDataResultBean giftDataResultBean = giftSresult.get(position);
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
        });

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
        void sendGiftMessage(int itemPosition, int sendTotal, int payBalance);
    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

}
