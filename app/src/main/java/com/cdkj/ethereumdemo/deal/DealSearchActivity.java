package com.cdkj.ethereumdemo.deal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.databinding.ActivityDealSearchBinding;
import com.cdkj.ethereumdemo.util.StringUtil;

/**
 * Created by lei on 2017/10/30.
 */

public class DealSearchActivity extends AbsBaseActivity {

    private ActivityDealSearchBinding mBinding;

    private String searchType = "adv";

    // 广告类型
    private String type = "1";
    private String[] types = {StringUtil.getStirng(R.string.deal_buy), StringUtil.getStirng(R.string.deal_sale)};
    private String[] typeValue = {"1", "0"};

    // 付款方式
    private String payType = "0";
    private String[] payTypes = {StringUtil.getStirng(R.string.zhifubao), StringUtil.getStirng(R.string.weixin), StringUtil.getStirng(R.string.card)};
    private String[] payTypeValue = {"0", "1", "2"};

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, DealSearchActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_deal_search, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(StringUtil.getStirng(R.string.deal_search));
        setTopLineState(true);
        setSubLeftImgState(true);

        initListener();
    }

    private void initListener() {

        mBinding.rlAdv.setOnClickListener(view -> {
            initView();

            mBinding.tvAdv.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mBinding.vAdv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

            mBinding.llAdv.setVisibility(View.VISIBLE);
            mBinding.llUser.setVisibility(View.GONE);

            searchType = "adv";
            mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.deal_search_adv));
        });

        mBinding.rlUser.setOnClickListener(view -> {
            initView();

            mBinding.tvUser.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mBinding.vUser.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

            mBinding.llAdv.setVisibility(View.GONE);
            mBinding.llUser.setVisibility(View.VISIBLE);

            searchType = "user";
            mBinding.btnConfirm.setText(StringUtil.getStirng(R.string.deal_search_user));
        });

        mBinding.llType.setOnClickListener(this::popupType);

        mBinding.llPayType.setOnClickListener(this::popupPayType);

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check()){
                if (searchType.equals("user")){
                    SearchUserActivity.open(this, mBinding.edtUser.getText().toString().trim());
                }else {
                    SearchDealActivity.open(this,
                            mBinding.edtMin.getText().toString().trim(),
                            mBinding.edtMax.getText().toString().trim(),
                            type ,
                            payType);

                }

            }

        });
    }

    private void initView(){
        mBinding.tvAdv.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBinding.tvUser.setTextColor(ContextCompat.getColor(this, R.color.black));

        mBinding.vAdv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mBinding.vUser.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

    }

    /**
     * 支付方式
     * @param view
     */
    private void popupType(View view) {
        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_wallet_type, null);

        TextView tvCancel = mView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = mView.findViewById(R.id.tv_confirm);
        NumberPicker npType = mView.findViewById(R.id.np_type);
        npType.setDisplayedValues(types);
        npType.setMinValue(0);
        npType.setMaxValue(types.length - 1);

        // 禁止输入
        npType.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


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

            type = typeValue[npType.getValue()];
            mBinding.tvType.setText(types[npType.getValue()]);

        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    /**
     * 支付方式
     * @param view
     */
    private void popupPayType(View view) {
        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_wallet_type, null);

        TextView tvCancel = mView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = mView.findViewById(R.id.tv_confirm);
        NumberPicker npType = mView.findViewById(R.id.np_type);
        npType.setDisplayedValues(payTypes);
        npType.setMinValue(0);
        npType.setMaxValue(payTypes.length - 1);

        // 禁止输入
        npType.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


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

            payType = payTypeValue[npType.getValue()];
            mBinding.tvWay.setText(payTypes[npType.getValue()]);


        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }


    private boolean check(){
        if (searchType.equals("adv")){

            if (type.equals("")){
                showToast(StringUtil.getStirng(R.string.deal_search_hint_type));
                return false;
            }

            if (mBinding.edtMin.getText().toString().trim().equals("")){
                showToast(StringUtil.getStirng(R.string.deal_search_hint_min));
                return false;
            }

            if (mBinding.edtMax.getText().toString().trim().equals("")){
                showToast(StringUtil.getStirng(R.string.deal_search_hint_max));
                return false;
            }

            if (payType.equals("")){
                showToast(StringUtil.getStirng(R.string.deal_search_hint_pay));
                return false;
            }

        }else {

            if (mBinding.edtUser.getText().toString().trim().equals("")){
                showToast(StringUtil.getStirng(R.string.deal_search_hint_user));
                return false;
            }

        }

        return true;
    }

}
