package com.cdkj.ethereumdemo.model;

/**
 * Created by lei on 2017/11/23.
 */

public class DealUserDataModel {


    /**
     * jiaoYiCount : 2
     * beiPingJiaCount : 0
     * beiHaoPingCount : 0
     * beiXinRenCount : 1
     * totalTradeCount : 2298487771000000000
     * isTrust : 0
     * isAddBlackList : 0
     * betweenTradeTimes : 0
     */

    private int jiaoYiCount;
    private int beiPingJiaCount;
    private int beiHaoPingCount;
    private int beiXinRenCount;
    private String totalTradeCount;
    private String isTrust;
    private String isAddBlackList;
    private String betweenTradeTimes;

    public int getJiaoYiCount() {
        return jiaoYiCount;
    }

    public void setJiaoYiCount(int jiaoYiCount) {
        this.jiaoYiCount = jiaoYiCount;
    }

    public int getBeiPingJiaCount() {
        return beiPingJiaCount;
    }

    public void setBeiPingJiaCount(int beiPingJiaCount) {
        this.beiPingJiaCount = beiPingJiaCount;
    }

    public int getBeiHaoPingCount() {
        return beiHaoPingCount;
    }

    public void setBeiHaoPingCount(int beiHaoPingCount) {
        this.beiHaoPingCount = beiHaoPingCount;
    }

    public int getBeiXinRenCount() {
        return beiXinRenCount;
    }

    public void setBeiXinRenCount(int beiXinRenCount) {
        this.beiXinRenCount = beiXinRenCount;
    }

    public String getTotalTradeCount() {
        return totalTradeCount;
    }

    public void setTotalTradeCount(String totalTradeCount) {
        this.totalTradeCount = totalTradeCount;
    }

    public String getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(String isTrust) {
        this.isTrust = isTrust;
    }

    public String getIsAddBlackList() {
        return isAddBlackList;
    }

    public void setIsAddBlackList(String isAddBlackList) {
        this.isAddBlackList = isAddBlackList;
    }

    public String getBetweenTradeTimes() {
        return betweenTradeTimes;
    }

    public void setBetweenTradeTimes(String betweenTradeTimes) {
        this.betweenTradeTimes = betweenTradeTimes;
    }
}
