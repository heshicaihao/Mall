package com.nettactic.mall.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nettactic.mall.utils.LogUtils;

/**
 * 联网接口
 * 
 * @author heshicaihao
 * 
 */
public class NetHelper {

	private static final String TAG = "NetHelper";

	/**
	 * 账号新注册, post请求
	 * 
	 * @param handler
	 */
	public static void registerUser(String phonenum, String authcode,
			String pwcode, String promote_code, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		params.put("authcode", authcode);
		params.put("pwcode", pwcode);
		params.put("promote_code", promote_code);
		HttpClient.post(MyURL.REGISTER_URL, params, handler);
	}

	/**
	 * 手机注册新账号验证码确认 post请求
	 * 
	 * @param handler
	 */
	public static void registerSendCode(String phonenum,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		HttpClient.post(MyURL.REGISTER_SEND_CODE_URL, params, handler);
	}

	/**
	 * 账号登录 post请求
	 * 
	 * @param handler
	 */
	public static void loginUser(String account_id, String account_pw,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_pw", account_pw);
		HttpClient.post(MyURL.LOGIN_URL, params, handler);
	}

	/**
	 * 密码重置发送验证码确认 post请求
	 * 
	 * @param handler
	 */
	public static void forgotSendCode(String phonenum,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		HttpClient.post(MyURL.FORGOT_SEND_CODE_URL, params, handler);
	}

	/**
	 * 密码重置 post请求
	 * 
	 * @param handler
	 */
	public static void resetPassword(String phonenum, String authcode,
			String pwcode_new, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		params.put("authcode", authcode);
		params.put("pwcode_new", pwcode_new);
		HttpClient.post(MyURL.RESET_PASSWORD_URL, params, handler);
	}

	/**
	 * 微信支付，接口 post请求
	 * 
	 * @param handler
	 */
	public static void wechatPay(String order_id, String body, String price,
			String account_id, String account_token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
		params.put("body", body);
		params.put("price", price);
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		HttpClient.post(MyURL.WECHAT_PAY_URL, params, handler);
	}

	/**
	 * 第三方登录 post请求 type 第三方登录类型标识（微博、微信或者QQ） 新浪微博:sina,微信:wechat,QQ:qzone
	 * 
	 * @param handler
	 */
	public static void threePartyLogin(String openid, String type,
			String username, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("openid", openid);
		params.put("type", type);
		params.put("username", username);
		HttpClient.post(MyURL.THREE_PARTY_LOGIN_URL, params, handler);
	}

	/**
	 * 地址列表 post请求
	 * 
	 * @param handler
	 */
	public static void getAddressList(String account_id, String account_token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		HttpClient.post(MyURL.ADDRESS_LIST_URL, params, handler);
	}

	/**
	 * 获取地区关系信息JSON数据 post请求
	 * 
	 * @param handler
	 */
	public static void getAreaInfo(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		HttpClient.post(MyURL.AREA_INFO_URL, params, handler);
	}

	/**
	 * 添加新地址 post请求
	 * 
	 * @param handler
	 */
	public static void addAddress(String account_id, String account_token,
			String ship_name, String ship_area, String ship_addr,
			String ship_zip, String ship_tel, String ship_mobile,
			boolean is_default, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_name", ship_name);
		params.put("ship_area", ship_area);
		params.put("ship_addr", ship_addr);
		params.put("ship_zip", ship_zip);
		params.put("ship_tel", ship_tel);
		params.put("ship_mobile", ship_mobile);
		params.put("is_default", is_default);
		HttpClient.post(MyURL.ADD_ADDRESS_URL, params, handler);
	}

	/**
	 * 更新单项地址簿信息 post请求
	 * 
	 * @param handler
	 */
	public static void reviseAddress(String account_id, String account_token,
			String ship_id, String ship_name, String ship_area,
			String ship_addr, String ship_zip, String ship_tel,
			String ship_mobile, boolean is_default,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_id", ship_id);
		params.put("ship_name", ship_name);
		params.put("ship_area", ship_area);
		params.put("ship_addr", ship_addr);
		params.put("ship_zip", ship_zip);
		params.put("ship_tel", ship_tel);
		params.put("ship_mobile", ship_mobile);
		params.put("is_default", is_default);
		HttpClient.post(MyURL.ADD_ADDRESS_URL, params, handler);
	}

	/**
	 * 删除地址post请求
	 * 
	 * @param handler
	 */
	public static void deleteAddress(String account_id, String account_token,
			String[] ship_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_id", ship_id);
		HttpClient.post(MyURL.DELETE_ADDRESS, params, handler);
	}

