package com.cdkj.ethereumdemo.model;

/**
 * Created by lei on 2017/11/16.
 */

public class SystemParameterModel {


    /**
     * id : 103
     * type : 1
     * parentKey : trade_time_out
     * dkey : 10
     * dvalue : 10
     * updater : admin
     * updateDatetime : Nov 16, 2017 3:21:06 PM
     * companyCode : CD-COIN000017
     * systemCode : CD-COIN000017
     */

    private int id;
    private String type;
    private String parentKey;
    private String dkey;
    private String ckey;
    private String dvalue;
    private String cvalue;
    private String updater;
    private String updateDatetime;
    private String companyCode;
    private String systemCode;

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getDkey() {
        return dkey;
    }

    public void setDkey(String dkey) {
        this.dkey = dkey;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
