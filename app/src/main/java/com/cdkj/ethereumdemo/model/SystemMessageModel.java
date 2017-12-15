package com.cdkj.ethereumdemo.model;


import java.util.List;

public class SystemMessageModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 2
     * totalPage : 1
     * list : [{"id":44,"fromSystemCode":"CD-COIN000017","channelType":"4","pushType":"41","toSystemCode":"CD-COIN000017","toKind":"C","smsType":"1","smsTitle":"C端用户公告","smsContent":"C端用户公告内容","status":"1","createDatetime":"Nov 14, 2017 5:51:35 PM","topushDatetime":"Nov 14, 2017 5:51:35 PM","pushedDatetime":"Nov 14, 2017 5:51:40 PM","updater":"admin","updateDatetime":"Nov 14, 2017 5:51:40 PM","remark":""},{"id":43,"fromSystemCode":"CD-COIN000017","channelType":"4","pushType":"41","toSystemCode":"CD-COIN000017","toKind":"C","smsType":"1","smsTitle":"倍可盈上线啦！！！","smsContent":"倍可盈app上线啦！！！","status":"1","createDatetime":"Nov 14, 2017 5:51:09 PM","topushDatetime":"Nov 14, 2017 5:51:09 PM","pushedDatetime":"Nov 14, 2017 5:51:14 PM","updater":"admin","updateDatetime":"Nov 14, 2017 5:51:14 PM","remark":""}]
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
         * id : 44
         * fromSystemCode : CD-COIN000017
         * channelType : 4
         * pushType : 41
         * toSystemCode : CD-COIN000017
         * toKind : C
         * smsType : 1
         * smsTitle : C端用户公告
         * smsContent : C端用户公告内容
         * status : 1
         * createDatetime : Nov 14, 2017 5:51:35 PM
         * topushDatetime : Nov 14, 2017 5:51:35 PM
         * pushedDatetime : Nov 14, 2017 5:51:40 PM
         * updater : admin
         * updateDatetime : Nov 14, 2017 5:51:40 PM
         * remark :
         */

        private int id;
        private String fromSystemCode;
        private String channelType;
        private String pushType;
        private String toSystemCode;
        private String toKind;
        private String smsType;
        private String smsTitle;
        private String smsContent;
        private String status;
        private String createDatetime;
        private String topushDatetime;
        private String pushedDatetime;
        private String updater;
        private String updateDatetime;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFromSystemCode() {
            return fromSystemCode;
        }

        public void setFromSystemCode(String fromSystemCode) {
            this.fromSystemCode = fromSystemCode;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getPushType() {
            return pushType;
        }

        public void setPushType(String pushType) {
            this.pushType = pushType;
        }

        public String getToSystemCode() {
            return toSystemCode;
        }

        public void setToSystemCode(String toSystemCode) {
            this.toSystemCode = toSystemCode;
        }

        public String getToKind() {
            return toKind;
        }

        public void setToKind(String toKind) {
            this.toKind = toKind;
        }

        public String getSmsType() {
            return smsType;
        }

        public void setSmsType(String smsType) {
            this.smsType = smsType;
        }

        public String getSmsTitle() {
            return smsTitle;
        }

        public void setSmsTitle(String smsTitle) {
            this.smsTitle = smsTitle;
        }

        public String getSmsContent() {
            return smsContent;
        }

        public void setSmsContent(String smsContent) {
            this.smsContent = smsContent;
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

        public String getTopushDatetime() {
            return topushDatetime;
        }

        public void setTopushDatetime(String topushDatetime) {
            this.topushDatetime = topushDatetime;
        }

        public String getPushedDatetime() {
            return pushedDatetime;
        }

        public void setPushedDatetime(String pushedDatetime) {
            this.pushedDatetime = pushedDatetime;
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
    }
}
