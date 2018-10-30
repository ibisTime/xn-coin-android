package com.cdkj.bcoin.deal;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.CoinRecyclerAdapter;
import com.cdkj.bcoin.adapter.DealAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.FragmentDealBinding;
import com.cdkj.bcoin.loader.BannerImageLoader;
import com.cdkj.bcoin.model.BannerModel;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.DealModel;
import com.cdkj.bcoin.util.CoinUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_ALL;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_SINGEL;
import static com.cdkj.baselibrary.appmanager.EventTags.DEAL_PAGE_CHANGE;


/**
 * Created by lei on 2017/8/21.
 */

public class DealFragment extends BaseRefreshFragment<DealDetailModel> {

    private FragmentDealBinding mBinding;

    private CoinRecyclerAdapter coinRecyclerAdapter;

    // 广告交易类型，买币页面应该取"卖币类型广告"，卖币反之
    private String tradeType = "1"; // 0：买币，1：卖币

    private List<String> banner = new ArrayList<>();
    private List<BannerModel> bannerData = new ArrayList<>();

    // 币种
    private String coinType;
    private List<BaseCoinModel> list = new ArrayList<>();

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static DealFragment getInstance() {
        DealFragment fragment = new DealFragment();
        return fragment;
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    protected void afterCreate(int pageIndex, int limit) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_deal, null, false);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mBinding.getRoot());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            DealDetailModel model = (DealDetailModel) mAdapter.getItem(position);

            // 是否是自己发布的
//            if (model.getUser().getUserId().equals(SPUtilHelper.getUserId())){
//
//                if (model.getTradeType().equals("1")){ // 卖币广告
//
//                    DealPublishSaleActivity.open(mActivity, YIFABU, model);
//
//                }else { // 卖币广告
//
//                    DealPublishBuyActivity.open(mActivity, YIFABU, model);
//
//                }
//
//            }else {

                if (!SPUtilHelper.isLogin(mActivity, false)) {
                    return;
                }

                DealActivity.open(mActivity, model.getCode());

//            }

        });

        inits();
        initTitleBar();
        initRecyclerView();
    }

    private void inits() {
        // 初始化默认查询币种
        list.clear();
        list.addAll(CoinUtil.getNotTokenCoinList());

        if (list.size() > 0){
            coinType = list.get(0).getSymbol();
        }else {
            coinType = "";
        }
    }

    private void initTitleBar() {
        setTitleBarNoLeft(StringUtil.getString(R.string.deal_buy),StringUtil.getString(R.string.deal_sale));
        setTopTitleLine(true);

        setTitleBarBtn1Click(v -> {
            setTitleBarBtnViewChange(1);

            tradeType = "1";
            onMRefresh(1,10, true);
        });

        setTitleBarBtn2Click(v -> {
            setTitleBarBtnViewChange(0);

            tradeType = "0";
            onMRefresh(1,10, false);
        });

        setTitleBarRightClick(v -> {
            DealSearchActivity.open(mActivity, coinType);
        });
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.rvCoin.setLayoutManager(linearLayoutManager);
        //设置适配器
        coinRecyclerAdapter = new CoinRecyclerAdapter(list);
        mBinding.rvCoin.setAdapter(coinRecyclerAdapter);

        coinRecyclerAdapter.setOnItemClickListener((adapter, view, position) -> {
            // 改变UI
            for (BaseCoinModel model : list) {
                model.setChoose(false);
            }
            list.get(position).setChoose(true);
            coinRecyclerAdapter.notifyDataSetChanged();

            // 储存选择的币种
            coinType = list.get(position).getSymbol();
            onMRefresh(1,10,true);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        onMRefresh(1,10,true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.banner.stopAutoPlay();
    }

    @Override
    protected void onMRefresh(int pageIndex, int limit) {
        EventBusModel model = new EventBusModel();
        model.setTag(BASE_COIN_LIST);
        // 是否需要通知所有需要的地方刷新CoinList配置
        model.setEvBoolean(false);
        // 不是的话需要告知其需要更新的位置
        model.setEvInfo("deal");
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
                if (data == null)
                    return;

                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

        // 刷新轮播图
        getBanner();
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<DealDetailModel> mDataList) {
        return new DealAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return StringUtil.getString(R.string.deal_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }

    /**
     * 获取banner
     */
    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("location", "trade"); // 交易位置轮播
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBanner("805806", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {

            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                if (data != null){
                    bannerData = data;
                    banner.clear();
                    for (BannerModel model : data) {
                        banner.add(model.getPic());
                    }
                }

                initBanner();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initBanner() {
        if (banner == null) return;

        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBinding.banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        mBinding.banner.setImages(banner);
        //设置banner动画效果
        mBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mBinding.banner.setOnBannerClickListener(position -> {

            if (bannerData.get(position-1).getUrl()!=null){
                if (bannerData.get(position-1).getUrl().indexOf("http") != -1){
                    WebViewActivity.openURL(mActivity,bannerData.get(position-1).getName(),bannerData.get(position-1).getUrl());
                }
            }

        });
        //banner设置方法全部调用完毕时最后调用
        mBinding.banner.start();

        // 设置在操作Banner时listView事件不触发
//        mBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }


    @Subscribe
    public void eventBusModel(EventBusModel eventBusModel) {
        if (eventBusModel == null) {
            return;
        }

        switch (eventBusModel.getTag()){
            case DEAL_PAGE_CHANGE:

                setTitleBarBtnViewChange(eventBusModel.getEvInt());
                tradeType = eventBusModel.getEvInt()+"";

                // 设置要显示的币种
                coinType = eventBusModel.getEvInfo();
                setTitleBarCoin(coinType);

                onMRefresh(1,10, true);

                break;

            // CoinList配置更新通知，单一通知需要验证是否是自己
            case BASE_COIN_LIST_NOTIFY_SINGEL:
                if (!eventBusModel.getEvInfo().equals("deal"))
                    return;

            case BASE_COIN_LIST_NOTIFY_ALL:
                inits();
                coinRecyclerAdapter.notifyDataSetChanged();

                onMRefresh(1,10,true);

                break;
        }
    }

}
