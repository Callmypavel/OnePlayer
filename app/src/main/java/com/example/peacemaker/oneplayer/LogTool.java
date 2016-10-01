package com.example.peacemaker.oneplayer;

import android.util.Log;

/**
 * Created by ouyan on 2016/9/22.
 */

public class LogTool {
    private static boolean isLogOpen = true;
    public static void log(String tag,String message){
        if(isLogOpen){
            Log.v(tag,message);
        }
    }
}
