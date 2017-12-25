package com.cdkj.bcoin.wallet.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityBillDetailBinding;
import com.cdkj.bcoin.model.BillModel;
import com.cdkj.bcoin.util.AccountUtil;

import java.math.BigDecimal;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * Created by lei on 2017/10/26.
 */

public class BillDetailActivity extends AbsBaseActivity {

    private BillModel.ListBean bean;
    private ActivityBillDetailBinding mBinding;

    public static void open(Context context, BillModel.ListBean bean){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, BillDetailActivity.class)
                .putExtra("bean", bean));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_bill_detail, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.wallet_title_bill_detail));
        setTopLineState(true);
        setSubLeftImgState(true);

        if (getIntent() != null){
            bean = (BillModel.ListBean) getIntent().getSerializableExtra("bean");
            if (bean != null){
                setView();
            }

        }


    }

    private void setView() {
        mBinding.tvInfo.setText(bean.getBizNote());
        BigDecimal tas = new BigDecimal(bean.getTransAmountString());

        int i=tas.compareTo(BigDecimal.ZERO);
        if (i==1){
            mBinding.tvAmount.setText("+"+ AccountUtil.weiToEth(tas)+bean.getCurrency());
        }else {
            mBinding.tvAmount.setText(AccountUtil.weiToEth(tas)+bean.getCurrency());
        }

        mBinding.tvBefore.setText(AccountUtil.weiToEth(new BigDecimal(bean.getPreAmountString())));
        mBinding.tvAfter.setText(AccountUtil.weiToEth(new BigDecimal(bean.getPostAmountString())));
        mBinding.tvDate.setText(DateUtil.formatStringData(bean.getCreateDatetime(),DEFAULT_DATE_FMT));
        mBinding.tvType.setText(AccountUtil.formatBizType(bean.getBizType()));
        mBinding.tvStatus.setText(AccountUtil.formatBillStatus(bean.getStatus()));
    }


}