	/**
	 * 设置默认地址信息 post请求
	 * 
	 * @param handler
	 */
	public static void setDefault(String account_id, String account_token,
			String ship_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_id", ship_id);
		HttpClient.post(MyURL.SET_DEFAULT_URL, params, handler);
	}

	/**
	 * 查询商品更新列表 post
	 * 
	 * @param handler
	 */
	public static void getAdInfo(String width, String height,
			String updatetime, String app_type, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("width", width);
		params.put("height", height);
		params.put("updatetime", updatetime);
		params.put("app_type", app_type);
		HttpClient.post(MyURL.AD_INFO_URL, params, handler);
	}

	/**
	 * 查询商品更新列表 post
	 * 
	 * @param handler
	 */
	public static void getCatList(String goods_updatetime,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("goods_updatetime", goods_updatetime);
		HttpClient.post(MyURL.CAT_LIST_URL, params, handler);
	}

	/**
	 * 商品列表接口 参数 post
	 * 
	 * @param handler
	 */
	public static void getNewGoodsList(String cat_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("cat_id", cat_id);
		HttpClient.post(MyURL.NEW_GOODS_LIST_URL, params, handler);
	}

	/**
	 * 获取临时账户申请临时作品id post请求
	 * 
	 * @param account_id
	 * @param token
	 * @param handler
	 */
	public static void getTempWorkId(String account_id, String token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("token", token);
		HttpClient.post(MyURL.TEMP_WORK_URL, params, handler);
	}

	/**
	 * 初始化商品信息请求 post
	 * 
	 * @param goods_id
	 *            商品Id
	 * @param handler
	 */
	public static void getGoodsInfo(String goods_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("goods_id", goods_id);
		HttpClient.post(MyURL.GOODS_URL, params, handler);
	}

	/**
	 * 初始化模板信息发起请求 post
	 * 
	 * @param template_id
	 *            模板Id
	 * @param handler
	 */
	public static void getTemplateInfo(String template_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("template_id", template_id);
		HttpClient.post(MyURL.TEMPLATE_URL, params, handler);
	}

	/**
	 * 上传作品信息 post请求
	 * 
	 * @param account_id
	 * @param account_token
	 * @param work_id
	 * @param template_id
	 * @param work_name
	 * @param file
	 * @param handler
	 */
	public static void saveWorks(String account_id, String account_token,
			String work_id, String template_id, String work_name, File file,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("work_id", work_id);
		params.put("template_id", template_id);
		params.put("work_name", work_name);
		try {
			params.put("content", file);
		} catch (FileNotFoundException e) {
			LogUtils.logd(TAG, "没有文件");
		}
		HttpClient.post(MyURL.SAVE_WORKS_URL, params, handler);
	}

	/**
	 * 获取配送方式列表 post请求
	 * 
	 * @param handler
	 */
	public static void getShippingList(String account_id, String account_token,
			String area_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("area_id", area_id);
		HttpClient.post(MyURL.SHIPPING_LIST_URL, params, handler);
	}

	/**
	 * 获取订单价钱信息 post请求
	 * 
	 * @param handler
	 */
	public static void getCheckCost(String account_id, String account_token,
			String shipping_id, String address_id, String objects,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("shipping_id", shipping_id);
		params.put("address_id", address_id);
		params.put("objects", objects);
		HttpClient.post(MyURL.CHECK_COST_URL, params, handler);
	}

	/**
	 * 提交新订单数据 post请求
	 * 
	 * @param handler
	 */
	public static void createOrders(String account_id, String account_token,
			String address_id, String shipping_id, String objects,
			String recommended_code, String memo,
			AsyncHttpResponseHandler handler) {

		RequestParams params = new RequestParams();

		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("address_id", address_id);
		params.put("shipping_id", shipping_id);
		params.put("objects", objects);
		params.put("recommended_code", recommended_code);
		params.put("memo", memo);

		HttpClient.post(MyURL.CREATE_ORDERS_URL, params, handler);
	}

	/**
	 * 获取账户全部订单列表信息 order_status, 订单支付状态：全部-all、待付款-unpaid、待收货-paid
	 * 
	 * @param handler
	 */
	public static void getOrderList(String account_id, String account_token,
			String order_status, int page_no, int page_size,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("order_status", order_status);
		params.put("page_no", page_no);
		params.put("page_size", page_size);

		HttpClient.post(MyURL.ORDER_GET_LIST_URL, params, handler);
	}

