package com.nettactic.mall.tab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heshicai.hao.xutils.BitmapUtils;
import com.heshicai.hao.xutils.bitmap.BitmapCommonUtils;
import com.heshicai.hao.xutils.bitmap.BitmapDisplayConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.MainActivity;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.SearchActivity;
import com.nettactic.mall.ad.AutoScrollViewPager;
import com.nettactic.mall.ad.CirclePageIndicator;
import com.nettactic.mall.ad.ImagePagerAdapter;
import com.nettactic.mall.adapter.HomeGridAdapter;
import com.nettactic.mall.adapter.HomeListAdapter;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.bean.ADBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.BitmapHelp;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StandardPokerDataUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;
import com.nettactic.mall.widget.LeftAndRightMarqueeView;
import com.nettactic.mall.widget.MyGridView;
import com.nettactic.mall.widget.PullToRefreshView;
import com.nettactic.mall.widget.UpAndDownMarqueeView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TabHomeFragment extends BaseFragment
        implements View.OnClickListener,
        PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {

    public final static int CATEGORY_FRAGMENT = 1;
    public final static int SHOPCART_FRAGMENT = 2;
    private String mPromoterUrl;
    private String mTime;
    private View mView;

    private View mHeader;
    private ListView mListView;
    private AutoScrollViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private ImageView mPromoterIV;
    private float screenensity;

    private JSONArray mAdItems;
    private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private int mCout = 0;
    private MyGridView mGridView;
    private LeftAndRightMarqueeView mLeftAndRightMarqueeView;
    private UpAndDownMarqueeView mUpAndDownMarqueeView;
    private MainActivity mainActivity;
    private PullToRefreshView mPullToRefreshView;
    private boolean mIsHeaderFlag = false;
    private boolean mIsFooterFlag = false;
    private RelativeLayout mSearchRl;
    private TextView mScanTv;
    private TextView mMessageTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab_home, null);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        initView(mView);
        if (!AndroidUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.show(MyApplication.getContext().getString(R.string.no_net));
            getSDinfo();
        } else {
            initData();
        }
