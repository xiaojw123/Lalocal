package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/12/14.
 */
public class PlayBackReviewResultBean{
        private int pageNumber;
        private int totalPages;
        private int pageSize;
        private int totalRows;
        private List<PlayBackReviewRowsBean> rows;
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

        public List<PlayBackReviewRowsBean> getRows() {
            return rows;
        }

        public void setRows(List<PlayBackReviewRowsBean> rows) {
            this.rows = rows;
        }


}
