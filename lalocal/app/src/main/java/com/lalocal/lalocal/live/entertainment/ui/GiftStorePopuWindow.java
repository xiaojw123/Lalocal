package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.RechargeActivity;
import com.lalocal.lalocal.help.UserHelper;
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

    public void showGiftStorePopuWindow() {
        View giftView = View.inflate(mContext, R.layout.audience_gift_page_layout, null);
        ViewPager audienceGiftVp = (ViewPager) giftView.findViewById(R.id.audience_gift_vp);
        IndicatorView mIndicatorView = (IndicatorView) giftView.findViewById(R.id.idv_banner);
        Button giftSend = (Button) giftView.findViewById(R.id.audience_gift_send);
        List<GridView> list = new ArrayList<>();
        View inflate = View.inflate(mContext, R.layout.audience_gift_viewpager_layout, null);
        final GiftGridView giftGridView = (GiftGridView) inflate.findViewById(R.id.audience_gift_list);
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

        giftSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSendClickListener != null) {
                    onSendClickListener.sendGiftMessage(itemPosition, sendTotal, payBalance);
                }
            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {//充值按钮
            @Override
            public void onClick(View v) {
                if (UserHelper.isLogined(mContext)) {
                    charge();
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
                        sendTotal = 10;
                        sendCount.setText("10");
                        break;
                    case 4:
                        sendTotal = 66;
                        sendCount.setText("66");
                        break;
                    case 5:
                        sendTotal = 99;
                        sendCount.setText("99");
                        break;
                    case 6:
                        sendTotal = 666;
                        sendCount.setText("666");
                        break;
                    case 7:
                        sendTotal = 999;
                        sendCount.setText("999");
                        break;
                    case 8:
                        sendCount.setText("");
                        sendTotal = 0;
                        sendCount.setVisibility(View.GONE);
                        clickCount = 0;
                        break;

                }

                payBalance = sendTotal * (giftDataResultBean.getGold());
                accountBalance.setText(String.valueOf(payBalance));
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
