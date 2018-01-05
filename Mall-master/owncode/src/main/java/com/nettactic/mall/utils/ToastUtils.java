package com.nettactic.mall.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;

/***
 * 提示工具类
 * 
 * @author heshicaihao
 * 
 */
public class ToastUtils {

	private static int yOffset = AndroidUtils.getMyScreenHeight()/2;
    private static Context context = MyApplication.getContext();
    private static Toast toast = null;
	/**
	 * 
	 * 短 提示语
	 * 
	 * @param resId
	 *            资源id
	 */
	public static void show(int resId) {
		 if (toast == null) {
			 toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(context.getString(resId));
				toast.setView(view);
	        } else {
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(context.getString(resId));
				toast.setView(view);
	        }
		toast.show();

	}

	/**
	 * 
	 * 短 提示语
	 * 
	 * @param resStr
	 *            提示语
	 */
	public static void show(String resStr) {
		 if (toast == null) {
			 toast = Toast.makeText(context, resStr,
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(resStr);
				toast.setView(view);
	        } else {
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(resStr);
				toast.setView(view);
	        }
		toast.show();
	}

	/**
	 * 
	 * 短 提示语
	 * 
	 * @param resId
	 *            资源id
	 */
	public static void shortShow(int resId) {
		 if (toast == null) {
			 toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(context.getString(resId));
				toast.setView(view);
	        } else {
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(context.getString(resId));
				toast.setView(view);
	        }
		toast.show();
	}

	/**
	 * 
	 * 短 提示语
	 * 
	 * @param resStr
	 *            提示语
	 */
	public static void shortShow(String resStr) {
		 if (toast == null) {
			 toast = Toast.makeText(context, resStr,
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(resStr);
				toast.setView(view);
	        } else {
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(resStr);
				toast.setView(view);
	        }
		toast.show();
	}

	/**
	 * 
	 * 长 提示语
	 * 
	 * @param resId
	 *            资源id
	 */
	public static void longShow(int resId) {
		 if (toast == null) {
			 toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(context.getString(resId));
				toast.setView(view);
	        } else {
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(context.getString(resId));
				toast.setView(view);
	        }
		toast.show();
	}

	/**
	 * 
	 * 长 提示语
	 * 
	 * @param resStr
	 *            提示语
	 */
	public static void longShow(String resStr) {
		 if (toast == null) {
			 toast = Toast.makeText(context, resStr,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(resStr);
				toast.setView(view);
	        } else {
				toast.setGravity(Gravity.BOTTOM, 0, yOffset);
				LayoutInflater mInflater = LayoutInflater.from(context);
				View view = mInflater.inflate(R.layout.view_my_toast, null);
				TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
				toast_tv.setText(resStr);
				toast.setView(view);
	        }
		toast.show();
	}
}
