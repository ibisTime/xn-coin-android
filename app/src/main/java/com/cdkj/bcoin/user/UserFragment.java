package com.cdkj.bcoin.user;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.QiNiuUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.FragmentUserBinding;
import com.cdkj.bcoin.deal.PublishBuyActivity;
import com.cdkj.bcoin.deal.SaleActivity;
import com.cdkj.bcoin.util.AccountUtil;
import com.qiniu.android.http.ResponseInfo;
import com.zendesk.sdk.support.SupportActivity;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.bcoin.util.DealUtil.DAIFABU;


/**
 * Created by lei on 2017/8/21.
 */

public class UserFragment extends BaseLazyFragment {

    private FragmentUserBinding mBinding;

    public final int PHOTOFLAG = 110;


    /**
     * 获得fragment实例
     *
     * @return
     */
    public static UserFragment getInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, null, false);

        initListener();

        return mBinding.getRoot();
    }


    private void initListener() {

        mBinding.rlPhoto.setOnClickListener(view -> {
            ImageSelectActivity.launch(mActivity, PHOTOFLAG);
        });

        mBinding.llBuy.setOnClickListener(view -> {
            PublishBuyActivity.open(mActivity,DAIFABU,null);
        });

        mBinding.llSale.setOnClickListener(view -> {
            SaleActivity.open(mActivity,DAIFABU,null);
        });

        mBinding.llAdv.setOnClickListener(view -> {
            UserPublishedActivity.open(mActivity);
        });

        mBinding.llAddress.setOnClickListener(view -> {
            UserAddressActivity.open(mActivity, null,null);
        });

        mBinding.llTrust.setOnClickListener(view -> {
            UserTrustActivity.open(mActivity);
        });

        mBinding.llInvite.setOnClickListener(view -> {
            UserInviteActivity.open(mActivity);
        });

        mBinding.llSetting.setOnClickListener(view -> {
            UserSettingActivity.open(mActivity);
        });

        mBinding.llTip.setOnClickListener(view -> {
            UserTipActivity.open(mActivity);
        });

        mBinding.llIssue.setOnClickListener(view -> {
//            WebViewActivity.openkey(mActivity, mBinding.tvIssue.getText().toString(),"questions");
            new SupportActivity.Builder().show(getActivity());
        });

        mBinding.llServices.setOnClickListener(view -> {
//            WebViewActivity.openkey(mActivity, mBinding.tvServices.getText().toString(),"service");

            VisitorInfo visitorData = new VisitorInfo.Builder()
                    .name(SPUtilHelper.getUserName())
                    .email(SPUtilHelper.getUserEmail())
                    .phoneNumber(SPUtilHelper.getUserPhoto())
                    .build();

            ZopimChat.setVisitorInfo(visitorData);
            startActivity(new Intent(mActivity, ZopimChatActivity.class));

        });

        mBinding.llAbout.setOnClickListener(view -> {
//            WebViewActivity.openkey(mActivity, mBinding.tvAbout.getText().toString(),"about_us");
            UserAboutActivity.open(mActivity);
        });
    }

    @Override
    protected void lazyLoad() {
        if (mBinding != null) {
            getUserInfoRequest();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!SPUtilHelper.getUserId().equals("")){
            // 已登陆时初始化登录用户的用户信息
            getUserInfoRequest();
        }

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PHOTOFLAG) {
            String path = data.getStringExtra(ImageSelectActivity.staticPath);
            new QiNiuUtil(mActivity).getQiniuURL(new QiNiuUtil.QiNiuCallBack() {
                @Override
                public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                    updateUserPhoto(key);
                }

                @Override
                public void onFal(String info) {
                }
            }, path);

        }
    }

    /**
     * 更新用户头像
     * @param key
     */
    private void updateUserPhoto(final String key) {
        Map<String, String> map = new HashMap<>();
        map.put("photo", key);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getJsonToString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    getUserInfoRequest();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest() {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(mActivity) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                if (data == null)
                    return;

                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setShowData(UserInfoModel data) {
        if (data == null) return;

        SPUtilHelper.saveSecretUserId(data.getSecretUserId());

        SPUtilHelper.saveUserPhoto(data.getPhoto());
        SPUtilHelper.saveUserEmail(data.getEmail());
        SPUtilHelper.saveUserName(data.getNickname());
        SPUtilHelper.saveRealName(data.getRealName());
        SPUtilHelper.saveUserPhoneNum(data.getMobile());
        SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
        SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());

        if (data.getNickname() == null)
            return;

        mBinding.tvNick.setText(data.getNickname());
        ImgUtils.loadAvatar(mActivity, data.getPhoto(), data.getNickname(), mBinding.imAvatar, mBinding.tvAvatar);

        if (data.getUserStatistics() == null)
            return;
        mBinding.tvDeal.setText(getStrRes(R.string.deal)+data.getUserStatistics().getJiaoYiCount()+"");
        mBinding.tvTrust.setText(getStrRes(R.string.trust)+data.getUserStatistics().getBeiXinRenCount()+"");
        if(data.getUserStatistics().getBeiPingJiaCount() == 0){
            mBinding.tvGood.setText(getStrRes(R.string.good)+"0%");
        }else {
            double hpRate = data.getUserStatistics().getBeiHaoPingCount() / data.getUserStatistics().getBeiPingJiaCount();
            mBinding.tvGood.setText(getStrRes(R.string.good)+AccountUtil.formatInt(hpRate * 100)+"%");
        }

        if (data.getLevel()!= null){
            mBinding.llAgent.setVisibility(data.getLevel().equals("2") ? View.VISIBLE : View.GONE);
        }
    }


}
