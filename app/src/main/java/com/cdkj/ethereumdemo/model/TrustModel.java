package com.cdkj.ethereumdemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lei on 2017/11/1.
 */

public class TrustModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 1
     * totalPage : 1
     * list : [{"code":"UR201711231105227853226","userId":"U201711212030187015235","toUser":"U201711211643415946351","status":"1","updateDatetime":"Nov 23, 2017 11:05:22 AM","fromUserInfo":{"userId":"U201711212030187015235","loginName":"18984955240","mobile":"18984955240","nickname":"RQQ","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","status":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false},"toUserInfo":{"userId":"U201711211643415946351","loginName":"13626595294","mobile":"13626595294","nickname":"飞雪连天射白鹿","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 21, 2017 4:43:41 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}}]
     */

    private int pageNO;
    private int start;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private List<ListBean> list;

    public int getPageNO() {
        return pageNO;
    }

    public void setPageNO(int pageNO) {
        this.pageNO = pageNO;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * code : UR201711231105227853226
         * userId : U201711212030187015235
         * toUser : U201711211643415946351
         * status : 1
         * updateDatetime : Nov 23, 2017 11:05:22 AM
         * fromUserInfo : {"userId":"U201711212030187015235","loginName":"18984955240","mobile":"18984955240","nickname":"RQQ","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","status":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}
         * toUserInfo : {"userId":"U201711211643415946351","loginName":"13626595294","mobile":"13626595294","nickname":"飞雪连天射白鹿","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 21, 2017 4:43:41 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}
         */

        private String code;
        private String userId;
        private String toUser;
        private String status;
        private String updateDatetime;
        private FromUserInfoBean fromUserInfo;
        private ToUserInfoBean toUserInfo;

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

        public String getToUser() {
            return toUser;
        }

        public void setToUser(String toUser) {
            this.toUser = toUser;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateDatetime() {
            return updateDatetime;
        }

        public void setUpdateDatetime(String updateDatetime) {
            this.updateDatetime = updateDatetime;
        }

        public FromUserInfoBean getFromUserInfo() {
            return fromUserInfo;
        }

        public void setFromUserInfo(FromUserInfoBean fromUserInfo) {
            this.fromUserInfo = fromUserInfo;
        }

        public ToUserInfoBean getToUserInfo() {
            return toUserInfo;
        }

        public void setToUserInfo(ToUserInfoBean toUserInfo) {
            this.toUserInfo = toUserInfo;
        }

        public static class FromUserInfoBean {
            /**
             * userId : U201711212030187015235
             * loginName : 18984955240
             * mobile : 18984955240
             * nickname : RQQ
             * loginPwdStrength : 1
             * kind : C
             * level : 1
             * tradePwdStrength : 1
             * status : 0
             * createDatetime : Nov 21, 2017 8:30:18 PM
             * companyCode : CD-COIN000017
             * systemCode : CD-COIN000017
             * tradepwdFlag : false
             */

            private String userId;
            private String loginName;
            private String mobile;
            private String nickname;
            private String loginPwdStrength;
            private String kind;
            private String level;
            private String photo = "";
            private String tradePwdStrength;
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

            public String getTradePwdStrength() {
                return tradePwdStrength;
            }

            public void setTradePwdStrength(String tradePwdStrength) {
                this.tradePwdStrength = tradePwdStrength;
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

        public static class ToUserInfoBean {
            /**
             * userId : U201711211643415946351
             * loginName : 13626595294
             * mobile : 13626595294
             * nickname : 飞雪连天射白鹿
             * loginPwdStrength : 1
             * kind : C
             * level : 1
             * status : 0
             * createDatetime : Nov 21, 2017 4:43:41 PM
             * companyCode : CD-COIN000017
             * systemCode : CD-COIN000017
             * tradepwdFlag : false
             */

            private String userId;
            private String loginName;
            private String mobile;
            private String nickname;
            private String loginPwdStrength;
            private String kind;
            private String level;
            private String status;
            private String photo = "";
            private String createDatetime;
            private String companyCode;
            private String systemCode;
            private boolean tradepwdFlag;
            private UserStatisticsBean userStatistics;

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
