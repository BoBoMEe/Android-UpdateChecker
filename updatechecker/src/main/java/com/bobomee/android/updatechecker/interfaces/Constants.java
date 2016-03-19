package com.bobomee.android.updatechecker.interfaces;

import android.os.Environment;

import java.io.File;

/**
 * Created by bobomee on 16/3/13.
 */
public interface Constants {
    // updatepath
    String appPath = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "UpdateCheacker";

    String apkSaveName = "newVersion.apk";
    String apkSavepath = appPath
            + File.separator + "download";

    int PROGRESS = 0;
    int COMPLETE = 1;

    String progress = "progress";

    String message = "message";
    String messagecode = "messagecode";

    String cancel = "cancel";

}
