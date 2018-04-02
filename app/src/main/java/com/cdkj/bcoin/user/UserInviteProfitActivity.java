package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.UserInviteProfitAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.UserInviteProfitModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2018/3/14.
 */

public class UserInviteProfitActivity extends BaseRefreshActivity<UserInviteProfitModel> {

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserInviteProfitActivity.class));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopTitle(getStrRes(R.string.user_title_invite_profit));
        setTopLineState(true);
        setSubLeftImgState(true);

        getListData(pageIndex,limit,true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApi.class).getInviteProfitList("805124", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<UserInviteProfitModel>(this) {

            @Override
            protected void onSuccess(List<UserInviteProfitModel> data, String SucMessage) {
                if (data == null || data.size() == 0)
                    return;

                setData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<UserInviteProfitModel> mDataList) {
        return new UserInviteProfitAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.invite_profit_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
