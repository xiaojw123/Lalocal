package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.res.Configuration;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.LiveSurfaceView;

import java.io.IOException;

/**
 * Created by android on 2016/7/20.
 */
public class CreateLiveRoomActivity extends  BaseActivity implements SurfaceHolder.Callback{
    private static Context context = null;
    private LiveSurfaceView surfaceview;
    private SurfaceHolder surfaceholder;
    private Camera camera = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_live_room_layout);
        surfaceview = (LiveSurfaceView)findViewById(R.id.live_create_room);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceholder.addCallback(CreateLiveRoomActivity.this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //获取camera对象
        camera = Camera.open();
        try {
            //设置预览监听
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();

            if (this.getResources().getConfiguration().orientation
                    != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                parameters.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            camera.setParameters(parameters);
            //启动摄像头预览
            camera.startPreview();
            System.out.println("camera.startpreview");

        } catch (IOException e) {
            e.printStackTrace();
            camera.release();
            System.out.println("camera.release");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }
}
