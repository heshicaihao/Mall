package com.nettactic.mall.utils;



import com.nettactic.mall.picdeal.DealService;
import com.nettactic.mall.picupload.UploadService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ServiceUtils {

	public static void startUploadService(Context context,Activity activity) {
		Intent it = new Intent(context, UploadService.class);
		it.putExtra("flag", "start");
		it.putExtra("url", "123456");
		activity.startService(it);
	}
	
	public static void stopUploadService(Context context,Activity activity) {
		Intent it = new Intent(context, UploadService.class);
		it.putExtra("flag", "stop");
		it.putExtra("url", "123456");
		activity.startService(it);
	}
	
	public static void startDealService(Context context,Activity activity) {
		Intent it = new Intent(context, DealService.class);
		it.putExtra("flag", "start");
		it.putExtra("url", "123456");
		activity.startService(it);
	}
	
	public static void stopDealService(Context context,Activity activity) {
		Intent it = new Intent(context, DealService.class);
		it.putExtra("flag", "stop");
		it.putExtra("flag", "123456");
		activity.startService(it);
	}

}
