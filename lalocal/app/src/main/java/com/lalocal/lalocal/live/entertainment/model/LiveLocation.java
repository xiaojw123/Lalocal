package com.lalocal.lalocal.live.entertainment.model;

import org.litepal.crud.DataSupport;

/**
 * Created by android on 2016/11/19.
 */
public class LiveLocation extends DataSupport {
    private String code_plus;
    private String name;
    public String getName() {
        return name;
    }

    public String getCodePlus() {
        return code_plus;
    }

    public void setCodePlus(String code_plus) {
        this.code_plus = code_plus;
    }

    public void setName(String name) {
        this.name = name;
    }
}
