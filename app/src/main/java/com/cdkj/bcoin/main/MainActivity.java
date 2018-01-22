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
import com.cdkj.baseim.event.GroupEvent;
import com.cdkj.baseim.event.MessageEvent;
import com.cdkj.baseim.event.RefreshEvent;
import com.cdkj.baseim.interfaces.TxImLoginInterface;
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
import com.cdkj.bcoin.order.OrderFragment;
import com.cdkj.bcoin.user.UserFragment;
import com.cdkj.bcoin.user.login.SignInActivity;
import com.cdkj.bcoin.util.StringUtil;
import com.cdkj.bcoin.wallet.WalletFragment;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_DONE;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_NEW;
import static com.cdkj.baselibrary.appmanager.EventTags.MAINCHANGESHOWINDEX;
import static com.cdkj.bcoin.util.UpdateUtil.startWeb;
import static com.cdkj.bcoin.util.ZenDeskUtil.initZenDeskIdentity;

@Route(path = "/main/page")
public class MainActivity extends AbsBaseActivity implements TxImLoginInterface {

    private PublishWindow mPublishWindow;
    private ActivityMainBinding mBinding;

    private TxImLoginPresenter mPresenter;

    public static final int MARKET = 0;
    public static final int ORDER = 1;
    public static final int DEAL = 2;
    public static final int WALLET = 3;
    public static final int MY = 4;
    private List<Fragment> fragments;

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

        getVersion();
//        groupEvent();


        if(!SPUtilHelper.getUserId().equals("")){
            getUserData();
            updateOnLineTime();

//            initTencent();
            initZenDeskIdentity(SPUtilHelper.getUserName(), SPUtilHelper.getUserEmail());

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 设置消息接收在后台
        PushUtil.getInstance().reset();

        if(!SPUtilHelper.getUserId().equals("")){
            // 设置腾讯云的昵称
            TXImManager.getInstance().setUserNickName(SPUtilHelper.getUserName(), new TXImManager.changeInfoBallBack() {
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

    private void initTencent() {
        // 登录腾讯云
        mPresenter = new TxImLoginPresenter(this);
        mPresenter.login();
    }

    private void init() {
        setShowIndex(DEAL);
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

        mBinding.layoutMainBottom.llOrder.setOnClickListener(v -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            setShowIndex(ORDER);

        });

        mBinding.layoutMainBottom.llDeal.setOnClickListener(v -> {
            setShowIndex(DEAL);

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
            case ORDER:
                mBinding.ivPublish.setVisibility(View.GONE);
                mBinding.layoutMainBottom.ivOrder.setImageResource(R.mipmap.main_order_light);
                mBinding.layoutMainBottom.tvOrder.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case DEAL:
                mBinding.ivPublish.setVisibility(View.VISIBLE);
                mBinding.layoutMainBottom.ivDeal.setImageResource(R.mipmap.main_deal_light);
                mBinding.layoutMainBottom.tvDeal.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
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

        mBinding.layoutMainBottom.ivOrder.setImageResource(R.mipmap.main_order_dark);
        mBinding.layoutMainBottom.tvOrder.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivDeal.setImageResource(R.mipmap.main_deal_dark);
        mBinding.layoutMainBottom.tvDeal.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

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
        fragments.add(OrderFragment.getInstance());
        fragments.add(DealFragment.getInstance());
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

    @Subscribe
    public void MainEventBus(EventBusModel eventBusModel) {
        if (eventBusModel == null) {
            return;
        }

        if (TextUtils.equals(eventBusModel.getTag(), MAINCHANGESHOWINDEX)) {
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

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("625918", StringUtils.getJsonToString(map));

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

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess() {

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

    /**
     * 设置刷新腾讯云群组监听
     */
    public void groupEvent(){
        TIMUserConfig userConfig = new TIMUserConfig()
                .setGroupEventListener(timGroupTipsElem -> Log.e("onGroupTipsEven1t", "you have new system msg!"))
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        Log.i("onForceOffline", "onForceOffline");
                        showToast("该账号在其他设备登录，请重新登录");
                        SPUtilHelper.logOutClear();
                        EventBus.getDefault().post(EventTags.AllFINISH);

                        SignInActivity.open(MainActivity.this,true);
                        finish();
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
//                        Log.i(tag, "onUserSigExpired");
                    }
                });

        RefreshEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);

    }
}
