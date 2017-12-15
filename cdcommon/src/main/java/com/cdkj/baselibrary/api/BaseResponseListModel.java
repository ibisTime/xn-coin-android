package com.cdkj.baselibrary.api;

import java.util.List;

/**
 * Created by 李先俊 on 2017/6/8.
 */

public class BaseResponseListModel<T>{

    private String errorCode;//状态码
    private String errorInfo;//状态描述
    private List<T> data;          //数据

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
