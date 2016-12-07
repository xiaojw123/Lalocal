package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/11/25.
 */
public class LiveRoomAvatarSortResp {

    private int returnCode;
    private String message;
    private long date;

    private ResultBean result;
    private Object errorCode;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        private int number;
        private List<UserAvatarsBean> userAvatars;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public List<UserAvatarsBean> getUserAvatars() {
            return userAvatars;
        }

        public void setUserAvatars(List<UserAvatarsBean> userAvatars) {
            this.userAvatars = userAvatars;
        }


    }
}
