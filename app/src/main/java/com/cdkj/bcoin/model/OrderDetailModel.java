package com.cdkj.bcoin.model;

import java.io.Serializable;

/**
 * Created by lei on 2017/10/31.
 */

public class OrderDetailModel implements Serializable {

    /**
     * code : JY201711171716012986371
     * type : buy
     * adsCode : ADSS201711161118209328091
     * buyUser : U201711121556304486170
     * sellUser : U201711102114095446687
     * leaveMessage : 哈哈哈哈
     * tradeCurrency : CNY
     * tradeCoin : ETH
     * tradePrice : 2169.077
     * tradeAmount : 1
     * fee : 0
     * count : 0
     * payType : 0
     * invalidDatetime : Nov 17, 2017 5:26:01 PM
     * status : 0
     * createDatetime : Nov 17, 2017 5:16:01 PM
     * updater : U201711121556304486170
     * updateDatatime : Nov 17, 2017 5:16:01 PM
     * remark : 新订单，等待支付
     * buyUserInfo : {"userId":"U201711121556304486170","loginName":"18984955240","mobile":"18984955240","photo":"ANDROID_1510823786678_580_580.jpg","nickname":"Lei","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 12, 2017 3:56:30 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}
     * sellUserInfo : {"userId":"U201711102114095446687","loginName":"15268501482","mobile":"15268501482","nickname":"111","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 10, 2017 9:14:09 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}
     */

    private String code;
    private String type;
    private String adsCode;
    private String buyUser;
    private String sellUser;
    private String leaveMessage;
    private String tradeCurrency;
    private String tradeCoin;
    private double tradePrice;
    private double tradeAmount;
    private String feeString;
    private String countString;
    private String bsComment; // 买家评价
    private String sbComment; // 卖家评价
    private String payType;
    private String invalidDatetime;
    private String status;
    private String createDatetime;
    private String updater;
    private String updateDatatime;
    private String remark;
    private BuyUserInfoBean buyUserInfo;
    private SellUserInfoBean sellUserInfo;

    public String getBsComment() {
        return bsComment;
    }

    public void setBsComment(String bsComment) {
        this.bsComment = bsComment;
    }

    public String getSbComment() {
        return sbComment;
    }

    public void setSbComment(String sbComment) {
        this.sbComment = sbComment;
    }

    public String getFeeString() {
        return feeString;
    }

    public void setFeeString(String feeString) {
        this.feeString = feeString;
    }

    public String getCountString() {
        return countString;
    }

    public void setCountString(String countString) {
        this.countString = countString;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdsCode() {
        return adsCode;
    }

    public void setAdsCode(String adsCode) {
        this.adsCode = adsCode;
    }

    public String getBuyUser() {
        return buyUser;
    }

    public void setBuyUser(String buyUser) {
        this.buyUser = buyUser;
    }

    public String getSellUser() {
        return sellUser;
    }

    public void setSellUser(String sellUser) {
        this.sellUser = sellUser;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
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

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getInvalidDatetime() {
        return invalidDatetime;
    }

    public void setInvalidDatetime(String invalidDatetime) {
        this.invalidDatetime = invalidDatetime;
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

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatatime() {
        return updateDatatime;
    }

    public void setUpdateDatatime(String updateDatatime) {
        this.updateDatatime = updateDatatime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BuyUserInfoBean getBuyUserInfo() {
        return buyUserInfo;
    }

    public void setBuyUserInfo(BuyUserInfoBean buyUserInfo) {
        this.buyUserInfo = buyUserInfo;
    }

    public SellUserInfoBean getSellUserInfo() {
        return sellUserInfo;
    }

    public void setSellUserInfo(SellUserInfoBean sellUserInfo) {
        this.sellUserInfo = sellUserInfo;
    }

    public static class BuyUserInfoBean implements Serializable {
        /**
         * userId : U201711121556304486170
         * loginName : 18984955240
         * mobile : 18984955240
         * photo : ANDROID_1510823786678_580_580.jpg
         * nickname : Lei
         * loginPwdStrength : 1
         * kind : C
         * level : 1
         * status : 0
         * createDatetime : Nov 12, 2017 3:56:30 PM
         * companyCode : CD-COIN000017
         * systemCode : CD-COIN000017
         * tradepwdFlag : false
         */

        private String userId;
        private String loginName;
        private String mobile;
        private String photo = "";
        private String nickname;
        private String loginPwdStrength;
        private String kind;
        private String level;
        private String status;
        private String createDatetime;
        private String companyCode;
        private String systemCode;
        private boolean tradepwdFlag;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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

    public static class SellUserInfoBean implements Serializable {
        /**
         * userId : U201711102114095446687
         * loginName : 15268501482
         * mobile : 15268501482
         * nickname : 111
         * loginPwdStrength : 1
         * kind : C
         * level : 1
         * status : 0
         * createDatetime : Nov 10, 2017 9:14:09 PM
         * companyCode : CD-COIN000017
         * systemCode : CD-COIN000017
         * tradepwdFlag : false
         */

        private String userId;
        private String loginName;
        private String mobile;
        private String photo = "";
        private String nickname;
        private String loginPwdStrength;
        private String kind;
        private String level;
        private String status;
        private String createDatetime;
        private String companyCode;
        private String systemCode;
        private boolean tradepwdFlag;

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
}
