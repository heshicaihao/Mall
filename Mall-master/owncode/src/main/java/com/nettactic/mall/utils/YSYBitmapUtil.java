package com.nettactic.mall.utils;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class YSYBitmapUtil {

	/**
	 * 根据 本地资源 限定大小读取文件
	 * 
	 * @param context
	 * @param resource
	 * @return
	 */
	public static Bitmap getBitmap(Context context, int resource) {
		InputStream is = context.getResources().openRawResource(resource);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inPreferredConfig = Bitmap.Config.RGB_565;
		option.inJustDecodeBounds = false;
		option.inSampleSize =1;

		option.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(is, null, option);
	}
	
	/**
	 * 根据 本地资源 限定大小读取文件
	 * 
	 * @param context
	 * @param resource
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(Context context, int resource, int width,
			int height) {
		InputStream is = context.getResources().openRawResource(resource);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inPreferredConfig = Bitmap.Config.RGB_565;
		option.inJustDecodeBounds = false;
		option.inSampleSize = calculateSampleSize(option, width, height);

		option.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(is, null, option);
	}

	/**
	 * 根据 路径 限定大小读取文件
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(String path, int width, int height) {
		Options option = new Options();
		option.inPreferredConfig = Bitmap.Config.RGB_565;
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, option);
		option.inSampleSize = calculateSampleSize(option, width, height);

		option.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, option);
	}

	/**
	 * BitmapUtils 的SampleSize 计算方法
	 * 
	 * @param option
	 * @param width
	 * @param height
	 * @return 
	 */
	private static int calculateSampleSize(Options option, int width, int height) {
		int sampleSize = 1;
		if (option.outWidth > width && option.outHeight > height) {
			int widthRate = Math.round((float) option.outWidth / (float) width);
			int heightRate = Math.round((float) option.outHeight
					/ (float) height);

			sampleSize = Math.max(widthRate, heightRate);
			if (sampleSize <= 0) {
				sampleSize = 1;
			}
		}
		return sampleSize;
	}

	/**
	 * Android源码，一种动态计算SampleSize 的方法。
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}
