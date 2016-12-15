package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/11/17.
 */

public class RecommendationsResp {

    /**
     * returnCode : 0
     * message : success
     * date : 1479370414357
     * result : {"id":30,"title":"啊快来咯哦","targetId":3511,"address":"LALOCAL神秘之地","type":1,"photo":"http://media.lalocal.cn/2016110714263218518017807781","user":{"id":8746,"nickName":"杜瑶歌曲","email":"duyaoyao@mddtrip.cn","avatar":"http://media.lalocal.cn/2016072917572116882175413670?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"我们的确如此，我们的确如此，我们的确如此，我们的确如此，我们的确如此，我们的确如此，我们的确如此，我","avatarOrigin":"http://media.lalocal.cn/2016072917572116882175413670","sortValue":168644}}
     * errorCode : 0
     */

    private int returnCode;
    private String message;
    private long date;
    private RecommendationsBean result;
    private int errorCode;


    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public RecommendationsBean getResult() {
        return result;
    }

    public void setResult(RecommendationsBean result) {
        this.result = result;
    }
}
