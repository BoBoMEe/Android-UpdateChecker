package com.bobomee.android.updatechecker.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.bobomee.android.updatechecker.interfaces.CheckCallback;
import com.bobomee.android.updatechecker.interfaces.DownloadCallback;
import com.bobomee.android.updatechecker.manager.ApkInfoManager;
import com.bobomee.android.updatechecker.manager.ThreadManager;
import com.bobomee.android.updatechecker.model.ApkInfo;
import com.bobomee.android.updatechecker.utils.GsonUtil;
import com.bobomee.android.updatechecker.utils.HttpUtil;
import com.bobomee.android.updatechecker.utils.ImitateUtil;
import com.bobomee.android.updatechecker.utils.NetStatusUtil;

import java.util.Map;

/**
 * Created by bobomee on 16/3/13.
 */
public class UpdateManager {

    protected Context context;
    protected String checkUrl;
    protected Map<String, String> params;
    protected CheckCallback checkCallback;

    protected CheckImpl checkImpl;
    protected Handler handler;

    private UpdateManager(UpdateBuilder updateBuilder) {
        this.context = updateBuilder.context;
        this.checkUrl = updateBuilder.checkUrl;
        this.params = updateBuilder.params;
        this.checkCallback = updateBuilder.checkCallback;
        this.handler = new Handler(Looper.getMainLooper());
        this.checkImpl = new CheckImpl(updateBuilder);
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {

        if (null != checkCallback) {
            //call back
            checkCallback.onCheckUpdatePrepare();
        }
        checkImpl.onCheckUpdatePrepare();

        if (NetStatusUtil.isNetworkConnected(context)) { // 检查网络连接是否正常

            ThreadManager.getLongPool().execute(new Runnable() {
                @Override
                public void run() {

                    String postString = HttpUtil.post(checkUrl, params);

                    //TODO：这里模拟数据
                    postString = ImitateUtil.convertToString(context, "checkdata.json");

                    if (!TextUtils.isEmpty(postString)) {

                        final ApkInfo apkInfo = GsonUtil.jsonToBean(postString, ApkInfo.class);

                        ApkInfoManager.INSTANCE.setApkInfo(apkInfo);

                        if (null != checkCallback) {
                            checkCallback.onCheckUpdateCompleted(apkInfo);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    checkCallback.onUICheckUpdateCompleted(apkInfo);
                                }
                            });
                        }
                        checkImpl.onCheckUpdateCompleted(apkInfo);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                checkImpl.onUICheckUpdateCompleted(apkInfo);
                            }
                        });

                    }

                }
            });

        }
    }


    public static class UpdateBuilder {
        protected Context context;
        protected String checkUrl;
        protected Map<String, String> params;
        protected CheckCallback checkCallback;
        protected DownloadCallback downloadCallback;

        protected boolean showCheckPrepareDialog = false;
        protected boolean showCheckCompleteDilog = false;

        protected boolean showProgressNotification = false;
        protected boolean showProgressDialog = false;

        protected boolean isAutoInstall = true;

        public UpdateBuilder(Context ctx) {
            this.context = ctx;
        }

        /**
         * 检查是否有新版本App的URL接口路径
         */
        public UpdateBuilder checkUrl(String checkUrl) {
            this.checkUrl = checkUrl;
            return this;
        }

        /**
         * 设置参数
         *
         * @param params 请求参数
         */
        public UpdateBuilder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        /**
         * 设置检查后的回调
         *
         * @param checkCallback 根据检查信息自定义回调
         */
        public UpdateBuilder checkCallback(CheckCallback checkCallback) {
            this.checkCallback = checkCallback;
            return this;
        }

        /**
         * 下载的回调
         *
         * @param downloadCallback 下载更新自定义回调
         */
        public UpdateBuilder downloadCallback(DownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;
            return this;
        }

        /**
         * 状态栏显示现在进度
         *
         * @param showProgressNotification true 显示，
         *                                 false不显示
         */
        public UpdateBuilder showProgressNotification(boolean showProgressNotification) {
            this.showProgressNotification = showProgressNotification;
            return this;
        }

        /**
         * 显示现在进度对话框
         *
         * @param showProgressDialog true 显示，
         *                           false不显示
         */
        public UpdateBuilder showProgressDialog(boolean showProgressDialog) {
            this.showProgressDialog = showProgressDialog;
            return this;
        }

        /**
         * 是否需要自动安装, 不设置默认自动安装
         *
         * @param isAuto true下载完成后自动安装，false下载完成后需在通知栏手动点击安装
         */
        public UpdateBuilder isAutoInstall(boolean isAuto) {
            this.isAutoInstall = isAuto;
            return this;
        }

        /**
         * 检查完成是否反馈 对话框
         *
         * @param showCheckCompleteDilog 弹出检查完成对话框，
         *                               false不提示
         */
        public UpdateBuilder showCheckCompleteDilog(boolean showCheckCompleteDilog) {
            this.showCheckCompleteDilog = showCheckCompleteDilog;
            return this;
        }

        /**
         * 开始检查时是否反馈 对话框
         *
         * @param showCheckPrepareDialog 弹出正在检查对话框，
         *                               false不提示
         */
        public UpdateBuilder showCheckPrepareDialog(boolean showCheckPrepareDialog) {
            this.showCheckPrepareDialog = showCheckPrepareDialog;
            return this;
        }

        /**
         * 构造UpdateManager对象
         */
        public UpdateManager build() {
            return new UpdateManager(this);
        }
    }
}
