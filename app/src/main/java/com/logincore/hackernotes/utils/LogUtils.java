package com.logincore.hackernotes.utils;

import android.util.Log;

public class LogUtils {

    public static boolean showLog = true;

    /**
     *
     * @param objTag 如果这个对象是String则直接使用，否则使用这个对象的类名
     * @param objMsg 使用这个对象的toString方法作为Log信息
     */
    public static void i(Object objTag, Object objMsg) {
        if (!showLog) {
            return;
        }

        String tag;
        if (objTag instanceof String) {
            tag = (String) objTag;
        } else if (objTag instanceof Class) {
            tag = ((Class) objTag).getSimpleName();
        } else {
            tag = objTag.getClass().getSimpleName();
        }

        String msg = (objMsg == null || objMsg.toString() == null) ? "null" : objMsg.toString();

        Log.i(tag, msg);
    }
}