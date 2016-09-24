package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.util.AppLog;

import java.util.List;

public class TravelPersonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_person_layout);
        initView();

    }

    public List<OrderDetail.PeopleItemListBean.ContactInfoListBean> getPersonsContactInfo() {
        return getIntent().getParcelableArrayListExtra(KeyParams.TRAVEL_PERSONS_CONCACT);
    }

    private void initView() {
        LinearLayout persons_container = (LinearLayout) findViewById(R.id.travel_persons_container);
        List<OrderDetail.PeopleItemListBean.ContactInfoListBean> personsContact = getPersonsContactInfo();
        AppLog.print("contact size___" + personsContact.size());
        LayoutInflater inflater = LayoutInflater.from(this);
        for (OrderDetail.PeopleItemListBean.ContactInfoListBean bean : personsContact) {
            View itemView = inflater.inflate(R.layout.travel_person_item, persons_container, false);
            ImageView leaderIc = (ImageView) itemView.findViewById(R.id.travel_person_leader_ic);
            TextView nameChTv = (TextView) itemView.findViewById(R.id.travel_person_name);
            TextView mobileNumbTv = (TextView) itemView.findViewById(R.id.travel_person_mobile_numb);
            TextView emailTv = (TextView) itemView.findViewById(R.id.travel_person_email);
            TextView sexTv = (TextView) itemView.findViewById(R.id.travel_person_sex);
            FrameLayout phoneCotainer= (FrameLayout) itemView.findViewById(R.id.travelperson_phone_container);
            FrameLayout emailCotainer= (FrameLayout) itemView.findViewById(R.id.travelperson_email_cotainer);
            FrameLayout sexCotainer= (FrameLayout) itemView.findViewById(R.id.travelperson_sex_container);
            List<OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean> contacts = bean.getItemList();
            String enName = "";
            for (OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean contact : contacts) {
                String value = contact.getValue();
                switch (contact.getCode()) {
                    case "7": // isleader  1: true 0: false
                        if (!TextUtils.isEmpty(value)){
                            sexCotainer.setVisibility(View.VISIBLE);
                        sexTv.setText(value);
                        }
                        break;
                    case "-1":
                        if ("1".equals(value)){
                        nameChTv.setTextColor(getResources().getColor(R.color.color_ffaa2a));
                        leaderIc.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "2":
                        if (!TextUtils.isEmpty(value)){
                            emailCotainer.setVisibility(View.VISIBLE);
                        emailTv.setText(value);
                        }
                        break;
                    case "12":
                        if (!TextUtils.isEmpty(value)){
                            phoneCotainer.setVisibility(View.VISIBLE);
                        mobileNumbTv.setText(value);
                        }
                        break;
                    case "0":
                        enName += value;
                        break;
                    case "1":
                        enName += "  " + value;
                        break;
                }

            }
            nameChTv.setText(enName);
            if (leaderIc.getVisibility()==View.VISIBLE){
                persons_container.addView(itemView,0);
            }else{
                persons_container.addView(itemView);
            }

        }


    }

}
