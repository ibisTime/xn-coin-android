package com.cdkj.bcoin.wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityExchangeBinding;
import com.cdkj.bcoin.model.RateModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/2.
 */

public class ExchangeActivity extends AbsBaseActivity {

    private ActivityExchangeBinding mBinding;

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, ExchangeActivity.class));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_exchange, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.wallet_title_exchange));
        setTopLineState(true);
        setSubLeftImgState(true);

        initListener();
        getRateList();
    }

    private void initListener() {
        mBinding.llExchangeUsd.setOnClickListener(view -> {
            ExchangePastActivity.open(this, "USD");
        });

        mBinding.llExchangeHkd.setOnClickListener(view -> {
            ExchangePastActivity.open(this, "HKD");
        });
    }

    /**
     * 获取汇率
     */
    private void getRateList() {
        Map<String, String> map = new HashMap<>();
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getRateList("625281", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<RateModel>(this) {

            @Override
            protected void onSuccess(List<RateModel> data, String SucMessage) {
                if (data == null)
                    return;

                for (RateModel model : data){
                    switch (model.getCurrency()){
                        case "HKD":
                            mBinding.tvRateHkd.setText(model.getRate()+"");
                            break;

                        case "USD":
                            mBinding.tvRateUsd.setText(model.getRate()+"");
                            break;
                    }
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }
}
