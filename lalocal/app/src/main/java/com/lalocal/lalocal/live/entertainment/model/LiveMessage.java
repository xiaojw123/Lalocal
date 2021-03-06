package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/9/20.
 */
public class LiveMessage {
    private String liveId;
    private String betGold;
    private String userRole;
    private String msg;
    private String isAt;
    private int style;
    private String scenesType;
    private String disableSendMsgUserId;
    private String disableSendMsgNickName;
    private String adminSendMsgUserId;
    private String adminSendMsgNickName;
    private String adminSendMsgImUserId;
    private String alarmLevel;
    private String userId;
    private String noticeUsers;
    private String questionUserName;
    private GiftBean giftModel;
    private String targetType;
    private ChallengeDetailsResp.ResultBean  challengeModel;

    private String channelId;


    public String getAdminSendMsgUserId() {
        return adminSendMsgUserId;
    }

    public void setAdminSendMsgUserId(String adminSendMsgUserId) {
        this.adminSendMsgUserId = adminSendMsgUserId;
    }

    public String getAdminSendMsgNickName() {
        return adminSendMsgNickName;
    }

    public void setAdminSendMsgNickName(String adminSendMsgNickName) {
        this.adminSendMsgNickName = adminSendMsgNickName;
    }

    public String getAdminSendMsgImUserId() {
        return adminSendMsgImUserId;
    }

    public void setAdminSendMsgImUserId(String adminSendMsgImUserId) {
        this.adminSendMsgImUserId = adminSendMsgImUserId;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(String onlineNum) {
        this.onlineNum = onlineNum;
    }

    private String onlineNum;

    public String getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(String creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    private String creatorAccount;

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getBetGold() {
        return betGold;
    }

    public void setBetGold(String betGold) {
        this.betGold = betGold;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIsAt() {
        return isAt;
    }

    public void setIsAt(String isAt) {
        this.isAt = isAt;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getScenesType() {
        return scenesType;
    }

    public void setScenesType(String scenesType) {
        this.scenesType = scenesType;
    }

    public String getDisableSendMsgUserId() {
        return disableSendMsgUserId;
    }

    public void setDisableSendMsgUserId(String disableSendMsgUserId) {
        this.disableSendMsgUserId = disableSendMsgUserId;
    }

    public String getDisableSendMsgNickName() {
        return disableSendMsgNickName;
    }

    public void setDisableSendMsgNickName(String disableSendMsgNickName) {
        this.disableSendMsgNickName = disableSendMsgNickName;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeUsers() {
        return noticeUsers;
    }

    public void setNoticeUsers(String noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public String getQuestionUserName() {
        return questionUserName;
    }

    public void setQuestionUserName(String questionUserName) {
        this.questionUserName = questionUserName;
    }

    public GiftBean getGiftModel() {
        return giftModel;
    }

    public void setGiftModel(GiftBean giftModel) {
        this.giftModel = giftModel;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public ChallengeDetailsResp.ResultBean  getChallengeModel() {
        return challengeModel;
    }

    public void setChallengeModel(ChallengeDetailsResp.ResultBean resultBean) {
        this.challengeModel = resultBean;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "LiveMessage{" +
                "liveId='" + liveId + '\'' +
                ", betGold='" + betGold + '\'' +
                ", userRole='" + userRole + '\'' +
                ", msg='" + msg + '\'' +
                ", isAt='" + isAt + '\'' +
                ", style=" + style +
                ", scenesType='" + scenesType + '\'' +
                ", disableSendMsgUserId='" + disableSendMsgUserId + '\'' +
                ", disableSendMsgNickName='" + disableSendMsgNickName + '\'' +
                ", adminSendMsgUserId='" + adminSendMsgUserId + '\'' +
                ", adminSendMsgNickName='" + adminSendMsgNickName + '\'' +
                ", adminSendMsgImUserId='" + adminSendMsgImUserId + '\'' +
                ", alarmLevel='" + alarmLevel + '\'' +
                ", userId='" + userId + '\'' +
                ", noticeUsers='" + noticeUsers + '\'' +
                ", questionUserName='" + questionUserName + '\'' +
                ", giftModel=" + giftModel +
                ", targetType='" + targetType + '\'' +
                ", challengeModel=" + challengeModel +
                ", channelId='" + channelId + '\'' +
                ", type='" + type + '\'' +
                ", onlineNum='" + onlineNum + '\'' +
                ", creatorAccount='" + creatorAccount + '\'' +
                '}';
    }
}
