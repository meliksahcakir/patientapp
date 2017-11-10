package com.physhome.physhome;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Meliksah on 6/17/2017.
 */

public class PermissionsUtil {
    public static final int PERMISSION_ALL = 100;

    public static boolean doesAppNeedPermissions(){
        return android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static String[] getPermissions(Context context)
            throws PackageManager.NameNotFoundException {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_PERMISSIONS);

        return info.requestedPermissions;
    }

    public static void askPermissions(Activity activity){
        if(doesAppNeedPermissions()) {
            try {
                String[] permissions = getPermissions(activity);

                if(!checkPermissions(activity, permissions)){
                    ActivityCompat.requestPermissions(activity, permissions,
                            PERMISSION_ALL);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkPermissions(Context context, String... permissions){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
