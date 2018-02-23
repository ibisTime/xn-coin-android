package com.cdkj.bcoin.deal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cdkj.baseim.activity.TxImLogingActivity;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baselibrary.activitys.AuthenticateActivity;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityDealBinding;
import com.cdkj.bcoin.databinding.DialogDealConfirmBinding;
import com.cdkj.bcoin.main.MainActivity;
import com.cdkj.bcoin.model.CoinModel;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.DealResultModel;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.model.SystemParameterModel;
import com.cdkj.bcoin.user.UserPersonActivity;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.EditTextJudgeNumberWatcher;
import com.cdkj.bcoin.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baseim.activity.TxImLogingActivity.DEAL;
import static com.cdkj.baselibrary.appmanager.MyConfig.CURRENCY;
import static com.cdkj.bcoin.util.AccountUtil.formatDouble;
import static com.cdkj.bcoin.util.DealUtil.YIFABU;
import static com.cdkj.bcoin.util.DealUtil.setDealPayType;

/**
 * Created by lei on 2017/10/30.
 */

public class DealActivity extends AbsBaseActivity {

    private ActivityDealBinding mBinding;

    private String code;
    private DealDetailModel bean;
    private String tradeString = StringUtil.getString(R.string.sale);

    private String inputType = "";

    public static void open(Context context, String code){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, DealActivity.class).putExtra("code",code));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_deal, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();
        getTradeRemind();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (code != null){
            getDeal();
        }
    }

    private void init() {
        if (getIntent() == null)
            return;

        code = getIntent().getStringExtra("code");

    }

    private void getDeal() {
        Map<String, String> map = new HashMap<>();
        map.put("adsCode", code);
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealDetail("625226", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealDetailModel>(this) {

            @Override
            protected void onSuccess(DealDetailModel data, String SucMessage) {
                if (data == null)
                    return;

                bean = data;

                setView();
                getAccount();
//                getHistory();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setView(){
        // 初始化交易币种
        mBinding.tvCoin.setText(bean.getTradeCoin());

        if (bean.getTradeType().equals("1")){ // 1是卖币，UI展示买币
            tradeString = getStrRes(R.string.buy);
        }else{ //反之
            tradeString = getStrRes(R.string.sale);

            mBinding.tvLeft.setVisibility(View.GONE);
            mBinding.tvBalance.setVisibility(View.VISIBLE);
        }

        // 设置标题和按钮内容
        setTopTitle(tradeString);
        mBinding.tvConfirm.setText(tradeString);
        mBinding.tvHowMany.setText(getStrRes(R.string.you_want)+tradeString+getStrRes(R.string.how_many));

        // 是否信任
        if (bean.getIsTrust() == 0){
            mBinding.btnTrust.setText(getStrRes(R.string.get_trust));
        }else {
            mBinding.btnTrust.setText(getStrRes(R.string.lost_trust));
        }

        // 填充数据
        mBinding.tvDeal.setText(bean.getUserStatistics().getJiaoYiCount()+"");
        mBinding.tvTrust.setText(bean.getUserStatistics().getBeiXinRenCount()+"");
        if(bean.getUserStatistics().getBeiPingJiaCount() == 0){
            mBinding.tvGood.setText("0%");
        }else {
            double hpRate = bean.getUserStatistics().getBeiHaoPingCount() / bean.getUserStatistics().getBeiPingJiaCount();
            mBinding.tvGood.setText(AccountUtil.formatInt(hpRate * 100)+"%");
        }

        setDealPayType(this, bean, mBinding.tvType);
        mBinding.tvAdv.setText(bean.getLeaveMessage());
        mBinding.tvPrice.setText(AccountUtil.formatDouble(bean.getTruePrice())+ CURRENCY);
        mBinding.tvLeft.setText(getStrRes(R.string.left_count) + AccountUtil.amountFormatUnit(new BigDecimal(bean.getLeftCountString()), bean.getTradeCoin(), 8));
        mBinding.tvLimit.setText(getStrRes(R.string.limit) + bean.getMinTrade()+"-"+formatDouble(bean.getMaxTrade())+CURRENCY);
        mBinding.edtCny.setHint(bean.getMinTrade()+"-"+formatDouble(bean.getMaxTrade())+CURRENCY);

        // 是否是自己的广告
        if (bean.getUser().getUserId().equals(SPUtilHelper.getUserId())){
            // 隐藏信任按钮
            mBinding.btnTrust.setVisibility(View.INVISIBLE);
            // 隐藏聊天按钮，设置操作按钮内容为编辑
            mBinding.llChat.setVisibility(View.INVISIBLE);
            mBinding.tvConfirm.setText("编辑");

            if (bean.getStatus().equals("2")){ // 已下架
                // 已下架时隐藏底部按钮
                mBinding.llBottom.setVisibility(View.GONE);

            }else {
                setSubRightTitleAndClick(getStrRes(R.string.out),v -> {
                    tip();
                });
            }

        }

        // 设置头像昵称
        if (bean.getUser() == null)
            return;
        mBinding.tvName.setText(bean.getUser().getNickname());
        ImgUtils.loadAvatar(this, bean.getUser().getPhoto(),
                bean.getUser().getNickname(), mBinding.ivAvatar, mBinding.tvAvatar);


        // 设置统计信息
        if (bean.getUserStatistics() == null)
            return;
        String amount;
        if (bean.getTradeCoin().equals("ETH")){
            amount = AccountUtil.amountFormatUnit(new BigDecimal(bean.getUserStatistics().getTotalTradeCountEth()),bean.getTradeCoin(), 8);
        }else {
            amount = AccountUtil.amountFormatUnit(new BigDecimal(bean.getUserStatistics().getTotalTradeCountSc()),bean.getTradeCoin(), 8);
        }

        double dh = Double.parseDouble(amount);

        if(dh == 0){
            mBinding.tvHistory.setText("0 "+bean.getTradeCoin());
        } else if (dh < 0.5){
            mBinding.tvHistory.setText("0-0.5 "+bean.getTradeCoin());
        }else if(0.5 <= dh && dh <= 1){
            mBinding.tvHistory.setText("0.5-1 "+bean.getTradeCoin());
        }else {
            mBinding.tvHistory.setText(amount.split("\\.")[0]+"+ "+bean.getTradeCoin());
        }

    }

    private void initListener() {
        mBinding.rlIcon.setOnClickListener(view -> {
            UserPersonActivity.open(this, bean.getUserId(),bean.getUser().getNickname(),bean.getUser().getPhoto());
        });

        mBinding.btnTrust.setOnClickListener(view -> trust());

        mBinding.tvConfirm.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            if (mBinding.tvConfirm.getText().equals("编辑")){

                if (bean.getTradeType().equals("1")){ // 卖币广告

                    SaleActivity.open(this, YIFABU, bean);

                }else { // 买币广告

                    PublishBuyActivity.open(this, YIFABU, bean);

                }

            }else { // 购买或者出售

                if (check()){

                    Double cny = Double.parseDouble(mBinding.edtCny.getText().toString());
                    if (bean.getMinTrade() <= cny && cny <= bean.getMaxTrade()){

                        // 购买数字货币之前需实名认证
                        if (tradeString.equals(getStrRes(R.string.buy))){
                            if (TextUtils.isEmpty(SPUtilHelper.getRealName())){
                                AuthenticateActivity.open(this);
                            }else {
                                popupType(view);
                            }
                        }else {
                            popupType(view);
                        }
                    }else {
                        showToast(getStrRes(R.string.trade_limit)+bean.getMinTrade()+"-"+formatDouble(bean.getMaxTrade())+CURRENCY);
                    }

                }
            }
        });

        mBinding.llChat.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            // 购买数字货币聊天之前需实名认证
            if (tradeString.equals(getStrRes(R.string.buy))){
                if (TextUtils.isEmpty(SPUtilHelper.getRealName())){
                    AuthenticateActivity.open(this);
                }else {
                    chatOrder();
                }
            }else {
                chatOrder();
            }

        });

        mBinding.edtCny.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputType.equals("edtCny")){

                    // 限制edtCny的 小数点前后输入位数
                    EditTextJudgeNumberWatcher.judgeNumber(editable, mBinding.edtCny, 10,2);

                    if (editable.toString().equals("")){
                        mBinding.edtCoin.setText("");
                    }else {
                        Double cny = Double.parseDouble(editable.toString());
                        Double price = bean.getTruePrice();

                        try {
                            mBinding.edtCoin.setText(AccountUtil.div(cny ,price, 8));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

        mBinding.edtCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputType.equals("edtCoin")){

                    EditTextJudgeNumberWatcher.judgeNumber(editable, mBinding.edtCoin, 10,8);

                    if (editable.toString().equals("")){
                        mBinding.edtCny.setText("");
                    }else {
                        Double coin = Double.parseDouble(editable.toString());

                        mBinding.edtCny.setText(AccountUtil.formatDouble(coin * bean.getTruePrice()));
                    }
                }

            }
        });

        mBinding.edtCny.setOnFocusChangeListener((view, b) -> {
            inputType = "edtCny";
        });

        mBinding.edtCoin.setOnFocusChangeListener((view, b) -> {
            inputType = "edtCoin";
        });
    }

    private boolean check(){

        if (mBinding.edtCny.getText().toString().equals("")){
            showToast(getStrRes(R.string.deal_buy_cny_hint));
            return false;
        }
        if (mBinding.edtCoin.getText().toString().equals("")){
            showToast(getStrRes(R.string.deal_buy_coin_hint));
            return false;
        }

        return true;
    }

    private void trust() {
        String code;
        Map<String, Object> map = new HashMap<>();
        map.put("toUser", bean.getUser().getUserId());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        if (mBinding.btnTrust.getText().equals(getStrRes(R.string.get_trust))){
            code = "805110";
        }else {
            code = "805111";
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest(code, StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess())
                    getDeal();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void popupType(View view) {
        DialogDealConfirmBinding popupBinding;

        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_deal_confirm, null, false);

        // 一个自定义的布局，作为显示的内容
        View mView = popupBinding.getRoot();

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor((v, event) -> {

            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });

        popupBinding.tvPopTip.setText(getStrRes(R.string.dialog_deal_tip1)+bean.getTradeCoin()+getStrRes(R.string.dialog_deal_tip2));

        popupBinding.tvPriceTitle.setText(tradeString+getStrRes(R.string.price));
        popupBinding.tvAmountTitle.setText(tradeString+getStrRes(R.string.amount));
        popupBinding.tvQuantityTitle.setText(tradeString+getStrRes(R.string.quantity));
        popupBinding.tvCancel.setText(getStrRes(R.string.cancel)+tradeString);
        popupBinding.tvConfirm.setText(getStrRes(R.string.confirm)+tradeString);

        popupBinding.tvPrice.setText(AccountUtil.formatDouble(bean.getTruePrice())+CURRENCY);
        popupBinding.tvAmount.setText(mBinding.edtCny.getText().toString().trim()+CURRENCY);
        popupBinding.tvQuantity.setText(mBinding.edtCoin.getText().toString().trim()+bean.getTradeCoin());

        popupBinding.tvCancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        popupBinding.tvConfirm.setOnClickListener(v -> {
            popupWindow.dismiss();
            if (tradeString.equals(getStrRes(R.string.buy))){
                buy();
            }else {
                sale();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void getAccount(){
        Map<String, Object> map = new HashMap<>();
        map.put("currency", bean.getTradeCoin());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {

            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {

                if (data == null)
                    return;

                for (CoinModel.AccountListBean model : data.getAccountList()){
                    if (model.getCurrency().equals(bean.getTradeCoin())){
                        BigDecimal amount = new BigDecimal(model.getAmountString());
                        BigDecimal frozenAmount = new BigDecimal(model.getFrozenAmountString());
                        mBinding.tvBalance.setText(getStrRes(R.string.deal_account_balance) +
                                AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), model.getCurrency(), 8));
                    }
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

//    private void getHistory(){
//        Map<String, Object> map = new HashMap<>();
//        map.put("currency", bean.getTradeCoin());
//        map.put("userId", bean.getUser().getUserId());
//        map.put("token", SPUtilHelper.getUserToken());
//
//        Call call = RetrofitUtils.createApi(MyApi.class).getDealHistory("625255", StringUtils.getJsonToString(map));
//
//        addCall(call);
//
//        call.enqueue(new BaseResponseModelCallBack<DealHistoryModel>(this) {
//
//            @Override
//            protected void onSuccess(DealHistoryModel data, String SucMessage) {
//
//                if (data == null)
//                    return;
//
//                double dh = Double.parseDouble(AccountUtil.amountFormatUnit(new BigDecimal(data.getTotalTradeCount()),bean.getTradeCoin(), 8));
//
//                if(dh == 0){
//                    mBinding.tvHistory.setText("0 "+bean.getTradeCoin());
//                } else if (dh < 0.5){
//                    mBinding.tvHistory.setText("0-0.5 "+bean.getTradeCoin());
//                }else if(0.5 <= dh && dh <= 1){
//                    mBinding.tvHistory.setText("0.5-1 "+bean.getTradeCoin());
//                }else {
//                    mBinding.tvHistory.setText(AccountUtil.amountFormatUnit(new BigDecimal(data.getTotalTradeCount()),bean.getTradeCoin(), 8).split("\\.")[0]+"+ "+bean.getTradeCoin());
//                }
//
//            }
//
//            @Override
//            protected void onFinish() {
//                disMissLoading();
//            }
//        });
//    }

    private void buy(){
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtCoin.getText().toString().trim());

        Map<String, Object> map = new HashMap<>();
        map.put("adsCode", bean.getCode());
        map.put("buyUser", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        if (bean.getTradeCoin().equals("ETH")){
            map.put("count", bigDecimal.multiply(AccountUtil.UNIT_ETH).toString().split("\\.")[0]);
        }else {
            map.put("count", bigDecimal.multiply(AccountUtil.UNIT_SC).toString().split("\\.")[0]);
        }
        map.put("tradeAmount", mBinding.edtCny.getText().toString().trim());
        map.put("tradePrice", bean.getTruePrice());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealResult("625240", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealResultModel>(this) {

            @Override
            protected void onSuccess(DealResultModel model, String SucMessage) {
                showToast(getStrRes(R.string.buy_success));
                finish();

                // 购买成功后跳到订单
                EventBusModel eventBusModel = new EventBusModel();
                eventBusModel.setEvInt(MainActivity.ORDER);
                eventBusModel.setEvInfo(bean.getTradeCoin());
                eventBusModel.setTag(EventTags.MAIN_CHANGE_SHOW_INDEX);
                EventBus.getDefault().post(eventBusModel);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void sale(){
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtCoin.getText().toString().trim());

        Map<String, Object> map = new HashMap<>();
        map.put("adsCode", bean.getCode());
        map.put("sellUser", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        if (bean.getTradeCoin().equals("ETH")){
            map.put("count", bigDecimal.multiply(AccountUtil.UNIT_ETH).toString().split("\\.")[0]);
        }else {
            map.put("count", bigDecimal.multiply(AccountUtil.UNIT_SC).toString().split("\\.")[0]);
        }
        map.put("tradeAmount", mBinding.edtCny.getText().toString().trim());
        map.put("tradePrice", bean.getTruePrice());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealResult("625241", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealResultModel>(this) {

                @Override
                protected void onSuccess(DealResultModel model, String SucMessage) {
                    showToast(getStrRes(R.string.sale_success));
                    finish();

                    // 出售成功后跳到订单
                    EventBusModel eventBusModel = new EventBusModel();
                    eventBusModel.setEvInt(MainActivity.ORDER);
                    eventBusModel.setEvInfo(bean.getTradeCoin());
                    eventBusModel.setTag(EventTags.MAIN_CHANGE_SHOW_INDEX);
                    EventBus.getDefault().post(eventBusModel);
                }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle(getStrRes(R.string.attention))
                .setMessage(getStrRes(R.string.out_confirm))
                .setPositiveButton(getStrRes(R.string.confirm), (dialogInterface, i) -> {
                    out();
                }).setNegativeButton(getStrRes(R.string.cancel), null).show();
    }

    private void out(){
        Map<String, Object> map = new HashMap<>();
        map.put("adsCode", bean.getCode());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealResult("625224", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealResultModel>(this) {

            @Override
            protected void onSuccess(DealResultModel model, String SucMessage) {
                showToast(getStrRes(R.string.outed));
                finish();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 聊天时生成一个待交易的订单作为聊天入口
     */
    private void chatOrder(){
        String code;

        Map<String, Object> map = new HashMap<>();
        map.put("adsCode", bean.getCode());
        map.put("token", SPUtilHelper.getUserToken());

        if (bean.getTradeType().equals("1")){ // 1是卖币，UI展示买币
            code = "625247";
            map.put("buyUser", SPUtilHelper.getUserId());
        }else { //反之
            code = "625248";
            map.put("sellUser", SPUtilHelper.getUserId());
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest(code, StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes model, String SucMessage) {

                if (model == null)
                    return;

                ImUserInfo info = new ImUserInfo();
                info.setLeftImg(bean.getUser().getPhoto());
                info.setLeftName(bean.getUser().getNickname());
                info.setRightImg(SPUtilHelper.getUserPhoto());
                info.setRightName(SPUtilHelper.getUserName());

                info.setIdentify(model.getCode());

                TxImLogingActivity.open(DealActivity.this, info,false,true, DEAL);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Subscribe
    public void openDealChatActivity(ImUserInfo imUserInfo){

        if (imUserInfo.getEventTag().equals(DEAL)){

            // 构建一个订单详情类
            OrderDetailModel orderDetailModel = new OrderDetailModel();
            orderDetailModel.setAdsCode(code);

            DealChatActivity.open(this, orderDetailModel, imUserInfo);
        }
    }

    /**
     * 获取交易提醒
     * @return
     */
    private void getTradeRemind() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "trade_remind");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.tvTip.setText(data.getCvalue()+"");

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}
