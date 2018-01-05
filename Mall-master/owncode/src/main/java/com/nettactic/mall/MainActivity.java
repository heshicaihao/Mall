package com.nettactic.mall;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.tab.TabClassifyFragment;
import com.nettactic.mall.tab.TabHomeFragment;
import com.nettactic.mall.tab.TabMeFragment;
import com.nettactic.mall.tab.TabShoppingCartFragment;
import com.nettactic.mall.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    public static final String SELECT_INDEX = "selectIndex";
    public static final String CACHE_SELECT_INDEX = "cacheSelectIndex";
    public static final int INDEX_HOME = 0;
    public static final int INDEX_CLASSIFY = 1;
    public static final int INDEX_SHOPPINGCART = 2;
    public static final int INDEX_ME = 3;
    private static MainActivity mInstance;
    private static Boolean mIsExit = false;
    public int mCurrentSelectIndex;
    private FragmentManager mFragmentManager;
    private TabHomeFragment mTabHomeFragment = null;
    private TabClassifyFragment mTabClassifyFragment = null;
    private TabShoppingCartFragment mTabShoppingCartFragment = null;
    private TabMeFragment mTabMeFragment = null;
    private RadioGroup mRadioGroup;
    private RadioButton mRbtnHome;
    private RadioButton mRbtnClassify;
    private RadioButton mRbtnShoppingCart;
    private RadioButton mRbtnMe;
    private RadioButton mCurrRb;
    private RadioButton mLastRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {

        addFragment();
        hiddenFragment();
        if (savedInstanceState != null) {
            mCurrentSelectIndex = savedInstanceState.getInt(CACHE_SELECT_INDEX, INDEX_HOME);
        } else {
            mCurrentSelectIndex = INDEX_HOME;
        }
        setSelectIndex(mCurrentSelectIndex);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int selectIndex = -1;
        if (getIntent() != null && getIntent().hasExtra(SELECT_INDEX)) {
            selectIndex = getIntent().getIntExtra(SELECT_INDEX, -1);
            if (selectIndex != -1) setSelectIndex(selectIndex);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CACHE_SELECT_INDEX, mCurrentSelectIndex);
    }

    private void initView() {
        initFragment();
        mTabHomeFragment.setMainActivity(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRbtnHome = (RadioButton) findViewById(R.id.rbtn_home);
        mRbtnClassify = (RadioButton) findViewById(R.id.rbtn_category);
        mRbtnShoppingCart = (RadioButton) findViewById(R.id.rbtn_shopcart);
        mRbtnMe = (RadioButton) findViewById(R.id.rbtn_mine);

        mInstance = this;
        mRadioGroup.setOnCheckedChangeListener(this);


    }

    private void initFragment() {
        mFragmentManager = this.getSupportFragmentManager();
        if (mTabHomeFragment == null) {
            mTabHomeFragment = new TabHomeFragment();
        }
        if (mTabClassifyFragment == null) {
            mTabClassifyFragment = new TabClassifyFragment();
        }
        if (mTabShoppingCartFragment == null) {
            mTabShoppingCartFragment = new TabShoppingCartFragment();
        }
        if (mTabMeFragment == null) {
            mTabMeFragment = new TabMeFragment();
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

    //add by zzx
    public void setShowFragment(int flag) {
        if (flag == TabHomeFragment.CATEGORY_FRAGMENT) {
            showFragment(mTabClassifyFragment);
            changeTabtextSelector(mRbtnClassify);
            mRbtnClassify.setChecked(true);
        } else if (flag == TabHomeFragment.SHOPCART_FRAGMENT) {
            showFragment(mTabShoppingCartFragment);
            changeTabtextSelector(mRbtnShoppingCart);
            mRbtnShoppingCart.setChecked(true);
        }
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
        mTransaction.hide(mTabHomeFragment);
        mTransaction.hide(mTabClassifyFragment);
        mTransaction.hide(mTabShoppingCartFragment);
        mTransaction.hide(mTabMeFragment);
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
        mTransaction.add(R.id.fragmentView, mTabHomeFragment);
        mTransaction.add(R.id.fragmentView, mTabClassifyFragment);
        mTransaction.add(R.id.fragmentView, mTabShoppingCartFragment);
        mTransaction.add(R.id.fragmentView, mTabMeFragment);
        mTransaction.commitAllowingStateLoss();
    }

    public void changeTabtextSelector(RadioButton rb) {

        mLastRb = mCurrRb;
        mCurrRb = rb;

        if (mLastRb != null) {
            mLastRb.setTextColor(getResources().getColor(R.color.home_color_black));
            mLastRb.setSelected(false);
        }

        if (mCurrRb != null) {
            mCurrRb.setTextColor(getResources().getColor(R.color.main_color));
            mCurrRb.setChecked(true);
        }
    }

    public void setSelectIndex(int index) {
        switch (index) {
            case INDEX_HOME:
                showFragment(mTabHomeFragment);
                changeTabtextSelector(mRbtnHome);
                mCurrentSelectIndex = INDEX_HOME;
                break;
            case INDEX_CLASSIFY:
                showFragment(mTabClassifyFragment);
                changeTabtextSelector(mRbtnClassify);
                mCurrentSelectIndex = INDEX_CLASSIFY;
                break;
            case INDEX_SHOPPINGCART:
                showFragment(mTabShoppingCartFragment);
                changeTabtextSelector(mRbtnShoppingCart);
                mCurrentSelectIndex = INDEX_SHOPPINGCART;
                break;
            case INDEX_ME:
                showFragment(mTabMeFragment);
                changeTabtextSelector(mRbtnMe);
                mCurrentSelectIndex = INDEX_ME;
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int key) {

        switch (key) {
            case R.id.rbtn_home:
                setSelectIndex(INDEX_HOME);
                break;
            case R.id.rbtn_category:
                setSelectIndex(INDEX_CLASSIFY);
                break;
            case R.id.rbtn_shopcart:
                setSelectIndex(INDEX_SHOPPINGCART);
                break;
            case R.id.rbtn_mine:
                setSelectIndex(INDEX_ME);
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

    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (mCurrentSelectIndex != INDEX_HOME) {
            setSelectIndex(INDEX_HOME);
        }
        if (mIsExit == false) {
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

}
