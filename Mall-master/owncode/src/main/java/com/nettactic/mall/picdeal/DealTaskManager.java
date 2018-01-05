package com.nettactic.mall.picdeal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 线程管理类
 * 
 * @author heshicaihao
 * 
 */
public class DealTaskManager {

	public String TAG = "UPTaskManager";
	// UI请求队列
	private LinkedList<DealTask> dealTasks;
	// 任务不能重复
	private Set<String> taskIdSet;

	private static DealTaskManager dealTaskMananger;

	private DealTaskManager() {

		dealTasks = new LinkedList<DealTask>();
		taskIdSet = new HashSet<String>();

	}

	public static synchronized DealTaskManager getInstance() {
		if (dealTaskMananger == null) {
			dealTaskMananger = new DealTaskManager();
		}
		return dealTaskMananger;
	}

	// 1.先执行
	public void addTask(DealTask dealTask) {
		synchronized (dealTasks) {
			if (!isTaskRepeat(dealTask.getFileId())) {
				// 增加下载任务
				dealTasks.addLast(dealTask);
			}
		}

	}

	public boolean isTaskRepeat(String fileId) {
		synchronized (taskIdSet) {
			if (taskIdSet.contains(fileId)) {
				// return true;
				return false;
			} else {
				// LogUtils.logd(TAG, "DealTask管理器增加上传任务：" + fileId);
				taskIdSet.add(fileId);
				return false;
			}
		}
	}

	public DealTask getDealTask() {
		synchronized (dealTasks) {
			if (dealTasks.size() > 0) {
				DealTask dealTask = dealTasks.removeFirst();
				// LogUtils.logd(TAG, "DealTask管理器增加上传任务：" +
				// dealTask.getFileId()
				// + "取出任务");
				return dealTask;
			}
		}
		return null;
	}

	public int getDealTaskSize() {
		int size = -1;
		size = dealTasks.size();
		return size;
	}
}
