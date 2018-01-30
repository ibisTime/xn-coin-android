package com.cdkj.bcoin.util;

import android.app.Activity;
import android.text.TextUtils;

import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.dialog.LoadingDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.deal.DealChatActivity;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.order.OrderActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2018/1/24.
 */

public class PushOrder {

    private OrderDetailModel bean;
    private Activity mContext;

    private Call call;
    protected LoadingDialog loadingDialog;

    public void getOrder(Activity context, String code, boolean isShow) {
        this.mContext = context;

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        call = RetrofitUtils.createApi(MyApi.class).getOrderDetail("625251", StringUtils.getJsonToString(map));

        if (isShow)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<OrderDetailModel>(mContext) {

            @Override
            protected void onSuccess(OrderDetailModel data, String SucMessage) {
                if (data == null)
                    return;

                bean = data;

                ImUserInfo info = new ImUserInfo();
                if (TextUtils.equals(bean.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家

                    info.setLeftImg(bean.getSellUserInfo().getPhoto());
                    info.setLeftName(bean.getSellUserInfo().getNickname());

                }else { // 自己是卖家
                    info.setLeftImg(bean.getBuyUserInfo().getPhoto());
                    info.setLeftName(bean.getBuyUserInfo().getNickname());

                }
                info.setRightImg(SPUtilHelper.getUserPhoto());
                info.setRightName(SPUtilHelper.getUserName());
                info.setIdentify(bean.getCode());

                if (bean.getStatus().equals("-1")){ // 待下单订单
                    DealChatActivity.open(mContext, bean, info);
                }else { // 已下单订单
                    OrderActivity.open(mContext, bean, info);
                }

                clear();
                SPUtilHelper.clearPushOrder();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    //处理持有对象
    public void clear() {
        if(this.call!=null){
            this.call.cancel();
            this.call = null;
        }
    }

    /**
     * 隐藏Dialog
     */
    public void disMissLoading() {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.closeDialog();
        }
    }

    /**
     * 显示dialog
     */
    public void showLoadingDialog() {
        if(loadingDialog==null){
            loadingDialog=new LoadingDialog(mContext);
        }
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.showDialog();
        }
    }
}
