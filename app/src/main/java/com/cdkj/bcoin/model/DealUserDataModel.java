package com.cdkj.bcoin.model;

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
    private int beiXinRenCount;
    private double beiPingJiaCount;
    private double beiHaoPingCount;
    private String totalTradeCountEth;
    private String totalTradeCountSc;
    private String isTrust;
    private String isAddBlackList;
    private String betweenTradeTimes;

    public String getTotalTradeCountEth() {
        return totalTradeCountEth;
    }

    public void setTotalTradeCountEth(String totalTradeCountEth) {
        this.totalTradeCountEth = totalTradeCountEth;
    }

    public String getTotalTradeCountSc() {
        return totalTradeCountSc;
    }

    public void setTotalTradeCountSc(String totalTradeCountSc) {
        this.totalTradeCountSc = totalTradeCountSc;
    }

    public int getJiaoYiCount() {
        return jiaoYiCount;
    }

    public void setJiaoYiCount(int jiaoYiCount) {
        this.jiaoYiCount = jiaoYiCount;
    }

    public double getBeiPingJiaCount() {
        return beiPingJiaCount;
    }

    public void setBeiPingJiaCount(double beiPingJiaCount) {
        this.beiPingJiaCount = beiPingJiaCount;
    }

    public double getBeiHaoPingCount() {
        return beiHaoPingCount;
    }

    public void setBeiHaoPingCount(double beiHaoPingCount) {
        this.beiHaoPingCount = beiHaoPingCount;
    }

    public int getBeiXinRenCount() {
        return beiXinRenCount;
    }

    public void setBeiXinRenCount(int beiXinRenCount) {
        this.beiXinRenCount = beiXinRenCount;
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
