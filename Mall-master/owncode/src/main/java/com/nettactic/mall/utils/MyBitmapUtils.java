package com.nettactic.mall.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Base64;

@SuppressWarnings("deprecation")
public class MyBitmapUtils {

	@SuppressWarnings("null")
	public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight,
					1024 * 600);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);
			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bmp2.recycle();
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return baos.toByteArray();
	}

	private static double getScaling(int src, int des) {
		// 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}

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

	/**
	 * 读取资源文件
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 画制定大小的图片
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * 生成全透明的图片
	 * 
	 * @param sourceImg
	 * @param number
	 *            0
	 * @return
	 */
	public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00000000);
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);
		return sourceImg;
	}

	/**
	 * 合成图片
	 */
	// private void MakePic() {
	// mMobBitmapR = BitmapUtils.resizeImage(
	// imageLoader.getBitmapByUrl(mMobileUrl), mMobileWidth,
	// mMobileHeight);
	// mFrameBitmapR = BitmapUtils.resizeImage(
	// imageLoader.getBitmapByUrl(mFrameUrl), mFrameWidth,
	// mFrameHeight);
	// Bitmap mResultBitmapR = BitmapUtils.resizeImage(mResultBitmap,
	// mFrameWidth, mFrameHeight);
	// if (mMobBitmapR != null && mFrameBitmapR != null
	// && mResultBitmap != null) {
	// Bitmap composeBitmap = BitmapUtils.getComposeBitmap(mMobBitmapR,
	// mFrameBitmapR, mResultBitmapR, mFrameX, mFrameY);
	// FileUtil.saveBitmap(composeBitmap, "mImgageId");
	// // byte[] bitmap2Bytes = BitmapUtils.Bitmap2Bytes(composeBitmap);
	//
	// Toast.makeText(getApplicationContext(),
	// getString(R.string.save_success), Toast.LENGTH_SHORT)
	// .show();
	// } else {
	// Toast.makeText(getApplicationContext(),
	// getString(R.string.please_edit_the_save),
	// Toast.LENGTH_SHORT).show();
	// }

	// }

	/**
	 * 合成图片
	 * 
	 * @param ABitmap 第一张图片
	 * @param BBitmap 第二张图片
	 * @param x
	 *            第二张图片 在第一张图片的x坐标
	 * @param y
	 *            第二张图片在第一张图片的x坐标
	 * @return Bitmap
	 */
	public static Bitmap getComposeBitmap(Bitmap ABitmap, Bitmap BBitmap,
			Bitmap CBitmap, int x, int y) {

		Bitmap ReBitmap = Bitmap.createBitmap(ABitmap.getWidth(),
				ABitmap.getHeight(), ABitmap.getConfig());
		Canvas canvas = new Canvas(ReBitmap);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawBitmap(ABitmap, new Matrix(), paint);
		canvas.drawBitmap(BBitmap, x, y, paint);
		canvas.drawBitmap(CBitmap, x, y, paint);
		return ReBitmap;
	}

	// 使用BitmapFactory.Options的inSampleSize参数来缩放
	public static Bitmap resizeImage2(String path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 不加载bitmap到内存中
		BitmapFactory.decodeFile(path, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize = 1;

		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			options.inSampleSize = sampleSize;
		}
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static float ResizeScale(Bitmap RBitmap, int MaxW, int MaxH) {
		Bitmap BitmapOrg = RBitmap;
		int PicWidth = BitmapOrg.getWidth();
		int PicHeight = BitmapOrg.getHeight();
		float PicPi = ((float) PicWidth) / PicHeight;
		float MaxPi = ((float) MaxW) / MaxH;
		float scale = 0;
		if (PicWidth > MaxW && PicHeight <= MaxH) {
			scale = ((float) MaxW) / PicWidth;
		} else if (PicWidth <= MaxW && PicHeight > MaxH) {
			scale = ((float) MaxH) / PicHeight;
		} else if (PicWidth <= MaxW && PicHeight <= MaxH) {
			scale = 1;
		} else if (PicWidth > MaxW && PicHeight > MaxH) {
			if (PicPi > MaxPi) {
				scale = ((float) MaxW) / PicWidth;
			} else {
				scale = ((float) MaxH) / PicHeight;
			}
		}
		return scale;

	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBitmap;

	}

	/**
	 * 图片转成string
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	// /**
	// * 图片转成string
	// *
	// * @param bitmap
	// * @return
	// */
	// public static ByteArrayOutputStream convertIconToString2(Bitmap bitmap) {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
	// bitmap.compress(CompressFormat.JPEG, 100, baos);
	// // byte[] appicon = baos.toByteArray();// 转为byte数组
	// return baos;
	//
	// }

	// 将Bitmap转换成InputStream
	public static ByteArrayInputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 图片转成string
	 * 
	 * @param bitmap
	 * @return
	 */
	// public static ByteArrayOutputStream convertIconToString2(Bitmap bitmap) {
	// ByteArrayInputStream baos = new ByteArrayInputStream();// outputstream
	// bitmap.compress(CompressFormat.JPEG, 100, baos);
	// // byte[] appicon = baos.toByteArray();// 转为byte数组
	// return baos;
	//
	// }

	/**
	 * string转成bitmap
	 * 
	 * @param st
	 */
	public static Bitmap convertStringToIcon(String st) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 算初始化图片显示比例
	 * 
	 * @param mPic
	 * @param mMask
	 */
	public static float getInitScale(Bitmap mPic, Bitmap mMask) {
		float mScale = 1;
		int PicW = mPic.getWidth();
		int PicH = mPic.getHeight();
		int PicMin = (PicW < PicH) ? PicW : PicH;
		int PicMaX = (PicW > PicH) ? PicW : PicH;
		float PicPi = (float) PicW / PicH;

		int MaskW = mMask.getWidth();
		int MaskH = mMask.getHeight();
		int MaskMin = (MaskW < MaskH) ? MaskW : MaskH;
		int MaskMaX = (MaskW > MaskH) ? MaskW : MaskH;
		float MaskPi = (float) MaskW / MaskH;

		if (MaskW < MaskH) {
			if (PicW < PicH) {
				if (MaskPi < PicPi) {
					mScale = (float) MaskMaX / PicMaX;// OK
				} else if (MaskPi == PicPi) {
					mScale = (float) MaskMin / PicMin;// OK
				} else {
					mScale = (float) MaskMin / PicMin;
				}
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;// OK
			} else {
				mScale = (float) MaskMaX / PicMin;// OK
			}
		} else if (MaskW == MaskH) {
			if (PicW < PicH) {
				mScale = (float) MaskMaX / PicMin;// OK
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;// OK
			} else {
				mScale = (float) MaskMaX / PicMin;// OK
			}
		} else {
			if (PicW < PicH) {
				mScale = (float) MaskMaX / PicMin;
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;
			} else {
				mScale = (float) MaskMaX / PicMin;
			}
		}
		return mScale;
	}

	/**
	 * 算初始化图片显示比例
	 * 
	 * @param PicW
	 * @param PicH
	 * @param MaskW
	 * @param MaskH
	 */
	public static float getInitScale(float PicW, float PicH, float MaskW, float MaskH) {
		float mScale = 1;
		float PicMin = (PicW < PicH) ? PicW : PicH;
		float PicMaX = (PicW > PicH) ? PicW : PicH;
		float PicPi = (float) PicW / PicH;

		float MaskMin = (MaskW < MaskH) ? MaskW : MaskH;
		float MaskMaX = (MaskW > MaskH) ? MaskW : MaskH;
		float MaskPi = (float) MaskW / MaskH;

		if (MaskW < MaskH) {
			if (PicW < PicH) {
				if (MaskPi < PicPi) {
					mScale = (float) MaskMaX / PicMaX;// OK
				} else if (MaskPi == PicPi) {
					mScale = (float) MaskMin / PicMin;// OK
				} else {
					mScale = (float) MaskMin / PicMin;
				}
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;// OK
			} else {
				mScale = (float) MaskMaX / PicMin;// OK
			}
		} else if (MaskW == MaskH) {
			if (PicW < PicH) {
				mScale = (float) MaskMaX / PicMin;// OK
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;// OK
			} else {
				mScale = (float) MaskMaX / PicMin;// OK
			}
		} else {
			if (PicW < PicH) {
				mScale = (float) MaskMaX / PicMin;
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;
			} else {
				mScale = (float) MaskMaX / PicMin;
			}
		}
		return mScale;
	}

	/**
	 * 图片显示比例
	 * @param mPic
	 * @param MaskW
	 * @param MaskH
	 */
	public static float getScale(Bitmap mPic, int MaskW, int MaskH) {
		float mScale = 1;
		int PicW = mPic.getWidth();
		int PicH = mPic.getHeight();
		int PicMin = (PicW < PicH) ? PicW : PicH;
		int PicMaX = (PicW > PicH) ? PicW : PicH;
		float PicPi = (float) PicW / PicH;

		int MaskMin = (MaskW < MaskH) ? MaskW : MaskH;
		int MaskMaX = (MaskW > MaskH) ? MaskW : MaskH;
		float MaskPi = (float) MaskW / MaskH;

		if (MaskW < MaskH) {
			if (PicW < PicH) {
				if (MaskPi < PicPi) {
					mScale = (float) MaskMaX / PicMaX;// OK
				} else if (MaskPi == PicPi) {
					mScale = (float) MaskMin / PicMin;// OK
				} else {
					mScale = (float) MaskMin / PicMin;
				}
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;// OK
			} else {
				mScale = (float) MaskMaX / PicMin;// OK
			}
		} else if (MaskW == MaskH) {
			if (PicW < PicH) {
				mScale = (float) MaskMaX / PicMin;// OK
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;// OK
			} else {
				mScale = (float) MaskMaX / PicMin;// OK
			}
		} else {
			if (PicW < PicH) {
				mScale = (float) MaskMaX / PicMin;
			} else if (PicW == PicH) {
				mScale = (float) MaskMaX / PicMaX;
			} else {
				mScale = (float) MaskMaX / PicMin;
			}
		}
		return mScale;
	}

	/**
	 * 
	 * 20170112前
	 * @param RBitmap
	 * @param MaxW
	 * @param MaxH
	 * @return
	 */
	public static Bitmap ResizePiImage(Bitmap RBitmap, float MaxW, float MaxH) {

		Bitmap BitmapOrg = RBitmap;
		int PicWidth = BitmapOrg.getWidth();
		int PicHeight = BitmapOrg.getHeight();
		float PicPi = ((float) PicWidth) / PicHeight;
		float MaxPi = ((float) MaxW) / MaxH;
		float scale = 0;
		if (PicWidth > MaxW && PicHeight <= MaxH) {
			scale = ((float) MaxW) / PicWidth;
		} else if (PicWidth <= MaxW && PicHeight > MaxH) {
			scale = ((float) MaxH) / PicHeight;
		} else if (PicWidth <= MaxW && PicHeight <= MaxH) {
			scale = 1;
		} else if (PicWidth > MaxW && PicHeight > MaxH) {
			if (PicPi > MaxPi) {
				scale = ((float) MaxW) / PicWidth;
			} else {
				scale = ((float) MaxH) / PicHeight;
			}
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap resizePiImage = Bitmap.createBitmap(BitmapOrg, 0, 0, PicWidth,
				PicHeight, matrix, false);
		return resizePiImage;

	}

	/**
	 * 20170112后的是OK
	 * 113
	 * 
	 * @param RBitmap
	 * @param MaxW
	 * @param MaxH
	 * @return
	 */
//	public static Bitmap ResizePiImage(Bitmap RBitmap, int MaxW, int MaxH) {
//
//		Bitmap BitmapOrg = RBitmap;
//		int PicW = BitmapOrg.getWidth();
//		int PicH = BitmapOrg.getHeight();
//		float mScale = 1;
//		int PicMin = (PicW < PicH) ? PicW : PicH;
//		int PicMaX = (PicW > PicH) ? PicW : PicH;
//		float PicPi = (float) PicW / PicH;
//		int MaskMin = (MaxW < MaxH) ? MaxW : MaxH;
//		int MaskMaX = (MaxW > MaxH) ? MaxW : MaxH;
//		float MaskPi = (float) MaxW / MaxH;
//
//		if (MaxW < MaxH) {
//			if (PicW < PicH) {
//				if (MaskPi < PicPi) {
//					mScale = (float) MaskMaX / PicMaX;// OK
//				} else if (MaskPi == PicPi) {
//					mScale = (float) MaskMin / PicMin;// OK
//				} else {
//					mScale = (float) MaskMin / PicMin;
//				}
//			} else if (PicW == PicH) {
//				mScale = (float) MaskMaX / PicMaX;// OK
//			} else {
//				mScale = (float) MaskMaX / PicMin;// OK
//			}
//		} else if (MaxW == MaxH) {
//			if (PicW < PicH) {
//				mScale = (float) MaskMaX / PicMin;// OK
//			} else if (PicW == PicH) {
//				mScale = (float) MaskMaX / PicMaX;// OK
//			} else {
//				mScale = (float) MaskMaX / PicMin;// OK
//			}
//		} else {
//			if (PicW < PicH) {
//				mScale = (float) MaskMaX / PicMin;
//			} else if (PicW == PicH) {
//				mScale = (float) MaskMaX / PicMaX;
//			} else {
//				mScale = (float) MaskMaX / PicMin;
//			}
//		}
//		Matrix matrix = new Matrix();
//		matrix.postScale(mScale, mScale);
//		Bitmap resizePiImage = Bitmap.createBitmap(BitmapOrg, 0, 0, PicW, PicH,
//				matrix, false);
//		return resizePiImage;
//
//	}

}
