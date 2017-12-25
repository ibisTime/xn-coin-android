package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cdkj.baseim.interfaces.TxImLogoutInterface;
import com.cdkj.baseim.interfaces.TxImLogoutPresenter;
import com.cdkj.baselibrary.activitys.AuthenticateActivity;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.activitys.UpdatePhoneActivity;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserSettingBinding;
import com.cdkj.bcoin.databinding.PopupGoogleBinding;
import com.cdkj.bcoin.user.login.SignInActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lei on 2017/11/1.
 */

public class UserSettingActivity extends AbsBaseActivity implements TxImLogoutInterface {

    private TxImLogoutPresenter mPresenter;

    private ActivityUserSettingBinding mBinding;

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserSettingActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_setting, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_setting));
        setTopLineState(true);
        setSubLeftImgState(true);


        mPresenter = new TxImLogoutPresenter(this);

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        mBinding.tvMail.setText(SPUtilHelper.getUserEmail());
        if (SPUtilHelper.getRealName() != null && SPUtilHelper.getRealName().length()>1){
            String name = "";
            for (int i = 1; i<SPUtilHelper.getRealName().length(); i++){
                name += "*";
            }
            mBinding.tvIdentity.setText(name+SPUtilHelper.getRealName().substring(SPUtilHelper.getRealName().length()-1,SPUtilHelper.getRealName().length()));
        }
        mBinding.tvMobile.setText(SPUtilHelper.getUserPhoneNum().substring(0,3)+"****"+SPUtilHelper.getUserPhoneNum().substring(SPUtilHelper.getUserPhoneNum().length()-4, SPUtilHelper.getUserPhoneNum().length()));

        if (!SPUtilHelper.getGoogleAuthFlag()) { // 未打开谷歌验证
            mBinding.tvGoogle.setText(getStrRes(R.string.user_google_close));
        }else {
            mBinding.tvGoogle.setText(getStrRes(R.string.user_google_open));
        }
    }

    private void initListener() {
        mBinding.llTradePwd.setOnClickListener(view -> {
            PayPwdModifyActivity.open(this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
        });

        mBinding.llIdentity.setOnClickListener(view -> {
            if (SPUtilHelper.getRealName() == null || SPUtilHelper.getRealName().equals("")){
                AuthenticateActivity.open(this);
            }else {
                showToast(getStrRes(R.string.user_identity_success));
            }
        });

        mBinding.llMail.setOnClickListener(view -> {
            UserEmailActivity.open(this, SPUtilHelper.getUserEmail());
        });

        mBinding.llMobile.setOnClickListener(view -> {
            UpdatePhoneActivity.open(this);
        });

        mBinding.llPassword.setOnClickListener(view -> {
            UserPasswordActivity.open(this);
        });

        mBinding.llGoogle.setOnClickListener(view -> {
            if (!SPUtilHelper.getGoogleAuthFlag()) { // 未打开谷歌验证
                UserGoogleActivity.open(this, "open");
            }else {
                popupType(view);
            }
        });

        mBinding.llLanguage.setOnClickListener(view -> {
            UserLanguageActivity.open(this);
        });


        mBinding.btnConfirm.setOnClickListener(view -> {
            mPresenter.logout();
        });
    }


    @Override
    public void emptyLoginUser() {
        SPUtilHelper.logOutClear();
        EventBus.getDefault().post(EventTags.AllFINISH);

        SignInActivity.open(UserSettingActivity.this,true);
        finish();
    }

    @Override
    public void onError(int code, String desc) {
        SPUtilHelper.logOutClear();
        EventBus.getDefault().post(EventTags.AllFINISH);

        SignInActivity.open(UserSettingActivity.this,true);
        finish();
    }

    @Override
    public void onSuccess() {
        SPUtilHelper.logOutClear();
        EventBus.getDefault().post(EventTags.AllFINISH);

        SignInActivity.open(UserSettingActivity.this,true);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }

    private void popupType(View view) {
        PopupGoogleBinding popupBinding;

        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_google, null, false);

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

        popupBinding.tvClose.setOnClickListener(v -> {
            popupWindow.dismiss();
            UserGoogleActivity.open(this, "close");
        });

        popupBinding.tvModify.setOnClickListener(v -> {
            popupWindow.dismiss();
            UserGoogleActivity.open(this, "modify");
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }
}
