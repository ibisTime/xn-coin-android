package com.cdkj.bcoin.wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.ExchangeAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityExchangePastBinding;
import com.cdkj.bcoin.model.ExchangeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/2.
 */

public class ExchangePastActivity extends BaseRefreshActivity<ExchangeModel.ListBean> {

    private ActivityExchangePastBinding mBinding;

    private String currency;

    public static void open(Context context, String currency){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, ExchangePastActivity.class).putExtra("currency", currency));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_exchange_past, null, false);

        setTopTitle(getStrRes(R.string.wallet_title_exchange));
        setTopLineState(true);
        setSubLeftImgState(true);

        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mBinding.getRoot());

        init();

        getListData(pageIndex,limit,true);
    }

    private void init() {
        if (getIntent() == null)
            return;

        currency = getIntent().getStringExtra("currency");
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("currency", currency);
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getExchange("625282", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ExchangeModel>(this) {

            @Override
            protected void onSuccess(ExchangeModel data, String SucMessage) {
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
    protected BaseQuickAdapter onCreateAdapter(List<ExchangeModel.ListBean> mDataList) {
        return new ExchangeAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.wallet_title_exchange_past_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
