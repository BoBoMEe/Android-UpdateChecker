package com.bobomee.android.updatechecker.model;

import java.io.Serializable;

/**
 * apk更新信息
 */
public class ApkInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String downloadUrl = ""; // 新版下载地址
    private String versionName = ""; // apk版本号(更新必备)
    private String updateMessage = ""; // apk更新日志

    public ApkInfo(String downloadUrl, String apkCode, String apkName,
                   String apkLog) {
        super();
        this.downloadUrl = downloadUrl;
        this.versionName = apkCode;
        this.updateMessage = apkLog;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }


    public String getVersionName() {
        return versionName;
    }


    public String getUpdateMessage() {
        return updateMessage;
    }

}
