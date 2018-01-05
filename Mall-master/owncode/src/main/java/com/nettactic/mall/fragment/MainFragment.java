package com.nettactic.mall.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapCommonUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.ad.AutoScrollViewPager;
import com.nettactic.mall.ad.CirclePageIndicator;
import com.nettactic.mall.ad.ImagePagerAdapter;
import com.nettactic.mall.adapter.HomeListAdapter;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.bean.ADBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 首页
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class MainFragment extends BaseFragment implements OnClickListener {

	private String mPromoterUrl;
	private String mTime;

	private View mHeader;
	private ListView mListView;
	private AutoScrollViewPager mViewPager;
	private CirclePageIndicator mIndicator;
	private ImageView mPromoterIV;
	private float screenensity;

	private JSONArray mAdItems;
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		initView(rootView);
		initData();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		getSDinfo();
		mViewPager.startAutoScroll();
	}

	@Override
	public void onPause() {
		super.onPause();
		mViewPager.stopAutoScroll();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.promoter_iv:
			if (!StringUtils.isEmpty(mPromoterUrl)) {
				startOtherWeb(MyApplication.getContext(), "", mPromoterUrl);
			}
			// startOtherWeb(MyApplication.getContext(), "",
			// MyURL.PROMOTE_PROPAGATE_URL);
			break;

		default:
			break;
		}

	}

	private void initData() {
		initXtils();
		screenensity = AndroidUtils.getScreenensity(getActivity());
		LogUtils.logd(TAG, "screenensity:"+screenensity);
		if (!AndroidUtils.isNetworkAvailable(getActivity())) {
			ToastUtils.show(R.string.no_net);
			getSDinfo();
		} else {
			mTime = getTime();
			initAdInfo();
			initCatList();
		}
	}



	/**
	 * 获取时间梭
	 * 
	 * @return
	 */
	private String getTime() {
		String time = null;
		String timefilePath = FileUtil.getFilePath(MyConstants.TIME,
				MyConstants.UPDATE_TIME, MyConstants.TXT);
		boolean fileIsExists = FileUtil.fileIsExists(timefilePath);
		if (fileIsExists) {
			time = FileUtil.readFile(MyConstants.TIME, MyConstants.UPDATE_TIME,
					MyConstants.TXT);
		} else {
			time = "0";
		}
		return time;
	}

	private void initAdInfo() {
		int screenWidth = AndroidUtils.getScreenWidth(getActivity());
		int screenHeight = AndroidUtils.getScreenHeight(getActivity());
		String app_type = MyConstants.ANDROID;

		NetHelper.getAdInfo(screenWidth + "", screenHeight + "", mTime,
				app_type, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getAdInfo+onSuccess");
						resolveGetAdInfo(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getAdInfo+onFailure");
					}
				});

	}

	private void resolveGetAdInfo(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			if ("0".equals(code)) {
				JSONObject result = JSONUtil.resolveResult(responseBody);
				FileUtil.saveFile(result.toString(), MyConstants.HOMEAD,
						MyConstants.HOME_AD_INFO, MyConstants.TXT);
				setADinfo(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initCatList() {
		NetHelper.getCatList(mTime, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "getCatList+onSuccess");
				resolveGetCatList(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getCatList+onFailure");
			}
		});

	}

	private void resolveGetCatList(byte[] responseBody) {

		try {
			String json = new String(responseBody, "UTF-8");
			// LogUtils.logd(TAG, "mItemsjson:" + json);
			JSONObject JSONObject = new JSONObject(json);
			JSONArray mItems = JSONObject.getJSONArray("result");
			// JSONObject result = JSONObject.getJSONObject("result");
			// JSONArray mItems = result.getJSONArray("items");
			ListSetAdapter(mItems);
			FileUtil.saveFile(mItems.toString(), MyConstants.CATLIST,
					MyConstants.CAT_LIST_INFO, MyConstants.TXT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView(View view) {
		mListView = (ListView) view.findViewById(R.id.goods_listview);
		mHeader = getActivity().getLayoutInflater().inflate(
				R.layout.view_header_home, null);
		getActivity().getLayoutInflater();
		mViewPager = (AutoScrollViewPager) mHeader
				.findViewById(R.id.view_pager);
		LayoutParams lp = mViewPager.getLayoutParams();
		lp.width = AndroidUtils.getScreenWidth(getActivity());
		lp.height = lp.width*400/750;
		mViewPager.setLayoutParams(lp);
		
		mIndicator = (CirclePageIndicator) mHeader.findViewById(R.id.indicator);
		mPromoterIV = (ImageView) mHeader.findViewById(R.id.promoter_iv);
		mListView.addHeaderView(mHeader);

		mPromoterIV.setOnClickListener(this);

	}

	/**
	 * 从SD读取 缓存Json数据
	 */
	private void getSDinfo() {
		String home_adPath = FileUtil.getFilePath(MyConstants.HOMEAD,
				MyConstants.HOME_AD_INFO, MyConstants.TXT);
		// LogUtils.logd(TAG, "home_adPath:" + home_adPath);
		if (FileUtil.fileIsExists(home_adPath)) {
			String ad_info = FileUtil.readFile(MyConstants.HOMEAD,
					MyConstants.HOME_AD_INFO, MyConstants.TXT);
			try {
				JSONObject result = new JSONObject(ad_info);
				setADinfo(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		String goods_listpath = FileUtil.getFilePath(MyConstants.CATLIST,
				MyConstants.CAT_LIST_INFO, MyConstants.TXT);
		if (FileUtil.fileIsExists(goods_listpath)) {
			String goods_list_info = FileUtil.readFile(MyConstants.CATLIST,
					MyConstants.CAT_LIST_INFO, MyConstants.TXT);
			try {
				JSONArray mItems = new JSONArray(goods_list_info);
				ListSetAdapter(mItems);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ListView 添加数据
	 * 
	 * @param items
	 * @throws JSONException
	 */
	private void AdListSetAdapter(JSONArray items) throws JSONException {
		setAdapter(ADBean.getDataToJson(items));
	}

	private void setAdapter(List<ADBean> imageIdList) {
		ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(
				getActivity(), MyApplication.getContext(), imageIdList);
		mViewPager.setAdapter(imagePagerAdapter);
		mIndicator.setViewPager(mViewPager);
		mIndicator.setRadius(4*screenensity);
		mIndicator.setOrientation(LinearLayout.HORIZONTAL);
		mIndicator.setStrokeWidth(2);
		mIndicator.setSnap(true);
		mViewPager.setInterval(5000);
		mViewPager
				.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
		// viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
		// viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		mViewPager.setCycle(true);
		mViewPager.setBorderAnimation(true);
	}

	/**
	 * ListView 添加数据
	 * 
	 * @param items
	 * @throws JSONException
	 */
	private void ListSetAdapter(JSONArray items) throws JSONException {

		HomeListAdapter mAdapter = new HomeListAdapter(getActivity(),
				MyApplication.getContext(), items);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 初始化
	 */
	private void initXtils() {
		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(MyApplication.getContext());
		}
		bigPicDisplayConfig = new BitmapDisplayConfig();
		// 图片太大时容易OOM。
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(getActivity()));
		bitmapUtils.configDefaultLoadingImage(R.mipmap.wait_im);
		bitmapUtils.configDefaultLoadFailedImage(R.mipmap.wait_im);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		// AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
		// 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		bitmapUtils.configDefaultImageLoadAnimation(animation);
	}

	/**
	 * 给广告加载数据
	 * 
	 * @param result
	 * @throws JSONException
	 */
	private void setADinfo(JSONObject result) throws JSONException {
		mAdItems = result.getJSONArray("carousel");
		AdListSetAdapter(mAdItems);

		JSONObject promoter = result.getJSONObject("promoter");
		String promoter_image_url = promoter.optString("image_url");
		LogUtils.logd(TAG, "promoter_image_url:" + promoter_image_url);
		mPromoterUrl = promoter.optString("url");
		if (!StringUtils.isEmpty(promoter_image_url)) {
			mPromoterIV.setVisibility(View.VISIBLE);
			bitmapUtils.display(mPromoterIV, promoter_image_url);
		} else {
			mPromoterIV.setVisibility(View.GONE);
		}
	}

}
