package com.bobomee.android.betautifuldialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bobomee on 16/3/12.
 */
public class DialogManager {

    public static Dialog showLoading(Context context, CharSequence text) {
        Dialog progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.loading_dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(text);
        progressDialog.show();
        return progressDialog;
    }


    public static void dismiss(Dialog dialog) {
        if (null != dialog)
            dialog.dismiss();
    }

    public static void dismissDelayed(final Dialog dialog, final long delayed) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (null != dialog)
                    dialog.dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, delayed);
    }

    public static Dialog showText(Context context, CharSequence text) {
        Dialog progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.text_dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(text);
        progressDialog.show();
        return progressDialog;
    }

    public static Dialog showAlert(Context context, CharSequence title, CharSequence message,
                                   CharSequence positiveButton, DialogInterface.OnClickListener positiveClicklistener,
                                   CharSequence negativeButton, DialogInterface.OnClickListener negativeClicklistener,
                                   DialogInterface.OnCancelListener onCancelListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(positiveButton) && null != positiveClicklistener)
            builder.setPositiveButton(positiveButton, positiveClicklistener);
        if (!TextUtils.isEmpty(negativeButton) && null != negativeClicklistener)
            builder.setNegativeButton(negativeButton, negativeClicklistener);

        builder.setCancelable(true);
        builder.setOnCancelListener(onCancelListener);
        return builder.show();
    }

    /**
     * show progress
     */
    public static ProgressDialog showProgress(Context context, CharSequence title, CharSequence message,
                                              CharSequence positiveButton, DialogInterface.OnClickListener positiveClicklistener,
                                              CharSequence negativeButton, DialogInterface.OnClickListener negativeClicklistener,
                                              DialogInterface.OnCancelListener onCancelListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        if (!TextUtils.isEmpty(positiveButton) && null != positiveClicklistener)
            progressDialog.setButton(positiveButton, positiveClicklistener);
        if (!TextUtils.isEmpty(negativeButton) && null != negativeClicklistener)
            progressDialog.setButton2(negativeButton, negativeClicklistener);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(onCancelListener);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * set dismiss or not when clicked the button
     */
    public static void setClickDismiss(DialogInterface dialog, boolean mShowing) {
        ReflectionUtil.setFieldValue(dialog, "mShowing", mShowing);
    }
}
