package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
            View itemView = inflater.inflate(R.layout.travel_person_item, persons_container, true);
            TextView fullNameTv = (TextView) itemView.findViewById(R.id.travel_person_fullname);
            TextView surnNameEnTv = (TextView) itemView.findViewById(R.id.travel_person_surname);
            TextView nameEnTv = (TextView) itemView.findViewById(R.id.travel_person_name);
            TextView mobileNumbTv = (TextView) itemView.findViewById(R.id.travel_person_mobile_numb);
            TextView emailTv = (TextView) itemView.findViewById(R.id.travel_person_email);
            TextView sexTv = (TextView) itemView.findViewById(R.id.travel_person_sex);
            List<OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean> contacts = bean.getItemList();
            String fullName = "";
            for (OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean contact : contacts) {
                AppLog.print("contants_name_____" + contact.getName() + ",,,,, value...." + contact.getValue()+"_code___"+contact.getCode());
                String value = contact.getValue();
                switch (contact.getCode()) {
                    case "-1": // isleader  1: true 0: false
                        String sex = value == "1" ? "女" : "男";
                        sexTv.setText(sex);
                        break;
                    case "2":
                        emailTv.setText(value);
                        break;
                    case "12":
                        mobileNumbTv.setText(value);
                        break;
                    case "0":
                        surnNameEnTv.setText("姓(拼音)： "+value);
                        fullName += value;
                        break;
                    case "1":
                        nameEnTv.setText("名(拼音)： "+value);
                        fullName += "  " + value;
                        break;
                }

            }
            fullNameTv.setText(fullName);
        }


    }

}
