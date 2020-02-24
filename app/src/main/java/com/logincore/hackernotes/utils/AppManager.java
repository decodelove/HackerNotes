package com.logincore.hackernotes.utils;

import android.app.Activity;
import java.util.Iterator;
import java.util.Stack;

public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;
    private AppManager(){}

    public static AppManager getInstance(){
        if (instance==null){
            instance = new AppManager();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity getTopActivity(){
        return activityStack.lastElement();
    }

    /**
     * 结束栈顶activity
     */
    public void finishTopActivity(){
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定activity
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }

    /**
     * 结束指定类名activity
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()){
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)){
                iterator.remove();
                activity.finish();
            }
        }
    }

    public void finishAllActivity(){
        for (int i = 0; i < activityStack.size(); i++) {
            if (null!=activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 得到指定类名的activity
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)){
                return activity;
            }
        }
        return null;
    }

    public void appExit(){
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }catch (Exception e){}
    }
}
