package com.cdkj.bcoin.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.MarketCoinAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.MarketCoinModel;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/10/27.
 */

public class MarketActivity extends BaseRefreshActivity<MarketCoinModel> {

    private String coin = "";

    public static void open(Context context, String coin){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MarketActivity.class).putExtra("coin", coin));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        // 初始化title
        setTopTitle(StringUtil.getStirng(R.string.market_title));
        setTopLineState(true);
        setSubLeftImgState(true);

        if (getIntent() == null)
            return;

        coin = getIntent().getStringExtra("coin");
        setTopTitle(coin + StringUtil.getStirng(R.string.market_title));
        getListData(pageIndex, limit, true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("coin", coin);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getMarketCoinList("625291", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<MarketCoinModel>(this) {


            @Override
            protected void onSuccess(List<MarketCoinModel> data, String SucMessage) {
                if (data == null)
                    return;
                setData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<MarketCoinModel> mDataList) {
        return new MarketCoinAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return StringUtil.getStirng(R.string.market_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
