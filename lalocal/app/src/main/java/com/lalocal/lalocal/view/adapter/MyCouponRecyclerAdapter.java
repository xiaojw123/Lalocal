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
import com.lalocal.lalocal.util.CommonUtil;

import java.util.ArrayList;
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
    List<View> lastSelectView = new ArrayList<>();
    boolean isClickEnalbe;


    public MyCouponRecyclerAdapter(List<Coupon> items) {
        mItems = items;
    }
    public void updateItems(List<Coupon> items){
        mItems=items;
        notifyDataSetChanged();

    }

    public void setItemClickEnale(boolean isClickEnalbe) {
        this.isClickEnalbe = isClickEnalbe;
    }


    public void setHandler(Handler handler) {
        mHandler = handler;

    }

    public void setUseGroup(TextView reductionTv, Button useBtn) {
        mReductionTv = reductionTv;
        mUseBtn = useBtn;
    }
    public List<View> getSelectedCouponViews(){
        return  lastSelectView;
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
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) couponHolder.itemView.getLayoutParams();
                if (position == 0) {
                    params.topMargin = (int) res.getDimension(R.dimen.dimen_size_20_dp);
                } else if (position == mItems.size() - 1) {
                    params.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_10_dp);
                }
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
                couponHolder.discount_tv.setText(CommonUtil.formartOrderPrice(item.getDiscount()));
                couponHolder.time_tv.setText("有效期至：" + item.getExpiredDateStr().replace("_", "."));
                couponHolder.itemView.setFocusable(true);
                couponHolder.itemView.setTag(item);
                if (isClickEnalbe) {

                    couponHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.isSelected()) {
                                v.setSelected(false);

                                if (lastSelectView.contains(v)) {
                                    lastSelectView.remove(v);
                                }
                            } else {
                                v.setSelected(true);

                                if (!lastSelectView.contains(v)) {
                                    lastSelectView.add(v);
                                }
                            }
                            updateCouponView(v);
                        }
                    });
                }
            }
        }

    }

    private void updateCouponView(View currentView) {
        if (lastSelectView.size() > 0) {
            Coupon item = (Coupon) currentView.getTag();
            if (item.getType() != 1) {
                for (View lastView : lastSelectView) {
                    if (lastView != currentView) {
                        lastView.setSelected(false);
                    }
                }
            }
            int couponValue = 0;
            for (View lastView : lastSelectView) {
                Coupon couponItem = (Coupon) lastView.getTag();
                couponValue += couponItem.getDiscount();
            }
            mReductionTv.setText("减免：" + couponValue);
            mUseBtn.setText("使用");
        } else {
            mReductionTv.setText("没有优惠券");
            mUseBtn.setText("不使用");
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
