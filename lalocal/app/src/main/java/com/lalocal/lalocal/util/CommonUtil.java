package com.lalocal.lalocal.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaojw on 2016/6/6.
 */
public class CommonUtil {
    private static final String DEVICE_ANDROID = "android";
    private static double EARTH_RADIUS = 6378.137;
    private static final int READ_PHONE_STATE_CODE = 112;

    public static int RESULT_DIALOG = 0;
    public static int REMIND_BACK = 0;

    public  static  String LONGITUDE="";
    public static   String LATITUDE="";

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    //textview设置不同前景色
    public static SpannableString getSpannelStyle(Context context, String text, int appearance, int start, int end) {
        SpannableString spannableStr = new SpannableString(text);
        spannableStr.setSpan(new TextAppearanceSpan(context, appearance), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStr;
    }


    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     *
     * @param lng1 loc1经度
     * @param lat1 loc1纬度
     * @param lng2 loc2经度
     * @param lat2 loc2纬度
     * @return
     */
    public static String getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = (double) (Math.round(distance * 10)) / 10;
        return String.valueOf(distance);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static String formartNum(long price) {
        NumberFormat nf = new DecimalFormat("###,###,###,###,###,###,###,###");
        return nf.format(price);
    }

    public static String formartNum(double price) {
        NumberFormat nf = new DecimalFormat("###,###,###,###,###,###,###,###.##");
        return nf.format(price);
    }

    public static String formartOrderPrice(double price) {
        NumberFormat nf = new DecimalFormat("¥ ###,###,###,###,###,###,###,###.##");
        return nf.format(price);
    }

    public static String fomartStartOrderPrice(double price) {
        return formartOrderPrice(price) + " 起";
    }

    //验证邮箱格式
    public static boolean checkEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    //验证密码长度
    public static boolean checkPassword(String psw) {
        int len = psw.length();
        if (psw != null && len >= 6 && len <= 20) {
            return true;
        }
        return false;
    }

    //验证昵称长度
    public static boolean checkNickname(String nickname) {
        if (!TextUtils.isEmpty(nickname) && nickname.length() < 20) {
            return true;
        }
        return false;
    }

    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
//        String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        return deviceUuid.toString();
    }

