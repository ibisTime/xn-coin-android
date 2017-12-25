package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.user.UserPersonActivity;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import java.util.List;

import static com.cdkj.bcoin.util.OrderUtil.getOrderStatus;

/**
 * Created by lei on 2017/10/31.
 */

public class OrderAdapter extends BaseQuickAdapter<OrderDetailModel, BaseViewHolder> {

    public OrderAdapter(@Nullable List<OrderDetailModel> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailModel item) {

        if (TextUtils.equals(item.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
            helper.setText(R.id.tv_type, StringUtil.getStirng(R.string.buy));

            helper.setText(R.id.tv_name, item.getSellUserInfo().getNickname());
            TextView tvAvatar = helper.getView(R.id.tv_avatar);
            ImageView ivAvatar = helper.getView(R.id.iv_avatar);
            ImgUtils.loadAvatar(mContext, item.getSellUserInfo().getPhoto(), item.getSellUserInfo().getNickname(), ivAvatar, tvAvatar);
        } else {
            helper.setText(R.id.tv_type, StringUtil.getStirng(R.string.sale));

            helper.setText(R.id.tv_name, item.getBuyUserInfo().getNickname());
            TextView tvAvatar = helper.getView(R.id.tv_avatar);
            ImageView ivAvatar = helper.getView(R.id.iv_avatar);
            ImgUtils.loadAvatar(mContext, item.getBuyUserInfo().getPhoto(), item.getBuyUserInfo().getNickname(), ivAvatar, tvAvatar);
        }

        helper.setText(R.id.tv_status, getOrderStatus(item));

        if (item.getStatus().equals("-1")) {
            helper.setText(R.id.tv_amount, "");
            helper.setText(R.id.tv_code, "");
        }else {
            helper.setText(R.id.tv_amount, StringUtil.getStirng(R.string.trade_amount) + item.getTradeAmount() + "CNY");
            helper.setText(R.id.tv_code, StringUtil.getStirng(R.string.order_code) + item.getCode().substring(item.getCode().length() - 8, item.getCode().length()));
        }

        if (TIMManagerExt.getInstance().getConversationList().size() > 0) {

            for (TIMConversation conversation : TIMManagerExt.getInstance().getConversationList()) {

                if (item.getCode().equals(conversation.getPeer())) {
                    //获取会话扩展实例
                    TIMConversationExt conExt = new TIMConversationExt(conversation);
                    long num = conExt.getUnreadMessageNum();

                    TextView tvMsg = helper.getView(R.id.tv_msg);
                    if (num > 0) {
                        tvMsg.setText(num+"");
                        tvMsg.setVisibility(View.VISIBLE);
                    }else {
                        tvMsg.setVisibility(View.GONE);
                    }
                }

            }

        }

        helper.getView(R.id.fl_avatar).setOnClickListener(view -> {
            if (TextUtils.equals(item.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                UserPersonActivity.open(mContext, item.getSellUserInfo().getUserId());
            } else {
                UserPersonActivity.open(mContext, item.getBuyUserInfo().getUserId());
            }

        });

    }


}
