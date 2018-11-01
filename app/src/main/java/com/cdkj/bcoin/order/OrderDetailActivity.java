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
import android.text.method.ScrollingMovementMethod;
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
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityOrderDetailBinding;
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

public class OrderDetailActivity extends AbsBaseActivity {

    private ActivityOrderDetailBinding mBinding;

    private OrderDetailModel model;

    private ImUserInfo imUserInfo;

    public static void open(Context context, OrderDetailModel model, ImUserInfo imUserInfo) {
        if (context == null) {
            return;
        }

        context.startActivity(new Intent(context, OrderDetailActivity.class).putExtra("model", model).putExtra("imUserInfo", imUserInfo));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_order_detail, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(StringUtil.getString(R.string.order_title));
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();
    }

    private void init() {
        if (getIntent() == null)
            return;

        model = (OrderDetailModel) getIntent().getSerializableExtra("model");
        imUserInfo = getIntent().getParcelableExtra("imUserInfo");

        if (model != null)
            setView();

        // 重写键盘，不根据点击坐标隐藏键盘，避免与聊天Fragment冲突
        isNeedHideKeyBord = false;

//        initChatFragment();
    }

    private void initChatFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.add(R.id.layout_chat, ChatFragment.getInstance(imUserInfo, TIMConversationType.Group));
        transaction.commit();
    }

    private void initListener() {
        mBinding.llOrderId.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(mBinding.tvOrderId.getText().toString())) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mBinding.tvOrderId.getText().toString().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(this, StringUtil.getString(R.string.order_copy_id));
            }
        });

        mBinding.tvAmount.setOnClickListener(view -> {
            String amount = mBinding.tvAmount.getText().toString();
            if (!TextUtils.isEmpty(amount)) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(amount.substring(0, amount.length() - 3)); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                ToastUtil.show(this, StringUtil.getString(R.string.order_copy_amount));
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {

            if (mBinding.btnConfirm.getText().equals(StringUtil.getString(R.string.order_tag))) {
                tip(StringUtil.getString(R.string.order_tag_confirm));
            } else if (mBinding.btnConfirm.getText().equals(StringUtil.getString(R.string.order_release))) {
                tip(StringUtil.getString(R.string.order_release_confirm));
            } else if (mBinding.btnConfirm.getText().equals(StringUtil.getString(R.string.order_evaluation))) {
                showEvaluate(view);
            } else if (mBinding.btnConfirm.getText().equals(StringUtil.getString(R.string.order_wallet))) {
                finish();
                EventBusModel eventBusModel = new EventBusModel();
                eventBusModel.setEvInt(MainActivity.WALLET); //显示认证界面
                eventBusModel.setTag(EventTags.MAIN_CHANGE_SHOW_INDEX);
                EventBus.getDefault().post(eventBusModel);
            }
        });

        mBinding.llOrderHide.setOnClickListener(view -> {
            // 重写点击Activity布局隐藏键盘
            hideKeyboard(view);
        });
    }

    private void tag() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", model.getCode());
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

                if (data.isSuccess()) {
                    showToast(StringUtil.getString(R.string.order_tag_success));
                    getOrder(true);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void cancel() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", model.getCode());
        map.put("updater", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("remark", "买家取消订单");

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625242", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()) {
                    showToast(StringUtil.getString(R.string.order_cancel_success));
                    getOrder(true);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void release() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", model.getCode());
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

                if (data.isSuccess()) {
                    showToast(StringUtil.getString(R.string.order_release_success));
                    getOrder(true);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void showEvaluate(View view) {
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
            if (evaluate[0].equals("")) {
                showToast("");
            } else {
                popupWindow.dismiss();
                evaluate(evaluate[0]);
            }

        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);
    }

    public void initPopupView(DialogOrderEvaluateBinding popupBinding) {
        popupBinding.ivGood.setBackgroundResource(R.mipmap.order_eva_good_g);
        popupBinding.tvGood.setTextColor(getResources().getColor(R.color.gray_b3b3b3));

        popupBinding.ivMedium.setBackgroundResource(R.mipmap.order_eva_medium_g);
        popupBinding.tvMedium.setTextColor(getResources().getColor(R.color.gray_b3b3b3));

        popupBinding.ivBad.setBackgroundResource(R.mipmap.order_eva_bad_g);
        popupBinding.tvBad.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
    }

    private void evaluate(String comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", model.getCode());
        map.put("comment", comment);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625245", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()) {
                    showToast(StringUtil.getString(R.string.order_evaluation_success));
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
        map.put("code", model.getCode());

        Call call = RetrofitUtils.createApi(MyApi.class).getOrderDetail("625251", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShow)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<OrderDetailModel>(this) {

            @Override
            protected void onSuccess(OrderDetailModel data, String SucMessage) {
                if (data == null)
                    return;

                model = data;
                setView();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void setView() {
        mBinding.tvOrderId.setText(model.getCode().substring(model.getCode().length() - 8, model.getCode().length()));
        mBinding.tvStatus.setText(getOrderStatus(model.getStatus()));

        mBinding.tvPrice.setText(AccountUtil.formatDouble(model.getTradePrice()) + MyConfig.CURRENCY);
        mBinding.tvAmount.setText(AccountUtil.formatDouble(model.getTradeAmount()) + MyConfig.CURRENCY);
        mBinding.tvQuantity.setText(AccountUtil.amountFormatUnit(new BigDecimal(model.getCountString()), model.getTradeCoin(), 8) + model.getTradeCoin());
        ImgUtils.loadImage(this, TextUtils.isEmpty(model.getPayAccountQr()) ? "" : model.getPayAccountQr(), mBinding.ivQr);

        mBinding.tvBuyer.setText(model.getBuyUserInfo().getNickname());
        mBinding.tvSeller.setText(model.getSellUserInfo().getNickname());
        mBinding.tvRemark.setText(StringUtil.getString(R.string.order_leave_message) + model.getLeaveMessage());
        mBinding.tvRemark.setMovementMethod(ScrollingMovementMethod.getInstance());

        // 设置右边Title按钮隐藏，之后根据条件显示
        setSubRightTitHide();

        String tip;
//        int limit =  DateUtil.getDateDValue(model.getInvalidDatetime(),model.getCreateDatetime());

        if (model.getStatus().equals("0")) { //待支付

            if (TextUtils.equals(model.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_tag));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);

                // 可以取消订单
                setSubRightTitleAndClick(StringUtil.getString(R.string.order_cancel), v -> {
                    tip(getStrRes(R.string.order_cancel_confirm));
                });

            } else {
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_tag_wait));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
            }

            tip = StringUtil.getString(R.string.order_save_limit_start) + "<font color='#f15353'>"
                    + DateUtil.formatStringData(model.getInvalidDatetime(), DateUtil.DATE_HMS)
                    + "</font>" + StringUtil.getString(R.string.order_save_limit_end);

        } else if (model.getStatus().equals("1")) { //已支付待释放

            if (TextUtils.equals(model.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_release_wait));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
            } else { // 自己是卖家
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_release));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            }

            tip = StringUtil.getString(R.string.order_save_limit_start) + "<font color='#f15353'>"
                    + DateUtil.formatStringData(model.getInvalidDatetime(), DateUtil.DATE_HMS)
                    + "</font>" + StringUtil.getString(R.string.order_save_limit_end);

            // 可以申请仲裁
            setSubRightTitleAndClick(StringUtil.getString(R.string.order_arbitrate), v -> {
                popupArbitrate(v);
            });

        } else if (model.getStatus().equals("2")) { //待评价

            if (TextUtils.equals(model.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家

                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_evaluation));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                tip = StringUtil.getString(R.string.order_evaluation_tip);

                if (!TextUtils.isEmpty(model.getBsComment())) { // 买家已评价
                    mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_evaluation_by_b));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
                    tip = StringUtil.getString(R.string.order_evaluation_tip_by_b);
                }

                if (!TextUtils.isEmpty(model.getSbComment())) { // 卖家已评价
                    mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_evaluation));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                    tip = StringUtil.getString(R.string.order_evaluation_tip_by_s);
                }

            } else { // 自己是卖家
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_evaluation));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                tip = StringUtil.getString(R.string.order_evaluation_tip);

                if (!TextUtils.isEmpty(model.getBsComment())) { // 买家已评价
                    mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_evaluation));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
                    tip = StringUtil.getString(R.string.order_evaluation_tip_by_b);
                }

                if (!TextUtils.isEmpty(model.getSbComment())) { // 卖家已评价
                    mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_evaluation_by_s));
                    mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
                    tip = StringUtil.getString(R.string.order_evaluation_tip_by_s);
                }

            }

        } else if (model.getStatus().equals("3")) { //已完成

            if (TextUtils.equals(model.getBuyUser(), SPUtilHelper.getUserId())) { // 自己是买家
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_wallet));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            } else { // 自己是卖家
                mBinding.btnConfirm.setText(StringUtil.getString(R.string.order_wallet));
                mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
            }

            tip = StringUtil.getString(R.string.order_done);

        } else {
            mBinding.btnConfirm.setText(OrderUtil.getOrderStatus(model.getStatus()));
            mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);

            tip = model.getRemark();
        }

        mBinding.tvTip.setText(Html.fromHtml(tip));
    }

    private void tip(String msg) {
        new AlertDialog.Builder(this).setTitle(StringUtil.getString(R.string.attention))
                .setMessage(msg)
                .setPositiveButton(StringUtil.getString(R.string.confirm), (dialogInterface, i) -> {
                    if (msg.equals(StringUtil.getString(R.string.order_release_confirm))) { // 释放货币
                        release();
                    } else if (msg.equals(StringUtil.getString(R.string.order_cancel_confirm))) { // 取消订单
                        cancel();
                    } else {
                        tag();
                    }

                }).setNegativeButton(StringUtil.getString(R.string.cancel), null).show();
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

    private void arbitrate(String reason) {
        Map<String, String> map = new HashMap<>();
        map.put("code", model.getCode());
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
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

                if (data.isSuccess()) {
                    showToast(StringUtil.getString(R.string.order_arbitrate_success));
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
        if (tag.equals(IM_MSG_UPDATE_ORDER)) {

            getOrder(false);

        }

    }

}
