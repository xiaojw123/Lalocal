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
    private String style;
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
    private String challengeModel;

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
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

    public String getChallengeModel() {
        return challengeModel;
    }

    public void setChallengeModel(String challengeModel) {
        this.challengeModel = challengeModel;
    }
}
