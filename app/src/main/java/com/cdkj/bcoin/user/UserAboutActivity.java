package com.cdkj.bcoin.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityUserAboutBinding;
import com.cdkj.bcoin.model.VersionModel;
import com.cdkj.bcoin.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.bcoin.util.UpdateUtil.startWeb;

/**
 * Created by lei on 2018/1/5.
 */

public class UserAboutActivity extends AbsBaseActivity {

    private ActivityUserAboutBinding mBinding;

    private String msg;
    private String url;
    private String force;


    public static void open(Context context){
        if (context == null)
            return;

        context.startActivity(new Intent(context, UserAboutActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_about, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_about));
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        getVersion();
    }

    private void init() {
        mBinding.tvAppName.setText(getStrRes(R.string.app_name));
        mBinding.tvVersion.setText("v"+getVersionName());
        mBinding.tvFuhao.setText("@");

        mBinding.tvUpdate.setOnClickListener(view -> {
            if (mBinding.tvUpdate.getText().toString().equals(getString(R.string.user_about_update))){
                update();
            }
        });
    }

    /**
     * 获取最新版本
     * @return
     */
    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "android-c");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("625918", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(this) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null)
                    return;

                if (data.getVersion().equals(getVersionName())) {
                    mBinding.tvUpdate.setText(getString(R.string.user_about_updated));
                }else {
                    msg = data.getNote();
                    url = data.getDownloadUrl();
                    force = data.getForceUpdate();

                    mBinding.tvUpdate.setText(getString(R.string.user_about_update));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void update() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(StringUtil.getString(R.string.tip))
                .setMessage(msg)
                .setPositiveButton(StringUtil.getString(R.string.confirm), (dialogInterface, i) -> {

                    startWeb(UserAboutActivity.this, url);
                    EventBus.getDefault().post(EventTags.AllFINISH);
                    finish();

                })
                .setCancelable(false);


        if(force.equals("1")){ // 强制更新
            builder.show();
        }else {
            builder.setNegativeButton(StringUtil.getString(R.string.cancel), null).show();
        }
    }
}
