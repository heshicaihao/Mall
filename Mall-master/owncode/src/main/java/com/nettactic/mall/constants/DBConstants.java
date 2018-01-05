package com.nettactic.mall.constants;

public class DBConstants {
	public static final String DATABASENAME = "mall.db";
	public static final int DATABASE_VERSION = 1;
	//作品表
	public static final String TABLENAME_WORKS = "works";
	// 选择图片表
	public static final String TABLENAME_SELECTPIC = "selectpic";
	//搜索历史表
	public static final String TABLENAME_SEARCH_HISTORY = "searchhistory";
	
	public static class TableColumn {
		
		public static class WorksInfo {
			public static final String ID = "id";
			public static final String WORKID = "workid";
			public static final String JSONSTRING = "jsonstring";
			public static final String GOODSNAME = "goodsname";
			public static final String QUANTITY = "quantity";
			public static final String PRICE = "price";
			public static final String IMAGEURL = "imageurl";
			public static final String GOODSID = "goodsid";
			public static final String TYPE = "type";
			public static final String PICURL = "picurl";
			public static final String PRODUCTID = "productid";
			public static final String USERID = "userid";
			public static final String EDITSTATE = "editstate";
			public static final String LASTTIME = "lasttime";

		}

		/**
		 * 选择图片表
		 * 
		 * @author heshicaihao
		 * 
		 */
		public static class SelectInfo {
			public static final String ID = "id";
			// 图片所在的作品id
			public static final String WORKID = "workid";
			// 图片 所在的作品的总页数
			public static final String TOTALPAGE = "totalpage";
			// 图片 所在的作品的第几页
			public static final String WORKPAGE = "workpage";
			// SD中图片原来的路径
			public static final String OLDPATH = "oldpath";
			// 图片的名字 （用GUI生成的压缩后保存到SD中的名字）
			public static final String IMGID = "imgid";
			// 压缩后保存到SD中的新路径
			public static final String NEWPATH = "newpath";
			// 图片是否被选中
			public static final String ISSELECT = "isselect";
			// 图片是否被压缩
			public static final String ISCONDENSE = "iscondense";
			// 图片是否保存到SD缓存中
			public static final String ISSAVE = "issave";
			// 图片是否上传到服务器成功
			public static final String ISNET = "isnet";
			// 图片是否正在上传到服务器
			public static final String ISUPLOAD = "isupload";
			// 图片压缩保存在SD中是否存在
			public static final String ISEXIST = "isexist";

			// 是否计算用户图片相对mask居中铺满
			public static final String ISCOMPUTE = "iscompute";
			// 计算用户图片相对mask居中铺满 的 x
			public static final String TRANSLATEX = "translatex";
			// 计算用户图片相对mask居中铺满 的 y
			public static final String TRANSLATEY = "translatey";
			// 计算用户图片相对mask居中铺满 初始缩放值
			public static final String INITSCALE = "initscale";
			// 用户图片相对mask 当前初始缩放值
			public static final String SCALE = "scale";
			// Mask 布局文件高 （像素）
			public static final String MASKLAYOUTHEIGHT = "masklayoutheight";
			// Mask 物理文件高 （像素）
			public static final String MASKHEIGHT = "maskheight";
			// Mask 物理文件宽 （像素）
			public static final String MASKWIDTH = "maskwidth";
			// 用户图片 物理文件高 （像素）
			public static final String PICHEIGHT = "picheight";
			// 用户图片 物理文件宽 （像素）
			public static final String PICWIDTH = "picwidth";
			// 计算用户图片相对mask居中铺满 的 x （服务器需要）
			public static final String CUTX = "cutx";
			// 是否计算用户图片相对mask居中铺满 的 y （服务器需要）
			public static final String CUTY = "cuty";
			// 用户图片 处理后 高（服务器需要）
			public static final String CUTWIDTH = "cutwidth";
			// 用户图片 处理后 宽 （服务器需要）
			public static final String CUTHEIGHT = "cutheight";
			// 用户图片 处理后 宽 （服务器需要）
			public static final String CARDINFO = "cardinfo";

		}

		public static class SearchHistoryInfo {
			public static final String ID = "id";
			public static final String KEYID = "keyid";
			public static final String KEY = "key";
			public static final String USERID = "userid";
			public static final String LASTTIME = "lasttime";

		}
	}

}
