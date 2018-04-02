package com.cdkj.bcoin.order;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityOrderBinding;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.model.OrderModel;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.MAIN_CHANGE_SHOW_INDEX;
import static com.cdkj.baselibrary.appmanager.EventTags.ORDER_CLOSE;
import static com.cdkj.baselibrary.appmanager.EventTags.ORDER_COIN_TIP;

/**
 * Created by lei on 2018/3/14.
 */

public class OrderActivity extends AbsBaseActivity {

    private ActivityOrderBinding mBinding;

    public static void open(Context context){
        if (context == null) {
            return;
        }

        context.startActivity(new Intent(context, OrderActivity.class));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_order, null ,false);

        init();

        return mBinding.getRoot();
    }

    private void init() {

        Fragment orderFragment = new OrderFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_order, orderFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        Log.e("for getOrder","a");
        getOrder();
    }

    private void getOrder() {
        List<String> statusList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("adsCode", "");
        map.put("buyUser", "");
        map.put("belongUser", SPUtilHelper.getUserId());
        map.put("code", "");
        map.put("sellUser", "");
        map.put("payType", "");
        map.put("statusList", statusList);
        map.put("tradeCoin", "");
        map.put("tradeCurrency", "");
        map.put("type", "");
        map.put("start", 1+"");
        map.put("limit", 1000+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getOrder("625250", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<OrderModel>(this) {

            @Override
            protected void onSuccess(OrderModel data, String SucMessage) {
                if (data.getList() == null || data.getList().size() == 0)
                    return;

                try{
                    getConversation(data.getList());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void getConversation(List<OrderDetailModel> data) {

        List<OrderDetailModel> unreadOrderList = new ArrayList<>();

        for (OrderDetailModel model : data){

            for (TIMConversation conversation : TIMManagerExt.getInstance().getConversationList()){

                // 会话Id是否等于订单Id
                if (model.getCode().equals(conversation.getPeer())) {

                    // 获取会话扩展实例
                    TIMConversationExt conExt = new TIMConversationExt(conversation);
                    // 当前会话实例的未读消息是否大于0
                    if (conExt.getUnreadMessageNum() > 0){
                        // 将已确定的未读订单添加到未读订单集合
                        unreadOrderList.add(model);
                    }
                }
            }
        }

        List<String> nowUnreadCoinList = new ArrayList<>();
        List<String> doneUnreadCoinList = new ArrayList<>();

        // 遍历未读订单集合，将未读订单所属的币种添加到 未读币种集合 里
        for (OrderDetailModel model : unreadOrderList){
            //判断订单的状态 -1,0,1,5为进行中订单
            if (model.getStatus().equals("-1") || model.getStatus().equals("0")
                    || model.getStatus().equals("1") || model.getStatus().equals("5")){

                if (nowUnreadCoinList.size() == 0){
                    nowUnreadCoinList.add(model.getTradeCoin());

                }else {
                    // 判断未读币种集合是否已经有该类币种,没有就添加
                    if (!nowUnreadCoinList.get(nowUnreadCoinList.size()-1).equals(model.getTradeCoin())){
                        nowUnreadCoinList.add(model.getTradeCoin());
                    }
                }

            }else { // 2,3,4为已结束订单

                if (doneUnreadCoinList.size() == 0){
                    doneUnreadCoinList.add(model.getTradeCoin());
                }else {
                    // 判断未读币种集合是否已经有该类币种,没有就添加
                    if (!doneUnreadCoinList.get(doneUnreadCoinList.size()-1).equals(model.getTradeCoin())){
                        doneUnreadCoinList.add(model.getTradeCoin());
                    }
                }
            }
        }


        // 将组装好的未读币种集合传给子Fragment，通知UI更新
        EventBusModel modelNow = new EventBusModel();
        modelNow.setTag(EventTags.ORDER_COIN_NOW_TIP);
        modelNow.setList(nowUnreadCoinList);
        EventBus.getDefault().post(modelNow);

        // 将组装好的未读币种集合传给子Fragment，通知UI更新
        EventBusModel model = new EventBusModel();
        model.setTag(EventTags.ORDER_COIN_DONE_TIP);
        model.setList(doneUnreadCoinList);
        EventBus.getDefault().post(model);

    }

    @Subscribe
    public void imMsgUpdate(String tag) {
        if (tag.equals(ORDER_CLOSE)){
            finish();
        }

        if (tag.equals(ORDER_COIN_TIP)){
            getOrder();
        }
    }

    @Subscribe
    public void MainEventBus(EventBusModel eventBusModel) {
        if (eventBusModel == null) {
            return;
        }

        if (TextUtils.equals(eventBusModel.getTag(), MAIN_CHANGE_SHOW_INDEX)) {
            finish();
        }
    }

}
