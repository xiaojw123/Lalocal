package com.lalocal.lalocal.model;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class HistoryItem extends DataSupport {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
