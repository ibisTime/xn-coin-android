package com.cdkj.baselibrary.utils;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2016-04-01.
 */
public class InputMethodUtils {

    /**
     * 弹出输入法窗口
     */
    public static void popupInputMethodWindow(final EditText et_search) {

        et_search.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager  imm = (InputMethodManager) et_search.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        },250);
    }

    /** 显示软键盘 */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 显示软键盘 */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /** 多少时间后显示软键盘 */
    public static void showInputMethod(final View view, long delayMillis) {
        // 显示输入法
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodUtils.showInputMethod(view);
            }
        }, delayMillis);
    }
}