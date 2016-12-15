package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/8/17.
 */
public class TouristInfoResp {

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
        private String token;
        private String accid;
        private Object name;
        private Object gender;
        private Object icon;
        private Object sign;
        private Object email;
        private Object birth;
        private Object mobile;
        private Object ex;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

        public Object getSign() {
            return sign;
        }

        public void setSign(Object sign) {
            this.sign = sign;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getBirth() {
            return birth;
        }

        public void setBirth(Object birth) {
            this.birth = birth;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getEx() {
            return ex;
        }

        public void setEx(Object ex) {
            this.ex = ex;
        }
    }
}
