package com.cdkj.bcoin.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.CoinAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.FragmentWalletBinding;
import com.cdkj.bcoin.model.CoinModel;
import com.cdkj.bcoin.model.RateModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * Created by lei on 2017/8/21.
 */

public class WalletFragment extends BaseLazyFragment {

    private CoinAdapter adapter;

    private FragmentWalletBinding mBinding;

    private BaseRefreshCallBack back;
    private RefreshHelper refreshHelper;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static WalletFragment getInstance() {
        WalletFragment fragment = new WalletFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, null, false);

        back = new BaseRefreshCallBack() {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                mBinding.refreshLayout.setEnableLoadmore(false);
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                adapter = new CoinAdapter(listData);
                return adapter;
            }

            @Override
            public void getListDataRequest(int pageIndex, int limit, boolean isShowDialog) {

                if(TextUtils.isEmpty(SPUtilHelper.getUserToken()))
                    return;

                Map<String, Object> map = new HashMap<>();
                map.put("currency", "");
                map.put("userId", SPUtilHelper.getUserId());
                map.put("token", SPUtilHelper.getUserToken());

                Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

                addCall(call);

                showLoadingDialog();

                call.enqueue(new BaseResponseModelCallBack<CoinModel>(mActivity) {

                    @Override
                    protected void onSuccess(CoinModel data, String SucMessage) {

                        if (data == null)
                            return;

                        setView(data);
                        refreshHelper.setData(data.getAccountList(), getStrRes(R.string.wallet_none), R.mipmap.order_none);
                    }

                    @Override
                    protected void onFinish() {
                        disMissLoading();
                    }
                });

            }
        };

        refreshHelper = new RefreshHelper(mActivity, back);

        init();
        initListener();

        // 创建时获取并保存汇率，否则直接打开汇率列表报错
        getRate();

        return mBinding.getRoot();
    }

    private void init() {

        refreshHelper.init(10);
        // 刷新
        refreshHelper.onDefaluteMRefresh(true);
    }

    private void initListener() {
        mBinding.llExchange.setOnClickListener(view -> {
            ExchangeActivity.open(mActivity);
        });
    }

    @Override
    protected void lazyLoad() {
        if (mBinding != null) {
            getRate();
            refreshHelper.onMRefresh(1,10,true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && mBinding != null) {
            getRate();
            refreshHelper.onMRefresh(1,10,true);
        }
    }

    @Override
    protected void onInvisible() {

    }

    private void setView(CoinModel data) {
        mBinding.tvCny.setText(data.getTotalAmountCNY()+"");
        mBinding.tvUsd.setText(data.getTotalAmountUSD()+"USD");
        mBinding.tvHkd.setText(data.getTotalAmountHKD()+"HKD");
    }

    /**
     * 获取汇率
     */
    private void getRate() {
        Map<String, String> map = new HashMap<>();
        map.put("currency", "USD");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getRate("625280", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<RateModel>(mActivity) {


            @Override
            protected void onSuccess(RateModel data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.tvRate.setText(data.getRate()+"");

                SPUtilHelper.saveRate(SPUtilHelper.USD,data.getRate()+"");
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}
