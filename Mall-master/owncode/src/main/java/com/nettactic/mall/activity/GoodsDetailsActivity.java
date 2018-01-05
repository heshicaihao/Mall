package com.nettactic.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.adapter.GoodsDetailsPagerAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.widget.NoScrollViewPager;
import com.nettactic.mall.widget.PagerSlidingTabStrip;

public class GoodsDetailsActivity extends BaseActivity implements View.OnClickListener {

    public NoScrollViewPager mContentPager;
    public PagerSlidingTabStrip mTabs;
    public TextView mImageAndTextTitle;

    private ImageView mBack;
    private ImageView mShare;
    private TextView mCollection;
    private TextView mShoppingCart;
    private TextView mAddShoppingCart;
    private TextView mNowBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        initView();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.collection:
                break;

            case R.id.shopping_cart:
                gotoShoppingCartActivity();
                break;

            case R.id.add_shopping_cart:
                break;

            case R.id.now_buy:
                break;

            case R.id.back:
                this.finish();
                AndroidUtils.exitActvityAnim(this);
                break;

            case R.id.share:
                break;

            default:
                break;

        }
    }

    private void gotoShoppingCartActivity() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(this);
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
        mContentPager = (NoScrollViewPager) this.findViewById(R.id.content_pager);
        mTabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
        mImageAndTextTitle = (TextView) findViewById(R.id.image_text_title_tv);

        mBack = (ImageView) findViewById(R.id.back);
        mShare = (ImageView) findViewById(R.id.share);

        mCollection = (TextView) findViewById(R.id.collection);
        mShoppingCart = (TextView) findViewById(R.id.shopping_cart);
        mAddShoppingCart = (TextView) findViewById(R.id.add_shopping_cart);
        mNowBuy = (TextView) findViewById(R.id.now_buy);

        mBack.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mShoppingCart.setOnClickListener(this);
        mAddShoppingCart.setOnClickListener(this);
        mNowBuy.setOnClickListener(this);
        setPagerAdapter();

    }

    private void setPagerAdapter() {
        GoodsDetailsPagerAdapter mAdapter = new GoodsDetailsPagerAdapter(getSupportFragmentManager());
        mContentPager.setAdapter(mAdapter);
        mContentPager.setOffscreenPageLimit(3);
        mContentPager.setPageTransformer(true, null);
        mTabs.setTextColorResource(R.color.light_gray_text);
        mTabs.setDividerColorResource(R.color.common_list_divider);
        mTabs.setIndicatorColorResource(R.color.pay_color_black);
        mTabs.setSelectedTextColorResource(R.color.pay_color_black);
        mTabs.setViewPager(mContentPager);
    }

}
