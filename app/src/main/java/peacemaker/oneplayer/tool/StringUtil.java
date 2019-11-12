package peacemaker.oneplayer.tool;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ouyan on 2016/10/9.
 */

public class StringUtil {
    public final static String result = "result";
    public final static int successCode = 1234;
    private static String url = "http://192.168.1.105/index2.html";
    private static String url_background = "http://192.168.1.105/OneBackGround";
    public static String record_path = "/mnt/sdcard/OneRecord/audio/";
    public static String getString(Context context,int resId){
        return context.getResources().getString(resId);
    }
    public static String getString(Context context,int resId,Object... args){
        String data = context.getResources().getString(resId);
        return String.format(data,args);
    }
    public static String getCurrentTimeString(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//可以方便地修改日期格式
        return dateFormat.format(currentDate);
    }
    public static String getUrl(){
        return url;
    }
    public static String getUrl_background(){
        return url_background;
    }
}
