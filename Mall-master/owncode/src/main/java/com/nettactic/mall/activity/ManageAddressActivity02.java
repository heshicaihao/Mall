package com.nettactic.mall.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.adapter.ManageAddressAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.LogUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 管理地址
 *
 * @author heshicaihao
 */
@SuppressWarnings({"deprecation", "ResourceAsColor"})
@SuppressLint({"InlinedApi", "HandlerLeak"})
public class ManageAddressActivity02 extends BaseActivity implements
        OnClickListener, OnItemClickListener, OnRefreshListener {

    private static final int REFRESH_COMPLETE = 0X110;
    private ListView mAddressList;
    private TextView mTitle;
    private RelativeLayout mAdd;
    private RelativeLayout mViewNull;
    private ImageView mBack;
    private Button mNullLeftBt;
    private Button mGotoHomeBt;

    private JSONArray mResult;
    private boolean mIsChoice = false;
    private boolean mIsEidt = false;
    private SwipeRefreshLayout mSwipeLayout;
    private ManageAddressAdapter mAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    if (mResult.length() != 0) {
                        mAddressList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                    mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);

        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.null_left_bt:
                gotoAddAddress();
                break;

            case R.id.goto_home_bt:
                AndroidUtils.finishActivity(this);
                break;

            case R.id.add_receipt_address:
                gotoAddAddress();
                break;
            case R.id.back:
                AndroidUtils.finishActivity(this);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (mResult.length() != 0) {
            JSONObject object = mResult.optJSONObject(position);
            LogUtils.logd(TAG, object.toString());
            if (mIsEidt) {
                gotoReviseAddress(object.toString());
            } else if (mIsChoice) {
                mAdapter.setSeclection(position);
                mAdapter.notifyDataSetInvalidated();
                goBackResult(object.toString());
            }
        }
    }

    @SuppressLint("InlinedApi")
    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mAdd = (RelativeLayout) findViewById(R.id.add_receipt_address);
        mViewNull = (RelativeLayout) findViewById(R.id.address_null_rl);
        mAddressList = (ListView) findViewById(R.id.address_list);
        mNullLeftBt = (Button) findViewById(R.id.null_left_bt);
        mGotoHomeBt = (Button) findViewById(R.id.goto_home_bt);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mBack = (ImageView) findViewById(R.id.back);

        mTitle.setText(R.string.manage_address);

        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mAdd.setOnClickListener(this);
        mAddressList.setOnItemClickListener(this);
        mSwipeLayout.setOnRefreshListener(this);
        mBack.setOnClickListener(this);
        mNullLeftBt.setOnClickListener(this);
        mGotoHomeBt.setOnClickListener(this);

    }

    private void initData() {

        getDataIntent();
        initGetAddressList();

    }

    private void getDataIntent() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        try {
            JSONObject jsonobject = new JSONObject(data);
            String code = jsonobject.getString("code");
            if (code.equals("10003")) {
                mIsChoice = false;
                mIsEidt = true;
            } else if (code.equals("10004")) {
                mIsChoice = true;
                mIsEidt = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void goBackResult(String obj) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("obj", obj);
        this.setResult(SubmitOrderActivity.RESULT_OK, intent);
        AndroidUtils.finishActivity(this);
    }

    private void initGetAddressList() {
        String mAccountId = mUserController.getUserInfo().getId();
        String mAccountToken = mUserController.getUserInfo().getToken();
        NetHelper.getAddressList(mAccountId, mAccountToken,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        LogUtils.logd(TAG, "initGetAddressListonSuccess");
                        resolveGetAddressList(responseBody);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        LogUtils.logd(TAG, "initGetAddressListonFailure");
                    }
                });
    }

    private void resolveGetAddressList(byte[] responseBody) {
        try {

            String json = new String(responseBody, "UTF-8");
            JSONObject JSONObject = new JSONObject(json);
            mResult = JSONObject.optJSONArray("result");
            if (mResult != null) {
                if (mResult.length() != 0) {
                    mViewNull.setVisibility(View.GONE);
                    mAdd.setVisibility(View.VISIBLE);
                    mSwipeLayout.setVisibility(View.VISIBLE);
                    mAdapter = new ManageAddressAdapter(this, mResult,
                            mIsChoice);
                    mAddressList.setAdapter(mAdapter);
                    mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 800);
                } else {
                    mViewNull.setVisibility(View.VISIBLE);
                    mAdd.setVisibility(View.GONE);
                    mSwipeLayout.setVisibility(View.GONE);
                }
            } else {
                mViewNull.setVisibility(View.VISIBLE);
                mAdd.setVisibility(View.GONE);
                mSwipeLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoAddAddress() {
        String data = null;
        Intent intent = new Intent(this, EditAddressActivity.class);
        intent.putExtra("mIsNoAddress", false);
        intent.putExtra("mIsEditAddress", false);
        intent.putExtra("data", data);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(this);
    }

    private void gotoReviseAddress(String obj) {
        Intent intent = new Intent(this, EditAddressActivity.class);
        intent.putExtra("mIsNoAddress", false);
        intent.putExtra("mIsEditAddress", true);
        intent.putExtra("data", obj);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(this);
    }

    @Override
    public void onRefresh() {
        initGetAddressList();
    }

}
