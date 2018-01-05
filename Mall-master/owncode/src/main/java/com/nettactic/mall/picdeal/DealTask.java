package com.nettactic.mall.picdeal;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;

import com.nettactic.mall.R;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.picupload.UploadTask;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.MyBitmapUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

public class DealTask implements Runnable {

	public String TAG = "DealTask";

	/***
	 * mMosaicType =ysbcsc 时 压缩计算保存 状态码
	 */
	public static final int YSBCSC = 1;

	/***
	 * mMosaicType =mosaicjson 时 拼Json 状态码
	 */
	public static final int MOSAICJSON = 2;

	/***
	 * mMosaicType =all 时 压缩计算保存 再 拼Json 状态码
	 */
	public static final int ALL = 3;

	/**
	 * DealTask 状态码
	 */
	private int mMosaicType = YSBCSC;
	private float mMaxPicWidth = MyConstants.INIT_MAXWIDTH;
	private float mMaxPicHeight = MyConstants.INIT_MAXHEIGHT;
	private String mWorkId;
	private SelectpicBean bean;
	public String imgid;
	public boolean mIsUpdatePic = false;
	public Bitmap mPicBitmap = null;
	public Bitmap mMaskBitmap = null;

	public float masklayoutheight;
	public float masklayoutwidth;
	private ACache mCache;
	private String maskUrl;

	@Override
	public void run() {

		switch (mMosaicType) {
		case YSBCSC:
			maskUrl = mCache.getAsString(MyConstants.MYFRAMEMASKURL);
			YSBCSC();
			break;
		case MOSAICJSON:
			maskUrl = mCache.getAsString(MyConstants.MYFRAMEMASKURL);
			MosaicJson();
			break;
		case ALL:
			maskUrl = mCache.getAsString(MyConstants.MYBACKUPFRAMEMASKURL);
			BACKUP();
			YSBCSC();
			MosaicJson();
			break;

		default:
			break;
		}

	}

