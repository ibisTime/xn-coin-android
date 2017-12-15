package com.cdkj.ethereumdemo.wallet.account;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.databinding.ActivityRechargeBinding;
import com.cdkj.ethereumdemo.model.CoinModel;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class RechargeActivity extends AbsBaseActivity {

    private CoinModel.AccountListBean model;

    private ActivityRechargeBinding mBinding;

    public static void open(Context context, CoinModel.AccountListBean model){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, RechargeActivity.class)
                .putExtra("model", model));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_recharge, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getStrRes(R.string.wallet_title_charge));
        setTopLineState(true);
        setSubLeftImgState(true);
        setSubRightTitleAndClick(getStrRes(R.string.wallet_charge_recode),v -> {
            BillActivity.open(this,model.getAccountNumber(), "charge");
        });

        if (getIntent() != null){
            model = (CoinModel.AccountListBean) getIntent().getSerializableExtra("model");

            if (! TextUtils.isEmpty(model.getCoinAddress())){
                initQRCodeAndAddress();
            }

        }

        initView();
        initListener();

    }

    private void initView() {
        if (model == null)
            return;

        switch (model.getCurrency()){
            case "ETH":
                mBinding.tvCoin.setText(getStrRes(R.string.wallet_charge_address_eth));
                break;

            case "BTC":
                mBinding.tvCoin.setText(getStrRes(R.string.wallet_charge_address_btc));
                break;

        }
    }

    private void initQRCodeAndAddress(){
        Bitmap mBitmap = CodeUtils.createImage(model.getCoinAddress(), 400, 400, null);
        mBinding.imgQRCode.setImageBitmap(mBitmap);
        mBinding.txtAddress.setText(model.getCoinAddress());

    }

    private void initListener(){

        mBinding.llClose.setOnClickListener(view -> {
            mBinding.llTip.setVisibility(View.GONE);
        });

        mBinding.llCopy.setOnClickListener(view -> {

            if (! TextUtils.isEmpty(model.getCoinAddress())){
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(model.getCoinAddress().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(RechargeActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
            }

        });

        mBinding.llAddress.setOnClickListener(view -> {

            if (! TextUtils.isEmpty(model.getCoinAddress())){
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(model.getCoinAddress().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(RechargeActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
            }

        });

//        mBinding.imgQRCode.setOnClickListener(view -> {
//
//        });
    }
}
