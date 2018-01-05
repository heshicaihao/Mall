package com.nettactic.mall.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nettactic.mall.bean.SelectpicBean;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.constants.MyConstants;
import com.nettactic.mall.utils.FileUtil;
import com.nettactic.mall.utils.JSONUtil;

public class SelectPicSQLHelper {

	/**
	 * 将选中图片信息 加入保存到数据库
	 */
	public static void add2DB(SelectpicBean selectpic) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.addSelectPic(selectpic);
	}

	/**
	 * 根据原来workid和 选中状态 第几页 查询 作品选中图片信息
	 * 
	 * @param isselect
	 *            是否选中
	 * @param workid
	 *            作品Id
	 * @param workpage
	 *            第几页
	 * 
	 * @return
	 */
	public static SelectpicBean queryByIsselect(String isselect, String workid,
			String workpage) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		List<SelectpicBean> listbean = dh.queryPicByIsselect(isselect, workid,
				workpage);
		SelectpicBean selectpicBean = null;
		selectpicBean = listbean.get(0);

		return selectpicBean;
	}

	/**
	 * 根据原来workid和 选中状态 查询 作品选中图片信息
	 * 
	 * @param isselect
	 *            是否选中
	 * @param workid
	 *            作品Id
	 * @return
	 */
	public static List<SelectpicBean> queryByIsselect(String isselect,
			String workid) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		List<SelectpicBean> bean = dh.queryPicByIsselect(isselect, workid);
		return bean;
	}

	/**
	 * 先根据imgid 查询数据库
	 * 
	 * @param imgid
	 * @return
	 */
	public static SelectpicBean queryByImgId2DB(String imgid) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		SelectpicBean bean = dh.queryPicByImgId(imgid);
		return bean;

	}

	/**
	 * 根据imgid 更新选中图片 信息到数据库
	 * 
	 * @param imgid
	 * @param key
	 * @param value
	 */
	public static void update2DB(String imgid, String key, String value) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.revisePic(imgid, key, value);
	}

	/**
	 * 根据imgid 更新选中图片 压缩 保存 SD是否存在 SD新路径 到数据库
	 * 
	 * @param imgid
	 * @param newpath
	 */
	public static void update2DBsava(String imgid, String newpath) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.revisePicSava(imgid, newpath);
	}

	/**
	 * 插入数据前 先根据oldpath workid 查询数据库
	 * 
	 * @param oldpath
	 * @param workid
	 * @return
	 */
	public static List<SelectpicBean> query2DB(String oldpath, String workid) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		ArrayList<SelectpicBean> list = dh.queryPicByOldPathAndWorkid(oldpath,
				workid);
		return list;

	}

	/**
	 * 保存 选中图片信息 和选中蒙版 信息
	 * 
	 * @return
	 */
	public static void savaPicMaskInfo(String imgid, float PicW, float PicH,
			float MaskW, float MaskH) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.savaPicMaskInfo(imgid, PicW, PicH, MaskW, MaskH);

	}

	/**
	 * 保存 计算结果
	 * 
	 * @return
	 */
	public static void savaComputeInfo(String imgid, float initscale,
			float scale, float translatex, float translatey, float cutx,
			float cuty, float cutwidth, float cutheight) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.savaComputeInfo(imgid, initscale, scale, translatex, translatey,
				cutx, cuty, cutwidth, cutheight);

	}

	/**
	 * 根据 workpage，workid 查询 imgid
	 * 
	 * @return
	 */
	public static String queryByWorkpageAndWorkid(String workpage, String workid) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		List<SelectpicBean> list = dh
				.queryByWorkpageAndWorkid(workpage, workid);
		SelectpicBean selectpicBean = list.get(0);
		String imgid = selectpicBean.getImgid();
		return imgid;

	}

	/**
	 * 根据imgid 更新workpage信息
	 * 
	 * @return
	 */
	public static void updateSelectPic(String imgid, String workpage) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.updateSelectPic(imgid, workpage);

	}

	/**
	 * 查询本作品中 选中的 没有上传成功的 Imgid
	 * 
	 * @param workid
	 * @param isselect
	 * @param isupload
	 * @return
	 */
	public static List<String> querySelectPicImgid(String workid,
			String isselect, String isupload) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		List<String> list = dh.querySelectPicImgid(workid, isselect, isupload);
		return list;
	}

	/**
	 * 查询本作品中 选中的 没有上传成功的 Imgid
	 * 
	 * @param workid
	 * @param isselect
	 * @param isupload
	 * @return
	 */
	public static List<String> querySelectPicImgid2(String workid,
			String isselect, String isupload) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		List<String> list = dh.querySelectPicImgid2(workid, isselect, isupload);
		return list;
	}

	/**
	 * 根据imgid，workpage 更新cardinfo信息
	 * 
	 * @return
	 */
	public static void updateSelectPic(String imgid, String workpage,
			String cardinfo) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.updateSelectPic(imgid, workpage, cardinfo);

	}

	/**
	 * 按 imgid 删除 数据库信息
	 */
	public static void deleteImgid(String imgid) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.deleteImgid(imgid);
	}

	/**
	 * 按workid 全部删除 数据库图片信息
	 */
	public static void deleteWorkid(String workid) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.deleteSelectpicByWorkid(workid);
	}

	/**
	 * 将SQL信息保存SD 没有用到
	 * 
	 * @param workid
	 */
	public static void saveWorksInfo(String workid) {
		List<SelectpicBean> list = queryByIsselect("true", workid);
		Collections.sort(list, new SelectpicBean());
		JSONArray arr = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			try {
				SelectpicBean bean = list.get(i);
				JSONObject object = JSONUtil.objectToJson(bean);
				arr.put(i, object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		String jsonstr = arr.toString();
		String worksdir = FileUtil.getWorksDir(workid);
		FileUtil.saveFile(jsonstr, worksdir, "Sl" + workid, MyConstants.TXT);

	}

	/**
	 * 查看全部 没有用到
	 * 
	 * @return
	 */
	// public static List<SelectpicBean> showAll() {
	// DataHelper dh = new DataHelper(MyApplication.getContext());
	// List<SelectpicBean> querySelectPic = dh.querySelectPic();
	// return querySelectPic;
	// }

}
