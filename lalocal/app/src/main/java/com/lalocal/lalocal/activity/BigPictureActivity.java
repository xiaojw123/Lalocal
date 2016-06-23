package com.lalocal.lalocal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.DrawableUtils;


public class BigPictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_picture_layout);
		ImageView iv= (ImageView) findViewById(R.id.iv);
		Intent intent = getIntent();
		String imageurl = intent.getStringExtra("imageurl");
		DrawableUtils.displayImg(this, iv, imageurl);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
