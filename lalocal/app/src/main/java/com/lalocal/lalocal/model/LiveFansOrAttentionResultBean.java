package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by android on 2016/8/7.
 */
public class LiveFansOrAttentionResultBean {

        private int pageNumber;
        private int totalPages;
        private int pageSize;
        private int totalRows;
        private List<LiveFansOrAttentionRowsBean> rows;
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

        public List<LiveFansOrAttentionRowsBean> getRows() {
            return rows;
        }

        public void setRows(List<LiveFansOrAttentionRowsBean> rows) {
            this.rows = rows;
        }


}
