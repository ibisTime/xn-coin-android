package com.cdkj.baselibrary.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import com.cdkj.baselibrary.R;

/**
 * Created by lei on 2018/1/2.
 */

public class MyPickerPopupWindow extends PopupWindow {

    private View contentView;
    private Context context;

    public MyPickerPopupWindow(Context context, int resource){
        super(context);
        this.context = context;
        this.init(resource);
    }


    private void init(int resource){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(resource, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_popup));
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

    }

    public void setNumberPicker(int viewId, String[] str){
        NumberPicker numberPicker = (NumberPicker) contentView.findViewById(viewId);
        numberPicker.setDisplayedValues(str);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(str.length - 1);
        // 禁止输入
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public String getNumberPicker(int viewId, String[] str){
        NumberPicker numberPicker = (NumberPicker) contentView.findViewById(viewId);
        return str[numberPicker.getValue()];
    }

    public int getNumberPickerValue(int viewId){
        NumberPicker numberPicker = (NumberPicker) contentView.findViewById(viewId);
        return numberPicker.getValue();
    }

    public <T extends View> T getView(int viewId) {
        View view = contentView.findViewById(viewId);
        return (T) view;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener){
        View view = getView(viewId);
        view.setOnClickListener(listener);
    }

    /**
     * 显示popupWindow
     * @param parent
     */
    public void show(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
            this.showAtLocation(parent, Gravity.CENTER, 0, 50);
        } else {
            this.dismiss();
        }
    }

}
