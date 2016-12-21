package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/12/19.
 */

public class CommentsResp {

    /**
     * returnCode : 0
     * message : success
     * date : 1482117913104
     * result : {"pageNumber":1,"totalPages":1,"pageSize":10,"totalRows":0,"rows":[]}
     * errorCode : 0
     */

    private int returnCode;
    private String message;
    private long date;
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
        /**
         * pageNumber : 1
         * totalPages : 1
         * pageSize : 10
         * totalRows : 0
         * rows : []
         */

        private int pageNumber;
        private int totalPages;
        private int pageSize;
        private int totalRows;
        private List<CommentRowBean> rows;

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

        public List<CommentRowBean> getRows() {
            return rows;
        }

        public void setRows(List<CommentRowBean> rows) {
            this.rows = rows;
        }
    }
}
