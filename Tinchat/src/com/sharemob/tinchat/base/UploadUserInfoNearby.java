package com.sharemob.tinchat.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;


public class UploadUserInfoNearby {

	private static final int CMD_UPLOADNEARBY=10054;
	
	public static void startUploadNearbyInfo(final double longitude,final double latitude,final String address){
		LocalUtils.AsyncSingleThread(new LocalUtils.LocalSingleThreadListener() {
			@Override
			public void doTaskExecutor() {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("lat", latitude);
				map.put("lng", longitude);
				map.put("address", address);
				map.put("uid", CacheManager.getInstance().userInfo.uid);
				//更新用户LBS地理位置
				CacheManager.getInstance().userInfo.address=address;
				CacheManager.getInstance().userInfo.longitude=longitude;
				CacheManager.getInstance().userInfo.latitude=latitude;
				
				LocalUtils.requestHttp(CMD_UPLOADNEARBY, map,new RequestCallback() {
					public void requestDidFinished(String body) {
						
					}
					public void requestDidFailed() {
						
					}
				});
			}
		});
	}
}

final class UploadInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public double longitude;
	public double latitude;
	public String location;
}
