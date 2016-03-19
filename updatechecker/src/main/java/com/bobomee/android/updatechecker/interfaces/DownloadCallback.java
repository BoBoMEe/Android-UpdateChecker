package com.bobomee.android.updatechecker.interfaces;

/**
 * Created by bobomee on 16/3/13.
 */
public interface DownloadCallback {
    /**
     * UI thread
     */
    void onDownloadPrepare();

    /**
     * UI thread
     */
    void onDownloadProgressChanged(int progress);

    /**
     * background thread
     */
    void onDownloadCompleted(boolean success, String errorMsg);

    /**
     * UI thread
     */
    void onUIDownloadCompleted(boolean success, String errorMsg);

    /**
     * background thread
     */
    void onDownloadCancel();
}
