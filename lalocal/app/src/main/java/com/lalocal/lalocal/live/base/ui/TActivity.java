package com.lalocal.lalocal.live.base.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.live.base.util.ReflectionUtil;
import com.lalocal.lalocal.live.entertainment.agora.openlive.EngineConfig;
import com.lalocal.lalocal.live.entertainment.agora.openlive.MyEngineEventHandler;
import com.lalocal.lalocal.live.entertainment.agora.openlive.WorkerThread;

import io.agora.rtc.RtcEngine;


public abstract class TActivity extends AppCompatActivity {

    private boolean destroyed = false;

    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View layout = findViewById(Window.ID_ANDROID_CONTENT);
        ((MyApplication) getApplication()).initWorkerThread();
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initUIandEvent();
            }
        });

    }

    protected abstract void initUIandEvent();
    protected abstract void deInitUIandEvent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deInitUIandEvent();
        destroyed = true;
    }

    protected RtcEngine rtcEngine() {
        return ((MyApplication) getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return ((MyApplication) getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((MyApplication) getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((MyApplication) getApplication()).getWorkerThread().eventHandler();
    }

    @Override
    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
        super.onBackPressed();
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus ：键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }

    /**
     * 判断页面是否已经被销毁（异步回调时使用）
     */
    protected boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void invokeFragmentManagerNoteStateNotSaved() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ReflectionUtil.invokeMethod(getFragmentManager(), "noteStateNotSaved", null);
        }
    }

    protected void switchFragmentContent(TFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fragment.getContainerId(), fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

}
