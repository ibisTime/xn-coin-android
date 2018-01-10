package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.TrustAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.TrustModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/1.
 */

public class UserTrustActivity extends BaseRefreshActivity<TrustModel.ListBean> {

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserTrustActivity.class));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopTitle(getStrRes(R.string.user_title_trust));
        setTopLineState(true);
        setSubLeftImgState(true);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            TrustModel.ListBean bean = (TrustModel.ListBean) mAdapter.getItem(position);

            UserPersonActivity.open(this, bean.getToUser(), bean.getToUserInfo().getNickname(), bean.getToUserInfo().getPhoto());
        });

        getListData(pageIndex,limit,true);

    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");
        map.put("type", "1"); // 1：信任，0：黑名单

        Call call = RetrofitUtils.createApi(MyApi.class).getTrust("805115", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<TrustModel>(this) {

            @Override
            protected void onSuccess(TrustModel data, String SucMessage) {
                if (data == null)
                    return;

                if (data.getList() == null)
                    return;

                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<TrustModel.ListBean> mDataList) {
        return new TrustAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.user_trust_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
