package com.cdkj.ethereumdemo.order;

import android.util.Log;

import com.cdkj.baseim.activity.TxImLogingActivity;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.adapter.OrderDoneAdapter;
import com.cdkj.ethereumdemo.api.MyApi;
import com.cdkj.ethereumdemo.deal.DealChatActivity;
import com.cdkj.ethereumdemo.model.OrderDetailModel;
import com.cdkj.ethereumdemo.model.OrderModel;
import com.cdkj.ethereumdemo.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static com.cdkj.baseim.activity.TxImLogingActivity.ORDER_DNS_DONE;
import static com.cdkj.baseim.activity.TxImLogingActivity.ORDER_DONE;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_UPDATE;

/**
 * Created by lei on 2017/11/29.
 */

public class OrderDoneFragment extends BaseRefreshFragment<OrderDetailModel> {

    private OrderDetailModel bean;

    private List<String> statusList = new ArrayList<>();

    public static OrderDoneFragment getInstance() {
        OrderDoneFragment fragment = new OrderDoneFragment();
        return fragment;
    }

    @Override
    protected void afterCreate(int pageIndex, int limit) {
        // 初始化
        statusList.clear();
        statusList.add("2");
        statusList.add("3");
        statusList.add("4");

        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            bean = (OrderDetailModel) adapter.getItem(position);

            ImUserInfo info = new ImUserInfo();
            if (bean.getBuyUser().equals(SPUtilHelper.getUserId())) { // 自己是买家

                info.setLeftImg(bean.getSellUserInfo().getPhoto());
                info.setLeftName(bean.getSellUserInfo().getNickname());

            }else { // 自己是卖家
                info.setLeftImg(bean.getBuyUserInfo().getPhoto());
                info.setLeftName(bean.getBuyUserInfo().getNickname());

            }
            info.setRightImg(SPUtilHelper.getUserPhoto());
            info.setRightName(SPUtilHelper.getUserName());
            info.setIdentify(bean.getCode());

            if (bean.getStatus().equals("-1")){ // 待下单订单
                TxImLogingActivity.open(mActivity, info,false,true, ORDER_DNS_DONE);
            }else { // 已下单订单
                TxImLogingActivity.open(mActivity, info,false,true, ORDER_DONE);
            }
        });

        getListData(pageIndex,limit,false);


    }

    @Override
    public void onResume() {
        super.onResume();
        onMRefresh(1,10,true);
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();

        if (mBinding == null)
            return;

        onMRefresh(1,10,true);
    }


    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {

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
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getOrder("625250", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<OrderModel>(mActivity) {

            @Override
            protected void onSuccess(OrderModel data, String SucMessage) {
                if (data.getList() == null)
                    return;

                setData(data.getList());
                getConversation();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<OrderDetailModel> mDataList) {
        return new OrderDoneAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return StringUtil.getStirng(R.string.order_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }

    @Subscribe
    public void openOrderActivity(ImUserInfo imUserInfo){
        if (imUserInfo.getEventTag().equals(ORDER_DONE)){
            Log.e("getRightImg",imUserInfo.getRightImg());
            Log.e("getRightName",imUserInfo.getRightName());
            Log.e("getLeftImg",imUserInfo.getLeftImg());
            Log.e("getLeftName",imUserInfo.getLeftName());
            Log.e("getIdentify",imUserInfo.getIdentify());
            Log.e("--------------","--------------");

            OrderActivity.open(mActivity, bean.getCode(), imUserInfo);
        }

    }

    @Subscribe
    public void openDealChatActivity(ImUserInfo imUserInfo){
        if (imUserInfo.getEventTag().equals(ORDER_DNS_DONE)){
            Log.e("getRightImg",imUserInfo.getRightImg());
            Log.e("getRightName",imUserInfo.getRightName());
            Log.e("getLeftImg",imUserInfo.getLeftImg());
            Log.e("getLeftName",imUserInfo.getLeftName());
            Log.e("getIdentify",imUserInfo.getIdentify());
            Log.e("--------------","--------------");

            DealChatActivity.open(mActivity, bean.getCode(), imUserInfo);

        }
    }

    private void getConversation() {
        int num = 0;

        OrderFragment.conversationList = TIMManagerExt.getInstance().getConversationList();
        List<OrderDetailModel> list = mAdapter.getData();
        String orderUserId;

        for (OrderDetailModel model : list){

            for (TIMConversation conversation : TIMManagerExt.getInstance().getConversationList()){

//                if (model.getBuyUser().equals(SPUtilHelper.getUserId())) { // 自己是买家
//                    orderUserId = model.getSellUser();
//                } else {
//                    orderUserId = model.getBuyUser();
//                }

                if (model.getCode().equals(conversation.getPeer())) {

                    //获取会话扩展实例
                    TIMConversationExt conExt = new TIMConversationExt(conversation);
                    num += conExt.getUnreadMessageNum();
                }

            }
        }

        if (num > -1){
            EventBusModel model = new EventBusModel();
            model.setTag(EventTags.IM_MSG_TIP_DONE);
            model.setEvInt(num);
            EventBus.getDefault().post(model);
        }
    }

    @Subscribe
    public void imMsgUpdate(String tag) {
        if (tag.equals(IM_MSG_UPDATE)){
            onMRefresh(1,10,false);

        }

    }
}
