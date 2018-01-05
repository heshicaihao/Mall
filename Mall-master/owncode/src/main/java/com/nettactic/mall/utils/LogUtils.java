package com.nettactic.mall.utils;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class LogUtils {
	private static final String GLOBAL_TAG = "Heshicaihao";
	private static final String TAG = "LogUtils";
	private static boolean sEnableLog = true;
//	private static boolean sEnableLog = false;

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sFormatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSS");

	public static void loadInitConfigs() {
		Log.d(TAG, "loadInitConfigs ...");
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
		}
	}

	public static void log(String tag, String msg) {
		if (sEnableLog) {
			if (null == msg) {
				msg = "";
			}
			Log.i(GLOBAL_TAG + "." + tag, "" + msg);
		}
	}

	public static void logv(String tag, String msg) {
		if (sEnableLog) {
			if (null == msg) {
				msg = "";
			}
			Log.v(GLOBAL_TAG + "." + tag, "" + msg);
		}
	}

	public static void logd(String tag, String msg) {
		if (sEnableLog) {
			if (null == msg) {
				msg = "";
			}
			Log.d(GLOBAL_TAG + "." + tag, msg);
		}
	}

	public static void loge(String tag, String msg) {
		if (sEnableLog) {
			if (null == msg) {
				msg = "";
			}
			Log.e(GLOBAL_TAG + "." + tag + ".E", "" + msg);
		}
	}

	public static void loge(String tag, String msg, Throwable e) {
		if (sEnableLog) {
			if (null == msg) {
				msg = "";
			}
			Log.e(GLOBAL_TAG + "." + tag, "" + msg, e);
		}
	}


	public static String getFunctionName() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
			sb.append("()");
			sb.append(" ");
		} catch (Exception e) {
		}
		return sb.toString();
	}

	public static String getThreadName() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(Thread.currentThread().getName());
			sb.append("-> ");
			sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
			sb.append("()");
			sb.append(" ");
		} catch (Exception e) {
			LogUtils.loge(TAG, e.getMessage());
		}
		return sb.toString();
	}

	public static String getCurrentLocation(String className,
			Exception exception) {
		int line = getLineNumber(exception);
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(Thread.currentThread().getName());
			sb.append("-> ");
			sb.append(className);
			sb.append("-> ");
			sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
			sb.append("()");
			sb.append("-> ");
			sb.append("line in");
			sb.append("-> ");
			sb.append(line);
		} catch (Exception e) {
			LogUtils.loge(TAG, e.getMessage());
		}
		return sb.toString();
	}

	private static int getLineNumber(Exception e) {
		StackTraceElement[] trace = e.getStackTrace();
		if (trace == null || trace.length == 0)
			return -1; //
		return trace[0].getLineNumber();
	}

	public static String getClassName() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("-> ");
			sb.append(Thread.currentThread().getStackTrace()[2].getClassName());
			sb.append(".");
		} catch (Exception e) {

		}
		return sb.toString();
	}
	
	
	
}

