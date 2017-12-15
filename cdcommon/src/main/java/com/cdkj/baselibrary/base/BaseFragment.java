package com.cdkj.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;


/**
 * Fragment 基类
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    private List<Call> mCallList;
    protected LoadingDialog loadingDialog;
    protected CompositeDisposable mSubscription;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }
    @Subscribe
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mCallList = new ArrayList<>();
        mSubscription=new CompositeDisposable();
    }
    protected void addCall(Call call) {
        mCallList.add(call);
    }

    protected void clearCall() {

        for (Call call : mCallList) {
            if (call == null){
                continue;
            }
            call.cancel();
        }

    }

    /**
     * 隐藏Dialog
     */
    public void disMissLoading() {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.closeDialog();
        }
    }

    /**
     * 显示dialog
     */
    public void showLoadingDialog() {
        if(loadingDialog==null){
            loadingDialog=new LoadingDialog(mActivity);
        }

        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.showDialog();
        }
    }


    @Subscribe
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        clearCall();
        if (mSubscription != null){
            mSubscription.dispose();
            mSubscription.clear();
        }
        mActivity = null;
    }

    /**
     * 获取StringRes
     * @return StringRes
     */
    public String getStrRes(int resources){

        return getString(resources);
    }

}

