package peacemaker.oneplayer.tool;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ouyan on 2016/9/22.
 */

public class LogTool {
    private static boolean isLogOpen = true;
    private static ArrayList<String> tags = new ArrayList<>();
    private static String[] bannedTags = {};
    public static void log(String tag,String message){
        if(!tags.contains(tag)){
            tags.add(tag);
        }
        if(isLogOpen){
            if(!isBanned(tag)) {
                Log.v(tag,"Pavel:" + message);
            }
        }
    }
    public static void log(Object object,String message){
        String tag = object.getClass().getSimpleName();
        if(!tags.contains(tag)){
            tags.add(tag);
        }
        if(isLogOpen){
            if(!isBanned(tag)) {
                Log.v(tag, "Pavel:" + message);
            }
        }
    }
    public static void log(Object object,Object... args){
        String tag;
        if(object instanceof String){
            tag = (String)object;
        }else {
            tag = object.getClass().getSimpleName();
        }
        if(!tags.contains(tag)){
            tags.add(tag);
        }
        if(isLogOpen){
            if(!isBanned(tag)) {
                for (Object arg : args) {
                    Log.v(tag, "Pavel:" + arg);
                }
            }
        }
    }

    public static void log(Object object,byte[] data){
        String tag = object.getClass().getSimpleName();
        if(!tags.contains(tag)){
            tags.add(tag);
        }
        if(isLogOpen){
            if(!isBanned(tag)) {
                Log.v(tag, "byte数组输出开始");
                for (byte b : data) {
                    Log.v(tag, "Pavel:" + b);
                }
                Log.v(tag, "byte数组输出结束");
            }
        }
    }
    public static void log(Object object, PersistableBundle bundle, Object... args){
        if (Build.VERSION.SDK_INT<=21){
            return;
        }
        String tag = object.getClass().getSimpleName();
        if(!tags.contains(tag)){
            tags.add(tag);
        }
        if(isLogOpen){
            if(!isBanned(tag)) {
                if(bundle!=null) {
                    Set<String> keySet = bundle.keySet();  //获取所有的Key,
                    for (String key : keySet) {
                        Log.v(tag, "Pavel:key=" + key + "\nvalue=" + bundle.get(key));
                    }
                }
                for (Object arg : args) {
                    Log.v(tag, "Pavel:" + arg);
                }
            }
        }
    }
    private static Boolean isBanned(String tag){
        for (int i = 0; i < bannedTags.length; i++) {
            if(bannedTags[i].equals(tag)){
                return true;
            }
        }
        return false;
    }
}
