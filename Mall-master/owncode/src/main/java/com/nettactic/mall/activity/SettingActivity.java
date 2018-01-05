package com.nettactic.mall.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.MyURL;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.update.UpdateManager;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

/**
 * 
 * 设置界面
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private LinearLayout mHelpLL;
	private LinearLayout mAppUpdateLL;
	private LinearLayout mShareLL;
	private LinearLayout mSuggestionsLL;
	private LinearLayout mContactUsLL;
	private Button mLogoutLL;
	private UMShareAPI mShareAPI = null;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();
		initData();
	}

	@Override
	public void onResume() {
		super.onResume();
		mUser = UserController.getInstance(this).getUserInfo();
		if (mUser.isIs_login()) {
			String promote_code_info_path = FileUtil.getFilePath(
					MyConstants.PCODE, MyConstants.PCODE_INFO + mUser.getId(),
					MyConstants.JSON);
			if (!FileUtil.fileIsExists(promote_code_info_path)) {
				initPopularizeinfo();
			}
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mLogoutLL = (Button) findViewById(R.id.logout_btn);
		mHelpLL = (LinearLayout) findViewById(R.id.help_ll);
		mAppUpdateLL = (LinearLayout) findViewById(R.id.app_update_ll);
		mShareLL = (LinearLayout) findViewById(R.id.app_share_ll);
		mSuggestionsLL = (LinearLayout) findViewById(R.id.suggestions_ll);
		mContactUsLL = (LinearLayout) findViewById(R.id.contact_us_ll);

		mTitle.setText(R.string.setting);
		mBack.setOnClickListener(this);

		mHelpLL.setOnClickListener(this);
		mAppUpdateLL.setOnClickListener(this);
		mShareLL.setOnClickListener(this);
		mSuggestionsLL.setOnClickListener(this);
		mContactUsLL.setOnClickListener(this);
		mLogoutLL.setOnClickListener(this);

	}

	private void initData() {
		mShareAPI = UMShareAPI.get(this);
		showHead();

		if (mUser.isIs_login()) {
			initPopularizeinfo();
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.help_ll:
			startOtherWeb(this, this.getString(R.string.help), MyURL.HELP_URL);
			break;
		case R.id.app_update_ll:
			onClickUpdate();
			break;
		case R.id.app_share_ll:
			String promotecode = null;
			if (mUser.isIs_login()) {
				promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
			}
			share(promotecode);
			break;
		case R.id.suggestions_ll:
			mUser = UserController.getInstance(this).getUserInfo();
			if (mUser.isIs_login()) {
				String url = MyURL.SUGGESTIONS_URL + mUser.getId();
				startOtherWeb(this, this.getString(R.string.suggestions), url);
			} else {
				hintLogin(this);
			}
			break;
		case R.id.contact_us_ll:
			startOtherWeb(this, this.getString(R.string.contact_us),
					MyURL.CONTACT_US_URL);
			break;

		case R.id.logout_btn:
			deletePCodeInfo();
			if (mUser.isIs_three_login()) {
				String type = mUser.getType();
				if ("qzone".equals(type)) {
					deleteOauth(SHARE_MEDIA.QZONE);
				} else if ("wechat".equals(type)) {
					deleteOauth(SHARE_MEDIA.WEIXIN);
				}
			}
			gotoLogout();
			showHead();
			break;

		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}

	}

	/**
	 * 退出登录时删除 推广码信息
	 */
	private void deletePCodeInfo() {
		String path = FileUtil.getSDPath() + "/" + MyConstants.PCODE;
		FileUtil.deleteFile(new File(path));
	}

	/**
	 * 检查更新
	 */
	private void onClickUpdate() {
		new UpdateManager(this, false);
	}

	private void deleteOauth(SHARE_MEDIA platform) {
		mShareAPI.deleteOauth(this, platform, new UMAuthListener() {

			@Override
			public void onComplete(SHARE_MEDIA platform, int action,
					Map<String, String> data) {
				// ToastUtils.show("delete Authorize succeed");
			}

			public void onError(SHARE_MEDIA platform, int action, Throwable t) {
				ToastUtils.show("delete Authorize fail");
			}

			@Override
			public void onCancel(SHARE_MEDIA platform, int action) {
				// ToastUtils.show("delete Authorize cancel");
			}

		});
	}

	/**
	 * 退出登录
	 */
	public void gotoLogout() {
		UserController mUserController = UserController
				.getInstance(getApplication());
		UserBean user = mUserController.getUserInfo();
		user.setUname("");
		user.setPassword("");
		user.setId("");
		user.setToken("");
		user.setIs_login(false);
		user.setTemp_id("");
		user.setTemp_token("");
		user.setIs_temp_login(false);
		user.setOpen_id("");
		user.setType("");
		user.setIs_three_login(false);
		mUserController.saveUserInfo(user);
		ToastUtils.show(R.string.logout_ok);
	}

	/**
	 * 显示用户头像
	 */
	private void showHead() {
		mUser = UserController.getInstance(this).getUserInfo();
		if (mUser.isIs_login()) {
			mLogoutLL.setVisibility(View.VISIBLE);
		} else {
			mLogoutLL.setVisibility(View.GONE);
		}
	}

	private void initPopularizeinfo() {
		String promotecode = AndroidUtils.getPromoteCodeInfo(mUser.getId());
		if (StringUtils.areEmpty(promotecode)) {
			NetHelper.getPromoteInfo(mUser.getId(),
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							LogUtils.logd(TAG, "initPopularizeinfo+onSuccess");
							LogUtils.logd(TAG, "mUser.getId()" + mUser.getId());
							resolvePromoteInfo(arg2);

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							LogUtils.logd(TAG, "initPopularizeinfo+onFailure");

						}
					});
		}
	}

	/**
	 * 解析 我的推广信息
	 * 
	 * @param arg2
	 */
	private void resolvePromoteInfo(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			if ("0".equals(code)) {
				JSONObject result = obj.optJSONObject("result");
				String mPromoteCode = result.optString("promote_code");
				AndroidUtils.savePromoteCodeInfo(mUser.getId(), mPromoteCode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
