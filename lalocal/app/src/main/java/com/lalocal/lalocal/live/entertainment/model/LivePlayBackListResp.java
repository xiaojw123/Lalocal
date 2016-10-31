package com.lalocal.lalocal.live.entertainment.model;

import com.lalocal.lalocal.model.LiveRowsBean;

import java.util.List;

/**
 * Created by android on 2016/10/11.
 */
public class LivePlayBackListResp {

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
        private int pageNumber;
        private boolean lastPage;
        private List<LiveRowsBean> rows;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public List<LiveRowsBean> getRows() {
            return rows;
        }

        public void setRows(List<LiveRowsBean> rows) {
            this.rows = rows;
        }


    }
}
