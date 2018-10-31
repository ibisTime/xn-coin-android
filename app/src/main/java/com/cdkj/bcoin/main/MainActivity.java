package com.cdkj.bcoin.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baseim.interfaces.TxImLoginPresenter;
import com.cdkj.baseim.maneger.TXImManager;
import com.cdkj.baseim.util.PushUtil;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityMainBinding;
import com.cdkj.bcoin.deal.DealFragment;
import com.cdkj.bcoin.deal.view.PublishWindow;
import com.cdkj.bcoin.market.MarketFragment;
import com.cdkj.bcoin.model.VersionModel;
import com.cdkj.bcoin.push.PushFragment;
import com.cdkj.bcoin.service.CoinListService;
import com.cdkj.bcoin.service.OrderTipService;
import com.cdkj.bcoin.user.UserFragment;
import com.cdkj.bcoin.util.CoinUtil;
import com.cdkj.bcoin.util.PushOrder;
import com.cdkj.bcoin.util.StringUtil;
import com.cdkj.bcoin.wallet.WalletFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_DONE;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_NEW;
import static com.cdkj.baselibrary.appmanager.EventTags.MAIN_CHANGE_SHOW_INDEX;
import static com.cdkj.bcoin.util.UpdateUtil.startWeb;
import static com.cdkj.bcoin.util.ZenDeskUtil.initZenDeskIdentity;

@Route(path = "/main/page")
public class MainActivity extends AbsBaseActivity {

    private PublishWindow mPublishWindow;
    private ActivityMainBinding mBinding;

    private TxImLoginPresenter mPresenter;

    private PushOrder pushOrder;

    public static int NOW_INDEX = 0;
    public static final int MARKET = 0;
    public static final int DEAL = 1;
    public static final int PUSH = 2;
    public static final int WALLET = 3;
    public static final int MY = 4;
    private List<Fragment> fragments;

    public static Context mContext;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViewPager();
        initListener();

        init();

        // 离线推送：如果有订单先打开订单详情，避免更新提示阻挡用户操作
        if (!SPUtilHelper.getPushOrder().equals("")){
            pushOrder.getOrder(this, SPUtilHelper.getPushOrder(), true);
        }else {
            getVersion();
        }

        if(!SPUtilHelper.getUserId().equals("")){
            getUserData();
            updateOnLineTime();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 设置消息接收在后台
        PushUtil.getInstance().reset();

        if(!SPUtilHelper.getUserId().equals("")){
            // 设置腾讯云的昵称
            TXImManager.getInstance().setUserNickName(SPUtilHelper.getUserName(), new TXImManager.ChangeInfoBallBack() {
                @Override
                public void onError(int i, String s) {
                    Log.e("TencentNickName:Error","code="+i+":"+s);
                }

                @Override
                public void onSuccess() {
                    Log.e("TencentNickName:Success","");
                }
            });

            initZenDeskIdentity(SPUtilHelper.getUserName(), SPUtilHelper.getUserEmail());
        }
    }


