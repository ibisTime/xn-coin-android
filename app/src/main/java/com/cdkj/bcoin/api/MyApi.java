package com.cdkj.bcoin.api;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.bcoin.model.AddressModel;
import com.cdkj.bcoin.model.BannerModel;
import com.cdkj.bcoin.model.BillModel;
import com.cdkj.bcoin.model.ConsultingModel;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.DealHistoryModel;
import com.cdkj.bcoin.model.DealModel;
import com.cdkj.bcoin.model.DealResultModel;
import com.cdkj.bcoin.model.DealUserDataModel;
import com.cdkj.bcoin.model.ExchangeModel;
import com.cdkj.bcoin.model.InviteModel;
import com.cdkj.bcoin.model.LastestPriceModel;
import com.cdkj.bcoin.model.MarketCoinModel;
import com.cdkj.bcoin.model.MarketModel;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.model.OrderModel;
import com.cdkj.bcoin.model.RateModel;
import com.cdkj.bcoin.model.SystemMessageModel;
import com.cdkj.bcoin.model.SystemParameterListModel;
import com.cdkj.bcoin.model.SystemParameterModel;
import com.cdkj.bcoin.model.TrustModel;
import com.cdkj.bcoin.model.UserInviteProfitModel;
import com.cdkj.bcoin.model.UserRefereeModel;
import com.cdkj.bcoin.model.UserSettingModel;
import com.cdkj.bcoin.model.VersionModel;
import com.cdkj.bcoin.model.WithdrawOrderModel;
import com.cdkj.bcoin.model.ZfqrImage;

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
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserLoginModel>> signUp(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<com.cdkj.bcoin.model.CoinModel>> getAccount(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账户
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealHistoryModel>> getDealHistory(@Field("code") String code, @Field("json") String json);

    /**
     * 获取统计信息
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealUserDataModel>> getDealUserData(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户账单
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<BillModel>> getBillListData(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户信息详情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserInfoModel>> getUserInfoDetails(@Field("code") String code, @Field("json") String json);

    /**
     * 获取轮播图
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<BannerModel>> getBanner(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemParameterModel>> getSystemParameter(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemParameterListModel>> getSystemParameterList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统参数
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<SystemParameterModel>> getSystemInformation(@Field("code") String code, @Field("json") String json);

    /**
     * 获取用户设置
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<UserSettingModel>> getUserSetting(@Field("code") String code, @Field("json") String json);

    /**
     * 获取邀请数据
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<InviteModel>> getInvite(@Field("code") String code, @Field("json") String json);

    /**
     * 获取电子货币行情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinModel>> getMarketCoinList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取电子货币行情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketCoinModel>> getMarketCoin(@Field("code") String code, @Field("json") String json);


    /**
     * 数字货币，平台干预后的价格
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<MarketCoinModel>> getTruePrice(@Field("code") String code, @Field("json") String json);

    /**
     * 获取法币汇率
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<RateModel>> getRate(@Field("code") String code, @Field("json") String json);

    /**
     * 获取法币汇率
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<RateModel>> getRateList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取货币行情
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<MarketModel>> getMarket(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统消息
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<SystemMessageModel>> getSystemMessage(@Field("code") String code, @Field("json") String json);

    /**
     * 获取系统咨询
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ConsultingModel>> getConsulting(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealModel>> getDeal(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<DealDetailModel>> getDealList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<WithdrawOrderModel>> getWithdrawOrder(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealDetailModel>> getDealDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取信任列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<TrustModel>> getTrust(@Field("code") String code, @Field("json") String json);

    /**
     * 获取推荐列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<UserRefereeModel>> getUserReferee(@Field("code") String code, @Field("json") String json);

    /**
     * 获取汇率
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ExchangeModel>> getExchange(@Field("code") String code, @Field("json") String json);

    /**
     * 获取交易结果
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<DealResultModel>> getDealResult(@Field("code") String code, @Field("json") String json);

    /**
     * 获取订单
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<OrderModel>> getOrder(@Field("code") String code, @Field("json") String json);

    /**
     * 获取订单
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<OrderDetailModel>> getOrderDetail(@Field("code") String code, @Field("json") String json);

    /**
     * 获取地址
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<AddressModel>> getAddress(@Field("code") String code, @Field("json") String json);

    /**
     * 获取版本
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<VersionModel>> getVersion(@Field("code") String code, @Field("json") String json);


    /**
     * 获取支持的币种
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<BaseCoinModel>> getCoinList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取收益列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseListModel<UserInviteProfitModel>> getInviteProfitList(@Field("code") String code, @Field("json") String json);

    /**
     * 获取收益列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<LastestPriceModel>> getLastOrderAmount(@Field("code") String code, @Field("json") String json);

    /**
     * 获取收益列表
     *
     * @param code
     * @param json
     * @return
     */
    @FormUrlEncoded
    @POST("api")
    Call<BaseResponseModel<ZfqrImage>> getQrData(@Field("code") String code, @Field("json") String json);


}
