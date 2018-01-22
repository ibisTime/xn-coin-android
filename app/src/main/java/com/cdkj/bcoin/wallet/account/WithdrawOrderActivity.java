package com.cdkj.bcoin.wallet.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.WithdrawOrderAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.WithdrawOrderModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2018/1/17.
 */

public class WithdrawOrderActivity extends BaseRefreshActivity<WithdrawOrderModel.ListBean> {

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WithdrawOrderActivity.class));
    }


    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopTitle(getString(R.string.wallet_withdraw_order_title));
        setTopLineState(true);
        setSubLeftImgState(true);

        getListData(pageIndex, limit, true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("companyCode", MyConfig.COMPANYCODE);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("limit", limit+"");
        map.put("start", pageIndex+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getWithdrawOrder("802755", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<WithdrawOrderModel>(this) {

            @Override
            protected void onSuccess(WithdrawOrderModel data, String SucMessage) {
                if (data == null)
                    return;

                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<WithdrawOrderModel.ListBean> mDataList) {
        return new WithdrawOrderAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.wallet_withdraw_order_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
