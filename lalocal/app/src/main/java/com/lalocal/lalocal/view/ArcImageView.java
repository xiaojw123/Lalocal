package com.lalocal.lalocal.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lalocal.lalocal.util.AppLog;

/**
 * Created by xiaojw on 2016/11/28.
 */

public class ArcImageView extends ImageView {
    private Paint mPaintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
    BitmapShader shader;
    Matrix matrix = new Matrix();
    float mSupX, mSupY;

    public ArcImageView(Context context) {
        super(context);
    }

    public ArcImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        AppLog.print("onDraw____");
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bmp = bd.getBitmap();
            shader = new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            float scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
                    * 1.0f / bmp.getHeight());
            matrix.setScale(scale, scale);
            shader.setLocalMatrix(matrix);
            mPaintBitmap.setShader(shader);
            mPaintBitmap.setStyle(Paint.Style.FILL);
            int offset = getHeight() * 16 / 100;
//            RectF oval = new RectF();                     //RectF对象
//            oval.left = 0;                              //左边
//            oval.top = getBottom() - offset*2;                                   //上边
//            oval.right = getWidth();                             //右边
//            oval.bottom = getBottom();
//            canvas.drawArc(oval, -360, 180, true, mPaintBitmap);
            RectF rectF = new RectF();
            rectF.left = 0;
            rectF.right = getWidth();
            rectF.top = 0;
            rectF.bottom = getBottom() - offset;
            if (mSupX == 0) {
                mSupX = rectF.right / 2;
            }
            if (mSupY == 0) {
                mSupY = rectF.bottom + offset;
            }
            Path path = new Path();
            path.addRect(rectF, Path.Direction.CW);
            path.moveTo(0, rectF.bottom);
            AppLog.print("sux___" + mSupX + ", suy__" + mSupY);
            path.quadTo(mSupX, mSupY, rectF.right, rectF.bottom);
            canvas.drawPath(path, mPaintBitmap);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        AppLog.print("onTouchEvent______" + event.getAction());
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            mSupX = event.getX();
//            mSupY = event.getY();
//            invalidate();
//        }else if (event.getAction()==MotionEvent.ACTION_CANCEL){
//            mSupX=0;
//            mSupY=0;
//            invalidate();
//        }
//        return super.onTouchEvent(event);
//    }
}
