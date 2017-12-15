package com.cdkj.baseim.api;

import com.cdkj.baseim.model.TencentSignModel;
import com.cdkj.baselibrary.api.BaseResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}
