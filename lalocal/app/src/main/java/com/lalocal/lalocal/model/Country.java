package com.lalocal.lalocal.model;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2016/6/14.
 */
public class Country extends DataSupport{
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
