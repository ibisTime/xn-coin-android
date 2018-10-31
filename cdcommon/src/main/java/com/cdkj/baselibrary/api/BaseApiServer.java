package com.cdkj.baselibrary.api;


import com.cdkj.baselibrary.model.AliTokenModel;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.QiniuGetTokenModel;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.model.pay.AliPayRequestMode;
import com.cdkj.baselibrary.model.pay.WxPayRequestModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 李先俊 on 2017/6/8.
 */

public interface BaseApiServer {
    /**
     * 登录
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserLoginModel>> userLogin(@Field("code") String code, @Field("json") String  json);

    /**
     *获取用户信息详情
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserInfoModel>> getUserInfoDetails(@Field("code") String code, @Field("json") String json);

    /**
     *只返回成功失败
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<IsSuccessModes>> successRequest(@Field("code") String code, @Field("json") String  json);

    /**
     *只返回code
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CodeModel>> codeRequest(@Field("code") String code, @Field("json") String  json);

    /**
     * 七牛
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<QiniuGetTokenModel>> getQiniuTOken(@Field("code") String code, @Field("json") String  json);
/**
     * 七牛
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<AliTokenModel>> getAliTOken(@Field("code") String code, @Field("json") String  json);


    /**
     * Test
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<String>> stringRequest(@Field("code") String code, @Field("json") String  json);


    /**
     * 支付(支付宝)
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<AliPayRequestMode>> aliPayRequest(@Field("code") String code, @Field("json") String  json);

    /**
     * 微信支付
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<WxPayRequestModel>> wxPayRequest(@Field("code") String code, @Field("json") String  json);


    /**
     * 根据ckey查询系统参数
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<IntroductionInfoModel>> getKeySystemInfo(@Field("code") String code, @Field("json") String  json);




}
