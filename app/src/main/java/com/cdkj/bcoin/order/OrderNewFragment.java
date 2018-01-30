package com.cdkj.bcoin.order;

import android.app.AlertDialog;
import android.text.TextUtils;

import com.cdkj.baseim.activity.TxImLogingActivity;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.OrderAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.deal.DealChatActivity;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.model.OrderModel;
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

import static com.cdkj.baseim.activity.TxImLogingActivity.ORDER_DNS_NEW;
import static com.cdkj.baseim.activity.TxImLogingActivity.ORDER_NEW;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_UPDATE;

/**
 * Created by lei on 2017/11/29.
 */

public class OrderNewFragment extends BaseRefreshFragment<OrderDetailModel> {

    private OrderDetailModel bean;

    private List<String> statusList = new ArrayList<>();

    public static OrderNewFragment getInstance() {
        OrderNewFragment fragment = new OrderNewFragment();
        return fragment;
    }

    @Override
    protected void afterCreate(int pageIndex, int limit) {
        // 初始化
        statusList.clear();
        statusList.add("-1");
        statusList.add("0");
        statusList.add("1");
        statusList.add("5");

        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            bean = (OrderDetailModel) adapter.getItem(position);

            ImUserInfo info = new ImUserInfo();
            if (TextUtils.equals(bean.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家

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
                TxImLogingActivity.open(mActivity, info,false,true, ORDER_DNS_NEW);
            }else { // 已下单订单
                TxImLogingActivity.open(mActivity, info,false,true, ORDER_NEW);
            }
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {

            OrderDetailModel orderDetailModel = (OrderDetailModel) adapter.getItem(position);

            if (orderDetailModel.getStatus().equals("-1")){
                deleteConfirm(orderDetailModel.getCode());
            }

            return true;
        });

        getListData(pageIndex,limit,true);

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
        return new OrderAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.order_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }

    @Subscribe
    public void openOrderActivity(ImUserInfo imUserInfo){
        if (imUserInfo.getEventTag().equals(ORDER_NEW)){

            OrderActivity.open(mActivity, bean, imUserInfo);
        }

    }

    @Subscribe
    public void openDealChatActivity(ImUserInfo imUserInfo){
        if (imUserInfo.getEventTag().equals(ORDER_DNS_NEW)){

            DealChatActivity.open(mActivity, bean, imUserInfo);

        }
    }

    private void getConversation() {
        int num = 0;

        List<OrderDetailModel> list = mAdapter.getData();

        for (OrderDetailModel model : list){

            for (TIMConversation conversation : TIMManagerExt.getInstance().getConversationList()){

                // 会话Id是否等于订单Id
                if (model.getCode().equals(conversation.getPeer())) {

                    //获取会话扩展实例
                    TIMConversationExt conExt = new TIMConversationExt(conversation);
                    num += conExt.getUnreadMessageNum();
                }

            }
        }

        if (num > -1){
            EventBusModel model = new EventBusModel();
            model.setTag(EventTags.IM_MSG_TIP_NEW);
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

    private void deleteConfirm(String code) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setTitle(getStrRes(R.string.attention))
                .setMessage(getStrRes(R.string.order_delete_confirm))
                .setPositiveButton(getStrRes(R.string.confirm), (dialogInterface, i) -> {

                    delete(code);

                })
                .setCancelable(false);

        builder.setNegativeButton(getStrRes(R.string.cancel), null).show();

    }

    private void delete(String code){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625249", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    ToastUtil.show(mActivity, getStrRes(R.string.order_delete_success));
                    onMRefresh(1,10,true);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }
}
