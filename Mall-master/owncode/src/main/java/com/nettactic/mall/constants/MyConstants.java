package com.nettactic.mall.constants;

public class MyConstants {

	// // 显示压缩图片防止内存溢出
	// public static final boolean ISPICCOMPRESS = true;
	// // 显示压缩图片防止内存溢出
	// public static final boolean ISMASKCOMPRESS = true;
	// App 是否运营
	public static final boolean ISOPERATE = true;
	// SD卡app名
	public static final String ANDROID = "android";
	// SD卡app文件夹名
	public static final String APP_DIR = "wc_mall/";
	// SD卡cache文件夹名
	public static final String CACHE_DIR = APP_DIR + ".cache";
	// SD卡zip文件 存放 文件夹名
	public static final String CACH_DIR = APP_DIR + ".cach";
	// SD卡work文件夹名
	public static final String WORK_DIR = APP_DIR + ".work";
	// SD卡area_data文件夹名
	public static final String AREA_DATA_DIR = APP_DIR + ".area_data";
	// SD卡work文件夹名
	public static final String DOWNLOAD = APP_DIR + ".download";
	// SD卡time文件夹名
	public static final String TIME = APP_DIR + ".time";
	// SD卡catList文件夹名
	public static final String CATLIST = APP_DIR + ".cat_list";
	// SD卡OrderList文件夹名
	public static final String ORDERLIST = APP_DIR + ".order_list";
	// SD卡首页广告文件夹名
	public static final String HOMEAD = APP_DIR + ".home_ad";
	// SD卡模板json数据文件夹名
	public static final String TEMPLATE = APP_DIR + ".template";
	// 本手机屏幕信息数据文件夹名
	public static final String SCREEN = APP_DIR + ".screen";
	// 推荐码信息文件夹名
	public static final String PCODE = APP_DIR + ".pcode";
	// 图片保存文件夹名
	public static final String PIC_DIR = APP_DIR + "image";

	// 后缀txt
	public static final String TXT = ".txt";
	// 后缀png
	public static final String PNG = ".png";
	// 后缀jpeg
	public static final String JPEG = ".jpeg";
	// 后缀json
	public static final String JSON = ".json";
	// 后缀zip
	public static final String ZIP = ".zip";
	
	// 推荐码信息
	public static final String PROMOTE_CODE = "promote_code";
	// 全国省市区数据
	public static final String AREA_DATA = "areaData";
	// 屏幕长宽信息文件名
	public static final String SCREEN_INFO = "screen_info";
	// 推荐码信息文件名
	public static final String PCODE_INFO = "pcode_info";

	// 时间梭文件名
	public static final String UPDATE_TIME = "update_time";
	// 商品分类列表信息文件名
	public static final String CAT_LIST_INFO = "cat_list_info";
	// 订单列表信息文件名
	public static final String ORDER_LIST_INFO = "order_list_info";
	// 首页广告文件名
	public static final String HOME_AD_INFO = "home_ad_info";
	// 作品id集
	public static final String WORKS_IDS = "mWorksIds";

	// 默认图片名字
	public static final String DEFAULT_PIC = "heshicaihao_default_pic";
	// 素材的名字
	public static final String MATERIAL = "e";

	// ACache的Key
	public static final String MINWIDTH = "minWidth";
	public static final String MINHEIGHT = "minHeight";
	public static final String MAXWIDTH = "maxWidth";
	public static final String MAXHEIGHT = "maxHeight";
	public static final String ORDERID = "orderIdStr";
	public static final String ORDERMONEY = "orderMoneyStr";
	public static final String EFFECTPICURL = "effectPicUrl";

	public static final String TOTALPAGE = "totalpage";
	public static final String MASKLAYOUTHEIGHT = "masklayoutheight";
	public static final String MASKWIDTH = "maskwidth";
	public static final String MASKHEIGHT = "maskheight";
	public static final String BACKUPMASKWIDTH = "backupmaskwidth";
	public static final String BACKUPMASKHEIGHT = "backupmaskheight";

	public static final String MYFRAMEMASKURL = "myframemaskurl";
	public static final String MYBACKUPFRAMEMASKURL = "mybackupframemaskurl";

	public static final String SELECT_IMGID = "select_imgid";
	public static final String SELECT_WORKPAGE = "select_workpage";
	public static final String SELECTED_ADDRESS = "selected_address";

	public static final String WORKID = "worksid";
	public static final String GOODSID = "goodsid";
	public static final String TEMPLATEID = "templateid";
	public static final String GOODSTYPE = "goodstype";
	public static final String GOODSPRICE = "goodsprice";
	public static final String GOODSURL = "goodsurl";
	public static final String GOODSNAME = "goodsname";