//        setRollData(mUpAndDownMarqueeView);
//        setRollData2(mLeftAndRightMarqueeView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSDinfo();
//        setRollData(mUpAndDownMarqueeView);
//        setRollData2(mLeftAndRightMarqueeView);
        mViewPager.startAutoScroll();


    }

    @Override
    public void onPause() {
        super.onPause();
        mViewPager.stopAutoScroll();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.promoter_iv:
                if (!StringUtils.isEmpty(mPromoterUrl)) {
                    startOtherWeb(MyApplication.getContext(), "", mPromoterUrl);
                }
                break;

            case R.id.image_left:

                break;

            case R.id.search_rl:
                gotoSearchActivity();
                break;

            case R.id.image_right:
                break;

            default:
                break;
        }
    }

    /**
     * 到搜索页
     */
    private void gotoSearchActivity() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        getActivity().startActivity(intent);
        AndroidUtils.enterActvityAnim(getActivity());
    }

    private void initData() {
        initXtils();
        screenensity = AndroidUtils.getScreenensity(getActivity());
        LogUtils.logd(TAG, "screenensity:" + screenensity);
        if (!AndroidUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.show(R.string.no_net);
            getSDinfo();
        } else {
            mTime = getTime();
            initAdInfo();
            initCatList();
        }
        setGridAdapter();

    }

    /**
     * 获取时间梭
     *
     * @return
     */
    private String getTime() {
        String time = null;
        String timefilePath = FileUtil.getFilePath(MyConstants.TIME,
                MyConstants.UPDATE_TIME, MyConstants.TXT);
        boolean fileIsExists = FileUtil.fileIsExists(timefilePath);
        if (fileIsExists) {
            time = FileUtil.readFile(MyConstants.TIME, MyConstants.UPDATE_TIME,
                    MyConstants.TXT);
        } else {
            time = "0";
        }
        return time;
    }

    private void initAdInfo() {
        int screenWidth = AndroidUtils.getScreenWidth(getActivity());
        int screenHeight = AndroidUtils.getScreenHeight(getActivity());
        String app_type = MyConstants.ANDROID;

        NetHelper.getAdInfo(screenWidth + "", screenHeight + "", mTime,
                app_type, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        LogUtils.logd(TAG, "getAdInfo+onSuccess");

                        resolveGetAdInfo(arg2);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        LogUtils.logd(TAG, "getAdInfo+onFailure");
                    }
                });

    }

    private void resolveGetAdInfo(byte[] responseBody) {
        try {
            String json = new String(responseBody, "UTF-8");
            JSONObject JSONObject = new JSONObject(json);
            String code = JSONObject.optString("code");
            if ("0".equals(code)) {
                JSONObject result = JSONUtil.resolveResult(responseBody);
                FileUtil.saveFile(result.toString(), MyConstants.HOMEAD,
                        MyConstants.HOME_AD_INFO, MyConstants.TXT);
                setADinfo(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initCatList() {
        NetHelper.getCatList(mTime, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                LogUtils.logd(TAG, "getCatList+onSuccess");
                if (mIsHeaderFlag) {
                    mPullToRefreshView.onHeaderRefreshComplete();
                }
                if (mIsFooterFlag) {
                    mPullToRefreshView.onFooterRefreshComplete();
                }
                resolveGetCatList(arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                LogUtils.logd(TAG, "getCatList+onFailure");
            }
        });

    }

    private void resolveGetCatList(byte[] responseBody) {

        try {
            String json = new String(responseBody, "UTF-8");
            // LogUtils.logd(TAG, "mItemsjson:" + json);
            JSONObject JSONObject = new JSONObject(json);
            JSONArray mItems = JSONObject.getJSONArray("result");
            // JSONObject result = JSONObject.getJSONObject("result");
            // JSONArray mItems = result.getJSONArray("items");
            ListSetAdapter(mItems);
            FileUtil.saveFile(mItems.toString(), MyConstants.CATLIST,
                    MyConstants.CAT_LIST_INFO, MyConstants.TXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        mCout++;

        mListView = (ListView) view.findViewById(R.id.goods_listview);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_refresh_view);
        mSearchRl = (RelativeLayout) view.findViewById(R.id.search_rl);
        mScanTv = (TextView) view.findViewById(R.id.image_left);
        mMessageTv = (TextView) view.findViewById(R.id.image_right);
        if (mCout == 1) {
            mHeader = getActivity().getLayoutInflater().inflate(
                    R.layout.view_header_home, null);
            getActivity().getLayoutInflater();
            mGridView = (MyGridView) mHeader
                    .findViewById(R.id.home_gridview);

            mUpAndDownMarqueeView = (UpAndDownMarqueeView) mHeader
                    .findViewById(R.id.up_and_down_marqueeview);
            setRollData(mUpAndDownMarqueeView);

            mLeftAndRightMarqueeView = (LeftAndRightMarqueeView) mHeader
                    .findViewById(R.id.left_and_right_marqueeView);
            setRollData2(mLeftAndRightMarqueeView);

            mViewPager = (AutoScrollViewPager) mHeader
                    .findViewById(R.id.view_pager);
            ViewGroup.LayoutParams lp = mViewPager.getLayoutParams();
            lp.width = AndroidUtils.getScreenWidth(getActivity());
            lp.height = lp.width * 400 / 750;
            mViewPager.setLayoutParams(lp);

            mIndicator = (CirclePageIndicator) mHeader.findViewById(R.id.indicator);
            mPromoterIV = (ImageView) mHeader.findViewById(R.id.promoter_iv);
            mListView.addHeaderView(mHeader);
        }
        mPromoterIV.setOnClickListener(this);
        mSearchRl.setOnClickListener(this);
        mScanTv.setOnClickListener(this);
        mMessageTv.setOnClickListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

    }

    /**
     * 设置上下滚动数据
     *
     * @param myMarqueeview
     */
    private void setRollData(final UpAndDownMarqueeView myMarqueeview) {
        ArrayList<String> data = new ArrayList<>();
        data.add("两个黄鹂鸣垂柳，");
        data.add("一行白鹭上青天。");
        data.add("窗含西岭千秋雪，");
        data.add("门泊东吴万里船。");
        myMarqueeview.setMarqueeData(data);
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    public void run() {
                        myMarqueeview.toggleMarquee();
                    }
                });
            }
        }, 500);
    }

    /**
     * 设置上下滚动数据
     *
     * @param marqueeview
     */
    private void setRollData2(final LeftAndRightMarqueeView marqueeview) {
        marqueeview.setText("两个黄鹂鸣垂柳，一行白鹭上青天。窗含西岭千秋雪，门泊东吴万里船。");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    public void run() {
                        marqueeview.startScroll();
                    }
                });
            }
        }, 500);
    }

    private void setGridAdapter() {
        List<Integer> mDatas = StandardPokerDataUtils.getData(8 + "");
        HomeGridAdapter mAdapter = new HomeGridAdapter(getActivity(), getContext(), mDatas);
        mGridView.setAdapter(mAdapter);
    }

    /**
     * 从SD读取 缓存Json数据
     */
    private void getSDinfo() {
        String home_adPath = FileUtil.getFilePath(MyConstants.HOMEAD,
                MyConstants.HOME_AD_INFO, MyConstants.TXT);
        // LogUtils.logd(TAG, "home_adPath:" + home_adPath);
        if (FileUtil.fileIsExists(home_adPath)) {
            String ad_info = FileUtil.readFile(MyConstants.HOMEAD,
                    MyConstants.HOME_AD_INFO, MyConstants.TXT);
            try {
                JSONObject result = new JSONObject(ad_info);
                setADinfo(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String goods_listpath = FileUtil.getFilePath(MyConstants.CATLIST,
                MyConstants.CAT_LIST_INFO, MyConstants.TXT);
        if (FileUtil.fileIsExists(goods_listpath)) {
            String goods_list_info = FileUtil.readFile(MyConstants.CATLIST,
                    MyConstants.CAT_LIST_INFO, MyConstants.TXT);
            if (mIsHeaderFlag) {
                mPullToRefreshView.onHeaderRefreshComplete();
            }
            if (mIsFooterFlag) {
                mPullToRefreshView.onFooterRefreshComplete();
            }
            try {
                JSONArray mItems = new JSONArray(goods_list_info);
                ListSetAdapter(mItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ListView 添加数据
     *
     * @param items
     * @throws JSONException
     */
    private void AdListSetAdapter(JSONArray items) throws JSONException {
        setAdapter(ADBean.getDataToJson(items));
    }

    private void setAdapter(List<ADBean> imageIdList) {
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(
                getActivity(), MyApplication.getContext(), imageIdList);
        mViewPager.setAdapter(imagePagerAdapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setRadius(4 * screenensity);
        mIndicator.setOrientation(LinearLayout.HORIZONTAL);
        mIndicator.setStrokeWidth(2);
        mIndicator.setSnap(true);
        mViewPager.setInterval(5000);
        mViewPager
                .setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        // viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
        // viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        mViewPager.setCycle(true);
        mViewPager.setBorderAnimation(true);
    }

    /**
     * ListView 添加数据
     *
     * @param items
     * @throws JSONException
     */
    private void ListSetAdapter(JSONArray items) throws JSONException {

        HomeListAdapter mAdapter = new HomeListAdapter(getActivity(),
                MyApplication.getContext(), items);
        mListView.setAdapter(mAdapter);

    }

    /**
     * 初始化
     */
    private void initXtils() {
        if (bitmapUtils == null) {
            bitmapUtils = BitmapHelp.getBitmapUtils(MyApplication.getContext());
        }
        bigPicDisplayConfig = new BitmapDisplayConfig();
        // 图片太大时容易OOM。
        bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
        bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
                .getScreenSize(getActivity()));
        bitmapUtils.configDefaultLoadingImage(R.mipmap.wait_im);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.wait_im);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        // AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
        // 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);
        bitmapUtils.configDefaultImageLoadAnimation(animation);
    }

    /**
     * 给广告加载数据
     *
     * @param result
     * @throws JSONException
     */
    private void setADinfo(JSONObject result) throws JSONException {
        mAdItems = result.getJSONArray("carousel");

        AdListSetAdapter(mAdItems);

        JSONObject promoter = result.getJSONObject("promoter");
        String promoter_image_url = promoter.optString("image_url");
        LogUtils.logd(TAG, "promoter_image_url:" + promoter_image_url);
        mPromoterUrl = promoter.optString("url");
        if (!StringUtils.isEmpty(promoter_image_url)) {
            mPromoterIV.setVisibility(View.VISIBLE);
            bitmapUtils.display(mPromoterIV, promoter_image_url);
        } else {
            mPromoterIV.setVisibility(View.GONE);
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mIsHeaderFlag = true;
        if (!AndroidUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.show(MyApplication.getContext().getString(R.string.no_net));
            getSDinfo();
        } else {
            initData();
        }
        setRollData(mUpAndDownMarqueeView);
        setRollData2(mLeftAndRightMarqueeView);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mIsFooterFlag = true;
        if (!AndroidUtils.isNetworkAvailable(getActivity())) {
            ToastUtils.show(MyApplication.getContext().getString(R.string.no_net));
            getSDinfo();
        } else {
            initData();
        }

    }

}







