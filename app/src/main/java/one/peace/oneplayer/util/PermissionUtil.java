package one.peace.oneplayer.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;


/**
 * Created by pavel on 2018/3/16.
 */

public class PermissionUtil {
    public static boolean requestAllPermission(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            for (String requestedPermission : requestedPermissions) {
                if (ContextCompat.checkSelfPermission(context, requestedPermission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
