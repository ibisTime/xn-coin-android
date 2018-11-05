package com.cdkj.bcoin.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.adapter.CoinRecyclerAdapter;
import com.cdkj.bcoin.databinding.FragmentOrderBinding;
import com.tencent.imsdk.TIMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.EventTags.ORDER_COIN_DONE_TIP;
import static com.cdkj.baselibrary.appmanager.EventTags.ORDER_COIN_NOW_TIP;
import static com.cdkj.bcoin.util.CoinUtil.getAllCoinList;

/**
 * Created by lei on 2017/11/29.
 */

public class OrderFragment extends BaseLazyFragment {

    private FragmentOrderBinding mBinding;

    private List<BaseCoinModel> list = new ArrayList<>();
    private CoinRecyclerAdapter coinRecyclerAdapter;

    List<String> nowUnreadCoinList = new ArrayList<>();
    List<String> doneUnreadCoinList = new ArrayList<>();

    private List<Fragment> fragments;

    // 币种
    protected static String coinType;

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
        initRecyclerView();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("onDestroy()","onDestroy()");
    }

    private void init() {
        list.clear();
        list.addAll(getAllCoinList());

        for (BaseCoinModel model : list){
            Log.e("isChoose",model.isChoose()+"");
        }

        if (list.size()>0)
            coinType = list.get(0).getSymbol();
    }


    private void initListener() {

        mBinding.flBack.setOnClickListener(view -> {
            // 更新订单列表消息状态
            EventBus.getDefault().post(EventTags.ORDER_CLOSE);
        });

        mBinding.llNow.setOnClickListener(view -> {
            ORDER_PAGE_INDEX = 0;

            setShowIndex(ORDER_PAGE_INDEX);
            setTitleBarBtnViewChange(ORDER_PAGE_INDEX);
            recyclerTip(ORDER_PAGE_INDEX);
        });

        mBinding.llDone.setOnClickListener(view -> {
            ORDER_PAGE_INDEX = 1;

            setShowIndex(ORDER_PAGE_INDEX);
            setTitleBarBtnViewChange(ORDER_PAGE_INDEX);
            recyclerTip(ORDER_PAGE_INDEX);
        });
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mBinding.pagerMain.setPagingEnabled(false);//禁止左右切换

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(OrderNewFragment.getInstance());//待处理
        fragments.add(OrderDoneFragment.getInstance());//一结束

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }


    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.rvCoin.setLayoutManager(linearLayoutManager);
        //设置适配器
        coinRecyclerAdapter = new CoinRecyclerAdapter(list);
        mBinding.rvCoin.setAdapter(coinRecyclerAdapter);

        coinRecyclerAdapter.setOnItemClickListener((adapter, view, position) -> {
            // 改变UI
            for (BaseCoinModel model : list) {
                model.setChoose(false);
            }
            list.get(position).setChoose(true);
            coinRecyclerAdapter.notifyDataSetChanged();

            // 储存选择的币种
            coinType = list.get(position).getSymbol();
            // 通知子Fragment刷新列表
            doRefreshList();
        });
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
        mBinding.tvDone.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        mBinding.tvDoneTipNum.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        mBinding.ivMsgDoneTip.setBackground(getResources().getDrawable(R.drawable.corner_im_msg_tip));
        mBinding.llDone.setBackground(getResources().getDrawable(R.drawable.corner_order_title_bar_right_white));

        mBinding.tvNow.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        mBinding.tvNowTipNum.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        mBinding.ivMsgNowTip.setBackground(getResources().getDrawable(R.drawable.corner_im_msg_tip));
        mBinding.llNow.setBackground(getResources().getDrawable(R.drawable.corner_order_title_bar_left_white));

        if (location == 0){
            mBinding.tvNow.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.tvNowTipNum.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.llNow.setBackground(getResources().getDrawable(R.drawable.corner_order_title_bar_left));
            mBinding.ivMsgNowTip.setBackground(getResources().getDrawable(R.drawable.corner_im_msg_tip_white));
        }else {
            mBinding.tvDone.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.tvDoneTipNum.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.llDone.setBackground(getResources().getDrawable(R.drawable.corner_order_title_bar_right));
            mBinding.ivMsgDoneTip.setBackground(getResources().getDrawable(R.drawable.corner_im_msg_tip_white));
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

    private void doRefreshList(){
        EventBusModel model = new EventBusModel();
        model.setTag(EventTags.ORDER_COIN_TYPE);
        EventBus.getDefault().post(model);

    }

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

//        if (model.getTag().equals(IM_MSG_TIP_NEW)){
//            mBinding.ivMsgNowTip.setVisibility(model.getEvInt() == 0 ? View.GONE:View.VISIBLE);
//            mBinding.tvNowTipNum.setText(model.getEvInt() == 0 ? "" :"("+model.getEvInt()+")");
//        }
//
//        if (model.getTag().equals(IM_MSG_TIP_DONE)){
//            mBinding.ivMsgDoneTip.setVisibility(model.getEvInt() == 0 ? View.GONE:View.VISIBLE);
//            mBinding.tvDoneTipNum.setText(model.getEvInt() == 0 ? "" :"("+model.getEvInt()+")");
//        }
    }

    @Subscribe
    public void nowTxImMsgUpdate(EventBusModel model) {
        if (model == null)
            return;

        if (model.getTag().equals(ORDER_COIN_NOW_TIP)){

            nowUnreadCoinList.clear();
            nowUnreadCoinList.addAll(model.getList());
            mBinding.ivMsgNowTip.setVisibility(model.getList().size() == 0 ? View.GONE:View.VISIBLE);

        }else if(model.getTag().equals(ORDER_COIN_DONE_TIP)) {

            doneUnreadCoinList.clear();
            doneUnreadCoinList.addAll(model.getList());
            mBinding.ivMsgDoneTip.setVisibility(model.getList().size() == 0 ? View.GONE:View.VISIBLE);

        }

        recyclerTip(ORDER_PAGE_INDEX);

    }

    public void recyclerTip(int fragmentPageIndex){

        List<String> list;

        if (fragmentPageIndex == 0){
            list = nowUnreadCoinList;
        }else {
            list = doneUnreadCoinList;
        }

        for (BaseCoinModel coinModel : this.list){
            coinModel.setShowTip(false);
        }

        for (BaseCoinModel coinModel : this.list){
            for (String coin : list){
                if (coin.equals(coinModel.getSymbol())){
                    coinModel.setShowTip(true);
                }
            }
        }

        // 刷新RecyclerView
        coinRecyclerAdapter.notifyDataSetChanged();
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
