package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/7/11.
 */
public class ArticleDetailsResultBean {
        private int id;
        private int targetType;
        private String title;
        private String description;
        private String photo;
        private int readNum;
        private int praiseNum;
        private long publishedAt;
        private String publishDate;
        private int authorId;
        private SpecialAuthorBean authorVO;
        private boolean praiseFlag;
        private Object praiseId;
        private int type;
        private String subTitle;
        private String content;
        private String url;
        private SpecialShareVOBean shareVO;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTargetType() {
            return targetType;
        }

        public void setTargetType(int targetType) {
            this.targetType = targetType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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

        public SpecialAuthorBean getAuthorVO() {
            return authorVO;
        }

        public void setAuthorVO(SpecialAuthorBean authorVO) {
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

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public SpecialShareVOBean getShareVO() {
            return shareVO;
        }

        public void setShareVO(SpecialShareVOBean shareVO) {
            this.shareVO = shareVO;
        }

    }

