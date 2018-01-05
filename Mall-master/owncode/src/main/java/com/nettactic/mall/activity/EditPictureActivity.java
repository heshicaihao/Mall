package com.nettactic.mall.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.utils.AndroidBug54971Workaround;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.MyBitmapUtils;
import com.nettactic.mall.utils.StringUtils;

/**
 * 编辑图片页
 * 
 * @author heshicaihao
 */
@SuppressWarnings("deprecation")
@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
public class EditPictureActivity extends BaseActivity implements
		OnClickListener, OnTouchListener {
	private static final int REQUESTCODE = 10001;
	private static final int DOWNLOAD_OK = 1000;
	private int num = 0;
	private int mMode = 0;
	private float boreW = MyConstants.BOREWIDTH;
	private float boreH = MyConstants.BOREHEIGHT;
	private float mInitScale = 1;
	private float mScale = mInitScale;
	private float mTranslateX;
	private float mTranslateY;
	private float mDesignW = 0f;
	private float mDesignH = 0f;
	private float mInitX = 0;
	private float mInitY = 0;
	private float mLastMoveX = -1;
	private float mLastMoveY = -1;
	private float mStartDis;
	private float mFrameWidth;
	private float mFrameHeight;
	private float mFrameX;
	private float mFrameY;
	private boolean mIsMove = false;
	private String mPicPath;
	private String mPicOldPath;
	private String mWorkId;
	private int mPage = 0;
	private String mData;

	private TextView mTitle;
	private TextView mBack;
	private TextView mSave;
	private RelativeLayout mEditMiddleRl;
	private LinearLayout mMinMenu;
	private ImageView mShadowImage;
	private ImageView mFrameImage;
	private ImageView mBackgroundImage;
	private ImageView mResultImage;
	private ImageView mDecoration01;
	private ImageView mDecoration02;
	private ImageView mDecoration03;
	private ImageView mDecoration04;
	private ImageView mEnlarge;
	private ImageView mNarrow;

	private ImageView mChoicePicture;
	private Bitmap mPicBitmap;
	private Bitmap mMaskBitmap;
	private Bitmap mResultBitmap;

	private ACache mCache;
	private JSONArray mDataJSONArray;
	private Handler mDownloadHandler;
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;
	private List<ImageView> mDecorationlist = new ArrayList<ImageView>();
	private String frameMaskUrl;
	private String frameUrl;
	private String backgroundUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setAttributes(params);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		setContentView(R.layout.activity_edit_pic);
		AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));

		initView();
		initData();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.save:
			String imgid = SelectPicSQLHelper.queryByWorkpageAndWorkid(
					num + 1 + "", mWorkId);
			
			String mWorksDir = FileUtil.getWorksDir(mWorkId);
			FileUtil.saveBitmap2Png(mResultBitmap, mWorksDir, mWorkId+imgid,
					MyConstants.PNG);
			saveMethod();
			break;

		case R.id.change_picture:
			initPicLocation(mPicBitmap, mMaskBitmap);
			gotoUpdatePicture();
			break;

		case R.id.enlarge:
			mScale += mInitScale * MyConstants.EVERY_TIME_SCALE;
			limitMaxScale();
			getUserSee();
			break;

		case R.id.narrow:
			mScale -= mInitScale * MyConstants.EVERY_TIME_SCALE;
			limitMinScale();
			getUserSee();
			break;

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mMode = 1;
			mIsMove = false;
			break;
		case MotionEvent.ACTION_UP:
			mMode = 0;
			mLastMoveX = -1;
			mLastMoveY = -1;
			LogUtils.logd(TAG, "changeMenu");
			changeMenu();
			mIsMove = true;
			break;
		case MotionEvent.ACTION_MOVE:
			onTouchMove(event);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mMode -= 1;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mMode += 1;
			mStartDis = AndroidUtils.distance(event);
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE:
			if (resultCode == RESULT_OK) {
				mPicOldPath = data.getStringExtra("Path");
				Bitmap sdCardImg = FileUtil.getSDCardImg(mPicOldPath, 3200,
						3200);
				if (sdCardImg != null) {
					mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
							MyConstants.INIT_MAXWIDTH,
							MyConstants.INIT_MAXHEIGHT);
				}
				initPicLocation(mPicBitmap, mMaskBitmap);
				getUserSee();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 触屏改变菜单显示
	 */
	private void changeMenu() {
		if (!mIsMove) {
			int visibility = mMinMenu.getVisibility();
			if (View.GONE == visibility) {
				mMinMenu.setVisibility(View.VISIBLE);
			} else if (View.VISIBLE == visibility) {
				mMinMenu.setVisibility(View.GONE);
			} else if (View.INVISIBLE == visibility) {
				mMinMenu.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * 触屏操作方法
	 * 
	 * @param event
	 */
	private void onTouchMove(MotionEvent event) {
		if (mMode == 1) {
			float xMove = event.getX();
			float yMove = event.getY();
			if (mLastMoveX == -1 && mLastMoveY == -1) {
				mLastMoveX = xMove;
				mLastMoveY = yMove;
			}
			float movedDistanceX = xMove - mLastMoveX;
			float movedDistanceY = yMove - mLastMoveY;
			if (Math.abs(movedDistanceX) > 3 || Math.abs(movedDistanceX) > 3) {
				mIsMove = true;
			}
			mTranslateX = mTranslateX + movedDistanceX;
			mTranslateY = mTranslateY + movedDistanceY;
			getUserSee();
			mLastMoveX = xMove;
			mLastMoveY = yMove;
		} else if (mMode >= 2) {
			float endDis = AndroidUtils.distance(event);
			mScale = mScale * (endDis / mStartDis);
			limitMaxScale();
			limitMinScale();
			mIsMove = true;
			getUserSee();
			mStartDis = endDis;
		}
	}

	/**
	 * 限制最小缩小倍数
	 */
	private void limitMinScale() {
		if (mScale < mInitScale * MyConstants.MIN_SCALE) {
			mScale = mInitScale * MyConstants.MIN_SCALE;
		}
	}

	/**
	 * 限制最大放大倍数
	 */
	private void limitMaxScale() {
		if (mScale > mInitScale * MyConstants.MAX_SCALE) {
			mScale = mInitScale * MyConstants.MAX_SCALE;
		}
	}

	private void gotoUpdatePicture() {
		List<SelectpicBean> list = SelectPicSQLHelper.query2DB(mPicOldPath,
				mWorkId);
		String imgid;
		if (list.size() == 0) {
			imgid = "";
		} else {
			SelectpicBean bean = list.get(0);
			imgid = bean.getImgid();
		}
		LogUtils.logd(TAG, "imgid:" + imgid);
		mCache.put(MyConstants.SELECT_IMGID, imgid);

		Intent intent = new Intent(this, UpdatePictureActivity.class);
		EditPictureActivity.this.startActivityForResult(intent, REQUESTCODE);
		AndroidUtils.enterActvityAnim(this);
	}

	private void initView() {

		mBack = (TextView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mSave = (TextView) findViewById(R.id.save);

		mShadowImage = (ImageView) findViewById(R.id.shadow_image);
		mDecoration01 = (ImageView) findViewById(R.id.decoration_01);
		mDecoration02 = (ImageView) findViewById(R.id.decoration_02);
		mDecoration03 = (ImageView) findViewById(R.id.decoration_03);
		mDecoration04 = (ImageView) findViewById(R.id.decoration_04);
		mFrameImage = (ImageView) findViewById(R.id.frame_image);
		mResultImage = (ImageView) findViewById(R.id.result_image);
		mBackgroundImage = (ImageView) findViewById(R.id.background_image);

		mEnlarge = (ImageView) findViewById(R.id.enlarge);
		mNarrow = (ImageView) findViewById(R.id.narrow);
		mChoicePicture = (ImageView) findViewById(R.id.change_picture);
		mEditMiddleRl = (RelativeLayout) findViewById(R.id.middle_rl_touch);

		mMinMenu = (LinearLayout) findViewById(R.id.min_menu);

		mTitle.setText(this.getString(R.string.edit_picture));
		
		Drawable drawableBack= getResources().getDrawable(R.mipmap.browse_back);
		drawableBack.setBounds(0, 0, drawableBack.getMinimumWidth(), drawableBack.getMinimumHeight());//这句一定要加  
		mBack.setCompoundDrawables(drawableBack,null,null,null);
		
		Drawable drawableSave= getResources().getDrawable(R.mipmap.save);
		drawableSave.setBounds(0, 0, drawableSave.getMinimumWidth(), drawableSave.getMinimumHeight());//这句一定要加  
		mSave.setCompoundDrawables(drawableSave,null,null,null);
		
		mSave.setVisibility(View.VISIBLE);
		mSave.setText(this.getString(R.string.save));

		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);

		mChoicePicture.setOnClickListener(this);
		mEnlarge.setOnClickListener(this);
		mNarrow.setOnClickListener(this);

		mEditMiddleRl.setOnTouchListener(this);

	}

	private void initData() {
		getDataCache();
		getDataIntent();
		initXutils();
		try {
			mDataJSONArray = new JSONArray(mData);
			LogUtils.logd(TAG, "mData:" + mData);
			JSONArray frameJSONArray = JsonDao.getElementTypeArray(
					mDataJSONArray, "frame");
			JSONObject object = (JSONObject) frameJSONArray.get(0);
			frameMaskUrl = object.optString("frameMaskUrl");

			frameUrl = object.optString("frameUrl");
			JSONArray backgroundJSONArray = JsonDao.getElementTypeArray(
					mDataJSONArray, "background");
			JSONObject backgroundobject = (JSONObject) backgroundJSONArray
					.get(0);
			backgroundUrl = backgroundobject.optString("url");
			if (mPage == 2) {
				mShadowImage.setBackgroundResource(R.mipmap.cs_szt);
				boreW = MyConstants.SZTBOREWIDTH;
				boreH = MyConstants.SZTBOREHEIGHT;
			} else {
				mShadowImage.setBackgroundResource(R.mipmap.cs);
				boreW = MyConstants.BOREWIDTH;
				boreH = MyConstants.BOREHEIGHT;
			}
			initDesign();
			initFrame();
			initDecoration();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		getResultResources(mPicPath);
		refreshChangFrameUI();
	}

	private void initDecoration() throws JSONException {
		mDecorationlist.add(mDecoration01);
		mDecorationlist.add(mDecoration02);
		mDecorationlist.add(mDecoration03);
		mDecorationlist.add(mDecoration04);
		JSONArray decorationJSONArray = JsonDao.getElementTypeArray(
				mDataJSONArray, "decoration");
		for (int i = 0; i < decorationJSONArray.length(); i++) {
			JSONObject decorationBean = (JSONObject) decorationJSONArray.get(i);
			layoutDecoration(mDecorationlist.get(i), decorationBean);

		}
	}

	private void initFrame() throws JSONException {
		JSONArray frameJSONArray = JsonDao.getElementTypeArray(mDataJSONArray,
				"frame");
		JSONObject frameBean = (JSONObject) frameJSONArray.get(0);
		mFrameWidth = Float.parseFloat(frameBean.optString("width"));
		mFrameHeight = Float.parseFloat(frameBean.optString("height"));
		mFrameX = Float.parseFloat(frameBean.optString("x"));
		mFrameY = Float.parseFloat(frameBean.optString("y"));

	}

	private void initDesign() throws JSONException {
		JSONArray PageJSONArray = JsonDao.getElementTypeArray(mDataJSONArray,
				"page");
		JSONObject pageBean = (JSONObject) PageJSONArray.get(0);
		float pageWidth = Float.parseFloat(pageBean.optString("pageWidth"));
		float pageHeight = Float.parseFloat(pageBean.optString("pageHeight"));
		mDesignW = 2 * boreW + pageWidth;
		mDesignH = 2 * boreH + pageHeight;

		mInitX = getMarginleft(mDesignW);
		mInitY = 0;
		setView(mShadowImage,  mDesignW,  mDesignH,  0,
				 mInitY);
		if (!StringUtils.isEmpty(backgroundUrl)) {
			setView(mBackgroundImage,  pageWidth, (int) pageHeight,
					 0 + boreW,  mInitY + boreH);
			String backgroundpath = FileUtil.getUrl2Path(backgroundUrl);
			bitmapUtils.display(mBackgroundImage, backgroundpath,
					bigPicDisplayConfig);
		}
	}

	private void initXutils() {
		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(MyApplication.getContext());
		}

		bigPicDisplayConfig = new BitmapDisplayConfig();
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		// bigPicDisplayConfig.setBitmapMaxSize(new BitmapSize(mScreenWidth / 5,
		// mScreenWidth / 5));
		bitmapUtils.configDefaultLoadingImage(R.mipmap.blank_bg);
		bitmapUtils.configDefaultLoadFailedImage(R.mipmap.blank_bg);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	private void layoutDecoration(ImageView view, JSONObject decorationBean) {
		float viewWidth = Float.parseFloat(decorationBean.optString("width"));
		float viewHeight = Float.parseFloat(decorationBean.optString("height"));
		float x = Float.parseFloat(decorationBean.optString("x"));
		float y = Float.parseFloat(decorationBean.optString("y"));
		String url = decorationBean.optString("url");
		setView(view, viewWidth, viewHeight, x + boreW, y + boreH);
		String path = FileUtil.getUrl2Path(url);
		bitmapUtils.display(view, path, bigPicDisplayConfig);
	}

	private void getDataIntent() {

		Intent intent = getIntent();
		num = intent.getIntExtra("num", -1);
		SelectpicBean bean = SelectPicSQLHelper.queryByIsselect("true",
				mWorkId, num + 1 + "");
		mData = bean.getCardinfo();
		mPicPath = bean.getNewpath();
		mPicOldPath = bean.getOldpath();

	}

	private void getDataCache() {
		mCache = ACache.get(this);
		mWorkId = mCache.getAsString(MyConstants.WORKID);
		String mTotalpage = mCache.getAsString(MyConstants.TOTALPAGE);
		mPage = Integer.parseInt(mTotalpage);
	}

	/**
	 * 获取mask 和 用户图片
	 */
	private void getResultResources(final String path) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (path != null) {

					Bitmap sdCardImg = FileUtil.getSDCardImg(path, 3200, 3200);
					if (sdCardImg != null) {
						mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
								MyConstants.INIT_MAXWIDTH,
								MyConstants.INIT_MAXHEIGHT);
					}
				}

				String frameMaskpath = FileUtil.getUrl2Path(frameMaskUrl);
				mMaskBitmap = FileUtil.getSDCardImg(frameMaskpath);
				if (mMaskBitmap != null && mPicBitmap != null) {
					mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
				}
			}
		}).start();

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
		mMatrix.postScale(scale, scale, mFrameWidth / 2, mFrameHeight / 2);
		pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(),
				mMatrix, true);
		picX = limitX(mask, picX, pic);
		picY = limitY(mask, picY, pic);
		mTranslateX = picX;
		mTranslateY = picY;
		mCanvas.drawBitmap(pic, picX, picY, null);
		mCanvas.drawBitmap(mask, 0, 0, mPaint);
		mPaint.setXfermode(null);

		return result;
	}

	/**
	 * 初始化用户图片居中，铺满mask。
	 * 
	 * @param mPicBitmap
	 * @param mMaskBitmap
	 */
	private void initPicLocation(Bitmap mPicBitmap, Bitmap mMaskBitmap) {
		float PicW = mPicBitmap.getWidth();
		float PicH = mPicBitmap.getHeight();
		float MaskW = mMaskBitmap.getWidth();
		float MaskH = mMaskBitmap.getHeight();
		mScale = MyBitmapUtils.getInitScale(mPicBitmap, mMaskBitmap);
		mInitScale = mScale;
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
	}

	/**
	 * 限制横向图片不出界
	 * 
	 * @param mask
	 * @param picX
	 * @param resizeBitmap
	 * @return
	 */
	private float limitX(Bitmap mask, float picX, Bitmap resizeBitmap) {
		float maxX = resizeBitmap.getWidth() - mask.getWidth();
		if (picX > 0) {
			picX = 0;
		} else if (picX < -maxX) {
			picX = -maxX;
		}
		return picX;
	}

	/**
	 * 限制纵向图片不出界
	 * 
	 * @param mask
	 * @param picY
	 * @param resizeBitmap
	 * @return
	 */
	private float limitY(Bitmap mask, float picY, Bitmap resizeBitmap) {
		float maxY = resizeBitmap.getHeight() - mask.getHeight();
		if (picY > 0) {
			picY = 0;
		} else if (picY < -maxY) {
			picY = -maxY;
		}
		return picY;
	}

	/**
	 * 切换画框 更新UI
	 */
	private void refreshChangFrameUI() {

		mDownloadHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case DOWNLOAD_OK:
					if (mPicBitmap != null && mMaskBitmap != null) {
						if (!StringUtils.isEmpty(frameUrl)) {
							setView(mFrameImage, mFrameWidth, mFrameHeight,
									mFrameX + boreW, mFrameY + boreH);
							String framepath = FileUtil.getUrl2Path(frameUrl);
							bitmapUtils.display(mFrameImage, framepath,
									bigPicDisplayConfig);
						}

						setView(mResultImage, mFrameWidth, mFrameHeight,
								mFrameX + boreW, mFrameY + boreH);
						try {
							JSONArray frameJSONArray = JsonDao
									.getElementTypeArray(mDataJSONArray,
											"frame");
							JSONObject object = (JSONObject) frameJSONArray
									.get(0);
							String remark = object.optString("remark");
							String cutx = object.optString("cutx");
							String cuty = object.optString("cuty");
							if (!StringUtils.isEmpty(remark)) {
								JSONObject remarJSONObject = new JSONObject(
										remark);
								mScale = Float.parseFloat(remarJSONObject
										.optString("mScale"));
								mInitScale = Float.parseFloat(remarJSONObject
										.optString("mInitScale"));
								float mPiMask = Float
										.parseFloat(remarJSONObject
												.optString("mPiMask"));
								mTranslateX = Float.parseFloat(cutx) / mPiMask;
								mTranslateY = Float.parseFloat(cuty) / mPiMask;
							} else {
								initPicLocation(mPicBitmap, mMaskBitmap);
							}
							getUserSee();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		};
	}

	private void getUserSee() {
		mResultBitmap = setMask(mPicBitmap, mMaskBitmap, mTranslateX,
				mTranslateY, mScale);
		mResultImage.setImageBitmap(mResultBitmap);
	}

	/**
	 * 设置控件 的位置
	 * 
	 * @param view
	 * @param viewHeight
	 * @param viewWidth
	 * @param y
	 * @param x
	 */
	private void setView(View view, float viewWidth, float viewHeight, float x, float y) {
		float pi = getPro();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				view.getLayoutParams());
		params.width = (int) Math.ceil(viewWidth * pi);
		params.height = (int) Math.ceil(viewHeight * pi);
		params.leftMargin = (int) (mInitX + x * pi);
		params.topMargin = (int) (mInitY + y * pi);
		view.setLayoutParams(params);
	}

	/**
	 * 获取图片缩放比例
	 * 
	 * @return
	 */
	private float getPro() {
		int mScreenHeight = AndroidUtils.getTotalScreenHeight(this);
		int title = AndroidUtils.dip2px(MyApplication.getContext(), 50);
//		int bottomStatusHeight = AndroidUtils
//				.getBottomStatusHeight(MyApplication.getContext());
		int bottomStatusHeight = 0;
		int bottom = AndroidUtils.dip2px(MyApplication.getContext(),
				30 + 77 + 4 * 2)+ bottomStatusHeight;;
		int userHeight = mScreenHeight - title - bottom;
		float viewHeight = mDesignH;
		float pi = userHeight / viewHeight;
		return pi;
	}

	/**
	 * 起始左边间距
	 * 
	 */
	private float getMarginleft(float viewWidth) {
		int mScreenWidth = AndroidUtils.getScreenWidth(this);
		float Padd = (mScreenWidth - viewWidth * getPro()) / 2;
		return Padd;
	}

	/**
	 * 拼Json 给编辑图片页
	 * 
	 * @param code
	 * @param goods_id
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getData(String code, String goods_id, String number) {
		JSONObject object = new JSONObject();
		try {
			object.put("code", code);
			JSONObject contentJson = new JSONObject();
			contentJson.put("goods_id", goods_id);
			contentJson.put("number", number);
			object.put("content", contentJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 保存键 处理事件
	 */
	private void saveMethod() {
		String imgid = SelectPicSQLHelper.queryByWorkpageAndWorkid(
				num + 1 + "", mWorkId);
		String newData = getNewData(imgid);
		SelectPicSQLHelper.updateSelectPic(imgid, num + 1 + "", newData);

		goBackBrowse();
	}

	/**
	 * 获取新的数据
	 * 
	 * @param imgid
	 * @return
	 */
	private String getNewData(String imgid) {
		int mMaskBitmapheight = mMaskBitmap.getHeight();
		int picwidth = mPicBitmap.getWidth();
		int picheight = mPicBitmap.getHeight();
		String singlePokerJSONArrayStr = JsonDao.getNewCardinfo(mDataJSONArray,
				mMaskBitmapheight, mFrameHeight, picwidth, picheight,
				mTranslateX, mTranslateY, mScale, mInitScale, imgid);
		return singlePokerJSONArrayStr;
	}

	/**
	 * 返回 BrowseActivity
	 */
	private void goBackBrowse() {
		Intent intent = new Intent();
		intent.putExtra("Path", "");
		EditPictureActivity.this.setResult(BrowseActivity.RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPicBitmap != null && (!mPicBitmap.isRecycled())) {
			mPicBitmap.recycle();
			mPicBitmap = null;
			System.gc();
		}
		if (mMaskBitmap != null && (!mMaskBitmap.isRecycled())) {
			mMaskBitmap.recycle();
			mMaskBitmap = null;
			System.gc();
		}
		if (mResultBitmap != null && (!mResultBitmap.isRecycled())) {
			mResultBitmap.recycle();
			mMaskBitmap = null;
			System.gc();
		}
		System.gc();
	}

}
