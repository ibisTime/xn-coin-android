package com.cdkj.bcoin.model;

/**
 * Created by lei on 2017/8/22.
 */

public class MarketCoinModel {


    /**
     * id : 1
     * coin : ETH
     * referCurrency : CNY
     * origin : bitfinex
     * lastPrice : 2123.104
     * bid : 2122.9713
     * ask : 2123.104
     * mid : 2123.0377
     * low : 2026.9009
     * high : 2136.4397
     * volume : 280568.99170261
     * updateDatetime : Nov 14, 2017 2:49:01 PM
     */

    private int id;
    private String coin;
    private String referCurrency;
    private String origin;
    private double lastPrice;
    private double bid;
    private double ask;
    private double mid;
    private double low;
    private double high;
    private String volume;
    private String updateDatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
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

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
