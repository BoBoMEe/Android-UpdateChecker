package com.bobomee.android.updatechecker.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.bobomee.android.updatechecker.R;
import com.bobomee.android.updatechecker.utils.AppUtil;
import com.bobomee.android.updatechecker.utils.NotificationUtil;

/**
 * Created by bobomee on 16/3/13.
 */
public class DownloadNotificationImpl {

    protected Context context;
    protected NotificationManagerCompat notificationManagerCompat;
    protected NotificationCompat.Builder builder;
    protected int id;

    public DownloadNotificationImpl(Context context) {
        this.context = context;
        this.notificationManagerCompat = NotificationUtil.initService(context);
        this.builder = NotificationUtil.initBuilder(context);
        this.id = (int) (System.currentTimeMillis() / 1000);
    }

    public void onDownloadPrepare() {
        NotificationUtil.setNotify(builder,
                context.getString(R.string.download),
                R.mipmap.ic_launcher,//不设置icon，notification不显示
                context.getString(R.string.downloadprepare),
                context.getString(R.string.download));
        NotificationUtil.invalidate(notificationManagerCompat, builder, id);
    }

    public void onDownloadProgressChanged(int progress) {
        if (progress % 5 == 0) {
            builder.setProgress(100, progress, false);
            NotificationUtil.setContentText(builder, context.getResources().getString(R.string.downloading, progress));
            NotificationUtil.invalidate(notificationManagerCompat, builder, id);
        }
    }

    public void onDownloadCompleted(boolean success, String errorMsg) {
        if (success) {
            Intent intent = AppUtil.getInstallIntent(Uri.fromFile(AppUtil.getInstallFile()));
            NotificationUtil.setActivityIntent(builder, context, intent);
            NotificationUtil.invalidate(notificationManagerCompat, builder, id);
        }
    }

    public void onUIDownloadCompleted(boolean success, String errorMsg) {
        NotificationUtil.setContentText(builder, success ?
                context.getString(R.string.downloadsuccess) :
                context.getString(R.string.downloadfailue));
        NotificationUtil.invalidate(notificationManagerCompat, builder, id);
    }

    public void onDownloadCancel() {
        NotificationUtil.clearNotify(notificationManagerCompat, id);
    }
}
