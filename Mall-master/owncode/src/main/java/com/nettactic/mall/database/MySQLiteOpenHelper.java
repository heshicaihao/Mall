package com.nettactic.mall.database;

import com.nettactic.mall.constants.DBConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (newVersion > oldVersion) {
			removeTables(db);
			createTables(db);
		}
	}

	/**
	 * 创建表
	 * 
	 * @param db
	 */
	private void createTables(SQLiteDatabase db) {
		createTableWorks(db);
		createTableSelectPic(db);
		createTableSearchHistory(db);
	}

	/**
	 * 移除表
	 * 
	 * @param db
	 */
	private void removeTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLENAME_WORKS);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLENAME_SELECTPIC);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLENAME_SEARCH_HISTORY);
	}

	/**
	 * 创建 搜索历史表
	 *
	 * @param db
	 */
	private void createTableSearchHistory(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DBConstants.TABLENAME_SEARCH_HISTORY + " ("
				+ DBConstants.TableColumn.SearchHistoryInfo.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.TableColumn.SearchHistoryInfo.KEYID
				+ " varchar(20),"
				+ DBConstants.TableColumn.SearchHistoryInfo.KEY
				+ " varchar(50),"
				+ DBConstants.TableColumn.SearchHistoryInfo.USERID
				+ " varchar(100),"
				+ DBConstants.TableColumn.SearchHistoryInfo.LASTTIME
				+ " varchar(50)" + ");");
	}

	/**
	 * 创建 作品表
	 * 
	 * @param db
	 */
	private void createTableWorks(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DBConstants.TABLENAME_WORKS + " ("
				+ DBConstants.TableColumn.WorksInfo.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.TableColumn.WorksInfo.WORKID + " varchar(20),"
				+ DBConstants.TableColumn.WorksInfo.JSONSTRING + " TEXT,"
				+ DBConstants.TableColumn.WorksInfo.GOODSNAME
				+ " varchar(100)," + DBConstants.TableColumn.WorksInfo.QUANTITY
				+ " varchar(20)," + DBConstants.TableColumn.WorksInfo.PRICE
				+ " varchar(20)," + DBConstants.TableColumn.WorksInfo.IMAGEURL
				+ " varchar(100)," + DBConstants.TableColumn.WorksInfo.GOODSID
				+ " varchar(20)," + DBConstants.TableColumn.WorksInfo.TYPE
				+ " varchar(100)," + DBConstants.TableColumn.WorksInfo.PICURL
				+ " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.PRODUCTID
				+ " varchar(100)," + DBConstants.TableColumn.WorksInfo.USERID
				+ " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.EDITSTATE
				+ " varchar(100)," + DBConstants.TableColumn.WorksInfo.LASTTIME
				+ " varchar(50)" + ");");
	}

	/**
	 * 创建 选图片表
	 * 
	 * @param db
	 */
	private void createTableSelectPic(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DBConstants.TABLENAME_SELECTPIC + " ("
				+ DBConstants.TableColumn.SelectInfo.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.TableColumn.SelectInfo.WORKID + " varchar(20),"
				+ DBConstants.TableColumn.SelectInfo.TOTALPAGE
				+ " varchar(20)," + DBConstants.TableColumn.SelectInfo.WORKPAGE
				+ " varchar(20)," + DBConstants.TableColumn.SelectInfo.OLDPATH
				+ " varchar(200)," + DBConstants.TableColumn.SelectInfo.IMGID
				+ " varchar(100)," + DBConstants.TableColumn.SelectInfo.NEWPATH
				+ " varchar(200),"
				+ DBConstants.TableColumn.SelectInfo.ISSELECT + " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.ISCONDENSE
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.ISSAVE
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.ISNET
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.ISUPLOAD
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.ISEXIST
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.ISCOMPUTE
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.TRANSLATEX
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.TRANSLATEY
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.INITSCALE
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.SCALE
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.MASKLAYOUTHEIGHT
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.MASKHEIGHT
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.MASKWIDTH
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.PICHEIGHT
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.PICWIDTH
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.CUTX
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.CUTY
				+ " varchar(50)," + DBConstants.TableColumn.SelectInfo.CUTWIDTH
				+ " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.CUTHEIGHT + " varchar(50),"
				+ DBConstants.TableColumn.SelectInfo.CARDINFO + " TEXT" + ");");

	}


}
