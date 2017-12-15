package com.cdkj.ethereumdemo.model;

import java.util.List;

/**
 * Created by lei on 2017/11/23.
 */

public class UserRefereeModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 1
     * totalPage : 1
     * list : [{"userId":"U201711241642184803950","loginName":"13868074591","mobile":"13868074591","nickname":"tianlei2","loginPwdStrength":"1","kind":"C","level":"1","userReferee":"U201711241015089785300","status":"0","createDatetime":"Nov 24, 2017 4:42:18 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false,"refereeUser":{"userId":"U201711241015089785300","loginName":"13868074590","mobile":"13868074590","nickname":"Tianlei","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 24, 2017 10:15:08 AM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false},"userStatistics":{"jiaoYiCount":0,"beiPingJiaCount":0,"beiHaoPingCount":0,"beiXinRenCount":0}}]
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
         * userId : U201711241642184803950
         * loginName : 13868074591
         * mobile : 13868074591
         * nickname : tianlei2
         * loginPwdStrength : 1
         * kind : C
         * level : 1
         * userReferee : U201711241015089785300
         * status : 0
         * createDatetime : Nov 24, 2017 4:42:18 PM
         * companyCode : CD-COIN000017
         * systemCode : CD-COIN000017
         * tradepwdFlag : false
         * refereeUser : {"userId":"U201711241015089785300","loginName":"13868074590","mobile":"13868074590","nickname":"Tianlei","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 24, 2017 10:15:08 AM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}
         * userStatistics : {"jiaoYiCount":0,"beiPingJiaCount":0,"beiHaoPingCount":0,"beiXinRenCount":0}
         */

        private String userId;
        private String loginName;
        private String mobile;
        private String nickname;
        private String photo = "";
        private String loginPwdStrength;
        private String kind;
        private String level;
        private String userReferee;
        private String status;
        private String createDatetime;
        private String companyCode;
        private String systemCode;
        private boolean tradepwdFlag;
        private RefereeUserBean refereeUser;
        private UserStatisticsBean userStatistics;

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

        public RefereeUserBean getRefereeUser() {
            return refereeUser;
        }

        public void setRefereeUser(RefereeUserBean refereeUser) {
            this.refereeUser = refereeUser;
        }

        public UserStatisticsBean getUserStatistics() {
            return userStatistics;
        }

        public void setUserStatistics(UserStatisticsBean userStatistics) {
            this.userStatistics = userStatistics;
        }

        public static class RefereeUserBean {
            /**
             * userId : U201711241015089785300
             * loginName : 13868074590
             * mobile : 13868074590
             * nickname : Tianlei
             * loginPwdStrength : 1
             * kind : C
             * level : 1
             * status : 0
             * createDatetime : Nov 24, 2017 10:15:08 AM
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

        public static class UserStatisticsBean {
            /**
             * jiaoYiCount : 0
             * beiPingJiaCount : 0
             * beiHaoPingCount : 0
             * beiXinRenCount : 0
             */

            private int jiaoYiCount;
            private int beiPingJiaCount;
            private int beiHaoPingCount;
            private int beiXinRenCount;

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
        }
    }
}
