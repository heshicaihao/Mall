package com.nettactic.mall.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.EditPictureActivity;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.StringUtils;

@SuppressLint({ "ValidFragment", "HandlerLeak" })
public class BrowseFragment extends BaseFragment implements OnClickListener {
	private int num = 0;

	private float mFrameWidth;
	private float mFrameHeight;
	private float mFrameX;
	private float mFrameY;
	private float boreW = MyConstants.BOREWIDTH;
	private float boreH = MyConstants.BOREHEIGHT;
	private float mDesignW = 0f;
	private float mDesignH = 0f;
	private float mInitX = 0;
	private float mInitY = 0;
	private String singlePokerJSONArrayStr;
	private String mWorkId;
	private int mPage = 0;

	private String frameUrl;
	private String backgroundUrl;

	private List<ImageView> mDecorationlist = new ArrayList<ImageView>();
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;
	private ACache mCache;
	private JSONArray mDataJSONArray;
	private String imgid;

	private ImageView mShadowImage;
	private ImageView mFrameImage;
	private ImageView mResultImage;
	private ImageView mBackgroundImage;
	private ImageView mDecoration01;
	private ImageView mDecoration02;
	private ImageView mDecoration03;
	private ImageView mDecoration04;
	private Bitmap sdCardImg;

	public BrowseFragment() {

	}

	public BrowseFragment(int num) {
		this.num = num;
		getDataCache();
		SelectpicBean bean = SelectPicSQLHelper.queryByIsselect("true",
				mWorkId, num + 1 + "");
		this.singlePokerJSONArrayStr = bean.getCardinfo();
		imgid = bean.getImgid();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_browse, container,
				false);

		initView(rootView);
		initData();
		return rootView;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.result_image:
			gotoEditPicture();

			break;

		default:
			break;
		}

	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Activity.RESULT_FIRST_USER:
			if (resultCode == getActivity().RESULT_OK) {
				SelectpicBean queryByIsselect = SelectPicSQLHelper
						.queryByIsselect("true", mWorkId, num + 1 + "");
				String imgid2 = queryByIsselect.getImgid();
				String mWorksDir = FileUtil.getWorksDir(mWorkId);
				String resultpath = FileUtil.getFilePath(mWorksDir, mWorkId
						+ imgid2, MyConstants.PNG);
				setView(mResultImage, mFrameWidth, mFrameHeight, mFrameX
						+ boreW, mFrameY + boreH);
				sdCardImg = FileUtil.getSDCardImg(resultpath);
				mResultImage.setImageBitmap(sdCardImg);
				// bitmapUtils.display(mResultImage, resultpath,
				// bigPicDisplayConfig);
			}
			break;
		default:
			break;
		}
	}

	private void gotoEditPicture() {
		Intent intent = new Intent(getActivity(), EditPictureActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("num", num);
		intent.putExtras(bundle);
		startActivityForResult(intent, Activity.RESULT_FIRST_USER);
	}

	private void initView(View view) {
		mShadowImage = (ImageView) view.findViewById(R.id.shadow_image);
		mBackgroundImage = (ImageView) view.findViewById(R.id.background_image);
		mFrameImage = (ImageView) view.findViewById(R.id.frame_image);
		mResultImage = (ImageView) view.findViewById(R.id.result_image);
		mDecoration01 = (ImageView) view.findViewById(R.id.decoration_01);
		mDecoration02 = (ImageView) view.findViewById(R.id.decoration_02);
		mDecoration03 = (ImageView) view.findViewById(R.id.decoration_03);
		mDecoration04 = (ImageView) view.findViewById(R.id.decoration_04);

		mResultImage.setOnClickListener(this);

	}

	private void initData() {
		getDataCache();
		initXutils();
		try {
			mDataJSONArray = new JSONArray(singlePokerJSONArrayStr);
			JSONArray frameJSONArray = JsonDao.getElementTypeArray(
					mDataJSONArray, "frame");
			JSONObject object = (JSONObject) frameJSONArray.get(0);
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
		setView(mFrameImage, mFrameWidth, mFrameHeight, mFrameX + boreW,
				mFrameY + boreH);
		String framepath = FileUtil.getUrl2Path(frameUrl);
		bitmapUtils.display(mFrameImage, framepath, bigPicDisplayConfig);
		setView(mResultImage, mFrameWidth, mFrameHeight, mFrameX + boreW,
				mFrameY + boreH);
		String mWorksDir = FileUtil.getWorksDir(mWorkId);
		String resultpath = FileUtil.getFilePath(mWorksDir, mWorkId + imgid,
				MyConstants.PNG);
		if (new File(resultpath).exists()) {
			sdCardImg = FileUtil.getSDCardImg(resultpath);
			mResultImage.setImageBitmap(sdCardImg);
		}
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
		setView(mShadowImage, mDesignW, mDesignH, 0, mInitY);
		if (!StringUtils.isEmpty(backgroundUrl)) {
			setView(mBackgroundImage, pageWidth, pageHeight, 0 + boreW, mInitY
					+ boreH);
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

	/**
	 * 设置控件 的位置
	 * 
	 * @param view
	 * @param viewHeight
	 * @param viewWidth
	 * @param y
	 * @param x
	 */
	private void setView(View view, float viewWidth, float viewHeight, float x,
			float y) {
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
		int mScreenHeight = AndroidUtils.getTotalScreenHeight(getActivity());
		int title = AndroidUtils.dip2px(MyApplication.getContext(), 50);
		// int bottomStatusHeight = AndroidUtils
		// .getBottomStatusHeight(MyApplication.getContext());
		int bottomStatusHeight = 0;
		int bottom = AndroidUtils.dip2px(MyApplication.getContext(),
				30 + 77 + 4 * 2) + bottomStatusHeight;
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
		int mScreenWidth = AndroidUtils.getScreenWidth(getActivity());
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

	private void getDataCache() {
		mCache = ACache.get(MyApplication.getContext());
		mWorkId = mCache.getAsString(MyConstants.WORKID);
		String mTotalpage = mCache.getAsString(MyConstants.TOTALPAGE);
		mPage = Integer.parseInt(mTotalpage);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (sdCardImg != null && (!sdCardImg.isRecycled())) {
			sdCardImg.recycle();
			sdCardImg = null;
			System.gc();
		}
		System.gc();
	}

}
