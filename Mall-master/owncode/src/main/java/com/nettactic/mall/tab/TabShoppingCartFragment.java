package com.nettactic.mall.tab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.OrderDetailsActivity;
import com.nettactic.mall.activity.OrderPayActivity;
import com.nettactic.mall.activity.WorksActivity;
import com.nettactic.mall.adapter.OrderChildAdapter;
import com.nettactic.mall.adapter.OrderHolder;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.bean.OrderBean;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.bean.WorksInfoBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.database.WorksSQLHelper;
import com.nettactic.mall.dialog.CustomDialog;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.SharedpreferncesUtil;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.MyListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TabShoppingCartFragment extends BaseFragment implements View.OnClickListener {

    private ListView mListView;
    private RelativeLayout mViewNull;
    private LinearLayout mContentLl;
    private TextView mTitle;
    private ImageView mBack;
    private Button mNullLeftBt;
    private Button mGotoHomeBt;
    private ImageView mPromptImage;
    private TextView mPromptInfo;
    private boolean mIsNoNull = false;
    private UserBean mUser;
    private View mView;
    private RelativeLayout mBottomView;
    private RelativeLayout mBottomShoppingView;
    private RelativeLayout mBottomEditView;
    private TextView mRightTextview;
    private List<WorksInfoBean> mlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab_shopping_cart, null);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        mUser = UserController.getInstance(getActivity()).getUserInfo();
        mIsNoNull = SharedpreferncesUtil.getOrder(getActivity());
        initView(mView);
        if (!AndroidUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.show(R.string.no_net);
        }
        initData();
        return mView;
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_textview:

                break;

            case R.id.goto_home_bt:
//                AndroidUtils.finishActivity(this);
                break;
            case R.id.null_left_bt:
//                AndroidUtils.finishActivity(this);
                break;
            case R.id.back:
//                AndroidUtils.finishActivity(this);
                break;

            default:
                break;
        }
    }

    public void initView(View view) {
        mTitle = (TextView) view.findViewById(R.id.title);
        mListView = (ListView) view.findViewById(R.id.listview);
        mViewNull = (RelativeLayout) view.findViewById(R.id.in_view_null);
        mNullLeftBt = (Button) view.findViewById(R.id.null_left_bt);
        mGotoHomeBt = (Button) view.findViewById(R.id.goto_home_bt);
        mPromptImage = (ImageView) view.findViewById(R.id.prompt_image);
        mBack = (ImageView) view.findViewById(R.id.back);
        mPromptInfo = (TextView) view.findViewById(R.id.prompt_info);
        mContentLl = (LinearLayout) view.findViewById(R.id.content_ll);

        mBottomView = (RelativeLayout) view.findViewById(R.id.view_bottom);
        mBottomShoppingView = (RelativeLayout) view.findViewById(R.id.view_shopping);
        mBottomEditView = (RelativeLayout) view.findViewById(R.id.view_edit);
        mRightTextview = (TextView) view.findViewById(R.id.right_textview);

        mTitle.setText(R.string.tab_shopping_cart);
        mPromptImage.setImageResource(R.mipmap.order_null);
        mPromptInfo.setText(this.getString(R.string.no_order_info));
        if (mUser.isIs_login() && mIsNoNull) {
            mViewNull.setVisibility(View.GONE);
            mContentLl.setVisibility(View.VISIBLE);
        } else {
            mViewNull.setVisibility(View.VISIBLE);
            mContentLl.setVisibility(View.GONE);
        }

        mBack.setOnClickListener(this);
        mNullLeftBt.setOnClickListener(this);
        mGotoHomeBt.setOnClickListener(this);
        mRightTextview.setOnClickListener(this);

    }

}