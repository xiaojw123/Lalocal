package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by xiaojw on 2016/9/3.
 * 积分、乐钻日志通用
 */
public class ConsumeRecord {


    /**
     * pageNumber : 1
     * totalPages : 1
     * pageSize : 10
     * totalRows : 1
     * rows : [{"id":108,"value":17,"channel":"直播","incomeFlag":true,"date":"2016-08-15 11:33:34"}]
     */

    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private int totalRows;
    /**
     * id : 108
     * value : 17
     * channel : 直播
     * incomeFlag : true
     * date : 2016-08-15 11:33:34
     */

    private List<RowsBean> rows;

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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private int id;
        private int value;
        private String channel;
        private boolean incomeFlag;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public boolean isIncomeFlag() {
            return incomeFlag;
        }

        public void setIncomeFlag(boolean incomeFlag) {
            this.incomeFlag = incomeFlag;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
