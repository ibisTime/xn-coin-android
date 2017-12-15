package com.cdkj.baselibrary.model.pay;

/**
 * Created by Administrator on 2017/4/19.
 */

public class WXPayModel {

    private WXPayInfoModel payParams;
    private String  prepayId;
    private String  payId;

    public WXPayInfoModel getPayParams() {
        return payParams;
    }

    public void setPayParams(WXPayInfoModel payParams) {
        this.payParams = payParams;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }
}
