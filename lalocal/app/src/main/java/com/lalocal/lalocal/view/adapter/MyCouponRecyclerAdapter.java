package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.util.AppLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/9/13.
 */
public class MyCouponRecyclerAdapter extends BaseRecyclerAdapter {
    List<Coupon> mItems;
    Context mContext;
    Handler mHandler;
    TextView mReductionTv;
    Button mUseBtn;
    int couponValue;


    public MyCouponRecyclerAdapter(List<Coupon> items) {
        mItems = items;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;

    }

    public void setUseGroup(TextView reductionTv, Button useBtn) {
        mReductionTv = reductionTv;
        mUseBtn = useBtn;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_coupon_rlv_item, parent, false);
        MyCouponHolder holder = new MyCouponHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mItems != null && mItems.size() > 0) {
            final Coupon item = mItems.get(position);
            if (item != null) {
                final MyCouponHolder couponHolder = (MyCouponHolder) holder;
                Resources res = mContext.getResources();
                if (item.getType() == 1) {
                    couponHolder.itemView.setBackgroundDrawable(res.getDrawable(R.drawable.my_coupon_card_bg1));
                    couponHolder.myCouponContainer.setActivated(false);
                    couponHolder.discount_tv.setActivated(false);
                    couponHolder.discount_units_tv.setActivated(false);
                    couponHolder.time_tv.setActivated(false);
                    couponHolder.rule1_tv.setActivated(false);
                    couponHolder.rule1_tv.setText("此优惠券可与同色优惠券叠加使用");
                } else {
                    couponHolder.itemView.setBackgroundDrawable(res.getDrawable(R.drawable.my_coupon_card_bg2));
                    couponHolder.myCouponContainer.setActivated(true);
                    couponHolder.discount_units_tv.setActivated(true);
                    couponHolder.time_tv.setActivated(true);
                    couponHolder.rule1_tv.setActivated(true);
                    couponHolder.rule1_tv.setText("不可叠加使用");
                }
                if (item.getType() == 1 && item.getStatus() == 0) {
                    couponHolder.cornerImg.setBackgroundDrawable(res.getDrawable(R.drawable.writer_mark_icon));
                } else if (item.getStatus() == 2) {
                    couponHolder.cornerImg.setBackgroundDrawable(res.getDrawable(R.drawable.losttime_mark_icon));
                }
                couponHolder.discount_tv.setText(String.valueOf((int) item.getDiscount()));
                couponHolder.time_tv.setText("有效期至：" + item.getExpiredDateStr().replace("_", "."));
                couponHolder.itemView.setFocusable(true);
                couponHolder.itemView.setTag(item);
                couponHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppLog.print("itemView____onclick________v isSelected_" + v.isSelected());
                        if (item.getType() == 1) {
                            if (v.isSelected()) {
                                v.setSelected(false);
                                couponValue -= item.getDiscount();
                            } else {
                                v.setSelected(true);
                                couponValue += item.getDiscount();
                            }
                            if (couponValue < 0) {
                                couponValue = 0;
                            }
                            if (couponValue == 0) {
                                mReductionTv.setText("没有减免");
                                mUseBtn.setText("不使用");
                            } else {
                                mReductionTv.setText("减免: " + couponValue);
                                mUseBtn.setText("使用");
                            }

                        }else{
                            couponValue=0;

                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyCouponHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_coupon_card)
        RelativeLayout myCouponContainer;
        @BindView(R.id.my_coupon_corner_img)
        ImageView cornerImg;
        @BindView(R.id.my_coupon_denomination_tv)
        TextView discount_tv;
        @BindView(R.id.my_coupon_discount_units)
        TextView discount_units_tv;
        @BindView(R.id.my_coupon_use_rule_tv)
        TextView rule1_tv;
        @BindView(R.id.my_coupon_time_tv)
        TextView time_tv;


        public MyCouponHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
