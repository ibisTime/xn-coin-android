package com.cdkj.ethereumdemo.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.adapter.PublishedAdapter;
import com.cdkj.ethereumdemo.api.MyApi;
import com.cdkj.ethereumdemo.databinding.ActivityUserPublishedBinding;
import com.cdkj.ethereumdemo.deal.DealActivity;
import com.cdkj.ethereumdemo.deal.PublishBuyActivity;
import com.cdkj.ethereumdemo.deal.SaleActivity;
import com.cdkj.ethereumdemo.model.DealDetailModel;
import com.cdkj.ethereumdemo.model.DealModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.ethereumdemo.util.DealUtil.CAOGAO;

/**
 * Created by lei on 2017/11/1.
 */

public class UserPublishedActivity extends BaseRefreshActivity<DealDetailModel> {

    private ActivityUserPublishedBinding mBinding;

    private List<String> statusList = new ArrayList<>();

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserPublishedActivity.class));
    }
    
    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_published, null, false);

        setTopTitle(getStrRes(R.string.user_title_published));
        setTopLineState(true);
        setSubLeftImgState(true);

        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mBinding.getRoot());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            DealDetailModel model = (DealDetailModel) adapter.getItem(position);

            if (model.getStatus().equals("0")){ //打开广告编辑
                if (model.getTradeType().equals("0")){ // 买币
                    PublishBuyActivity.open(this, CAOGAO, model);
                }else {
                    SaleActivity.open(this, CAOGAO, model);
                }
            }else {
                DealActivity.open(this, model.getCode());
            }

        });

        initListener();

        // 初始化
        statusList.add("0");
        getListData(pageIndex,limit,true);
    }

    private void initListener() {

        mBinding.rlNotYet.setOnClickListener(view -> {
            initView();

            mBinding.tvNotYet.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mBinding.vNotYet.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

            statusList.clear();
            statusList.add("0");
            getListData(1,10,true);
        });

        mBinding.rlYet.setOnClickListener(view -> {
            initView();

            mBinding.tvYet.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mBinding.vYet.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

            statusList.clear();
            statusList.add("1");
            statusList.add("2");
            statusList.add("3");
            getListData(1,10,false);
        });

    }

    private void initView() {
        mBinding.tvNotYet.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBinding.vNotYet.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        mBinding.tvYet.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBinding.vYet.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, Object> map = new HashMap<>();
        map.put("coin", "ETH");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("statusList", statusList);
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getDeal("625227", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealModel>(this) {

            @Override
            protected void onSuccess(DealModel data, String SucMessage) {
                if (data == null)
                    return;


                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<DealDetailModel> mDataList) {
        return new PublishedAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.user_published_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onMRefresh(1,10);
    }
}
