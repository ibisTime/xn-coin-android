package com.cdkj.bcoin.user;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.views.MyPickerPopupWindow;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserAddAddressBinding;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/20.
 */

public class UserAddAddressActivity extends AbsBaseActivity implements SendCodeInterface {

    private ActivityUserAddAddressBinding mBinding;

    private SendPhoneCodePresenter mPresenter;

    private boolean isDefault = false;

    private InputDialog inputDialog;

    // 地址类型:ETH、BTC
    private String addressType;

    // 输入方式
    private String type;
    private String[] types;

    // 粘贴板
    private ClipboardManager mClipboard = null;

    private PermissionHelper permissionHelper;

    public static void open(Context context, String addressType){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserAddAddressActivity.class).putExtra("addressType", addressType));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_add_address, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_address_add));
        setTopLineState(true);
        setSubLeftImgState(true);

        mPresenter = new SendPhoneCodePresenter(this);

        init();
        initListener();
    }

    private void init() {
        type = getStrRes(R.string.popup_scan);
        types = new String[]{getStrRes(R.string.popup_scan), getStrRes(R.string.popup_paste)};

        addressType = getIntent().getStringExtra("addressType");
        mBinding.tvType.setText(addressType);
    }

    private void initListener() {
        mBinding.llAddress.setOnClickListener(view -> {
            initPopup(view);
        });

        mBinding.btnSend.setOnClickListener(view -> {

            mPresenter.sendCodeRequest(SPUtilHelper.getUserPhoneNum(),"802170","C",this);
        });

        mBinding.sbTransfer.setOnCheckedChangeListener((compoundButton, b) -> {

            if (SPUtilHelper.getGoogleAuthFlag()){ // 已开启谷歌验证
                mBinding.llGoogle.setVisibility(b ? View.VISIBLE : View.GONE);
                mBinding.lineGoogle.setVisibility(b ? View.VISIBLE : View.GONE);
            }

            // 是否默认
            isDefault = b;

        });

        mBinding.btnPaste.setOnClickListener(view -> {
            mBinding.edtGoogle.setText(SystemUtils.paste(this));
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check()){
                if (isDefault){
                    showInputDialog();
                }else {
                    add("");
                }

            }
        });
    }

    @Subscribe
    public void changeUi(String tag){
        if (tag == null)
            return;

        if (tag.equals(EventTags.CHANGE_CODE_BTN)){
            mBinding.btnSend.setBackground(ContextCompat.getDrawable(UserAddAddressActivity.this,R.drawable.corner_sign_btn));
        }
    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));

        //改变ui
        mBinding.btnSend.setBackground(ContextCompat.getDrawable(UserAddAddressActivity.this,R.drawable.corner_sign_btn_gray));
    }

    @Override
    public void CodeFailed(String code, String msg) {
        showToast(msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }

    /**
     *
     * @param view
     */
    private void initPopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, types);

        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
            type = popupWindow.getNumberPicker(R.id.np_type, types);

            if (type.equals(getStrRes(R.string.popup_scan))){
                scan();
            }else if (type.equals(getStrRes(R.string.popup_paste))){
                paste();
            }

            popupWindow.dismiss();
        });

        popupWindow.show(view);
    }


    private void paste() {
        // Gets a handle to the clipboard service.
        if (null == mClipboard) {
            mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }

        String resultString = "";
        // 检查剪贴板是否有内容
        if (!mClipboard.hasPrimaryClip()) {
            Toast.makeText(this, getStrRes(R.string.copy_tip_none), Toast.LENGTH_SHORT).show();
        }
        else {
            ClipData clipData = mClipboard.getPrimaryClip();
            int count = clipData.getItemCount();

            for (int i = 0; i < count; ++i) {

                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item
                        .coerceToText(this);

                resultString += str;
            }

        }
        mBinding.tvAddress.setText(resultString);
    }

    private void scan(){
        permissionHelper = new PermissionHelper(this);

        permissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(UserAddAddressActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 100);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getStrRes(R.string.camera_refused));
            }
        }, Manifest.permission.CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
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
                    Toast.makeText(UserAddAddressActivity.this, getStrRes(R.string.qrcode_parsing_failure), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtTag.getText().toString().trim())){
            showToast(getStrRes(R.string.user_address_tag_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.tvAddress.getText().toString().trim())){
            showToast(getStrRes(R.string.user_address_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())){
            showToast(getStrRes(R.string.sms_code_hint));
            return false;
        }
        
        if (isDefault){
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
                            add(inputDialog.getContentView().getText().toString().trim());
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

    /**
     * 添加地址
     * @param tradePwd
     */
    private void add(String tradePwd) {
        Map<String, String> map = new HashMap<>();

        map.put("currency", addressType);
        map.put("token", SPUtilHelper.getUserToken());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("address", mBinding.tvAddress.getText().toString().trim());
        if (isDefault){
            map.put("isCerti", "1");
        }else {
            map.put("isCerti", "0");
        }
        map.put("smsCaptcha", mBinding.edtCode.getText().toString().trim());
        map.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        map.put("label", mBinding.edtTag.getText().toString().trim());
        map.put("tradePwd", tradePwd);


        Call call = RetrofitUtils.getBaseAPiService().successRequest("802170", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                showToast(getStrRes(R.string.user_title_address_add_success));
                finish();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


}
