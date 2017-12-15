package com.cdkj.ethereumdemo.model;


import java.util.ArrayList;
import java.util.List;

public class ConsultingModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 1
     * totalPage : 1
     * list : [{"code":"NS20171132102213544","type":"1","title":"HUOBIPRO（火币PRO站）点对点交易 如何用人民币买币?","keywords":"0","advPic":"如何注册_1510899584789.png","pic":"banner_1510899576993.png","content":"","scanNum":0,"commentNum":0,"location":"0","orderNo":1,"status":"1","updater":"admin","updateDatetime":"Nov 17, 2017 2:23:17 PM","remark":"","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"}]
     */

    private int pageNO;
    private int start;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private List<ListBean> list = new ArrayList<>();

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
         * code : NS20171132102213544
         * type : 1
         * title : HUOBIPRO（火币PRO站）点对点交易 如何用人民币买币?
         * keywords : 0
         * advPic : 如何注册_1510899584789.png
         * pic : banner_1510899576993.png
         * content :
         * scanNum : 0
         * commentNum : 0
         * location : 0
         * orderNo : 1
         * status : 1
         * updater : admin
         * updateDatetime : Nov 17, 2017 2:23:17 PM
         * remark :
         * companyCode : CD-COIN000017
         * systemCode : CD-COIN000017
         */

        private String code;
        private String type;
        private String title;
        private String keywords;
        private String advPic;
        private String pic;
        private String content;
        private int scanNum;
        private int commentNum;
        private String location;
        private int orderNo;
        private String status;
        private String updater;
        private String updateDatetime;
        private String remark;
        private String companyCode;
        private String systemCode;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getScanNum() {
            return scanNum;
        }

        public void setScanNum(int scanNum) {
            this.scanNum = scanNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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
    }
}
