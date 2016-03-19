package com.bobomee.android.updatechecker.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bobomee.android.betautifuldialog.DialogManager;
import com.bobomee.android.updatechecker.R;

/**
 * Created by bobomee on 16/3/13.
 */
public class DownloadProgressDialogImpl {

    protected DownloadImpl downloadImpl;
    protected ProgressDialog progressDialog;
    protected Context context;

    public DownloadProgressDialogImpl(DownloadImpl downloadImpl, UpdateManager.UpdateBuilder builder) {
        this.downloadImpl = downloadImpl;
        this.context = builder.context;
    }

    public void onDownloadPrepare() {
        progressDialog = DialogManager.showProgress(context,
                context.getString(R.string.download),
                context.getString(R.string.downloadprepare),
                context.getString(R.string.background),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                },
                context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadImpl.oncancel();
                    }
                },
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                }
        );
    }

    public void onDownloadProgressChanged(int progress) {
        if (progressDialog.isShowing()) {
            progressDialog.setProgress(progress);
            progressDialog.setMessage(context.getResources().getString(R.string.downloading, progress));
        }
    }

    public void onDownloadCompleted(boolean success, String errorMsg) {

    }

    public void onUIDownloadCompleted(boolean success, String errorMsg) {
        if (progressDialog.isShowing()) {
            if (success) {
                progressDialog.setMessage(context.getString(R.string.downloadsuccess));
            } else {
                progressDialog.setMessage(context.getString(R.string.downloadfailue));
            }
        }
        progressDialog.dismiss();
    }

    public void onDownloadCancel() {
        progressDialog.dismiss();
    }
}
