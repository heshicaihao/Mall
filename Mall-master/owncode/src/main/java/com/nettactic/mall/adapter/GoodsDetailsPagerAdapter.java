package com.nettactic.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nettactic.mall.R;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.fragment.GoodsCommentFragment;
import com.nettactic.mall.fragment.GoodsDetailFragment;
import com.nettactic.mall.fragment.GoodsInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshicaihao on 2017/7/13.
 */

public class GoodsDetailsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<String> mTitleName = new ArrayList<String>();

    public GoodsDetailsPagerAdapter(FragmentManager fm) {
        super(fm);
        GoodsInfoFragment mGoodsInfoFragment = new GoodsInfoFragment();
        GoodsDetailFragment mGoodsDetailFragment = new GoodsDetailFragment();
        GoodsCommentFragment mGoodsCommentFragment = new GoodsCommentFragment();
        mFragmentList.add(mGoodsInfoFragment);
        mFragmentList.add(mGoodsDetailFragment);
        mFragmentList.add(mGoodsCommentFragment);
        mTitleName.add(MyApplication.getContext().getString(R.string.goods));
        mTitleName.add(MyApplication.getContext().getString(R.string.details));
        mTitleName.add(MyApplication.getContext().getString(R.string.evaluate));

    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return mTitleName.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleName.get(position);
    }

}
