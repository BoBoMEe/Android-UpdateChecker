package com.bobomee.android.updatechecker.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.bobomee.android.updatechecker.interfaces.Constants;
import com.bobomee.android.updatechecker.utils.AppUtil;
import com.bobomee.android.updatechecker.utils.IOUtil;
import com.bobomee.android.updatechecker.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

/**
 * Created by bobomee on 16/3/12.
 */
public class DownloadIntentService extends IntentService implements Constants {

    private int lastPercent = -1;

    private static final String RECEICER = "receiver";
    private static final String DOWNLOADURL = "downloadurl";

    private Bundle bundle = new Bundle();

    private boolean onCancel = false;

    public static void startDownLoad(Context context, ResultReceiver resultReceiver, String downloadUrl) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra(RECEICER, resultReceiver);
        intent.putExtra(DOWNLOADURL, downloadUrl);
        context.startService(intent);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p/>
     * Used to name the worker thread, important only for debugging.
     */

    public DownloadIntentService() {
        super("DownLoadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mUrl = intent.getStringExtra(DOWNLOADURL);
        final ResultReceiver resultReceiver = intent.getParcelableExtra(RECEICER);

        // register cancel receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(cancel);
        CancelReceiver receiver = new CancelReceiver();
        registerReceiver(receiver, filter);

        FileOutputStream fos = null;
        InputStream is = null;

        try {

            URL url = new URL(mUrl);

            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                File filePath = AppUtil.getInstallFilePath();

                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                File apkFile = AppUtil.getInstallFile();

                if (apkFile.exists()) {
                    apkFile.delete();
                }

                int length = conn.getContentLength();
                is = conn.getInputStream();

                fos = new FileOutputStream(apkFile);

                int count = 0, numread;

                byte buf[] = new byte[512];

                while ((numread = is.read(buf)) > 0 && !onCancel) {

                    count += numread;

                    final int percent = getPercent(count, length);

                    if (lastPercent != percent) {
                        bundle.putInt(progress, percent);

                        resultReceiver.send(PROGRESS, bundle);

                        lastPercent = percent;
                    }

                    fos.write(buf, 0, numread);
                }

                downloadComplete(resultReceiver, true, "success");
            } else {
                final String msg = conn.getResponseMessage();
                downloadComplete(resultReceiver, false, msg);
            }
        } catch (Exception e) {
            final String msg = e.toString();
            LogUtil.d(msg);
            downloadComplete(resultReceiver, false, msg);
        } finally {
            IOUtil.closeQuietly(is);
            IOUtil.closeQuietly(fos);
        }

        // unregisterReceiver
        unregisterReceiver(receiver);

    }

    private void downloadComplete(ResultReceiver resultReceiver, boolean code, String msg) {

        bundle.putBoolean(messagecode, code);
        bundle.putString(message, msg);
        resultReceiver.send(COMPLETE, bundle);
    }

    private int getPercent(int diliverNum, int queryMailNum) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) diliverNum / (float) queryMailNum * 100);
        return Integer.parseInt(result);
    }

    private class CancelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // stop the service
            onCancel = true;
        }
    }

}
