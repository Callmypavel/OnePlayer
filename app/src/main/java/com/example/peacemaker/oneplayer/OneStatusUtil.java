package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;

/**
 * Created by ouyan on 2016/9/24.
 */

public class OneStatusUtil {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public static void setStatusColor(Activity activity,int color){
        if (Build.VERSION.SDK_INT >= 21) {
            if(activity!=null){
                Window window = activity.getWindow();
                window.setStatusBarColor(color);
            }

        }
    }

}
