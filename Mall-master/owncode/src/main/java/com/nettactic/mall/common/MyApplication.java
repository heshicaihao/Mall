package com.nettactic.mall.common;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.jpush.android.api.JPushInterface;

import com.nettactic.mall.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.picdeal.DealTaskManager;
import com.nettactic.mall.picupload.UploadTaskManager;

public class MyApplication extends Application {

	// 用于存放倒计时时间
	public static Map<String, Long> TimeMap;
	public static UploadTaskManager mUploadTaskMananger;
	public static DealTaskManager mDealTaskManager;
	private static Context context;
	private static boolean mIsAtLeastGB;
	private static String PREF_NAME = "creativelocker.pref";
	private static String LAST_REFRESH_TIME = "last_refresh_time.pref";

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		initData();

	}

	/**
	 * 获得当前app运行的Context
	 * 
	 * @return
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * 获得上传任务管理器
	 * 
	 * @return
	 */
	public static UploadTaskManager getUploadTaskMananger() {
		return mUploadTaskMananger;
	}

	/**
	 * 获得上传任务管理器
	 * 
	 * @return
	 */
	public static DealTaskManager getDealTaskManager() {
		return mDealTaskManager;
	}

	public void initData() {
		
		mUploadTaskMananger = UploadTaskManager.getInstance();
		mDealTaskManager = DealTaskManager.getInstance();
		JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

		DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
				.showImageForEmptyUri(R.mipmap.blank_bg)
				.showImageOnFail(R.mipmap.blank_bg)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)//缓存一百张图片
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
		// 网络状态监听
	}

	{

		// 微信 appid appsecret
		PlatformConfig.setWeixin(MyConstants.WX_APP_ID,
				MyConstants.WX_APP_SECRET);

		 // 新浪微博 appkey appsecret
		 PlatformConfig.setSinaWeibo(MyConstants.SINA_APP_KEY,
		 MyConstants.SINA_APP_SECRET);

		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone(MyConstants.QQ_APP_ID, MyConstants.QQ_APP_KEY);

	}

	/***
	 * 记录列表上次刷新时间
	 * 
	 * @return void
	 * @param key
	 * @param value
	 */
	public static void putToLastRefreshTime(String key, String value) {
		SharedPreferences preferences = getPreferences(LAST_REFRESH_TIME);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		apply(editor);
	}

	@SuppressLint("NewApi")
	public static void apply(SharedPreferences.Editor editor) {
		if (mIsAtLeastGB) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	public static void set(String key, boolean value) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(key, value);
		apply(editor);
	}

	public static void set(String key, String value) {
		Editor editor = getPreferences().edit();
		editor.putString(key, value);
		apply(editor);
	}

	public static boolean get(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	public static String get(String key, String defValue) {
		return getPreferences().getString(key, defValue);
	}

	public static int get(String key, int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	public static long get(String key, long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	public static float get(String key, float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	public static SharedPreferences getPreferences() {
		SharedPreferences pre = context.getSharedPreferences(PREF_NAME,
				Context.MODE_MULTI_PROCESS);
		return pre;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	public static SharedPreferences getPreferences(String prefName) {
		return context.getSharedPreferences(prefName,
				Context.MODE_MULTI_PROCESS);
	}

}
