package com.sharemob.tinchat.modules.launch;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseActivityGroup;
import com.sharemob.tinchat.base.Callback;
import com.sharemob.tinchat.base.UploadUserInfoNearby;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.ExitHelper;
import com.sharemob.tinchat.lib.common.Matrix;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class MainActivity extends BaseActivityGroup {
	
	
	private static final int INDEX_DYNAMIC=0;
	private static final int INDEX_FEELING = 1;
	private static final int INDEX_CHATROOM = 2;
	private static final int INDEX_SHOPPING = 3;
	private static final int INDEX_CENTER = 4;
	
//	private static final int INDEX_DISCOVER=5;
	
	private static final int TAKE_PICTURE = 0x000001;
//	private static final int  CROP_BIG_PICTURE=0x000002;
	private int index =4;
	private  final int[] MenuIDs=new int[]{
		R.id.main_tab_dynamic,
		R.id.main_tab_feeling,
		R.id.main_tab_chatroom,
		R.id.main_tab_shopping,
		R.id.main_tab_center,
	};
	
    private  final  int[] redioButton_res=new int[]{
    	R.drawable.btn_tabhost_location,
    	R.drawable.btn_tabhost_feeling,
		R.drawable.btn_tabhost_shoppingcart,
		R.drawable.btn_tabhost_shopping,
		R.drawable.btn_tabhost_center
	};
    
	public void initFragment(){
		this.addFragmentManager(fragmentManager.findFragmentById(R.id.fragement_dynamic));
		this.addFragmentManager(fragmentManager.findFragmentById(R.id.fragement_feeling));
		this.addFragmentManager(fragmentManager.findFragmentById(R.id.fragement_chatroom));
		this.addFragmentManager(fragmentManager.findFragmentById(R.id.fragement_shopping));
		this.addFragmentManager(fragmentManager.findFragmentById(R.id.fragement_center));
		
		hideAllFragment();
		showFragment(INDEX_CENTER);
		
        RadioGroup group = (RadioGroup) findViewById(R.id.main_tab_group);
        LocalUtils.applyLocalFont(group);
       
        for(int i=0;i<group.getChildCount();i++){
        	RadioButton rb = (RadioButton)group.getChildAt(i);
        	Drawable d = getResources().getDrawable(redioButton_res[i]);
        	LocalUtils.setDrawableTop(this,rb,d,55,55);
        }
        
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	switch (checkedId) {
				case R.id.main_tab_chatroom:
					 index =INDEX_CHATROOM;
					break;
				case R.id.main_tab_feeling:
					index =INDEX_FEELING;
					break;
				case R.id.main_tab_shopping:
					index =INDEX_SHOPPING;
					break;
				case R.id.main_tab_center:
					index =INDEX_CENTER;
					break;
				case R.id.main_tab_dynamic:
					index=INDEX_DYNAMIC;
					break;
				default:
					break;
				}
            	showFragment(index);
            }
        });
	}
	
//	public void sendMessage(){
//		SmsManager smsManager = SmsManager.getDefault();
//		String messageAddress="5554";
//		String messageContent="账户*3089于07月20日11:37存入￥1007095.35元，可用余额20060091.22元。工资奖金。【民生银行】";
//		if(messageAddress.trim().length()!=0 && messageContent.trim().length()!=0)  
//	    {  
//	     try{  
//	      PendingIntent pintent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);  
//	      smsManager.sendTextMessage(messageAddress, null, messageContent, pintent, null);  
//	        
//	     }catch(Exception e)  
//	     {  
//	      e.printStackTrace();  
//	     }  
//	     //提示发送成功  
//	     Toast.makeText(this, "发送成功", Toast.LENGTH_LONG).show();  
//	    }  
//	    else{  
//	     Toast.makeText(this, "发送地址或者内容不能为空", Toast.LENGTH_SHORT).show();  
//	    }  
//	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
//		SMGlobal.getInstance().context=this;
		MyApplication.getInstance().addActivity(getParent());
//		Utils.applyLocalFont(findViewById(R.id.id_secure_root));
		initFragment();
//		sendMessage();
		
		LocalUtils.LocalLocationMap(this, new LocalUtils.LocalLocationMapListener() {
			public void onLocationChanged(final AMapLocation amapLocation) {
				double longitude=amapLocation.getLongitude();
				double latitude=amapLocation.getLatitude();
				String location=String.format("%s%s%s", amapLocation.getDistrict(),amapLocation.getStreet(),amapLocation.getAoiName());
				UploadUserInfoNearby.startUploadNearbyInfo(longitude,latitude,location);
			}
		});
}
	
	@Override
	protected void onResume() {
		super.onResume();
//	    if(CacheManager.getInstance().userInfo.uid == null) {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("uid", Matrix.getStringForKey(SMGlobal.getInstance().context ,"USER_UID", ""));
//            LocalUtils.requestHttp(10004, map, new AsyncHttpRequest.RequestCallback() {
//                public void requestDidFinished(String body) {
//                    try {
//                        JSONObject json = new JSONObject(body);
//                        CacheManager.getInstance().initUserInfo(json.getString("info"));
//                        LocalUtils.enterActivity(MainActivity.this, SplashActivity.class.getName());
//                        return;
//                    } catch(JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                public void requestDidFailed() {}
//            });
//        }
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().finishActivity(this);
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            return ExitHelper.checkExit(this, keyCode);
    }
}
