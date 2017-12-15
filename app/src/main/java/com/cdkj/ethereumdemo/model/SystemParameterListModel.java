package com.cdkj.ethereumdemo.model;

import java.util.List;

/**
 * Created by lei on 2017/11/16.
 */

public class SystemParameterListModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 20
     * totalCount : 8
     * totalPage : 1
     * list : [{"id":26,"type":"sell_ads_hint","ckey":"protectPrice","cvalue":"广告最低可成交的价格，可帮助您在价格剧烈波动时保持稳定的盈利，比如最低为5000时，市场价处于5000以下时，您的广告依旧以5000的价格展示。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币最低价格说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":25,"type":"sell_ads_hint","ckey":"totalCount","cvalue":"您想出售的货币数量，发布的时候会被冻结。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币总额说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":24,"type":"sell_ads_hint","ckey":"payLimit","cvalue":"您希望对方在此期限内付款。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币支付超时说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":23,"type":"sell_ads_hint","ckey":"payType","cvalue":"您需指定最有效的付款方式，帮助对方更快的与您达成交易。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币支付方式说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":22,"type":"sell_ads_hint","ckey":"maxTrade","cvalue":"一次交易中的最大交易限制，您的钱包余额也会影响最大量的设置。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币最大交易说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":21,"type":"sell_ads_hint","ckey":"minTrade","cvalue":"一次交易最低的交易限制","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币最小交易说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":20,"type":"sell_ads_hint","ckey":"price","cvalue":"基于比例得出的报价，每10分钟更新一次。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币价格说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"},{"id":19,"type":"sell_ads_hint","ckey":"premiumRate","cvalue":"基于市场价的溢出比例，市场价是根据部分大型交易所实时价格得出的，确保您的报价趋于一个相对合理的范围，比如当前价格为8000，溢价比例为10%，那么价格为8800。","updater":"admin","updateDatetime":"Nov 23, 2017 11:02:32 AM","remark":"卖币溢价率说明","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017"}]
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
         * id : 26
         * type : sell_ads_hint
         * ckey : protectPrice
         * cvalue : 广告最低可成交的价格，可帮助您在价格剧烈波动时保持稳定的盈利，比如最低为5000时，市场价处于5000以下时，您的广告依旧以5000的价格展示。
         * updater : admin
         * updateDatetime : Nov 23, 2017 11:02:32 AM
         * remark : 卖币最低价格说明
         * companyCode : CD-COIN000017
         * systemCode : CD-COIN000017
         */

        private int id;
        private String type;
        private String ckey;
        private String cvalue;
        private String updater;
        private String updateDatetime;
        private String remark;
        private String companyCode;
        private String systemCode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCkey() {
            return ckey;
        }

        public void setCkey(String ckey) {
            this.ckey = ckey;
        }

        public String getCvalue() {
            return cvalue;
        }

        public void setCvalue(String cvalue) {
            this.cvalue = cvalue;
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
