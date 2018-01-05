package com.nettactic.mall.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nettactic.mall.R;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.picupload.UploadTask;
import com.nettactic.mall.utils.AssetsUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;

public class JsonDao {
	public static String TAG = "JsonDao";

	/**
	 * 拼JSONObject 得到作品信息
	 * 
	 * @param listSelectpic
	 * @return
	 */
	public static JSONObject getNewWorksInfo(List<SelectpicBean> listSelectpic) {
		int size = listSelectpic.size();
		JSONObject newJsonObject = new JSONObject();
		try {
			String pagetype = size + "";
			JSONArray bigace = new JSONArray();
			JSONArray smallace = new JSONArray();
			JSONArray backup = new JSONArray();
			JSONArray inpage = new JSONArray();
			String singlePokerStr = null;
			int cout = -1;
			for (int i = 0; i < size; i++) {
				cout++;
				SelectpicBean selectpicBean = listSelectpic.get(i);
				singlePokerStr = selectpicBean.getCardinfo();
				if (size == 2) {
					for (int j = 0; j < size; j++) {
						inpage.put(cout, singlePokerStr);
					}
				} else {
					if (i == 0) {
						bigace.put(cout, singlePokerStr);
					} else if (i == 1) {
						smallace.put(cout - 1, singlePokerStr);
					} else if (i == 2) {
						backup.put(cout - 2, singlePokerStr);
					} else {
						for (int j = 0; j < size - 3; j++) {
							inpage.put(cout - 3, singlePokerStr);
						}
					}
				}
			}
			newJsonObject.put("pagetype", pagetype);
			newJsonObject.put("bigace", bigace);
			newJsonObject.put("smallace", smallace);
			newJsonObject.put("backup", backup);
			newJsonObject.put("inpage", inpage);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newJsonObject;
	}

	/**
	 * 拼JSONObject 得到Cardinfo
	 * 
	 * @param oldJSONArray
	 * @param maskheight
	 * @param masklayoutheight
	 * @param picwidth
	 * @param picheight
	 * @param translatex
	 * @param translatey
	 * @param scale
	 * @param initscale
	 * @param imgid
	 * @return singlePokerJSONArrayStr
	 */
	public static String getNewCardinfo(JSONArray oldJSONArray,
			float maskheight, float masklayoutheight, float picwidth,
			float picheight, float translatex, float translatey, float scale,
			float initscale, String imgid) {
		String singlePokerJSONArrayStr = null;
		JSONArray array = new JSONArray();
		int cout = -1;
		try {
			for (int i = 0; i < oldJSONArray.length(); i++) {
				JSONObject object = (JSONObject) oldJSONArray.get(i);
				String elementType = object.optString("elementType");
				cout++;
				if ("frame".equals(elementType)) {

					JSONObject newFrameJson = getNewFrame(object, maskheight,
							masklayoutheight, picwidth, picheight, translatex,
							translatey, scale, initscale, imgid);
					array.put(cout, newFrameJson);
				} else {
					array.put(cout, object);
				}
			}
			singlePokerJSONArrayStr = array.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return singlePokerJSONArrayStr;

	}

	/**
	 * 更新 Fame 信息
	 * 
	 * @param oldFrameJson
	 * @param maskheight
	 * @param masklayoutheight
	 * @param picwidth
	 * @param picheight
	 * @param translatex
	 * @param translatey
	 * @param scale
	 * @param initscale
	 * @param imgid
	 * @return
	 */
	public static JSONObject getNewFrame(JSONObject oldFrameJson,
			float maskheight, float masklayoutheight, float picwidth,
			float picheight, float translatex, float translatey, float scale,
			float initscale, String imgid) {

		float mPiMask = masklayoutheight / (float) maskheight;
		JSONObject newFrameJson = new JSONObject();
		try {
			newFrameJson.put("frameID", oldFrameJson.get("frameID"));
			newFrameJson.put("frameMaskUrl", oldFrameJson.get("frameMaskUrl"));
			newFrameJson.put("tier", oldFrameJson.get("tier"));
			newFrameJson.put("frameUrl", oldFrameJson.get("frameUrl"));
			newFrameJson.put("type", oldFrameJson.get("type"));
			newFrameJson.put("id", oldFrameJson.get("id"));
			newFrameJson.put("distortion", oldFrameJson.get("distortion"));
			newFrameJson.put("height", oldFrameJson.get("height"));
			newFrameJson.put("rotation", oldFrameJson.get("rotation"));
			newFrameJson.put("pageID", oldFrameJson.get("pageID"));
			newFrameJson.put("alpha", oldFrameJson.get("alpha"));
			newFrameJson.put("frameWidth", oldFrameJson.get("frameWidth"));
			newFrameJson.put("left", oldFrameJson.get("left"));
			newFrameJson.put("theOrder", oldFrameJson.get("theOrder"));
			newFrameJson.put("right", oldFrameJson.get("right"));
			newFrameJson.put("top", oldFrameJson.get("top"));
			newFrameJson.put("frameHeight", oldFrameJson.get("frameHeight"));
			newFrameJson.put("relateID", oldFrameJson.get("relateID"));
			newFrameJson.put("isEdit", oldFrameJson.get("isEdit"));
			newFrameJson.put("width", oldFrameJson.get("width"));
			newFrameJson.put("imgid", oldFrameJson.get("imgid"));
			newFrameJson.put("elementType", oldFrameJson.get("elementType"));
			newFrameJson.put("relateFID", oldFrameJson.get("relateFID"));
			newFrameJson.put("bottom", oldFrameJson.get("bottom"));
			newFrameJson.put("y", oldFrameJson.get("y"));
			newFrameJson.put("x", oldFrameJson.get("x"));

			newFrameJson.put("cutx", "" + translatex * mPiMask);
			newFrameJson.put("cuty", "" + translatey * mPiMask);
			newFrameJson.put("cutwidth", "" + picwidth * scale * mPiMask);
			newFrameJson.put("cutheight", "" + picheight * scale * mPiMask);
			newFrameJson.put("remark",
					getRemarkInfo("" + initscale, "" + scale, "" + mPiMask));
			newFrameJson.put("url", "imageid://" + imgid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newFrameJson;

	}

	/***
	 * 拼字符串存服务器
	 * 
	 * @param mInitScale
	 * @param mScale
	 * @param mPiMask
	 * @return
	 */
	public static String getRemarkInfo(String mInitScale, String mScale,
			String mPiMask) {
		JSONObject object = new JSONObject();
		try {
			object.put("mInitScale", mInitScale);
			object.put("mScale", mScale);
			object.put("mPiMask", mPiMask);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 将初始模板信息 转化全部 单页信息
	 * 
	 * @param myJsonObject
	 * @return List<String>
	 */
	public static List<String> getListSinglePoker(JSONObject myJsonObject) {
		List<String> list = new ArrayList<String>();
		try {

			JSONArray bigace = myJsonObject.getJSONArray("bigace");
			if (bigace != null && bigace.length() > 0) {
				String bigaceStr = (String) bigace.get(0);
				list.add(bigaceStr);
			}

			JSONArray smallace = myJsonObject.getJSONArray("smallace");
			if (smallace != null && smallace.length() > 0) {
				String smallaceStr = (String) smallace.get(0);
				list.add(smallaceStr);
			}

			JSONArray backup = myJsonObject.getJSONArray("backup");
			if (backup != null && backup.length() > 0) {
				String backupStr = (String) backup.get(0);
				list.add(backupStr);
			}
			JSONArray inpage = myJsonObject.getJSONArray("inpage");
			if (inpage != null && inpage.length() > 0) {
				for (int i = 0; i < inpage.length(); i++) {
					String inpageStr = (String) inpage.get(i);
					list.add(inpageStr);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 在JSONArray 中根据key 选出 同一类elementType
	 * 
	 * @param myJsonObject
	 * @param key
	 * @return
	 */
	public static JSONArray getElementTypeArray(JSONArray myJsonObject,
			String key) {
		JSONArray jsonarray = new JSONArray();
		int cout = -1;
		try {
			for (int i = 0; i < myJsonObject.length(); i++) {
				JSONObject jsonobject;
				jsonobject = (JSONObject) myJsonObject.get(i);
				String elementType = jsonobject.getString("elementType");
				if (key.equals(elementType)) {
					cout++;
					jsonarray.put(cout, jsonobject);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonarray;
	}

	/**
	 * 拼Json 给浏览图片页
	 * 
	 * @param code
	 * @param work_id
	 * @param goods_id
	 * @param number
	 * @param pic_id
	 * @param cover_id
	 * @return
	 */
	public static String getIntentData(String code, String work_id,
			String goods_id, String number, String pic_id, String cover_id) {
		JSONObject object = new JSONObject();
		try {
			object.put("code", code);
			JSONObject contentJson = new JSONObject();
			contentJson.put("work_id", work_id);
			contentJson.put("goods_id", goods_id);
			contentJson.put("number", number);
			contentJson.put("pic_id", pic_id);
			contentJson.put("cover_id", cover_id);
			object.put("content", contentJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 处理不成功的继续 压缩 上传
	 * 
	 * @param work_id
	 */
	public static void doUnsuccessful(String work_id) {
		List<String> list = SelectPicSQLHelper.querySelectPicImgid(work_id,
				"true", "false");

		LogUtils.logd("UPTaskManager", "list.size:" + list.size());

		for (int i = 0; i < list.size(); i++) {
			String imgid = list.get(i);
			MyApplication.getUploadTaskMananger().addTask(
					new UploadTask(imgid, false));
		}
	}

	/**
	 * 拼 支付宝 提现 信息
	 */
	public static String getAlpayNoteInfo(String styles, String ali_name,
			String ali_account) {
		Context context = MyApplication.getContext();
		String info = context.getString(R.string.withdrawals_styles_add)
				+ styles + ";";
		info += context.getString(R.string.ali_name_add) + ali_name + ";";
		info += context.getString(R.string.ali_account_add) + ali_account + ";";

		return info;

	}

	/**
	 * 拼 银行卡 提现 信息
	 */
	public static String getUnionPayNoteInfo(String styles,
			String unionpay_name, String unionpay_account,
			String unionpay_bank_name) {
		Context context = MyApplication.getContext();
		String info = context.getString(R.string.withdrawals_styles_add)
				+ styles + ";";
		info += context.getString(R.string.unionpay_name_add) + unionpay_name
				+ ";";
		info += context.getString(R.string.unionpay_account_add)
				+ unionpay_account + ";";
		info += context.getString(R.string.unionpay_bank_name_add)
				+ unionpay_bank_name + ";";

		return info;

	}

	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static int upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				// String dirstr = folderPath + ze.getName();
				String dirstr = folderPath;
				dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		String lastDir = baseDir;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				lastDir += (dirs[i] + "/");
				File dir = new File(lastDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
			File ret = new File(lastDir, dirs[dirs.length - 1]);
			return ret;
		} else {

			return new File(baseDir, absFileName);

		}

	}

	/**
	 * 在assets 获取素材包 到SD解压。
	 * 
	 * @throws ZipException
	 * @throws IOException
	 */
	public static void getMaterial() {
		String fileName = MyConstants.MATERIAL + MyConstants.ZIP;
		String zipPath = FileUtil.getSDPath() + "/" + MyConstants.CACH_DIR;
		File zipdirFirstFolder = new File(zipPath);
		if (!zipdirFirstFolder.exists()) {
			zipdirFirstFolder.mkdirs();
		}
		String filePath = FileUtil.getFilePath(MyConstants.CACH_DIR,
				MyConstants.MATERIAL, MyConstants.ZIP);
		try {
			if (!new File(filePath).exists()) {
				AssetsUtils.copyDataToSD(MyApplication.getContext(), fileName,
						filePath);
				LogUtils.logd(TAG, "copyDataToSD");
			}
			File zipFile = new File(filePath);
			String folderPath = FileUtil.getSDPath() + "/"
					+ MyConstants.CACH_DIR;
			File folderdirFirstFolder = new File(folderPath);
			if (!folderdirFirstFolder.exists()) {
				folderdirFirstFolder.mkdirs();
			}
			LogUtils.logd(TAG, "folderPath:" + filePath);
			String cachePath = FileUtil.getFilePath(MyConstants.CACHE_DIR,
					"black_down_01", MyConstants.PNG);
			if (!new File(cachePath).exists()) {
				JsonDao.upZipFile(zipFile, folderPath);
				LogUtils.logd(TAG, "upZipFile");
			}
		} catch (ZipException e) {
			e.printStackTrace();
			LogUtils.logd(TAG, "ZipException:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.logd(TAG, "IOException:" + e);
		}
	}
}
