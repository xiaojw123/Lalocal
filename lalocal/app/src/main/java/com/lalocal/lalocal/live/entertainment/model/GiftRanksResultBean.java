package com.lalocal.lalocal.live.entertainment.model;

import com.lalocal.lalocal.model.SpecialShareVOBean;

import java.util.List;

/**
 * Created by android on 2016/9/11.
 */
public class GiftRanksResultBean {
        private TotalRanksBean myTotalRank;
        private TotalRanksBean mycurrentRank;
        private SpecialShareVOBean shareVO;
        private List<TotalRanksBean> totalRanks;
        private List<TotalRanksBean> currentRanks;
        public TotalRanksBean getMyTotalRank() {
            return myTotalRank;
        }

        public void setMyTotalRank(TotalRanksBean myTotalRank) {
            this.myTotalRank = myTotalRank;
        }

        public TotalRanksBean getMycurrentRank() {
            return mycurrentRank;
        }

        public void setMycurrentRank(TotalRanksBean mycurrentRank) {
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
