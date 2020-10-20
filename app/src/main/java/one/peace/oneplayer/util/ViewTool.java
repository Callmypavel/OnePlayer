package one.peace.oneplayer.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * Created by pavel on 2019/11/25.
 */
public class ViewTool {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public static void setStatusColor(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= 21) {
            if(activity!=null){
                Window window = activity.getWindow();
                window.setStatusBarColor(color);
            }

        }
    }

    public static void showView(View view, boolean isShowView) {
        showView(view, isShowView, false);
    }

    public static void showView(View view, boolean isShowView, boolean isHoldPosition) {
        if (view != null) {
            if (isShowView) {
                view.setVisibility(View.VISIBLE);
            } else {
                if (isHoldPosition) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }


}
