package com.lalocal.lalocal.live.base.util;

import android.app.Activity;

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

    private static Stack<Activity> activityStack = new Stack<Activity>();


    public static void addActivity(Activity activity) {
        activityStack.add(activity);
    }


    public static void removeCurrent() {
        if(activityStack.size()>0){
            Activity lastElement = activityStack.lastElement();
            lastElement.finish();
            activityStack.remove(lastElement);
        }

    }


    /**
     * 删除指定的activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activityStack.get(i).getClass().equals(activity.getClass())) {
                activity.finish();
                activityStack.remove(activity);
                break;
            }
        }
    }

    public static void clear() {
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }


    public static int getSize() {
        return activityStack.size();
    }
}
