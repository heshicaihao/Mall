package com.nettactic.mall.picdeal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 其中downloadTasks表示的是线程队列， taskIdSet是任务队列，作用就是用来管理线程队列，
 * 此程序用的是去重操作。已经下载过的文件，不会再次下载。
 * 
 * 线程池
 * @author heshicaihao
 * 
 */
public class DealManagerThread implements Runnable {

	private DealTaskManager mTaskManager;

	// 创建一个可重用固定线程数的线程池
	private ExecutorService pool;
	// 线程池大小
	private final int POOL_SIZE = 1;
	// 轮询时间
	private final int SLEEP_TIME =2000;
	// 是否停止
	private boolean isStop = false;

	public DealManagerThread() {
		mTaskManager = DealTaskManager.getInstance();
		pool = Executors.newFixedThreadPool(POOL_SIZE);

	}

	public void run() {
		while (!isStop) {
			DealTask downloadTask = mTaskManager.getDealTask();
			if (downloadTask != null) {
				pool.execute(downloadTask);
			} else { // 如果当前未有downloadTask在任务队列中
				try {
					// 查询任务完成失败的,重新加载任务队列
					// 轮询,
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		if (isStop) {
			pool.shutdown();
		}
	}

	/**
	 * @param isStop
	 *            the isStop to set
	 */
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

}
