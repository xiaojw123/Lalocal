package com.lalocal.lalocal.util;
import java.util.ArrayList;
import java.util.List;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.view.viewpager.CycleViewPager;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;


/**
 * ImageView创建工厂
 */
public class ViewFactory {
	/**
	 * 轮播广告
	 */
	public static void initialize(final Context context,View vhdf,final CycleViewPager cycleViewPager,List<RecommendAdResultBean> result) {

		List<ImageView> views = new ArrayList<ImageView>();

		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(context, result.get(result.size() - 1).photo));
		for (int i = 0; i < result.size(); i++) {
			views.add(ViewFactory.getImageView(context, result.get(i).photo));
		}

		views.add(ViewFactory.getImageView(context, result.get(0).photo));
		// 设置循环，在调用setData方法前调�?
		cycleViewPager.setCycle(true);
		// 在加载数据前设置是否循环
		cycleViewPager.setData(views,result , new CycleViewPager.ImageCycleViewListener() {
			
			@Override
			public void onImageClick(RecommendAdResultBean info, int postion, View imageView) {
				// TODO Auto-generated method stub
				if (cycleViewPager.isCycle()) {
					postion = postion - 1;
					Log.i("hhhfh","postion:"+postion);
				}
			}
		});
		//设置轮播
		cycleViewPager.setWheel(true);
		// 设置轮播时间
		cycleViewPager.setTime(2000);
		//设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		DrawableUtils.displayImg(context, imageView, url);
		return imageView;
	}
}
