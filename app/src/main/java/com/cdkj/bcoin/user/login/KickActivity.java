package com.cdkj.bcoin.user.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityKickBinding;
import com.cdkj.bcoin.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lei on 2018/1/23.
 */

public class KickActivity extends Activity {

    private ActivityKickBinding mBinding;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent= new Intent(context, KickActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(addMainView());

        afterCreate();
    }

    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_kick, null, false);
        return mBinding.getRoot();
    }

    public void afterCreate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(StringUtil.getString(R.string.attention))
                .setMessage(StringUtil.getString(R.string.kick_logout))
                .setPositiveButton(StringUtil.getString(R.string.confirm), (dialogInterface, i) -> {
                    SPUtilHelper.logOutClear();
                    EventBus.getDefault().post(EventTags.AllFINISH);

                    SignInActivity.open(KickActivity.this,true);
                    finish();

                })
                .setCancelable(false);

        builder.show();
    }
}
