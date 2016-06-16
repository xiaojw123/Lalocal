package com.lalocal.lalocal.service.callback;

import com.lalocal.lalocal.model.User;

/**
 * Created by xiaojw on 2016/6/1.
 */
public abstract class ICallBack {


    public void onUpHeaderComplete(){

    }

    public void onResetPasswordComplete(int code,String msg){

    }

    public void onResigterComplete(String resultCode, String message, String email, String psw) {

    }

    public void onCheckEmail(boolean isChecked,String email) {


    }
    public void onSendVerCode(int code,String email){

    }


    public void onLoginSucess(String code, String message, User user) {


    }

    public void onRequestFailed(String  error) {


    }


}
