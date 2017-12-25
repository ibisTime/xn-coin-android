package com.cdkj.bcoin.order;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cdkj.baseim.fragment.ChatFragment;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityOrderBinding;
import com.cdkj.bcoin.databinding.DialogOrderArbitrateBinding;
import com.cdkj.bcoin.databinding.DialogOrderEvaluateBinding;
import com.cdkj.bcoin.main.MainActivity;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.OrderUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.tencent.imsdk.TIMConversationType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_UPDATE_ORDER;
import static com.cdkj.bcoin.util.OrderUtil.getOrderStatus;

/**
 * Created by lei on 2017/10/31.
 */

public class OrderActivity extends AbsBaseActivity {

    private ActivityOrderBinding mBinding;

    private String code;
    private OrderDetailModel bean;

    private ImUserInfo imUserInfo;

    public static void open(Context context, String code, ImUserInfo imUserInfo){
        if (context == null) {
            return;
        }

        context.startActivity(new Intent(context, OrderActivity.class).putExtra("code", code).putExtra("imUserInfo",imUserInfo));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_order, null ,false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(StringUtil.getStirng(R.string.order_title));
        setTopLineState(true);
        setSubLeftImgState(true);


        init();
        initListener();

        getOrder(true);
    }

    private void init() {
        if (getIntent() == null)
            return;

        code = getIntent().getStringExtra("code");
        imUserInfo = getIntent().getParcelableExtra("imUserInfo");

        // 重写键盘，不根据点击坐标隐藏键盘，避免与聊天Fragment冲突
        isNeedHideKeyBord = false;

        initChatFragment();
    }

