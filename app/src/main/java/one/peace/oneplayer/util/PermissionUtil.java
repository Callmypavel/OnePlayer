package one.peace.oneplayer.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;



/**
 * Created by pavel on 2018/3/16.
 */

public class PermissionUtil {
    public static int REQUEST_PERMISSION_CODE = 1;
    public static String[] mRequestedPermissions;
    private static OnPermissionStateListener mOnPermissionStateListener;
    public interface OnPermissionStateListener{
        void onPermissionAllGranted(boolean isPermissionAllGranted);
    }
    public static void requestAllPermissions(Activity activity,OnPermissionStateListener onPermissionStateListener) {
        if (activity == null){
            return;
        }
        mOnPermissionStateListener = onPermissionStateListener;
        try {
            //申请所有权限
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            requestPermissions(activity,packageInfo.requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void requestPermissions(Activity activity,String[] requestedPermissions) {
        if (activity == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //申请所有权限
            mRequestedPermissions = requestedPermissions;
            activity.requestPermissions(requestedPermissions, REQUEST_PERMISSION_CODE);
        }

    }

    public static void onPermissionResult(final Activity activity, int requestCode, int[] grantResults){
        if (mRequestedPermissions != null) {
            if (requestCode == PermissionUtil.REQUEST_PERMISSION_CODE) {
                if (grantResults.length > 0) {
                    final ArrayList<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            //该权限已拒绝
                            deniedPermissions.add(mRequestedPermissions[i]);
                        }
                    }
                    if (deniedPermissions.size() > 0) {
                        DialogUtil.showDialog(activity, "部分权限未获取"
                                , StringUtil.listToString(deniedPermissions, "、") + "权限未获取，未获取全部权限将无法使用此应用!",
                                "重新获取权限",
                                "退出应用",
                                new DialogUtil.DialogClickListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        requestPermissions(activity, StringUtil.listToStringArray(deniedPermissions));
                                    }

                                    @Override
                                    public void onNegativeClick() {
                                        System.exit(0);
                                    }
                                });
                    }
                    if (mOnPermissionStateListener != null){
                        mOnPermissionStateListener.onPermissionAllGranted(deniedPermissions.size() == 0);
                    }
                }

            }
        }
    }


}
