package com.nettactic.mall.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nettactic.mall.R;
import com.nettactic.mall.base.BaseFragment;

public class GoodsCommentFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goods_comment, container,
                false);

        return rootView;

    }

}
