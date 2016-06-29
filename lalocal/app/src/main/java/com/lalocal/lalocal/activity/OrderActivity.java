package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout travel_people_container;
    private LinearLayout pay_money_container;
    private ImageView post_img;
    private TextView title_tv;
    private TextView packages_tv;
    private TextView travel_time_tv;
    private TextView travel_person_num;
    private TextView pay_money;
    private TextView order_numb;
    private TextView order_created_time;
    private TextView pay_time;
    private TextView pay_channel;
    private TextView evalute_time;
    private int height, left;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);
        initParams();
        initView();
        ContentService service = new ContentService(this);
        service.setCallBack(new CallBack());
        service.getOrderDetail(getOrderId());
    }

    private void initParams() {
        height = (int) getResources().getDimension(R.dimen.order_travel_time_height);
        left = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
    }

    private void initView() {
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        travel_people_container = (LinearLayout) findViewById(R.id.order_travel_people_container);
        pay_money_container = (LinearLayout) findViewById(R.id.order_pay_money_contaienr);
        post_img = (ImageView) findViewById(R.id.order_detail_img);
        title_tv = (TextView) findViewById(R.id.order_detail_title);
        packages_tv = (TextView) findViewById(R.id.order_packages_intro);
        travel_time_tv = (TextView) findViewById(R.id.order_travel_time);
        travel_person_num = (TextView) findViewById(R.id.order_person_num_tv);
        pay_money = (TextView) findViewById(R.id.order_pay_money);
        order_numb = (TextView) findViewById(R.id.order_num_text);
        order_created_time = (TextView) findViewById(R.id.order_created_time_text);
        pay_time = (TextView) findViewById(R.id.order_pay_time_text);
        pay_channel = (TextView) findViewById(R.id.order_pay_channel_text);
        evalute_time = (TextView) findViewById(R.id.order_evaluate_time_text);
        backImg.setOnClickListener(this);
    }

    public int getOrderId() {
        return getIntent().getIntExtra(KeyParams.ORDER_ID, -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back_btn:
                finish();
                break;

        }
    }

    class CallBack extends ICallBack {
        @Override
        public void onGetOrderDetail(OrderDetail detail) {
            updateView(detail);
        }


    }

    private void updateView(OrderDetail detail) {
        if (detail == null) {
            return;
        }
        DrawableUtils.displayImg(this, post_img, detail.getPhoto());
        title_tv.setText(detail.getName());
        travel_time_tv.setText(detail.getOrderDate());
        List<OrderDetail.ProduItemListBean> productList = detail.getProduItemList();
        if (productList != null && productList.size() > 0) {
            OrderDetail.ProduItemListBean item = productList.get(0);
            if (item != null) {
                packages_tv.setText(item.getName());
            }
        }
        setTravelPerson(detail.getPeopleItemList());
        int couponValue = detail.getCouponValue();
        setOrderPayPrice(detail.getOrderPayList(), couponValue);
        setCoupon(couponValue);
        order_numb.setText(detail.getOrderNumb());
        order_created_time.setText(detail.getCreatedTime());
        pay_time.setText(detail.getPayTime());
        String payType=detail.getPayType();
        if (payType==null){
            pay_channel.setText("未支付");
        }


    }

    private void setCoupon(int couponValue) {
        FrameLayout itemContainer = getItemContainer();
        pay_money_container.addView(itemContainer);
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView name = new TextView(this);
        setCouponText(name);
        name.setLayoutParams(params1);
        name.setText("优惠券");
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params2.gravity = Gravity.RIGHT;
        TextView value = new TextView(this);
        value.setLayoutParams(params2);
        setCouponText(value);
        value.setText("- ¥ " + couponValue);
        itemContainer.addView(name);
        itemContainer.addView(value);

    }

    @NonNull
    private FrameLayout getItemContainer() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        FrameLayout itemContainer = new FrameLayout(this);
        itemContainer.setLayoutParams(params);
        itemContainer.setPadding(left, 0, left, 0);
        return itemContainer;
    }


    public void setCouponText(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.color_ffaa2a));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.text_size_15_sp));
        textView.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void setTravelPerson(List<OrderDetail.PeopleItemListBean> peopleList) {
        if (peopleList != null && peopleList.size() > 0) {
            OrderDetail.PeopleItemListBean peopleItemListBean = peopleList.get(0);
            travel_person_num.setText(String.valueOf(peopleItemListBean.getAmount()));
            List<OrderDetail.PeopleItemListBean.ContactInfoListBean> contactInfoListBeenList = peopleItemListBean.getContactInfoList();
            for (OrderDetail.PeopleItemListBean.ContactInfoListBean contactInfoListBean : contactInfoListBeenList) {
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTextAppearance(this, R.style.OrderCardItemTextStyle);
                List<OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean> itemList = contactInfoListBean.getItemList();
                for (OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean item : itemList) {
                    if (item.getType() == 5) {
                        textView.setText(item.getValue());
                    }
                }
                travel_people_container.addView(textView);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                params.leftMargin = left;
                params.height = height;
            }

        }
    }


    private void setOrderPayPrice(List<OrderItem.OrderPay> orderPayList, int couponValue) {
        int orderPayPrice = 0;
        for (OrderItem.OrderPay orderPay : orderPayList) {
            FrameLayout itemContainer = getItemContainer();
            pay_money_container.addView(itemContainer);
            TextView name = new TextView(this);
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            name.setLayoutParams(params1);
            name.setTextAppearance(this, R.style.OrderCardItemTextStyle);
            name.setText(orderPay.getName() + "*" + orderPay.getAmount());
            name.setGravity(Gravity.CENTER_VERTICAL);
            TextView value = new TextView(this);
            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params2.gravity = Gravity.RIGHT;
            value.setLayoutParams(params2);
            value.setGravity(Gravity.CENTER_VERTICAL);
            value.setTextAppearance(this, R.style.OrderCardItemTextStyle);
            int toalPrice = (int) orderPay.getUnit() * orderPay.getAmount();
            orderPayPrice += toalPrice;
            value.setText("¥ " + String.valueOf(toalPrice));
            itemContainer.addView(name);
            itemContainer.addView(value);
        }
        int paymoeny = orderPayPrice - couponValue;
        paymoeny = Math.max(paymoeny, 0);
        pay_money.setText("¥ "+String.valueOf(paymoeny));

    }


}
