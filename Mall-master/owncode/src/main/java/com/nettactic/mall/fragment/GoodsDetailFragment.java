package com.nettactic.mall.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseFragment;

public class GoodsDetailFragment extends BaseFragment implements View.OnClickListener {

    private static final int VIEW_GOODSDETAIL_LEFT = 10;
    private static final int VIEW_GOODSDETAIL_MIDDLE = 11;
    private static final int VIEW_GOODSDETAIL_RIGHT = 12;
    private int mSign = VIEW_GOODSDETAIL_LEFT;
    private int deText = R.color.pay_color_black;
    private int peText = R.color.main_color;
    private TextView mLeftTv;
    private TextView mMiddleTv;
    private TextView mRightTv;
    private FragmentManager mFragmentManager;
    private GoodsDetailLeftFragment mGoodsDetailLeftFragment = null;
    private GoodsDetailMiddleFragment mGoodsDetailMiddleFragment = null;
    private GoodsDetailRightFragment mGoodsDetailRightFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goods_details, container,
                false);
        initView(rootView);
        initData();
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.goods_details_goods_introduce_tv:
                mSign = VIEW_GOODSDETAIL_LEFT;
                changButUI(mSign);
                break;
            case R.id.goods_details_goods_config_tv:
                mSign = VIEW_GOODSDETAIL_MIDDLE;
                changButUI(mSign);
                break;
            case R.id.goods_details_after_sale_packing_tv:
                mSign = VIEW_GOODSDETAIL_RIGHT;
                changButUI(mSign);
                break;
            default:
                break;
        }
    }

    private void changButUI(int mSign) {
        setDefaultAllUI();
        switch (mSign) {
            case VIEW_GOODSDETAIL_LEFT:
                showFragment(mGoodsDetailLeftFragment);
                mLeftTv.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_GOODSDETAIL_MIDDLE:
                showFragment(mGoodsDetailMiddleFragment);
                mMiddleTv.setTextColor(getResources().getColor(peText));

                break;
            case VIEW_GOODSDETAIL_RIGHT:
                showFragment(mGoodsDetailRightFragment);
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
        if (mGoodsDetailLeftFragment == null) {
            mGoodsDetailLeftFragment = new GoodsDetailLeftFragment();
        }
        if (mGoodsDetailMiddleFragment == null) {
            mGoodsDetailMiddleFragment = new GoodsDetailMiddleFragment();
        }
        if (mGoodsDetailRightFragment == null) {
            mGoodsDetailRightFragment = new GoodsDetailRightFragment();
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
        mTransaction.add(R.id.goods_details_content, mGoodsDetailLeftFragment);
        mTransaction.add(R.id.goods_details_content, mGoodsDetailMiddleFragment);
        mTransaction.add(R.id.goods_details_content, mGoodsDetailRightFragment);
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
        mTransaction.hide(mGoodsDetailLeftFragment);
        mTransaction.hide(mGoodsDetailMiddleFragment);
        mTransaction.hide(mGoodsDetailRightFragment);
        mTransaction.commitAllowingStateLoss();
    }

    private void initView(View view) {
        initFragment();
        mLeftTv = (TextView) view.findViewById(R.id.goods_details_goods_introduce_tv);
        mMiddleTv = (TextView) view.findViewById(R.id.goods_details_goods_config_tv);
        mRightTv = (TextView) view.findViewById(R.id.goods_details_after_sale_packing_tv);

        mLeftTv.setOnClickListener(this);
        mMiddleTv.setOnClickListener(this);
        mRightTv.setOnClickListener(this);

    }

    private void initData() {
        addFragment();
        hiddenFragment();
        changButUI(VIEW_GOODSDETAIL_LEFT);

    }


}
