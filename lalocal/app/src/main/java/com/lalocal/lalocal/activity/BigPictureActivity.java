package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.BigPictureBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
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
	private Bitmap bitmap;
	private String content;

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
			content = bean.getContent();
			textContent.setText(content);
		}else{
			pictureShare.setVisibility(View.GONE);
			masking.setVisibility(View.GONE);
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
		bitmap = ((BitmapDrawable) photoIv.getDrawable()).getBitmap();
		Bitmap bitmap2 = drawTextToBitmap(BigPictureActivity.this, this.bitmap, content);
		AppLog.i("TAG","ffaff:"+bitmap2.toString());
		SpecialShareVOBean shareVO=new SpecialShareVOBean();
		shareVO.setImg(bean.getImgUrl());
		shareVO.setUrl(bean.getImgUrl());
		shareVO.setBitmap(bitmap2);
		shareActivity = new SharePopupWindow(this, shareVO);
		shareActivity.setPlatformActionListener(this);
		shareActivity.showShareWindow();
		shareActivity.showAtLocation(findViewById(R.id.big_picture_main),
				Gravity.CENTER, 0, 0);



	}


	public static Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap, String gText) {
	/*	BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize=3;*/

		Matrix matrix = new Matrix();
		matrix.postScale(0.7f,0.7f); //长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		int totalWidth = resizeBmp.getWidth();
		int totalHeight = resizeBmp.getHeight();
		int textWidth = totalWidth*3/6;
		int textHeight = totalHeight/2;
		int xWidth = (totalWidth - textWidth) / 2;
		int yWidth = (totalHeight - totalHeight) / 2;
		Bitmap icon = Bitmap.createBitmap(totalWidth, totalHeight,
				Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(icon);

		Paint photoPaint = new Paint();

		photoPaint.setDither(true);

		// 过滤
		photoPaint.setFilterBitmap(true);
		// 创建一个指定的新矩形的坐标
		Rect src = new Rect(0, 0, totalWidth, totalHeight);
		// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, totalWidth, totalHeight);
		// 将photo缩放或扩大到dst使用的填充区photoPaint
		canvas.drawBitmap(resizeBmp, src, dst, photoPaint);
		// 设置文字画笔
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		// 字体大小

		textPaint.setTextSize((int) (6 * scale*2));

		// 采用默认的宽度
		textPaint.setTypeface(Typeface.DEFAULT);
		// 文字画笔采用的颜色
		textPaint.setColor(Color.WHITE);
		canvas.translate(xWidth, 180);
		StaticLayout layout = new StaticLayout(gText, textPaint, textWidth,
				Layout.Alignment.ALIGN_CENTER, 1.2F, 0.0F, true);// 这个StaticLayout是让文字在图片中多行显示的关键，android之所以强大就是它已经帮你封装好了，通过对StaticLayout的设置就可以让EditText中的文字多行显示
		layout.draw(canvas);
	/*	Matrix matrix = new Matrix();
		matrix.postScale(0.7f,0.7f); //长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(icon,0,0,icon.getWidth(),icon.getHeight(),matrix,true);*/
		return  icon;


















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
		AppLog.i("TAG","BigPicture:"+throwable.toString());
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
