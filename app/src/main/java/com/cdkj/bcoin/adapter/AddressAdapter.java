package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;

import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.AddressModel;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class AddressAdapter extends BaseQuickAdapter<AddressModel.ListBean, BaseViewHolder> {

    public AddressAdapter(@Nullable List<AddressModel.ListBean> data) {
        super(R.layout.item_user_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressModel.ListBean item) {

        helper.setText(R.id.tv_name,item.getLabel());
        helper.setText(R.id.tv_coin,item.getCurrency());
        helper.setText(R.id.tv_address,item.getAddress());

        switch (item.getStatus()){
            case "0":
                helper.setText(R.id.tv_status, StringUtil.getString(R.string.address_unattestation));
                break;

            case "1":
                helper.setText(R.id.tv_status, StringUtil.getString(R.string.address_attestation));
                break;

        }
    }
}
