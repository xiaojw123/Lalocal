package com.lalocal.lalocal.model;

import org.litepal.crud.DataSupport;

/**
 * Created by android on 2016/11/19.
 */
public class LiveHistoryItem extends DataSupport {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
