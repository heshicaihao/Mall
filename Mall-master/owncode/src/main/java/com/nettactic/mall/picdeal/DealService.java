package com.nettactic.mall.picdeal;

import com.nettactic.mall.utils.LogUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class DealService extends Service {

	public static String TAG = "DealService";
	public Thread mThread ;
	public DealManagerThread mDealManagerThread;
	public static int cout = 0;
	private static DealService instance;
	
	@Override
	public void onCreate() {
		LogUtils.logd(TAG, "onCreate");
		instance = this;
		DealTaskManager.getInstance();
		mDealManagerThread = new DealManagerThread();
		super.onCreate();
		LogUtils.logd(TAG, "onCreate++++");
	}

	@Override
	public IBinder onBind(Intent intent) {
		LogUtils.logd(TAG, "onBind");
		return null;
	}

	/**
	 * Android 2.0时 使用的开启Service
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		LogUtils.logd(TAG, "onStart");
	}

	/**
	 * Android 2.0以后 使用的开启Service
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtils.logd(TAG, "onStartCommand");
		String msg =  intent.getExtras().getString("flag");
		String url = intent.getExtras().getString("url");
		LogUtils.logd(TAG, "msg:"+msg);
		LogUtils.logd(TAG, "url:"+url);
		if (mThread == null) {
			mThread = new Thread(mDealManagerThread);
		}
		if (mThread==null) {
			LogUtils.logd(TAG, "mThread:+mThread==null");
		}
		if (mDealManagerThread==null) {
			LogUtils.logd(TAG, "mUploadManagerThread:+mUploadManagerThread==null");
		}
		if (msg.equals("start")) {
			startUpload();
		} else if (msg.equals("stop")) {
//			mDealManagerThread.setStop(true);
			stopSelf();
		}

//		return super.onStartCommand(intent, flags, startId);
		return START_STICKY_COMPATIBILITY;
	}

	@Override
	public void onDestroy() {
		try {
			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		mDealManagerThread.setStop(true);
		mThread = null;
		super.onDestroy();
	}

	public static DealService getInstance() {

		return instance;
	}

	private void startUpload() {
		mThread = new Thread(mDealManagerThread);
		mThread.start();
	}

}