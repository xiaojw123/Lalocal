package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/11/25.
 */
public class LiveRoomAvatarSortResp {

    private int returnCode;
    private String message;
    private long date;

    private ResultBean result;
    private Object errorCode;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        private int number;
        private List<UserAvatarsBean> userAvatars;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public List<UserAvatarsBean> getUserAvatars() {
            return userAvatars;
        }

        public void setUserAvatars(List<UserAvatarsBean> userAvatars) {
            this.userAvatars = userAvatars;
        }

        public static class UserAvatarsBean {
            private boolean sex;
            private int id;
            private String nickName;
            private String avatar;
            private String description;
            private int role;
            private Object fansNum;
            private Object attentionNum;
            private AttentionVOBean attentionVO;
            private String avatarOrigin;
            private String accId;

            public boolean isSex() {
                return sex;
            }

            public void setSex(boolean sex) {
                this.sex = sex;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            public Object getFansNum() {
                return fansNum;
            }

            public void setFansNum(Object fansNum) {
                this.fansNum = fansNum;
            }

            public Object getAttentionNum() {
                return attentionNum;
            }

            public void setAttentionNum(Object attentionNum) {
                this.attentionNum = attentionNum;
            }

            public AttentionVOBean getAttentionVO() {
                return attentionVO;
            }

            public void setAttentionVO(AttentionVOBean attentionVO) {
                this.attentionVO = attentionVO;
            }

            public String getAvatarOrigin() {
                return avatarOrigin;
            }

            public void setAvatarOrigin(String avatarOrigin) {
                this.avatarOrigin = avatarOrigin;
            }

            public String getAccId() {
                return accId;
            }

            public void setAccId(String accId) {
                this.accId = accId;
            }

            public static class AttentionVOBean {
                private Object status;

                public Object getStatus() {
                    return status;
                }

                public void setStatus(Object status) {
                    this.status = status;
                }
            }
        }
    }
}
