package com.bobomee.android.updatechecker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.bobomee.android.updatechecker.interfaces.Constants;

import java.io.File;

/**
 * Created by bobomee on 16/3/12.
 */
public class AppUtil implements Constants {

    public static Intent getInstallIntent(Uri uri) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(uri, "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    /**
     * install apk file
     */
    public static void installApk(Context mContext, Uri uri) {
        Intent installIntent = getInstallIntent(uri);
        mContext.startActivity(installIntent);
    }

    /**
     * Return the versionName of this application's package.
     */
    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info != null) {
            return info.versionName;
        } else {
            return null;
        }
    }

    /**
     * Return the PackageInfo of this application's package.
     */
    public static PackageInfo getPackageInfo(Context context) {
        if (null == context) {
            return null;
        }
        String packageName = context.getPackageName();
        PackageInfo info = null;
        PackageManager manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return info;
    }

    public static File getInstallFile() {
        return new File(apkSavepath, apkSaveName);
    }

    public static File getInstallFilePath() {
        return new File(apkSavepath);
    }

}
