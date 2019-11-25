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
    public static int REQUEST_PERMISSION_CODE = 1;
    public static boolean isAllPermissionGranted = false;
    public static void requestAllPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
                String[] requestedPermissions = packageInfo.requestedPermissions;
                for (String requestedPermission : requestedPermissions) {
                    if (ContextCompat.checkSelfPermission(activity, requestedPermission) != PackageManager.PERMISSION_GRANTED) {
                        activity.requestPermissions(new String[]{requestedPermission}, REQUEST_PERMISSION_CODE);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public static void onPermissionResult(Activity activity,int requestCode, int[] grantResults){
        if (requestCode == PermissionUtil.REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                PermissionUtil.isAllPermissionGranted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        PermissionUtil.isAllPermissionGranted = false;
                    }
                }
                if (!PermissionUtil.isAllPermissionGranted){
                    PermissionUtil.requestAllPermission(activity);
                }
            }

        }
    }


}
