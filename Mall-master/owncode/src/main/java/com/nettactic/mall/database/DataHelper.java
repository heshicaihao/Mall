package com.nettactic.mall.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nettactic.mall.bean.SearchHistoryInfoBean;
import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.bean.WorksInfoBean;
import com.nettactic.mall.constants.DBConstants;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite本地数据库操作（增删改查） 工具类
 */
public class DataHelper {
    private static String TAG = "DataHelper";
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;

    public DataHelper(Context context) {
        // 定义一个SQLite数据库
        dbHelper = new MySQLiteOpenHelper(context, DBConstants.DATABASENAME,
                null, DBConstants.DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    /**
     * 插入选择图片
     *
     * @param data
     */
    public void addSelectPic(SelectpicBean data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            LogUtils.logd(TAG, "addSelectPic");
            insertSelectPic(sqlite, cursor, data);
            LogUtils.logd(TAG, "addSelectPic后");
        } catch (Exception e) {
            LogUtils.logd(TAG, "Exception+addSelectPic" + e);
        } finally {
            if (sqlite != null) {
                sqlite.close();
                sqlite = null;
            }
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 插入 SQL数据
     *
     * @param data
     */
    private void insertSelectPic(SQLiteDatabase sqlite, Cursor cursor,
                                 SelectpicBean data) {
        LogUtils.logd(TAG, "rawQuery" + data.getImgid());

        cursor = sqlite.rawQuery("select * from selectpic where imgid=?",
                new String[]{data.getImgid()});
        LogUtils.logd(TAG, "cursor或");
        if (cursor != null && cursor.moveToFirst()) {
            String Sql = "update selectpic set workid=?,totalpage=?,oldpath=?,newpath=? where imgid=?";
            // 存在 则修改
            sqlite.execSQL(
                    Sql,
                    new String[]{data.getWorkid(), data.getTotalpage(),
                            data.getOldpath(), data.getNewpath()});
        } else {
            String sql = "insert into " + DBConstants.TABLENAME_SELECTPIC;
            sql += "(imgid, workid, totalpage, oldpath, newpath, workpage, isselect, iscondense, issave, isnet,isupload, isexist,"
                    + " iscompute, translatex, translatey, initscale, scale, masklayoutheight, maskheight, maskwidth, picheight, picwidth,cutx, cuty,cutwidth, cutheight, cardinfo ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            sqlite.execSQL(
                    sql,
                    new String[]{data.getImgid(), data.getWorkid(),
                            data.getTotalpage(), data.getOldpath(),
                            data.getNewpath(), data.getWorkpage(),
                            data.getIsselect(), data.getIscondense(),
                            data.getIssave(), data.getIsnet(),
                            data.getIsupload(), data.getIsexist(),
                            data.getIscompute(), data.getTranslatex(),
                            data.getTranslatey(), data.getInitscale(),
                            data.getScale(), data.getMasklayoutheight(),
                            data.getMaskheight(), data.getMaskwidth(),
                            data.getPicheight(), data.getPicwidth(),
                            data.getCutx(), data.getCuty(), data.getCutwidth(),
                            data.getCutheight(), data.getCardinfo()});
            LogUtils.logd(TAG, "execSQL" + data.getImgid());
        }
    }

    /**
     * 设置 数据
     *
     * @param cursor
     */
    private SelectpicBean setData2Selectpic(Cursor cursor) {
        SelectpicBean data = new SelectpicBean();
        data.setImgid(cursor.getString(cursor.getColumnIndex("imgid")));
        data.setWorkid(cursor.getString(cursor.getColumnIndex("workid")));
        data.setTotalpage(cursor.getString(cursor.getColumnIndex("totalpage")));
        data.setOldpath(cursor.getString(cursor.getColumnIndex("oldpath")));
        data.setNewpath(cursor.getString(cursor.getColumnIndex("newpath")));
        data.setWorkpage(cursor.getString(cursor.getColumnIndex("workpage")));
        data.setIsselect(cursor.getString(cursor.getColumnIndex("isselect")));
        data.setIscondense(cursor.getString(cursor.getColumnIndex("iscondense")));
        data.setIssave(cursor.getString(cursor.getColumnIndex("issave")));
        data.setIsnet(cursor.getString(cursor.getColumnIndex("isnet")));
        data.setIsupload(cursor.getString(cursor.getColumnIndex("isupload")));
        data.setIsexist(cursor.getString(cursor.getColumnIndex("isexist")));
        data.setIscompute(cursor.getString(cursor.getColumnIndex("iscompute")));
        data.setTranslatex(cursor.getString(cursor.getColumnIndex("translatex")));
        data.setTranslatey(cursor.getString(cursor.getColumnIndex("translatey")));
        data.setInitscale(cursor.getString(cursor.getColumnIndex("initscale")));
        data.setScale(cursor.getString(cursor.getColumnIndex("scale")));
        data.setMasklayoutheight(cursor.getString(cursor
                .getColumnIndex("masklayoutheight")));
        data.setMaskheight(cursor.getString(cursor.getColumnIndex("maskheight")));
        data.setMaskwidth(cursor.getString(cursor.getColumnIndex("maskwidth")));
        data.setPicheight(cursor.getString(cursor.getColumnIndex("picheight")));
        data.setPicwidth(cursor.getString(cursor.getColumnIndex("picwidth")));
        data.setCutx(cursor.getString(cursor.getColumnIndex("cutx")));
        data.setCuty(cursor.getString(cursor.getColumnIndex("cuty")));
        data.setCutwidth(cursor.getString(cursor.getColumnIndex("cutwidth")));
        data.setCutheight(cursor.getString(cursor.getColumnIndex("cutheight")));
        data.setCardinfo(cursor.getString(cursor.getColumnIndex("cardinfo")));

        return data;
    }

    /**
     * 根据原来路径 和 workid查询 选中图片
     *
     * @param imgid 原来路径
     * @return
     */
    public SelectpicBean queryPicByImgId(String imgid) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();

        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where imgid=? ";
        Cursor cursor = sqlite.rawQuery(sqlStr, new String[]{imgid});
        SelectpicBean data = new SelectpicBean();
        if (cursor != null && cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                data = setData2Selectpic(cursor);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return data;
    }

    /**
     * 根据原来workid和 选中状态 查询 作品选中图片信息
     *
     * @param isselect 是否选中
     * @param workid   作品Id
     * @param workpage 第几页
     * @return
     */
    public List<SelectpicBean> queryPicByIsselect(String isselect,
                                                  String workid, String workpage) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        List<SelectpicBean> lists = new ArrayList<SelectpicBean>();
        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where isselect=?  and workid=?  and workpage=?";
        Cursor cursor = sqlite.rawQuery(sqlStr, new String[]{isselect,
                workid, workpage});
        if (cursor != null && cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                SelectpicBean data = setData2Selectpic(cursor);
                lists.add(data);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }

    /**
     * 根据原来workid和 选中状态 查询 作品选中图片信息
     *
     * @param isselect 是否选中
     * @param workid   作品Id
     * @return
     */
    public List<SelectpicBean> queryPicByIsselect(String isselect, String workid) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        List<SelectpicBean> lists = new ArrayList<SelectpicBean>();
        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where isselect=?  and workid=?";
        Cursor cursor = sqlite.rawQuery(sqlStr,
                new String[]{isselect, workid});
        if (cursor != null && cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                SelectpicBean data = setData2Selectpic(cursor);
                lists.add(data);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }

    /**
     * 根据 workpage，workid 查询 imgid
     *
     * @return
     */

    public List<SelectpicBean> queryByWorkpageAndWorkid(String workpage,
                                                        String workid) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        List<SelectpicBean> lists = new ArrayList<SelectpicBean>();

        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where workpage=?  and workid=?";
        Cursor cursor = sqlite.rawQuery(sqlStr,
                new String[]{workpage, workid});

        if (cursor != null && cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                SelectpicBean data = setData2Selectpic(cursor);
                lists.add(data);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }

    /**
     * 根据原来路径 和 workid查询 选中图片
     *
     * @param oldpath 原来路径
     * @param workid  作品Id
     * @return
     */
    public ArrayList<SelectpicBean> queryPicByOldPathAndWorkid(String oldpath,
                                                               String workid) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<SelectpicBean> lists = new ArrayList<SelectpicBean>();

        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where oldpath=?  and workid=?";
        Cursor cursor = sqlite.rawQuery(sqlStr,
                new String[]{oldpath, workid});

        if (cursor != null && cursor.moveToFirst()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                SelectpicBean data = setData2Selectpic(cursor);
                lists.add(data);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }

    /**
     * 根据imgid 更新选中图片 信息到数据库
     *
     * @param imgid
     * @param key
     * @param value
     */
    public void revisePic(String imgid, String key, String value) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(key, value);
        sqlite.update(DBConstants.TABLENAME_SELECTPIC, values, "imgid=?",
                new String[]{imgid});

        sqlite.close();
        this.Close();
    }

    /**
     * 根据imgid 更新选中图片 压缩 保存 SD是否存在 SD新路径 到数据库
     *
     * @param imgid
     * @param newpath
     */
    public void revisePicSava(String imgid, String newpath) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        if (!StringUtils.isEmpty(newpath)) {
            values.put("newpath", newpath);
            values.put("iscondense", "true");
            values.put("issave", "true");
            // values.put("isexist", "true");
        } else {
            values.put("newpath", "");
            values.put("iscondense", "false");
            values.put("issave", "false");
            // values.put("isexist", "false");
        }
        sqlite.update(DBConstants.TABLENAME_SELECTPIC, values, "imgid=?",
                new String[]{imgid});

        sqlite.close();
        this.Close();
    }