    private void init() {
        mContext = this;
        pushOrder = new PushOrder();

        OrderTipService.open(this);
        CoinListService.open(this);

        setShowIndex(MARKET);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mBinding.layoutMainBottom.llMarket.setOnClickListener(v -> {
            setShowIndex(MARKET);

        });

        mBinding.layoutMainBottom.llDeal.setOnClickListener(v -> {
            setShowIndex(DEAL);

        });

        mBinding.layoutMainBottom.llPush.setOnClickListener(v -> {

            setShowIndex(PUSH);

        });


        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            setShowIndex(WALLET);

        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            setShowIndex(MY);

        });

        mBinding.ivPublish.setOnClickListener(view -> {
            showMoreWindow(view);
        });

    }

    private void showMoreWindow(View view) {
        if (null == mPublishWindow) {

            // 通过主页当前的tabIndex判断发布的广告是否是Token币,NOW_INDEX = 2 时在PUSH界面，发布Token币
            // 配置Coin列表为空时，不可发布
            if (MainActivity.NOW_INDEX == 2){
                if (CoinUtil.getTokenCoinArray().length == 0){
                    return;
                }
            }else {
                if (CoinUtil.getNotTokenCoinArray().length == 0){
                    return;
                }
            }
            mPublishWindow = new PublishWindow(this);
            mPublishWindow.init();
        }

        mPublishWindow.showMoreWindow(view);
    }

    public void setTabIndex(int index) {
        setTabDark();
        switch (index) {
            case MARKET:
                mBinding.ivPublish.setVisibility(View.GONE);
                mBinding.layoutMainBottom.ivMarket.setImageResource(R.mipmap.main_market_light);
                mBinding.layoutMainBottom.tvMarket.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case DEAL:
                mBinding.ivPublish.setVisibility(View.VISIBLE);
                mBinding.layoutMainBottom.ivDeal.setImageResource(R.mipmap.main_deal_light);
                mBinding.layoutMainBottom.tvDeal.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case PUSH:
                mBinding.ivPublish.setVisibility(View.VISIBLE);
                mBinding.layoutMainBottom.ivPush.setImageResource(R.mipmap.main_push_light);
                mBinding.layoutMainBottom.tvPush.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case WALLET:
                mBinding.ivPublish.setVisibility(View.GONE);
                mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_light);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;

            case MY:
                mBinding.ivPublish.setVisibility(View.GONE);
                mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_light);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }

    }

    private void setTabDark(){
        mBinding.layoutMainBottom.ivMarket.setImageResource(R.mipmap.main_market_dark);
        mBinding.layoutMainBottom.tvMarket.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivDeal.setImageResource(R.mipmap.main_deal_dark);
        mBinding.layoutMainBottom.tvDeal.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivPush.setImageResource(R.mipmap.main_push_dark);
        mBinding.layoutMainBottom.tvPush.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_dark);
        mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_dark);
        mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mBinding.pagerMain.setPagingEnabled(false);//禁止左右切换

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(MarketFragment.getInstance());
        fragments.add(DealFragment.getInstance());
        fragments.add(PushFragment.getInstance());
        fragments.add(WalletFragment.getInstance());
        fragments.add(UserFragment.getInstance());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }


    /**
     * 设置要显示的界面
     *
     * @param index
     */
    private void setShowIndex(int index) {
        NOW_INDEX = index;

        if (index < 0 && index >= fragments.size()) {
            return;
        }

        mBinding.pagerMain.setCurrentItem(index, false);
        setTabIndex(index);

        if(!SPUtilHelper.getUserId().equals("")){
            updateOnLineTime();
        }
    }


    /**
     * 初始化用户信息
     */
    public void getUserData() {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {

                if (data == null)
                    return;

                SPUtilHelper.saveSecretUserId(data.getSecretUserId());

                SPUtilHelper.saveUserPhoto(data.getPhoto());
                SPUtilHelper.saveUserEmail(data.getEmail());
                SPUtilHelper.saveUserName(data.getNickname());
                SPUtilHelper.saveRealName(data.getRealName());
                SPUtilHelper.saveUserPhoneNum(data.getMobile());
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());

                //
                initZenDeskIdentity(SPUtilHelper.getUserName(), SPUtilHelper.getUserEmail());

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 更新在线时间
     */
    public void updateOnLineTime() {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805083", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data == null)
                    return;

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = fragments.get(fragments.size()-1);
        fragment.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onBackPressed() {
        showDoubleWarnListen(StringUtil.getString(R.string.exit_confirm), view -> {
            EventBus.getDefault().post(EventTags.AllFINISH);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        OrderTipService.close(this);
        CoinListService.close(this);
    }

    @Subscribe
    public void MainEventBus(EventBusModel eventBusModel) {
        if (eventBusModel == null) {
            return;
        }

        if (TextUtils.equals(eventBusModel.getTag(), MAIN_CHANGE_SHOW_INDEX)) {
            setShowIndex(eventBusModel.getEvInt());
        }
    }

    /**
     * 获取最新版本
     * @return
     */
    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "android-c");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("660918", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(this) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null)
                    return;

                if (data.getVersion() == null)
                    return;

                if (!data.getVersion().equals(getVersionName())) {
                    update(data.getNote(), data.getDownloadUrl(), data.getForceUpdate());
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void update(String msg, final String url, String force) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(StringUtil.getString(R.string.tip))
                .setMessage(msg)
                .setPositiveButton(StringUtil.getString(R.string.confirm), (dialogInterface, i) -> {

                    startWeb(MainActivity.this,url);
                    EventBus.getDefault().post(EventTags.AllFINISH);
                    finish();

                })
                .setCancelable(false);


        if(force.equals("1")){ // 强制更新
            builder.show();
        }else {
            builder.setNegativeButton(StringUtil.getString(R.string.cancel), null).show();
        }
    }


    int newUnreadMsg = 0;
    int doneUnreadMsg = 0;

    @Subscribe
    public void txImMsgUpdate(EventBusModel model) {
        if (model == null)
            return;

        if (model.getTag().equals(IM_MSG_TIP_NEW)){
            newUnreadMsg = model.getEvInt();
            setMsgUnread();
        }

        if (model.getTag().equals(IM_MSG_TIP_DONE)){
            doneUnreadMsg = model.getEvInt();
            setMsgUnread();
        }
    }

    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(){

        mBinding.layoutMainBottom.ivMsgTip.setVisibility(newUnreadMsg+doneUnreadMsg == 0 ? View.GONE:View.VISIBLE);
    }

}
