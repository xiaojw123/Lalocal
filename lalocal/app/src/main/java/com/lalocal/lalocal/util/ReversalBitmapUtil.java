package com.lalocal.lalocal.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * Created by android on 2016/8/17.
 */
public class ReversalBitmapUtil {

    public  static  byte[]  reversal(byte[] b){
        if (b.length != 0) {
            Bitmap bitmap1= BitmapFactory.decodeByteArray(b, 0, b.length);
            Bitmap bitmap2 = adjustPhotoRotation(bitmap1, -90);
            if(bitmap2!=null){
                return bitmap2Bytes(bitmap2);
            }else {
                return  null;
            }
        } else {
            return null;
        }

    }

    public static  byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap adjustPhotoRotation(Bitmap bm, int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }


}
