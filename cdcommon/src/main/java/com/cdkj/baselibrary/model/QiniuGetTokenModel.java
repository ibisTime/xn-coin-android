package com.cdkj.baselibrary.model;

/**用于获取七牛TOken
 * Created by 李先俊 on 2017/6/14.
 */

public class QiniuGetTokenModel {

    private String uploadToken;
    private String token;
    private String encodedPolicy;

    public String getEncodedPolicy() {
        return encodedPolicy;
    }

    public void setEncodedPolicy(String encodedPolicy) {
        this.encodedPolicy = encodedPolicy;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }
}
