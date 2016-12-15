package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/9/11.
 */
public class CurrentRanksBean {
        private int gold;
        private RankUserBean user;
        private int rank;
        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public RankUserBean getUser() {
            return user;
        }

        public void setUser(RankUserBean user) {
            this.user = user;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }


}
