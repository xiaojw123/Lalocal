package com.lalocal.lalocal.live.entertainment.model;

import com.lalocal.lalocal.model.SpecialShareVOBean;

import java.util.List;

/**
 * Created by android on 2016/9/11.
 */
public class GiftRanksResultBean {
        private Object myTotalRank;
        private Object mycurrentRank;
        private SpecialShareVOBean shareVO;
        private List<TotalRanksBean> totalRanks;
        private List<TotalRanksBean> currentRanks;
        public Object getMyTotalRank() {
            return myTotalRank;
        }

        public void setMyTotalRank(Object myTotalRank) {
            this.myTotalRank = myTotalRank;
        }

        public Object getMycurrentRank() {
            return mycurrentRank;
        }

        public void setMycurrentRank(Object mycurrentRank) {
            this.mycurrentRank = mycurrentRank;
        }

        public SpecialShareVOBean getShareVO() {
            return shareVO;
        }

        public void setShareVO(SpecialShareVOBean shareVO) {
            this.shareVO = shareVO;
        }

        public List<TotalRanksBean> getTotalRanks() {
            return totalRanks;
        }

        public void setTotalRanks(List<TotalRanksBean> totalRanks) {
            this.totalRanks = totalRanks;
        }

        public List<TotalRanksBean> getCurrentRanks() {
            return currentRanks;
        }

        public void setCurrentRanks(List<TotalRanksBean> currentRanks) {
            this.currentRanks = currentRanks;
        }



}
