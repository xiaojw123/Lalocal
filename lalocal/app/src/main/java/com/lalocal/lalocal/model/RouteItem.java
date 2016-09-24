package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class RouteItem extends SearchItem{

    /**
     * id : 33
     * title : 我们去海南岛
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/201606291745178216279920902
     * author : {"authorId":159,"authorName":"小LA","authorAvatar":"http://7xpid3.com1.z0.glb.clouddn.com/2016032416511212798078771516?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"我们拒绝大众无聊透顶的走马观花，我们前往，发现local生活，发现更有乐趣的一万种可能。 是旅行，更是寻找与体验。  ","qrCode":null,"publicTitle":null,"publicDescription":null}
     * praiseFlag : false
     * praiseId : null
     */


    /**
     * authorId : 159
     * authorName : 小LA
     * authorAvatar : http://7xpid3.com1.z0.glb.clouddn.com/2016032416511212798078771516?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * description : 我们拒绝大众无聊透顶的走马观花，我们前往，发现local生活，发现更有乐趣的一万种可能。 是旅行，更是寻找与体验。  
     * qrCode : null
     * publicTitle : null
     * publicDescription : null
     */

    private AuthorBean author;
    private boolean praiseFlag;
    private Object praiseId;

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public boolean isPraiseFlag() {
        return praiseFlag;
    }

    public void setPraiseFlag(boolean praiseFlag) {
        this.praiseFlag = praiseFlag;
    }

    public Object getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(Object praiseId) {
        this.praiseId = praiseId;
    }

    public static class AuthorBean {
        private int authorId;
        private String authorName;
        private String authorAvatar;
        private String description;
        private Object qrCode;
        private Object publicTitle;
        private Object publicDescription;

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorAvatar() {
            return authorAvatar;
        }

        public void setAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getQrCode() {
            return qrCode;
        }

        public void setQrCode(Object qrCode) {
            this.qrCode = qrCode;
        }

        public Object getPublicTitle() {
            return publicTitle;
        }

        public void setPublicTitle(Object publicTitle) {
            this.publicTitle = publicTitle;
        }

        public Object getPublicDescription() {
            return publicDescription;
        }

        public void setPublicDescription(Object publicDescription) {
            this.publicDescription = publicDescription;
        }
    }
}
