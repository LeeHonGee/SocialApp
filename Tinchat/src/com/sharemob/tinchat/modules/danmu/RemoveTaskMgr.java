package com.sharemob.tinchat.modules.danmu;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class RemoveTaskMgr{
	private static final String TAG = "RemoveThread";
	private Handler removeHandler = null;
	private boolean loop=true;
	private static final int REMOVE_VIEW = 2;
	private static final int Delayed = 2000;

	public RemoveTaskMgr(final RemoveTaskMgr.Callback listener) {
		removeHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case REMOVE_VIEW:
					listener.removeItem();
					break;
				default:
					break;
				}
			}
		};
	}

	public Handler getHandler() {
		return this.removeHandler;
	}

	public void closed() {
		this.removeHandler.getLooper().quit();
	}

	public void start(){
		TaskMgrPool.getInstance().submitTaskworker(new TaskMgrCallback(){
			public void taskworker() {
				while(loop){
					try {
						Thread.sleep(Delayed);
						removeHandler.sendEmptyMessage(REMOVE_VIEW);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public interface Callback{
		public void removeItem();
	}

}
