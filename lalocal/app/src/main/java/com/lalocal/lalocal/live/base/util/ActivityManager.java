package com.lalocal.lalocal.live.base.util;

import android.app.Activity;

import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackNewActivity;

import java.util.Stack;

/**
 * Created by android on 2016/10/24.
 */
public class ActivityManager {
    public static ActivityManager appManager = null;
    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (appManager == null) {
            appManager = new ActivityManager();
        }
        return appManager;
    }

    private static Stack<PlayBackNewActivity> playBackActivityStack = new Stack<PlayBackNewActivity>();
    private static Stack<AudienceActivity> audienceActivityStack =new Stack<AudienceActivity>();


    public static void addPlayBackActivity(PlayBackNewActivity activity) {
        playBackActivityStack.add(activity);
    }

    public static void audienceActivityStack(AudienceActivity activity) {
        audienceActivityStack.add(activity);
    }

    public static void removePlayBackCurrent() {
        if(playBackActivityStack.size()>0){
            Activity lastElement = playBackActivityStack.lastElement();
            lastElement.finish();
            playBackActivityStack.remove(lastElement);
        }

    }

    public static void removeAudienceCurrent() {
        if(audienceActivityStack.size()>0){
            Activity lastElement = audienceActivityStack.lastElement();
            lastElement.finish();
            audienceActivityStack.remove(lastElement);
        }

    }


    /**
     * 删除指定的activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        for (int i = playBackActivityStack.size() - 1; i >= 0; i--) {
            if (playBackActivityStack.get(i).getClass().equals(activity.getClass())) {
                activity.finish();
                playBackActivityStack.remove(activity);
                break;
            }
        }
    }

    public static void clear() {
        for (Activity activity : playBackActivityStack) {
            activity.finish();
        }
        playBackActivityStack.clear();
    }


    public static int getplayBackSize() {
        return playBackActivityStack.size();
    }
}
