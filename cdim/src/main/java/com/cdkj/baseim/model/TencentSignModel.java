package com.cdkj.baseim.model;

/**
 * 用于获取腾讯签名等
 * Created by cdkj on 2017/10/27.
 */

public class TencentSignModel {


    /**
     * txAppCode : 1400050575
     * txAppAdmin : admin
     * accountType : 19287
     * sign : eJxlj8FOg0AURfd8xYStRmeQAesOiI20JYCycFaEMlN5wQIOD4UY-92KTSRxfc7Nyf00CCFmtnu6KsqyHRrMceqUSe6ISc3LP9h1IPMC8xst-0E1dqBVXhxQ6RkyzrlF6dIBqRqEA5yNQh6hWeBe1vnc*N3bpzGn3OVLBV5mGN2LIEyDRw1bDLi9nwK3GodnLsR6Xw0J87Odkokt3nRbOdFD2Hth5W22F3xyoI4-rl1a9bHPBPrI1lky1pugjV-L0YnQ8dImXSQRjup8iNGVZXN2u6DvSvfQNrNg0ZNi0RX9eW18Gd8TRF0H
     */

    private String txAppCode;
    private String txAppAdmin;
    private String accountType;
    private String sign;

    public String getTxAppCode() {
        return txAppCode;
    }

    public void setTxAppCode(String txAppCode) {
        this.txAppCode = txAppCode;
    }

    public String getTxAppAdmin() {
        return txAppAdmin;
    }

    public void setTxAppAdmin(String txAppAdmin) {
        this.txAppAdmin = txAppAdmin;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
