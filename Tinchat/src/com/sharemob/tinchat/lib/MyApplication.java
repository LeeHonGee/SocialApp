package com.sharemob.tinchat.lib;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.androlua.CrashHandler;
import com.sharemob.tinchat.lib.common.FileUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.thirdparty.weixin.WXConstant;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2015-9-11 下午3:39:22]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public class MyApplication extends Application {

	private static MyApplication instance = new MyApplication();
	private List<Activity> stackList = new LinkedList<Activity>();
	public static Context context = null;
	private UncaughtExceptionHandler defaultExceptionHandler;

	public MyApplication() {
		System.out.println("启动Application");
		WXConstant.context = this;
		MyApplication.context = this;
		SMGlobal.getInstance().context=this;
	}
	
	public synchronized static MyApplication getInstance(){
		return instance;
	}
	
	public void addActivity(Activity activity) { 
		stackList.add(activity); 
    } 
	
	public void removeActivity(Activity activity) { 
		stackList.remove(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : stackList) { 
                if (activity != null){ 
                    activity.finish();
                }
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
 
	public static Context getContext() {
		return WXConstant.context;
	}

	public void onCreate() {
		super.onCreate();

		WXConstant.api = WXAPIFactory.createWXAPI(this, WXConstant.AppID, false);

		// 捕获应用异常
		catchUncaughtExceptionHandler();
		// 对更新服务检测更新版本
		UpdateService();

		GlobalParams.getInstance().ImageLoaderConfiguration();

		String filePath = String.format("%s%s%s", this.getFilesDir(),File.separator, "tinchat.sqlite");
		System.out.println(filePath);
		FileUtils.xcopyFileFromAssets(this, "tinchat.sqlite", filePath);

		// AssetsUtils.copyFilesFassets(this,
		// "script",String.format("%s%s%s",getFilesDir().getAbsolutePath(),File.separator,"script"));
		CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());

	}

	/**
	 * 检测更新服务组件
	 */
	private void UpdateService() {

	}

	 public void finishActivity(Activity activity) {
	        if(activity != null) {
	            stackList.remove(activity);
	            activity.finish();
	            activity = null;
	        }
	}
	 
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * 记录未捕捉异常
	 * 
	 * @exception throws
	 */
	private void catchUncaughtExceptionHandler() {
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				defaultExceptionHandler.uncaughtException(thread, ex);
			}
		});
	}
}
