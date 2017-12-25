package com.cdkj.bcoin.model;

import java.util.List;

/**
 * Created by lei on 2017/11/1.
 */

public class AddressModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 2
     * totalPage : 1
     * list : [{"code":"ETH201711212030200574934","type":"X","address":"0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5","password":"99605460","userId":"U201711212030187015235","balance":0,"status":"0","createDatetime":"Nov 21, 2017 8:30:20 PM","updateDatetime":"Nov 21, 2017 8:30:20 PM","user":{"userId":"U201711212030187015235","loginName":"18984955240","mobile":"18984955240","nickname":"RQQ","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","status":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}},{"code":"ETH201711221636295846448","type":"Y","address":"0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5","label":"可盈可乐","userId":"U201711212030187015235","balance":0,"status":"1","createDatetime":"Nov 22, 2017 4:36:29 PM","updateDatetime":"Nov 22, 2017 4:36:29 PM","user":{"userId":"U201711212030187015235","loginName":"18984955240","mobile":"18984955240","nickname":"RQQ","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","status":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}}]
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
         * code : ETH201711212030200574934
         * type : X
         * address : 0x2fa7a62e954995b6b96ecc9f7b622a7ad784f6e5
         * password : 99605460
         * userId : U201711212030187015235
         * balance : 0
         * status : 0
         * createDatetime : Nov 21, 2017 8:30:20 PM
         * updateDatetime : Nov 21, 2017 8:30:20 PM
         * user : {"userId":"U201711212030187015235","loginName":"18984955240","mobile":"18984955240","nickname":"RQQ","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","status":"0","createDatetime":"Nov 21, 2017 8:30:18 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}
         * label : 可盈可乐
         */

        private String code;
        private String type;
        private String address;
        private String password;
        private String userId;
        private int balance;
        private String status;
        private String createDatetime;
        private String updateDatetime;
        private UserBean user;
        private String label;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public static class UserBean {
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
            private String tradePwdStrength;
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
    }
}
