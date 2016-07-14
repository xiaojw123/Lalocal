package com.lalocal.lalocal.activity;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.DensityUtil;


/**
 * Created by lenovo on 2016/7/4.
 */
public class TestAct extends BaseActivity{
    private Button btnThum;
    // private ImageView imgThum;
    private ImageView imgSource;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ImageView iv = (ImageView) findViewById(R.id.source);
  //      imageView = (ImageView)findViewById(R.id.masking);
       Bitmap bit = drawTextToBitmap(this, R.drawable.a, "哈哈哈哈发哈分行");
        iv.setImageBitmap(bit);
    //    setupViews();
    }


    private void setupViews() {
        btnThum = (Button) findViewById(R.id.getThum);
        imgSource = (ImageView) findViewById(R.id.source);
       // Bitmap mark = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap mark = BitmapFactory.decodeResource(this.getResources(),R.drawable.icon);
        Bitmap photo = BitmapFactory.decodeResource(this.getResources(),R.drawable.a);
        Bitmap bitmap1 = createBitmap(photo,mark,200,10);
        bitmap1 = createBitmap(bitmap1,"dwtedx", 800, 200);
        if (bitmap1 != null) {
            imgSource.setImageBitmap(bitmap1);
        }
    }

    private Bitmap createBitmap(Bitmap photo, Bitmap watermark,int mark_x,int mark_y){
        //左上角 mark_x = 0；mark_y=0;
        //右上角 mark_x = photo.getWidth() - watermark.getWidth()；mark_y=0;
        //左下角 mark_x = 0；mark_y=photo.getHeight() - watermark.getHeight();
		/*左上角 mark_x = photo.getWidth() - watermark.getWidth()；
	/     mark_y = photo.getHeight() - watermark.getHeight();*/

        String tag = "createBitmap";
        // Log.d( tag, "create a new bitmap" );
        if (photo == null) {
            return null;

        }
        int photoWidth = photo.getWidth();
        int photoHeight = photo.getHeight();
        int markWidth = watermark.getWidth();
        int markHeight = watermark.getHeight();

        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888);
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);

        // draw src into
        cv.drawBitmap(photo, 10,10, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark,  mark_x , mark_y , null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;

    }

    private Bitmap createBitmap(Bitmap photo, String str,int mark_x,int mark_y){

        int width = photo.getWidth(), hight = photo.getHeight();
        System.out.println("宽"+width+"高"+hight);
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(80.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.WHITE);//采用的颜色

        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
        canvas.drawText(str, mark_x, mark_y, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();


        return icon;
    }


    public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
       paint.setTextSize((int) (12 * scale*2));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x =((bitmap.getWidth() - bounds.width()))/3 ;
        int y = (bitmap.getHeight() + bounds.height())/3;
        canvas.drawText(gText, x , y, paint);
        return bitmap;
    }
}
