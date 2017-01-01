package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/11/28.
 */

public class MeItem {
   private int id;
   private String name;
   private int drawableId;
   private int msgCount;

    public MeItem(int id,String name,int drawableId){
        this.id=id;
        this.name=name;
        this.drawableId=drawableId;


    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
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
