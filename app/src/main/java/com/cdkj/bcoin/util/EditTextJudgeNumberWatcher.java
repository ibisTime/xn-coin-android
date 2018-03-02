package com.cdkj.bcoin.util;

/**
 * Created by lei on 2018/1/18.
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @Description:EditText内容输入限制最大：小数点前五位，小数点后2位
 */

public class EditTextJudgeNumberWatcher implements TextWatcher {

    private int font = 10;
    private int after = 8;
    private EditText editText;

    /**
     *
     * @param editText
     * @param font 限制小数点前的位数
     * @param after 限制小数点后的位数
     */
    public EditTextJudgeNumberWatcher(EditText editText, int font, int after) {
        this.editText = editText;
        this.font = font;
        this.after = after;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        judgeNumber(editable,editText,font,after);
    }

    /**
     * 金额输入框中的内容限制（最大：小数点前 front 位，小数点后 after 位）
     *
     * @param edt
     */
    public static void judgeNumber(Editable edt,EditText editText,int font, int after) {

        String temp = edt.toString();
        int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
        int index = editText.getSelectionStart();//获取光标位置
        if (posDot == 0) {//必须先输入数字后才能输入小数点
            // 删除所有字符
            edt.delete(0, temp.length());
            return;
        }
        if (posDot < 0) {//不包含小数点
            if (temp.length() <= font) {
                return;//小于五位数直接返回
            } else {
                edt.delete(index-1, index);//删除光标前的字符
                return;
            }
        }
        if (posDot > font) {//小数点前大于5位数就删除光标前一位
            edt.delete(index-1, index);//删除光标前的字符
            return;
        }
        if (temp.length() - posDot - 1 > after){ //如果包含小数点
            edt.delete(index-1, index);//删除光标前的字符
            return;
        }
    }
}