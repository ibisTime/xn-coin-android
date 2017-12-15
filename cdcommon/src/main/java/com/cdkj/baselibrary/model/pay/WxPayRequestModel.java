package com.cdkj.baselibrary.model.pay;

/**
 * Created by 李先俊 on 2017/7/31.
 */

public class WxPayRequestModel {


    /**
     * payCode : CZ201707311731501098855946622
     * appId : wxbea7a28408973ab3
     * partnerid : 1483267162
     * prepayId : wx20170731173231dfff63770c0179716127
     * wechatPackage : Sign=WXPay
     * nonceStr : 05e29cd52be4dd80e9bfc3620fbdc08b
     * timeStamp : 1501493510
     * sign : 540D37CBDCA0362973945AA0994F24F4
     */

    private String payCode;
    private String appId;
    private String partnerid;
    private String prepayId;
    private String wechatPackage;
    private String nonceStr;
    private String timeStamp;
    private String sign;

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getWechatPackage() {
        return wechatPackage;
    }

    public void setWechatPackage(String wechatPackage) {
        this.wechatPackage = wechatPackage;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
