/**
 *  文件名:CacheManager.java
 *  修改人:lihangjie
 *  创建时间:2015-9-17 上午11:06:36
 */
package com.sharemob.tinchat.modules.dao.cache;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.Matrix;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.lib.common.Utils;
import com.sharemob.tinchat.modules.dao.entity.UserInfo;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-9-17 上午11:06:36]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class CacheManager {
	
		public enum CallAuth{	
			Mine,Other
		}
		public int CallState=CallAuth.Mine.ordinal();
	    public static int TitleBgColor=0x2f2b2f;
	    public static String IP = "192.168.0.103";
//	    public static String IP="172.16.4.37";
	    public UserInfo userInfo=new UserInfo();
	    public static String LOCAL_IMAGE_URL=new String("http://"+IP+"/img/%s"); 
	    public List<String> browserList= new ArrayList<String>();
	    private static CacheManager instance = new CacheManager();
	    public static CacheManager getInstance() {
	        return instance;
	    }
	    
	    public void initUserInfo(String json) {
	        if(userInfo.uid == null) {
	            userInfo.loadData(json);
	            Matrix.setStringForKey(SMGlobal.getInstance().context,"USER_UID", userInfo.uid);
	            Matrix.setStringForKey(SMGlobal.getInstance().context,"USER_PASSWORD", userInfo.password);
	            Matrix.setIntegerForKey(SMGlobal.getInstance().context,"USER_ID", userInfo.ID);
	            return;
	        }else{
	            userInfo.loadData(json);
	        }
	    }
	    
	    public final String getUserInfoUid() {
	    	return userInfo.uid;
//	        return Matrix.getStringForKey(SMGlobal.getInstance().context,"USER_UID", "9239b5c11ca107a8248ea3ae976c316d");
	    }
	    
//	    public void getTemporary() {
//	        String json = Matrix.getStringForKey(SMGlobal.getInstance().context,"UserInfo", null);
//	        if(json != null) {
//	            userInfo.loadData(json);
//	        }
//	    }
	    
	    public static void requestHttp(String url, AsyncHttpRequest.RequestCallback requestCallback) {
	        AsyncHttpRequest httpRequest = new AsyncHttpRequest();
	        String[] parameters = url.split("\\?");
	        httpRequest.doRequest(parameters[0x0], parameters[0x1], false, requestCallback);
	    }
	    
	    public void requestHttp(Context context, String url) {
	        AsyncHttpRequest httpRequest = new AsyncHttpRequest();
	        String[] parameters = url.split("\\?");
	        httpRequest.doRequest(parameters[0x0], parameters[0x1], false, new AsyncHttpRequest.RequestCallback() {
	            public void requestDidFinished(String body) {
	                Utils.sendBroadcast(SMGlobal.getInstance().context, SMGlobal.NETWORK_OK, body);
	            }
	            public void requestDidFailed() {
	                Utils.sendBroadcast(SMGlobal.getInstance().context, SMGlobal.NETWORK_NOT_FOUND, "");
	            }
	        });
	    }
}