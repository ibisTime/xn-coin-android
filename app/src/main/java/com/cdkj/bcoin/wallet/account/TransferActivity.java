package com.cdkj.bcoin.wallet.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityTransferBinding;

/**
 * Created by lei on 2017/10/18.
 */

public class TransferActivity extends AbsBaseActivity {

    private String userId;
    private InputDialog inputDialog;
    private ActivityTransferBinding mBinding;

    public static void open(Context context,String userId){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, TransferActivity.class).putExtra("userId", userId));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transfer, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        init();
        initListener();
    }

    private void init() {
        userId = getIntent().getStringExtra("userId");
    }

    private void initListener(){
        mBinding.btnTransfer.setOnClickListener(view -> {

            if (check()){
//                showInputDialog();
           }

        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())){
                ToastUtil.show(TransferActivity.this, "请填写手机号码");
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtQuantity.getText().toString().trim())){
            ToastUtil.show(TransferActivity.this, "请填写数量");
            return false;
        }

//        if (TextUtils.isEmpty(mBinding.edtRemark.getText().toString().trim())){
//            ToastUtil.show(TransferActivity.this, "请填写备注");
//            return false;
//        }

        return true;
    }

    private void showInputDialog() {
        if(inputDialog ==null){

            inputDialog = new InputDialog(this).builder().setTitle("请输入资金密码")
                    .setPositiveBtn("确定", (view, inputMsg) -> {

                        if (inputDialog.getContentView().getText().toString().trim().equals("")) {
                            showToast("请输入资金密码");
                        } else {
                            inputDialog.dismiss();
                        }

                    })
                    .setNegativeBtn("取消", null)
                    .setContentMsg("");

            inputDialog.getContentView().setText("");
            inputDialog.getContentView().setHint("请输入资金密码");
        }

        inputDialog.getContentView().setText("");
        inputDialog.show();
    }

//    private void getTransfer() {
//        BigDecimal bigDecimal = new BigDecimal(mBinding.edtQuantity.getText().toString().trim());
//
//        String api = "/user/tx?userId="+ userId
//                +"&toMobile=" + mBinding.edtMobile.getText().toString().trim()
//                +"&amount=" + bigDecimal.multiply(AmountUtil.unit)+"";
//
//        new Xutil().post(api, new Xutil.XUtils3CallBackPost() {
//            @Override
//            public void onSuccess(String result) {
//                showToast("转账成功");
//            }
//
//            @Override
//            public void onTip(String tip) {
//                Toast.makeText(TransferActivity.this, tip, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(TransferActivity.this, "无法连接服务器，请稍后重试", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
