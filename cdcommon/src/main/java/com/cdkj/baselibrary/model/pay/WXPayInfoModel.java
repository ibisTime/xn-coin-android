package com.cdkj.baselibrary.model.pay;

/**调用微信支付对象
 * Created by Administrator on 2017/4/13.
 */

public class WXPayInfoModel {

/*     request.appId = model.getAppid();  //应用ID

        request.partnerId = model.getPartnerid();  //商户号

        request.prepayId = ;     //预支付ID

        request.packageValue = ; //扩展字段

        request.nonceStr = ;      //随机字符串

        request.timeStamp = ;    //签名*/

    private String appid;
    private String partnerid;
    private String prepayid;
    private String package_;
    private String noncestr;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackage_() {
        return package_;
    }

    public void setPackage_(String package_) {
        this.package_ = package_;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
