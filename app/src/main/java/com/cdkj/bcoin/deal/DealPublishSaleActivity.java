package com.cdkj.bcoin.deal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.MyPickerPopupWindow;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityDealPublishSaleBinding;
import com.cdkj.bcoin.model.CoinModel;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.MarketCoinModel;
import com.cdkj.bcoin.model.SystemParameterListModel;
import com.cdkj.bcoin.model.SystemParameterModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.CoinUtil;
import com.cdkj.bcoin.util.EditTextJudgeNumberWatcher;
import com.cdkj.bcoin.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.bcoin.util.AccountUtil.getUnit;
import static com.cdkj.bcoin.util.DealUtil.CAOGAO;
import static com.cdkj.bcoin.util.DealUtil.DAIFABU;
import static com.cdkj.bcoin.util.DealUtil.YIFABU;
import static com.cdkj.bcoin.util.DealUtil.getPayType;

/**
 * Created by lei on 2017/10/29.
 */

public class DealPublishSaleActivity extends AbsBaseActivity {

    private ActivityDealPublishSaleBinding mBinding;

    // 币种
    private String coinType;
    private double marketPrice;

    // 开始时间TextViewList
    private List<TextView> startTimeList;
    // 结束时间TextViewList
    private List<TextView> endTimeList;

    // hour
    private String startHour = "00:00";
    private String[] startHours;
    // minute
    private String endHour = "00:00";
    private String[] endHours;


    // 付款方式
    private String type = "0";
    private String[] types;
    private String[] typeValue = {"0", "1", "2"};

    // 付款时限
    private String limit = "";
    private String[] limits;

    // 高级设置
    private boolean settingSwitch = false;

    // 高级设置
    private boolean openTimeFlag = false;

    // 仅粉丝
    private String onlyFans = "0";

    // 广告详情
    private DealDetailModel bean;
    // 当前广告状态
    private String status;

    private SystemParameterListModel model;

