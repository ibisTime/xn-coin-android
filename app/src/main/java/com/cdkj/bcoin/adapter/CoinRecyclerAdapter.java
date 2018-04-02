package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;

import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.bcoin.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.bcoin.util.DeviceUtils.getScreenWith;


public class CoinRecyclerAdapter extends BaseQuickAdapter<BaseCoinModel, BaseViewHolder> {


    public CoinRecyclerAdapter(@Nullable List<BaseCoinModel> data) {
        super(R.layout.item_coin_recycle, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseCoinModel item) {
        if(item.isChoose()){
            helper.setTextColor(R.id.tv_coin, ContextCompat.getColor(mContext, R.color.colorAccent));
            helper.setBackgroundColor(R.id.line_coin, ContextCompat.getColor(mContext, R.color.colorAccent));
        }else{
            helper.setTextColor(R.id.tv_coin, ContextCompat.getColor(mContext, R.color.black));
            helper.setBackgroundColor(R.id.line_coin, ContextCompat.getColor(mContext, R.color.white));
        }
        helper.setText(R.id.tv_coin, item.getSymbol());

        helper.setVisible(R.id.iv_msg_tip, item.isShowTip());

        RelativeLayout rlCoin = helper.getView(R.id.ll_coin);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) rlCoin.getLayoutParams();
        if (getData().size() < 5){
            //获取当前控件的布局对象
            params.width = getScreenWith(mContext) / getData().size(); //设置当前控件布局的宽度为屏幕的getData().size()分之一
        }else {
            //获取当前控件的布局对象
            params.width = getScreenWith(mContext) / 5; //设置当前控件布局的宽度为屏幕的五分之一
        }
        rlCoin.setLayoutParams(params);//将设置好的布局参数应用到控件中

    }
}
