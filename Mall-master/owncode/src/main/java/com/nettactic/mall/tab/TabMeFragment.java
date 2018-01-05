package com.nettactic.mall.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nettactic.mall.R;
import com.nettactic.mall.activity.AboutMeActivity;
import com.nettactic.mall.activity.ApplyPromoterActivity;
import com.nettactic.mall.activity.LoginActivity;
import com.nettactic.mall.activity.ManageAddressActivity;
import com.nettactic.mall.activity.OrderActivity;
import com.nettactic.mall.activity.PopularizeActivity;
import com.nettactic.mall.activity.RegisterActivity;
import com.nettactic.mall.activity.SettingActivity;
import com.nettactic.mall.activity.WithdrawalsActivity;
import com.nettactic.mall.activity.WorksActivity;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.common.UserController;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.net.NetHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.JSONUtil;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;
import com.nettactic.mall.utils.ToastUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class TabMeFragment extends BaseFragment  implements View.OnClickListener {

    private ImageView mBack;
    private TextView mTitle;
    private ImageView mSave;

    private LinearLayout mUnloginLL;
    private LinearLayout mLoginLL;
    private LinearLayout mMyOrderLL;
    private LinearLayout mMyWorksLL;
    private LinearLayout mManageAddressLL;
    private LinearLayout mSettingLL;
    private LinearLayout mAboutMeLL;
    private LinearLayout mMyPopularizeLL;

    private Button mLoginBt;
    private Button mRegisterBt;
    private Button mApplyPromoter;
    private TextView mName;
    private TextView mWithdrawals;
    private TextView mBalanceValue;

    private UserBean mUser;
    private String code04 = "10003";
    private ACache mCache;
    private ImageView mMan_head;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab_me, null);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        mUser = UserController.getInstance(getContext()).getUserInfo();
        initView(mView);
        initData();
        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
        showHead();
        refreshUI();
    }

    @Override
    public void onClick(View view) {
        mUser = UserController.getInstance(getActivity()).getUserInfo();
        switch (view.getId()) {

            case R.id.login:
                gotoOther(getActivity(), LoginActivity.class);
                break;

            case R.id.register:
                gotoOther(getActivity(), RegisterActivity.class);
                break;

            case R.id.withdrawals:
                gotoOther(getActivity(), WithdrawalsActivity.class);
                break;

            case R.id.my_order_ll:
                if (mUser.isIs_login()) {
                    gotoOther(getActivity(), OrderActivity.class, 2);
//                    finish();
                } else {
                    hintLogin(getActivity());
                }
                break;
            case R.id.my_works_ll:
                if (mUser.isIs_login()) {
                    gotoOther(getActivity(), WorksActivity.class);
//                    finish();
                } else {
                    hintLogin(getActivity());
                }
                break;
            case R.id.manage_address_ll:
                if (mUser.isIs_login()) {
                    String data = JSONUtil.getData(code04, "2");
                    gotoManageAddress(data);
//                    finish();
                } else {
                    hintLogin(getActivity());
                }
                break;
            case R.id.setting_ll:
                gotoOther(getActivity(), SettingActivity.class);
                break;

            case R.id.about_me_ll:
                gotoOther(getActivity(), AboutMeActivity.class);
                break;
            case R.id.my_popularize_ll:
                if (mUser.isIs_login()) {
                    gotoOther(getActivity(), PopularizeActivity.class);
//                    finish();
                } else {
                    hintLogin(getActivity());
                }
                break;

            case R.id.save:
//                AndroidUtils.finishActivity(this);
                break;

            case R.id.apply_promoter:
                if (mUser.isIs_login()) {
                    startActivity(getActivity(), ApplyPromoterActivity.class);
                    // finish();
                } else {
                    hintLogin(getActivity());
                }
                break;

            case R.id.back:
//                AndroidUtils.finishActivity(getActivity());

                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        mBack = (ImageView) view.findViewById(R.id.back);
        mTitle = (TextView) view.findViewById(R.id.title);
        mSave = (ImageView) view.findViewById(R.id.save);
        mName = (TextView) view.findViewById(R.id.account_value);
        mWithdrawals = (TextView) view.findViewById(R.id.withdrawals);
        mBalanceValue = (TextView) view.findViewById(R.id.balance_value);
        mUnloginLL = (LinearLayout) view.findViewById(R.id.unlogin_ll);
        mLoginLL = (LinearLayout) view.findViewById(R.id.login_ll);
        mMyOrderLL = (LinearLayout) view.findViewById(R.id.my_order_ll);
        mMyWorksLL = (LinearLayout) view.findViewById(R.id.my_works_ll);
        mManageAddressLL = (LinearLayout) view.findViewById(R.id.manage_address_ll);
        mSettingLL = (LinearLayout) view.findViewById(R.id.setting_ll);
        mAboutMeLL = (LinearLayout) view.findViewById(R.id.about_me_ll);
        mMyPopularizeLL = (LinearLayout) view.findViewById(R.id.my_popularize_ll);
        mApplyPromoter = (Button) view.findViewById(R.id.apply_promoter);
        mLoginBt = (Button) view.findViewById(R.id.login);
        mRegisterBt = (Button) view.findViewById(R.id.register);
        mMan_head = (ImageView) view.findViewById(R.id.un_login_man_head);

        mTitle.setText(R.string.me);
        mWithdrawals.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        showHead();
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mMyOrderLL.setOnClickListener(this);
        mMyWorksLL.setOnClickListener(this);
        mManageAddressLL.setOnClickListener(this);
        mSettingLL.setOnClickListener(this);

        mAboutMeLL.setOnClickListener(this);
        mMyPopularizeLL.setOnClickListener(this);
        mLoginBt.setOnClickListener(this);
        mRegisterBt.setOnClickListener(this);
        mWithdrawals.setOnClickListener(this);
        mApplyPromoter.setOnClickListener(this);
    }

    private void initData() {
        mCache = ACache.get(getContext());
        mCache.put(MyConstants.SELECTED_ADDRESS, 0 + "");
        refreshUI();
    }

    /**
     *
     * 登录变化 更新UI
     *
     */
    private void refreshUI() {
        changeHead();
        getMoney();
        initPopularizeinfo();
    }

    /**
     *
     * 改变头像
     */
    private void changeHead() {
        mUser = UserController.getInstance(MyApplication.getContext())
                .getUserInfo();
        if (mUser.isIs_login()) {
            String head_portrait = mUser.getHead_portrait();
            if (!StringUtils.isEmpty(head_portrait)) {
                Glide.with(this).load(head_portrait)
                        .placeholder(R.mipmap.man_head)
                        .crossFade().into(mMan_head);
            }
        }else {
            mMan_head.setImageResource(R.mipmap.man_head);
        }

    }

    /**
     * 跳转到其他界面
     */
    private void gotoOther(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(getActivity());

    }

    private void gotoManageAddress(String data) {
        Intent intent = new Intent(getActivity(), ManageAddressActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        AndroidUtils.enterActvityAnim(getActivity());
    }

    /**
     * 跳转到其他界面
     */
    private void gotoOther(Context context, Class<?> activity, int CurrentTabNum) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("CurrentTabNum", CurrentTabNum);
        startActivity(intent);
//        getActivity().finish();
        // AndroidUtils.finishActivity(getActivity());
        AndroidUtils.enterActvityAnim(getActivity());
    }

    /**
     * 显示用户头像
     */
    private void showHead() {
        mUser = UserController.getInstance(getActivity()).getUserInfo();
        if (mUser.isIs_login()) {
            mLoginLL.setVisibility(View.VISIBLE);
            mUnloginLL.setVisibility(View.GONE);

            if (mUser.isIs_three_login()) {
                mName.setText(mUser.getUsername());
            } else {
                String mobile = mUser.getUname();
                String maskNumber = mobile.substring(0, 3) + "****"
                        + mobile.substring(7, mobile.length());
                mName.setText(maskNumber);
            }
        } else {
            mLoginLL.setVisibility(View.GONE);
            mUnloginLL.setVisibility(View.VISIBLE);

        }
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
                    double balanceD = Double.parseDouble(balance);
                    if (balanceD == 0) {
                        mWithdrawals.setVisibility(View.GONE);
                    } else {
                        mWithdrawals.setVisibility(View.VISIBLE);
                    }
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
}

