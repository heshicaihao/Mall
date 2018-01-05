package com.nettactic.mall.dialog;


import com.nettactic.mall.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;



/**
 * @Description:自定义对话框
 * @author http://blog.csdn.net/finddreams
 */
public class FrameProgressDialog extends ProgressDialog {

	public FrameProgressDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_frame_progress);
	}

}
