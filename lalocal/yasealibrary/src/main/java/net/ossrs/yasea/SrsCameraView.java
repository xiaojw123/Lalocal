package net.ossrs.yasea;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import com.seu.magicfilter.base.gpuimage.GPUImageFilter;
import com.seu.magicfilter.utils.MagicFilterFactory;
import com.seu.magicfilter.utils.MagicFilterType;
import com.seu.magicfilter.utils.OpenGLUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Leo Ma on 2016/2/25.
 */
public class SrsCameraView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private GPUImageFilter magicFilter;
    private SurfaceTexture surfaceTexture;
    private int mOESTextureId = OpenGLUtils.NO_TEXTURE;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private float mInputAspectRatio;
    private float mOutputAspectRatio;
    private float[] mProjectionMatrix = new float[16];
    private float[] mSurfaceMatrix = new float[16];
    private float[] mTransformMatrix = new float[16];

    private Camera mCamera;
    private ByteBuffer mGLPreviewBuffer;
    private int mCamId = -1;
    private int mPreviewRotation = 90;
    private int mPreviewOrientation = Configuration.ORIENTATION_PORTRAIT;

    private Thread worker;
    private final Object writeLock = new Object();
    private ConcurrentLinkedQueue<IntBuffer> mGLIntBufferCache = new ConcurrentLinkedQueue<>();
    private PreviewCallback mPrevCb;

    public SrsCameraView(Context context) {
        this(context, null);
    }

    public SrsCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glDisable(GL10.GL_DITHER);
        GLES20.glClearColor(0, 0, 0, 0);

        magicFilter = new GPUImageFilter(MagicFilterType.NONE);
        magicFilter.init(getContext().getApplicationContext());
        magicFilter.onInputSizeChanged(mPreviewWidth, mPreviewHeight);

        mOESTextureId = OpenGLUtils.getExternalOESTextureID();
        surfaceTexture = new SurfaceTexture(mOESTextureId);
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });

        // For camera preview on activity creation
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surfaceTexture);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        magicFilter.onDisplaySizeChanged(width, height);

        mOutputAspectRatio = width > height ? (float) width / height : (float) height / width;
        float aspectRatio = mOutputAspectRatio / mInputAspectRatio;
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -1.0f, 1.0f, -aspectRatio, aspectRatio, -1.0f, 1.0f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1.0f, 1.0f, -1.0f, 1.0f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        surfaceTexture.updateTexImage();

        surfaceTexture.getTransformMatrix(mSurfaceMatrix);
        Matrix.multiplyMM(mTransformMatrix, 0, mSurfaceMatrix, 0, mProjectionMatrix, 0);
        magicFilter.setTextureTransformMatrix(mTransformMatrix);

        magicFilter.onDrawFrame(mOESTextureId);
        mGLIntBufferCache.add(magicFilter.getGLFboBuffer());
        synchronized (writeLock) {
            writeLock.notifyAll();
        }
    }

    public void setPreviewCallback(PreviewCallback cb) {
        mPrevCb = cb;
    }

    public int[] setPreviewResolution(int width, int height) {
        getHolder().setFixedSize(width, height);

        mCamera = openCamera();
        mPreviewWidth = width;
        mPreviewHeight = height;
      //  Camera.Size rs = adaptPreviewResolution(mCamera.new Size(width, height));
        Camera.Size rs = getOptimalPreviewSize(mCamera.getParameters().getSupportedVideoSizes(),mCamera.new Size(width, height));
        if (rs != null) {
            mPreviewWidth = rs.width;
            mPreviewHeight = rs.height;
        }
        mCamera.getParameters().setPreviewSize(mPreviewWidth, mPreviewHeight);

        mGLPreviewBuffer = ByteBuffer.allocate(mPreviewWidth * mPreviewHeight * 4);
        mInputAspectRatio = mPreviewWidth > mPreviewHeight ?
            (float) mPreviewWidth / mPreviewHeight : (float) mPreviewHeight / mPreviewWidth;

        return new int[] { mPreviewWidth, mPreviewHeight };
    }

    public boolean setFilter(final MagicFilterType type) {
        if (mCamera == null) {
            return false;
        }

        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (magicFilter != null) {
                    magicFilter.destroy();
                }
                magicFilter = MagicFilterFactory.initFilters(type);
                if (magicFilter != null) {
                    magicFilter.init(getContext().getApplicationContext());
                    magicFilter.onInputSizeChanged(mPreviewWidth, mPreviewHeight);
                    magicFilter.onDisplaySizeChanged(mSurfaceWidth, mSurfaceHeight);
                }
            }
        });
        requestRender();
        return true;
    }

    private void deleteTextures() {
        if (mOESTextureId != OpenGLUtils.NO_TEXTURE) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    GLES20.glDeleteTextures(1, new int[]{ mOESTextureId }, 0);
                    mOESTextureId = OpenGLUtils.NO_TEXTURE;
                }
            });
        }
    }

    public void setCameraId(int id) {
        mCamId = id;
        setPreviewOrientation(mPreviewOrientation);
    }

    public void setPreviewOrientation(int orientation) {
        mPreviewOrientation = orientation;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCamId, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPreviewRotation = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? 270 : 90;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPreviewRotation = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? 180 : 0;
            }
        } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPreviewRotation = 90;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPreviewRotation = 0;
            }
        }
    }

    public int getCameraId() {
        return mCamId;
    }

    public boolean startCamera() {
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    while (!mGLIntBufferCache.isEmpty()) {
                        IntBuffer picture = mGLIntBufferCache.poll();
                        mGLPreviewBuffer.asIntBuffer().put(picture.array());
                        mPrevCb.onGetRgbaFrame(mGLPreviewBuffer.array(), mPreviewWidth, mPreviewHeight);
                    }
                    // Waiting for next frame
                    synchronized (writeLock) {
                        try {
                            // isEmpty() may take some time, so we set timeout to detect next frame
                            writeLock.wait(500);
                        } catch (InterruptedException ie) {
                            worker.interrupt();
                        }
                    }
                }
            }
        });
        worker.start();
        if (mCamera == null) {
            mCamera = openCamera();
            if (mCamera == null) {
                return false;
            }
        }

        Camera.Parameters params = mCamera.getParameters();
        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (!supportedFocusModes.isEmpty()) {
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else {
                params.setFocusMode(supportedFocusModes.get(0));
            }
        }


        params.setPictureSize(mPreviewWidth, mPreviewHeight);
        params.setPreviewSize(mPreviewWidth, mPreviewHeight);
        int[] range = adaptFpsRange(SrsEncoder.VFPS, params.getSupportedPreviewFpsRange());
     //  params.setPreviewFpsRange(range[0], range[1]);
        Log.d("TAG","获取预览帧数:"+range[0]+"     :"+range[1]);
      //  params.setPreviewFpsRange(1000,30000);
        params.setPreviewFormat(ImageFormat.NV21);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//闪光灯
        params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        params.setColorEffect(Camera.Parameters.EFFECT_NONE);

        if (!params.getSupportedFocusModes().isEmpty()) {
            params.setFocusMode(params.getSupportedFocusModes().get(0));
        }
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(mPreviewRotation);

        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();

        return true;
    }

    public void stopCamera() {
        if (worker != null) {
            worker.interrupt();
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                worker.interrupt();
            }
            mGLIntBufferCache.clear();
            worker = null;
        }

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera openCamera() {
        Camera camera;
        if (mCamId < 0) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            int numCameras = Camera.getNumberOfCameras();
            int frontCamId = -1;
            int backCamId = -1;
            for (int i = 0; i < numCameras; i++) {
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    backCamId = i;
                } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    frontCamId = i;
                    break;
                }
            }
            if (frontCamId != -1) {
                mCamId = frontCamId;
            } else if (backCamId != -1) {
                mCamId = backCamId;
            } else {
                mCamId = 0;
            }
        }
        camera = Camera.open(mCamId);
        return camera;
    }

    private Camera.Size adaptPreviewResolution(Camera.Size resolution) {
        float diff = 100f;
        float xdy = (float) resolution.width / (float) resolution.height;
        Camera.Size best = null;
        for (Camera.Size size : mCamera.getParameters().getSupportedPreviewSizes()) {
            if (size.equals(resolution)) {
                return size;
            }
            float tmp = Math.abs(((float) size.width / (float) size.height) - xdy);
            if (tmp < diff) {
                diff = tmp;
                best = size;
            }
        }
        return best;
    }

    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, Camera.Size resolution) {
        int w=resolution.width;
        int h=resolution.height;
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



    private int[] adaptFpsRange(int expectedFps, List<int[]> fpsRanges) {
        expectedFps *= 1000;
        int[] closestRange = fpsRanges.get(0);
        int measure = Math.abs(closestRange[0] - expectedFps) + Math.abs(closestRange[1] - expectedFps);
        for (int[] range : fpsRanges) {
            if (range[0] <= expectedFps && range[1] >= expectedFps) {
                int curMeasure = Math.abs(range[0] - expectedFps) + Math.abs(range[1] - expectedFps);
                if (curMeasure < measure) {
                    closestRange = range;
                    measure = curMeasure;
                }
            }
        }
        return closestRange;
    }

    public interface PreviewCallback {

        void onGetRgbaFrame(byte[] data, int width, int height);
    }
}