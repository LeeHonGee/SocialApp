package com.sharemob.tinchat.lib.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.sharemob.tinchat.lib.NetworkUtils;

public final class MobLocation {

	private static final String TAG="MobLocation";
	private static final String MethodName="doSetUpRequest";
	private static final String location_api_URL="http://api.map.baidu.com/location/ip?ak=2fab2ce4b8b5fff92eb03a9ab9e4b00a";
	
	public static final void send_Setup(Context ctx){
		NetworkUtils.send(location_api_URL,"",false,MobLocation.class.getName(),MethodName);
	}
	
	public void  doSetUpRequest(String body){
		try {
			JSONObject	json = new JSONObject(body);
			String[] address=json.getString("address").split("\\|");
			String country=address[0];
			String province=address[1];
			String city=address[2];
			Log.i(TAG,"------"+country+","+province+","+city);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
