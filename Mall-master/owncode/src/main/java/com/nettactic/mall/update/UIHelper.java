package com.nettactic.mall.update;


import com.nettactic.mall.update.DownloadService.DownloadBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class UIHelper {

	public static void openDownLoadService(Context context, String downurl,
            String tilte) {
        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {}
        };
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {}

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadBinder binder = (DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();

            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

}
