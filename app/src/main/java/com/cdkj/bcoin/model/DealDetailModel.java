package com.cdkj.bcoin.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lei on 2017/10/29.
 */

public class DealDetailModel implements Serializable {

    private String code;
    private String userId;
    private UserBean user;
    private String tradeCurrency;
    private String tradeCoin;
    private String onlyTrust;
    private String tradeType;
    private int isTrust;
    private double premiumRate;
    private String totalCountString;
    private String leftCountString;
    private double marketPrice;
    private double truePrice;
    private int protectPrice;
    private int minTrade;
    private int maxTrade;
    private String payType;
    private int payLimit;
    private String status;
    private String createDatetime;
    private String updateDatetime;
    private String leaveMessage;
    private UserStatisticsBean userStatistics;
    private List<DisplayTimeBean> displayTime;


    public List<DisplayTimeBean> getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(List<DisplayTimeBean> displayTime) {
        this.displayTime = displayTime;
    }

    public int getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(int isTrust) {
        this.isTrust = isTrust;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public String getTradeCoin() {
        return tradeCoin;
    }

    public void setTradeCoin(String tradeCoin) {
        this.tradeCoin = tradeCoin;
    }

    public String getOnlyTrust() {
        return onlyTrust;
    }

    public void setOnlyTrust(String onlyTrust) {
        this.onlyTrust = onlyTrust;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public double getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(double premiumRate) {
        this.premiumRate = premiumRate;
    }

    public String getTotalCountString() {
        return totalCountString;
    }

    public void setTotalCountString(String totalCountString) {
        this.totalCountString = totalCountString;
    }

    public String getLeftCountString() {
        return leftCountString;
    }

    public void setLeftCountString(String leftCountString) {
        this.leftCountString = leftCountString;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getTruePrice() {
        return truePrice;
    }

    public void setTruePrice(double truePrice) {
        this.truePrice = truePrice;
    }

    public int getProtectPrice() {
        return protectPrice;
    }

    public void setProtectPrice(int protectPrice) {
        this.protectPrice = protectPrice;
    }

    public int getMinTrade() {
        return minTrade;
    }

    public void setMinTrade(int minTrade) {
        this.minTrade = minTrade;
    }

    public int getMaxTrade() {
        return maxTrade;
    }

    public void setMaxTrade(int maxTrade) {
        this.maxTrade = maxTrade;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getPayLimit() {
        return payLimit;
    }

    public void setPayLimit(int payLimit) {
        this.payLimit = payLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public UserStatisticsBean getUserStatistics() {
        return userStatistics;
    }

    public void setUserStatistics(UserStatisticsBean userStatistics) {
        this.userStatistics = userStatistics;
    }

    public static class UserBean implements Serializable {

        private String userId;
        private String loginName;
        private String mobile;
        private String nickname;
        private String loginPwdStrength;
        private String kind;
        private String photo = "";
        private String level;
        private String status;
        private String lastLogin;
        private String createDatetime;
        private String companyCode;
        private String systemCode;
        private boolean tradepwdFlag;

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLoginPwdStrength() {
            return loginPwdStrength;
        }

        public void setLoginPwdStrength(String loginPwdStrength) {
            this.loginPwdStrength = loginPwdStrength;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public boolean isTradepwdFlag() {
            return tradepwdFlag;
        }

        public void setTradepwdFlag(boolean tradepwdFlag) {
            this.tradepwdFlag = tradepwdFlag;
        }
    }

    public static class UserStatisticsBean implements Serializable {
        private int jiaoYiCount;
        private int beiXinRenCount;
        private double beiPingJiaCount;
        private double beiHaoPingCount;

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
    }

    public static class DisplayTimeBean {
        /**
         * id : 12
         * adsCode : ADS201711292040116641521
         * week : 1
         * startTime : 0
         * endTime : 1
         */

        private int id;
        private String adsCode;
        private String week;
        private int startTime;
        private int endTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdsCode() {
            return adsCode;
        }

        public void setAdsCode(String adsCode) {
            this.adsCode = adsCode;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }
    }

}
