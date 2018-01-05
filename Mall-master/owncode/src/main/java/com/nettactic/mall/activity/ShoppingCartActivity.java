package com.nettactic.mall.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.utils.AndroidUtils;

/**
 * heshicaihao
 */
public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mTitle;
    private RelativeLayout mBottomView;
    private RelativeLayout mBottomShoppingView;
    private RelativeLayout mBottomEditView;
    private TextView mRightTv;
    private boolean mIsEdit = false;
    private RelativeLayout mNullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_textview:
                if (mIsEdit) {
                    mIsEdit = false;
                    setBottomView(mIsEdit);
                } else {
                    mIsEdit = true;
                    setBottomView(mIsEdit);
                }

                break;

            case R.id.back:
                this.finish();
                AndroidUtils.exitActvityAnim(this);
                break;

            default:

                break;

        }

    }

    /**
     * 显示底部栏
     *
     * @param mIsEdit
     */
    private void setBottomView(boolean mIsEdit) {
        mBottomView.setVisibility(View.VISIBLE);
        if (mIsEdit) {
            mRightTv.setText(MyApplication.getContext().getString(R.string.over));
            mBottomShoppingView.setVisibility(View.GONE);
            mBottomEditView.setVisibility(View.VISIBLE);
        } else {
            mRightTv.setText(MyApplication.getContext().getString(R.string.edit));
            mBottomShoppingView.setVisibility(View.VISIBLE);
            mBottomEditView.setVisibility(View.GONE);
        }
    }

    /**
     * 监听返回
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mBottomView = (RelativeLayout) findViewById(R.id.view_bottom);
        mBottomShoppingView = (RelativeLayout) findViewById(R.id.view_shopping);
        mBottomEditView = (RelativeLayout) findViewById(R.id.view_edit);
        mRightTv = (TextView) findViewById(R.id.right_textview);
        mNullView = (RelativeLayout) findViewById(R.id.in_view_null);

        setBottomView(mIsEdit);
        mTitle.setText(MyApplication.getContext().getString(R.string.tab_shopping_cart));
        mBack.setOnClickListener(this);
        mRightTv.setOnClickListener(this);

    }

    private void initData() {

    }

}
