package com.cdkj.baselibrary.model;

/**
 * Created by 李先俊 on 2017/6/16.
 */

public class EventBusModel {

    private String tag;

    private int evInt;

    private String evInfo;

    private boolean evBoolean;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getEvInt() {
        return evInt;
    }

    public void setEvInt(int evInt) {
        this.evInt = evInt;
    }

    public String getEvInfo() {
        return evInfo;
    }

    public void setEvInfo(String evInfo) {
        this.evInfo = evInfo;
    }

    public boolean isEvBoolean() {
        return evBoolean;
    }

    public void setEvBoolean(boolean evBoolean) {
        this.evBoolean = evBoolean;
    }
}
