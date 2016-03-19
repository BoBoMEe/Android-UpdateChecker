package com.bobomee.android.updatechecker.core;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bobomee.android.betautifuldialog.DialogManager;
import com.bobomee.android.updatechecker.R;
import com.bobomee.android.updatechecker.interfaces.SimpleCheckCallback;
import com.bobomee.android.updatechecker.manager.ApkInfoManager;
import com.bobomee.android.updatechecker.utils.AppUtil;
import com.bobomee.android.updatechecker.utils.LogUtil;

/**
 * Created by bobomee on 16/3/13.
 */
public class CheckImpl extends SimpleCheckCallback {

    protected boolean showCheckCompleteDilog = false;
    protected boolean showCheckPrepareDialog = false;
    protected Context context;
    protected Dialog dialog;
    protected boolean hasUpdate = false;

    protected DownloadImpl downloadImpl;

    public CheckImpl(UpdateManager.UpdateBuilder builder) {
        this.showCheckCompleteDilog = builder.showCheckCompleteDilog;
        this.context = builder.context;
        this.showCheckPrepareDialog = builder.showCheckPrepareDialog;
        this.downloadImpl = new DownloadImpl(builder);
    }

    @Override
    public void onCheckUpdatePrepare() {
        super.onCheckUpdatePrepare();
        //default action
        if (showCheckPrepareDialog) {
            dialog = DialogManager.showLoading(context, context.getString(R.string.cheack_prepare));
        }

    }

    @Override
    public void onCheckUpdateCompleted(Object checkInfo) {
        super.onCheckUpdateCompleted(checkInfo);

        String clientVersion = AppUtil.getVersionName(context);
        String serverVerion = ApkInfoManager.INSTANCE.getApkInfo().getVersionName();

        assert clientVersion != null;
        if (serverVerion.compareTo(clientVersion) > 0) {
            // have new version
            hasUpdate = true;
        } else {
            hasUpdate = false;
        }

    }

    @Override
    public void onUICheckUpdateCompleted(Object checkInfo) {
        super.onUICheckUpdateCompleted(checkInfo);
        DialogManager.dismiss(dialog);

        if (hasUpdate) {
            //if show ，download after user empowered
            if (showCheckCompleteDilog) {
                DialogManager.showAlert(context,
                        context.getString(R.string.cheack_complete_newversion),
                        ApkInfoManager.INSTANCE.getApkInfo().getUpdateMessage(),
                        context.getString(R.string.update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtil.e("update");
                                downloadImpl.onDownloadPrepare();
                                downloadImpl.onCheckComplete2Down(context);
                            }
                        },
                        context.getString(R.string.later), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtil.e("later");
                            }
                        },
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                LogUtil.e("cancel");
                            }
                        }
                );
            } else {
                downloadImpl.onDownloadPrepare();
                downloadImpl.onCheckComplete2Down(context);
            }
        } else {
            if (showCheckCompleteDilog) {
                dialog = DialogManager.showText(context, context.getString(R.string.cheack_complete_noupdate));
                DialogManager.dismissDelayed(dialog, 3000);
            }
        }

    }

}


