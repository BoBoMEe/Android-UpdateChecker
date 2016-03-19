package com.bobomee.android.updatechecker.manager;


import com.bobomee.android.updatechecker.model.ApkInfo;

/**
 * Created by bobomee on 16/3/13.
 */
public enum ApkInfoManager {

    INSTANCE;

    private ApkInfo apkInfo;

    public ApkInfo getApkInfo() {
        return apkInfo;
    }

    public void setApkInfo(ApkInfo apkInfo) {
        this.apkInfo = apkInfo;
    }
}
