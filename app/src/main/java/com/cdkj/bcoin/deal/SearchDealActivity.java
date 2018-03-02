package com.cdkj.bcoin.deal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.DealAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.DealModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.bcoin.util.DealUtil.YIFABU;

/**
 * Created by lei on 2017/11/21.
 */

public class SearchDealActivity extends BaseRefreshActivity<DealDetailModel> {


    private String coin;
    private String minPrice;
    private String maxPrice;
    private String payType;
    private String tradeType;

    public static void open(Context context, String minPrice, String maxPrice, String tradeType, String payType, String coin){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, SearchDealActivity.class)
                .putExtra("coin",coin)
                .putExtra("minPrice",minPrice)
                .putExtra("maxPrice",maxPrice)
                .putExtra("tradeType",tradeType)
                .putExtra("payType",payType));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        init(pageIndex, limit);

        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            DealDetailModel model = (DealDetailModel) mAdapter.getItem(position);

            // 是否是自己发布的
            if (model.getUser().getUserId().equals(SPUtilHelper.getUserId())){

                if (model.getTradeType().equals("1")){ // 卖币广告

                    PublishSaleActivity.open(this, YIFABU, model);

                }else { // 卖币广告

                    PublishBuyActivity.open(this, YIFABU, model);

                }

            }else {

                if (!SPUtilHelper.isLogin(this, false)) {
                    return;
                }

                DealActivity.open(this, model.getCode());
            }
        });
    }

    private void init(int pageIndex, int limit) {
        setTopTitle(getStrRes(R.string.deal_title_search_deal));
        setTopLineState(true);
        setSubLeftImgState(true);

        if (getIntent() == null)
            return;

        coin = getIntent().getStringExtra("coin");
        minPrice = getIntent().getStringExtra("minPrice");
        maxPrice = getIntent().getStringExtra("maxPrice");
        tradeType = getIntent().getStringExtra("tradeType");
        payType = getIntent().getStringExtra("payType");

        getListData(pageIndex,limit,true);
    }


    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("coin", coin);
        map.put("minPrice", minPrice);
        map.put("maxPrice", maxPrice);
        map.put("payType", payType);
        map.put("tradeType", tradeType);
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getDeal("625228", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealModel>(this) {

            @Override
            protected void onSuccess(DealModel data, String SucMessage) {
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
    protected BaseQuickAdapter onCreateAdapter(List<DealDetailModel> mDataList) {
        return new DealAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.deal_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
