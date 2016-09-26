package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
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
import com.lalocal.lalocal.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    //    List<SparseArray<View>> lastSelectView = new ArrayList<>();
    SparseArray<View> lastSelViews = new SparseArray<>();
    List<Coupon> seliIems = new ArrayList<>();
    //    SparseArray<Coupon> seliCopIems = new SparseArray<>();
    Map<Integer, Coupon> seliCopMap = new HashMap<>();
    boolean isClickEnalbe;


    public MyCouponRecyclerAdapter(List<Coupon> items) {
        mItems = items;
    }

    public void updateItems(List<Coupon> items) {
        mItems = items;
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

    //    public List<View> getSelectedCouponViews() {
//        return lastSelectView;
//    }
    public Map<Integer, Coupon> getSelectedCouponMap() {
        return seliCopMap;
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
        AppLog.print("onBindViewHolder___");
        if (mItems != null && mItems.size() > 0) {
            final Coupon item = mItems.get(position);
            if (item != null) {
                MyCouponHolder couponHolder = (MyCouponHolder) holder;
                Resources res = mContext.getResources();
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) couponHolder.itemView.getLayoutParams();
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
//                if (item.getType() == 1 && item.getStatus() == 0) {
//                    couponHolder.cornerImg.setBackgroundDrawable(res.getDrawable(R.drawable.writer_mark_icon));
//                } else if (item.getStatus() == 2) {
//                    couponHolder.cornerImg.setBackgroundDrawable(res.getDrawable(R.drawable.losttime_mark_icon));
//                }
                if (item.getStatus() == 2) {
                    couponHolder.cornerImg.setBackgroundDrawable(res.getDrawable(R.drawable.losttime_mark_icon));
                } else {
                    if (item.getType() == 1) {
                        couponHolder.cornerImg.setBackgroundDrawable(res.getDrawable(R.drawable.writer_mark_icon));
                    } else {
                        couponHolder.cornerImg.setBackgroundDrawable(null);
                    }

                }


                couponHolder.discount_tv.setText(CommonUtil.formartOrderPrice(item.getDiscount()));
                couponHolder.time_tv.setText("有效期至：" + item.getExpiredDateStr().replace("_", "."));
                couponHolder.itemView.setFocusable(true);
                couponHolder.itemView.setTag(item);
                AppLog.print("onBindViewHolder_____itemView__position___" + position + ", itemView__" + couponHolder.itemView);
//                View view = lastSelViews.get(position);
//                if (view != null) {
//                    couponHolder.itemView.setSelected(true);
//                } else {
//                    couponHolder.itemView.setSelected(false);
//                }
//                Coupon coupon = seliCopIems.get(position);
                Coupon coupon = seliCopMap.get(position);
                AppLog.print("seliCopMap__coupon__" + coupon + "____map___" + seliCopMap);
                if (coupon != null) {
                    couponHolder.itemView.setSelected(true);
                } else {
                    couponHolder.itemView.setSelected(false);
                }

                if (isClickEnalbe && (item.getStatus() == 0 || item.getStatus() == 1)) {

                    couponHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppLog.print("itemView onClick________" + v);
                            if (v.isSelected()) {
                                v.setSelected(false);
                                AppLog.print("unSelectedPoistion___" + position);
                                updateCouponView(item, false, position);
//                                if (lastSelViews.get(position) != null) {
//                                    AppLog.print("lastSelView____!=null");
//                                    lastSelViews.delete(position);
//                                }
                            } else {
                                AppLog.print("selectedPosition____" + position);
                                v.setSelected(true);
                                updateCouponView(item, true, position);
//                                lastSelViews.append(position, v);
                            }
//                            for (int i = 0; i < lastSelViews.size(); i++) {
//                                int key = lastSelViews.keyAt(i);
//                                AppLog.print("lastSeview_____key__" + key);
//                            }
//                                AppLog.print("item type__！-1———size—--"+lastSelViews.size());
//                            AppLog.print("item type___"+item.getType());
//                            if (item.getType() != 1) {
//                                AppLog.print("item type__！-1———size—--"+lastSelViews.size());
//                                for (int i = 0; i < lastSelViews.size(); i++) {
//                                    AppLog.print("xxxxxxx__"+i);
//                                    int key = lastSelViews.keyAt(i);
//                                    AppLog.print("selView_____key___" + key+", position__"+position+",   i___"+i);
//                                    if (key != position) {
//                                        AppLog.print("spararry del___"+key);
//                                        lastSelViews.removeAt(i);
//                                    }
//                                }
//                            }
//                            for (int i = 0; i < lastSelViews.size(); i++) {
//                                int key = lastSelViews.keyAt(i);
//                                AppLog.print("lastSel key____" + key);
//                            }

//                            updateCouponView(v, position);
                        }
                    });
                }
            }
        }

    }

    public void updateHistoryCouponList(int type) {
        AppLog.print("start size____" + seliCopMap.size() + "__type_" + type);
        Iterator<Map.Entry<Integer, Coupon>> it = seliCopMap.entrySet().iterator();
        while (it.hasNext()) {
//            Map.Entry<Integer, String> entry=it.next();
//            int key=entry.getKey();
//            if(key%2==1){
//                System.out.println("delete this: "+key+" = "+key);
//                //map.put(key, "奇数");   //ConcurrentModificationException
//                //map.remove(key);      //ConcurrentModificationException
//                it.remove();        //OK
//            }
//
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            Map.Entry<Integer, Coupon> entry = it.next();
            Coupon coupon = entry.getValue();
            if (coupon.getType() != type) {
                AppLog.print("!=type");
                AppLog.print("recommoveAt___key__" + entry.getKey());
                int key = entry.getKey();
                if (seliCopMap.containsKey(key)) {
                    AppLog.print("removie_____");
//                    seliCopMap.remove(key);
                    it.remove();
                    notifyItemChanged(key);
                }

            }

        }
//
//        for (Map.Entry<Integer, Coupon> entry : seliCopMap.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            Coupon coupon = entry.getValue();
//            if (coupon.getType() != type) {
//                AppLog.print("!=type");
//                AppLog.print("recommoveAt___key__" + entry.getKey());
//                Integer key = entry.getKey();
//                if (seliCopMap.containsKey(key)) {
//                    AppLog.print("removie_____");
//                    seliCopMap.remove(key)
//                }
////                notifyItemChanged(entry.getKey());
//                notifyDataSetChanged();
//            }
//
//        }
//        for (int i = 0; i < seliCopIems.size(); i++) {
//            Coupon coupon = seliCopIems.valueAt(i);
//            AppLog.print("xxxx______seli_cotype_"+coupon.getType());
//            if (coupon.getType() != type) {
//                AppLog.print("!=type");
//                seliCopIems.removeAt(i);
//                AppLog.print("recommoveAt___");
//                int key = seliCopIems.keyAt(i);
//                notifyItemChanged(key);
//            }
//        }
//        AppLog.print("end size____"+seliCopIems.size());
    }

    private void updateCouponView(Coupon itemCoupon, boolean isSelected, int position) {
        if (isSelected) {
            int type = itemCoupon.getType();
            if (type == 0) {
                seliCopMap.clear();
                notifyDataSetChanged();
            } else {
                updateHistoryCouponList(itemCoupon.getType());
            }
//            seliCopIems.append(position, itemCoupon);
            seliCopMap.put(position, itemCoupon);
        } else {
//            seliCopIems.delete(position);
            if (seliCopMap.containsKey(position)) {
                seliCopMap.remove(position);
            }
        }

        if (seliCopMap.size() > 0) {
            double couponValue = 0;

            for (Map.Entry<Integer, Coupon> entry : seliCopMap.entrySet()) {
                Coupon coupon = entry.getValue();
                couponValue += coupon.getDiscount();
            }
//            for (Coupon coupon : seliIems) {
//            }
            mReductionTv.setText("减免：" + CommonUtil.formartOrderPrice(couponValue));
            mUseBtn.setText("使用");
        } else {
            mReductionTv.setText("没有优惠券");
            mUseBtn.setText("不使用");
        }


    }

    //        private void updateCouponView(Coupon itemCoupon, boolean isSelected) {
