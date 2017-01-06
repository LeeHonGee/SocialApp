//package com.sharemob.tinchat.lib;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.sharemob.tinchat.lib.common.SMGlobal;
//import com.sharemob.tinchat.lib.websocket.WebSocketConnection;
//import com.sharemob.tinchat.lib.websocket.WebSocketConnectionHandler;
//import com.sharemob.tinchat.lib.websocket.WebSocketException;
//import com.sharemob.tinchat.lib.websocket.WebSocketOptions;
//import com.sharemob.tinchat.modules.dao.cache.CacheManager;
//
//public class WSManager {
//
//	private static final String TAG=WSManager.class.getSimpleName();
//	private  ExecutorService singleThreadExecutor;
//	private  String WSURL = "ws://%s/websocket";
//	private final int ReceiveTimeout=15*1000;
//	private final int ConnectTimeout=10*1000;
//	private Activity activity;
//	protected WebSocketConnection websocket =null;
//	private static WSManager instance=null;
//	private WSManager() {
//		singleThreadExecutor = Executors.newFixedThreadPool(1);
//		this.setWSUrl(CacheManager.IP);
//	}
//	
//	public static WSManager getInstance() {
//		if(instance==null){
//			return instance=new WSManager();
//		}
//		return instance;
//	}
//	
//	private void setWSUrl(String address){
//		this.WSURL=String.format(WSURL,address);
//		System.out.println(WSURL);
//	}
//	
//	public void receiveTextMessage(String text){
//		Intent intent =new Intent();
//		Bundle bundle=new Bundle();
//		intent.setAction(SMGlobal.CHATING_WS_ACTION);
//		bundle.putString("text", text);
//		intent.putExtras(bundle);
//		SMGlobal.getInstance().context.sendBroadcast(intent);
//	}
//	
//	public void onCreate(final Map<String,Object> map){
////		if(websocket!=null){
////			return ;
////		}
//		try {
//			WebSocketOptions  options=new WebSocketOptions();
//			options.setSocketReceiveTimeout(ReceiveTimeout);
//			options.setSocketConnectTimeout(ConnectTimeout);
//			websocket = new WebSocketConnection();
//			websocket.connect(WSURL, new WebSocketConnectionHandler() {
//				public void onOpen() {
//					
//					JSONObject object=new JSONObject(map);
//					System.out.println(object.toString());
//					websocket.sendTextMessage(object.toString());
//				}
//				@Override
//                public void onBinaryMessage(byte[] payload) {
//                        System.out.println("onBinaryMessage size="+payload.length);
//                }
//				 @Override
//                 public void onRawTextMessage(byte[] payload) {
//                         System.out.println("onRawTextMessage size="+payload.length);
//                 }
//				public void onTextMessage(String payload) {
//					Log.i(TAG, "Got echo: " + payload);
//					receiveTextMessage(payload);
//				}
//
//				public void onClose(int code, String reason) {
//					Log.i(TAG,"Close－－－－－当前网络已断开");
////					websocket.reconnect();
//				
//				}
//			},options);
//		} catch (WebSocketException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void sendTextMessage(final String json){
////		singleThreadExecutor.execute(new Runnable() {
////			public void run() {
////				try {
////				
////				} catch (Exception e) {
////					e.printStackTrace();
////				} 
////			}
////		});
////		if(!websocket.isConnected()){
////			websocket.reconnect();
////		}else{
////			Toast.makeText(SMGlobal.getInstance().context, "网络链接断开", Toast.LENGTH_SHORT).show();
////		}
//		websocket.sendTextMessage(json);
//	}
//	
//	public void onDestroy() {
//		if (websocket.isConnected()) {
//			websocket.disconnect();
//			singleThreadExecutor.shutdown();
//		}
//	}
//}
