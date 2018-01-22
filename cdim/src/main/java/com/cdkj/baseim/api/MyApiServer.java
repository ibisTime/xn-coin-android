package com.cdkj.baseim.api;

import com.cdkj.baseim.model.TencentSignModel;
import com.cdkj.baselibrary.api.BaseResponseModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lei on 2017/11/9.
 */

public interface MyApiServer {
    /**
     * //注册
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<TencentSignModel>> getTencentSign(@Field("code") String code, @Field("json") String json);

    /**
     * 人行登录验证码
     *
     * @return
     */
    @Headers({
            "Accept: image/gif, image/jpeg, image/pjpeg, application/x-ms-application, application/xaml+xml, application/x-ms-xbap, */*",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: zh-CN",
            "Connection: Keep-Alive",
            "Cache-Control: no-cache",
            "Content-Type: application/x-www-form-urlencoded",
            "Host: ipcrs.pbccrc.org.cn",
            "Referer: https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp"
    })
    @GET("https://ipcrs.pbccrc.org.cn/imgrc.do")
    Call<ResponseBody> rhLoginCode(@Query("a") String a);
}
