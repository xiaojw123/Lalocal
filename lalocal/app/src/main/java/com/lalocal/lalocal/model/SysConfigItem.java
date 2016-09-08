package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/9/1.
 */
public class SysConfigItem {


    /**
     * id : 0
     * enumKey : welcome_img
     * enumValue : null
     * type : 0
     * name : 开机欢迎页
     * createdAt : 1452324145000
     * createdBy : null
     * updatedAt : 1466384365000
     * updatedBy : null
     */

    private int id;
    private String enumKey;
    private String enumValue;
    private int type;
    private String name;
    private long createdAt;
    private Object createdBy;
    private long updatedAt;
    private Object updatedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnumKey() {
        return enumKey;
    }

    public void setEnumKey(String enumKey) {
        this.enumKey = enumKey;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }
}
