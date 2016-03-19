package com.bobomee.android.updatechecker.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

import com.bobomee.android.updatechecker.interfaces.Constants;
import com.bobomee.android.updatechecker.interfaces.DownloadCallback;
import com.bobomee.android.updatechecker.interfaces.OnCacelDownload;
import com.bobomee.android.updatechecker.interfaces.SimpleDownloadCallback;
import com.bobomee.android.updatechecker.manager.ApkInfoManager;
import com.bobomee.android.updatechecker.service.DownloadIntentService;
import com.bobomee.android.updatechecker.utils.AppUtil;

/**
 * Created by bobomee on 16/3/13.
 */
public class DownloadImpl extends SimpleDownloadCallback implements Constants, OnCacelDownload {

    protected boolean showProgressNotification = false;
    protected boolean showProgressDialog = false;
    protected boolean isAutoInstall = true;

    protected Context context;
    protected DownloadCallback downloadCallback;

    protected DownloadProgressDialogImpl progressDialog;
    protected DownloadNotificationImpl notification;

    protected Handler handler;

    public DownloadImpl(UpdateManager.UpdateBuilder builder) {
        this.showProgressNotification = builder.showProgressNotification;
        this.showProgressDialog = builder.showProgressDialog;
        this.isAutoInstall = builder.isAutoInstall;
        this.context = builder.context;
        this.downloadCallback = builder.downloadCallback;
        this.progressDialog = new DownloadProgressDialogImpl(this, builder);
        this.notification = new DownloadNotificationImpl(context);
        this.handler = new Handler(context.getMainLooper());
    }

    @Override
    public void onDownloadPrepare() {
        super.onDownloadPrepare();

        if (null != downloadCallback) {
            downloadCallback.onDownloadPrepare();
        }

        if (showProgressDialog) {
            progressDialog.onDownloadPrepare();
        }

        if (showProgressNotification) {
            notification.onDownloadPrepare();
        }

    }

    @Override
    public void onDownloadProgressChanged(int progress) {
        super.onDownloadProgressChanged(progress);
        if (null != downloadCallback) {
            downloadCallback.onDownloadProgressChanged(progress);
        }

        if (showProgressDialog) {
            progressDialog.onDownloadProgressChanged(progress);
        }

        if (showProgressNotification) {
            notification.onDownloadProgressChanged(progress);
        }

    }

    @Override
    public void onDownloadCompleted(boolean success, String errorMsg) {
        super.onDownloadCompleted(success, errorMsg);
        if (null != downloadCallback) {
            downloadCallback.onDownloadCompleted(success, errorMsg);
        }

        if (showProgressDialog) {
            progressDialog.onDownloadCompleted(success, errorMsg);
        }

        if (showProgressNotification) {
            notification.onDownloadCompleted(success, errorMsg);
        }

        if (isAutoInstall) {
            //auto install
            if (success) {
                AppUtil.installApk(context, Uri.fromFile(AppUtil.getInstallFile()));
            }
        }

    }

    @Override
    public void onUIDownloadCompleted(boolean success, String errorMsg) {
        super.onUIDownloadCompleted(success, errorMsg);
        if (null != downloadCallback) {
            downloadCallback.onUIDownloadCompleted(success, errorMsg);
        }

        if (showProgressDialog) {
            progressDialog.onUIDownloadCompleted(success, errorMsg);
        }

        if (showProgressNotification) {
            notification.onUIDownloadCompleted(success, errorMsg);
        }
    }

    @Override
    public void onDownloadCancel() {

        Intent intent = new Intent(cancel);
        context.sendBroadcast(intent);

        if (showProgressDialog) {
            progressDialog.onDownloadCancel();
        }

        if (showProgressNotification) {
            notification.onDownloadCancel();
        }

        if (null != downloadCallback) {
            downloadCallback.onDownloadCancel();
        }

    }

    public void onCheckComplete2Down(Context context) {

        String downloadUrl = ApkInfoManager.INSTANCE.getApkInfo().getDownloadUrl();

        DownloadIntentService.startDownLoad(context, resultReceiver, downloadUrl);
    }


    final ResultReceiver resultReceiver = new ResultReceiver(null) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == PROGRESS) {
                final int result = resultData.getInt(progress);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onDownloadProgressChanged(result);
                    }
                });
            } else if (resultCode == COMPLETE) {
                final String result = resultData.getString(message);
                final boolean code = resultData.getBoolean(messagecode);

                onDownloadCompleted(code, result);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUIDownloadCompleted(code, result);
                    }
                });
            }
        }
    };

    @Override
    public void oncancel() {
        onDownloadCancel();
    }
}
