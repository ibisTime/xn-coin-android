package com.cdkj.ethereumdemo.deal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cdkj.baseim.activity.TxImLogingActivity;
import com.cdkj.baseim.model.ImUserInfo;
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
import com.cdkj.ethereumdemo.main.MainActivity;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.api.MyApi;
import com.cdkj.ethereumdemo.databinding.ActivityDealBinding;
import com.cdkj.ethereumdemo.databinding.DialogDealConfirmBinding;
import com.cdkj.ethereumdemo.model.CoinModel;
import com.cdkj.ethereumdemo.model.DealDetailModel;
import com.cdkj.ethereumdemo.model.DealHistoryModel;
import com.cdkj.ethereumdemo.model.DealResultModel;
import com.cdkj.ethereumdemo.model.SystemParameterModel;
import com.cdkj.ethereumdemo.user.UserPersonActivity;
import com.cdkj.ethereumdemo.util.AccountUtil;
import com.cdkj.ethereumdemo.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baseim.activity.TxImLogingActivity.DEAL;
import static com.cdkj.ethereumdemo.util.DealUtil.setBgRes;

/**
 * Created by lei on 2017/10/30.
 */

public class DealActivity extends AbsBaseActivity {

    private ActivityDealBinding mBinding;

    private String code;
    private DealDetailModel bean;
    private String tradeString = StringUtil.getStirng(R.string.sale);

    private String inputType = "";

    // 未支付订单
    private String chatOrderCde;

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

    private void init() {
        if (getIntent() == null)
            return;

        code = getIntent().getStringExtra("code");

        getDeal();

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
                getHistory();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setView(){

        if (bean.getUser().getUserId().equals(SPUtilHelper.getUserId())){
            mBinding.btnTrust.setVisibility(View.GONE);
            mBinding.llBottom.setVisibility(View.GONE);

            if (!bean.getStatus().equals("3")){ // 已下架
                setSubRightTitleAndClick(StringUtil.getStirng(R.string.out),v -> {
                    tip();
                });
            }

        }

        if (bean.getTradeType().equals("1")){ // 1是卖币，UI展示买币
            tradeString = StringUtil.getStirng(R.string.buy);
        }else{ //反之
            tradeString = StringUtil.getStirng(R.string.sale);

            mBinding.tvLeft.setVisibility(View.GONE);
            mBinding.tvBalance.setVisibility(View.VISIBLE);
        }
        //
        setTopTitle(tradeString);
        mBinding.tvConfirm.setText(tradeString);
        mBinding.tvHowMany.setText(StringUtil.getStirng(R.string.you_want)+tradeString+StringUtil.getStirng(R.string.how_many));

        if (bean.getIsTrust() == 0){
            mBinding.btnTrust.setText(StringUtil.getStirng(R.string.get_trust));
        }else {
            mBinding.btnTrust.setText(StringUtil.getStirng(R.string.lost_trust));
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

        mBinding.ivPayType.setBackgroundResource(setBgRes(bean));
        mBinding.tvAdv.setText(bean.getLeaveMessage());
        mBinding.tvPrice.setText(AccountUtil.formatDouble(bean.getTruePrice())+"CNY");
        mBinding.tvLeft.setText(StringUtil.getStirng(R.string.left_count) + AccountUtil.weiToEth(new BigDecimal(bean.getLeftCountString())));
        mBinding.tvLimit.setText(StringUtil.getStirng(R.string.limit) + bean.getMinTrade()+"-"+bean.getMaxTrade()+"CNY");
        mBinding.edtCny.setHint(bean.getMinTrade()+"-"+bean.getMaxTrade()+"CNY");

        if (bean.getUser() == null)
            return;
        mBinding.tvName.setText(bean.getUser().getNickname());
        ImgUtils.loadAvatar(this, bean.getUser().getPhoto(),
                bean.getUser().getNickname(), mBinding.ivAvatar, mBinding.tvAvatar);

    }

    private void initListener() {
        mBinding.rlIcon.setOnClickListener(view -> {
            UserPersonActivity.open(this, bean.getUserId());
        });

        mBinding.btnTrust.setOnClickListener(view -> trust());

        mBinding.tvConfirm.setOnClickListener(view -> {
            if (mBinding.edtCny.getText().toString().equals("")){
               showToast(StringUtil.getStirng(R.string.deal_buy_cny_hint));
               return;
            }
            if (mBinding.edtCoin.getText().toString().equals("")){
                showToast(StringUtil.getStirng(R.string.deal_buy_coin_hint));
                return;
            }

            Double cny = Double.parseDouble(mBinding.edtCny.getText().toString());
            if (bean.getMinTrade() <= cny && cny <= bean.getMaxTrade()){
                popupType(view);
            }else {
                showToast(StringUtil.getStirng(R.string.trade_limit)+bean.getMinTrade()+"-"+bean.getMaxTrade()+"CNY");
            }

        });

        mBinding.llChat.setOnClickListener(view -> {
            chatOrder();
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
                    if (editable.toString().equals("")){
                        mBinding.edtCny.setText("");
                    }else {
//                        BigDecimal coin = new BigDecimal(editable.toString());
//                        BigDecimal price = new BigDecimal(bean.getTruePrice());
                        Double coin = Double.parseDouble(editable.toString());

                        mBinding.edtCny.setText(AccountUtil.formatDouble(coin * bean.getTruePrice()));
                    }
                }

            }
        });

        mBinding.edtCny.setOnFocusChangeListener((view, b) -> {
            Log.e("edtCny",b+"");
            inputType = "edtCny";
        });

        mBinding.edtCoin.setOnFocusChangeListener((view, b) -> {
            Log.e("edtCoin",b+"");
            inputType = "edtCoin";
        });
    }

