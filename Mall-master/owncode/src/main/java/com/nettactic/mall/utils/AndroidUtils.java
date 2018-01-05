package com.nettactic.mall.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;

@SuppressWarnings("deprecation")
@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class AndroidUtils {

	private static boolean sIsGoBack = false;
	private static final String TAG = "Utils";

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getRespStatus(String url) {
		int status = -1;
		try {
			HttpHead head = new HttpHead(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(head);
			status = resp.getStatusLine().getStatusCode();
		} catch (Exception e) {
		}
		return status;
	}

	public static String getCurrentProgramPkgName(Context context) {
		/** 获取系统服务 ActivityManager */
		@SuppressWarnings("static-access")
		ActivityManager manager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager
				.getRunningAppProcesses();

		String packageName = listOfProcesses.get(0).processName;
		return packageName;

	}

	public static boolean isInAdvertiseWhiteList(Context context) {
		/** 获取系统服务 ActivityManager */
		@SuppressWarnings("static-access")
		ActivityManager manager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager
				.getRunningAppProcesses();

		for (int j = 0; j < 2; j++) {
			LogUtils.log("TestPackageName",
					"processName=" + listOfProcesses.get(j).processName
							+ "==position=" + j);
			if (listOfProcesses.get(j).processName.equals("com.tencent.mm")
					|| listOfProcesses.get(j).processName
							.equals("com.gionee.aora.market")) {
				return true;
			}
		}
		return false;

	}

	public static void inputFilterSpace(final EditText edit) {
		edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16),
				new InputFilter() {
					public CharSequence filter(CharSequence src, int start,
							int end, Spanned dst, int dstart, int dend) {
						if (src.length() < 1) {
							return null;
						} else {
							char temp[] = (src.toString()).toCharArray();
							char result[] = new char[temp.length];
							for (int i = 0, j = 0; i < temp.length; i++) {
								if (temp[i] == ' ') {
									continue;
								} else {
									result[j++] = temp[i];
								}
							}
							return String.valueOf(result).trim();
						}

					}
				} });
	}

	public static int getNotifyBarHeight(Activity context) {
		Rect frame;
		try {
			frame = new Rect();
			context.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
			return frame.top;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取标题栏的高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getTitleHeight(Activity activity) {
		Rect rect = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT)
				.getTop();
		int titleBarHeight = contentViewTop - statusBarHeight;

		return titleBarHeight;
	}

	/**
	 * 获取标题栏的高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getNavigationBarHeight(Activity activity) {
		try {
			if (checkDeviceHasNavigationBar(activity)) {
				Resources resources = activity.getResources();
				int resourceId = resources.getIdentifier(
						"navigation_bar_height", "dimen", "android");
				if (resourceId > 0) {
					return resources.getDimensionPixelSize(resourceId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void hideNavigationBar(Activity activity) {
		View decorView = activity.getWindow().getDecorView();
		if (AndroidUtils.getAndroidSDKVersion() >= 11) {
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}

	private static boolean checkDeviceHasNavigationBar(Context context) {
		boolean hasNavigationBar = false;
		try {
			Resources rs = context.getResources();
			int id = rs.getIdentifier("config_showNavigationBar", "bool",
					"android");
			if (id > 0) {
				hasNavigationBar = rs.getBoolean(id);
			}

			@SuppressWarnings("rawtypes")
			Class systemPropertiesClass = Class
					.forName("android.os.SystemProperties");
			@SuppressWarnings("unchecked")
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass,
					"qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {
		}
		return hasNavigationBar;
	}

	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		try {
			Resources rs = context.getResources();
			int id = rs.getIdentifier("navigation_bar_height", "dimen",
					"android");
			if (id > 0 && checkDeviceHasNavigationBar(context)) {
				navigationBarHeight = rs.getDimensionPixelSize(id);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return navigationBarHeight;
	}

	public static void finishActivity(Activity activity) {

		if (!hideInputSoftware(activity)) {
			activity.finish();
			exitActvityAnim(activity);

		}
	}

	public static boolean hideInputSoftware(Activity context) {

		try {
			View v = context.getWindow().peekDecorView();
			if (v != null && v.getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getCurrentTwoGState(Context context) {
		SharedPreferences sp = context.getSharedPreferences("twoginfo", 0);
		int currentTwoGState = sp.getInt("twoginfo", 0);
		if (sIsGoBack) {
			currentTwoGState = 0;
		}
		return currentTwoGState;
	}

	public interface LoadListener {
		void startLoad();
	}

	/**
	 * @param context
	 * @param pxValue
	 * @return current screen px value
	 * @description transform the hdpi px value to current screen px value
	 * @author yuwei
	 */
	public static int px2px(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / 1.5 * scale);
	}

	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * @param context
	 * @return the immei number
	 * @description get the immei number of current devices
	 * @author yuwei
	 */
	public static String getIMEI(Context context) {
		String imei = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * @param context
	 * @return version name
	 * @description to call this method you can get the current version name of
	 *              the app,it may defined in the manifest.xml
	 * @author yuwei
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "unknown version";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = " " + packInfo.versionName;
		} catch (Exception e) {
			LogUtils.loge(TAG, LogUtils.getThreadName(), e);
		}
		return versionName;
	}

	/**
	 * @param context
	 * @return current version code
	 * @description to call this method you can get the current version code of
	 *              the app,it may defined in the manifest.mxl
	 * @author yuwei
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionCode = packInfo.versionCode;
		} catch (Exception e) {
			LogUtils.loge(TAG, LogUtils.getThreadName(), e);

		}
		return versionCode;
	}

	/**
	 * @param context
	 * @return
	 * @description to call this method you can get a string contained the immei
	 *              number of the devices android the version name of the
	 *              current app
	 * @author yuwei
	 */
	public static String getAppInfo(Context context) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("IMEI:");
		sb.append(getIMEI(context));
		sb.append(" Application versionName:");
		sb.append(getAppVersionName(context));
		sb.append("]");
		return sb.toString();
	}

	@SuppressLint("NewApi")
	public static Drawable getAppIcon(Context context) {
		Drawable appIcon = null;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			appIcon = packInfo.applicationInfo.loadIcon(packageManager);
			if (appIcon == null) {
				appIcon = packInfo.applicationInfo.loadLogo(packageManager);
			}
		} catch (Exception e) {
			LogUtils.loge(TAG, LogUtils.getThreadName(), e);
		}
		return appIcon;
	}

	/**
	 * @param key
	 * @param defalut
	 * @return system properties
	 * @description get the system properties
	 * @author yuwei
	 */
	public static String getSystemProperties(String key, String defalut) {
		String info = defalut;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Object obj = c.newInstance();
			Method method = c.getMethod("get", String.class, String.class);
			info = (String) method.invoke(obj, key, defalut);
		} catch (Exception e) {
			LogUtils.loge(TAG, LogUtils.getThreadName(), e);
			e.printStackTrace();
			info = defalut;
		}
		return info;
	}

	/**
	 * @param date
	 * @return
	 * @description get the format time string
	 * @author yuwei
	 */
	public static String getDataFormatStr(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}

	/**
	 * @param time
	 * @return
	 * @author yuwei
	 * @description TODO is the day of time equal to current day
	 */
	public static boolean isDateToday(long time) {
		long now = System.currentTimeMillis();
		long t1 = time / (1000 * 60 * 60 * 24);
		long t2 = now / (1000 * 60 * 60 * 24);
		return t1 == t2;
	}

	/**
	 * @param context
	 * @param view
	 *            which the keybord bind to
	 * @author yuwei
	 * @description TODO make the keybord from shown to invisible
	 */
	public static void hidenKeybord(Context context, View view) {
		if (context == null || view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void enterActvityAnim(Activity activity) {
		activity.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public static void exitActvityAnim(Activity activity) {
		activity.overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
	}

	public static void webActivityEnterAnim(Activity activity) {
		activity.overridePendingTransition(R.anim.push_left_in,
				R.anim.zoom_exit);
	}

	public static void webActivityExitAnim(Activity activity) {
		activity.overridePendingTransition(R.anim.zoom_enter,
				R.anim.push_right_out);
	}

	public static boolean isHadPermission(Context context, String permission) {
		try {
			return context.checkPermission(permission, Binder.getCallingPid(),
					Binder.getCallingUid()) == PackageManager.PERMISSION_GRANTED;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean hideInputMethod(Activity activity) {

		try {
			View v = activity.getWindow().peekDecorView();
			if (v != null && v.getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean translateTopBar(Context context) {
		Window window = ((Activity) context).getWindow();
		String versionName = getSystemProperties("ro.miui.ui.version.name", "");
		if (TextUtils.isEmpty(versionName)) {
			return false;
		}
		if (versionName.equals("V6")) {
			Class<? extends Window> clazz = window.getClass();
			try {
				int tranceFlag = 0;
				int darkModeFlag = 0;
				Class<?> layoutParams = Class
						.forName("android.view.MiuiWindowManager$LayoutParams");

				Field field = layoutParams
						.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
				tranceFlag = field.getInt(layoutParams);

				field = layoutParams
						.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);

				Method extraFlagField = clazz.getMethod("setExtraFlags",
						int.class, int.class);
				// 只需要状态栏透明
				extraFlagField.invoke(window, tranceFlag, tranceFlag);
				// 状态栏透明且黑色字体
				extraFlagField.invoke(window, tranceFlag | darkModeFlag,
						tranceFlag | darkModeFlag);
				// 清除黑色字体
				// extraFlagField.invoke(window, 0, darkModeFlag);
				return true;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return false;

	}

	public static Bitmap takeScreenShot(Activity activity) throws Exception {
		LogUtils.log(TAG, LogUtils.getThreadName());
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println(statusBarHeight);
		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题栏 //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	} // 保存到sdcard

	/**
	 * @param packageName
	 * @author yuwei
	 * @description TODO
	 */
	public static void startActivityByPackageName(Context context,
			String packageName) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}

	/**
	 * 同步一下cookie
	 */
	public static void synCookies(Context context, String url, String cookies) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		cookieManager.setCookie(url, cookies);// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}

	/**
	 * 将Uri复制到剪切板。
	 */
	@SuppressLint("NewApi")
	public static void copyUriToClipboard(Uri uri, Context context) {
		// 得到剪贴板管理器
		if (AndroidUtils.getAndroidSDKVersion() >= 11) {
			ClipboardManager cmb = (ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setPrimaryClip(ClipData.newRawUri("URI", uri));
		} else {
			android.text.ClipboardManager cmb = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setText(uri.toString());
		}
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 
	 * @author yuwei
	 * @description TODO
	 */
	public static void setMiuiTopMargain(Context context, View mHeadView) {
		if (translateTopBar(context)) {
			LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();
			params.topMargin = dip2px(context, 15);
			mHeadView.setLayoutParams(params);
		}
	}

	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName)
				&& currentPackageName.equals(context.getPackageName())) {
			return true;
		}
		return false;
	}

	public static boolean isServerWorked(Activity activity, String serverName) {
		ActivityManager myManager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(serverName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取手机屏幕 宽度
	 * 
	 * @return width
	 */
	public static int getScreenWidth(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

	/**
	 * 获取手机屏幕 高度
	 * 
	 * @return height
	 */
	public static int getScreenHeight(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels;
		return height;
	}

	/**
	 * 获取手机屏幕 密度
	 * 
	 * @return height
	 */
	public static float getScreenensity(Activity mActivity) {
		DisplayMetrics metric = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density;
		return density;
	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Activity context) {
		int statusBarHeight = 0;
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object o = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = (Integer) field.get(o);
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
			Rect frame = new Rect();
			context.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
			statusBarHeight = frame.top;
		}
		return statusBarHeight;
	}

	/**
	 * dip转化为px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转化为dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);

	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param context
	 * @param pxValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param context
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	private static long lastClickTime;

	/**
	 * 防止快速点击
	 * 
	 * @return boolean
	 */
	public synchronized static boolean isNoFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1000) {
			return false;
		}
		lastClickTime = time;
		return true;
	}

	/**
	 * 手机截屏
	 * 
	 * @param activity
	 * @param view
	 * @param topMargin
	 * @param bottomMargin
	 * @param leftMargin
	 * @param rightMargin
	 * @return Bitmap
	 */
	public static Bitmap takeScreenShot(Activity activity, View view,
			int topMargin, int bottomMargin, int leftMargin, int rightMargin) {
		// view = mActivity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap screenBitmap = view.getDrawingCache();
		int x = leftMargin;
		int y = topMargin;
		int width = getScreenWidth(activity) - leftMargin - rightMargin;
		int height = getScreenHeight(activity) - topMargin - bottomMargin;
		Bitmap viewBitmap = null;
		if (x > 0 && y > 0 && width > 0 && height > 0) {
			viewBitmap = Bitmap.createBitmap(screenBitmap, x, y, width, height);
		} else {
			viewBitmap = screenBitmap;
		}

		if (viewBitmap.getWidth() > 400 || viewBitmap.getHeight() > 800) {
			viewBitmap = MyBitmapUtils.ResizePiImage(viewBitmap, 400, 800);
		}
		view.destroyDrawingCache();
		return viewBitmap;
	}

	/**
	 * 计算两个手指间的距离
	 * 
	 * @param event
	 * @return
	 */
	@SuppressLint("FloatMath")
	public static float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * 可接受的电话格式有：
		 */
		String expression = "^(1[0-9][0-9])[0-9]{8}$";

		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 判断是否是正常字符
	 * 
	 * @param mChar
	 * @return
	 */
	public static boolean isNormalCharValid(String mChar) {
		boolean isValid = false;

		String expression = "\\:*?<>|\"\n\t";

		CharSequence inputStr = mChar;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	// 判断是否是否本机号码
	public static boolean isMyphone(Context context, EditText myPhone) {
		TelephonyManager phoneMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = phoneMgr.getLine1Number();
		if (myPhone.equals(number)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	public static String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile("(?<!\\d)\\d{6}(?!\\d)");
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/**
	 * 通过包名 获取 版本号
	 * 
	 * @param packageName
	 * @return
	 */
	public static int getVersionCode(String packageName) {
		int versionCode = 0;
		try {
			versionCode = MyApplication.getContext().getPackageManager()
					.getPackageInfo(packageName, 0).versionCode;
		} catch (PackageManager.NameNotFoundException ex) {
			versionCode = 0;
		}
		return versionCode;
	}

	/**
	 * 安装Apk
	 * 
	 * @param context
	 * @param file
	 */
	public static void installAPK(Context context, File file) {
		if (file == null || !file.exists())
			return;
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static boolean isNetworkAvailable(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					System.out.println(i + "===状态==="
							+ networkInfo[i].getState());
					System.out.println(i + "===类型==="
							+ networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 * 
	 * @param view
	 *            控件
	 * @param left
	 *            左
	 * @param top
	 *            上
	 * @param right
	 *            右
	 * @param bottom
	 *            下
	 */
	public static void setMargin(Context context, View view, int left, int top,
			int right, int bottom) {
		int leftpx = AndroidUtils.dip2px(context, left);
		int toppx = AndroidUtils.dip2px(context, top);
		int rightpx = AndroidUtils.dip2px(context, right);
		int bottompx = AndroidUtils.dip2px(context, bottom);
		RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mParams.setMargins(leftpx, toppx, rightpx, bottompx);
		view.setLayoutParams(mParams);
	}

	/**
	 * 获取屏幕信息 保存到SD中
	 * 
	 * @param activity
	 */
	public static void getScreenInfo(Activity activity) {
		int screenWidth = AndroidUtils.getScreenWidth(activity);
		int screenHeight = AndroidUtils.getScreenHeight(activity);
		String screenData = JSONUtil.getScreenData(screenWidth + "",
				screenHeight + "");
		FileUtil.saveFile(screenData, MyConstants.SCREEN,
				MyConstants.SCREEN_INFO, MyConstants.TXT);
	}

	/**
	 * 获取屏幕信息 保存到SD中
	 * 
	 */
	public static int getMyScreenWidth() {
		int screenwidth = 0;
		String screen_infoStr = FileUtil.readFile(MyConstants.SCREEN,
				MyConstants.SCREEN_INFO, MyConstants.TXT);
		try {
			JSONObject screen_info = new JSONObject(screen_infoStr);
			String screenwidthStr = screen_info.optString("screenwidth");
			screenwidth = Integer.parseInt(screenwidthStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return screenwidth;

	}

	/**
	 * 获取屏幕信息 保存到SD中
	 * 
	 */
	public static int getMyScreenHeight() {
		int screenheight = 0;
		String screen_infoStr = FileUtil.readFile(MyConstants.SCREEN,
				MyConstants.SCREEN_INFO, MyConstants.TXT);
		try {
			JSONObject screen_info = new JSONObject(screen_infoStr);
			String screenwidthStr = screen_info.optString("screenheight");
			screenheight = Integer.parseInt(screenwidthStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return screenheight;

	}

	/**
	 * 实现文本复制功能 add by wangqianzhou
	 * 
	 * @param content
	 */
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * 实现粘贴功能 add by wangqianzhou
	 * 
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

	/**
	 * 检查虚拟键是否存在
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean navigationBarExist(Activity activity) {
		WindowManager windowManager = activity.getWindowManager();
		Display d = windowManager.getDefaultDisplay();

		DisplayMetrics realDisplayMetrics = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			d.getRealMetrics(realDisplayMetrics);
		}

		int realHeight = realDisplayMetrics.heightPixels;
		int realWidth = realDisplayMetrics.widthPixels;

		DisplayMetrics displayMetrics = new DisplayMetrics();
		d.getMetrics(displayMetrics);

		int displayHeight = displayMetrics.heightPixels;
		int displayWidth = displayMetrics.widthPixels;

		return (realWidth - displayWidth) > 0
				|| (realHeight - displayHeight) > 0;
	}

	/**
	 * 获取 虚拟按键的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getBottomStatusHeight(Context context) {
		int totalHeight = getTotalScreenHeight(context);

		int contentHeight = getScreenHeight(context);

		return totalHeight - contentHeight;
	}

	/**
	 * 获取屏幕原始尺寸高度，包括虚拟功能键高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getTotalScreenHeight(Context context) {
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi = displayMetrics.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 保存推荐码信息 保存到SD中
	 * 
	 * @param user_id
	 * @param promote_code
	 */
	public static void savePromoteCodeInfo(String user_id, String promote_code) {
		String promotecodeinfo = JSONUtil.getPromoteCodeData(user_id,
				promote_code);
		FileUtil.saveFile(promotecodeinfo, MyConstants.PCODE,
				MyConstants.PCODE_INFO + user_id, MyConstants.JSON);
	}

	/**
	 * 获得推荐码
	 * 
	 * @param user_id
	 * @return
	 */
	public static String getPromoteCodeInfo(String user_id) {
		String PromoteCode = null;
		String promote_code_info_path = FileUtil.getFilePath(MyConstants.PCODE,
				MyConstants.PCODE_INFO + user_id, MyConstants.JSON);
		if (FileUtil.fileIsExists(promote_code_info_path)) {
			String promote_code_infoStr = FileUtil.readFile(MyConstants.PCODE,
					MyConstants.PCODE_INFO + user_id, MyConstants.JSON);
			try {
				JSONObject promote_code_info = new JSONObject(
						promote_code_infoStr);
				String user_idJSON = promote_code_info.optString("user_id");
				if (user_idJSON.equals(user_id)) {
					PromoteCode = promote_code_info.optString("promote_code");
				} else {
					PromoteCode = null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				PromoteCode = null;
			}
		} else {
			PromoteCode = null;
		}
		return PromoteCode;
	}

}
