package com.cdkj.bcoin.deal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.views.MyPickerPopupWindow;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityDealSearchBinding;

import static com.cdkj.baselibrary.appmanager.MyConfig.COIN_TYPE;

/**
 * Created by lei on 2017/10/30.
 */

public class DealSearchActivity extends AbsBaseActivity {

    private ActivityDealSearchBinding mBinding;

    private String searchType = "adv";

    // 币种
    private String coinType;

    // 广告类型
    private String type = "";
    private String[] types;
    private String[] typeValue = {"1", "0"};

    // 付款方式
    private String payType = "";
    private String[] payTypes;
    private String[] payTypeValue = {"0", "1", "2"};

    public static void open(Context context, String coinType){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, DealSearchActivity.class).putExtra("coinType",coinType));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_deal_search, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.deal_search));
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();
    }

    private void init() {
        if (getIntent() == null)
            return;

        coinType = getIntent().getStringExtra("coinType");
        // 设置选择币
        mBinding.tvCoinSelect.setText(coinType);

        types = new String[]{getStrRes(R.string.deal_buy), getStrRes(R.string.deal_sale)};
        payTypes = new String[]{getStrRes(R.string.zhifubao), getStrRes(R.string.weixin), getStrRes(R.string.card)};
    }

    private void initListener() {

        mBinding.rlAdv.setOnClickListener(view -> {
            initView();

            mBinding.tvAdv.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mBinding.vAdv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

            mBinding.llAdv.setVisibility(View.VISIBLE);
            mBinding.llUser.setVisibility(View.GONE);

            searchType = "adv";
            mBinding.btnConfirm.setText(getStrRes(R.string.deal_search_adv));
        });

        mBinding.rlUser.setOnClickListener(view -> {
            initView();

            mBinding.tvUser.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mBinding.vUser.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

            mBinding.llAdv.setVisibility(View.GONE);
            mBinding.llUser.setVisibility(View.VISIBLE);

            searchType = "user";
            mBinding.btnConfirm.setText(getStrRes(R.string.deal_search_user));
        });

        mBinding.llCoinSelect.setOnClickListener(view -> {
            initPopup(view);
        });

        mBinding.llType.setOnClickListener(this::initTypePopup);

        mBinding.llPayType.setOnClickListener(this::initPayTypePopup);

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check()){
                if (searchType.equals("user")){
                    SearchUserActivity.open(this, mBinding.edtUser.getText().toString().trim());
                }else {
                    SearchDealActivity.open(this,
                            mBinding.edtMin.getText().toString().trim(),
                            mBinding.edtMax.getText().toString().trim(),
                            type ,
                            payType,
                            coinType);

                }

            }

        });
    }

    private void initView(){
        mBinding.tvAdv.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBinding.tvUser.setTextColor(ContextCompat.getColor(this, R.color.black));

        mBinding.vAdv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mBinding.vUser.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

    }

    /**
     * 选择币种
     * @param view
     */
    private void initPopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, COIN_TYPE);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            coinType = popupWindow.getNumberPicker(R.id.np_type, COIN_TYPE);

            // 设置选择币
            mBinding.tvCoinSelect.setText(coinType);

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }


    /**
     * 选择广告类型
     * @param view
     */
    private void initTypePopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, types);

        popupWindow.setOnClickListener(R.id.tv_cancel, (View v) -> popupWindow.dismiss());

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            type = typeValue[popupWindow.getNumberPickerValue(R.id.np_type)];
            mBinding.tvType.setText(popupWindow.getNumberPicker(R.id.np_type, types));

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
        popupWindow.setNumberPicker(R.id.np_type, payTypes);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> popupWindow.dismiss());

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            payType = payTypeValue[popupWindow.getNumberPickerValue(R.id.np_type)];
            mBinding.tvWay.setText(popupWindow.getNumberPicker(R.id.np_type, payTypes));

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }


    private boolean check(){
        if (searchType.equals("adv")){

            if (type.equals("")){
                showToast(getStrRes(R.string.deal_search_hint_type));
                return false;
            }

            if (mBinding.edtMin.getText().toString().trim().equals("")){
                showToast(getStrRes(R.string.deal_search_hint_min));
                return false;
            }

            if (mBinding.edtMax.getText().toString().trim().equals("")){
                showToast(getStrRes(R.string.deal_search_hint_max));
                return false;
            }

//            if (payType.equals("")){
//                showToast(getStrRes(R.string.deal_search_hint_pay));
//                return false;
//            }

        }else {

            if (mBinding.edtUser.getText().toString().trim().equals("")){
                showToast(getStrRes(R.string.deal_search_hint_user));
                return false;
            }

        }

        return true;
    }

}
