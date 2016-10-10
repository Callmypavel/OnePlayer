package com.example.peacemaker.oneplayer;

import android.content.Context;

/**
 * Created by ouyan on 2016/10/9.
 */

public class StringUtil {
    public static String getString(Context context,int resId){
        return context.getResources().getString(resId);
    }
    public static String getString(Context context,int resId,Object... args){
        String data = context.getResources().getString(resId);
        return String.format(data,args);
    }
}
