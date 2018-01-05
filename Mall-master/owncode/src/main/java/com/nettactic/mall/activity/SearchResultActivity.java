package com.nettactic.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.adapter.GoodsListRecyclerViewAdapter;
import com.nettactic.mall.adapter.SectionAdapter;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.FirstEvent;
import com.nettactic.mall.bean.SearchHistoryInfoBean;
import com.nettactic.mall.database.SearchHistorySQLHelper;
import com.nettactic.mall.fragment.FilterFragment;
import com.nettactic.mall.frame.ColorDrawable;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.AssetsUtils;
import com.nettactic.mall.utils.GUID;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 搜索结果 商品列表页
 */
public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    private final int selectPos[] = new int[]{0, 0, 0, 0};
    private int secindex = FIRST;
    private int mGoodsType = 0;
    private ImageView mBack;
    private TextView mSearchTv;
    private ImageView mChangeIv;
    private ImageView mStickIv;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private TextView mFilterTv;
    private RelativeLayout mRightLayout;
    private TextView FirstTv;
    private TextView SecondTv;
    private TextView ThirdTv;
    private TextView FourthTv;
    private FilterFragment mFilterFragment;
    private GoodsListRecyclerViewAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private SectionAdapter mPopAdapter;
    private ListView view_list;
    private JSONArray mMainMenuData;
    private  JSONArray mAllMenuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
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

            case R.id.search_edittext:
                this.finish();
                AndroidUtils.exitActvityAnim(this);
                break;

            case R.id.change_iv:
                changeType();
                break;

            case R.id.stick_iv:
                mRecyclerView.scrollToPosition(0);
                break;

            case R.id.filter_tv:
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;

            case R.id.first_tv:
                secindex = FIRST;
                showSectionPop(secindex);
                break;

            case R.id.second_tv:
                secindex = SECOND;
                showSectionPop(secindex);
                break;

            case R.id.third_tv:
                secindex = THIRD;
                showSectionPop(secindex);
                break;

            case R.id.fourth_tv:
                secindex = FOURTH;
                showSectionPop(secindex);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.back);
        mSearchTv = (TextView) findViewById(R.id.search_tv);
        mChangeIv = (ImageView) findViewById(R.id.change_iv);
        mStickIv = (ImageView) findViewById(R.id.stick_iv);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mRightLayout = (RelativeLayout) findViewById(R.id.right_rlayout);

        mFilterTv = (TextView) findViewById(R.id.filter_tv);

        FirstTv = (TextView) findViewById(R.id.first_tv);
        SecondTv = (TextView) findViewById(R.id.second_tv);
        ThirdTv = (TextView) findViewById(R.id.third_tv);
        FourthTv = (TextView) findViewById(R.id.fourth_tv);

        mBack.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
        mChangeIv.setOnClickListener(this);
        mStickIv.setOnClickListener(this);

        mFilterTv.setOnClickListener(this);

        FirstTv.setOnClickListener(this);
        SecondTv.setOnClickListener(this);
        ThirdTv.setOnClickListener(this);
        FourthTv.setOnClickListener(this);

    }

    private void initData() {
        EventBus.getDefault().register(this);
        setRightLayout(mRightLayout, 0.8f);

        String json = AssetsUtils.getJson(this, "filter_info.txt");
        resolveInfo(json);
        setListRecycler();

    }

    private void resolveInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                JSONObject main_menu = result.optJSONObject("main_menu");
                mMainMenuData = main_menu.optJSONArray("menu_data");
                for (int i = 0; i < 4; i++) {
                    setInitText(mMainMenuData, i, 0);
                }
                JSONObject all_menu = result.optJSONObject("all_menu");
                mAllMenuData = all_menu.optJSONArray("menu_data");
                addFragment(mAllMenuData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
    }

    /**
     * 添加Fragment()
     */
    private void addFragment(JSONArray data) {
        mFilterFragment = new FilterFragment();
        mFilterFragment.setData(data);
        this.getFragmentManager().beginTransaction().replace(R.id.right_rlayout, mFilterFragment).commit();
    }

    /**
     * 设置RecyclerView
     */
    private void setListRecycler() {
        mAdapter = new GoodsListRecyclerViewAdapter(this,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        //设置滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见位置
                    int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
                    //当滑动到第十个以上时显示置顶图标
                    if (firstVisibleItemPosition > 10) {
                        mStickIv.setVisibility(View.VISIBLE);
                    } else {
                        mStickIv.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 设置 RightLayout 宽
     *
     * @param view
     * @param sise
     */
    private void setRightLayout(View view, float sise) {
        int mScreenWidth = AndroidUtils.getMyScreenWidth();
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (mScreenWidth * sise);
        view.setLayoutParams(params);
    }

    /**
     * 切换 RecyclerView 样式
     */
    private void changeType() {
        if (mGoodsType == 0) {
            mChangeIv.setImageResource(R.mipmap.good_type_grid);
            //1：设置布局类型
            mAdapter.setType(1);
            //2：设置对应的布局管理器
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            //3：刷新adapter
            mAdapter.notifyDataSetChanged();
            mGoodsType = 1;
        } else {
            mChangeIv.setImageResource(R.mipmap.good_type_linear);
            mAdapter.setType(0);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.notifyDataSetChanged();
            mGoodsType = 0;
        }
    }

    protected void selectSecCheck(int index) {
        FirstTv.setTextColor(0xff404040);
        SecondTv.setTextColor(0xff404040);
        ThirdTv.setTextColor(0xff404040);
        FourthTv.setTextColor(0xff404040);

        switch (index) {
            case FIRST:
                FirstTv.setTextColor(this.getResources().getColor(
                        R.color.main_color));
                break;
            case SECOND:
                SecondTv.setTextColor(this.getResources().getColor(
                        R.color.main_color));
                break;
            case THIRD:
                ThirdTv.setTextColor(this.getResources().getColor(
                        R.color.main_color));
                break;
            case FOURTH:
                FourthTv.setTextColor(this.getResources().getColor(
                        R.color.main_color));
                break;
            default:
                FirstTv.setTextColor(this.getResources().getColor(
                        R.color.main_color));
                break;

        }
    }

    private void showSectionPop(final int secindex) {
        selectSecCheck(secindex);

        View layout = LayoutInflater.from(this).inflate(
                R.layout.view_popup_category, null);
        view_list = (ListView) layout.findViewById(R.id.view_list);
        RelativeLayout blankarea = (RelativeLayout) layout
                .findViewById(R.id.blankarea);
        blankarea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
            }
        });
        view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectPos[secindex] = position;
                mPopAdapter.notifyDataSetChanged();
                setPopAdapter(mMainMenuData, secindex, position);
                setInitText(mMainMenuData, secindex, position);
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(layout, 0,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWindowLayoutMode(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(R.color.white);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        mPopupWindow.setOutsideTouchable(true);
        setPopAdapter(mMainMenuData, secindex, selectPos[secindex]);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                selectSecCheck(secindex);
            }
        });
        mPopupWindow.showAsDropDown(FirstTv, 0, 0);
        mPopupWindow.update();
    }

    private void setInitText(JSONArray data, int secindex, int position) {
        try {
            JSONObject data_num = (JSONObject) data.get(secindex);
            JSONArray data_num_content = data_num.optJSONArray("content");
            JSONObject jsonobject = (JSONObject) data_num_content.get(position);
            String title = jsonobject.optString("title");
            TextView view = null;
            switch (secindex) {
                case FIRST:
                    view = FirstTv;
                    break;
                case SECOND:
                    view = SecondTv;
                    break;
                case THIRD:
                    view = ThirdTv;
                    break;
                case FOURTH:
                    view = FourthTv;
                    break;
                default:
                    view = FirstTv;
            }
            view.setText(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPopAdapter(JSONArray mMainMenuData, int secindex, int position) {
        try {
            JSONObject jsonobject = (JSONObject) mMainMenuData.get(secindex);
            JSONArray content = jsonobject.optJSONArray("content");
            mPopAdapter = new SectionAdapter(SearchResultActivity.this,
                    content, position);
            view_list.setAdapter(mPopAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
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



}
