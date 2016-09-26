package com.lalocal.lalocal.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by xiaojw on 2016/9/13.
 */
public class KeyboardUtil {

    //显示软键盘
    public static void showSoftKey(EditText editText) {
        Context context = editText.getContext();
        editText.setCursorVisible(true);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(
                editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    //关闭软键盘
    public static void hidenSoftKey(EditText editText) {
        Context context = editText.getContext();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(),
                    0);
        }
    }

}