    public static void open(Context context, String status, DealDetailModel bean){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, DealPublishSaleActivity.class).putExtra("status",status).putExtra("bean",bean));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_deal_publish_sale, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initHour();
        initListener();

        getLimit();
        getListData();

    }

    private void init() {
        types = new String[]{getStrRes(R.string.zhifubao), getStrRes(R.string.weixin), getStrRes(R.string.card)};

        if (getIntent() != null){
            status = getIntent().getStringExtra("status");
            bean = (DealDetailModel) getIntent().getSerializableExtra("bean");
        }


        switch (status){ // "1", "直接发布" "2", "草稿发布" "3", "编辑发布，原广告下

            case DAIFABU:
                setTopTitle(StringUtil.getString(R.string.deal_publish_sale));
                setSubRightTitleAndClick(StringUtil.getString(R.string.deal_publish_save), v -> {
                    if (check()){
                        sale("0");
                    }
                });
                break;

            case CAOGAO:
                setTopTitle(StringUtil.getString(R.string.deal_publish_sale));
                setSubRightTitHide();
                break;

            case YIFABU:
                setTopTitle(StringUtil.getString(R.string.deal_publish_edit));
                setSubRightTitHide();
                break;
        }

        //
        if (bean != null){
            // 初始化默认币种
            coinType = bean.getTradeCoin();
            // 设置币种
            setCoinCurrency();
            // 获取币种行情价格
            getMarketPrice(true);
        }else {
            // 初始化默认币种
            coinType = CoinUtil.getNotTokenCoinArray()[0];
            // 设置币种
            setCoinCurrency();
            // 获取币种行情价格
            getMarketPrice(false);
        }
    }

    private void getMarketPrice(boolean isSetView) {
        Map<String, Object> map = new HashMap<>();
        map.put("coin", coinType);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getTruePrice("625292", StringUtils.getJsonToString(map));

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MarketCoinModel>(this) {

            @Override
            protected void onSuccess(MarketCoinModel data, String SucMessage) {
                if (data == null)
                    return;

                // 保存行情价格
                marketPrice = data.getMid();
                // 设置行情价格
                mBinding.tvMarketPrice.setText(getStrRes(R.string.deal_market_price)+AccountUtil.formatDouble(marketPrice));

                // 根据已输入的溢价行情计算价格
                if (mBinding.edtPremium.getText().toString().equals("")){
                    mBinding.tvPrice.setText(AccountUtil.formatDouble(marketPrice));
                } else if (mBinding.edtPremium.getText().toString().equals("-") || mBinding.edtPremium.getText().toString().equals(".")){
                    return;
                } else {
                    Double price = Double.parseDouble(AccountUtil.formatDouble(marketPrice)+"");
                    Double premiumRate = Double.parseDouble(mBinding.edtPremium.getText().toString());

                    try {
                        mBinding.tvPrice.setText(AccountUtil.formatDouble(price + (price * premiumRate /100)));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                // 获取账户余额
                getAccount();

                if (isSetView)
                    setView();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void setCoinCurrency() {
        // 设置选择币
        mBinding.tvCoinSelect.setText(coinType);
        mBinding.tvAmountCoin.setText(coinType);

    }

    private void initHour() {

        int i = 24;
        startHours = new String[25];
        for (int j=0; j<i; j++){
            if (j < 10){
                startHours[j] = "0"+j+":00";
            }else {
                startHours[j] = j+":00";
            }
        }
        startHours[startHours.length-1] = StringUtil.getString(R.string.deal_open_time_close);

        endHours = new String[25];
        endHours[0] = "23:59";
        endHours[1] = StringUtil.getString(R.string.deal_open_time_close);
        for (int j=2; j<=i; j++){
            if (j < 10){
                endHours[j] = "0"+(j-1)+":00";
            }else {
                endHours[j] = (j-1)+":00";
            }
        }

        startTimeList = new ArrayList<>();
        startTimeList.add(mBinding.llOpenTime.tvStart1);
        startTimeList.add(mBinding.llOpenTime.tvStart2);
        startTimeList.add(mBinding.llOpenTime.tvStart3);
        startTimeList.add(mBinding.llOpenTime.tvStart4);
        startTimeList.add(mBinding.llOpenTime.tvStart5);
        startTimeList.add(mBinding.llOpenTime.tvStart6);
        startTimeList.add(mBinding.llOpenTime.tvStart7);

        endTimeList = new ArrayList<>();
        endTimeList.add(mBinding.llOpenTime.tvEnd1);
        endTimeList.add(mBinding.llOpenTime.tvEnd2);
        endTimeList.add(mBinding.llOpenTime.tvEnd3);
        endTimeList.add(mBinding.llOpenTime.tvEnd4);
        endTimeList.add(mBinding.llOpenTime.tvEnd5);
        endTimeList.add(mBinding.llOpenTime.tvEnd6);
        endTimeList.add(mBinding.llOpenTime.tvEnd7);
    }

    private void initListener() {

        mBinding.llCoinSelect.setOnClickListener(view -> {
            // 新发布广告时可以更改币种
            if (bean == null){
                initPopup(view);
            }
        });

        mBinding.llAnytime.setOnClickListener(view -> {
            openTimeFlag = false;

            mBinding.ivAnytime.setBackgroundResource(R.mipmap.deal_choose);
            mBinding.ivCustom.setBackgroundResource(R.mipmap.deal_unchoose);
            mBinding.llOpenTime.llOpenTime.setVisibility(View.GONE);
        });

        mBinding.llCustom.setOnClickListener(view -> {
            openTimeFlag = true;

            mBinding.ivCustom.setBackgroundResource(R.mipmap.deal_choose);
            mBinding.ivAnytime.setBackgroundResource(R.mipmap.deal_unchoose);
            mBinding.llOpenTime.llOpenTime.setVisibility(View.VISIBLE);
        });

        mBinding.llOpenTime.llOt1.setOnClickListener(view -> {
            initCustomPopup(view,1);
        });

        mBinding.llOpenTime.llOt2.setOnClickListener(view -> {
            initCustomPopup(view,2);
        });

        mBinding.llOpenTime.llOt3.setOnClickListener(view -> {
            initCustomPopup(view,3);
        });

        mBinding.llOpenTime.llOt4.setOnClickListener(view -> {
            initCustomPopup(view,4);
        });

        mBinding.llOpenTime.llOt5.setOnClickListener(view -> {
            initCustomPopup(view,5);
        });

        mBinding.llOpenTime.llOt6.setOnClickListener(view -> {
            initCustomPopup(view,6);
        });

        mBinding.llOpenTime.llOt7.setOnClickListener(view -> {
            initCustomPopup(view,7);
        });

        mBinding.llSelect.setOnClickListener(this::initPayTypePopup);

        mBinding.llLimit.setOnClickListener(this::initLimitPopup);

        mBinding.llTick.setOnClickListener(view -> {
            if (onlyFans.equals("0")){
                mBinding.ivTick.setBackgroundResource(R.mipmap.deal_tick);
                onlyFans = "1";
            }else {
                mBinding.ivTick.setBackgroundResource(R.mipmap.deal_unchoose);
                onlyFans = "0";
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check()){
                switch (status){ // "1", "直接发布" "2", "草稿发布" "3", "编辑发布，原广告下

                    case DAIFABU:
                        sale("1");
                        break;

                    case CAOGAO:
                        sale("2");
                        break;

                    case YIFABU:
                        sale("3");
                        break;
                }
            }
        });

        mBinding.llSettingSwitch.setOnClickListener(view -> {
            if (settingSwitch){
                settingSwitch = false;
                mBinding.ivSetting.setBackgroundResource(R.mipmap.more);
            }else {
                settingSwitch = true;
                mBinding.ivSetting.setBackgroundResource(R.mipmap.deal_down);
            }
            mBinding.llSetting.setVisibility(settingSwitch ? View.VISIBLE : View.GONE);

        });

        mBinding.edtPremium.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")){
                    mBinding.tvPrice.setText(AccountUtil.formatDouble(marketPrice));
                    showToast("请输入溢价");
                } else if (editable.toString().equals("-") || editable.toString().equals(".")){
                    return;
                } else {
                    Double price = Double.parseDouble(AccountUtil.formatDouble(marketPrice));
                    Double premiumRate = Double.parseDouble(editable.toString());

                    try {
                        mBinding.tvPrice.setText(AccountUtil.formatDouble(price + (price * premiumRate /100)));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        // 说明
        mBinding.llCoin.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llPrice.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llPremiumRate.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llProtectPrice.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llMaxTrade.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llMinTrade.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llTotalCount.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llPayType.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llPayLimit.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llDisplayTime.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        mBinding.llTrust.setOnClickListener(view -> {
            getFieldExplain(view.getTag().toString());
        });

        // 限制EditText的小数点前后输入位数
        mBinding.edtPremium.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtPremium,8,2));
        mBinding.edtProtectPrice.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtProtectPrice,8,2));
        mBinding.edtMax.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtMax,8,2));
        mBinding.edtMin.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtMin,8 ,2));
        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount,8,8));

    }


    /**
     * 选择自定义时间
     * @param view
     */
    private void initCustomPopup(View view, int location) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.dialog_deal_time);
        popupWindow.setNumberPicker(R.id.np_start_hour, startHours);
        popupWindow.setNumberPicker(R.id.np_end_hour, endHours);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {

            startHour = startHours[popupWindow.getNumberPickerValue(R.id.np_start_hour)];
            endHour = endHours[popupWindow.getNumberPickerValue(R.id.np_end_hour)];

            if(startHour.equals(getStrRes(R.string.deal_open_time_close)) && !endHour.equals(getStrRes(R.string.deal_open_time_close)) ){
                showToast(getStrRes(R.string.deal_publish_hint_end));
                return;
            }
            if (!startHour.equals(getStrRes(R.string.deal_open_time_close)) && endHour.equals(getStrRes(R.string.deal_open_time_close))){
                showToast(getStrRes(R.string.deal_publish_hint_start));
                return;
            }

            switch (location){

                case 1:
                    mBinding.llOpenTime.tvStart1.setText(startHour);
                    mBinding.llOpenTime.tvEnd1.setText(endHour);
                    break;

                case 2:
                    mBinding.llOpenTime.tvStart2.setText(startHour);
                    mBinding.llOpenTime.tvEnd2.setText(endHour);
                    break;

                case 3:
                    mBinding.llOpenTime.tvStart3.setText(startHour);
                    mBinding.llOpenTime.tvEnd3.setText(endHour);
                    break;

                case 4:
                    mBinding.llOpenTime.tvStart4.setText(startHour);
                    mBinding.llOpenTime.tvEnd4.setText(endHour);

                    break;
                case 5:
                    mBinding.llOpenTime.tvStart5.setText(startHour);
                    mBinding.llOpenTime.tvEnd5.setText(endHour);
                    break;

                case 6:
                    mBinding.llOpenTime.tvStart6.setText(startHour);
                    mBinding.llOpenTime.tvEnd6.setText(endHour);
                    break;

                case 7:
                    mBinding.llOpenTime.tvStart7.setText(startHour);
                    mBinding.llOpenTime.tvEnd7.setText(endHour);
                    break;

            }

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }

    /**
     * 选择币种
     * @param view
     */
    private void initPopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, CoinUtil.getNotTokenCoinArray());

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            coinType = popupWindow.getNumberPicker(R.id.np_type, CoinUtil.getNotTokenCoinArray());

            // 重置价格
            mBinding.tvPrice.setText("");
            mBinding.tvMarketPrice.setText(getStrRes(R.string.deal_market_price)+"");
            // 设置币种
            setCoinCurrency();
            // 获取所选币种行情价格
            getMarketPrice(false);

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }

    /**
     * 选择支付方式
     * @param view
     */
    private void initPayTypePopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, types);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            type = popupWindow.getNumberPicker(R.id.np_type, typeValue);

            switch (type){
                case "0":
                    mBinding.tvWay.setText(getStrRes(R.string.zhifubao));
                    break;

                case "1":
                    mBinding.tvWay.setText(getStrRes(R.string.weixin));
                    break;

                case "2":
                    mBinding.tvWay.setText(getStrRes(R.string.card));
                    break;
            }

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }


    /**
     * 获取广告详情
     * @return
     */
    private void getDeal() {
        Map<String, String> map = new HashMap<>();
        map.put("adsCode", bean.getCode());
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
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setView() {
        mBinding.edtPremium.setText(AccountUtil.formatDouble(bean.getPremiumRate() * 100)+"");
        mBinding.edtMin.setText(AccountUtil.formatDouble(bean.getMinTrade()));
        mBinding.edtMax.setText(AccountUtil.formatDouble(bean.getMaxTrade()));
        mBinding.edtProtectPrice.setText(AccountUtil.formatDouble(bean.getProtectPrice()));
        mBinding.edtAmount.setText(AccountUtil.amountFormatUnit(new BigDecimal(bean.getLeftCountString()), bean.getTradeCoin(), 8));
        mBinding.tvWay.setText(types[Integer.parseInt(bean.getPayType())]);
        mBinding.tvLimit.setText(bean.getPayLimit()+"");
        mBinding.edtRemark.setText(bean.getLeaveMessage());

        if (bean.getOnlyTrust().equals("1")){
            mBinding.ivTick.setBackgroundResource(R.mipmap.deal_tick);
            onlyFans = "1";
        }else {
            mBinding.ivTick.setBackgroundResource(R.mipmap.deal_unchoose);
            onlyFans = "0";
        }

        if (bean.getDisplayTime() != null){
            if (bean.getDisplayTime().size() == 0)
                return;

            if (bean.getDisplayTime().size() >7)
                return;

            mBinding.ivCustom.setBackgroundResource(R.mipmap.deal_choose);
            mBinding.ivAnytime.setBackgroundResource(R.mipmap.deal_unchoose);
            mBinding.llOpenTime.llOpenTime.setVisibility(View.VISIBLE);

            for (int i =0; i<bean.getDisplayTime().size(); i++){

                int week = Integer.parseInt(bean.getDisplayTime().get(i).getWeek());
                if (week < 1)
                    return;

                startTimeList.get(week - 1).setText(formatOpenStartTime(bean.getDisplayTime().get(i).getStartTime(),
                        bean.getDisplayTime().get(i).getEndTime()));
                endTimeList.get(week -1 ).setText(formatOpenEndTime(bean.getDisplayTime().get(i).getStartTime(),
                        bean.getDisplayTime().get(i).getEndTime()));

            }
        }
    }

    private String formatOpenStartTime(int startTime, int endTime){
        if (startTime == endTime && startTime == 24){
            return StringUtil.getString(R.string.deal_open_time_close);

        }else {
            if (startTime < 10){
                return "0"+startTime+":00";
            }else {
                if (startTime == 24){
                    return "23:59";
                }else {
                    return startTime+":00";
                }
            }
        }
    }

    private String formatOpenEndTime(int startTime, int endTime){
        if (startTime == endTime && endTime == 24){
            return StringUtil.getString(R.string.deal_open_time_close);
        }else {
            if (endTime < 10){
                return "0"+endTime+":00";
            }else {
                if (endTime == 24){
                    return "23:59";
                }else {
                    return endTime+":00";
                }
            }
        }
    }

    /**
     * 获取付款时限
     * @return
     */
    private void getLimit() {
        Map<String, String> map = new HashMap<>();
        map.put("parentKey", "trade_time_out");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemInformation("660906", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(List<SystemParameterModel> data, String SucMessage) {
                if (data == null)
                    return;

                // 初始化付款时限
                limits = new String[data.size()];
                int maxLimit = 0;
                for (int i=0; i<data.size(); i++){
                    // 遍历出最大的付款时限
                    if (Integer.parseInt(data.get(i).getDvalue()) > maxLimit){
                        maxLimit = Integer.parseInt(data.get(i).getDvalue());
                    }

                    limits[i] = data.get(i).getDvalue();
                }
                mBinding.tvLimit.setText(maxLimit+"");
                limit = limits[0];

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 选择付款时限
     * @param view
     */
    private void initLimitPopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, limits);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            limit = popupWindow.getNumberPicker(R.id.np_type, limits);
            mBinding.tvLimit.setText(limit);

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }


    private Boolean check(){
        if (mBinding.tvPrice.getText().toString().equals("")){
            showToast(getStrRes(R.string.deal_publish_hint_price));
            return false;
        }

        if (mBinding.edtPremium.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_premium));
            return false;
        }

        Double premium = Double.parseDouble(mBinding.edtPremium.getText().toString().trim());
        if (premium < -99.99 || premium > 99.99){
            showToast(StringUtil.getString(R.string.deal_publish_hint_premium_scope_start)
                    + "-99.99%~+99.99%"
                    + getStrRes(R.string.deal_publish_hint_premium_scope_end));
            return false;
        }

        if (mBinding.edtProtectPrice.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_protect_sale));
            return false;
        }

        if (mBinding.edtMin.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_min));
            return false;
        }
        if (mBinding.edtMax.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_max));
            return false;
        }
        if (mBinding.edtAmount.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_amount_sale_hint));
            return false;
        }
        if (mBinding.tvWay.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_way));
            return false;
        }
        if (mBinding.tvLimit.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_limit));
            return false;
        }
        if (mBinding.edtRemark.getText().toString().equals("")){
            showToast(StringUtil.getString(R.string.deal_publish_hint_remark));
            return false;
        }

        return true;
    }

    /**
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间选择状态
     */
    private String isOpenTimeOpen(TextView startTime, TextView endTime){

        if (startTime.getText().equals("00:00") && endTime.getText().equals("23:59")){
            // 时间为00:00~23:59,全天打开
            return "open";
        } else if (startTime.getText().equals(StringUtil.getString(R.string.deal_open_time_close)) && endTime.getText().equals(StringUtil.getString(R.string.deal_open_time_close))){
            // 全天关闭
            return "close";
        }else {
            // 开放时间自定义
            return "custom";
        }

    }

    private JSONArray getOpenTime(){

        List<TextView> startTimeList = new ArrayList<>();
        startTimeList.add(mBinding.llOpenTime.tvStart1);
        startTimeList.add(mBinding.llOpenTime.tvStart2);
        startTimeList.add(mBinding.llOpenTime.tvStart3);
        startTimeList.add(mBinding.llOpenTime.tvStart4);
        startTimeList.add(mBinding.llOpenTime.tvStart5);
        startTimeList.add(mBinding.llOpenTime.tvStart6);
        startTimeList.add(mBinding.llOpenTime.tvStart7);

        List<TextView> endTimeList = new ArrayList<>();
        endTimeList.add(mBinding.llOpenTime.tvEnd1);
        endTimeList.add(mBinding.llOpenTime.tvEnd2);
        endTimeList.add(mBinding.llOpenTime.tvEnd3);
        endTimeList.add(mBinding.llOpenTime.tvEnd4);
        endTimeList.add(mBinding.llOpenTime.tvEnd5);
        endTimeList.add(mBinding.llOpenTime.tvEnd6);
        endTimeList.add(mBinding.llOpenTime.tvEnd7);

        JSONArray displayTime = new JSONArray();
        JSONObject displayTimeObject;
        try {
            for (int i=0; i<7; i++){

                String status = isOpenTimeOpen(startTimeList.get(i), endTimeList.get(i));

                if (status.equals("custom")){
                    displayTimeObject = new JSONObject();
                    displayTimeObject.put("endTime",Integer.parseInt(endTimeList.get(i).getText().toString().split(":")[0]));
                    displayTimeObject.put("startTime",Integer.parseInt(startTimeList.get(i).getText().toString().split(":")[0]));
                    displayTimeObject.put("week",i+1);
                } else if(status.equals("close")) {
                    displayTimeObject = new JSONObject();
                    displayTimeObject.put("endTime",24);
                    displayTimeObject.put("startTime",24);
                    displayTimeObject.put("week",i+1);
                } else {
                    displayTimeObject = new JSONObject();
                    displayTimeObject.put("endTime",24);
                    displayTimeObject.put("startTime",0);
                    displayTimeObject.put("week",i+1);
                }
                displayTime.put(displayTimeObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return displayTime;

    }

    private void sale(String publishType) {

        JSONObject object = new JSONObject();

        BigDecimal bigDecimal = new BigDecimal(mBinding.edtAmount.getText().toString().trim());

        try {
            if (bean != null){ // 草稿
                object.put("adsCode", bean.getCode());
            }
            object.put("token", SPUtilHelper.getUserToken());
            object.put("userId", SPUtilHelper.getUserId());
            object.put("displayTime", getOpenTime());
            object.put("truePrice", "0");
            object.put("leaveMessage", mBinding.edtRemark.getText().toString().trim());
            object.put("maxTrade", mBinding.edtMax.getText().toString().trim());
            object.put("minTrade", mBinding.edtMin.getText().toString().trim());
            object.put("onlyTrust", onlyFans);
            object.put("payLimit", mBinding.tvLimit.getText().toString());
            object.put("payType", getPayType(mBinding.tvWay.getText().toString()));
            object.put("premiumRate", (Double.parseDouble(mBinding.edtPremium.getText().toString()) / 100)+"");
            object.put("totalCount", bigDecimal.multiply(getUnit(coinType)).toString().split("\\.")[0]);
            object.put("protectPrice", mBinding.edtProtectPrice.getText().toString().trim());
            object.put("publishType", publishType);
            object.put("tradeCoin", coinType);
            object.put("tradeCurrency", "CNY");
            object.put("tradeType", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625220", object.toString());

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    if (publishType.equals("0")){
                        showToast(StringUtil.getString(R.string.deal_publish_save_success));
                    }else {
                        EventBusModel model = new EventBusModel();
                        model.setTag(EventTags.DEAL_PAGE_CHANGE);
                        // 交易界面显示的币种
                        model.setEvInfo(coinType);
                        model.setEvInt(1);
                        EventBus.getDefault().post(model);

                        showToast(StringUtil.getString(R.string.deal_publish_success));
                    }
                    finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void getAccount(){
        Map<String, Object> map = new HashMap<>();
        map.put("currency", coinType);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {

            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {

                if (data == null)
                    return;

                for (CoinModel.AccountListBean model : data.getAccountList()){
                    if (model.getCurrency().equals(coinType)){

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

    protected void getListData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "sell_ads_hint");
        map.put("start", "1");
        map.put("limit", "20");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameterList("660915", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemParameterListModel>(this) {

            @Override
            protected void onSuccess(SystemParameterListModel data, String SucMessage) {
                if (data == null)
                    return;

                model = data;

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     *
     * @return
     */
    private void getFieldExplain(String key){

        for(SystemParameterListModel.ListBean bean : model.getList()){
            if (bean.getCkey().equals(key)){
                new AlertDialog.Builder(this).setTitle(StringUtil.getString(R.string.tip))
                        .setMessage(bean.getCvalue())
                        .setPositiveButton(StringUtil.getString(R.string.confirm), null).show();
            }
        }
    }

}
