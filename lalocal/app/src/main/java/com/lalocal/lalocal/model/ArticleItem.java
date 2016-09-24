package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class ArticleItem extends SearchItem{

    /**
     * id : 7
     * targetType : 13
     * title : 和风浮动，于不经意间邂逅高山之颜
     * description : 一丝古老，几缕恬静，再加上充溢在街头巷尾、花木屋舍的浪漫情愫，然后就有了高山城。
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/2016032514521512145471396118
     * readNum : 1494
     * praiseNum : 1
     * publishedAt : 1459614719000
     * publishDate : 2016-04-03 00:31:59
     * authorId : 20
     * authorVO : {"authorId":20,"authorName":"haru","authorAvatar":"http://7xpid3.com1.z0.glb.clouddn.com/2016031709533814192507574438?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"","qrCode":null,"publicTitle":null,"publicDescription":null}
     * praiseFlag : false
     * praiseId : null
     * type : 1
     */

    private int targetType;
    private String description;
    private int readNum;
    private int praiseNum;
    private long publishedAt;
    private String publishDate;
    private int authorId;
    /**
     * authorId : 20
     * authorName : haru
     * authorAvatar : http://7xpid3.com1.z0.glb.clouddn.com/2016031709533814192507574438?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * description :
     * qrCode : null
     * publicTitle : null
     * publicDescription : null
     */

    private AuthorVOBean authorVO;
    private boolean praiseFlag;
    private Object praiseId;
    private int type;


    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public AuthorVOBean getAuthorVO() {
        return authorVO;
    }

    public void setAuthorVO(AuthorVOBean authorVO) {
        this.authorVO = authorVO;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class AuthorVOBean {
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
