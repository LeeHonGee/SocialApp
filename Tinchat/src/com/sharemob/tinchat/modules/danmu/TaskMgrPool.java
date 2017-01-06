/**
 *  文件名:TaskMgr.java
 *  修改人:lihangjie
 *  创建时间:2015-11-23 上午10:14:01
 */
package com.sharemob.tinchat.modules.danmu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-11-23 上午10:14:01]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class TaskMgrPool {

	private static TaskMgrPool instance=null;
	private ExecutorService singleThreadExecutor = null;
	private TaskMgrPool(){
		singleThreadExecutor = Executors.newFixedThreadPool(10);
	}
	public static TaskMgrPool getInstance(){
		if(instance==null){
			instance=new TaskMgrPool();
		}
		return instance;
	}
	
	
	public void submitTaskworker(final TaskMgrCallback taskMgrCallback){
		singleThreadExecutor.submit(new Runnable() {
			public void run() {
				taskMgrCallback.taskworker();
			}
		},1000);
	}
}
interface TaskMgrCallback{
	public void taskworker();
}