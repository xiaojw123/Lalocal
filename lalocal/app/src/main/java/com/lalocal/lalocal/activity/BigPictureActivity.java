package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.BigPictureBean;
import com.lalocal.lalocal.util.DrawableUtils;


public class BigPictureActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_picture_layout);
		LinearLayout bigLayout= (LinearLayout) findViewById(R.id.big_img);
		Intent intent = getIntent();
		BigPictureBean bean=intent.getParcelableExtra("bigbean");

		View inflate = View.inflate(this, R.layout.photo_to_text, null);
		TextView textContent = (TextView) inflate.findViewById(R.id.photo_to_text_content);
		ImageView photoIv= (ImageView) inflate.findViewById(R.id.photo_to_text_iv);
		TextView textName = (TextView) inflate.findViewById(R.id.photo_to_text_name);
		DrawableUtils.displayImg(this, photoIv, bean.getImgUrl());
		if(bean.getContent()!=null){
			textContent.setText(bean.getContent());
		}else if(bean.getName()!=null){
			textName.setText("- -"+bean.getName());
		}

		bigLayout.addView(inflate);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//finish();
		return true;
	}

}
