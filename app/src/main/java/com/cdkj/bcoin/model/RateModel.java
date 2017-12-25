package com.cdkj.bcoin.model;

/**
 * Created by lei on 2017/8/22.
 */

public class RateModel {


    /**
     * currency : USD
     * referCurrency : CNY
     * origin : juhe
     * rate : 6.6347
     * updateDatetime : Nov 13, 2017 4:10:56 PM
     */

    private String currency;
    private String referCurrency;
    private String origin;
    private double rate;
    private String updateDatetime;

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
