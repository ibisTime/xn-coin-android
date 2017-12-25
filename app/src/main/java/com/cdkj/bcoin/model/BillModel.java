package com.cdkj.bcoin.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lei on 2017/10/18.
 */

public class BillModel implements Serializable {

    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 1
     * totalPage : 1
     * list : [{"code":"AJ201708241748271515895195545","refNo":"QX201708241748271495116020977","accountNumber":"A201708171530061354538831588","transAmount":-5050,"userId":"U201708171530061085458","realName":"15738777150","type":"B","currency":"CNY","bizType":"-11","bizNote":"线下取现","preAmount":1000000000000,"postAmount":999999994950,"status":"1","remark":"记得对账哦","createDatetime":"Aug 24, 2017 5:48:27 PM","workDate":"20170824","channelType":"90","systemCode":"CD-CDZT000009","companyCode":"CD-CDZT000009"}]
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

    public static class ListBean implements Serializable {

        /**
         * code : AJ201711232052479015244
         * kind : 0
         * refNo : JY201711232051429556440
         * accountNumber : A201711212030187096126
         * transAmountString : 3849220000000000
         * userId : U201711212030187015235
         * realName : 18984955240
         * type : C
         * currency : ETH
         * bizType : buy
         * bizNote : 交易买入
         * preAmountString : 5000000000000000000000
         * postAmountString : 5000003849220000000000
         * status : 1
         * remark : 记得对账哦
         * createDatetime : Nov 23, 2017 8:52:47 PM
         * workDate : 20171123
         * channelType : 0
         * systemCode : CD-COIN000017
         * companyCode : CD-COIN000017
         */

        private String code;
        private String kind;
        private String refNo;
        private String accountNumber;
        private String transAmountString;
        private String userId;
        private String realName;
        private String type;
        private String currency;
        private String bizType;
        private String bizNote;
        private String preAmountString;
        private String postAmountString;
        private String status;
        private String remark;
        private String createDatetime;
        private String workDate;
        private String channelType;
        private String systemCode;
        private String companyCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getTransAmountString() {
            return transAmountString;
        }

        public void setTransAmountString(String transAmountString) {
            this.transAmountString = transAmountString;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getBizType() {
            return bizType;
        }

        public void setBizType(String bizType) {
            this.bizType = bizType;
        }

        public String getBizNote() {
            return bizNote;
        }

        public void setBizNote(String bizNote) {
            this.bizNote = bizNote;
        }

        public String getPreAmountString() {
            return preAmountString;
        }

        public void setPreAmountString(String preAmountString) {
            this.preAmountString = preAmountString;
        }

        public String getPostAmountString() {
            return postAmountString;
        }

        public void setPostAmountString(String postAmountString) {
            this.postAmountString = postAmountString;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }
    }
}
