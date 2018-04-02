package com.cdkj.bcoin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cdkj.baseim.util.VibratorUtil;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.api.MyApi;
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

import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_UPDATE;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_VIBRATOR;

/**
 * Created by lei on 2018/3/14.
 */

public class OrderTipService extends Service {

    Call call;

    public static void open(Context context){
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context,OrderTipService.class);
        context.startService(intent);
    }

    public static void close(Context context){
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context,OrderTipService.class);
        context.stopService(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 更新订单列表消息状态
        EventBus.getDefault().post(EventTags.IM_MSG_UPDATE);

        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe
    public void imMsgUpdate(String tag) {
        if (tag.equals(IM_MSG_UPDATE)){
            getOrder();
        }

    }


    public void getOrder(){
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
        map.put("tradeCoin", "");
        map.put("tradeCurrency", "");
        map.put("type", "");
        map.put("start", 1+"");
        map.put("limit", 1000+"");

        call = RetrofitUtils.createApi(MyApi.class).getOrder("625250", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<OrderModel>(this) {

            @Override
            protected void onSuccess(OrderModel data, String SucMessage) {
                if (data.getList() == null)
                    return;

                getConversation(data.getList());
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    private void getConversation(List<OrderDetailModel> lists) {
        int num = 0;

        List<String> listTip = new ArrayList<>();

        for (OrderDetailModel model : lists){

            for (TIMConversation conversation : TIMManagerExt.getInstance().getConversationList()){

                // 会话Id是否等于订单Id
                if (model.getCode().equals(conversation.getPeer())) {

                    //获取会话扩展实例
                    TIMConversationExt conExt = new TIMConversationExt(conversation);
                    num += conExt.getUnreadMessageNum();

                    if (listTip.size() == 0){

                        if (model.getStatus().equals("2")||model.getStatus().equals("3")||model.getStatus().equals("4")){
                            listTip.add("done");
                        }else {
                            listTip.add("now");
                        }

                    }else {

                        if (model.getStatus().equals("2")||model.getStatus().equals("3")||model.getStatus().equals("4")){
                            // 判断未读币种集合是否已经有该类币种,没有就添加
                            if (!listTip.contains("done")){
                                listTip.add("done");
                            }

                        }else {
                            // 判断未读币种集合是否已经有该类币种,没有就添加
                            if (!listTip.contains("now")){
                                listTip.add("now");
                            }
                        }
                    }
                }
            }
        }

        if (num > -1){

            for (String str : listTip){

                if (str.equals("done")){
                    EventBusModel model = new EventBusModel();
                    model.setTag(EventTags.IM_MSG_TIP_DONE);
                    model.setEvInt(num);
                    EventBus.getDefault().post(model);
                }else {
                    EventBusModel model = new EventBusModel();
                    model.setTag(EventTags.IM_MSG_TIP_NEW);
                    model.setEvInt(num);
                    EventBus.getDefault().post(model);
                }
            }

        }
    }

    @Subscribe
    public void imMsgVibrator(String tag) {
        if (tag.equals(IM_MSG_VIBRATOR)){

            // 新消息震动提示
            long[] patter = {0, 350, 0, 350};
            VibratorUtil.vibrate(this, patter,-1);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (call == null)
            return;
        call.cancel();
    }
}
