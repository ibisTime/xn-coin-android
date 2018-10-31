package com.cdkj.baselibrary.model;

import java.io.Serializable;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public class UserInfoModel {

    private String userId;
    private String zfbAccount;
    private String zfbQr;
    private String secretUserId;
    private String loginName;
    private String mobile;
    private String nickname;
    private String loginPwdStrength;
    private String kind;
    private String level;
    private String userReferee;
    private String idNo;
    private String realName = "";
    private String photo = "";
    private String email = "";
    private String roleCode;
    private double divRate;
    private String status;
    private String province;
    private String city;
    private String area;
    private String lastOrderDatetime;
    private String createDatetime;
    private String updater;
    private String updateDatetime;
    private String companyCode;
    private String systemCode;
    private boolean tradepwdFlag;
    private boolean googleAuthFlag;
    private int totalFollowNum;
    private int totalFansNum;
    private RefereeUserBean refereeUser;
    private UserStatisticsBean userStatistics;

    public String getZfbAccount() {
        return zfbAccount;
    }

    public void setZfbAccount(String zfbAccount) {
        this.zfbAccount = zfbAccount;
    }

    public String getZfbQr() {
        return zfbQr;
    }

    public void setZfbQr(String zfbQr) {
        this.zfbQr = zfbQr;
    }

    public String getSecretUserId() {
        return secretUserId;
    }

    public void setSecretUserId(String secretUserId) {
        this.secretUserId = secretUserId;
    }

    public boolean isGoogleAuthFlag() {
        return googleAuthFlag;
    }

    public void setGoogleAuthFlag(boolean googleAuthFlag) {
        this.googleAuthFlag = googleAuthFlag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatisticsBean getUserStatistics() {
        return userStatistics;
    }

    public void setUserStatistics(UserStatisticsBean userStatistics) {
        this.userStatistics = userStatistics;
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

    public String getUserReferee() {
        return userReferee;
    }

    public void setUserReferee(String userReferee) {
        this.userReferee = userReferee;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public double getDivRate() {
        return divRate;
    }

    public void setDivRate(double divRate) {
        this.divRate = divRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLastOrderDatetime() {
        return lastOrderDatetime;
    }

    public void setLastOrderDatetime(String lastOrderDatetime) {
        this.lastOrderDatetime = lastOrderDatetime;
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

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
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

    public int getTotalFollowNum() {
        return totalFollowNum;
    }

    public void setTotalFollowNum(int totalFollowNum) {
        this.totalFollowNum = totalFollowNum;
    }

    public int getTotalFansNum() {
        return totalFansNum;
    }

    public void setTotalFansNum(int totalFansNum) {
        this.totalFansNum = totalFansNum;
    }

    public RefereeUserBean getRefereeUser() {
        return refereeUser;
    }

    public void setRefereeUser(RefereeUserBean refereeUser) {
        this.refereeUser = refereeUser;
    }


    public static class RefereeUserBean {

        private String userId;
        private String loginName;
        private String mobile;
        private String nickname;
        private String loginPwdStrength;
        private String kind;
        private String level;
        private String idNo;
        private String realName;
        private String roleCode;
        private double divRate;
        private String status;
        private String province;
        private String city;
        private String area;
        private String lastOrderDatetime;
        private String createDatetime;
        private String updater;
        private String updateDatetime;
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

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public double getDivRate() {
            return divRate;
        }

        public void setDivRate(double divRate) {
            this.divRate = divRate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLastOrderDatetime() {
            return lastOrderDatetime;
        }

        public void setLastOrderDatetime(String lastOrderDatetime) {
            this.lastOrderDatetime = lastOrderDatetime;
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

        public String getUpdateDatetime() {
            return updateDatetime;
        }

        public void setUpdateDatetime(String updateDatetime) {
            this.updateDatetime = updateDatetime;
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
}
