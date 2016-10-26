package com.lalocal.lalocal.live.base.util;

import android.app.Dialog;

import java.util.Stack;

/**
 * Created by android on 2016/10/14.
 */
public class DialogUtil {
    public static DialogUtil appManager = null;

    private DialogUtil() {

    }
    public static DialogUtil getInstance() {
        if (appManager == null) {
            appManager = new DialogUtil();
        }
        return appManager;
    }
    private static Stack<Dialog> dialogStack = new Stack<Dialog>();//后进先出
    public static void addDialog(Dialog dialog) {
        dialogStack.add(dialog);
    }
    public static void removeCurrent() {
        if(dialogStack.size()>0){
            Dialog lastElement = dialogStack.lastElement();
            lastElement.dismiss();
            dialogStack.remove(lastElement);
        }
    }

    /**
     * 删除指定的dialog
     *
     * @param dialog
     */
    public static void removeDialog(Dialog dialog) {
        for (int i = dialogStack.size() - 1; i >= 0; i--) {
            if (dialogStack.get(i).getClass().equals(dialog.getClass())) {
                dialog.dismiss();
                dialogStack.remove(dialog);
                break;
            }
        }
    }

    public static void clear() {
        if(dialogStack.size()>0){
        for (Dialog dialog : dialogStack) {
            dialog.dismiss();
        }
        dialogStack.clear();
        }
    }


    public static int getSize() {
        return dialogStack.size();
    }
}
