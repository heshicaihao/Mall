package com.nettactic.mall.dialog;

import java.util.Timer;
import java.util.TimerTask;

import com.nettactic.mall.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class UploadCustomDialog extends AlertDialog {
	
	public  Context context;
	
	public UploadCustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	private TextView tv_loading;
	private ProgressBar progressBar;

	private Timer timer;
	private int count = 1;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_upload_progress);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		progressBar = (ProgressBar) findViewById(R.id.pb);

		// 设置Dialog显示的宽度，
		Display d = getWindow().getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		// 这里设置为屏幕宽度的百分之八十
		lp.width = (int) (d.getWidth() * 0.8);
		getWindow().setAttributes(lp);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 300, 300);
		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (timer != null) {
					timer.cancel();
				}
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			count++;
			if (count > 3) {
				count = 1;
			}
			String textpre = context.getString(R.string.saving_works);
			String text = null;
			switch (count) {
			case 1:
				text = textpre + context.getString(R.string.one_spot);
				tv_loading.setText(text);
				break;
			case 2:
				text = textpre + context.getString(R.string.two_spot);
				tv_loading.setText(text);
				break;
			case 3:
				text = textpre + context.getString(R.string.three_spot);
				tv_loading.setText(text);
				break;
			}
		}
	};

	public void setProgress(int progress) {
		progressBar.setProgress(progress);
		if (progress == 100) {
			this.dismiss();
		}
	}
	

}