	private void BACKUP() {
		bean = SelectPicSQLHelper.queryByImgId2DB(imgid);
		mWorkId = bean.getWorkid();
		String mWorksDir = FileUtil.getWorksDir(mWorkId);
		String oldpath = bean.getOldpath();
		String newpath = FileUtil.getFilePath(mWorksDir, imgid,
				MyConstants.JPEG);
		File file = new File(newpath);

		if (!file.exists()) {
			// DealService.cout++;
			Bitmap sdCardImg = FileUtil.getSDCardImg(oldpath, 3200, 3200);

			if (sdCardImg != null) {
				mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
						mMaxPicWidth, mMaxPicHeight);
				String mMaskPath = FileUtil.getUrl2Path(maskUrl);
				mMaskBitmap = FileUtil.getSDCardImg(mMaskPath);

			}
			FileUtil.saveBitmap2Jpeg(mPicBitmap, mWorksDir, imgid,
					MyConstants.JPEG);
			float maskwidth =  Float.parseFloat(bean.getMaskwidth());
			float maskheight =  Float.parseFloat(bean.getMaskheight());
			SelectPicSQLHelper.savaPicMaskInfo(imgid, (float)mPicBitmap.getWidth(),
					(float)mPicBitmap.getHeight(), maskwidth, maskheight);
			String iscompute = bean.getIscompute();
			if (iscompute.equals("false")) {
				initLocation(mPicBitmap.getWidth(), mPicBitmap.getHeight(),
						maskwidth, maskheight);
			}
			if (file.exists()) {
				if (file != null) {
					SelectPicSQLHelper.update2DBsava(imgid, newpath);
					SelectPicSQLHelper.update2DB(imgid, "iscompute", "true");
					String cardinfo = bean.getCardinfo();
					if (!StringUtils.isEmpty(cardinfo)) {
						try {
							DealService.cout++;
							JSONArray oldJSONArray = new JSONArray(cardinfo);
							masklayoutheight =  Float.parseFloat(bean
									.getMasklayoutheight());
							masklayoutwidth = masklayoutheight * maskwidth
									/ maskheight;
							float picwidth = mPicBitmap.getWidth();
							float picheight = mPicBitmap.getHeight();
							float translatex = Float.parseFloat(bean
									.getTranslatex());
							float translatey = Float.parseFloat(bean
									.getTranslatey());
							float scale = Float.parseFloat(bean.getScale());
							float initscale = Float.parseFloat(bean
									.getInitscale());
							String newCardinfo = JsonDao.getNewCardinfo(
									oldJSONArray, maskheight, masklayoutheight,
									picwidth, picheight, translatex,
									translatey, scale, initscale, imgid);
							SelectPicSQLHelper.update2DB(imgid, "cardinfo",
									newCardinfo);
							LogUtils.logd(TAG, DealService.cout
									+ "updatecardinfo--OK");
							String filePath = FileUtil.getFilePath(mWorksDir, mWorkId + imgid,
									MyConstants.PNG);
							if (new File(filePath).exists()) {
								SelectPicSQLHelper.update2DB(imgid, "isexist", "true");
							} else {
								SelectPicSQLHelper.update2DB(imgid, "isexist", "false");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						ToastUtils.show(R.string.json_error);
					}
					// 测试现注释掉
					MyApplication.getUploadTaskMananger().addTask(
							new UploadTask(imgid, mIsUpdatePic));
				}
			} else {
				SelectPicSQLHelper.update2DBsava(imgid, "");
			}
		}

	}

	public DealTask(String imgid, boolean mIsUpdatePic, int mMosaicType) {
		mCache = ACache.get(MyApplication.getContext());
		this.imgid = imgid;
		this.mIsUpdatePic = mIsUpdatePic;
		this.mMosaicType = mMosaicType;

	}

	public String getFileId() {
		return imgid;
	}

	private void MosaicJson() {

		bean = SelectPicSQLHelper.queryByImgId2DB(imgid);
		String iscompute = bean.getIscompute();
		String cardinfo = bean.getCardinfo();

		if ("true".equals(iscompute) && !StringUtils.isEmpty(cardinfo)) {
			try {
				DealService.cout++;
				JSONArray oldJSONArray = new JSONArray(cardinfo);
				float maskheight = Float.parseFloat(bean.getMaskheight());
				float maskwidth = Float.parseFloat(bean.getMaskwidth());
				masklayoutheight = Float.parseFloat(bean.getMasklayoutheight());
				masklayoutwidth = masklayoutheight * maskwidth / maskheight;
				float picwidth = Float.parseFloat(bean.getPicwidth());
				float picheight = Float.parseFloat(bean.getPicheight());
				float translatex = Float.parseFloat(bean.getTranslatex());
				float translatey = Float.parseFloat(bean.getTranslatey());
				float scale = Float.parseFloat(bean.getScale());
				float initscale = Float.parseFloat(bean.getInitscale());

				String newCardinfo = JsonDao.getNewCardinfo(oldJSONArray,
						maskheight, masklayoutheight, picwidth, picheight,
						translatex, translatey, scale, initscale, imgid);

				SelectPicSQLHelper.update2DB(imgid, "cardinfo", newCardinfo);
				LogUtils.logd(TAG, DealService.cout + "updatecardinfo--OK");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			ToastUtils.show(R.string.json_error);
		}
	}

	private void YSBCSC() {
		bean = SelectPicSQLHelper.queryByImgId2DB(imgid);
		mWorkId = bean.getWorkid();
		String mWorksDir = FileUtil.getWorksDir(mWorkId);
		String oldpath = bean.getOldpath();
		String newpath = FileUtil.getFilePath(mWorksDir, imgid,
				MyConstants.JPEG);
		File file = new File(newpath);

		if (!file.exists()) {
			// DealService.cout++;
			Bitmap sdCardImg = FileUtil.getSDCardImg(oldpath, 3200, 3200);

			if (sdCardImg != null) {
				mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
						mMaxPicWidth, mMaxPicHeight);
				String mMaskPath = FileUtil.getUrl2Path(maskUrl);
				mMaskBitmap = FileUtil.getSDCardImg(mMaskPath);

			}
			if (mPicBitmap!=null) {
				FileUtil.saveBitmap2Jpeg(mPicBitmap, mWorksDir, imgid,
						MyConstants.JPEG);
			}
			float maskwidth = Float.parseFloat(bean.getMaskwidth());
			float maskheight = Float.parseFloat(bean.getMaskheight());
			SelectPicSQLHelper.savaPicMaskInfo(imgid, mPicBitmap.getWidth(),
					mPicBitmap.getHeight(), maskwidth, maskheight);
			String iscompute = bean.getIscompute();
			if (iscompute.equals("false")) {
				initLocation(mPicBitmap.getWidth(), mPicBitmap.getHeight(),
						maskwidth, maskheight);
			}
			if (file.exists()) {
				if (file != null) {
					SelectPicSQLHelper.update2DBsava(imgid, newpath);
					SelectPicSQLHelper.update2DB(imgid, "iscompute", "true");
					// 测试现注释掉
					MyApplication.getUploadTaskMananger().addTask(
							new UploadTask(imgid, mIsUpdatePic));
				}
			} else {
				SelectPicSQLHelper.update2DBsava(imgid, "");
			}
		}
	}

	/**
	 * 计算 出图 比例 初始 mScale
	 * 
	 * @param mPicBitmap
	 */
	private void initLocation(float PicW, float PicH, float MaskW, float MaskH) {

		float mInitScale = 1;
		float mTranslateX = 0.0f;
		float mTranslateY = 0.0f;
		mInitScale = MyBitmapUtils.getInitScale(PicW, PicH, MaskW, MaskH);
		float mScale = mInitScale;
		if (PicW < PicH) {
			if (PicH * mScale > MaskH) {
				mTranslateX = 0;
				mTranslateY = -(PicH * mScale - MaskH) / (float) 2;
			} else {
				mTranslateX = -(PicW * mScale - MaskW) / (float) 2;
				mTranslateY = 0;
				
			}
		} else {
			if (PicW * mScale > MaskW) {
				mTranslateX = -(PicW * mScale - MaskW) / (float) 2;
				mTranslateY = 0;
			} else {
				mTranslateX = 0;
				mTranslateY = -(PicH * mScale - MaskH) / (float) 2;
			}
		}

		float mPiMask = Float.parseFloat(bean.getMasklayoutheight())
				/ MaskW;

		float cutx = mTranslateX * mPiMask;
		float cuty = mTranslateY * mPiMask;
		float cutwidth = PicW * mScale * mPiMask;
		float cutheight = PicH * mScale * mPiMask;
		SelectPicSQLHelper.savaComputeInfo(imgid, mInitScale, mScale,
				mTranslateX, mTranslateY, cutx, cuty, cutwidth, cutheight);
		String mWorksDir = FileUtil.getWorksDir(mWorkId);
		Bitmap setMask = setMask(mPicBitmap, mMaskBitmap, mTranslateX,
				mTranslateY, mInitScale);
		FileUtil.saveBitmap2Png(setMask, mWorksDir, mWorkId + imgid,
				MyConstants.PNG);
		if (mMosaicType!=ALL) {
			String filePath = FileUtil.getFilePath(mWorksDir, mWorkId + imgid,
					MyConstants.PNG);
			if (new File(filePath).exists()) {
				SelectPicSQLHelper.update2DB(imgid, "isexist", "true");
			} else {
				SelectPicSQLHelper.update2DB(imgid, "isexist", "false");
			}
		}
		// 计入数据库
		// LogUtils.logd(TAG, DealService.cout + "+mScale:" + mScale);

	}

	/**
	 * @param pic
	 *            用户图片
	 * @param mask
	 *            蒙版
	 * @param picX
	 *            用户图片偏移量X
	 * @param picY
	 *            用户图片偏移量Y
	 * @param scale
	 *            缩放比例
	 * @param degree
	 *            旋转角度
	 * @return Bitmap
	 */
	private Bitmap setMask(Bitmap pic, Bitmap mask, float picX, float picY,
			float scale) {
		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
				Config.ARGB_8888);
		Canvas mCanvas = new Canvas(result);
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		Matrix mMatrix = new Matrix();
		mMatrix.postScale(scale, scale, masklayoutwidth / 2,
				masklayoutheight / 2);
		pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(),
				mMatrix, true);
		mCanvas.drawBitmap(pic, picX, picY, null);
		mCanvas.drawBitmap(mask, 0, 0, mPaint);
		mPaint.setXfermode(null);

		return result;
	}

}
