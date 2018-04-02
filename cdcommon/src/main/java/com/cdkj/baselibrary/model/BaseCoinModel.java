package com.cdkj.baselibrary.model;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;

/**
 * Created by lei on 2018/3/14.
 */

public class BaseCoinModel extends DataSupport implements Cloneable {


    /**
     * symbol : OGC
     * ename : OrangeCoin
     * cname : 橙币
     * type : 1
     * unit : 8
     * icon : http://m.hichengdai.com/icon.png
     * pic1 : http://m.hichengdai.com/pic1.png
     * pic2 : http://m.hichengdai.com/pic2.png
     * pic3 : http://m.hichengdai.com/pic3.png
     * orderNo : 4
     * collectStart : 1000000000
     * withdrawFee : 100000000
     * contractAddress : 0xA1b7f66d2c5Cd89A848c75CCda085117825A0Af9
     * status : 0
     * updater : admin
     * updateDatetime : Mar 13, 2018 10:01:59 PM
     * remark : 发布测试
     */

    private String symbol;
    private String ename;
    private String cname;
    private String type;
    private int unit;
    private String icon;
    private String pic1;
    private String pic2;
    private String pic3;
    private int orderNo;
    private int collectStart;
    private BigDecimal withdrawFee;
    private String withdrawFeeString;
    private String contractAddress;
    private String status;
    private String updater;
    private String updateDatetime;
    private String remark;
    private boolean choose = false;
    private boolean isShowTip = false;

    public String getWithdrawFeeString() {
        return withdrawFeeString;
    }

    public void setWithdrawFeeString(String withdrawFeeString) {
        this.withdrawFeeString = withdrawFeeString;
    }

    public boolean isShowTip() {
        return isShowTip;
    }

    public void setShowTip(boolean showTip) {
        isShowTip = showTip;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getCollectStart() {
        return collectStart;
    }

    public void setCollectStart(int collectStart) {
        this.collectStart = collectStart;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
