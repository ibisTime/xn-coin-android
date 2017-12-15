package com.cdkj.baselibrary.model.pay;

/**用于RXBUS传递支付信息
 * Created by Administrator on 2017/4/19.
 */

public class PaySucceedInfo {

    private int callType;//支付类型  1 支付宝 2微信
    private int payType;//下单类型 1 下单支付 2充值支付
    private boolean paySucceed;//支付是否成功

    private String tag;//

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public boolean isPaySucceed() {
        return paySucceed;
    }

    public void setPaySucceed(boolean paySucceed) {
        this.paySucceed = paySucceed;
    }

    public int getCallType() {

        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }
}
