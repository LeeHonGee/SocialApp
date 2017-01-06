/**
 *  文件名:SendMessageTaskMgr.java
 *  修改人:lihangjie
 *  创建时间:2015-11-22 下午10:33:31
 */
package com.sharemob.tinchat.modules.danmu;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-11-22 下午10:33:31]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class SendTaskMgr {
	private static final int ADD_VIEW=1;
	private Handler handler=null;
	public SendTaskMgr(final Callback callback) {
         handler = new Handler() {
        	 public void handleMessage(Message msg) {
                 super.handleMessage(msg);
             	switch (msg.what) {
				case ADD_VIEW:
					callback.sendMessageItem();
					DanmuItem danmuItem=(DanmuItem)msg.obj;
					CacheMemory.list_danmu.add(danmuItem);
					
					break;
				default:
					break;
				}
        	 }
         };
	}

	
	public void sendMessage(DanmuItem danmuItem){
		Message message = Message.obtain();
		message.what =ADD_VIEW;
		message.obj=danmuItem;
		handler.sendMessage(message);
	}
	
	public interface Callback{
		public void sendMessageItem();
	}
}