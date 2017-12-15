package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.ConsultingModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/11/22.
 */

public class ConsultingAdapter extends BaseQuickAdapter<ConsultingModel.ListBean, BaseViewHolder> {

    public ConsultingAdapter(@Nullable List<ConsultingModel.ListBean> data) {
        super(R.layout.item_market_consulting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConsultingModel.ListBean item) {
        ImageView iv = helper.getView(R.id.iv_item);

        ImgUtils.loadImage(mContext, item.getAdvPic(), iv);
        iv.setOnClickListener(view -> {
            WebViewActivity.openContent(mContext, item.getTitle(), item.getContent());
        });
    }
}
