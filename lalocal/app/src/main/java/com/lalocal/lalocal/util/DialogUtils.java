package com.lalocal.lalocal.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Created by wangjie on 2016/12/22.
 */

public class DialogUtils {

    /**
     * 显示常规对话框
     * @param context
     * @param iconId 标题图标
     * @param title
     * @param message
     * @param confirm 确认按钮文本
     * @param listener 确认按钮监听事件
     */
    public static void createNormalDialog(Context context, int iconId, String title, String message,
                                   String confirm, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (iconId > 0) {
            builder.setIcon(iconId);
        }

        if (TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.setMessage(message)
                .setPositiveButton(confirm, listener)
                .show();
    }

    /**
     * 创建列表对话框
     * @param context
     * @param iconId
     * @param title
     */
    public void createListDialog(Context context, int iconId, String title,
                                 String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (iconId > 0) {
            builder.setIcon(iconId);
        }

        if (TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.setItems(items, listener)
                .show();
    }

    /**
     * 单选按钮对话框
     * @param context
     * @param iconId
     * @param title
     * @param items
     * @param itemClickListener
     * @param confirm
     * @param confirmListener
     */
    public void createSingleChoiceListDialog(Context context, int iconId, String title, String[] items,
                                  DialogInterface.OnClickListener itemClickListener, String confirm, DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (iconId > 0) {
            builder.setIcon(iconId);
        }

        if (TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.setSingleChoiceItems(items, 0, itemClickListener)
                .setPositiveButton(confirm, confirmListener)
                .show();
    }

    /**
     * 创建多选按钮
     * @param context
     * @param iconId
     * @param title
     * @param items
     * @param checked 列表预选项
     * @param multiChoiceListener 多选监听事件
     * @param confirm
     * @param confirmListener
     */
    public void createMultiChoiceListDialog(Context context, int iconId, String title, String[] items, boolean[] checked,
                                            DialogInterface.OnMultiChoiceClickListener multiChoiceListener, String confirm,
                                            DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (iconId > 0) {
            builder.setIcon(iconId);
        }

        if (TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.setMultiChoiceItems(items, checked, multiChoiceListener)
                .setPositiveButton(confirm, confirmListener)
                .show();
    }

}
