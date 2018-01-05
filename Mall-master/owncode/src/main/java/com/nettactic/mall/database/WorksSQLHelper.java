package com.nettactic.mall.database;

import java.util.ArrayList;
import java.util.List;

import com.nettactic.mall.bean.WorksInfoBean;
import com.nettactic.mall.common.MyApplication;

public class WorksSQLHelper {

	/**
	 * 最近作品插入 保留20条记录
	 * 
	 * @param data
	 */
	public static void addWorks(WorksInfoBean data) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.addWorks(data);
	}

	/**
	 * 此用户 作品 查询多个
	 * 
	 * @return
	 */
	public static ArrayList<WorksInfoBean> queryWorks(ArrayList<String> work_ids) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		ArrayList<WorksInfoBean> list = dh.queryWorks(work_ids);
		return list;
	}

	/**
	 * 修改多个作品 编辑状态
	 * 
	 * @param work_ids
	 */
	public static void reviseEditState(ArrayList<String> work_ids) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		dh.reviseEditState(work_ids);
	}

	/**
	 * 根据user_id 查询 作品信息
	 * 
	 * @param user_id
	 */
	public static List<WorksInfoBean> queryWorksByUserId(String user_id) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		List<WorksInfoBean> list = dh.queryWorksByUserId(user_id);
		return list;
	}

	/**
	 * 作品 删单条
	 * 
	 * @param data
	 */
	public static int deleteWorks(WorksInfoBean data) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		int id = dh.deleteWorks(data);
		return id;
	}
	
	/**
	 * 作品 删单条
	 * 
	 * @param work_id
	 */
	public static int deleteWorks(String work_id) {
		DataHelper dh = new DataHelper(MyApplication.getContext());
		int id = dh.deleteWorks(work_id);
		return id;
	}


}
