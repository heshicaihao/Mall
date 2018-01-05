package com.nettactic.mall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.activity.AboutMeActivity;
import com.nettactic.mall.activity.ApplyPromoterActivity;
import com.nettactic.mall.activity.LoginActivity;
import com.nettactic.mall.activity.ManageAddressActivity;
import com.nettactic.mall.activity.MeActivity;
import com.nettactic.mall.activity.OrderActivity;
import com.nettactic.mall.activity.PopularizeActivity;
import com.nettactic.mall.activity.RegisterActivity;
import com.nettactic.mall.activity.SettingActivity;
import com.nettactic.mall.activity.WorksActivity;
import com.nettactic.mall.base.BaseActivity;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.cache.JsonDao;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.fragment.MainFragment;
import com.nettactic.mall.frame.MenuDrawer;
import com.nettactic.mall.frame.Position;
import com.nettactic.mall.jpush.ExampleUtil;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.update.UpdateManager;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.NetworkStateService;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * WLMainActivity
 *
 * @author heshicaihao
 */
@SuppressWarnings("deprecation")
public class WLMainActivity extends BaseActivity implements OnClickListener {

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final String STATE_ACTIVE_POSITION = "com.wlzndjk.poker.WLMainActivity.activePosition";
    private static final String STATE_CONTENT_TEXT = "com.wlzndjk.poker.WLMainActivity.contentText";
    public static boolean isForeground = false;
    private static Boolean mIsExit = false;
    private boolean mIsHome = false;
    private int ACTIVE_POSITION = -1;
    private String mContentText;
    private ImageView mBack;
    private TextView mTitle;
    private ImageView mSave;
    private MenuDrawer mMenuDrawer;
    private View mLeftView;
    private LinearLayout mHomePageLL;
    private LinearLayout mMyOrderLL;
    private LinearLayout mMyWorksLL;
    private LinearLayout mManageAddressLL;
    private LinearLayout mSettingLL;
    private LinearLayout mAboutMeLL;
    private LinearLayout mMyPopularizeLL;
    private LinearLayout mUnloginLL;
    private LinearLayout mLoginLL;
    private Button mLoginBt;
    private Button mRegisterBt;
    private Button mApplyPromoter;
    private MainFragment main_fragment;
    private UserBean mUser;
    private String code04 = "10003";
    private ACache mCache;
    private TextView mAccountValue;
    private TextView mBalanceValue;
    private ImageView mMan_head;
    private MessageReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveState(savedInstanceState);
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT,
                Position.LEFT);
        mMenuDrawer.setContentView(R.layout.wl_activity_main);
        LayoutInflater inflater = LayoutInflater.from(this);
        mLeftView = inflater.inflate(R.layout.view_main_left_menu, null);
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        initView();
        initData();

        onClickUpdate();
    }

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
        AndroidUtils.getScreenInfo(this);
        initFragment();
        showUserView();
        refreshUI();
    }

    @Override
    public void onClick(View view) {
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        switch (view.getId()) {
            case R.id.home_page_ll:
                changeFragment(main_fragment, R.string.app_name, true);
                break;

            case R.id.my_order_ll:
                if (mUser.isIs_login()) {
                    startActivity(this, OrderActivity.class);
                } else {
                    hintLogin(this);
                }
                break;

            case R.id.my_works_ll:
                if (mUser.isIs_login()) {
                    startActivity(this, WorksActivity.class);
                } else {
                    hintLogin(this);
                }
                break;

            case R.id.manage_address_ll:
                if (mUser.isIs_login()) {
                    String data = JSONUtil.getData(code04, "2");
                    gotoManageAddress(data);
                } else {
                    hintLogin(this);
                }
                break;

            case R.id.setting_ll:
                startActivity(this, SettingActivity.class);
                break;

            case R.id.about_me_ll:
                startActivity(this, AboutMeActivity.class);
                break;

            case R.id.my_popularize_ll:
                if (mUser.isIs_login()) {
                    startActivity(this, PopularizeActivity.class);
                } else {
                    hintLogin(this);
                }
                break;

            case R.id.login:
                startActivity(this, LoginActivity.class);
                break;

            case R.id.register:
                startActivity(this, RegisterActivity.class);
                break;

            case R.id.save:
                startActivity(this, MeActivity.class);
                break;

            case R.id.apply_promoter:
                if (mUser.isIs_login()) {
                    startActivity(this, ApplyPromoterActivity.class);
                } else {
                    hintLogin(this);
                }
                break;

            case R.id.back:
                mMenuDrawer.openMenu();
                break;

            default:
                break;
        }
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, ACTIVE_POSITION);
        outState.putString(STATE_CONTENT_TEXT, mContentText);
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mSave = (ImageView) findViewById(R.id.save);
        mHomePageLL = (LinearLayout) mLeftView.findViewById(R.id.home_page_ll);
        mMyOrderLL = (LinearLayout) mLeftView.findViewById(R.id.my_order_ll);
        mMyWorksLL = (LinearLayout) mLeftView.findViewById(R.id.my_works_ll);
        mManageAddressLL = (LinearLayout) mLeftView
                .findViewById(R.id.manage_address_ll);
        mSettingLL = (LinearLayout) mLeftView.findViewById(R.id.setting_ll);
        mAboutMeLL = (LinearLayout) mLeftView.findViewById(R.id.about_me_ll);
        mMyPopularizeLL = (LinearLayout) mLeftView
                .findViewById(R.id.my_popularize_ll);
        mUnloginLL = (LinearLayout) mLeftView.findViewById(R.id.unlogin_ll);
        mLoginLL = (LinearLayout) mLeftView.findViewById(R.id.login_ll);
        mLoginBt = (Button) mLeftView.findViewById(R.id.login);
        mRegisterBt = (Button) mLeftView.findViewById(R.id.register);
        mAccountValue = (TextView) mLeftView.findViewById(R.id.account_value);
        mBalanceValue = (TextView) mLeftView.findViewById(R.id.balance_value);
        mApplyPromoter = (Button) mLeftView.findViewById(R.id.apply_promoter);
        mMan_head = (ImageView) mLeftView.findViewById(R.id.un_login_man_head);

        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mHomePageLL.setOnClickListener(this);
        mMyOrderLL.setOnClickListener(this);
        mMyWorksLL.setOnClickListener(this);
        mManageAddressLL.setOnClickListener(this);
        mSettingLL.setOnClickListener(this);
        mAboutMeLL.setOnClickListener(this);
        mMyPopularizeLL.setOnClickListener(this);
        mLoginBt.setOnClickListener(this);
        mRegisterBt.setOnClickListener(this);
        mApplyPromoter.setOnClickListener(this);

    }

    private void initData() {
        mCache = ACache.get(getApplication());
        AndroidUtils.getScreenInfo(this);
        mCache.put(MyConstants.SELECTED_ADDRESS, 0 + "");
        String area_dataPath = FileUtil.getFilePath(MyConstants.AREA_DATA_DIR,
                MyConstants.AREA_DATA, MyConstants.TXT);
        if (!FileUtil.fileIsExists(area_dataPath)) {
            getAreaData();
        }
        Intent i = new Intent(this, NetworkStateService.class);
        startService(i);
        mMenuDrawer.setMenuView(mLeftView);
        mMenuDrawer.setDropShadow(R.drawable.shadow_nav);
        int screenWidth = AndroidUtils.getScreenWidth(this);
        mMenuDrawer.setMenuSize(screenWidth * 2 / 3);
        initFragment();
        changeFragment(main_fragment, R.string.app_name, true);
        showUserView();
        JsonDao.getMaterial();
        refreshUI();
    }

    /**
     * 登录变化 更新UI
     */
    private void refreshUI() {
        changeHead();
        getMoney();
        initPopularizeinfo();
    }

    /**
     * 改变头像
     */
    private void changeHead() {
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        if (mUser.isIs_login()) {
            String head_portrait = mUser.getHead_portrait();
            if (!StringUtils.isEmpty(head_portrait)) {
                Glide.with(this).load(head_portrait)
                        .placeholder(R.mipmap.man_head).crossFade()
                        .into(mMan_head);
            }
        } else {
            mMan_head.setImageResource(R.mipmap.man_head);
        }

    }

    /**
     * 切换MainActivity 的fragment
     *
     * @param fragment
     * @param resid
     * @param isHome
     */
    private void changeFragment(Fragment fragment, int resid, boolean isHome) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_view, fragment).commit();
        mMenuDrawer.closeMenu();
        mTitle.setText(resid);
        mIsHome = isHome;

    }

    /**
     * 保存状态
     */
    private void saveState(Bundle inState) {
        if (inState != null) {
            ACTIVE_POSITION = inState.getInt(STATE_ACTIVE_POSITION);
            mContentText = inState.getString(STATE_CONTENT_TEXT);
        }
    }

    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (mIsExit == false) {
            final int drawerState = mMenuDrawer.getDrawerState();
            if (drawerState == MenuDrawer.STATE_OPEN
                    || drawerState == MenuDrawer.STATE_OPENING) {
                mMenuDrawer.closeMenu();
                return;
            }
            if (!mIsHome) {
                changeFragment(main_fragment, R.string.app_name, true);
                return;
            }
            mIsExit = true; // 准备退出
            ToastUtils.show(R.string.tip_double_click_exit);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        if (main_fragment == null) {
            main_fragment = new MainFragment();
        }

    }

    /**
     * 显示用户界面
     */
    private void showUserView() {
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        if (mUser.isIs_login()) {
            mUnloginLL.setVisibility(View.GONE);
            mLoginLL.setVisibility(View.VISIBLE);
            if (mUser.isIs_three_login()) {
                mAccountValue.setText(mUser.getUsername());
            } else {
                String mobile = mUser.getUname();
                String maskNumber = mobile.substring(0, 3) + "****"
                        + mobile.substring(7, mobile.length());
                mAccountValue.setText(maskNumber);
            }
            // balance_value.setText(mUser.getUsername());
        } else {
            mUnloginLL.setVisibility(View.VISIBLE);
            mLoginLL.setVisibility(View.GONE);
        }
    }

    private void gotoManageAddress(String data) {
        Intent intent = new Intent(this, ManageAddressActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(this);
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    private void setCostomMsg(String msg) {
        // if (null != msgText) {
        // msgText.setText(msg);
        // msgText.setVisibility(android.view.View.VISIBLE);
        // }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void onClickUpdate() {
        new UpdateManager(this, true);
    }

    /**
     * 获取余额
     */
    private void getMoney() {
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        if (mUser.isIs_login()) {
            mUser.getId();
            initWithdrawals(mUser.getId());
        }
    }

    /**
     * 初始化 获取提现金额
     *
     * @param account_id
     */
    private void initWithdrawals(String account_id) {
        NetHelper.getWithdraw(account_id, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                LogUtils.logd(TAG, "initWithdrawals+onSuccess");
                resolveInitWithdrawals(arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                LogUtils.logd(TAG, "initWithdrawals+onFailure");
            }
        });
    }

    /**
     * 解析 初始化 获取提现金额
     *
     * @param arg2
     */
    protected void resolveInitWithdrawals(byte[] arg2) {
        try {
            String json = new String(arg2, "UTF-8");
            LogUtils.logd(TAG, "json:" + json);
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            String msg = obj.optString("msg");
            JSONObject result = obj.optJSONObject("result");
            if ("0".equals(code)) {
                if (result != null) {
                    String balance = result.optString("balance");
                    String balanceformat2point = StringUtils
                            .format2point(balance);
                    String balanceStr = balanceformat2point;
                    mBalanceValue.setText(balanceStr);
                }
            } else {
                ToastUtils.shortShow(msg);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initPopularizeinfo() {
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        if (mUser.isIs_login()) {
            NetHelper.getPromoteInfo(mUser.getId(),
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              byte[] arg2) {
                            LogUtils.logd(TAG, "initPopularizeinfo+onSuccess");
                            LogUtils.logd(TAG, "mUser.getId()" + mUser.getId());
                            resolvePromoteInfo(arg2);

                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1,
                                              byte[] arg2, Throwable arg3) {
                            LogUtils.logd(TAG, "initPopularizeinfo+onFailure");

                        }
                    });
        } else {
            mApplyPromoter.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 解析 我的推广信息
     *
     * @param arg2
     */
    private void resolvePromoteInfo(byte[] arg2) {
        try {
            String json = new String(arg2, "UTF-8");
            LogUtils.logd(TAG, "json:" + json);
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                if (result != null) {
                    Boolean mIsPromoter = result.optBoolean("is_promoter",
                            false);
                    Boolean mIsApply = result.optBoolean("is_apply", false);
                    if (mUser.isIs_login()) {
                        if (mIsPromoter) {
                            mApplyPromoter.setVisibility(View.GONE);
                        } else {
                            if (mIsApply) {
                                mApplyPromoter.setVisibility(View.GONE);
                            } else {
                                mApplyPromoter.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        mApplyPromoter.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取地址信息
     */
    private void getAreaData() {
        NetHelper.getAreaInfo(new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                resolveAreaData(arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 解析地址信息
     *
     * @param arg2
     */
    private void resolveAreaData(byte[] arg2) {

        try {
            String json = new String(arg2, "UTF-8");
            LogUtils.logd(TAG, "resolveAreaDatajson:" + json);
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONArray areadata = obj.optJSONArray("result");
                FileUtil.saveFile(areadata.toString(),
                        MyConstants.AREA_DATA_DIR, MyConstants.AREA_DATA,
                        MyConstants.TXT);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

}
