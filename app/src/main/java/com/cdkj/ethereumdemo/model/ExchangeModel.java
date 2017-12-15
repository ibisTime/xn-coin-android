package com.cdkj.ethereumdemo.model;

import java.util.List;

/**
 * Created by lei on 2017/11/2.
 */

public class ExchangeModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 5
     * totalPage : 1
     * list : [{"id":17,"currency":"USD","referCurrency":"CNY","origin":"juhe","rate":6.581,"updateDatetime":"Nov 25, 2017 12:11:24 AM"},{"id":15,"currency":"USD","referCurrency":"CNY","origin":"juhe","rate":6.581,"updateDatetime":"Nov 25, 2017 12:00:00 AM"},{"id":13,"currency":"USD","referCurrency":"CNY","origin":"juhe","rate":6.6021,"updateDatetime":"Nov 24, 2017 12:00:00 AM"},{"id":3,"currency":"USD","referCurrency":"CNY","origin":"juhe","rate":6.6021,"updateDatetime":"Nov 23, 2017 12:26:12 PM"},{"id":2,"currency":"USD","referCurrency":"CNY","origin":"juhe","rate":6.629,"updateDatetime":"Nov 23, 2017 12:00:49 AM"}]
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
         * id : 17
         * currency : USD
         * referCurrency : CNY
         * origin : juhe
         * rate : 6.581
         * updateDatetime : Nov 25, 2017 12:11:24 AM
         */

        private int id;
        private String currency;
        private String referCurrency;
        private String origin;
        private double rate;
        private String updateDatetime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getReferCurrency() {
            return referCurrency;
        }

        public void setReferCurrency(String referCurrency) {
            this.referCurrency = referCurrency;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getUpdateDatetime() {
            return updateDatetime;
        }

        public void setUpdateDatetime(String updateDatetime) {
            this.updateDatetime = updateDatetime;
        }
    }
}
