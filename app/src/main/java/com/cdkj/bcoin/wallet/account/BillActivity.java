package com.cdkj.bcoin.wallet.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.BillAdapter;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.model.BillModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class BillActivity extends BaseRefreshActivity<BillModel.ListBean> {

    public static final String TYPE_ALL = "all";
    public static final String TYPE_CHARGE = "charge";
    public static final String TYPE_WITHDRAW = "withdraw";
    public static final String TYPE_FROZEN = "frozen";

    private String openType;
    private String accountNumber;

    private String type = "";
    private String kind = "";
    private String[] types;
    private String[] bizType = {"", "charge", "withdraw", "buy", "sell", "tradefee", "withdrawfee", "invite"};
    NumberPicker numberPicker;


    public static void open(Context context,String accountNumber,String openType) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, BillActivity.class)
                .putExtra("openType", openType)
                .putExtra("accountNumber", accountNumber));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopLineState(true);
        setSubLeftImgState(true);
        setSubRightTitleAndClick(getStrRes(R.string.bill_type_select), v -> {
            popupType(v);
        });

        init();
        getListData(pageIndex,limit,false);
    }

    private void init() {

        types = new String[]{getStrRes(R.string.bill_type_all), getStrRes(R.string.bill_type_charge),
                getStrRes(R.string.bill_type_withdraw), getStrRes(R.string.biz_type_buy), getStrRes(R.string.biz_type_sell),
                getStrRes(R.string.biz_type_tradefee), getStrRes(R.string.biz_type_withdrawfee), getStrRes(R.string.biz_type_invite)};

        if (getIntent() == null)
            return;

        openType = getIntent().getStringExtra("openType");
        accountNumber = getIntent().getStringExtra("accountNumber");

        if (openType != null){
            switch (openType){

                case TYPE_ALL: // 默认显示全部流水
                    type = "";
                    kind = "0";
                    setTopTitle(getStrRes(R.string.bill_title_all));
                    break;

                case TYPE_CHARGE: // 默认显示充值流水
                    type = TYPE_CHARGE;
                    kind = "0";
                    setTopTitle(getStrRes(R.string.bill_title_charge));
                    setSubRightTitHide();
                    break;

                case TYPE_WITHDRAW: // 默认显示提币流水
                    type = TYPE_WITHDRAW;
                    kind = "0";
                    setTopTitle(getStrRes(R.string.bill_title_withdraw));
                    setSubRightTitHide();
                    break;

                case TYPE_FROZEN: // 默认显示冻结流水
                    type = TYPE_FROZEN;
                    kind = "1";
                    setTopTitle(getStrRes(R.string.bill_title_frozen));
                    setSubRightTitHide();
                    break;

            }
        }

        // item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            BillDetailActivity.open(this, (BillModel.ListBean) adapter.getItem(position));
        });
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {

        Map<String, String> map = new HashMap<>();
        map.put("limit", limit+"");
        map.put("start", pageIndex+"");

        if (type.equals("frozen")){
            map.put("bizType", "");
            map.put("kind", kind);
        }else {
            map.put("bizType", type);
            map.put("kind", kind);
        }
        map.put("accountNumber", accountNumber);
        map.put("token", SPUtilHelper.getUserToken());
        map.put("systemCode", MyConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBillListData("802524", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<BillModel>(this) {

            @Override
            protected void onSuccess(BillModel data, String SucMessage) {
                if (data == null)
                    return;

                // 重新初始化type
                type = "";

                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<BillModel.ListBean> mDataList) {
        return new BillAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        if (openType != null){
            switch (openType){

                default: // 默认显示全部流水
                    return getStrRes(R.string.bill_all_none);

                case TYPE_CHARGE: // 默认显示充值流水
                    return getStrRes(R.string.bill_charge_none);

                case TYPE_WITHDRAW: // 默认显示提币流水
                    return getStrRes(R.string.bill_withdraw_none);

                case TYPE_FROZEN: // 默认显示冻结流水
                    return getStrRes(R.string.bill_frozen_none);

            }
        }else {
            return getStrRes(R.string.bill_none);
        }

    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }

    private NumberPicker.OnValueChangeListener provinceChangedListener = (arg0, arg1, arg2) -> type = bizType[arg2];

    private void popupType(View view) {


        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_bill_type, null);

        TextView tvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);
        numberPicker = (NumberPicker) mView.findViewById(R.id.np_type);
        numberPicker.setDisplayedValues(types);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(types.length - 1);
        numberPicker.setOnValueChangedListener(provinceChangedListener);
        // 禁止输入
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor((v, event) -> {

            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });

        tvCancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        tvConfirm.setOnClickListener(v -> {
            popupWindow.dismiss();
            getListData(1,10,true);
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

}
