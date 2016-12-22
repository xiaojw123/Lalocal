package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/12/16.
 */
public class PlayBackMsgResultBean {
    private int returnCode;
    private String message;
    private long date;
    private Object errorCode;
    private List<ResultBean> result;
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

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int style;
        private int userId;
        private String content;
        private long sendAt;
        private String fromNick;
        private Object giftModel;

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getSendAt() {
            return sendAt;
        }

        public void setSendAt(long sendAt) {
            this.sendAt = sendAt;
        }

        public String getFromNick() {
            return fromNick;
        }

        public void setFromNick(String fromNick) {
            this.fromNick = fromNick;
        }

        public Object getGiftModel() {
            return giftModel;
        }

        public void setGiftModel(Object giftModel) {
            this.giftModel = giftModel;
        }
    }
}
