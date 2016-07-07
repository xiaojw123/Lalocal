package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.BigPictureBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.SecretTextView;
import com.lalocal.lalocal.view.SharePopupWindow;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;


public class BigPictureActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {

	private ImageView shareBtn;
	private SharePopupWindow shareActivity;
	private LinearLayout bigLayout;
	private LinearLayout pictureShare;
	private SecretTextView textContent;
	private View inflate;
	private ImageView photoIv;
	private SecretTextView textName;
	private View masking;
	private BigPictureBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.big_picture_layout);
		initView();
		initData();


	}

	private void initView() {
		bigLayout = (LinearLayout) findViewById(R.id.big_img);
		pictureShare = (LinearLayout) findViewById(R.id.picture_share);
		shareBtn = (ImageView) findViewById(R.id.bigpicture_img_share);
		inflate = View.inflate(this, R.layout.photo_to_text, null);
		textContent = (SecretTextView) inflate.findViewById(R.id.photo_to_text_content);
		photoIv = (ImageView) inflate.findViewById(R.id.photo_to_text_iv);
		textName = (SecretTextView) inflate.findViewById(R.id.photo_to_text_name);
		masking = inflate.findViewById(R.id.masking);
	}

	private void initData() {
		Intent intent = getIntent();
		bean = intent.getParcelableExtra("bigbean");

		boolean share = bean.isShare();
		if(!share){
			pictureShare.setVisibility(View.GONE);
			masking.setVisibility(View.GONE);
		}

		DrawableUtils.displayImg(this, photoIv, bean.getImgUrl());
		if(!"".equals(bean.getContent())){
			textContent.setIsVisible(true);
			textContent.setText(bean.getContent());
		}
		if(!"".equals(bean.getName())){
			textName.setText("- -"+ bean.getName());
		}

		bigLayout.addView(inflate);
		shareBtn.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}
	//分享
	@Override
	public void onClick(View v) {
		SpecialShareVOBean shareVO=new SpecialShareVOBean();
		shareVO.setImg(bean.getImgUrl());
		shareActivity = new SharePopupWindow(this, shareVO);
		shareActivity.setPlatformActionListener(this);
		shareActivity.showShareWindow();
		shareActivity.showAtLocation(findViewById(R.id.big_picture_main),
				Gravity.CENTER, 0, 0);
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {

	}

	@Override
	public void onCancel(Platform platform, int i) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK();
	}
}
