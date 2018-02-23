package com.cdkj.bcoin.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baseim.util.VibratorUtil;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.FragmentOrderBinding;
import com.tencent.imsdk.TIMConversation;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_DONE;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_TIP_NEW;
import static com.cdkj.baselibrary.appmanager.EventTags.IM_MSG_VIBRATOR;

/**
 * Created by lei on 2017/11/29.
 */

public class OrderFragment extends BaseLazyFragment {

    private FragmentOrderBinding mBinding;

    private List<Fragment> fragments;

    // 币种
//    protected static String coinType;

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

        init();
        initListener();
        initViewPager();

        return mBinding.getRoot();
    }

    private void init() {
        // 订单列表不分币种，隐藏币种选择
        mBinding.llCoin.setVisibility(View.GONE);
        // 初始化查询币种
//        coinType = COIN_TYPE[0];
//        mBinding.tvCoin.setText(coinType);
    }


    private void initListener() {

//        mBinding.llCoin.setOnClickListener(view -> {
//            initPopup(view);
//        });

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

    }

    @Override
    protected void onInvisible() {

    }

//    /**
//     *
//     * @param view
//     */
//    private void initPopup(View view) {
//        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(mActivity, R.layout.popup_picker);
//        popupWindow.setNumberPicker(R.id.np_type, COIN_TYPE);
//
//        popupWindow.setOnClickListener(R.id.tv_cancel,v -> {
//            popupWindow.dismiss();
//        });
//
//        popupWindow.setOnClickListener(R.id.tv_confirm,v -> {
//            coinType = popupWindow.getNumberPicker(R.id.np_type, COIN_TYPE);
//            mBinding.tvCoin.setText(coinType);
//
//            doRefreshList();
//            popupWindow.dismiss();
//        });
//
//        popupWindow.show(view);
//    }

//    private void doRefreshList(){
//        EventBusModel model = new EventBusModel();
//        model.setTag(EventTags.ORDER_COIN_TYPE);
//        EventBus.getDefault().post(model);
//
//    }

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

    @Subscribe
    public void imMsgVibrator(String tag) {
        if (tag.equals(IM_MSG_VIBRATOR)){

            // 新消息震动提示
            long[] patter = {0, 350, 0, 350};
            VibratorUtil.vibrate(mActivity,patter,-1);

        }

    }

//    /**
//     * 根据交易的广告的coin查询对应的订单列表
//     * @param eventBusModel
//     */
//    @Subscribe
//    public void changeOrderCoin(EventBusModel eventBusModel) {
//        if (eventBusModel == null) {
//            return;
//        }
//
//        if (TextUtils.equals(eventBusModel.getTag(), MAIN_CHANGE_SHOW_INDEX)) {
//           if (eventBusModel.getEvInfo() != null && !eventBusModel.getEvInfo().equals("")){
//               coinType = eventBusModel.getEvInfo();
//               mBinding.tvCoin.setText(coinType);
//
//               doRefreshList();
//           }
//        }
//    }

}