    private void trust() {
        String code;
        Map<String, Object> map = new HashMap<>();
        map.put("toUser", bean.getUser().getUserId());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        if (mBinding.btnTrust.getText().equals(StringUtil.getStirng(R.string.get_trust))){
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

        popupBinding.tvPriceTitle.setText(tradeString+StringUtil.getStirng(R.string.price));
        popupBinding.tvAmountTitle.setText(tradeString+StringUtil.getStirng(R.string.amount));
        popupBinding.tvQuantityTitle.setText(tradeString+StringUtil.getStirng(R.string.quantity));
        popupBinding.tvCancel.setText(StringUtil.getStirng(R.string.cancel)+tradeString);
        popupBinding.tvConfirm.setText(StringUtil.getStirng(R.string.confirm)+tradeString);

        popupBinding.tvPrice.setText(AccountUtil.formatDouble(bean.getTruePrice())+"CNY");
        popupBinding.tvAmount.setText(mBinding.edtCny.getText().toString().trim()+"CNY");
        popupBinding.tvQuantity.setText(mBinding.edtCoin.getText().toString().trim()+bean.getTradeCoin());

        popupBinding.tvCancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        popupBinding.tvConfirm.setOnClickListener(v -> {
            popupWindow.dismiss();
            if (tradeString.equals(StringUtil.getStirng(R.string.buy))){
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
                        mBinding.tvBalance.setText(StringUtil.getStirng(R.string.deal_account_balance) +
                                AccountUtil.sub(Double.parseDouble(model.getAmountString()),
                                        Double.parseDouble(model.getFrozenAmountString())));
                    }
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void getHistory(){
        Map<String, Object> map = new HashMap<>();
        map.put("userId", bean.getUser().getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealHistory("625255", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<DealHistoryModel>(this) {

            @Override
            protected void onSuccess(DealHistoryModel data, String SucMessage) {

                if (data == null)
                    return;

                double dh = Double.parseDouble(AccountUtil.weiToEth(new BigDecimal(data.getTotalTradeCount())));

                if(dh == 0){
                    mBinding.tvHistory.setText("0 ETH");
                } else if (dh < 0.5){
                    mBinding.tvHistory.setText("0-0.5 ETH");
                }else if(0.5 <= dh && dh <= 1){
                    mBinding.tvHistory.setText("0.5-1 ETH");
                }else {
                    mBinding.tvHistory.setText(AccountUtil.weiToEth(new BigDecimal(data.getTotalTradeCount())).split("\\.")[0]+"+ ETH");
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void buy(){
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtCoin.getText().toString().trim());

        Map<String, Object> map = new HashMap<>();
        map.put("adsCode", bean.getCode());
        map.put("buyUser", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("count", bigDecimal.multiply(AccountUtil.UNIT).toString().split("\\.")[0]);
        map.put("tradeAmount", mBinding.edtCny.getText().toString().trim());
        map.put("tradePrice", bean.getTruePrice());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealResult("625240", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealResultModel>(this) {

            @Override
            protected void onSuccess(DealResultModel model, String SucMessage) {
                showToast(StringUtil.getStirng(R.string.buy_success));
                finish();
                EventBusModel eventBusModel = new EventBusModel();
                eventBusModel.setEvInt(MainActivity.ORDER); //显示认证界面
                eventBusModel.setTag(EventTags.MAINCHANGESHOWINDEX);
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
        map.put("count", bigDecimal.multiply(AccountUtil.UNIT).toString().split("\\.")[0]);
        map.put("tradeAmount", mBinding.edtCny.getText().toString().trim());
        map.put("tradePrice", bean.getTruePrice());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealResult("625241", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealResultModel>(this) {

                @Override
                protected void onSuccess(DealResultModel model, String SucMessage) {
                    showToast(StringUtil.getStirng(R.string.sale_success));
                    finish();
                    EventBusModel eventBusModel = new EventBusModel();
                    eventBusModel.setEvInt(MainActivity.ORDER); //显示认证界面
                    eventBusModel.setTag(EventTags.MAINCHANGESHOWINDEX);
                    EventBus.getDefault().post(eventBusModel);
                }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle(StringUtil.getStirng(R.string.attention))
                .setMessage(StringUtil.getStirng(R.string.out_confirm))
                .setPositiveButton(StringUtil.getStirng(R.string.confirm), (dialogInterface, i) -> {
                    out();
                }).setNegativeButton(StringUtil.getStirng(R.string.cancel), null).show();
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
                showToast(StringUtil.getStirng(R.string.outed));
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
        if (bean.getTradeType().equals("1")){ // 1是卖币，UI展示买币
            code = "625247";
            map.put("buyUser", SPUtilHelper.getUserId());
        }else { //反之
            code= "625248";
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

                chatOrderCde = model.getCode();

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
            DealChatActivity.open(this, chatOrderCde, imUserInfo);
        }
    }

    /**
     * 获取交易提醒
     * @return
     */
    private void getTradeRemind() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "trade_remind");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("625917", StringUtils.getJsonToString(map));

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
