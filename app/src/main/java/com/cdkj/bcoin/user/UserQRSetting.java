package com.cdkj.bcoin.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityUserQrsettingBinding;
import com.cdkj.bcoin.model.ZfqrImage;
import com.cdkj.bcoin.util.AliOssUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 收款款设置  支付宝二维码  账号的设置
 */
public class UserQRSetting extends AbsBaseActivity {

    private ActivityUserQrsettingBinding mBinding;
    private int REQUEST_CODE = 1000;
    private String QRName;
    private String name;
    private String amount;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserQRSetting.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_qrsetting, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_qr));
        setTopLineState(true);
        setSubLeftImgState(true);

        initListener();

        initData();

    }

    /**
     * 复现数据
     */
    private void initData() {

        if (!TextUtils.isEmpty(SPUtilHelper.getZfbAccount())) {
            mBinding.etName.setText(SPUtilHelper.getZfbAccount());
        }
        if (!TextUtils.isEmpty(SPUtilHelper.getZfbQr())) {
            ImgUtils.loadImage(UserQRSetting.this, SPUtilHelper.getZfbQr(), mBinding.ivQr);
        }
        getData();
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        Call<BaseResponseModel<ZfqrImage>> qrData = RetrofitUtils.createApi(MyApi.class).getQrData("805970", StringUtils.getJsonToString(map));
        showLoadingDialog();

        qrData.enqueue(new BaseResponseModelCallBack<ZfqrImage>(this) {
            @Override
            protected void onSuccess(ZfqrImage data, String SucMessage) {
                List<ZfqrImage.ZfbQrListBean> zfbQrList = data.getZfbQrList();
                if (zfbQrList == null || zfbQrList.size() == 0) {
                    return;
                }
                setImageData(zfbQrList);
            }

            @Override
            protected void onFinish() {
                disMissLoading();

            }
        });
//

    }

    private void setImageData(List<ZfqrImage.ZfbQrListBean> data) {
        for (ZfqrImage.ZfbQrListBean item : data) {

            if (TextUtils.isEmpty(item.getZfbQrUrl())) {
                continue;
            }
            switch (item.getAmount()) {
                case "0":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg11);
                    break;
                case "100":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg12);
                    amount = "100";
                    break;
                case "200":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg13);
                    amount = "200";

                    break;
                case "300":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg21);
                    amount = "300";
                    break;
                case "400":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg22);
                    amount = "400";
                    break;
                case "500":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg23);
                    amount = "500";
                    break;
                case "1000":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg31);
                    amount = "1000";
                    break;
                case "2000":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg32);
                    amount = "2000";
                    break;
                case "5000":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg33);
                    amount = "5000";
                    break;
                case "10000":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg41);
                    amount = "10000";
                    break;
                case "20000":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg42);
                    amount = "20000";
                    break;
                case "50000":
                    ImgUtils.loadImage(UserQRSetting.this, item.getZfbQrUrl(), mBinding.ivImg43);
                    amount = "50000";
                    break;
            }

        }

    }

    private void initListener() {
        mBinding.btnConfirm.setOnClickListener(v -> {
            if (check()) {
                //提交
                submit();
            }
        });
        mBinding.ivQr.setOnClickListener(v -> {
            ImageSelectActivity.launch(UserQRSetting.this, REQUEST_CODE);
        });
        mBinding.ivImg11.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 11));
        mBinding.ivImg12.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 12));
        mBinding.ivImg13.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 13));
        mBinding.ivImg21.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 21));
        mBinding.ivImg22.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 22));
        mBinding.ivImg23.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 23));
        mBinding.ivImg31.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 31));
        mBinding.ivImg32.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 32));
        mBinding.ivImg33.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 33));
        mBinding.ivImg41.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 41));
        mBinding.ivImg42.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 42));
        mBinding.ivImg43.setOnClickListener(v -> ImageSelectActivity.launch(UserQRSetting.this, 43));
        ;
    }

    private Boolean check() {
        name = mBinding.etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(this, "请填写收款账号");
            return false;
        }
        if (TextUtils.isEmpty(QRName)) {
            ToastUtil.show(this, "请上传收款码");
            return false;
        }

        return true;
    }

    //二维码设置接口
    private void submit() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("zfbAccount", name);//账号
        map.put("zfbQr", QRName);//二维码
        Call<BaseResponseModel<IsSuccessModes>> baseResponseModelCall = RetrofitUtils.getBaseAPiService().successRequest("805097", StringUtils.getJsonToString(map));
        showLoadingDialog();
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    ToastUtil.show(UserQRSetting.this, "成功");
                    SPUtilHelper.saveZfbAccount(mBinding.etName.getText().toString());
                    SPUtilHelper.saveZfbQr(QRName);
                    finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        String path = data.getStringExtra(ImageSelectActivity.staticPath);
//
        new AliOssUtils(this).getAliURL(new AliOssUtils.AliUpLoadBack() {
            @Override
            public void onSuccess(String name, String etag, String requestId) {
//                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivQr);
//                QRName = name;
                setImage(requestCode, name);
            }

            @Override
            public void onFal(String info) {
                ToastUtil.show(UserQRSetting.this, "出错了" + info);
            }
        }, path);


//        if (requestCode == REQUEST_CODE) {
//            String path = data.getStringExtra(ImageSelectActivity.staticPath);
//
//            new AliOssUtils(this).getAliURL(new AliOssUtils.AliUpLoadBack() {
//                @Override
//                public void onSuccess(String name, String etag, String requestId) {
//                    ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivQr);
//                    QRName = name;
//                }
//
//                @Override
//                public void onFal(String info) {
//                    ToastUtil.show(UserQRSetting.this, "出错了" + info);
//                }
//            }, path);
//        }
    }

    /**
     * s设置image
     *
     * @param requestCode
     * @param name
     */
    private void setImage(int requestCode, String name) {
        switch (requestCode) {
            case 11:
                amount = "";
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg11);
                break;
            case 12:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg12);
                amount = "100";
                break;
            case 13:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg13);
                amount = "200";

                break;
            case 21:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg21);
                amount = "300";
                break;
            case 22:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg22);
                amount = "400";
                break;
            case 23:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg23);
                amount = "500";
                break;
            case 31:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg31);
                amount = "1000";
                break;
            case 32:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg32);
                amount = "2000";
                break;
            case 33:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg33);
                amount = "5000";
                break;
            case 41:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg41);
                amount = "10000";
                break;
            case 42:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg42);
                amount = "20000";
                break;
            case 43:
                ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivImg43);
                amount = "50000";
                break;
        }
        upLoad(name);
    }

    private void upLoad(String name) {
        HashMap map = new HashMap();
        map.put("amount", amount);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("zfbQr", name);
        if (TextUtils.isEmpty(amount)) {
            map.put("zfbAccount", "支付宝");
        }
        Call<BaseResponseModel<IsSuccessModes>> baseResponseModelCall = RetrofitUtils.getBaseAPiService().successRequest("805097", StringUtils.getJsonToString(map));
        showLoadingDialog();
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    ToastUtil.show(UserQRSetting.this, "成功");
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
