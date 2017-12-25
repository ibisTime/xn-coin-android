package com.cdkj.bcoin.user;

import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.PublishedAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.DealModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/16.
 */

public class UserPublishedFragment extends BaseRefreshFragment<DealDetailModel> {

    private String type;

    private DealModel model;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static UserFragment getInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void afterCreate(int pageIndex, int limit) {
        type = getArguments().getString("type");

        getListData(pageIndex, limit, true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("coin", "ETH");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("publishType", type);
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getDeal("625227", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealModel>(mActivity) {

            @Override
            protected void onSuccess(DealModel data, String SucMessage) {
                if (data == null)
                    return;

                model = data;

                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<DealDetailModel> mDataList) {
        return new PublishedAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.user_published_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
