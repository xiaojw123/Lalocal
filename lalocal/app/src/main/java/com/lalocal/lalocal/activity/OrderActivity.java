package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements View.OnClickListener, CustomTitleView.onBackBtnClickListener {
    private LinearLayout travel_people_container;
    private LinearLayout pay_money_container;
    private FrameLayout pay_time_container;
    private FrameLayout evaluteTimeCotainer;
    private FrameLayout pay_channel_cotainer;
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
    private Button evalute_btn;
    private ImageView more_function_img;
    private OrderDetail mOrderDetail;
    private FrameLayout mTravelPersonContainer;
    private List<OrderDetail.PeopleItemListBean.ContactInfoListBean> travelpersonsInfo;
    /*补充信息模块 问题1 问题2。。动态添加*/
    //容器
    private LinearLayout additional_info_llt;

    //接送时间
//    private TextView shuttle_time_tv;
    //导游语言
//    private TextView guide_language_tv;
    /*   联系人模块*/
    private TextView contact_name_tv;
    private TextView contact_phone_tv;
    private TextView contact_email_tv;
    /* 其他*/
    //总金额
//    private double mAccoutPrice;
    //页面类型
    private boolean isPreView;
    private LinearLayout toalinfo_container;
    LayoutInflater mInflater;
    LinearLayout mOrderPayContainer;
    LinearLayout mPreViewContainer;
    LinearLayout mMoreContainer;
    RelativeLayout mTitleCotainer;
    View contentView;
    TextView cancel_order_tv, customer_service_tv;
    PopupWindow popupWindow;
    CustomTitleView order_title_ctv;
    /*    附加信息*/
    FrameLayout shuttle_up_container, remark_cotainer, language_cotainer;
    //接送地点
    private TextView shuttle_setup_tv, remark_tv, language_tv;
    boolean isCancelOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);
        ButterKnife.bind(this);
        AppLog.print("OrderActivity___oncreateView___order__" + getOrderId());
        initParams();
        initView();
        setLoaderCallBack(new CallBack());
        mContentloader.getOrderDetail(getOrderId());
    }

    private void initParams() {
        height = (int) getResources().getDimension(R.dimen.order_travel_time_height);
        left = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
        isPreView = getIntent().getBooleanExtra(KeyParams.PRE_VIEW_PARAMS, false);
        mInflater = LayoutInflater.from(this);
    }

    private void initView() {
        contentView = LayoutInflater.from(this).inflate(R.layout.orderdetail_more_operation, null);
        cancel_order_tv = (TextView) contentView.findViewById(R.id.morefunction_cancel_order);
        customer_service_tv = (TextView) contentView.findViewById(R.id.morefunction_customer_sevice);
        shuttle_up_container = (FrameLayout) findViewById(R.id.orderdetail_shuttle_setup_container);
        remark_cotainer = (FrameLayout) findViewById(R.id.orderdetail_remark_container);
        language_cotainer = (FrameLayout) findViewById(R.id.orderdetail_language_container);
        shuttle_setup_tv = (TextView) findViewById(R.id.orderdetail_shuttle_setup_tv);
        remark_tv = (TextView) findViewById(R.id.orderdetail_shuttle_remark_tv);
        language_tv = (TextView) findViewById(R.id.orderdetail_language_tv);
        order_title_ctv = (CustomTitleView) findViewById(R.id.orderdetail_title_ctv);
        mTitleCotainer = (RelativeLayout) findViewById(R.id.orderdetail_tiltle_container);
        mMoreContainer = (LinearLayout) findViewById(R.id.morefunction_container);
        mPreViewContainer = (LinearLayout) findViewById(R.id.order_preview_container);
        mOrderPayContainer = (LinearLayout) findViewById(R.id.order_pay_container);
        toalinfo_container = (LinearLayout) findViewById(R.id.order_toalinfo_container);
        additional_info_llt = (LinearLayout) findViewById(R.id.orderdetail_additional_info_container);
        contact_name_tv = (TextView) findViewById(R.id.orderdetail_contact_name_tv);
        contact_phone_tv = (TextView) findViewById(R.id.orderdetail_contact_phone_tv);
        contact_email_tv = (TextView) findViewById(R.id.orderdetail_contact_email_tv);
        mTravelPersonContainer = (FrameLayout) findViewById(R.id.order_person_container);
        more_function_img = (ImageView) findViewById(R.id.order_more_img);
        evalute_btn = (Button) findViewById(R.id.order_immediately_evaluate_btn);
        travel_people_container = (LinearLayout) findViewById(R.id.order_travel_people_container);
        pay_channel_cotainer = (FrameLayout) findViewById(R.id.order_pay_channel_container);
        pay_time_container = (FrameLayout) findViewById(R.id.order_pay_time_container);
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
        evaluteTimeCotainer = (FrameLayout) findViewById(R.id.order_evaluate_time_cotainer);
        order_title_ctv.setOnBackClickListener(this);
        cancel_order_tv.setOnClickListener(this);
        customer_service_tv.setOnClickListener(this);
        mTravelPersonContainer.setOnClickListener(this);
        evalute_btn.setOnClickListener(this);
        more_function_img.setOnClickListener(this);
        if (isPreView) {
            mOrderPayContainer.setVisibility(View.GONE);
            more_function_img.setVisibility(View.GONE);
            order_title_ctv.setTitle(getResources().getString(R.string.order_info));
        } else {
            mTitleCotainer.setOnClickListener(this);
            mOrderPayContainer.setVisibility(View.VISIBLE);
            mPreViewContainer.setVisibility(View.GONE);
            order_title_ctv.setTitle(getResources().getString(R.string.order_detail));
        }

    }

    private int getOrderId() {
        return getIntent().getIntExtra(KeyParams.ORDER_ID, -1);
    }

    private String getActionType() {
        return getIntent().getStringExtra(KeyParams.ACTION_TYPE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderdetail_tiltle_container:
                if (mOrderDetail != null) {
                    Intent intent1 = new Intent(this, ProductDetailsActivity.class);
                    SpecialToH5Bean stb = new SpecialToH5Bean();
                    stb.setTargetId(mOrderDetail.getProductionId());
                    intent1.putExtra("productdetails", stb);
                    startActivity(intent1);
                }
                break;

            case R.id.order_person_container:
                if (travelpersonsInfo != null) {
                    Intent intent = new Intent(this, TravelPersonActivity.class);
                    intent.putParcelableArrayListExtra(KeyParams.TRAVEL_PERSONS_CONCACT, (ArrayList<? extends Parcelable>) travelpersonsInfo);
                    startActivity(intent);
                }
                break;
            case R.id.order_immediately_evaluate_btn:
                String text = evalute_btn.getText().toString();
                if (getResources().getString(R.string.pay_immediately).equals(text)) {
                    if (KeyParams.ACTION_UPDATE_ORDER.equals(getActionType())) {
                        setResult(MyOrderActivity.UPDATE_MY_ORDER);
                    }
                    Intent intent = new Intent(this, PayActivity.class);
                    intent.putExtra(PayActivity.ORDER_ID, mOrderDetail.getId());
                    startActivity(intent);
                } else if (getResources().getString(R.string.evaluate_immediately).equals(text)) {
                    //TODO:评价功能待开发

                }
                break;
            case R.id.order_more_img:
                showPopWindow();
                break;
            case R.id.morefunction_customer_sevice:
                popupWindow.dismiss();
                startOnlineService();
                break;
            case R.id.morefunction_cancel_order:
                popupWindow.dismiss();
                if (mOrderDetail != null) {
                    mContentloader.cancelOrder(mOrderDetail.getId());
                }

                break;
        }
    }


    public void startOnlineService() {
        Intent intent = new Intent(this, ChatActivity.class);
        if (mOrderDetail != null) {
            intent.putExtra(Constant.ITEM_TITLE, mOrderDetail.getName());
            intent.putExtra(Constant.ITEM_POST_URL, mOrderDetail.getPhoto());
            intent.putExtra(Constant.ITEM_PRICE, String.valueOf(mOrderDetail.getFee()));
        }
        startActivity(intent);
    }

    public void showPopWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setContentView(contentView);
        }
        if (cancel_order_tv.getVisibility() == View.VISIBLE) {
            contentView.setPadding(0, ((int) getResources().getDimension(R.dimen.dimen_size_15_dp)), 0, ((int) getResources().getDimension(R.dimen.dimen_size_15_dp)));
        } else {
            contentView.setPadding(0, ((int) getResources().getDimension(R.dimen.dimen_size_10_dp)), 0, ((int) getResources().getDimension(R.dimen.dimen_size_10_dp)));
        }
        popupWindow.showAsDropDown(more_function_img, 0, -contentView.getPaddingTop());
    }

    @Override
    public void onBackClick() {
        setBackResult();
    }

    @Override
    public void onBackPressed() {
        setBackResult();
        super.onBackPressed();

    }

    public void setBackResult() {
        AppLog.print("setBackResult___actionType___");
        if (!KeyParams.ACTION_UPDATE_ORDER.equals(getActionType())) {
            setResult(PayActivity.RESULT_BACK_PRODUCT);
        } else {
            Intent intent = new Intent();
            sendBroadcast(intent);
        }

    }


    class CallBack extends ICallBack {
        @Override
        public void onGetOrderDetail(OrderDetail detail) {
            mOrderDetail = detail;
            if (isCancelOrder) {
                isCancelOrder = false;
                updateModuleOrderInfo();
            } else {
                updateView();
            }
        }

        @Override
        public void onCancelSuccess() {
            if (cancel_order_tv.getVisibility() == View.VISIBLE) {
                cancel_order_tv.setVisibility(View.GONE);
            }
            isCancelOrder = true;
            if (KeyParams.ACTION_UPDATE_ORDER.equals(getActionType())) {
                setResult(MyOrderActivity.UPDATE_MY_ORDER);
            }
            mContentloader.getOrderDetail(getOrderId());
        }
    }

    private void updateView() {
        if (mOrderDetail != null) {
            title_tv.setText(mOrderDetail.getName());
            DrawableUtils.displayImg(this, post_img, mOrderDetail.getPhoto());
            updateModuleContacts();
            updateModuleTravelInfo();
            updateModuleTravelPerson();
            if (!isPreView) {
                updateModulePayAmount();
                updateModuleOrderInfo();
            }
        }

    }

    private void updateModuleOrderInfo() {
        int status = mOrderDetail.getStatus();
        AppLog.print("order status____" + status);
//        订单状态 已取消/已预订(未支付)/已支付/已消费/已评价/申请退款/退款中/已退款/退款失败
        switch (status) {
            case 0://已取消
                evalute_btn.setVisibility(View.GONE);
                pay_time_container.setVisibility(View.GONE);
                pay_channel_cotainer.setVisibility(View.GONE);
                evaluteTimeCotainer.setVisibility(View.GONE);
                break;
            case 1://未支付
                evalute_btn.setVisibility(View.VISIBLE);
                evalute_btn.setEnabled(true);
                evalute_btn.setText(getResources().getString(R.string.pay_immediately));
                evalute_btn.setEnabled(true);
                evalute_btn.setBackgroundColor(getResources().getColor(R.color.color_ffaa2a));
                pay_time_container.setVisibility(View.GONE);
                pay_channel_cotainer.setVisibility(View.GONE);
                evaluteTimeCotainer.setVisibility(View.GONE);
                cancel_order_tv.setVisibility(View.VISIBLE);
                break;
            case 2://已支付
                evalute_btn.setVisibility(View.GONE);
                pay_time_container.setVisibility(View.VISIBLE);
                pay_channel_cotainer.setVisibility(View.VISIBLE);
                evaluteTimeCotainer.setVisibility(View.GONE);
                break;
            case 3://已消费k
                evalute_btn.setVisibility(View.VISIBLE);
                evalute_btn.setEnabled(false);
                evalute_btn.setText(getResources().getString(R.string.evaluate_immediately));
                evalute_btn.setBackgroundColor(getResources().getColor(R.color.color_b3));
                pay_time_container.setVisibility(View.VISIBLE);
                pay_channel_cotainer.setVisibility(View.VISIBLE);
                evaluteTimeCotainer.setVisibility(View.GONE);
                break;
            case 4:
                evalute_btn.setVisibility(View.GONE);
                pay_time_container.setVisibility(View.VISIBLE);
                pay_channel_cotainer.setVisibility(View.VISIBLE);
                evaluteTimeCotainer.setVisibility(View.VISIBLE);
                break;
//            /申请退款/退款中/已退款/退款失败
            default:
                evalute_btn.setVisibility(View.GONE);
                pay_time_container.setVisibility(View.GONE);
                pay_channel_cotainer.setVisibility(View.GONE);
                evaluteTimeCotainer.setVisibility(View.GONE);
                break;
        }
        order_numb.setText(mOrderDetail.getOrderNumb());
        order_created_time.setText(mOrderDetail.getCreatedTime());
        if (pay_time_container.getVisibility() == View.VISIBLE) {
            pay_time.setText(mOrderDetail.getPayTime());
        }
        if (pay_channel_cotainer.getVisibility() == View.VISIBLE) {
            // 支付方式 0：支付宝app支付 1：微信app支付 2：支付宝手机网页支付 3：微信公众号支付 4:信程支付 5：优惠券支付
            switch (mOrderDetail.getPayType()) {
                case 0:
                    pay_channel.setText("支付宝");
                    break;
                case 1:
                    pay_channel.setText("微信");
                    ;
                    break;
                case 2:
                    pay_channel.setText("支付宝手机网页");
                    break;
                case 3:
                    pay_channel.setText("微信公众号");
                    break;
                case 4:
                    pay_channel.setText("信程");
                    break;
                case 5:
                    pay_channel.setText("优惠券");
                    break;
            }
        }
        if (evaluteTimeCotainer.getVisibility() == View.VISIBLE) {
            evalute_time.setText(mOrderDetail.getAppraiseTime());
        }
    }


    private void updateModuleTravelInfo() {
        travel_time_tv.setText(mOrderDetail.getOrderDate());
        List<OrderDetail.ProduItemListBean> productList = mOrderDetail.getProduItemList();
        if (productList != null && productList.size() > 0) {
            OrderDetail.ProduItemListBean item = productList.get(0);
            if (item != null) {
                packages_tv.setText(item.getName());
            }

        }

        showAdddtionInfo();

    }

    private void showAdddtionInfo() {
        String pkiup = mOrderDetail.getPickUpPoint();
        String language = mOrderDetail.getLanguageName();
        String remark = mOrderDetail.getRemark();

        if (!TextUtils.isEmpty(pkiup)) {
            if (additional_info_llt.getVisibility() != View.VISIBLE) {
                additional_info_llt.setVisibility(View.VISIBLE);
            }
            shuttle_up_container.setVisibility(View.VISIBLE);
            shuttle_setup_tv.setText(pkiup);
        }
        if (!TextUtils.isEmpty(language)) {
            if (additional_info_llt.getVisibility() != View.VISIBLE) {
                additional_info_llt.setVisibility(View.VISIBLE);
            }
            language_cotainer.setVisibility(View.VISIBLE);
            language_tv.setText(language);
        }
        if (!TextUtils.isEmpty(remark)) {
            if (additional_info_llt.getVisibility() != View.VISIBLE) {
                additional_info_llt.setVisibility(View.VISIBLE);
            }
            remark_cotainer.setVisibility(View.VISIBLE);
            remark_tv.setText(remark);
        }
        List<OrderDetail.Question> questions = mOrderDetail.getOrderQuestions();
        if (questions != null && questions.size() > 0) {
            if (additional_info_llt.getVisibility() != View.VISIBLE) {
                additional_info_llt.setVisibility(View.VISIBLE);
            }
            int i = 0;
            for (OrderDetail.Question question : questions) {
                View quetionView = getQuestionItemView();
                TextView question_sort = (TextView) quetionView.findViewById(R.id.question_sort);
                TextView question_content = (TextView) quetionView.findViewById(R.id.question_content);
                TextView question_answer = (TextView) quetionView.findViewById(R.id.question_answer);
                ++i;
                question_sort.setText("问题" + i);
                question_content.setText(question.getContent());
                question_answer.setText(question.getAnswer());
                additional_info_llt.addView(quetionView);
            }
        }


    }

    public View getQuestionItemView() {
        View questionView = mInflater.inflate(R.layout.question_item, additional_info_llt, false);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) questionView.getLayoutParams();
        params.topMargin = (int) getResources().getDimension(R.dimen.dimen_size_8_dp);
        params.leftMargin = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
        params.rightMargin = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
        return questionView;

    }

    private void updateModuleContacts() {
        contact_name_tv.setText(mOrderDetail.getUserName());
        contact_phone_tv.setText(mOrderDetail.getPhone());
        contact_email_tv.setText(mOrderDetail.getEmail());
    }

    private void setCoupon(double couponValue) {
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

    private void updateModuleTravelPerson() {
        List<OrderDetail.PeopleItemListBean> peopleList = mOrderDetail.getPeopleItemList();
        if (peopleList != null && peopleList.size() > 0) {
            OrderDetail.PeopleItemListBean peopleItemListBean = peopleList.get(0);
            travel_person_num.setText(String.valueOf(peopleItemListBean.getAmount()));
            travelpersonsInfo = peopleItemListBean.getContactInfoList();
            for (OrderDetail.PeopleItemListBean.ContactInfoListBean contactInfoListBean : travelpersonsInfo) {
                if (isPreView) {
                    List<OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean> itemList = contactInfoListBean.getItemList();
                    if (itemList.size() > 0) {

                        View itemView = mInflater.inflate(R.layout.travel_person_item, toalinfo_container, false);
                        LinearLayout.LayoutParams itemParams = (LinearLayout.LayoutParams) itemView.getLayoutParams();
                        itemParams.topMargin = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
                        TextView fullname_tv = (TextView) itemView.findViewById(R.id.travel_person_name);
                        ImageView leader_ic = (ImageView) itemView.findViewById(R.id.travel_person_leader_ic);
                        TextView phone_tv = (TextView) itemView.findViewById(R.id.travel_person_mobile_numb);
                        TextView email_tv = (TextView) itemView.findViewById(R.id.travel_person_email);
                        TextView sex_tv = (TextView) itemView.findViewById(R.id.travel_person_sex);
                        FrameLayout phoneCotainer = (FrameLayout) itemView.findViewById(R.id.travelperson_phone_container);
                        FrameLayout emailCotainer = (FrameLayout) itemView.findViewById(R.id.travelperson_email_cotainer);
                        FrameLayout sexCotainer = (FrameLayout) itemView.findViewById(R.id.travelperson_sex_container);
                        String sName = "";
                        String sValue = "";
                        for (OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean item : itemList) {
//                            String code = item.getCode();
                            int type = item.getType();
                            String value = item.getValue();

                            switch (type) {
                                case 1: // isleader  1: true 0: false
                                    sexCotainer.setVisibility(View.VISIBLE);
                                    sex_tv.setText(value);
                                    break;
                                case 8:
                                    if ("1".equals(value)) {
                                        fullname_tv.setTextColor(getResources().getColor(R.color.color_ffaa2a));
                                        leader_ic.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 0:
                                    emailCotainer.setVisibility(View.VISIBLE);
                                    email_tv.setText(value);

                                    break;
                                case 3:
                                    phoneCotainer.setVisibility(View.VISIBLE);
                                    phone_tv.setText(value);
                                    break;
                                case 9:
                                    sName = value;
                                    break;
                                case 5:
                                    sValue = value;
                                    break;
                            }

                        }
                        fullname_tv.setText(sName + "  " + sValue);
                        if (mPreViewContainer.getVisibility() != View.VISIBLE) {
                            mPreViewContainer.setVisibility(View.VISIBLE);
                        }
                        if (leader_ic.getVisibility() == View.VISIBLE) {
                            mPreViewContainer.addView(itemView, 0);
                        } else {
                            mPreViewContainer.addView(itemView);
                        }

                    }


                } else {
                    List<OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean> itemList = contactInfoListBean.getItemList();
                    if (itemList.size() > 0) {
                        travel_people_container.setVisibility(View.VISIBLE);
                        FrameLayout container = null;
                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.order_travel_time_height));
                        textView.setLayoutParams(params1);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_15_sp));
                        textView.setSingleLine();
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        String sName="";
                        String sValue="";
                        for (OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean item : itemList) {
                            String code = item.getCode();
                            int type = item.getType();
                            String value = item.getValue();
                            if (type == 8) {
                                if ("1".equals(value)) {
                                    container = new FrameLayout(this);
                                    container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    ImageView leadImg = new ImageView(this);
                                    leadImg.setBackgroundResource(R.drawable.teamleader_ic);
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
                                    params.rightMargin = (int) getResources().getDimension(R.dimen.dimen_size_15_dp);
                                    leadImg.setLayoutParams(params);
                                    container.addView(textView);
                                    container.addView(leadImg);
                                }
                            } else {
                                if (type==9) {
                                    sName=item.getValue();
                                } else if (type==5) {
                                    sValue=item.getValue();
                                }
                            }
                        }
                        textView.setText(sName+"  "+sValue);
                        if (container != null) {
                            textView.setTextColor(getResources().getColor(R.color.color_ffaa2a));
                            travel_people_container.addView(container, 2);
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
                            params.leftMargin = left;
                            params.height = height;
                            params.rightMargin = height;
                        } else {
                            textView.setTextColor(getResources().getColor(R.color.color_b3));
                            travel_people_container.addView(textView);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                            params.leftMargin = left;
                            params.height = height;
                            params.rightMargin = height;
                        }

                    }
                }
            }
        }
    }


    private void updateModulePayAmount() {
        List<OrderDetail.OrderPayListBean> orderPayList = mOrderDetail.getOrderPayList();
        double couponValue = mOrderDetail.getCouponValue();
        if (couponValue > 0) {
            setCoupon(couponValue);
        }
        for (OrderDetail.OrderPayListBean orderPay : orderPayList) {
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
            double toalPrice = orderPay.getUnit() * orderPay.getAmount();
            value.setText(CommonUtil.formartOrderPrice(toalPrice));
            itemContainer.addView(name);
            itemContainer.addView(value);
        }
        pay_money.setText(CommonUtil.formartOrderPrice(mOrderDetail.getFee()));

    }


}
