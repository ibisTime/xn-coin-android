package com.cdkj.bcoin.model;

/**
 * Created by lei on 2017/11/16.
 */

public class UserSettingModel {


    /**
     * id : 2
     * userId : U201711212030187015235
     * type : 0
     * value : 1
     * createDatetime : Dec 6, 2017 1:55:17 PM
     * updateDatetime : Dec 6, 2017 1:55:17 PM
     */

    private int id;
    private String userId;
    private String type;
    private String value;
    private String createDatetime;
    private String updateDatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
