package com.cwave.calculation.splash;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Util class to grant permissions. */
public class Permissions {
  private static final String TAG = "Permission";

  // Asks for permissions for a {@link Activity}.
  public static void grantRuntimePermissions(Activity activity, int requestCode) {

    List<String> requiredPermissions = getRequiredPermissions(activity);
    if (requiredPermissions == null) {
      Log.i(TAG, "No runtime permission is required.");
      return;
    }

    List<String> askPermissions = new ArrayList<>();
    for (String permission : requiredPermissions) {
      if (!isPermissionGranted(activity, permission)) {
        askPermissions.add(permission);
      }
    }

    if (!askPermissions.isEmpty()) {
      ActivityCompat.requestPermissions(
          activity, askPermissions.toArray(new String[0]), requestCode);
    }
  }

  public static boolean ifAllPermissionGranted(Activity activity) {
    List<String> requiredPermissions = getRequiredPermissions(activity);
    for (String permission : requiredPermissions) {
      if (!isPermissionGranted(activity, permission)) {
        return false;
      }
    }
    return true;
  }

  /** Checks if a permission is granted. */
  private static boolean isPermissionGranted(Context context, String permission) {
    if (ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED) {
      Log.i(TAG, permission + " granted");
      return true;
    }
    Log.i(TAG, permission + " not granted yet");
    return false;
  }

  @Nullable
  private static List<String> getRequiredPermissions(Context context) {
    try {
      PackageInfo info = context
          .getPackageManager()
          .getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
      String[] ps = info.requestedPermissions;
      if (ps != null && ps.length > 0) {
        return Arrays.asList(ps);
      } else {
        return null;
      }
    } catch (Exception e) {
      Log.e(TAG, "Get permission error " + e);
      return null;
    }
  }
}
