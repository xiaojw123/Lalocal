package com.lalocal.lalocal.model;

import android.annotation.SuppressLint;
import android.os.Parcel;

/**
 * Created by xiaojw on 2016/6/16.
 */
@SuppressLint("ParcelCreator")
public class LoginUser extends User{
    boolean sex;
    String phone;
    String areaCode;




    public LoginUser(Parcel in) {
        super(in);
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
