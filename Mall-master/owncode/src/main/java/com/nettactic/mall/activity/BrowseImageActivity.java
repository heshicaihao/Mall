package com.nettactic.mall.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.fragment.BrowseImageFragment;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BrowseImageActivity extends FragmentActivity implements View.OnClickListener, OnPageChangeListener {
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private static final String STATE_POSITION = "STATE_POSITION";
    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView mIndicator;
    private ImageView mBack;
    private ImageView mSave;
    private ImageView mImageView;
    private JSONArray mUrls;
    private Bundle mState;
    private String url;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_image);
        this.mState = savedInstanceState;
        getIntentData();
        initView();
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                AndroidUtils.exitActvityAnim(this);

                break;
            case R.id.save:
                String myUrl = getUrl(mUrls, position);
                LoadPic(myUrl);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            AndroidUtils.exitActvityAnim(this);
        }
        return false;
    }

    @Override
    public void onPageSelected(int position) {
        CharSequence text = getString(R.string.viewpager_indicator,
                position + 1, mPager.getAdapter().getCount());
        mIndicator.setText(text);
        this.position = position;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initData() {
        setStata();
    }

    private void initView() {
        mPager = (HackyViewPager) findViewById(R.id.pager);
        mIndicator = (TextView) findViewById(R.id.indicator);
        mBack = (ImageView) findViewById(R.id.back);
        mSave = (ImageView) findViewById(R.id.save);
        mImageView = (ImageView) findViewById(R.id.imageview);

        setAdapter();
        mPager.setOnPageChangeListener(this);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    /**
     * 从Intent 获取数据
     */
    private void getIntentData() {
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String urls0 = getIntent().getStringExtra(EXTRA_IMAGE_URLS);
        try {
            mUrls = new JSONArray(urls0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置初始状态
     */
    private void setStata() {
        if (mState != null) {
            pagerPosition = mState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(pagerPosition);
    }

    /**
     * 设置 Adapter
     */
    private void setAdapter() {
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), mUrls);
        mPager.setAdapter(mAdapter);
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        mIndicator.setText(text);
    }

    private String getUrl(JSONArray fileList, int position) {
        String url = null;
        try {
            JSONObject jsonobject = (JSONObject) fileList.get(position);
            url = jsonobject.optString("image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;

    }


    /**
     * 下载图片
     *
     * @param myUrl
     */
    private void LoadPic(String myUrl) {
        ImageLoader.getInstance().displayImage(myUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                ToastUtils.show(message);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                String name = FileUtil.getenURl(url);
                String path = FileUtil.getFilePath(MyConstants.PIC_DIR, name, MyConstants.JPEG);
                FileUtil.saveBitmap2Jpeg(loadedImage, MyConstants.PIC_DIR, name, MyConstants.JPEG);
                String show = MyApplication.getContext().getString(R.string.pic_ok) + path;
                ToastUtils.show(show);
            }
        });
    }


    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public JSONArray fileList;

        public ImagePagerAdapter(FragmentManager fm, JSONArray fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            url = getUrl(fileList, position);
            return BrowseImageFragment.newInstance(url);
        }
    }
}