package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.TrustModel;
import com.cdkj.ethereumdemo.util.AccountUtil;
import com.cdkj.ethereumdemo.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class TrustAdapter extends BaseQuickAdapter<TrustModel.ListBean, BaseViewHolder> {

    public TrustAdapter(@Nullable List<TrustModel.ListBean> data) {
        super(R.layout.item_trust, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrustModel.ListBean item) {

        if (item.getToUserInfo() != null)
        helper.setText(R.id.tv_name,item.getToUserInfo().getNickname());
        TextView tvAvatar = helper.getView(R.id.tv_avatar);
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImgUtils.loadAvatar(mContext, item.getToUserInfo().getPhoto(), item.getToUserInfo().getNickname(), ivAvatar, tvAvatar);

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getUpdateDatetime(), DateUtil.DATE_YMD));

        if (item.getToUserInfo().getUserStatistics() == null)
            return;
        helper.setText(R.id.tv_deal, StringUtil.getStirng(R.string.deal)+item.getToUserInfo().getUserStatistics().getJiaoYiCount());
        helper.setText(R.id.tv_trust, StringUtil.getStirng(R.string.trust)+item.getToUserInfo().getUserStatistics().getBeiXinRenCount());
        if(item.getToUserInfo().getUserStatistics().getBeiPingJiaCount() == 0){
            helper.setText(R.id.tv_good, StringUtil.getStirng(R.string.good) +"0%");
        }else {
            double hpRate = item.getToUserInfo().getUserStatistics().getBeiHaoPingCount() / item.getToUserInfo().getUserStatistics().getBeiPingJiaCount();
            helper.setText(R.id.tv_good, StringUtil.getStirng(R.string.good)+AccountUtil.formatInt(hpRate * 100)+"%");
        }
    }
}
