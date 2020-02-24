package com.logincore.hackernotes.utils;

import android.content.Context;
import android.widget.Toast;

import com.logincore.hackernotes.MainActivity;

public class ToastUtils {

    public static void show(Context context, Object obj){
        if (obj instanceof String){
            Toast.makeText(context, (CharSequence) obj,Toast.LENGTH_SHORT).show();
        }else if (obj instanceof Integer){
            Toast.makeText(context, String.valueOf(obj),Toast.LENGTH_SHORT).show();
        } else if (obj instanceof Class){
            Toast.makeText(context,((Class) obj).getName(),Toast.LENGTH_SHORT).show();
        }
    }
}
