package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.lalocal.lalocal.R;

/**
 * Created by android on 2016/10/14.
 */
public class CustomSliderView extends BaseSliderView {
    public CustomSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_slider_view_layout,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return null;
    }
}
