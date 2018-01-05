package com.nettactic.mall.picupload;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.nettactic.mall.utils.LogUtils;

/**
 * 线程管理类
 * 
 * @author heshicaihao
 * 
 */
public class UploadTaskManager {

	public String TAG = "UPTaskManager";
	// UI请求队列
	private LinkedList<UploadTask> uploadTasks;
	// 任务不能重复
	private Set<String> taskIdSet;

	private static UploadTaskManager uploadTaskMananger;

	private UploadTaskManager() {

		uploadTasks = new LinkedList<UploadTask>();
		taskIdSet = new HashSet<String>();

	}

	public static synchronized UploadTaskManager getInstance() {
		if (uploadTaskMananger == null) {
			uploadTaskMananger = new UploadTaskManager();
		}
		return uploadTaskMananger;
	}

	// 1.先执行
	public void addTask(UploadTask uploadTask) {
		synchronized (uploadTasks) {
			if (!isTaskRepeat(uploadTask.getFileId())) {
				// 增加下载任务
				uploadTasks.addLast(uploadTask);
			}
		}

	}

	public boolean isTaskRepeat(String fileId) {
		synchronized (taskIdSet) {
			if (taskIdSet.contains(fileId)) {
				// return true;
				return false;
			} else {
//				LogUtils.logd(TAG, "uploadTasks管理器增加上传任务：" + fileId);
				taskIdSet.add(fileId);
				return false;
			}
		}
	}

	public UploadTask getUploadTask() {
		synchronized (uploadTasks) {
			if (uploadTasks.size() > 0) {
				UploadTask uploadTask = uploadTasks.removeFirst();
				LogUtils.logd(TAG, "uploadTasks管理器增加上传任务：" + uploadTask.getFileId()
						+ "取出任务");
				return uploadTask;
			}
		}
		return null;
	}
	
	public int getUploadTaskSize() {
		int size = -1;
		size = uploadTasks.size();
		return size;
	}
}
