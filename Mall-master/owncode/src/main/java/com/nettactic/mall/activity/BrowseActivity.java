package com.nettactic.mall.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.bean.WorksInfoBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.SelectPicSQLHelper;
import com.nettactic.mall.database.WorksSQLHelper;
import com.nettactic.mall.dialog.CustomDialog;
import com.nettactic.mall.dialog.UploadCustomDialog;
import com.nettactic.mall.fragment.BrowseFragment;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.ServiceUtils;
import com.nettactic.mall.utils.StandardPokerDataUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.MyHScrollView;

/**
 * 作品浏览
 * 
 * @author heshicaihao
 * 
 */
@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "HandlerLeak" })
public class BrowseActivity extends BaseActivity implements OnClickListener {
	private List<Integer> mDatas = new ArrayList<Integer>();

	private TextView mBack;
	private TextView mTitle;
	private TextView mSave;
	private TextView mBuy;
	private ImageView mLeft;
	private ImageView mRight;
	private ViewPager mViewPager;
	private MyHScrollView mHScrollview;
	private MyPagerAdapter mPagerAdapter;
	private String mTotalpage;
	private String mWorkId;
	private ACache mCache;
	private List<SelectpicBean> listSelectpic;
	private UserBean mUser;
	private ArrayList<String> mWorksIds = new ArrayList<String>();
	private String mAccountId;
	private String mAccountToken;
	private String mTemplateId;
	private UploadCustomDialog mUploadCustomDialog;
	private Boolean mIsBuy = false;
	private int count = 0;
	private int mSaveCount = 0;
	private int page = 0;
	private File contentFile;
	private Handler mDownloadHandler;
	private static final int DOWNLOAD_OK = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setAttributes(params);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		setContentView(R.layout.activity_browse);
//		AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
		ServiceUtils.startUploadService(this, BrowseActivity.this);
		mUser = UserController.getInstance(this).getUserInfo();
		initView();
		initData();

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			showDialog(this);
			break;

		case R.id.save:
			mIsBuy = false;
			saveWorks();

			break;

