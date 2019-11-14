package one.peace.oneplayer.util;

import android.content.Context;

import androidx.annotation.NonNull;

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
    public static boolean isEndWith(String[] strings,@NonNull String target){
        for (String string : strings) {
            if (string.endsWith(target)){
                return true;
            }
        }
        return false;
    }
}