    private void initChatFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.add(R.id.layout_chat, ChatFragment.getInstance(imUserInfo, TIMConversationType.Group));
        transaction.commit();
    }

    private void initListener() {
        mBinding.llOrderId.setOnClickListener(view -> {
            if (! TextUtils.isEmpty(mBinding.tvOrderId.getText().toString())){
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mBinding.tvOrderId.getText().toString().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(this, StringUtil.getStirng(R.string.order_copy_id));
            }
        });

        mBinding.tvAmount.setOnClickListener(view -> {
            String amount = mBinding.tvAmount.getText().toString();
            if (! TextUtils.isEmpty(amount)){
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(amount.substring(0,amount.length()-3)); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(this, StringUtil.getStirng(R.string.order_copy_amount));
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {

            if (mBinding.btnConfirm.getText().equals(StringUtil.getStirng(R.string.order_tag))){

                tip(StringUtil.getStirng(R.string.order_tag_confirm));
            }else if (mBinding.btnConfirm.getText().equals(StringUtil.getStirng(R.string.order_release))){
                tip(StringUtil.getStirng(R.string.order_release_confirm));
            }else if (mBinding.btnConfirm.getText().equals(StringUtil.getStirng(R.string.order_evaluation))){
                showEvaluate(view);
            }else if(mBinding.btnConfirm.getText().equals(StringUtil.getStirng(R.string.order_wallet))){
                finish();
                EventBusModel eventBusModel = new EventBusModel();
                eventBusModel.setEvInt(MainActivity.WALLET); //显示认证界面
                eventBusModel.setTag(EventTags.MAINCHANGESHOWINDEX);
                EventBus.getDefault().post(eventBusModel);
            }
        });

        mBinding.llOrderHide.setOnClickListener(view -> {
            // 重写点击Activity布局隐藏键盘
            hideKeyboard(view);
        });
    }

    private void tag(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", bean.getCode());
        map.put("updater", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("remark", "买家标记打款");

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625243", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    showToast(StringUtil.getStirng(R.string.order_tag_success));
                    getOrder(true);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void release(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", bean.getCode());
        map.put("updater", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("remark", "卖家释放货币");

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625244", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    showToast(StringUtil.getStirng(R.string.order_release_success));
                    getOrder(true);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void showEvaluate(View view){
        final String[] evaluate = {"2"};
        DialogOrderEvaluateBinding popupBinding;

        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_order_evaluate, null, false);

        // 一个自定义的布局，作为显示的内容
        View mView = popupBinding.getRoot();

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor((v, event) -> {

            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });

        popupBinding.llGood.setOnClickListener(view1 -> {
            initPopupView(popupBinding);
            popupBinding.ivGood.setBackgroundResource(R.mipmap.order_eva_good_r);
            popupBinding.tvGood.setTextColor(getResources().getColor(R.color.colorAccent));
            evaluate[0] = "2";
        });

        popupBinding.llMedium.setOnClickListener(view1 -> {
            initPopupView(popupBinding);
            popupBinding.ivMedium.setBackgroundResource(R.mipmap.order_eva_medium_r);
            popupBinding.tvMedium.setTextColor(getResources().getColor(R.color.colorAccent));

            evaluate[0] = "1";
        });

        popupBinding.llBad.setOnClickListener(view1 -> {
            initPopupView(popupBinding);
            popupBinding.ivBad.setBackgroundResource(R.mipmap.order_eva_bad_r);
            popupBinding.tvBad.setTextColor(getResources().getColor(R.color.colorAccent));

            evaluate[0] = "0";
        });


        popupBinding.tvConfirm.setOnClickListener(v -> {
            if (evaluate[0].equals("")){
                showToast("");
            }else {
                popupWindow.dismiss();
                evaluate(evaluate[0]);
            }

        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);
    }

    public void initPopupView(DialogOrderEvaluateBinding popupBinding){
        popupBinding.ivGood.setBackgroundResource(R.mipmap.order_eva_good_g);
        popupBinding.tvGood.setTextColor(getResources().getColor(R.color.gray_b3b3b3));

        popupBinding.ivMedium.setBackgroundResource(R.mipmap.order_eva_medium_g);
        popupBinding.tvMedium.setTextColor(getResources().getColor(R.color.gray_b3b3b3));

        popupBinding.ivBad.setBackgroundResource(R.mipmap.order_eva_bad_g);
        popupBinding.tvBad.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
    }

    private void evaluate(String comment){
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("comment", comment);
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625245", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    showToast(StringUtil.getStirng(R.string.order_evaluation_success));
                    getOrder(true);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    protected void getOrder(boolean isShow) {

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        Call call = RetrofitUtils.createApi(MyApi.class).getOrderDetail("625251", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShow)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<OrderDetailModel>(this) {

            @Override
            protected void onSuccess(OrderDetailModel data, String SucMessage) {
                if (data == null)
                    return;

                bean = data;
                setView();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void setView() {
        mBinding.tvOrderId.setText(bean.getCode().substring(bean.getCode().length()-8, bean.getCode().length()));
        mBinding.tvStatus.setText(getOrderStatus(bean));

        mBinding.tvPrice.setText(bean.getTradePrice()+"CNY");
        mBinding.tvAmount.setText(bean.getTradeAmount()+"CNY");
        mBinding.tvQuantity.setText(AccountUtil.weiToEth(new BigDecimal(bean.getCountString()))+"ETH");

        mBinding.tvBuyer.setText(bean.getBuyUserInfo().getNickname());
        mBinding.tvSeller.setText(bean.getSellUserInfo().getNickname());
        mBinding.tvRemark.setText(StringUtil.getStirng(R.string.order_leave_message) + bean.getLeaveMessage());

        //
        setSubRightTitHide();
        String tip="";
//        int limit =  DateUtil.getDateDValue(bean.getInvalidDatetime(),bean.getCreateDatetime());

        if (bean.getStatus().equals("0")) { //待支付

            if (TextUtils.equals(bean.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_tag));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            }else {
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_tag_wait));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
            }

            tip = StringUtil.getStirng(R.string.order_save_limit_start)+"<font color='#f15353'>"
                    + DateUtil.formatStringData(bean.getInvalidDatetime(), DateUtil.DATE_HMS)
                    + "</font>"+StringUtil.getStirng(R.string.order_save_limit_end);


        } else if (bean.getStatus().equals("1")){ //已支付待释放

            if (TextUtils.equals(bean.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_release_wait));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
            }else { // 自己是卖家
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_release));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            }

            tip = StringUtil.getStirng(R.string.order_save_limit_start)+"<font color='#f15353'>"
                    + DateUtil.formatStringData(bean.getInvalidDatetime(), DateUtil.DATE_HMS)
                    + "</font>"+StringUtil.getStirng(R.string.order_save_limit_end);

            // 可以申请仲裁
            setSubRightTitleAndClick(StringUtil.getStirng(R.string.order_arbitrate), v -> {
                popupArbitrate(v);
            });

        } else if(bean.getStatus().equals("2")){ //待评价

            if (TextUtils.equals(bean.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家

                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_evaluation));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                tip = StringUtil.getStirng(R.string.order_evaluation_tip);

                if (!TextUtils.isEmpty(bean.getBsComment())){ // 买家已评价
                    mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_evaluation_by_b));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
                    tip = StringUtil.getStirng(R.string.order_evaluation_tip_by_b);
                }

                if (!TextUtils.isEmpty(bean.getSbComment())){ // 卖家已评价
                    mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_evaluation));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                    tip = StringUtil.getStirng(R.string.order_evaluation_tip_by_s);
                }

            }else { // 自己是卖家
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_evaluation));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                tip = StringUtil.getStirng(R.string.order_evaluation_tip);

                if (!TextUtils.isEmpty(bean.getBsComment())){ // 买家已评价
                    mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_evaluation));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                    tip = StringUtil.getStirng(R.string.order_evaluation_tip_by_b);
                }

                if (!TextUtils.isEmpty(bean.getSbComment())){ // 卖家已评价
                    mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_evaluation_by_s));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
                    tip = StringUtil.getStirng(R.string.order_evaluation_tip_by_s);
                }

            }

        } else if(bean.getStatus().equals("3")){ //已完成

            if (TextUtils.equals(bean.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_wallet));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            }else { // 自己是卖家
                mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.order_wallet));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            }

            tip = StringUtil.getStirng(R.string.order_done);

        } else {
            mBinding.btnConfirm.setText(OrderUtil.getOrderStatus(bean));
            mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);

            tip = bean.getRemark();
        }

        mBinding.tvTip.setText(Html.fromHtml(tip));
    }

    private void tip(String msg) {
        new AlertDialog.Builder(this).setTitle(StringUtil.getStirng(R.string.attention))
                .setMessage(msg)
                .setPositiveButton(StringUtil.getStirng(R.string.confirm), (dialogInterface, i) -> {
                    if (msg.equals(StringUtil.getStirng(R.string.order_release_confirm))){
                        release();
                    }else {
                        tag();
                    }

                }).setNegativeButton(StringUtil.getStirng(R.string.cancel), null).show();
    }

    private void popupArbitrate(View view) {
        DialogOrderArbitrateBinding popupBinding;

        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_order_arbitrate, null, false);

        // 一个自定义的布局，作为显示的内容
        View mView = popupBinding.getRoot();

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor((v, event) -> {

            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });


        popupBinding.tvCancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        popupBinding.tvConfirm.setOnClickListener(v -> {
            popupWindow.dismiss();
            arbitrate(popupBinding.edtReason.getText().toString());
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void arbitrate(String reason){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("reason", reason);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625246", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    showToast(StringUtil.getStirng(R.string.order_arbitrate_success));
                    getOrder(true);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Subscribe
    public void imMsgUpdate(String tag) {
        if (tag.equals(IM_MSG_UPDATE_ORDER)){
            getOrder(false);

        }

    }

}
