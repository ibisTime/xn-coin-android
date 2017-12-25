package com.cdkj.bcoin.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.SystemMessageAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.SystemMessageModel;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/10/27.
 */

public class SystemMessageActivity extends BaseRefreshActivity<SystemMessageModel.ListBean> {


    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, SystemMessageActivity.class));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        // 初始化title
        setTopTitle(StringUtil.getStirng(R.string.market_title_message));
        setTopLineState(true);
        setSubLeftImgState(true);

        getListData(pageIndex,limit,true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("fromSystemCode", MyConfig.SYSTEMCODE);
        map.put("channelType", "4");
        map.put("pushType", "");
        map.put("toSystemCode", MyConfig.SYSTEMCODE);
        map.put("toKind", "C");
        map.put("status","1");
        map.put("toMobile", "");
        map.put("smsType", "");
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemMessage("804040", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemMessageModel>(this) {

            @Override
            protected void onSuccess(SystemMessageModel data, String SucMessage) {
                if (data == null)
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
    protected BaseQuickAdapter onCreateAdapter(List<SystemMessageModel.ListBean> mDataList) {
        return new SystemMessageAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return StringUtil.getStirng(R.string.market_message_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
