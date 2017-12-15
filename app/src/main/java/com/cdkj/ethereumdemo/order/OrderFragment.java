package com.cdkj.ethereumdemo.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.databinding.FragmentOrderBinding;
import com.tencent.imsdk.TIMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_DONE;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_NEW;

/**
 * Created by lei on 2017/11/29.
 */

public class OrderFragment extends BaseLazyFragment {

    private FragmentOrderBinding mBinding;

    private List<Fragment> fragments;

    // 币种
    private String type = "ETH";

    private String[] types = {"ETH"};

    public static List<TIMConversation> conversationList;

    private static int ORDER_PAGE_INDEX = 0;

    /**
     * 获得fragment实例
     *
     * @return
             */
    public static OrderFragment getInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, null, false);

        initListener();
        initViewPager();

        return mBinding.getRoot();
    }

    private void initListener() {

        mBinding.llCoin.setOnClickListener(view -> {
            popupType(view);
        });

        mBinding.rlBtn1.setOnClickListener(view -> {
            setTitleBarBtnViewChange(0);
            setShowIndex(0);

            ORDER_PAGE_INDEX = 0;
        });

        mBinding.rlBtn2.setOnClickListener(view -> {
            setTitleBarBtnViewChange(1);
            setShowIndex(1);

            ORDER_PAGE_INDEX = 1;
        });
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mBinding.pagerMain.setPagingEnabled(false);//禁止左右切换

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(OrderNewFragment.getInstance());
        fragments.add(OrderDoneFragment.getInstance());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }

    /**
     * 设置要显示的界面
     *
     * @param index
     */
    private void setShowIndex(int index) {
        if (index < 0 && index >= fragments.size()) {
            return;
        }
        mBinding.pagerMain.setCurrentItem(index, false);
    }

    public void setTitleBarBtnViewChange(int location){

        // 初始化
        mBinding.tvBtn1.setTextColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.black));
        mBinding.vBtn1.setBackgroundColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.white));
        mBinding.tvBtn2.setTextColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.black));
        mBinding.vBtn2.setBackgroundColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.white));

        if (location == 0){
            mBinding.tvBtn1.setTextColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.colorPrimary));
            mBinding.vBtn1.setBackgroundColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.colorPrimary));
        }else {
            mBinding.tvBtn2.setTextColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.colorPrimary));
            mBinding.vBtn2.setBackgroundColor(ContextCompat.getColor(mActivity, com.cdkj.baselibrary.R.color.colorPrimary));
        }

    }

    @Override
    protected void lazyLoad() {
        // 更新订单列表消息状态
        EventBus.getDefault().post(EventTags.IM_MSG_UPDATE);
    }

    @Override
    protected void onInvisible() {

    }

    private void popupType(View view) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wallet_type, null);

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

        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private NumberPicker.OnValueChangeListener ChangedListener = (arg0, arg1, arg2) -> type = types[arg2];

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = fragments.get(fragments.size()-1);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Subscribe
    public void txImMsgUpdate(EventBusModel model) {
        if (model == null)
            return;

        if (model.getTag().equals(IM_MSG_TIP_NEW)){
            mBinding.ivMsgNewTip.setVisibility(model.getEvInt() == 0 ? View.GONE:View.VISIBLE);
        }

        if (model.getTag().equals(IM_MSG_TIP_DONE)){
            mBinding.ivMsgDoneTip.setVisibility(model.getEvInt() == 0 ? View.GONE:View.VISIBLE);
        }
    }
}
