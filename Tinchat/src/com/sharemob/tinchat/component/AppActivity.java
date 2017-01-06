/**
 *  文件名:AppActivity.java
 *  修改人:lihangjie
 *  创建时间:2016-5-18 下午2:59:16
 */
package com.sharemob.tinchat.component;

import android.content.IntentFilter;
import android.os.Bundle;

import com.androlua.LuaActivity;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.common.SMGlobal;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-5-18 下午2:59:16]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class AppActivity extends LuaActivity {

	 public static final String NAME="AppActivity";
//	 public static final String PARAMETERS="parameters";
	 public static final String ACCOUNTID="accountID";
	 public static final String ONESELF="oneself";
	 
	  private void register_http_response_text()
	  {
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(SMGlobal.HTTP_RESPONSE_TEXT_ACTION);
	    filter.addAction(SMGlobal.UPLOAD_IMAGE_DYNAMIC_ACTION);
	    filter.addAction(SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION);
	    filter.addAction(SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION);
	    filter.addAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO);
	    filter.addAction(SMGlobal.ACTIONO_DELETE_PHTOTO);
	    filter.addAction(SMGlobal.UPLOAD_MULTIPARTFILE_ACTION);
	    filter.addAction(SMGlobal.CHATING_EMOJI_ACTION);
	    filter.addAction(SMGlobal.CHATING_IMAGE_ACTION);
	    filter.addAction(SMGlobal.CHATING_LOCATION_ACTION);
	    filter.addAction(SMGlobal.CHATING_VOICE_ACTION);
	    filter.addAction(SMGlobal.CHATING_WS_ACTION);
	    registerReceiver(filter);
	  }
	 
	 public void onCreate(Bundle savedInstanceState)
	 {
		String activityName=getIntent().getStringExtra(AppActivity.NAME); 
	    OverrideFunc(savedInstanceState,this,activityName);
	    super.onCreate(savedInstanceState);
	    MyApplication.getInstance().addActivity(this);
	    register_http_response_text();
	    LocalUtils.applyLocalFont(getWindow().getDecorView());
	  }
	 
	 public String getParameters(){
		return this.getIntent().getStringExtra(AppActivity.PARAMETERS);
	 }
	 
	 @Override
	 protected void onDestroy() {
		super.onDestroy();
		this.loadingListener.cleanBitmapList();
	 }
	 
	@Override
	 public void finish() {
		 super.finish();
		 overridePendingTransition(R.anim.animi_remain,R.anim.out_to_right);
	 }
}
