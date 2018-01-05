package com.nettactic.mall.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.adapter.SearchHistoryListAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.SearchHistoryInfoBean;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.database.SearchHistorySQLHelper;
import com.nettactic.mall.dialog.CustomDialog;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.AssetsUtils;
import com.nettactic.mall.utils.GUID;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.widget.LineBreakLayout;
import com.nettactic.mall.widget.MyListView;
import com.nettactic.mall.widget.SingleLineBreakLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private String mKey;
    private List<SearchHistoryInfoBean> mList;
    private UserBean mUser;

    private HorizontalScrollView mHScrollview;
    private SingleLineBreakLayout mSingleLineBreakLayout;
    private LineBreakLayout mLineBreakLayout;
    private MyListView mMyListView;
    private TextView mSearchBtn;
    private ImageView mBack;
    private EditText mEditText;
    private LinearLayout mCleanLl;
    private LinearLayout mBottomLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mUser = UserController.getInstance(this).getUserInfo();
        initView();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mEditText.setText("");
        setAdapter();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                this.finish();
                AndroidUtils.exitActvityAnim(this);
                break;

            case R.id.search_textview:
                dealSearchBtn();

                break;

            case R.id.clean_ll:
                showDialog(this);

                break;

            default:
                break;

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

    private void gotoSearchResult(String key) {
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra("keyword", key);
        SearchActivity.this.startActivity(intent);
        AndroidUtils.enterActvityAnim(this);

    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.back);
        mSearchBtn = (TextView) findViewById(R.id.search_textview);
        mEditText = (EditText) findViewById(R.id.search_edittext);
        mHScrollview = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        mSingleLineBreakLayout = (SingleLineBreakLayout) findViewById(R.id.single_hot_search_gridview);
        mLineBreakLayout = (LineBreakLayout) findViewById(R.id.hot_search_gridview);
        mMyListView = (MyListView) findViewById(R.id.search_history_listview);
        mCleanLl = (LinearLayout) findViewById(R.id.clean_ll);
        mBottomLl = (LinearLayout) findViewById(R.id.bottom_ll);

        mBack.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mCleanLl.setOnClickListener(this);
        mHScrollview.setHorizontalScrollBarEnabled(false);

    }

    private void initData() {
        setAdapter();
        String json = AssetsUtils.getJson(this, "hot_search.txt");
        resolveInfo(json);

    }

    private void setAdapter() {
        mList = SearchHistorySQLHelper.querytSearchHistory();
        if (mList.size() != 0) {
            mSingleLineBreakLayout.setVisibility(View.VISIBLE);
            mLineBreakLayout.setVisibility(View.GONE);
            mBottomLl.setVisibility(View.VISIBLE);

            SearchHistoryListAdapter mListAdapter = new SearchHistoryListAdapter(this, this, mList);
            mMyListView.setAdapter(mListAdapter);
        } else {
            mSingleLineBreakLayout.setVisibility(View.GONE);
            mLineBreakLayout.setVisibility(View.VISIBLE);
            mBottomLl.setVisibility(View.GONE);
        }

    }

    private void resolveInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                mKey = result.optString("hint_search");
                mEditText.setHint(mKey);
                JSONArray jsonarray = result.optJSONArray("hot_search_info");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = (JSONObject) jsonarray.get(i);
                    String name = jsonobject.optString("name");
                    list.add(name);
                }
                mLineBreakLayout.setLables(this, list, false);
                mSingleLineBreakLayout.setLables(this, list, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示对话框
     *
     * @param mContext
     */
    private void showDialog(Context mContext) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(mContext.getString(R.string.confirm_clean_search_history));
        builder.setPositiveButton(mContext.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (AndroidUtils.isNoFastClick()) {
                            dialog.dismiss();

                        }
                    }
                });

        builder.setNegativeButton(mContext.getString(R.string.ok),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (AndroidUtils.isNoFastClick()) {
                            dialog.dismiss();
                            SearchHistorySQLHelper.deleteSearchHistory();
                            setAdapter();
                        }
                    }
                });

        builder.create().show();
    }


    /**
     * 处理点击搜索按钮
     */
    private void dealSearchBtn() {
        mKey = mEditText.getText().toString().trim();
        String keyId = GUID.getGUID();
        String userid = mUser.getId();
        String lasttime = System.currentTimeMillis() + "";
        if (StringUtils.isEmpty(mKey)) {
            mKey = mEditText.getHint().toString().trim();
        } else {
            SearchHistoryInfoBean data = new SearchHistoryInfoBean(keyId, mKey, userid, lasttime);
            SearchHistorySQLHelper.addSearchHistory(data);
        }
        gotoSearchResult(mKey);
    }


}