//        if (isSelected) {
//            if (itemCoupon.getType() != 1) {
//                seliIems.clear();
//            }
//            seliIems.add(itemCoupon);
//        } else {
//            seliIems.remove(itemCoupon);
//        }
//        if (seliIems.size() > 0) {
//            int couponValue = 0;
//            for (Coupon coupon : seliIems) {
//                couponValue += coupon.getDiscount();
//            }
//            mReductionTv.setText("减免：" + couponValue);
//            mUseBtn.setText("使用");
//        } else {
//            mReductionTv.setText("没有优惠券");
//            mUseBtn.setText("不使用");
//        }
//
//
//    }
//
    private void updateCouponView(View currentView, int position) {
        AppLog.print("position____" + position + ",  spSelViews___" + lastSelViews);
        if (lastSelViews.size() > 0) {
            Coupon item = (Coupon) currentView.getTag();
            if (item.getType() != 1) {
                for (int i = 0; i < lastSelViews.size(); i++) {
                    int key = lastSelViews.keyAt(i);
                    AppLog.print("selView_____key___" + key);
                    if (key != position) {
                        lastSelViews.delete(key);
                    }
                }
                for (int i = 0; i < lastSelViews.size(); i++) {
                    int key = lastSelViews.keyAt(i);
                    AppLog.print("lastSel key____" + key);
                }
            }
            int couponValue = 0;
            for (int i = 0; i < lastSelViews.size(); i++) {
                int key = lastSelViews.keyAt(i);
                View selView = lastSelViews.get(key);
                Coupon couponItem = (Coupon) selView.getTag();
                couponValue += couponItem.getDiscount();

            }
//            for (View lastView : lastSelectView) {
//                Coupon couponItem = (Coupon) lastView.getTag();
//                couponValue += couponItem.getDiscount();
//            }
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
