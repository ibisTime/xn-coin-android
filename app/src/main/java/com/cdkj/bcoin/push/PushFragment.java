package com.cdkj.bcoin.push;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.MyPickerPopupWindow;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.PushAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.FragmentPushBinding;
import com.cdkj.bcoin.deal.DealActivity;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.DealModel;
import com.cdkj.bcoin.model.LastestPriceModel;
import com.cdkj.bcoin.order.OrderActivity;
import com.cdkj.bcoin.user.UserPublishedActivity;
import com.cdkj.bcoin.util.CoinUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_ALL;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_SINGEL;
import static com.cdkj.bcoin.util.AccountUtil.formatDouble;

/**
 * Created by lei on 2018/3/13.
 */

public class PushFragment extends BaseRefreshFragment<DealDetailModel> {

    private FragmentPushBinding mBinding;

    // PUSH广告的交易类型，买币页面应该取"卖币类型广告"，卖币反之
    private String tradeType = "1";// 0：买，1：卖

    // 币种
    private String coinType = "";
    private String[] coin;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static PushFragment getInstance() {
        PushFragment fragment = new PushFragment();
        return fragment;
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    protected void afterCreate(int pageIndex, int limit) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_push, null, false);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mBinding.getRoot());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            DealDetailModel model = (DealDetailModel) mAdapter.getItem(position);

            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }

            DealActivity.open(mActivity, model.getCode(),model.getTradeType());

        });

        inits();
        initTitleBar();
        initListener();

    }

    @Override
    public void onResume() {
        super.onResume();

        onMRefresh(1,10,true);
    }

    private void inits() {
        // 初始化查询币种
        coin = CoinUtil.getTokenCoinArray();
        if (coin.length > 0){
            coinType = coin[0];
            mBinding.tvCoin.setText(coinType);
        }else {
            coinType = "-1";
            mBinding.tvCoin.setText("");
        }


    }

    private void initTitleBar() {
        setTopTitle(StringUtil.getString(R.string.main_tab_push));
        setTopTitleLine(true);
    }

    private void initListener() {
        mBinding.llCoin.setOnClickListener(view -> {
            if (coin.length > 0){
                initPopup(view);
            }

        });

        mBinding.fragmentPushBar.llBuy.setOnClickListener(view -> {
            tradeType = "1";
            initBarBtn(tradeType);

            onMRefresh(1,10, false);
        });

        mBinding.fragmentPushBar.llSale.setOnClickListener(view -> {
            tradeType = "0";
            initBarBtn(tradeType);

            onMRefresh(1,10, false);
        });

        mBinding.fragmentPushBar.llDeal.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }

            UserPublishedActivity.open(mActivity, coinType);
        });

        mBinding.fragmentPushBar.llOrder.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }

            OrderActivity.open(mActivity);
        });
    }

    /**
     *
     * @param view
     */
    private void initPopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(mActivity, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, coin);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            coinType = popupWindow.getNumberPicker(R.id.np_type, coin);
            mBinding.tvCoin.setText(coinType);

            onMRefresh(1,10, false);
            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }

    private void initBarBtn(String location){
        if (location.equals("1")){
            mBinding.fragmentPushBar.ivBuy.setVisibility(View.VISIBLE);
            mBinding.fragmentPushBar.ivSale.setVisibility(View.GONE);
        }else {
            mBinding.fragmentPushBar.ivBuy.setVisibility(View.GONE);
            mBinding.fragmentPushBar.ivSale.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onMRefresh(int pageIndex, int limit) {
        EventBusModel model = new EventBusModel();
        model.setTag(BASE_COIN_LIST);
        // 是否需要通知所有需要的地方刷新CoinList配置
        model.setEvBoolean(false);
        // 不是的话需要告知其需要更新的位置
        model.setEvInfo("push");
        EventBus.getDefault().post(model);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, Object> map = new HashMap<>();
        map.put("coin", coinType);
        map.put("tradeType", tradeType);
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getDeal("625228", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealModel>(mActivity) {

            @Override
            protected void onSuccess(DealModel data, String SucMessage) {
                if (data == null || data.getList() == null)
                    return;

                setData(data.getList());

                getLastOrderAmount();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<DealDetailModel> mDataList) {
        return new PushAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return StringUtil.getString(R.string.push_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }

    /**
     * 获取banner
     */
    private void getLastOrderAmount() {
        if (coinType.equals("-1")){
            mBinding.llNewTradPrice.setText("");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("tradeCoin", coinType);

        Call call = RetrofitUtils.createApi(MyApi.class).getLastOrderAmount("625283", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<LastestPriceModel>(mActivity) {

            @Override
            protected void onSuccess(LastestPriceModel data, String SucMessage) {
                if (data == null){
                    return;
                }
                mBinding.llNewTradPrice.setText(MoneyUtils.MONEYSING+formatDouble(data.getLastestPrice()));

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Subscribe
    public void eventBusModel(EventBusModel model) {
        if (model == null)
            return;

        switch (model.getTag()){

            // CoinList配置更新通知，单一通知需要验证是否是自己
            case BASE_COIN_LIST_NOTIFY_SINGEL:
                if (!model.getEvInfo().equals("push"))
                    return;

            case BASE_COIN_LIST_NOTIFY_ALL:
                inits();
                onMRefresh(1,10,true);

                break;
        }

    }
}
