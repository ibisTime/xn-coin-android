package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.UserInviteHistoryAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.UserRefereeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/23.
 */

public class UserInviteHistoryActivity extends BaseRefreshActivity<UserRefereeModel.ListBean> {

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserInviteHistoryActivity.class));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopTitle(getStrRes(R.string.user_title_invite_history));
        setTopLineState(true);
        setSubLeftImgState(true);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserRefereeModel.ListBean bean = (UserRefereeModel.ListBean) mAdapter.getItem(position);

            assert bean != null;
            UserPersonActivity.open(this, bean.getUserId(), bean.getNickname(), bean.getPhoto());
        });

        getListData(pageIndex,limit,true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, Object> map = new HashMap<>();
        map.put("userReferee", SPUtilHelper.getUserId());
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getUserReferee("805120", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserRefereeModel>(this) {

            @Override
            protected void onSuccess(UserRefereeModel data, String SucMessage) {
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
    protected BaseQuickAdapter onCreateAdapter(List<UserRefereeModel.ListBean> mDataList) {
        return new UserInviteHistoryAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.invite_history_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