	// 每次缩放最小倍数
	public static final float EVERY_TIME_SCALE = (float) 0.1;
	// 缩放最大倍数
	public static final float MAX_SCALE = (float) 1.5;
	// 缩放最小倍数
	public static final float MIN_SCALE = (float) 1;
	// 编辑页标题高 dip
	public static final int EDITTITLE_WIDTH = 50;
	// 编辑页底部高 dip
	public static final int EDITBOTTOM_WIDTH = 70;
	// 编辑页底部高 dip
	public static final int SINGLE_GRID_WIDTH = 80;
	// 每次旋转最小角度
	public static final int EVERY_TIME_ROTATION = 90;
	// 可编辑用户图片最小宽
	public static final int INIT_MINWIDTH = 500;
	// 可编辑用户图片最小高
	public static final int INIT_MINHEIGHT = 500;
	// 可编辑用户图片最大宽
	public static final int INIT_MAXWIDTH = 500;
	// 可编辑用户图片最大高
	public static final int INIT_MAXHEIGHT = 500;

	// 阴影圈宽度
	public static final float BOREWIDTH = 36;
	// 阴影圈高度
	public static final float BOREHEIGHT = 57;
	
	// 深圳通阴影圈高度
	public static final float PI = (float)401.574/(float)673.227;
	// 深圳通阴影圈宽度
	public static final float SZTBOREWIDTH =(float)36*PI-(float)1.2;
	// 深圳通阴影圈高度
	public static final float SZTBOREHEIGHT =(float)57*PI-(float)1.7;

	// 可编辑用户图片最大高
	public static final double MB_MAX_CACHE = 6;

	// 可编辑用户图片最大高
	public static final int BROWSE_PIC_SIZE = 300;

	// 我的跳转到登录
	public static final int ME2LOGIN = 1;

	// 图片编辑跳转到登录
	public static final int EDITPICTURE2LOGIN = 2;

	// 注册跳转到登录
	public static final int REGISTER2LOGIN = 3;

	// H5跳转到登录
	public static final int H52LOGIN = 4;

	// ShareSDK的App Key
	public static final String SHARE_APP_KEY = "f4c7c1f82626";
	// ShareSDK的App Secret
	public static final String SHARE_APP_SECRET = "6d796e5dea21756629fa1e24d94a5e2f";

	// 友盟的App Key
	public static final String UM_APP_KEY = "581c38cff5ade471670023e0";

	public static final String TEMPIMG = "tempimg";
	// 微信分享的App ID(分享、支付)
	public static final String WX_APP_ID = "wxf4289f7a12e758c0";
	// 微信分享的App Secret(分享、支付)
	public static final String WX_APP_SECRET = "4321611803b3ca8b656a6e7aef13db1f";
	// QQ分享的App ID
	public static final String QQ_APP_ID = "1105807462";
	// QQ分享的App Key
	public static final String QQ_APP_KEY = "BJbUquqCHstZ5JUW";

	// 新浪分享的App ID
	public static final String SINA_APP_KEY = "44430004";
	// 新浪分享的App Secret
	public static final String SINA_APP_SECRET = "1276b71cd1882ac518a7e30c113fc507";
	// 支付宝 商户PID
	public static final String ALI_PARTNER = "2088001359945912";
	// 支付宝 商户收款账号
	public static final String ALI_SELLER = "393064198@qq.com";
	// 支付宝 商户私钥，pkcs8格式
	public static final String ALI_RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANIyQus+7IPB3/w8"
			+ "E3qdIzst+W+QJnW7LvD7YSbyZ6Ll0paaHGPzNAvWYHfkLNcXYKuHpP+owGOAYsgu"
			+ "ntGCwAuNkvXeRxRnw+n9qAbXyoaI49MSoJOCFYbhrusv0KoINkkSeCbIWlEEyjkI"
			+ "INmOme0oBc3DiU7YoqoM022nZNdtAgMBAAECgYAH14IxnpZaUAhYM7GSWHxw/8UX"
			+ "iWw15jbZHLfCKTb+l85keflnLTKa4nOU5XlayO99VsEsHJKY47hLuZmJPXSGqH/p"
			+ "Kyw2sM6ORWeoXdArVu4gpCDG0tZftFFszzUBR3ewSlvDiatV7bgJoWA2a8rgVccG"
			+ "4YQBXtEP7mhRSZw0LQJBAO4+DyoEKvKjLoxHvJ4CZUG7OmnvYwhaIn96S8EwQ5EV"
			+ "QhgdDXWnZiSl02yvyWXXIaXyMHvkzu1yl2yhXTU8cb8CQQDh3Qzpg/J1uvPHB/Gz"
			+ "tIjRvLEh/+5ysjnoAMf9q8MUFTiaO31E/vlscsiCOXyhqYxNr94jovDpkmpsPJEH"
			+ "hKnTAkAKNwr7aDZXNPv389N4iu3AhcCZbjQ21XaJNLsuy1z6SYkN86DyHH1asOmB"
			+ "zX+7KCA/N0Uw6rycnb80Xy7Z63+NAkEAxu8Gn2qLFGzMqEHz/h8F3w2+Sj78S2N1"
			+ "I0YE4AX39WlXiGjbRfj5wS48Mo7lmhIWtJoxItwyKte+CoUyDBgp5wJBAITceyhN"
			+ "XCFGBhzmJIYodZ2Qet/XRLkVliG48wp5bWth0MXMfzB7ud24JSF4QQ6QzDKhz5uK"
			+ "hgw4/p9Q9t0JeM8=";

}
