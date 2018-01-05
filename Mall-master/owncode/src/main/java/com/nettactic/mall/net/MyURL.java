package com.nettactic.mall.net;

/**
 * URL
 * 
 * @author heshicaihao
 */
public class MyURL {

	// App分享 URL
	public static final String SHARE_APP_URL = ConfigURL.URL + "share/";

	// 作品分享 URL
	public static final String SHARE_WORK_URL = ConfigURL.URL + "share/";

	// 产品介绍 H5 URL
	public static final String APP_INTRODUCTION_URL = ConfigURL.URL
			+ "m.php?controller=site&action=app_article&id=7";
	
	// 用户协议 H5 URL
	public static final String TREATY_URL = ConfigURL.URL
			+ "m.php?controller=site&action=app_article&id=3";

	// 免责声明 H5 URL
	public static final String DISCLAIMER_URL = ConfigURL.URL
			+ "m.php?controller=site&action=app_article&id=2";

	// 联系我们 H5 URL
	public static final String CONTACT_US_URL = ConfigURL.URL
			+ "/m.php?controller=site&action=app_article&id=1";
	
	// 帮助指引H5 URL
	public static final String HELP_URL = ConfigURL.URL
			+ "/m.php?controller=site&action=app_help";

	// 反馈建议 H5 URL
	public static final String SUGGESTIONS_URL = ConfigURL.URL
			+ "/m.php?controller=site&action=app_feedback&id=";
	
	// 成为推广人宣传
	public static final String PROMOTE_PROPAGATE_URL = ConfigURL.URL
			+ "m.php?controller=site&action=app_promote";
	
	// logo URL
	public static final String LOGO_URL = ConfigURL.URL
			+ "upload/Elements/logo_diy_poker.png";
	
	// 安装包 服务器存储地址
	public static final String SERVER_APP_URL = ConfigURL.URL
			+ "upload/WLPoker.apk";
	// 素材
	public static final String MATERIAL_URL = ConfigURL.URL
			+ "upload/Elements/";

	// (01)账号新注册
	public static final String REGISTER_URL = ConfigURL.URL
			+ "api.php/member/create";

	// (02)手机注册新账号验证码确认
	public static final String REGISTER_SEND_CODE_URL = ConfigURL.URL
			+ "api.php/member/send_signup_vcode_sms";

	// (03)账号登录
	public static final String LOGIN_URL = ConfigURL.URL
			+ "api.php/member/post_login";

	// (04)密码重置, 验证码确认
	public static final String FORGOT_SEND_CODE_URL = ConfigURL.URL
			+ "api.php/member/send_forgot_vcode_sms";

	// (05)密码重置过程
	public static final String RESET_PASSWORD_URL = ConfigURL.URL
			+ "api.php/member/resetpassword";

	// (06)第三方登录接口
	public static final String THREE_PARTY_LOGIN_URL = ConfigURL.URL
			+ "api.php/member/openid_login";

	// (07)作品单张图片上传
	public static final String ADDPICTURE_URL = ConfigURL.URL
			+ "api.php/storage/save";

	// (08)更新单个作品照片上传
	public static final String UPDATEPIC_URL = ConfigURL.URL
			+ "api.php/storage/update";

	// (09)支付宝支付 回掉函数
	public static final String MALIPAY_CALLBACK_URL = ConfigURL.URL
			+ "notify.v.1.0.php/block/server_callback/_id/10";

	// (09)微信支付，接口
	public static final String WECHAT_PAY_URL = ConfigURL.URL
			+ "api.php/orders/sendWechatPay";

	// (10)地址列表
	public static final String ADDRESS_LIST_URL = ConfigURL.URL
			+ "api.php/address/get_list";

	// (11)获取地区关系信息
	public static final String AREA_INFO_URL = ConfigURL.URL
			+ "api.php/address/getAreaInfo";

	// (12)添加新地址 或者 更新地址
	public static final String ADD_ADDRESS_URL = ConfigURL.URL
			+ "api.php/address/save";

	// (13)广告列表接口
	public static final String AD_INFO_URL = ConfigURL.URL
			+ "api.php/services/getAdInfo";

	// (14)首页商品类获取接口
	public static final String CAT_LIST_URL = ConfigURL.URL
			+ "api.php/goods/get_cat_list";

	// (15)删除地址
	public static final String DELETE_ADDRESS = ConfigURL.URL
			+ "api.php/address/delete";

	// (16)设置默认地址信息
	public static final String SET_DEFAULT_URL = ConfigURL.URL
			+ "api.php/address/set_default";

	// (17)单个品牌商品列表接口
	public static final String NEW_GOODS_LIST_URL = ConfigURL.URL
			+ "api.php/goods/get_goods_list";

	// (18)获取作品id
	public static final String TEMP_WORK_URL = ConfigURL.URL
			+ "api.php/member/get_temp_work_id";

	// (19)商品信息
	public static final String GOODS_URL = ConfigURL.URL
			+ "api.php/goods/get_item";

	// (20)模板信息
	public static final String TEMPLATE_URL = ConfigURL.URL
			+ "api.php/template/get_template";

	// (21)保存作品信息
	public static final String SAVE_WORKS_URL = ConfigURL.URL
			+ "api.php/works/save";

	// (22)获取配送方式列表
	public static final String SHIPPING_LIST_URL = ConfigURL.URL
			+ "api.php/orders/get_dlytype";

	// (23)获取订单价钱信息
	public static final String CHECK_COST_URL = ConfigURL.URL
			+ "api.php/orders/check_cost";

	// (24)提交新订单数据
	public static final String CREATE_ORDERS_URL = ConfigURL.URL
			+ "api.php/orders/create";

	// (25)获取全部订单列表信息
	public static final String ORDER_GET_LIST_URL = ConfigURL.URL
			+ "api.php/orders/get_list";

	// (26)订单取消
	public static final String CANCEL_ORDER_URL = ConfigURL.URL
			+ "api.php/orders/cancel";

	// (27)获取详细订单信息
	public static final String ORDER_DETAIL_URL = ConfigURL.URL
			+ "api.php/orders/detail";

	// (28)检查版本更新状态
	public static final String GET_VERSION_URL = ConfigURL.URL
			+ "api.php/services/getversion";

	// (29)查询作品数据状态
	public static final String GET_STATUS_URL = ConfigURL.URL
			+ "api.php/works/get_status";

	// (30)获取我的推广信息
	public static final String GET_MY_PROMOTE_URL = ConfigURL.URL
			+ "api.php/member/get_my_promote";

	// (31)成为推广人（有推广码和无推广码）
	public static final String SET_PROMOTER_URL = ConfigURL.URL
			+ "api.php/member/set_promoter";

	// (32)获取我要提现信息
	public static final String GET_WITHDRAW_URL = ConfigURL.URL
			+ "api.php/member/get_withdraw";

	// (33)申请提现接口
	public static final String SET_WITHDRAW_URL = ConfigURL.URL
			+ "api.php/member/set_withdraw";

	// (34)余额支付，接口(混合支付第一步)
	public static final String DEPOSIT_PAY_URL = ConfigURL.URL
			+ "api.php/orders/deposit_pay";
	
	// (35)获取推荐人的推荐码
	public static final String GETUSERCODE_URL = ConfigURL.URL
			+ "api.php/share/getUserCode";

	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////

}