	/**
	 * 订单取消 post请求
	 * 
	 * @param handler
	 */
	public static void cancelOrder(String account_id, String account_token,
			String order_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("order_id", order_id);

		HttpClient.post(MyURL.CANCEL_ORDER_URL, params, handler);
	}

	/**
	 * 获取详细订单信息
	 * 
	 * @param handler
	 */
	public static void getOrderDetail(String account_id, String account_token,
			String order_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("order_id", order_id);
		HttpClient.post(MyURL.ORDER_DETAIL_URL, params, handler);
	}

	/**
	 * 检查版本更新状态 post请求 app_type：(String类型, 分别 "android", 或者是"ios")
	 * 
	 * @param handler
	 */
	public static void getVersionFromNet(String version, String app_type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("version", version);
		params.put("app_type", app_type);
		HttpClient.post(MyURL.GET_VERSION_URL, params, handler);
	}

	/**
	 * 获取详细订单信息
	 * 
	 * @param handler
	 */
	public static void getStatus(String account_id, String account_token,
			String work_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("work_id", work_id);
		HttpClient.post(MyURL.GET_STATUS_URL, params, handler);
	}

	/**
	 * 获取我的推广信息
	 * 
	 * @param handler
	 */
	public static void getPromoteInfo(String account_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		HttpClient.post(MyURL.GET_MY_PROMOTE_URL, params, handler);
	}

	/**
	 * 成为推广人（有推广码）
	 * 
	 * @param handler
	 */
	public static void setPromoter(String account_id, String promote_code,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("promote_code", promote_code);
		HttpClient.post(MyURL.SET_PROMOTER_URL, params, handler);
	}

	/**
	 * 成为推广人（无推广码）
	 * 
	 * @param handler
	 */
	public static void setPromoter(String account_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		HttpClient.post(MyURL.SET_PROMOTER_URL, params, handler);
	}

	/**
	 * 获取我要提现信息
	 * 
	 * @param handler
	 */
	public static void getWithdraw(String account_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		HttpClient.post(MyURL.GET_WITHDRAW_URL, params, handler);
	}

	/**
	 * 申请提现接口
	 * 
	 * @param handler
	 */
	public static void setWithdraw(String account_id, String amount,
			String name, String note, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("amount", amount);
		params.put("name", name);
		params.put("note", note);
		HttpClient.post(MyURL.SET_WITHDRAW_URL, params, handler);
	}

	/**
	 * 申请提现接口
	 * 
	 * @param handler
	 */
	public static void depositPay(String order_id, String account_id,
			String account_token, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		HttpClient.post(MyURL.DEPOSIT_PAY_URL, params, handler);
	}
	
	/**
	 * 获取推荐人的推荐码 接口
	 * 
	 * @param handler
	 */
//	public static void getUserCode(AsyncHttpResponseHandler handler) {
//		RequestParams params = new RequestParams();
//		HttpClient.post(MyURL.GETUSERCODE_URL, params, handler);
//	}
	
	
	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * @param version
	 * @param app_type (String类型, 分别 "android", 或者是"ios")
	 * @param callback
	 */
	public static void getVersionFromNet(String version, String app_type,
										 AsyncCallBack callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", version);//用户名
		params.put("app_type", app_type);
		AsyncHttp.postAsync(MyURL.GET_VERSION_URL, params, callback);

	}

	/**
	 * 获取地址信息
	 *
	 * @param callback
	 */
	public static void getAreaInfo(AsyncCallBack callback) {
		AsyncHttp.postAsync(MyURL.AREA_INFO_URL,callback);
	}

	/**
	 * 测试 post 有参数
	 *
	 * @param callback
	 */
	public static void login(String account_id, String account_pw, AsyncCallBack callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("account_id", account_id);//用户名
		params.put("account_pw", account_pw);
		AsyncHttp.postAsync(MyURL.GET_VERSION_URL, params, callback);
	}


	/**
	 * 微信支付，接口 post请求
	 * @param order_id
	 * @param body
	 * @param price
	 * @param account_id
	 * @param account_token
	 * @param callback
	 */
	public static void wechatPay(String order_id, String body, String price,
								 String account_id, String account_token,
								 AsyncCallBack callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_id", order_id);
		params.put("body", body);
		params.put("price", price);
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		AsyncHttp.postAsync(MyURL.WECHAT_PAY_URL, params, callback);

	}


}
