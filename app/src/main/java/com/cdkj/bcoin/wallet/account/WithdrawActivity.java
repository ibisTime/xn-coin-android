package com.cdkj.bcoin.wallet.account;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityWithdrawBinding;
import com.cdkj.bcoin.model.CoinModel;
import com.cdkj.bcoin.model.SystemParameterModel;
import com.cdkj.bcoin.user.UserAddressActivity;
import com.cdkj.bcoin.util.AccountUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.SystemUtils.paste;
import static com.cdkj.bcoin.user.UserAddressActivity.TYPE_WITHDRAW;

/**
 * Created by lei on 2017/10/18.
 */

public class WithdrawActivity extends AbsBaseActivity {

    private boolean isTransfer = false;

    private CoinModel.AccountListBean model;
    private PermissionHelper permissionHelper;

    private InputDialog inputDialog;
    private ActivityWithdrawBinding mBinding;

    // 是否需要交易密码和谷歌验证 认证账户不需要交易密码和谷歌验证
    private boolean isCerti = true;

    public static void open(Context context, CoinModel.AccountListBean model){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WithdrawActivity.class).putExtra("model", model));
    }

    // 输入方式
    private String type;
    private String[] types;

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_withdraw, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.wallet_title_withdraw));
        setTopLineState(true);
        setSubLeftImgState(true);
        setSubRightTitleAndClick(getStrRes(R.string.wallet_charge_recode),v -> {
            BillActivity.open(this,model.getAccountNumber(),BillActivity.TYPE_WITHDRAW);
        });

        init();

        initListener();
        getWithdrawFee();
    }

    private void init() {
        type = getStrRes(R.string.popup_select);
        types = new String[]{getStrRes(R.string.popup_select), getStrRes(R.string.popup_scan), getStrRes(R.string.popup_paste)};

        if (getIntent() != null){
            model = (CoinModel.AccountListBean) getIntent().getSerializableExtra("model");
            if (model != null){
                mBinding.tvBalance.setText(AccountUtil.sub(Double.parseDouble(model.getAmountString()), Double.parseDouble(model.getFrozenAmountString())));
                mBinding.tvCurrency.setText(model.getCurrency());
            }

        }
    }

    private void initListener(){

        mBinding.llAddress.setOnClickListener(view -> {
            popupType(view);
        });

        mBinding.btnPaste.setOnClickListener(view -> {
            mBinding.edtGoogle.setText(SystemUtils.paste(this));
        });

        mBinding.btnWithdraw.setOnClickListener(view -> {

            if (check()){
                if (isCerti){
                    showInputDialog();
                }else {
                    if (isTransfer){ // 内部转账
                        transfer("");
                    }else {
                        withdrawal("");
                    }
                }

            }

        });


        mBinding.tvAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (SPUtilHelper.getGoogleAuthFlag()){ // 已开启谷歌验证
                    mBinding.llGoogle.setVisibility(isCerti ? View.VISIBLE : View.GONE);
                    mBinding.lineGoogle.setVisibility(isCerti ? View.VISIBLE : View.GONE);
                }

            }
        });
    }

    //权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.tvAddress.getText().toString().trim())){
            ToastUtil.show(WithdrawActivity.this, getStrRes(R.string.wallet_withdraw_address_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtAmount.getText().toString().trim())){
            ToastUtil.show(WithdrawActivity.this, getStrRes(R.string.wallet_withdraw_amount_hint));
            return false;
        }

        if (isCerti){
            if (SPUtilHelper.getGoogleAuthFlag()){
                if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())){
                    showToast(getStrRes(R.string.google_code_hint));
                    return false;
                }
            }
        }

        return true;
    }

    private void showInputDialog() {
        if(inputDialog ==null){
            inputDialog = new InputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                        if (inputDialog.getContentView().getText().toString().trim().equals("")) {
                            showToast(getStrRes(R.string.trade_code_hint));
                        } else {
                            if (isTransfer){ // 内部转账
                                transfer(inputDialog.getContentView().getText().toString().trim());
                            }else {
                                withdrawal(inputDialog.getContentView().getText().toString().trim());
                            }


                            inputDialog.dismiss();
                        }

                    })
                    .setNegativeBtn(getStrRes(R.string.cancel), null)
                    .setContentMsg("");
            inputDialog.getContentView().setText("");
            inputDialog.getContentView().setHint(getStrRes(R.string.trade_code_hint));
        }
        inputDialog.getContentView().setText("");
        inputDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!TextUtils.isEmpty(result))
                        mBinding.tvAddress.setText(result);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WithdrawActivity.this, getStrRes(R.string.qrcode_parsing_failure), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getWithdrawFee() {
        Map<String, String> map = new HashMap<>();

        map.put("key", "withdraw_fee");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("625917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.edtCommission.setText(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 提现
     * @param tradePwd
     */
    private void withdrawal(String tradePwd) {
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtAmount.getText().toString().trim());

        Map<String, String> map = new HashMap<>();

        map.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("accountNumber", model.getAccountNumber());
        map.put("amount", bigDecimal.multiply(AccountUtil.UNIT).toString().split("\\.")[0]);
        map.put("payCardNo", mBinding.tvAddress.getText().toString().trim());
        map.put("payCardInfo", model.getCurrency());
        map.put("applyNote", "C端提现");
        map.put("tradePwd", tradePwd);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("802750", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                showToast(getStrRes(R.string.wallet_withdraw_success));
                finish();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 内部转账
     * @param tradePwd
     */
    private void transfer(String tradePwd) {
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtAmount.getText().toString().trim());

        Map<String, String> map = new HashMap<>();

        map.put("token", SPUtilHelper.getUserToken());
        map.put("fromUserId", SPUtilHelper.getUserId());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("amount", bigDecimal.multiply(AccountUtil.UNIT)+"");
        map.put("toAddress", mBinding.tvAddress.getText().toString().trim());
        map.put("tradePwd", tradePwd);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625851", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                showToast(getStrRes(R.string.wallet_transfer_success));
                finish();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    private void popupType(View view) {


        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_wallet_type, null);

        TextView tvCancel = mView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = mView.findViewById(R.id.tv_confirm);
        NumberPicker npType = mView.findViewById(R.id.np_type);
        npType.setDisplayedValues(types);
        npType.setMinValue(0);
        npType.setMaxValue(types.length - 1);
        npType.setOnValueChangedListener(ChangedListener);
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

            if (type.equals(getStrRes(R.string.popup_select))){
                UserAddressActivity.open(this, TYPE_WITHDRAW);
            } else if (type.equals(getStrRes(R.string.popup_scan))){
                // 是否需要交易密码和谷歌验证 认证账户不需要交易密码和谷歌验证
                isCerti = true;
                scan();
            } else if (type.equals(getStrRes(R.string.popup_paste))){
                // 是否需要交易密码和谷歌验证 认证账户不需要交易密码和谷歌验证
                isCerti = true;

                mBinding.tvAddress.setText(paste(this));
            }

            // 初始化type（没有滚动的情况）
            type = getStrRes(R.string.popup_select);
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private NumberPicker.OnValueChangeListener ChangedListener = (arg0, arg1, arg2) -> type = types[arg2];



    private void scan(){
        permissionHelper = new PermissionHelper(this);

        permissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(WithdrawActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 100);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getStrRes(R.string.camera_refused));
            }
        }, Manifest.permission.CAMERA);
    }

    @Subscribe
    public void setAddress(EventBusModel model){

        if (model == null)
            return;

        if (model.getTag().equals(EventTags.ADDRESS_SELECT)){
            if (model.getEvInt() == 0){ // 地址未认证，需要交易密码
                isCerti = true;
            }else { // 地址已认证，不需要交易密码
                isCerti = false;
            }
            mBinding.tvAddress.setText(model.getEvInfo());
        }

    }
}
