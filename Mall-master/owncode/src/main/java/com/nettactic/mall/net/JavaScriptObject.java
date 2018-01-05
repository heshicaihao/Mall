package com.nettactic.mall.net;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.nettactic.mall.R;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * H5 调用的类
 * 
 * @author heshicaihao
 * 
 */
public class JavaScriptObject {

	private Context mContxt;
	private Activity mActivity;
	public UMImage mUMImage;
	private String mShareUrl;

	public JavaScriptObject(Context mContxt, Activity mActivity,String mUser_id) {
		this.mContxt = mContxt;
		this.mActivity = mActivity;
		String promotecode = AndroidUtils.getPromoteCodeInfo(mUser_id);
		mShareUrl = MyURL.SHARE_APP_URL+"?code="+promotecode;
		mUMImage = new UMImage(mContxt, MyURL.LOGO_URL);
	}

	@JavascriptInterface
	public void gotoPromoteShare() {
		share();
	}

	/**
	 * 分享
	 */
	public void share() {
		new ShareAction(mActivity)
				.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
						SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
				.setListenerList(umShareListener, umShareListener)
				.setShareboardclickCallback(shareBoardlistener).open();
	}

	public UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			String resStr = platform
					+ mContxt.getResources().getString(R.string.share_success);
			ToastUtils.show(resStr);
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			String resStr = platform
					+ mContxt.getResources().getString(R.string.share_error);
			ToastUtils.show(resStr);
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			String resStr = platform
					+ mContxt.getResources().getString(R.string.share_cancel);
			ToastUtils.show(resStr);
		}
	};

	public ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(mActivity)
					.setPlatform(share_media)
					.setCallback(umShareListener)
					.withTitle(
							mContxt.getResources().getString(R.string.app_name))
					.withText(
							mContxt.getResources().getString(
									R.string.share_app_info))
					.withTargetUrl(mShareUrl).withMedia(mUMImage)
					.share();
		}
	};

}
