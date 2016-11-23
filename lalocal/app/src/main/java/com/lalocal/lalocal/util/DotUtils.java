package com.lalocal.lalocal.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.lalocal.lalocal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/11/23.
 */

public class DotUtils {

    public static final int AD_DOT = 0x01;
    public static final int DARK_DOT = 0x02;
    public static final int WHITE_DOT = 0x03;
    public static final int ROUND_LIGHT_DOT = 0X04;
    public static final int ROUND_WHITE_DOT = 0x05;

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
            } else {
                width = (int) context.getResources().getDimension(R.dimen.dot_size);
                height = width;

                marginHorizontal = DensityUtil.dip2px(context, 4);
                marginVertical = DensityUtil.dip2px(context, 15);

                if (type == DARK_DOT) {
                    selectedResId = R.drawable.icon_dark_dot_selected;
                    normalResId = R.drawable.icon_dark_dot_normal;
                } else if (type == WHITE_DOT) {
                    selectedResId = R.drawable.icon_white_dot_selected;
                    normalResId = R.drawable.icon_white_dot_normal;
                } else if (type == ROUND_LIGHT_DOT) {
                    selectedResId = R.drawable.icon_round_light_dot_selected;
                    normalResId = R.drawable.icon_round_light_dot_normal;
                } else if (type == ROUND_WHITE_DOT) {
                    selectedResId = R.drawable.icon_round_white_dot_normal;
                    normalResId = R.drawable.icon_round_white_dot_selected;
                }
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
            } else {
                width = (int) context.getResources().getDimension(R.dimen.dot_size);
                height = width;

                marginHorizontal = DensityUtil.dip2px(context, 4);
                marginVertical = DensityUtil.dip2px(context, 15);


                if (type == DARK_DOT) {
                    selectedResId = R.drawable.icon_dark_dot_selected;
                    normalResId = R.drawable.icon_dark_dot_normal;
                } else if (type == WHITE_DOT) {
                    selectedResId = R.drawable.icon_white_dot_selected;
                    normalResId = R.drawable.icon_white_dot_normal;
                } else if (type == ROUND_LIGHT_DOT) {
                    selectedResId = R.drawable.icon_round_light_dot_selected;
                    normalResId = R.drawable.icon_round_light_dot_normal;
                } else if (type == ROUND_WHITE_DOT) {
                    selectedResId = R.drawable.icon_round_white_dot_selected;
                    normalResId = R.drawable.icon_round_white_dot_normal;
                }
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
        } else if (type == ROUND_LIGHT_DOT) {
            selectedResId = R.drawable.icon_round_light_dot_selected;
            normalResId = R.drawable.icon_round_light_dot_normal;
        } else if (type == ROUND_WHITE_DOT) {
            selectedResId = R.drawable.icon_round_white_dot_selected;
            normalResId = R.drawable.icon_round_white_dot_normal;
        }

        for (int i = 0; i < dotBtns.size(); i++) {
            if (i == finalI) {
                dotBtns.get(i).setBackgroundResource(selectedResId);
            } else {
                dotBtns.get(i).setBackgroundResource(normalResId);
            }
        }
    }
}
