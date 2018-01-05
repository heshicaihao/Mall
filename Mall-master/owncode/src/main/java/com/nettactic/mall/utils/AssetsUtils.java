package com.nettactic.mall.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nettactic.mall.common.MyApplication;

public class AssetsUtils {

	/**
	 * Assets 中读 json
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					context.getAssets().open(fileName), "UTF-8"));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	/**
	 * Assets 中读 图片
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImage(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static String getTemplateData(int template_id) {
		String template_name = "template_dome" + template_id + ".json";
		String json = AssetsUtils.getJson(MyApplication.getContext(),
				template_name);
		return json;
	}

	/**
	 * 将assets 中的资源下载到 SD
	 * 
	 * @param context
	 * @param fileName
	 *            assets中的文件名
	 * @param outFilePath
	 * @throws IOException
	 */
	public static void copyDataToSD(Context context, String fileName,
			String outFilePath) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(outFilePath);
		myInput = context.getAssets().open(fileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

}
