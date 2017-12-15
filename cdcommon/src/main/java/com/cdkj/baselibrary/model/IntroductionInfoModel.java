package com.cdkj.baselibrary.model;

/**
 * Created by 李先俊 on 2017/6/28.
 */

public class IntroductionInfoModel {


    /**
     * id : 35
     * type : 2
     * ckey : integralRule
     * cvalue : 积分规则
     * note : 积分规则
     * updater : admin
     * updateDatetime : Jun 28, 2017 4:21:40 PM
     * companyCode : CD-JKEG000011
     * systemCode : CD-JKEG000011
     */

    private int id;
    private String type;
    private String ckey;
    private String cvalue;
    private String updater;
    private String updateDatetime;
    private String companyCode;
    private String systemCode;

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
