package com.bobomee.android.updatecheackerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bobomee.android.updatechecker.core.UpdateManager;
import com.bobomee.android.updatechecker.interfaces.CheckCallback;
import com.bobomee.android.updatechecker.interfaces.SimpleDownloadCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbar) setSupportActionBar(toolbar);
    }

    public void cheack(View v) {

        UpdateManager checkManager =
                new UpdateManager.UpdateBuilder(this)
                        .checkUrl(null).
                        params(null).
                        showCheckPrepareDialog(true).
                        showCheckCompleteDilog(true).
                        showProgressDialog(true).
                        showProgressNotification(true).
                        isAutoInstall(true).
                        checkCallback(new CheckCallback() {
                            @Override
                            public void onCheckUpdatePrepare() {

                            }

                            @Override
                            public void onCheckUpdateCompleted(Object checkInfo) {

                            }

                            @Override
                            public void onUICheckUpdateCompleted(Object checkInfo) {

                            }
                        }).
                        downloadCallback(new SimpleDownloadCallback() {
                            @Override
                            public void onDownloadProgressChanged(int progress) {
                                super.onDownloadProgressChanged(progress);
                                Toast.makeText(MainActivity.this, progress + "", Toast.LENGTH_LONG).show();
                            }
                        }).
                        build();

        checkManager.checkUpdate();
    }

}