		case R.id.buy:
			mIsBuy = true;
			saveWorks();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(this);
		}
		return false;
	}

	private void gotoSubmitOrder() {
		mWorksIds.add(mWorkId);
		if (mWorksIds.size() == 0) {
			ToastUtils.show(R.string.choice_works);
			return;
		}
		Intent intent = new Intent(this, SubmitOrderActivity.class);
		intent.putExtra(MyConstants.WORKS_IDS, mWorksIds);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 
	 * 保存作品
	 */
	private void saveWorks() {
		mSaveCount = 0;
		page = Integer.parseInt(mTotalpage);
		mUploadCustomDialog.show();
		deleteUnSelectPic();
		savaWorks2BD();
		savaWorks2SD();
		uploadWorkInfo(contentFile);
		showUploadDialog();
		refreshChangFrameUI();
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
					// getWorksStatus();
					if (mIsBuy) {
						gotoSubmitOrder();
					} else {
						gotoOther(MyApplication.getContext(),
								WorksActivity.class);
					}
					AndroidUtils.finishActivity(BrowseActivity.this);
					break;
				}
			}
		};
	}

	private void showUploadDialog() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				count = getCout();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mUploadCustomDialog != null
								&& mUploadCustomDialog.isShowing()) {
							mUploadCustomDialog.setProgress(count);
						}
					}
				});
				if (count >= 100) {
					mSaveCount++;
					if (mSaveCount == 2) {
						mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
					}
					LogUtils.logd(TAG, "showUploadDialog:" + "OK");
					timer.cancel();

				}
			}
		}, 0, 500);
		mUploadCustomDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						if (timer != null)
							timer.cancel();
						count = 0;
						mUploadCustomDialog.dismiss();
					}
				});
	}

	/**
	 * 保存作品信息到SD卡
	 */
	private void savaWorks2SD() {
		listSelectpic = SelectPicSQLHelper.queryByIsselect("true", mWorkId);
		Collections.sort(listSelectpic, new SelectpicBean());
		JSONObject str2Net = JsonDao.getNewWorksInfo(listSelectpic);
		String contentStr = str2Net.toString();
		FileUtil.saveFile(contentStr, FileUtil.getWorksDir(mWorkId), mWorkId,
				MyConstants.JSON);
		String mWorkInfopath = FileUtil.getFilePath(
				FileUtil.getWorksDir(mWorkId), mWorkId, MyConstants.JSON);
		contentFile = new File(mWorkInfopath);

	}

	/**
	 * 删除未选择图片 和数据记录
	 */
	private void deleteUnSelectPic() {
		List<SelectpicBean> unlistSelectpic = SelectPicSQLHelper
				.queryByIsselect("false", mWorkId);
		for (int i = 0; i < unlistSelectpic.size(); i++) {
			SelectpicBean unselectpicBean = unlistSelectpic.get(i);
			String imgid = unselectpicBean.getImgid();
			SelectPicSQLHelper.deleteImgid(imgid);
			FileUtil.deleteFromSD(FileUtil.getWorksDir(mWorkId), imgid,
					MyConstants.JPEG);
		}
	}

	/**
	 * 保存作品到数据库
	 */
	private void savaWorks2BD() {

		String account_id = mUser.getId();
		String type = mCache.getAsString(MyConstants.GOODSTYPE);
		String lasttime = System.currentTimeMillis() + "";
		String mJsonStr = mTotalpage;
		String mTitleStr = mCache.getAsString(MyConstants.GOODSNAME);
		String mQuantity = "1";
		String mPrice = mCache.getAsString(MyConstants.GOODSPRICE);
		String mCoverId = mCache.getAsString(MyConstants.GOODSURL);
		String mGoodsId = mCache.getAsString(MyConstants.GOODSID);
		String mPicId = mTemplateId;
		String mProductId = "0";
		String mAccountId = account_id;
		WorksInfoBean works = new WorksInfoBean(mWorkId, mJsonStr, mTitleStr,
				mQuantity, mPrice, mCoverId, mGoodsId, type, mPicId,
				mProductId, mAccountId, "0", lasttime);
		WorksSQLHelper.addWorks(works);
	}

	private void initView() {

		mTitle = (TextView) findViewById(R.id.title);
		mBack = (TextView) findViewById(R.id.back);
		mSave = (TextView) findViewById(R.id.save);
		mBuy = (TextView) findViewById(R.id.buy);
		mLeft = (ImageView) findViewById(R.id.left);
		mRight = (ImageView) findViewById(R.id.right);
		mViewPager = (ViewPager) findViewById(R.id.middle_viewpage);
		mHScrollview = (MyHScrollView) findViewById(R.id.h_scrollview);

		mTitle.setText(R.string.browse_works);
		Drawable drawableBack= getResources().getDrawable(R.mipmap.browse_back);
		drawableBack.setBounds(0, 0, drawableBack.getMinimumWidth(), drawableBack.getMinimumHeight());//这句一定要加  
		mBack.setCompoundDrawables(drawableBack,null,null,null);
		
		Drawable drawableSave= getResources().getDrawable(R.mipmap.save);
		drawableSave.setBounds(0, 0, drawableSave.getMinimumWidth(), drawableSave.getMinimumHeight());//这句一定要加  
		mSave.setCompoundDrawables(drawableSave,null,null,null);
		
		Drawable drawableBuy= getResources().getDrawable(R.mipmap.buy);
		drawableBuy.setBounds(0, 0, drawableBuy.getMinimumWidth(), drawableBuy.getMinimumHeight());//这句一定要加  
		mBuy.setCompoundDrawables(drawableBuy,null,null,null);

		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mBuy.setOnClickListener(this);
		mUploadCustomDialog = new UploadCustomDialog(this);

	}

	private void initData() {
		mAccountId = mUser.getId();
		getDataCache();
		getData();
		mDatas = StandardPokerDataUtils.getData(mTotalpage);
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(1);
		mHScrollview.setViewPager(mViewPager, mLeft, mRight, mDatas);
		mPagerAdapter.notifyDataSetChanged();

	}

	private void getData() {
		listSelectpic = SelectPicSQLHelper.queryByIsselect("true", mWorkId);
		Collections.sort(listSelectpic, new SelectpicBean());

	}

	private void getDataCache() {
		mCache = ACache.get(this);
		mTotalpage = mCache.getAsString(MyConstants.TOTALPAGE);

		mWorkId = mCache.getAsString(MyConstants.WORKID);
		mTemplateId = mCache.getAsString(MyConstants.TEMPLATEID);

	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return new BrowseFragment(arg0);
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Integer integer = mDatas.get(position);
			int integerInt = integer;
			String str = integerInt + "";
			return str;
		}
	}

	/**
	 * 跳转到其他界面
	 */
	private void gotoOther(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 上传作品信息
	 * 
	 */
	private void uploadWorkInfo(File myContent) {
		String work_name = "我在作品" + mWorkId;
		NetHelper.saveWorks(mAccountId, mAccountToken, mWorkId, mTemplateId,
				work_name, myContent, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "uploadWorkInfoonSuccess");
						resolvesaveWorksInfo(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "uploadWorkInfoonFailure");
						// mDialog.dismiss();
					}
				});
	}

	private void resolvesaveWorksInfo(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			if ("0".equals(code)) {
				mSaveCount++;
				if (mSaveCount == 2) {
					mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
				}
			} else {
				ToastUtils.show(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示对话框
	 * 
	 * @param context
	 */
	private void showDialog(Context context) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(context
				.getString(R.string.do_you_want_to_quit_browse));
		// builder.setTitle("提示");
		builder.setPositiveButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
							// AndroidUtils
							// .finishActivity(BrowseActivity.this);

						}
					}
				});

		builder.setNegativeButton(context.getString(R.string.save_works),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
							saveWorks();

						}
					}
				});

		builder.create().show();
	}

	private int getCout() {
		List<String> list = SelectPicSQLHelper.querySelectPicImgid(mWorkId,
				"true", "false");
		int size = list.size();
		int f = page - size;
		double d = f / (double) page;
		int count = (int) (d * 100);
		return count;
	}

}
