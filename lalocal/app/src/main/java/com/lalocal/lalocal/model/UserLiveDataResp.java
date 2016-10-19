package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/10/14.
 */
public class UserLiveDataResp {

    /**
     * returnCode : 0
     * message : success
     * date : 1476435829891
     * result : {"pageNumber":1,"totalPages":1,"pageSize":10,"totalRows":4,"rows":[]}
     * errorCode : 0
     */

    private int returnCode;
    private String message;
    private long date;
    /**
     * pageNumber : 1
     * totalPages : 1
     * pageSize : 10
     * totalRows : 4
     * rows : []
     */

    private ResultBean result;
    private int errorCode;

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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        private int pageNumber;
        private int totalPages;
        private int pageSize;
        private int totalRows;
        private List<LiveRowsBean> rows;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public List<?> getRows() {
            return rows;
        }

        public void setRows(List<LiveRowsBean> rows) {
            this.rows = rows;
        }
    }
}
