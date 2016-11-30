package com.lalocal.lalocal.model;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2016/11/21.
 */

public class MessageItem extends DataSupport{


    /**
     * id : 163
     * content : 阿萨德哈和
     * description : 的很好的话
     * targetId : 67
     * targetType : 10
     * targetUrl :
     * type : 1
     * createTime : 2016-08-19
     * timeStamp : 1471611186000
     */

    private int id;
    private String content;
    private String description;
    private int targetId;
    private int targetType;
    private String targetUrl;
    private int type;
    private String createTime;
    private long timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
