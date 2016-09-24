package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/9/20.
 */
public class ArticlesResultBean {

    /**
     * pageNumber : 4
     * totalPages : 18
     * pageSize : 10
     * totalRows : 177
     * rows : []
     */

    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private int totalRows;
    private List<ArticleDetailsResultBean> rows;

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

    public List<ArticleDetailsResultBean> getRows() {
        return rows;
    }

    public void setRows(List<ArticleDetailsResultBean> rows) {
        this.rows = rows;
    }
}
