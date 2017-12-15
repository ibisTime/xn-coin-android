package com.cdkj.baselibrary.utils.payutils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.pay.PayResult;
import com.cdkj.baselibrary.model.pay.PaySucceedInfo;
import com.cdkj.baselibrary.model.pay.WxPayRequestModel;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/4/13.
 */

public class PayUtil {

    //支付工具类型
    public static final int ALIPAY = 1;         //支付宝
    public static final int WEIXINPAY = 2;      //微信支付

    public static String CALLWXPAYTAG = "";      //调用微信支付类型

    /**
     * 调用微信支付
     */
    public static void callWXPay(Context mContext, WxPayRequestModel model, String paytag) {

        if (mContext == null || model == null) {
            CommonDialog commonDialog = new CommonDialog(mContext).builder()
                    .setTitle("提示").setContentMsg("微信支付出现未知错误")
                    .setNegativeBtn("确定", null);

            commonDialog.show();
            return;
        }

        WXUtils.SpId = model.getAppId();

        CALLWXPAYTAG = paytag;

        IWXAPI api = WXAPIFactory.createWXAPI(mContext, null);

        if (!api.isWXAppInstalled()) {
            CommonDialog commonDialog = new CommonDialog(mContext).builder()
                    .setTitle("提示").setContentMsg("亲，您要先安装微信才能使用微信支付哦！")
                    .setNegativeBtn("确定", null);

            commonDialog.show();
            return;

        }

        if (!api.isWXAppSupportAPI()) {
            CommonDialog commonDialog = new CommonDialog(mContext).builder()
                    .setTitle("提示").setContentMsg("亲，您的微信版本过低，请先更新微信！")
                    .setNegativeBtn("确定", null);

            commonDialog.show();
            return;
        }

        // 将该app注册到微信
        api.registerApp(model.getAppId());

        PayReq request = new PayReq();

        Map<String, String> map = new HashMap<>();

        request.appId = model.getAppId();  //应用ID

        request.partnerId = model.getPartnerid();  //商户号

        request.prepayId = model.getPrepayId();     //预支付ID

        request.packageValue = model.getWechatPackage(); //扩展字段

        request.nonceStr = model.getNonceStr();      //随机字符串

        request.timeStamp = model.getTimeStamp();    //签名

        request.sign = model.getSign();

        api.sendReq(request);
    }

    //支付宝支付
    public static void callAlipay(final Activity mContext, final String payInfo, final String callTag) {

        if (mContext == null || TextUtils.isEmpty(payInfo)) {
            CommonDialog commonDialog = new CommonDialog(mContext).builder()
                    .setTitle("提示").setContentMsg("支付宝调用失败")
                    .setNegativeBtn("确定", null);

            commonDialog.show();
        }

        Observable.just(payInfo).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                return alipay.pay(payInfo, true);
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        PayResult payResult = new PayResult(result);

                        String resultStatus = payResult.getResultStatus();

                        PaySucceedInfo aLiPayModel = new PaySucceedInfo();
                        aLiPayModel.setCallType(ALIPAY);
                        aLiPayModel.setTag(callTag);
                        //支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {

                            aLiPayModel.setPaySucceed(true);
                            EventBus.getDefault().post(aLiPayModel);

                        } else {
                            //支付失败
                            aLiPayModel.setPaySucceed(false);
                            EventBus.getDefault().post(aLiPayModel);

                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
//                            ToastUtils.showToast(mContext, "支付结果确认中");
                            } else {
//                            ToastUtils.showToast(mContext, "未支付");
                            }
                        }
                    }
                });
    }


}
