package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/12/5.
 */

public class ChannelIndexTotalResult {
    /**
     * channelList : [{"id":528,"title":"我们的生活","photo":"http://media.lalocal.cn/201609142100087203199938218","user":{"id":9308,"nickName":"幸福的四叶草NT","email":"1234324","avatar":"http://media.lalocal.cn/201609142100087203199938218?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"","avatarOrigin":"http://media.lalocal.cn/201609142100087203199938218","sortValue":63354},"onlineUser":56,"status":1,"type":2,"style":0,"cid":null,"cname":"dev-528-42","pushUrl":null,"pullUrl":"rtmp://pili-live-rtmp.lalocal.cn/lalocal/11812-dev-528-42","rtmpUrl":"rtmp://pili-live-rtmp.lalocal.cn/lalocal/11812-dev-528-42","httpUrl":"http://pili-live-hls.lalocal.cn/lalocal/11812-dev-528-42.m3u8","hlsPullUrl":"http://pili-live-hdl.lalocal.cn/lalocal/11812-dev-528-42.flv","annoucement":"欢迎来到乐可旅行世界美景直播间，我们提倡绿色直播，拒绝低俗、引诱、色情、暴露等直播内容，若管理员发现违规内容会给予直接禁号处理，管理员24小时在线巡查","shareVO":{"url":"https://dev.lalocal.cn/wechat/channel?id=528","title":"我美我帅我出门，你丑你宅家里蹲。幸福的四叶草NT正在乐可旅行直播我们的生活，快来围观~","desc":"我美我帅我出门，你丑你宅家里蹲。幸福的四叶草NT正在乐可旅行直播我们的生活，快来围观~","img":"http://media.lalocal.cn/201609142100087203199938218?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","type":3,"targetType":15,"targetId":528},"roomId":4968334,"createrAccId":"user_9308","address":"中国∙杭州市","number":42,"challengeStatus":false,"addressList":null,"lastMsg":"yyyy:Jdbdbdfjjfjfjf","direction":1}]
     * lastDynamicUser : null
     * historyList : {"pageNumber":1,"lastPage":false,"rows":[{"id":4971,"startAt":"2016-12-04 13:34:35","endAt":"2016-12-04 13:37:31","totalScore":0,"onlineNumber":0,"direction":1,"photo":"http://media.lalocal.cn/2016110810062516794299298377","title":"HK几毫米了攻击力里kill","channelId":97,"number":52,"user":{"id":8825,"nickName":"Dinggo","email":"4455@qq.com","avatar":"http://media.lalocal.cn/2016110810062516794299298377?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"回到家打开代扣代缴宣布","avatarOrigin":"http://media.lalocal.cn/2016110810062516794299298377","sortValue":90484},"address":"中国","videoList":null,"shareVO":{"url":"https://dev.lalocal.cn/wechat/recording?id=4971","title":"Dinggo正在乐可旅行直播HK几毫米了攻击力里kill","desc":"Dinggo正在乐可旅行直播HK几毫米了攻击力里kill","img":"http://media.lalocal.cn/2016110810062516794299298377?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","type":3,"targetType":20,"targetId":4971},"praiseFlag":false,"praiseId":null,"recommendTitle":null,"praiseNum":0,"shareNum":1,"description":null,"readNum":331,"commentNum":0}]}
     * advertisingList : [{"title":"1","photo":"http://media.lalocal.cn/201611161353327880198339665","url":"http://video.lalocal.cn/video/mp4/20161201153937.mp4","publishFlag":true,"sort":7,"description":"1111","targetId":null,"targetType":-1,"shareVO":{"url":"https://dev.lalocal.cn/wechat/h5_daren","title":"1","desc":"1111","img":"http://media.lalocal.cn/201611161353327880198339665?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","type":3,"targetType":null,"targetId":null},"id":20}]
     * categoryList : [{"id":1,"name":"美食","publishFlag":true,"photo":"http://media.lalocal.cn/2016021716275916280553191126","prePhoto":null,"sort":1},{"id":2,"name":"探险","publishFlag":true,"photo":"http://media.lalocal.cn/2016021716275916280553191126","prePhoto":null,"sort":1}]
     */

    private LiveUserBean lastDynamicUser;
    private HistoryListBean historyList;
    private List<LiveRowsBean> channelList;
    private List<RecommendAdResultBean> advertisingList;
    private List<CategoryBean> categoryList;

    public LiveUserBean getLastDynamicUser() {
        return lastDynamicUser;
    }

    public void setLastDynamicUser(LiveUserBean lastDynamicUser) {
        this.lastDynamicUser = lastDynamicUser;
    }

    public HistoryListBean getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListBean historyList) {
        this.historyList = historyList;
    }

    public List<LiveRowsBean> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<LiveRowsBean> channelList) {
        this.channelList = channelList;
    }

    public List<RecommendAdResultBean> getAdvertisingList() {
        return advertisingList;
    }

    public void setAdvertisingList(List<RecommendAdResultBean> advertisingList) {
        this.advertisingList = advertisingList;
    }

    public List<CategoryBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryBean> categoryList) {
        this.categoryList = categoryList;
    }

    public static class HistoryListBean {
        /**
         * pageNumber : 1
         * lastPage : false
         * rows : [{"id":4971,"startAt":"2016-12-04 13:34:35","endAt":"2016-12-04 13:37:31","totalScore":0,"onlineNumber":0,"direction":1,"photo":"http://media.lalocal.cn/2016110810062516794299298377","title":"HK几毫米了攻击力里kill","channelId":97,"number":52,"user":{"id":8825,"nickName":"Dinggo","email":"4455@qq.com","avatar":"http://media.lalocal.cn/2016110810062516794299298377?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"回到家打开代扣代缴宣布","avatarOrigin":"http://media.lalocal.cn/2016110810062516794299298377","sortValue":90484},"address":"中国","videoList":null,"shareVO":{"url":"https://dev.lalocal.cn/wechat/recording?id=4971","title":"Dinggo正在乐可旅行直播HK几毫米了攻击力里kill","desc":"Dinggo正在乐可旅行直播HK几毫米了攻击力里kill","img":"http://media.lalocal.cn/2016110810062516794299298377?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","type":3,"targetType":20,"targetId":4971},"praiseFlag":false,"praiseId":null,"recommendTitle":null,"praiseNum":0,"shareNum":1,"description":null,"readNum":331,"commentNum":0}]
         */

        private int pageNumber;
        private boolean lastPage;
        private List<LiveRowsBean> rows;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public List<LiveRowsBean> getRows() {
            return rows;
        }

        public void setRows(List<LiveRowsBean> rows) {
            this.rows = rows;
        }


    }
}