    public static String getDevice() {
        return DEVICE_ANDROID;
    }


    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() < 1) {
            return true;
        } else {
            return false;
        }

    }

    public static void showPromptDialog(Context context, String message, CustomDialog.CustomDialogListener listener) {

        CustomDialog dialog = new CustomDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle(context.getResources().getString(R.string.prompt));
        dialog.setNeturalBtn(context.getResources().getString(R.string.sure), listener);
        dialog.setMessage(message);
        dialog.show();
    }


    public static void showToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public static final int AD_DOT = 0x01;
    public static final int DARK_DOT = 0x02;
    public static final int WHITE_DOT = 0x03;

    /**
     * 初始化小点
     *
     * @param context
     * @param viewPager
     * @param dotContainer
     * @param size
     * @param selected
     * @return
     */
    public static List<Button> initDot(Context context, final ViewPager viewPager, LinearLayout dotContainer, int size, int selected, final int type) {
        final List<Button> dotBtns = new ArrayList<>();
        if (size > 0 && selected >= 0 && selected < size) {
            // 移除所有视图
            ((ViewGroup) dotContainer).removeAllViews();

            int width = 0;
            int height = 0;
            int marginHorizontal = 0;
            int marginVertical = 0;
            int selectedResId = 0;
            int normalResId = 0;

            if (type == AD_DOT) {
                width = (int) context.getResources().getDimension(R.dimen.dot_rect_width);
                height = (int) context.getResources().getDimension(R.dimen.dot_rect_height);

                marginHorizontal = DensityUtil.dip2px(context, 2);
                marginVertical = DensityUtil.dip2px(context, 10);

                selectedResId = R.color.black;
                normalResId = R.color.color_761a1a1a;
            } else if (type == DARK_DOT) {
                width = (int) context.getResources().getDimension(R.dimen.dot_size);
                ;
                height = width;

                marginHorizontal = DensityUtil.dip2px(context, 4);
                marginVertical = DensityUtil.dip2px(context, 15);

                selectedResId = R.drawable.icon_dark_dot_selected;
                normalResId = R.drawable.icon_dark_dot_normal;
            } else if (type == WHITE_DOT) {
                width = (int) context.getResources().getDimension(R.dimen.dot_size);
                ;
                height = width;

                marginHorizontal = DensityUtil.dip2px(context, 4);
                marginVertical = DensityUtil.dip2px(context, 15);

                selectedResId = R.drawable.icon_white_dot_selected;
                normalResId = R.drawable.icon_white_dot_normal;
            }

            for (int i = 0; i < size; i++) {
                // 新建一个按钮
                Button btn = new Button(context);
                // 点的大小
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                // 设置点的边距
                params.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
                // 设置按钮的大小属性
                btn.setLayoutParams(params);
                if (i == selected) {
                    btn.setBackgroundResource(selectedResId);
                } else {
                    btn.setBackgroundResource(normalResId);
                }
                dotBtns.add(btn);
                dotContainer.addView(btn);
            }

            for (int i = 0; i < dotBtns.size(); i++) {
                final int finalI = i;
                dotBtns.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(finalI);
                        selectDotBtn(dotBtns, finalI, type);
                    }
                });
            }
        }
        return dotBtns;
    }

    /**
     * 初始化小点
     *
     * @param context
     * @param sliderLayout
     * @param dotContainer
     * @param size
     * @param selected
     * @return
     */
    public static List<Button> initDot(Context context, final SliderLayout sliderLayout, LinearLayout dotContainer, int size, int selected, final int type) {
        final List<Button> dotBtns = new ArrayList<>();
        if (size > 0 && selected >= 0 && selected < size) {
            // 移除所有视图
            ((ViewGroup) dotContainer).removeAllViews();

            int width = 0;
            int height = 0;
            int marginHorizontal = 0;
            int marginVertical = 0;
            int selectedResId = 0;
            int normalResId = 0;

            if (type == AD_DOT) {
                width = (int) context.getResources().getDimension(R.dimen.dot_rect_width);
                height = (int) context.getResources().getDimension(R.dimen.dot_rect_height);

                marginHorizontal = DensityUtil.dip2px(context, 2);
                marginVertical = DensityUtil.dip2px(context, 10);

                selectedResId = R.color.black;
                normalResId = R.color.color_761a1a1a;
            } else if (type == DARK_DOT) {
                width = (int) context.getResources().getDimension(R.dimen.dot_size);
                ;
                height = width;

                marginHorizontal = DensityUtil.dip2px(context, 4);
                marginVertical = DensityUtil.dip2px(context, 15);

                selectedResId = R.drawable.icon_white_dot_selected;
                normalResId = R.drawable.icon_dark_dot_normal;
            } else if (type == WHITE_DOT) {
                width = (int) context.getResources().getDimension(R.dimen.dot_size);
                ;
                height = width;

                marginHorizontal = DensityUtil.dip2px(context, 4);
                marginVertical = DensityUtil.dip2px(context, 15);

                selectedResId = R.drawable.icon_white_dot_selected;
                normalResId = R.drawable.icon_white_dot_normal;
            }

            for (int i = 0; i < size; i++) {
                // 新建一个按钮
                Button btn = new Button(context);
                // 点的大小
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                // 设置点的边距
                params.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
                // 设置按钮的大小属性
                btn.setLayoutParams(params);
                if (i == selected) {
                    btn.setBackgroundResource(selectedResId);
                } else {
                    btn.setBackgroundResource(normalResId);
                }
                dotBtns.add(btn);
                dotContainer.addView(btn);
            }

            for (int i = 0; i < dotBtns.size(); i++) {
                final int finalI = i;
                dotBtns.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sliderLayout.setCurrentPosition(finalI);
                        selectDotBtn(dotBtns, finalI, type);
                    }
                });
            }
        }
        return dotBtns;
    }

    /**
     * 选择按钮
     *
     * @param finalI
     */
    public static void selectDotBtn(List<Button> dotBtns, int finalI, int type) {

        int selectedResId = 0;
        int normalResId = 0;

        if (type == AD_DOT) {
            selectedResId = R.color.black;
            normalResId = R.color.color_761a1a1a;
        } else if (type == DARK_DOT) {
            selectedResId = R.drawable.icon_dark_dot_selected;
            normalResId = R.drawable.icon_dark_dot_normal;
        } else if (type == WHITE_DOT) {
            selectedResId = R.drawable.icon_white_dot_selected;
            normalResId = R.drawable.icon_white_dot_normal;
        }

        for (int i = 0; i < dotBtns.size(); i++) {
            if (i == finalI) {
                dotBtns.get(i).setBackgroundResource(selectedResId);
            } else {
                dotBtns.get(i).setBackgroundResource(normalResId);
            }
        }
    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Window window, float bgAlpha) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha;
        window.setAttributes(lp);
    }

    /**
     * 计算两个时间的时间差，以“00:00:00”格式输出
     * @param start
     * @param end
     * @return
     */
    public static String calculateDuration(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long s = sdf.parse(start).getTime();
            long e = sdf.parse(end).getTime();
            long duration = e - s;

            duration = duration % (24 * 60 * 60 * 1000);
            int hour = (int) (duration / (60 * 60 * 1000));

            duration = duration % (60 * 60 * 1000);
            int minute = (int) (duration / (60 * 1000));

            duration = duration % (60 * 1000);
            int second = (int) (duration / 1000);

            return formatNum(hour) + ":" + formatNum(minute) + ":" + formatNum(second);

        } catch (ParseException e) {
            return "00:00:00";
        }
    }

    public static String formatNum(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return String.valueOf(num);
    }

    /**
     * 给数字加上","分隔符
     *
     * @param num
     * @return
     */
    public static String formatNumWithComma(double num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return nf.format(num);
    }


}
