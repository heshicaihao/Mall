package com.nettactic.mall.bean;

import com.nettactic.mall.base.BaseBean;

public class UserBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 用户输入信息
	private String uname;
	private String password;
	// 服务器返回id信息
	private String id;
	private String token;
	private boolean is_login = false;
	// 服务器返回临时id信息
	private String temp_id;
	private String temp_token;
	private boolean is_temp_login = false;
	// 三方登录信息
	private String open_id;
	private String type;
	private String username;
	private String head_portrait;
	private boolean is_three_login = false;
	// 订单信息是否空
	private boolean is_order_null = true;

	public UserBean() {
		super();
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isIs_login() {
		return is_login;
	}

	public void setIs_login(boolean is_login) {
		this.is_login = is_login;
	}

	public String getTemp_id() {
		return temp_id;
	}

	public void setTemp_id(String temp_id) {
		this.temp_id = temp_id;
	}

	public String getTemp_token() {
		return temp_token;
	}

	public void setTemp_token(String temp_token) {
		this.temp_token = temp_token;
	}

	public boolean isIs_temp_login() {
		return is_temp_login;
	}

	public void setIs_temp_login(boolean is_temp_login) {
		this.is_temp_login = is_temp_login;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHead_portrait() {
		return head_portrait;
	}

	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}

	public boolean isIs_three_login() {
		return is_three_login;
	}

	public void setIs_three_login(boolean is_three_login) {
		this.is_three_login = is_three_login;
	}

	public boolean isIs_order_null() {
		return is_order_null;
	}

	public void setIs_order_null(boolean is_order_null) {
		this.is_order_null = is_order_null;
	}

	@Override
	public String toString() {
		return "UserBean [uname=" + uname + ", password=" + password + ", id="
				+ id + ", token=" + token + ", is_login=" + is_login
				+ ", temp_id=" + temp_id + ", temp_token=" + temp_token
				+ ", is_temp_login=" + is_temp_login + ", open_id=" + open_id
				+ ", type=" + type + ", username=" + username
				+ ", head_portrait=" + head_portrait + ", is_three_login="
				+ is_three_login + ", is_order_null=" + is_order_null + "]";
	}

	public UserBean(String uname, String password, String id, String token,
			boolean is_login, String temp_id, String temp_token,
			boolean is_temp_login, String open_id, String type,
			String username, String head_portrait, boolean is_three_login,
			boolean is_order_null) {
		super();
		this.uname = uname;
		this.password = password;
		this.id = id;
		this.token = token;
		this.is_login = is_login;
		this.temp_id = temp_id;
		this.temp_token = temp_token;
		this.is_temp_login = is_temp_login;
		this.open_id = open_id;
		this.type = type;
		this.username = username;
		this.head_portrait = head_portrait;
		this.is_three_login = is_three_login;
		this.is_order_null = is_order_null;
	}

	

}
