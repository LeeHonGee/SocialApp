/**
 *  文件名:EgameLaunchActivity.java
 *  修改人:lihangjie
 *  创建时间:2016-1-2 下午10:06:40
 */
package com.sharemob.tinchat.modules.launch;

import android.os.Bundle;

import com.androlua.LuaActivity;
import com.sharemob.tinchat.lib.MyApplication;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2016-1-2 下午10:06:40]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public class SplashActivity extends LuaActivity {

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		MyApplication.getInstance().addActivity(this);
		OverrideFunc(bundle, this, "SplashActivity");
	}

	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().finishActivity(this);
	}

}