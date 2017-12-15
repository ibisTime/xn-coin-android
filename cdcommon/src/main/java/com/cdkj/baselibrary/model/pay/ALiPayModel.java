package com.cdkj.baselibrary.model.pay;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ALiPayModel {

    private String payParams;
    private String prepayId;
    private String payId;


    public String getPayParams() {
        return payParams;
    }

    public void setPayParams(String payParams) {
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
