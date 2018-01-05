package com.nettactic.mall.common;


import com.nettactic.mall.bean.UserBean;
import com.nettactic.mall.utils.SharedpreferncesUtil;

import android.content.Context;


/**
 * 封装用户 业务逻辑
 * 
 * @author heshuyuan
 * 
 */
public class UserController {

	private static UserController instance;
	private final Context mContext;

	private UserController(Context context) {
		mContext = context;
	}

	public static UserController getInstance(Context context) {
		if (instance == null) {
			instance = new UserController(context);
		}
		return instance;
	}

	/**
	 * 是否已经登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		if (getUserInfo() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 获取本地缓存的用户信息
	 * 
	 * @return null 表示无用户信息
	 */
	public UserBean getUserInfo() {
		return SharedpreferncesUtil.getUserInfo(mContext);
	}

	/**
	 * 缓存用户信息
	 * 
	 * @param user
	 */
	public void saveUserInfo(UserBean user) {
		SharedpreferncesUtil.saveUserInfo(mContext, user);
	}

	/**
	 * 登出
	 */
	public void loginOut() {
		SharedpreferncesUtil.clearByKey(SharedpreferncesUtil.KEY_USER_INFO,
				mContext);
	}

}
