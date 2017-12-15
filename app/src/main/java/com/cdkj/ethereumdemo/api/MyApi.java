package com.cdkj.ethereumdemo.api;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.ethereumdemo.model.AddressModel;
import com.cdkj.ethereumdemo.model.BannerModel;
import com.cdkj.ethereumdemo.model.BillModel;
import com.cdkj.ethereumdemo.model.CoinModel;
import com.cdkj.ethereumdemo.model.ConsultingModel;
import com.cdkj.ethereumdemo.model.DealDetailModel;
import com.cdkj.ethereumdemo.model.DealHistoryModel;
import com.cdkj.ethereumdemo.model.DealModel;
import com.cdkj.ethereumdemo.model.DealResultModel;
import com.cdkj.ethereumdemo.model.DealUserDataModel;
import com.cdkj.ethereumdemo.model.ExchangeModel;
import com.cdkj.ethereumdemo.model.InviteModel;
import com.cdkj.ethereumdemo.model.MarketCoinModel;
import com.cdkj.ethereumdemo.model.MarketModel;
import com.cdkj.ethereumdemo.model.OrderDetailModel;
import com.cdkj.ethereumdemo.model.OrderModel;
import com.cdkj.ethereumdemo.model.RateModel;
import com.cdkj.ethereumdemo.model.SystemMessageModel;
import com.cdkj.ethereumdemo.model.SystemParameterListModel;
import com.cdkj.ethereumdemo.model.SystemParameterModel;
import com.cdkj.ethereumdemo.model.TrustModel;
import com.cdkj.ethereumdemo.model.UserRefereeModel;
import com.cdkj.ethereumdemo.model.UserSettingModel;
import com.cdkj.ethereumdemo.model.VersionModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lei on 2017/10/19.
 */

public interface MyApi {

    /**
     * 注册
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserLoginModel>> signUp( @Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<CoinModel>> getAccount(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealHistoryModel>> getDealHistory(@Field("code") String code, @Field("json") String json);

    /**
     * 获取统计信息
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealUserDataModel>> getDealUserData(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账单
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BillModel>> getBillListData(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户信息详情
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserInfoModel>> getUserInfoDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取轮播图
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<BannerModel>> getBanner(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemParameterModel>> getSystemParameter(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemParameterListModel>> getSystemParameterList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<SystemParameterModel>> getSystemInformation(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户设置
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<UserSettingModel>> getUserSetting(@Field("code") String code, @Field("json") String json);

    /**
     * 获取邀请数据
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<InviteModel>> getInvite(@Field("code") String code, @Field("json") String json);

    /**
     * 获取电子货币行情
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinModel>> getMarketCoinList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取电子货币行情
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinModel>> getMarketCoin(@Field("code") String code, @Field("json") String json);


    /**
     * 数字货币，平台干预后的价格
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<MarketCoinModel>> getTruePrice(@Field("code") String code, @Field("json") String json);

    /**
     * 获取法币汇率
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<RateModel>> getRate(@Field("code") String code, @Field("json") String json);

    /**
     * 获取法币汇率
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<RateModel>> getRateList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取货币行情
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketModel>> getMarket(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统消息
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemMessageModel>> getSystemMessage(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统咨询
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ConsultingModel>> getConsulting(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealModel>> getDeal(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<DealDetailModel>> getDealList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealDetailModel>> getDealDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取信任列表
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<TrustModel>> getTrust(@Field("code") String code, @Field("json") String json);

    /**
     * 获取推荐列表
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserRefereeModel>> getUserReferee(@Field("code") String code, @Field("json") String json);

    /**
     * 获取汇率
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ExchangeModel>> getExchange(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易结果
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealResultModel>> getDealResult(@Field("code") String code, @Field("json") String json);

    /**
     * 获取订单
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<OrderModel>> getOrder(@Field("code") String code, @Field("json") String json);

    /**
     * 获取订单
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<OrderDetailModel>> getOrderDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取地址
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<AddressModel>> getAddress(@Field("code") String code, @Field("json") String json);

    /**
     * 获取版本
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<VersionModel>> getVersion(@Field("code") String code, @Field("json") String json);

}
