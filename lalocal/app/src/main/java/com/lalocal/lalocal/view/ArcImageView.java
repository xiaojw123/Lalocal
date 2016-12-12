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

/**
 * Created by xiaojw on 2016/11/28.
 */

public class ArcImageView extends ImageView {
    private static final  float SCALE_X=1.0f;
    private static final  float SCALE_Y=0.93f;
    private Paint mPaintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
    BitmapShader shader;
    Matrix matrix = new Matrix();

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
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bmp = bd.getBitmap();
            shader = new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
//            float scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
//                    * 1.0f / bmp.getHeight());
//            matrix.setScale(scale, scale);
            float scaleX =getWidth()*SCALE_X/bmp.getWidth();
            float scaleY=getHeight()*SCALE_Y/bmp.getHeight();
            matrix.setScale(scaleX, scaleY);
            shader.setLocalMatrix(matrix);
            mPaintBitmap.setShader(shader);
            mPaintBitmap.setStyle(Paint.Style.FILL);
            int offset = getHeight()*16/100;
            RectF rectF = new RectF();
            rectF.left =getLeft();
            rectF.right = getRight();
            rectF.top = getTop();
            rectF.bottom = getBottom() - offset;
            Path path = new Path();
            path.addRect(rectF, Path.Direction.CW);
            path.moveTo(rectF.left,rectF.bottom);
            path.quadTo(getLeft()+getWidth()/2,getBottom(),rectF.right,rectF.bottom);
            canvas.drawPath(path, mPaintBitmap);
        }


    }
}