    /**
     * 保存 选中图片信息 和选中蒙版 信息
     *
     * @return
     */
    public void savaPicMaskInfo(String imgid, float PicW, float PicH, float MaskW,
                                float MaskH) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("picwidth", PicW + "");
        values.put("picheight", PicH + "");
        values.put("maskwidth", MaskW + "");
        values.put("maskheight", MaskH + "");
        sqlite.update(DBConstants.TABLENAME_SELECTPIC, values, "imgid=?",
                new String[]{imgid});

        sqlite.close();
        this.Close();
    }

    /**
     * 保存 计算结果
     *
     * @return
     */
    public void savaComputeInfo(String imgid, float initscale, float scale,
                                float translatex, float translatey, float cutx, float cuty,
                                float cutwidth, float cutheight) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("initscale", initscale + "");
        values.put("scale", scale + "");
        values.put("translatex", translatex + "");
        values.put("translatey", translatey + "");
        values.put("cutx", cutx + "");
        values.put("cuty", cuty + "");
        values.put("cutwidth", cutwidth + "");
        values.put("cutheight", cutheight + "");
        sqlite.update(DBConstants.TABLENAME_SELECTPIC, values, "imgid=?",
                new String[]{imgid});

        sqlite.close();
        this.Close();
    }

    /**
     * 根据imgid 更新workpage信息
     *
     * @return
     */
    public void updateSelectPic(String imgid, String workpage) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("workpage", workpage);
        if ("-1".equals(workpage)) {
            values.put("isselect", "false");
        } else {
            values.put("isselect", "true");
        }
        sqlite.update(DBConstants.TABLENAME_SELECTPIC, values, "imgid=?",
                new String[]{imgid});
        sqlite.close();
        this.Close();
    }

    /**
     * 根据imgid，workpage 更新cardinfo信息
     *
     * @return
     */
    public void updateSelectPic(String imgid, String workpage, String cardinfo) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("workpage", workpage);
        values.put("cardinfo", cardinfo);
        if ("-1".equals(workpage)) {
            values.put("isselect", "false");
        } else {
            values.put("isselect", "true");
        }
        sqlite.update(DBConstants.TABLENAME_SELECTPIC, values, "imgid=?",
                new String[]{imgid});
        sqlite.close();
        this.Close();
    }

    /**
     * 按 imgid 删除 数据库信息
     */
    public void deleteImgid(String imgid) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        sqlite.delete(DBConstants.TABLENAME_SELECTPIC, "imgid=?",
                new String[]{imgid});
        sqlite.close();
        this.Close();
    }

    /**
     * 按workid 全部删除 数据库图片信息
     */
    public int deleteSelectpicByWorkid(String workid) {
        int id = 0;
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            id = sqlite.delete(DBConstants.TABLENAME_SELECTPIC, "workid=?",
                    new String[]{workid});
        } catch (Exception e) {
            LogUtils.logd(TAG, "Exception" + e);
        } finally {
            if (sqlite != null) {
                sqlite.close();
                sqlite = null;
            }
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return id;
    }

    /**
     * 查询本作品中 选中的 没有上传成功的 Imgid
     *
     * @param workid
     * @param isselect
     * @param isupload
     * @return
     */
    public List<String> querySelectPicImgid(String workid, String isselect,
                                            String isupload) {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where workid=?  and isselect=?  and isupload=?";
        Cursor cursor = sqlite.rawQuery(sqlStr, new String[]{workid,
                isselect, isupload});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            SelectpicBean data = setData2Selectpic(cursor);
            String imgid = data.getImgid();
            list.add(imgid);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();

        return list;
    }

    /**
     * 全部图片 查询
     *
     * @return
     */
    // public List<SelectpicBean> querySelectPic() {
    // SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
    // ArrayList<SelectpicBean> lists = new ArrayList<SelectpicBean>();
    // Cursor cursor = sqlite.rawQuery("select * from "
    // + DBConstants.TABLENAME_SELECTPIC + " order by imgid desc",
    // null);
    // for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
    //
    // SelectpicBean data = setData2Selectpic(cursor);
    // lists.add(data);
    // }
    // if (cursor != null && !cursor.isClosed()) {
    // cursor.close();
    // }
    // sqlite.close();
    // this.Close();
    // return lists;
    // }

    // /////////////////////////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////

    /**
     * 最近作品插入 保留20条记录
     *
     * @param data
     */
    public void addWorks(WorksInfoBean data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            LogUtils.logd(TAG, "insertWorks");
            insertWorks(sqlite, cursor, data);
        } catch (Exception e) {
            LogUtils.logd(TAG, "Exception" + e);
        } finally {
            if (sqlite != null) {
                sqlite.close();
                sqlite = null;
            }
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void insertWorks(SQLiteDatabase sqlite, Cursor cursor,
                             WorksInfoBean data) {
        cursor = sqlite.rawQuery("select * from works where workid=?",
                new String[]{data.getWorkid()});
        if (cursor != null && cursor.moveToFirst()) {
            LogUtils.logd(TAG, "存在 则修改" + data.getQuantity());
            String Sql = "update works set jsonstring=?,imageurl=?,picurl=?,quantity=?,userid=?,editstate=?,lasttime=? where workid=?";
            // 存在 则修改
            sqlite.execSQL(
                    Sql,
                    new String[]{data.getJsonstring(), data.getImageurl(),
                            data.getPicurl(), data.getQuantity(),
                            data.getUserid(), data.getEditstate(),
                            System.currentTimeMillis() + "", data.getWorkid()});
        } else {
            String sql = "insert into " + DBConstants.TABLENAME_WORKS;
            sql += "(workid, jsonstring, goodsname, quantity, price, imageurl, goodsid, type, picurl, productid,userid, editstate, lasttime) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)";
            sqlite.execSQL(
                    sql,
                    new String[]{data.getWorkid(), data.getJsonstring(),
                            data.getGoodsname(), data.getQuantity(),
                            data.getPrice(), data.getImageurl(),
                            data.getGoodsid(), data.getType(),
                            data.getPicurl(), data.getProductid(),
                            data.getUserid(), data.getEditstate(),
                            System.currentTimeMillis() + ""});
            LogUtils.logd(TAG, "execSQL" + data.getWorkid());
        }
    }

    /**
     * 此用户 作品 查询多个
     *
     * @return
     */
    public ArrayList<WorksInfoBean> queryWorks(ArrayList<String> work_ids) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<WorksInfoBean> lists = new ArrayList<WorksInfoBean>();

        String ids = "";
        for (int i = 0; i < work_ids.size(); i++) {
            if (i == 0) {
                ids += work_ids.get(i);
            } else {
                ids += "," + work_ids.get(i);
            }
        }
        Cursor cursor = sqlite.rawQuery("select * from "
                + DBConstants.TABLENAME_WORKS + " where workid in (" + ids
                + ") order by workid desc", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            WorksInfoBean data = setData2Works(cursor);
            lists.add(data);
        }
        LogUtils.logd(TAG, "lists:" + lists.size());
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }

    private WorksInfoBean setData2Works(Cursor cursor) {
        WorksInfoBean data = new WorksInfoBean();
        data.setWorkid(cursor.getString(cursor.getColumnIndex("workid")));
        data.setJsonstring(cursor.getString(cursor.getColumnIndex("jsonstring")));
        data.setGoodsname(cursor.getString(cursor.getColumnIndex("goodsname")));
        data.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
        data.setPrice(cursor.getString(cursor.getColumnIndex("price")));
        data.setImageurl(cursor.getString(cursor.getColumnIndex("imageurl")));
        data.setGoodsid(cursor.getString(cursor.getColumnIndex("goodsid")));
        data.setType(cursor.getString(cursor.getColumnIndex("type")));
        data.setPicurl(cursor.getString(cursor.getColumnIndex("picurl")));
        data.setProductid(cursor.getString(cursor.getColumnIndex("productid")));
        data.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
        data.setEditstate(cursor.getString(cursor.getColumnIndex("editstate")));
        data.setLasttime(cursor.getString(cursor.getColumnIndex("lasttime")));
        return data;
    }

    /**
     * 修改多个作品 编辑状态
     *
     * @return
     */
    public void reviseEditState(ArrayList<String> work_ids) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        String ids = "";
        for (int i = 0; i < work_ids.size(); i++) {
            if (i == 0) {
                ids += work_ids.get(i);
            } else {
                ids += "," + work_ids.get(i);
            }
        }
        sqlite.execSQL("update " + DBConstants.TABLENAME_WORKS
                + " set editstate = 1 where workid in (" + ids + ") ");
        sqlite.close();
        this.Close();
    }

    /**
     * 根据user_id 查询 作品信息
     *
     * @param user_id
     */
    public ArrayList<WorksInfoBean> queryWorksByUserId(String user_id) {
        LogUtils.logd(TAG, "user_id:" + user_id);
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<WorksInfoBean> lists = new ArrayList<WorksInfoBean>();

        Cursor cursor = sqlite.rawQuery("select * from works where userid=?",
                new String[]{user_id});
        LogUtils.logd(TAG, "cursor += null" + cursor);
        if (cursor != null && cursor.moveToFirst()) {
            LogUtils.logd(TAG, "cursor != null");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                LogUtils.logd(TAG, "!cursor.isAfterLast()");
                WorksInfoBean data = setData2Works(cursor);
                lists.add(data);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }

    /**
     * 作品 删单条
     *
     * @param data
     */
    public int deleteWorks(WorksInfoBean data) {
        int id = 0;
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        try {
            id = sqlite.delete(DBConstants.TABLENAME_WORKS, "workid=?",
                    new String[]{data.getWorkid()});
        } catch (Exception e) {
            LogUtils.logd(TAG, "Exception" + e);
        } finally {
            if (sqlite != null) {
                sqlite.close();
                sqlite = null;
            }
        }

        return id;
    }

    /**
     * 作品 删单条
     *
     * @param work_id
     */
    public int deleteWorks(String work_id) {
        int id = 0;
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        try {
            id = sqlite.delete(DBConstants.TABLENAME_WORKS, "workid=?",
                    new String[]{work_id});
        } catch (Exception e) {
            LogUtils.logd(TAG, "Exception" + e);
        } finally {
            if (sqlite != null) {
                sqlite.close();
                sqlite = null;
            }
        }

        return id;
    }


    /**
     * 查询本作品中 选中的 没有上传成功的 Imgid
     *
     * @param workid
     * @param isselect
     * @param isexist
     * @return
     */
    public List<String> querySelectPicImgid2(String workid, String isselect,
                                             String isexist) {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        String sqlStr = "select * from " + DBConstants.TABLENAME_SELECTPIC
                + " where workid=?  and isselect=?  and isexist=?";
        Cursor cursor = sqlite.rawQuery(sqlStr, new String[]{workid,
                isselect, isexist});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            SelectpicBean data = setData2Selectpic(cursor);
            String imgid = data.getImgid();
            list.add(imgid);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();

        return list;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 查询记录 插入
     *
     * @param data
     */
    public void addSearchHistory(SearchHistoryInfoBean data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            LogUtils.logd(TAG, "insertSearchHistory");
            insertSearchHistory(sqlite, cursor, data);
        } catch (Exception e) {
            LogUtils.logd(TAG, "Exception" + e);
        } finally {
            if (sqlite != null) {
                sqlite.close();
                sqlite = null;
            }
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void insertSearchHistory(SQLiteDatabase sqlite, Cursor cursor,
                                     SearchHistoryInfoBean data) {
        cursor = sqlite.rawQuery("select * from searchhistory where key=?",
                new String[]{data.getKey()});
        if (cursor != null && cursor.moveToFirst()) {
            LogUtils.logd(TAG, "存在 则修改" + data.getKey());
            String Sql = "update searchhistory set keyid=?,userid=?,lasttime=? where key=?";
            // 存在 则修改
            sqlite.execSQL(
                    Sql,
                    new String[]{data.getKeyid(), data.getUserid(),
                            System.currentTimeMillis() + "", data.getKey()});
        } else {
            String sql = "insert into " + DBConstants.TABLENAME_SEARCH_HISTORY;
            sql += "(keyid, key, userid, lasttime) values(?, ?, ?, ?)";
            sqlite.execSQL(
                    sql,
                    new String[]{data.getKeyid(), data.getKey(),
                            data.getUserid(), System.currentTimeMillis() + ""});
            LogUtils.logd(TAG, "execSQL" + data.getKey());
        }
    }

    /**
     * 全部图片 查询
     *
     * @return
     */
    public List<SearchHistoryInfoBean> querytSearchHistory() {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        List<SearchHistoryInfoBean> lists = new ArrayList<SearchHistoryInfoBean>();
        Cursor cursor = sqlite.rawQuery("select * from "
                        + DBConstants.TABLENAME_SEARCH_HISTORY + " order by lasttime desc",
                null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            SearchHistoryInfoBean data = setData2SearchHistory(cursor);
            lists.add(data);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        this.Close();
        return lists;
    }


    /**
     * 全删 搜索记录
     *
     */
    public int deleteSearchHistory() {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        int id = sqlite.delete(DBConstants.TABLENAME_SEARCH_HISTORY, null, null);
        sqlite.close();
        this.Close();
        return id;
    }


    /**
     * 设置 数据
     *
     * @param cursor
     */
    private SearchHistoryInfoBean setData2SearchHistory(Cursor cursor) {
        SearchHistoryInfoBean data = new SearchHistoryInfoBean();
        data.setKeyid(cursor.getString(cursor.getColumnIndex("keyid")));
        data.setKey(cursor.getString(cursor.getColumnIndex("key")));
        data.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
        data.setLasttime(cursor.getString(cursor.getColumnIndex("lasttime")));

        return data;
    }

}
