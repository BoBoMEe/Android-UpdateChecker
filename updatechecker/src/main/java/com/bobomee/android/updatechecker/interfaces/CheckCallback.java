package com.bobomee.android.updatechecker.interfaces;

/**
 * Created by bobomee on 16/3/13.
 */
public interface CheckCallback {
    /**
     * UI thread
     */
    void onCheckUpdatePrepare();

    /**
     * background thread
     */
    void onCheckUpdateCompleted(Object checkInfo);

    /**
     * UI thread
     */
    void onUICheckUpdateCompleted(Object checkInfo);
}
