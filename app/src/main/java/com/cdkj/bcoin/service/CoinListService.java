package com.cdkj.bcoin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.api.MyApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_ALL;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_SINGEL;

/**
 * Created by lei on 2018/3/14.
 */

public class CoinListService extends Service {

    Call call;

    public static void open(Context context){
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context,CoinListService.class);
        context.startService(intent);
    }

    public static void close(Context context){
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context,CoinListService.class);
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

    @Subscribe
    public void eventBusModel(EventBusModel model) {
        if (model.getTag().equals(BASE_COIN_LIST)){
            getCoinList(model.isEvBoolean(), model.getEvInfo());
        }

    }

    private void getCoinList(boolean isNotifyAll, String notifyLocation){

        // 初始化call 避免程序退出时Call空指针
        Map<String, String> map = new HashMap<>();
        map.put("type", "");
        map.put("ename", "");
        map.put("cname", "");
        map.put("symbol", "");
        map.put("status", "0"); // 0已发布，1已撤下
        map.put("contractAddress", "");

        call = RetrofitUtils.createApi(MyApi.class).getCoinList("802267", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseListCallBack<BaseCoinModel>(this) {

            @Override
            protected void onSuccess(List<BaseCoinModel> data, String SucMessage) {
                if (data == null)
                    return;

                // 如果数据库已有数据，清空重新加载
                if(DataSupport.isExist(BaseCoinModel.class))
                    DataSupport.deleteAll(BaseCoinModel.class);

                // 初始化交易界面默认所选择的币
                data.get(0).setChoose(true);
                DataSupport.saveAll(data);

                doNotify(isNotifyAll, notifyLocation);
            }

            @Override
            protected void onReqFailure(int errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

                doNotify(isNotifyAll, notifyLocation);
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    public void doNotify(boolean isNotifyAll,String notifyLocation){

        EventBusModel model = new EventBusModel();
        if (isNotifyAll){
            model.setTag(BASE_COIN_LIST_NOTIFY_ALL);
            EventBus.getDefault().post(model);
        }else {
            model.setTag(BASE_COIN_LIST_NOTIFY_SINGEL);
            model.setEvInfo(notifyLocation);
            EventBus.getDefault().post(model);
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
