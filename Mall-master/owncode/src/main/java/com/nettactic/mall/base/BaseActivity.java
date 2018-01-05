package com.nettactic.mall.base;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.LoginActivity;
import com.nettactic.mall.activity.OtherWebActivity;
import com.nettactic.mall.activity.OtherWebNoTitleActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.AppManager;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.dialog.CustomProgressDialog;
import com.nettactic.mall.dialog.FrameProgressDialog;
import com.nettactic.mall.dialog.UpdateDialog;
import com.nettactic.mall.net.MyURL;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.StatusBarUtil;
import com.nettactic.mall.utils.ToastUtils;

public class BaseActivity extends FragmentActivity {
	public String TAG = getClass().getName();

	public static int LOAD_SUCCESS = 1;
	public static int LOAD_ERROR = -1;
	public static int FIRST_LOAD = 1;
	public static int LOAD_MORE_DATA = 2;
	public static int REFRESH_DATA = 3;
	public UserController mUserController;
	public UserBean user;
	public CustomProgressDialog dialog;
	public FrameProgressDialog frameDialog;
	public UMImage mUMImage;
	public String mShareUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		setStatusBar();
		mUserController = UserController.getInstance(this);
		String mImageurl = MyURL.LOGO_URL;
		mUMImage = new UMImage(this, mImageurl);
		user = new UserBean();
		dialog = new CustomProgressDialog(this);
		frameDialog = new FrameProgressDialog(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AndroidUtils.exitActvityAnim(this);
	}

	/**
	 * 打开H5界面
	 * 
	 * @param context
	 */
	public void startOtherWeb(Context context, String title, String url) {
		Intent intent = new Intent(context, OtherWebActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		startActivity(intent);
	}

	/**
	 * 打开H5界面(无标题)
	 * 
	 * @param context
	 */
	public void startOtherWebNoTitle(Context context, String title, String url) {
		Intent intent = new Intent(context, OtherWebNoTitleActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		startActivity(intent);
	}

	public void startActivity(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		context.startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FragmentManager fm = getSupportFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0
					|| index >= fm.getFragments().size()) {
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}

	}

	/**
	 * 递归调用，对所有子Fragement生效
	 * 
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment frag, int requestCode, int resultCode,
			Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}

	public void showFrameDialog() {
		frameDialog.show();
	}

	public void dismissFrameDialog() {
		frameDialog.dismiss();
	}

	public void showmeidialog() {

		dialog.show();
	}

	public void dismissmeidialog() {
		dialog.dismiss();
	}

	/**
	 * 提示登录
	 */
	public void hintLogin(Context context) {
		showLoginDialog(context);
	}

	@SuppressWarnings("deprecation")
	public void setStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtil.setColor(this,
					getResources().getColor(R.color.main_color));
		}

	}

	/**
	 * 显示对话框
	 * 
	 * @param context
	 */
	public void showLoginDialog(final Context context) {

		UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
		builder.setMessage(context.getString(R.string.you_no_login));
		builder.setTitle(context.getString(R.string.prompt_message));
		builder.setPositiveButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});

		builder.setNegativeButton(context.getString(R.string.now_login),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 设置你的操作事项
						startActivity(context, LoginActivity.class);
						dialog.dismiss();

					}
				});

		builder.create().show();
	}

	/**
	 * 分享
	 */
	public void share(String promotecode) {
		mShareUrl = MyURL.SHARE_APP_URL + "?code=" + promotecode;
		new ShareAction(this)
				.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
						SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
				.setListenerList(umShareListener, umShareListener)
				.setShareboardclickCallback(shareBoardlistener).open();
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			String resStr = platform
					+ getResources().getString(R.string.share_success);
			ToastUtils.show(resStr);
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			String resStr = platform
					+ getResources().getString(R.string.share_error);
			ToastUtils.show(resStr);
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			String resStr = platform
					+ getResources().getString(R.string.share_cancel);
			ToastUtils.show(resStr);
		}
	};

	private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(BaseActivity.this)
					.setPlatform(share_media)
					.setCallback(umShareListener)
					.withTitle(getResources().getString(R.string.app_name))
					.withText(getResources().getString(R.string.share_app_info))
					.withTargetUrl(mShareUrl).withMedia(mUMImage).share();
		}
	};

}
