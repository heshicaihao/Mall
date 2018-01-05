package com.nettactic.mall.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.activity.GoodsDetailsActivity;
import com.nettactic.mall.activity.ManageAddressActivity;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.AssetsUtils;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.widget.HackyViewPager;
import com.nettactic.mall.widget.SlideDetailsLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoodsInfoFragment extends BaseFragment implements SlideDetailsLayout.OnSlideDetailsListener, View.OnClickListener {

    private static final String STATE_POSITION = "STATE_POSITION";
    private static final int VIEW_GOODSINFO_LEFT = 20;
    private static final int VIEW_GOODSINFO_MIDDLE = 21;
    private static final int VIEW_GOODSINFO_RIGHT = 22;
    public GoodsDetailsActivity mActivity;
    private int mSign = VIEW_GOODSINFO_LEFT;
    private int mPagerPosition;
    private int deText = R.color.pay_color_black;
    private int peText = R.color.main_color;
    private List<String> list = new ArrayList<String>();
    private SlideDetailsLayout mSlideDetailsLayout;
    private LinearLayout mPullUpLl;
    private HackyViewPager mHackyViewPager;
    private TextView mIndicator;
    private TextView mLeftTv;
    private TextView mMiddleTv;
    private TextView mRightTv;
    private ImageView mStickIv;
    private ScrollView mScrollView;
    private FragmentManager mFragmentManager;
    private GoodsInfoLeftFragment mGoodsInfoLeftFragment = null;
    private GoodsInfoMiddleFragment mGoodsInfoMiddleFragment = null;
    private GoodsInfoRightFragment mGoodsInfoRightFragment = null;
    private Bundle savedInstanceState;
    private JSONArray jsonarray;
    private LinearLayout mSelectedContentLl;
    private TextView mSelectedContentTv;
    private LinearLayout mAddressLl;
    private TextView mSentToAddressTv;
    private String code04 = "10004";
    private String data = JSONUtil.getData(code04, "2");
    private int mCout = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (GoodsDetailsActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goods_info, container,
                false);
        this.savedInstanceState = savedInstanceState;
        initView(rootView);
        initData();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        mCout++;
        if (mCout>1){
            mSentToAddressTv.setText("广东深圳福田");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_introduce_tv:
                mSign = VIEW_GOODSINFO_LEFT;
                changButUI(mSign);
                break;
            case R.id.goods_config_tv:
                mSign = VIEW_GOODSINFO_MIDDLE;
                changButUI(mSign);
                break;
            case R.id.after_sale_packing_tv:
                mSign = VIEW_GOODSINFO_RIGHT;
                changButUI(mSign);
                break;
            case R.id.ll_pull_up:
                mActivity.mContentPager.setCurrentItem(1);
                break;
            case R.id.stick_iv:
                //点击滑动到顶部
                mScrollView.smoothScrollTo(0, 0);
                mSlideDetailsLayout.smoothClose(true);
                break;

            case R.id.selected_content_ll:

                break;

            case R.id.address_ll:
                gotoManageAddress(data);
                break;

            default:
                break;
        }
    }

    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.OPEN) {
            //当前为图文详情页
            mStickIv.setVisibility(View.VISIBLE);
            mActivity.mContentPager.setNoScroll(true);
            mActivity.mImageAndTextTitle.setVisibility(View.VISIBLE);
            mActivity.mTabs.setVisibility(View.GONE);
        } else {
            //当前为商品详情页
            mStickIv.setVisibility(View.GONE);
            mActivity.mContentPager.setNoScroll(false);
            mActivity.mImageAndTextTitle.setVisibility(View.GONE);
            mActivity.mTabs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mHackyViewPager.getCurrentItem());
    }

    private void setAdapter() {
        CharSequence text = getString(R.string.viewpager_indicator, 1, mHackyViewPager
                .getAdapter().getCount());
        mIndicator.setText(text);
        // 更新下标
        mHackyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mHackyViewPager.getAdapter().getCount());
                mIndicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            mPagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mHackyViewPager.setCurrentItem(mPagerPosition);
    }

    private void resolveInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                jsonarray = result.optJSONArray("image_info");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = (JSONObject) jsonarray.get(i);
                    String name = jsonobject.optString("image_url");
                    list.add(name);
                    ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                            this.getChildFragmentManager(), list);
                    mHackyViewPager.setAdapter(mAdapter);
                    setAdapter();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void changButUI(int mSign) {
        setDefaultAllUI();
        switch (mSign) {
            case VIEW_GOODSINFO_LEFT:
                showFragment(mGoodsInfoLeftFragment);
                mLeftTv.setTextColor(getResources().getColor(peText));
                break;
            case VIEW_GOODSINFO_MIDDLE:
                showFragment(mGoodsInfoMiddleFragment);
                mMiddleTv.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_GOODSINFO_RIGHT:
                showFragment(mGoodsInfoRightFragment);
                mRightTv.setTextColor(getResources().getColor(peText));

                break;
            default:
                break;
        }
    }

    private void setDefaultAllUI() {
        mLeftTv.setTextColor(getResources().getColor(deText));

        mMiddleTv.setTextColor(getResources().getColor(deText));

        mRightTv.setTextColor(getResources().getColor(deText));
    }

    private void initFragment() {
        mFragmentManager = getActivity().getSupportFragmentManager();
        if (mGoodsInfoLeftFragment == null) {
            mGoodsInfoLeftFragment = new GoodsInfoLeftFragment();
        }
        if (mGoodsInfoMiddleFragment == null) {
            mGoodsInfoMiddleFragment = new GoodsInfoMiddleFragment();
        }
        if (mGoodsInfoRightFragment == null) {
            mGoodsInfoRightFragment = new GoodsInfoRightFragment();
        }

    }

    /**
     * @throws
     * @Title: showFragment
     * @Description:
     * @param: @param fragment
     * @return: void
     */
    private void showFragment(BaseFragment fragment) {
        hiddenFragment();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.show(fragment);
        mTransaction.commitAllowingStateLoss();

    }

    /**
     * @throws
     * @Title: addFragment
     * @Description:
     * @param:
     * @return: void
     */

    private void addFragment() {

        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.goods_info_content, mGoodsInfoLeftFragment);
        mTransaction.add(R.id.goods_info_content, mGoodsInfoMiddleFragment);
        mTransaction.add(R.id.goods_info_content, mGoodsInfoRightFragment);
        mTransaction.commitAllowingStateLoss();
    }

    /**
     * @throws
     * @Title: hiddenFragment
     * @Description:
     * @param:
     * @return: void
     */
    private void hiddenFragment() {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mGoodsInfoLeftFragment);
        mTransaction.hide(mGoodsInfoMiddleFragment);
        mTransaction.hide(mGoodsInfoRightFragment);
        mTransaction.commitAllowingStateLoss();
    }


    private void initView(View view) {
        initFragment();
        mHackyViewPager = (HackyViewPager) view.findViewById(R.id.pager);
        mIndicator = (TextView) view.findViewById(R.id.indicator);
        mSlideDetailsLayout = (SlideDetailsLayout) view.findViewById(R.id.sv_switch);
        mPullUpLl = (LinearLayout) view.findViewById(R.id.ll_pull_up);
        mLeftTv = (TextView) view.findViewById(R.id.goods_introduce_tv);
        mMiddleTv = (TextView) view.findViewById(R.id.goods_config_tv);
        mRightTv = (TextView) view.findViewById(R.id.after_sale_packing_tv);
        mStickIv = (ImageView) view.findViewById(R.id.stick_iv);
        mScrollView = (ScrollView) view.findViewById(R.id.sv_goods_info);
        mSelectedContentLl = (LinearLayout) view.findViewById(R.id.selected_content_ll);
        mSelectedContentTv = (TextView) view.findViewById(R.id.selected_content);
        mAddressLl = (LinearLayout) view.findViewById(R.id.address_ll);
        mSentToAddressTv = (TextView) view.findViewById(R.id.sent_to_address);

        ViewGroup.LayoutParams lp = mHackyViewPager.getLayoutParams();
        lp.width = AndroidUtils.getScreenWidth(getActivity());
        lp.height = lp.width * 480 / 800;
        mHackyViewPager.setLayoutParams(lp);
        mSlideDetailsLayout.setOnSlideDetailsListener(this);
        mPullUpLl.setOnClickListener(this);
        mLeftTv.setOnClickListener(this);
        mMiddleTv.setOnClickListener(this);
        mRightTv.setOnClickListener(this);
        mStickIv.setOnClickListener(this);
        mSelectedContentLl.setOnClickListener(this);
        mAddressLl.setOnClickListener(this);

    }

    private void initData() {

        addFragment();
        hiddenFragment();
        changButUI(VIEW_GOODSINFO_LEFT);
        String json = AssetsUtils.getJson(getContext(), "image_url.txt");
        resolveInfo(json);
    }

    private void gotoManageAddress(String data) {
        Intent intent = new Intent(getActivity(),
                ManageAddressActivity.class);
        intent.putExtra("data", data.toString());
        getActivity().startActivity(intent);
        AndroidUtils.enterActvityAnim(getActivity());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<String> list;

        public ImagePagerAdapter(FragmentManager fm, List<String> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = list.get(position);
            return GoodsInfoImageFragment.newInstance(url, jsonarray, position);
        }
    }

}
