package com.lalocal.lalocal.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.im.view.BaseZoomableImageView;
import com.lalocal.lalocal.im.view.ImageGestureListener;
import com.lalocal.lalocal.im.view.MultiTouchZoomableImageView;
import com.lalocal.lalocal.live.im.util.media.BitmapDecoder;
import com.lalocal.lalocal.live.im.util.media.ImageUtil;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class WatchMessagePictureActivity extends BaseActivity {
    public static final String IM_MESSAGE = "img_msg";
    MultiTouchZoomableImageView img;

    public static void start(Context context, IMMessage imMessage) {
        Intent intent = new Intent(context, WatchMessagePictureActivity.class);
        intent.putExtra(IM_MESSAGE, imMessage);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_message_picture);
        img = (MultiTouchZoomableImageView) findViewById(R.id.watch_image_view);
        setThumbnail();
        onImageViewFound(img);
    }

    private void setThumbnail() {
        IMMessage message = (IMMessage) getIntent().getSerializableExtra(IM_MESSAGE);
        FileAttachment fileAttachment = (FileAttachment) message.getAttachment();
        if (fileAttachment != null) {
            String imgPath = fileAttachment.getPath();
            if (TextUtils.isEmpty(imgPath)) {
                imgPath = fileAttachment.getThumbPath();
            }
            if (TextUtils.isEmpty(imgPath)) {
                img.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(R.drawable.androidloading));
                return;
            }
            if (!TextUtils.isEmpty(imgPath)) {
                Bitmap bitmap = BitmapDecoder.decodeSampledForDisplay(imgPath);
                bitmap = ImageUtil.rotateBitmapInNeeded(imgPath, bitmap);
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                    return;
                }
            }
        }
        img.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(R.drawable.androidloading));
    }

    // 设置图片点击事件
    protected void onImageViewFound(BaseZoomableImageView imageView) {
        imageView.setImageGestureListener(new ImageGestureListener() {

            @Override
            public void onImageGestureSingleTapConfirmed() {
                onImageViewTouched();
            }

            @Override
            public void onImageGestureLongPress() {
            }

            @Override
            public void onImageGestureFlingDown() {
                finish();
            }
        });
    }

    // 图片单击
    protected void onImageViewTouched() {
        finish();
    }


}
