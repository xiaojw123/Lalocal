package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderDetail;

import java.util.List;

public class TravelPersonActivity extends BaseActivity implements View.OnClickListener {

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
        ImageView backImg= (ImageView) findViewById(R.id.common_back_btn);
        backImg.setOnClickListener(this);
        LinearLayout persons_container = (LinearLayout) findViewById(R.id.travel_persons_container);
        List<OrderDetail.PeopleItemListBean.ContactInfoListBean> personsContact = getPersonsContactInfo();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (OrderDetail.PeopleItemListBean.ContactInfoListBean bean : personsContact) {
            View itemView = inflater.inflate(R.layout.travel_person_item, persons_container, true);
            TextView chNameTv = (TextView) itemView.findViewById(R.id.travel_person_name_ch);
            TextView enNameTv = (TextView) itemView.findViewById(R.id.travel_person_name_en);
            TextView mobileNumbTv = (TextView) itemView.findViewById(R.id.travel_person_mobile_numb);
            TextView emailTv = (TextView) itemView.findViewById(R.id.travel_person_email);
            TextView sexTv = (TextView) itemView.findViewById(R.id.travel_person_sex);
            List<OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean> contacts = bean.getItemList();
            for (OrderDetail.PeopleItemListBean.ContactInfoListBean.ItemListBean contact : contacts) {
                String value = contact.getValue();
                switch (contact.getName()) {
                    case "邮件":
                        emailTv.setText(value);
                        break;
                    case "性别":
                        sexTv.setText(value);
                        break;
                    case "电话":
                        mobileNumbTv.setText(value);
                        break;
                    case "姓名":
                        chNameTv.setText(value);
                        break;
                    case "拼音":
                        enNameTv.setText(value);
                        break;
                }

            }


        }


    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
