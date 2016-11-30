package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/11/28.
 */

public class MeItem {
   private int id;
   private String name;
   private int drawableId;
   private String msgCount;

    public MeItem(int id,String name,int drawableId){
        this.id=id;
        this.name=name;
        this.drawableId=drawableId;


    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
