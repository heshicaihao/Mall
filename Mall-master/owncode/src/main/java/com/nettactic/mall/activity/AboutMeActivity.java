package com.nettactic.mall.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.net.MyURL;
import com.nettactic.mall.utils.AndroidUtils;

/**
 * 
 * 关于我们
 * 
 * @author heshicaihao
 * 
 */
public class AboutMeActivity extends BaseActivity implements OnClickListener {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mVersionName;
	private LinearLayout mVersionLL;
	private LinearLayout mDisclaimerLL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_me);

		initView();
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mVersionName = (TextView) findViewById(R.id.version_tv);
		mBack = (ImageView) findViewById(R.id.back);
		mVersionLL = (LinearLayout) findViewById(R.id.version_ll);
		mDisclaimerLL = (LinearLayout) findViewById(R.id.disclaimer_ll);

		mTitle.setText(R.string.about_me);
		mVersionName.setText("V"
				+ AndroidUtils.getAppVersionName(getApplicationContext()));
		mBack.setOnClickListener(this);
		mVersionLL.setOnClickListener(this);
		mDisclaimerLL.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.back:
			AndroidUtils.finishActivity(this);

			break;
		case R.id.version_ll:
			startOtherWeb(this, this.getString(R.string.app_name),
					MyURL.APP_INTRODUCTION_URL);
			break;
		case R.id.disclaimer_ll:
			startOtherWeb(this, this.getString(R.string.disclaimer),
					MyURL.DISCLAIMER_URL);
			break;
		default:
			break;
		}
	}

}
