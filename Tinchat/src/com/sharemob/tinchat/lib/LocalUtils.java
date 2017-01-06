package com.sharemob.tinchat.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.androlua.LuaFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.Callback;
import com.sharemob.tinchat.base.LocalAlertDialog;
import com.sharemob.tinchat.base.LocalSheetDialog;
import com.sharemob.tinchat.base.LocalSheetDialog.OnSheetItemClickListener;
import com.sharemob.tinchat.base.LocalSheetDialog.SheetItemColor;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.component.ChangeAddressDialog;
import com.sharemob.tinchat.component.ChangeAddressDialog.OnAddressCListener;
import com.sharemob.tinchat.component.LocalWheelDialog;
import com.sharemob.tinchat.component.wheeldialog.AbstractWheelTextAdapter;
import com.sharemob.tinchat.component.wheeldialog.ArrayWheelAdapter;
import com.sharemob.tinchat.component.wheeldialog.NumericWheelAdapter;
import com.sharemob.tinchat.component.wheeldialog.OnWheelScrollListener;
import com.sharemob.tinchat.component.wheeldialog.WheelView;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.Color;
import com.sharemob.tinchat.lib.common.FileUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.lib.common.UploadFileEntity;
import com.sharemob.tinchat.lib.common.UploadUtils;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.lib.refreashtabview.refreash.ILoadingLayout;
import com.sharemob.tinchat.lib.refreashtabview.refreash.PullToRefreshListView;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;
import com.sharemob.tinchat.thirdparty.weixin.WXConstant;

public final class LocalUtils {
	public static void initWheelViewDay(Activity activity, WheelView view,int year, int month) {
		view.setViewAdapter(new NumericWheelAdapter(activity, 1, LocalUtils.getDay(year, month), "%02d"));
	}

	public interface LocalMediaPlayerListener {
		void onCompletion();
	}

	public interface LocalSingleThreadListener {
		void doTaskExecutor();
	}

	public static interface LocalLocationMapListener {
		void onLocationChanged(AMapLocation amapLocation);
	}

	public static interface LocalEmojiListener {
		void loadImage(String name);
	}
	
	public static interface LocalAlertDialogListener{
		public void confirm();
		public void cancel();
	}

	public static void emoji(Activity activity,
			final LocalEmojiListener listener) {
		try {
			String emojis[] = activity.getAssets().list("emojis");
			if (emojis.length > 0) {
				for (String emoji : emojis) {
					listener.loadImage(emoji);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
    public static void SingleWheelDailog(final Activity activity,final String title,final String[] items,final String item_key) {
        ArrayWheelAdapter adapter = new ArrayWheelAdapter(activity, items);
        LocalWheelDialog dialog = new LocalWheelDialog(activity).builder();
        dialog.setTitle(title);
        final Map<String, Object> map = new HashMap<String, Object>();
        OnWheelScrollListener listener = new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            	
            }
            public void onScrollingFinished(WheelView wheel) {
                int indexItem = wheel.getCurrentItem();
                String ItemName = wheel.getTextItem(indexItem);
                map.put("WHEELDAIALOG_KEY", ItemName);
            }
        };
        dialog.getFirstWheelView().setViewAdapter(adapter);
        dialog.setScrollListener(listener, null);
        WheelView wv_first = dialog.getFirstWheelView();
        wv_first.setViewAdapter(adapter);
        
        
       dialog.setNegativeButton("取消", new View.OnClickListener() {
            public void onClick(View v) {
            	
            }
        });
       dialog.setPositiveButton("确定", new View.OnClickListener() {
            public void onClick(View v) {
                if(map.containsKey("WHEELDAIALOG_KEY")) {
                    String item_value = map.get("WHEELDAIALOG_KEY").toString();
                    System.out.println(item_value);
                    Intent intent = new Intent();
                    intent.setAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO);
                    intent.putExtra(SMGlobal.BUNDLE_ITEM_KEY, item_key);
                    intent.putExtra(SMGlobal.BUNDLE_ITEM_VALUE, item_value);
                    activity.sendBroadcast(intent);
                }
            }
        });
       dialog.show();
    }
	
	public static String getFilePrefix(String filename){
		String prefix=filename.substring(filename.lastIndexOf(".")+1);
		return prefix;
	}
	
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 

	public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
	} 
	/**
	 * "yyyy-MM-dd hh:mm:ss"
	 * @return
	 */
	public static String getDateTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new java.util.Date());
	}
	
	public static String getDateFromLong(String pattern, long time) {
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    Date dt = new Date((0x3e8 * time));
	    return format.format(dt);
	}
	
	public static String simpleDateFormat(String pattern, String time) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date=new Date();
		try {
			 date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
        return format.format(date);
	}
	
	public static void UploadMultipartFile(final Activity activity,final List<String> list,final String userid,final int type,final String actionName){
		AsyncSingleThread(new LocalSingleThreadListener() {
			@Override
			public void doTaskExecutor() {
				//---------多文件上传使用分批次提交---------
				for(String path:list){
					UploadFileEntity fileEntity=new UploadFileEntity(userid,type);
					fileEntity.setActionName(actionName);
					fileEntity.setLocalFilePath(path);
					System.out.println("void:"+path);
					String url=String.format("%s?data=%s", GlobalParams.URL_UploadMultipart,fileEntity.UploadParameters());
					System.out.println(url);
					UploadUtils.UploadFile(activity,fileEntity, url);
				}
			}
		});
	}
	
	public static void UploadVoiceFile(final Activity activity,final String path,final String userid){
		AsyncSingleThread(new LocalSingleThreadListener() {
			@Override
			public void doTaskExecutor() {
					UploadFileEntity fileEntity=new UploadFileEntity(userid,SMGlobal.UPLOAD_FILE_Voice);
					fileEntity.setLocalFilePath(path);
					String url=String.format("%s?data=%s", GlobalParams.URL_UploadMultipart,fileEntity.UploadParameters());
					System.out.println(url);
					UploadUtils.UploadFile(activity,fileEntity, url);
			}
		});
	}
	
	public static void UploadVideoFile(final Activity activity,final String path,final String userid){
		AsyncSingleThread(new LocalSingleThreadListener() {
			@Override
			public void doTaskExecutor() {
					UploadFileEntity fileEntity=new UploadFileEntity(userid,SMGlobal.UPLOAD_FILE_Video);
					fileEntity.setLocalFilePath(path);
					String url=String.format("%s?data=%s", GlobalParams.URL_UploadMultipart,fileEntity.UploadParameters());
					System.out.println(url);
					UploadUtils.UploadFile(activity,fileEntity, url);
			}
		});
	}
	
	public static String requestHttpForApp(int cmd,Map<String,Object> map, AsyncHttpRequest.RequestCallback callback) {
	        JSONObject object = new JSONObject(map);
	        String data = object.toString();
	        String url = String.format("http://%s/api/app?cmd=%d&data=%s", CacheManager.IP, cmd, data);
	        System.out.println(url);
	        CacheManager.requestHttp(url, callback);
	        return data;
	    }
	
	public static String requestHttp(int cmd,Map<String,Object> map,RequestCallback callback){
		JSONObject object=new JSONObject(map);
		String data=object.toString();
		String url=String.format("http://%s/api/user?cmd=%d&data=%s",CacheManager.IP,cmd,data);
		System.out.println(url);
		CacheManager.requestHttp(url,callback);
		return data;
	}
	
	public static void EditorTextDel(EditText mEdit){
		int selection = mEdit.getSelectionStart();
		String text = mEdit.getText().toString();
//		System.out.println(text);
		if (selection > 0) {
			String text2 = text.substring(selection - 1);
			if ("]".equals(text2)) {
				int start = text.lastIndexOf("[");
				int end = selection;
				mEdit.getText().delete(start, end);
				return;
			}
			mEdit.getText().delete(selection - 1, selection);
		}
	}
	
	public static void AsyncLoadingVoice(final Context activity,final ImageView chating_btn_voice,final String voice_url){
//		chating_btn_voice.callOnClick();
//		ClipDrawable mDownloadProgressDrawable = (ClipDrawable)chating_btn_voice.getDrawable();
//		mDownloadProgressDrawable.setLevel(40*100);
		final AnimationDrawable AniDraw = (AnimationDrawable) chating_btn_voice.getBackground();
		 final ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);
			singleThreadExecutor.execute(new Runnable() {
				public void run() {
			      try {
			  		Uri uri = Uri.parse(voice_url);
					final MediaPlayer player = MediaPlayer.create(activity, uri);
//					chating_btn_voice.setOnClickListener(new OnClickListener() {
//								public void onClick(View v) {
									if (player.isPlaying()) {
										player.pause();
										AniDraw.stop();
										chating_btn_voice.clearAnimation();
										AniDraw.selectDrawable(0);
									} else {
										AniDraw.start();
										try {
											player.start();
											player.setOnCompletionListener(new OnCompletionListener() {
												public void onCompletion(MediaPlayer mp) {
													AniDraw.stop();
													chating_btn_voice.clearAnimation();
													AniDraw.selectDrawable(0);
												}
											});
										} catch (IllegalStateException e) {
											e.printStackTrace();
										}

									}
//								}
//							});
			      	
			      } catch (Exception e) {
			      		e.printStackTrace();
			      	}finally{
			      		singleThreadExecutor.shutdown();
			      	}
				}
			});
	}
	
	public static void AsyncProgressVoice(final Context activity,final ImageView bgImageView,final ImageView chating_btn_voice,final String voice_url,final int time){
//		bgImageView.setImageResource(R.drawable.play_voice_clip);
		final ClipDrawable clip = (ClipDrawable)bgImageView.getDrawable();
		if(clip.getLevel()>0){
			LocalUtils.showToast("正在播放中");
			return;
		}
		
		final Timer timer = new Timer();
		final Handler handler = new Handler()
	    {
	        	public void handleMessage(Message msg)
	        	{
	        		if(msg.what == 0x1233)
	        		{
	        			//修改ClipDrawable的level值
	        			clip.setLevel(clip.getLevel()+(10000/(time*5)));
	        			//取消定时器
	        			if(clip.getLevel()==10000){
	        				timer.cancel();
	        				clip.setLevel(0);
	        			}
	        		}
	        	}
	    };
	    
        timer.schedule(new TimerTask()
        {
        	public void run()
        	{
        		Message msg = new Message();
        		msg.what = 0x1233;
        		//发送消息,通知应用修改ClipDrawable对象的level值
        		handler.sendMessage(msg);
        	}
        },0,200);
		
		final AnimationDrawable AniDraw = (AnimationDrawable) chating_btn_voice.getBackground();
	      try {
		  		Uri uri = Uri.parse(voice_url);
				final MediaPlayer player = MediaPlayer.create(activity, uri);
				final ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);
				singleThreadExecutor.execute(new Runnable() {
					public void run() {
						if (player.isPlaying()) {
							player.pause();
							AniDraw.stop();
							chating_btn_voice.clearAnimation();
							AniDraw.selectDrawable(0);
						} else {
							AniDraw.start();
							try {
								player.start();
								player.setOnCompletionListener(new OnCompletionListener() {
									public void onCompletion(MediaPlayer mp) {
										AniDraw.stop();
										chating_btn_voice.clearAnimation();
										AniDraw.selectDrawable(0);
									}
								});
							} catch (IllegalStateException e) {
								e.printStackTrace();
							}finally{
								singleThreadExecutor.shutdown();
							}

						}
					}
				});
//				bgImageView.setOnClickListener(new OnClickListener() {
//							public void onClick(View v) {
//								 
//							}
//						});
		      	
		      } catch (Exception e) {
		      		e.printStackTrace();
		      	}finally{
//		      		singleThreadExecutor.shutdown();
		      	}
		
	}
	
	public static void setLayoutParams(RelativeLayout relativeLayout,PullLoadMoreRecyclerView recyclerView,LinearLayout linearLayout){
		//删除原有视图布局,重新定义布局样式,视图还是原来控件
		relativeLayout.removeAllViews();
		RelativeLayout.LayoutParams lp_linearLayout = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp_linearLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//与父容器的左侧对齐
		linearLayout.setLayoutParams(lp_linearLayout);
		linearLayout.setVisibility(View.VISIBLE);
		relativeLayout.addView(linearLayout);
		
		RelativeLayout.LayoutParams lp_recyclerView = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp_recyclerView.addRule(RelativeLayout.ABOVE, linearLayout.getId());
		recyclerView.setLayoutParams(lp_recyclerView);
		recyclerView.setVisibility(View.VISIBLE);
		relativeLayout.addView(recyclerView);
	}
	
	public static void setEditChangedListener(EditText editText,final Button btn_send){
		editText.addTextChangedListener(new  TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				if(s.length()==0){
					btn_send.setVisibility(View.GONE);
				}
			}
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				if(s.length()==0){
					btn_send.setVisibility(View.GONE);
				}
			}
			public void afterTextChanged(Editable s) {
				if(s.length()>0){
					btn_send.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}
	public static void enterAppActivity(Context context,Map<String,Object> map,String activityName){ 
		try {
			JSONObject object=new JSONObject(map);
			System.out.println(object.toString());
			Class<?> clazz = Class.forName(AppActivity.class.getName());
			Intent intent = new Intent();
			intent.putExtra(AppActivity.NAME,activityName);
			intent.putExtra(AppActivity.PARAMETERS,object.toString());
			intent.setClass(context, clazz);
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.in_from_right,R.anim.animi_remain);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void enterActivity(Context context,String activityName){ 
		try {
			Class<?> clazz = Class.forName(activityName);
			Intent intent = new Intent();
			intent.setClass(context, clazz);
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.in_from_right,R.anim.animi_remain);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void enterAppFragmentActivity(Activity activity,Map<String,Object> map,String activityName){ 
//		try {
//			JSONObject object=new JSONObject(map);
//			Class<?> clazz = Class.forName(AppFragmentActivity.class.getName());
//			Intent intent = new Intent();
//			intent.putExtra(AppActivity.NAME,activityName);
//			intent.putExtra(AppActivity.PARAMETERS,object.toString());
//			intent.setClass(activity, clazz);
//			activity.startActivity(intent);
//			activity.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void setRecyclerViewScrollListener(RecyclerView recyclerView){
		int count=	recyclerView.getAdapter().getItemCount();
		if(count>0){
			recyclerView.scrollToPosition(count-1);
		}
		System.out.println(count);
	}
	
	public static void controlKeyboardLayout(final View root, final View scrollToView) {  
        root.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {  
            @Override  
            public void onGlobalLayout() {  
                Rect rect = new Rect();  
                //获取root在窗体的可视区域  
                root.getWindowVisibleDisplayFrame(rect);  
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)  
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;  
                //若不可视区域高度大于100，则键盘显示  
                if (rootInvisibleHeight > 200) {  
                    int[] location = new int[2];  
                    //获取scrollToView在窗体的坐标  
                    scrollToView.getLocationInWindow(location);  
                    //计算root滚动高度，使scrollToView在可见区域  
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;  
                    root.scrollTo(0, srollHeight);
                } else {  
                    //键盘隐藏  
                    root.scrollTo(0, 0);  
                }  
            }  
        });  
    }  

	public static void LocalLocationMap(Context activity,
			final TextView tv_location) {
		final AMapLocationClient mLocationClient = new AMapLocationClient(
				activity);
		AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		mLocationOption.setNeedAddress(true);
		mLocationOption.setOnceLocation(true);
		mLocationOption.setWifiActiveScan(true);
		mLocationOption.setMockEnable(false);
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();

		AMapLocationListener mLocationListener = new AMapLocationListener() {
			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				if (amapLocation != null) {
//					amapLocation.getLatitude();// 获取纬度
//					amapLocation.getLongitude();// 获取经度
//					amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//					amapLocation.getCountry();// 国家信息
//					amapLocation.getProvince();// 省信息
//					amapLocation.getCity();// 城市信息
//					amapLocation.getDistrict();// 城区信息
//					amapLocation.getStreet();// 街道信息
//					amapLocation.getAoiName();// 获取当前定位点的AOI信息
//					System.out.println(amapLocation.getLatitude());
//					System.out.println(amapLocation.getLongitude());
//					System.out.println(amapLocation.getAddress());
//					System.out.println(amapLocation.getStreetNum());
//					System.out.println(amapLocation.getAoiName());
//					// String uri=json.getString("uri");
//					double Altitude = amapLocation.getLatitude();
//					System.out.println(Altitude);
//					double Longitude = amapLocation.getLongitude();
//					amapLocation.getAoiName();
//					System.out.println(Longitude);
//					String url_location = LocalUtils.getLocationImage(
//							String.valueOf(Longitude),
//							String.valueOf(Altitude), 330, 220);
//					System.out.println(url_location);
//					System.out.println(amapLocation.getCity()
//							+ amapLocation.getDistrict()
//							+ amapLocation.getStreet()
//							+ amapLocation.getAoiName());
					tv_location.setText(
							amapLocation.getCity()+ 
							amapLocation.getDistrict()+ 
							amapLocation.getStreet()+ 
							amapLocation.getAoiName());
					
					mLocationClient.stopLocation();
					mLocationClient.onDestroy();
				}
			}
		};

		mLocationClient.setLocationListener(mLocationListener);
	}
	
	public static void LocalLocationMap(Activity activity,
			final LocalLocationMapListener mapListener) {
		final AMapLocationClient mLocationClient = new AMapLocationClient(
				activity);
		AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		mLocationOption.setNeedAddress(true);
		mLocationOption.setOnceLocation(true);
		mLocationOption.setWifiActiveScan(true);
		mLocationOption.setMockEnable(false);
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();

		AMapLocationListener mLocationListener = new AMapLocationListener() {
			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				if (amapLocation != null) {
//					amapLocation.getLatitude();// 获取纬度
//					amapLocation.getLongitude();// 获取经度
//					amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//					amapLocation.getCountry();// 国家信息
//					amapLocation.getProvince();// 省信息
//					amapLocation.getCity();// 城市信息
//					amapLocation.getDistrict();// 城区信息
//					amapLocation.getStreet();// 街道信息
//					amapLocation.getAoiName();// 获取当前定位点的AOI信息
//					System.out.println(amapLocation.getLatitude());
//					System.out.println(amapLocation.getLongitude());
//					System.out.println(amapLocation.getAddress());
//					System.out.println(amapLocation.getStreetNum());
//					System.out.println(amapLocation.getAoiName());
//					// String uri=json.getString("uri");
					double Altitude = amapLocation.getLatitude();
					System.out.println(Altitude);
					double Longitude = amapLocation.getLongitude();
//					amapLocation.getAoiName();
					System.out.println(Longitude);
					String url_location = LocalUtils.getLocationImage(
							String.valueOf(Longitude),
							String.valueOf(Altitude), 330, 220);
					System.out.println(url_location);
//					System.out.println(amapLocation.getCity()
//							+ amapLocation.getDistrict()
//							+ amapLocation.getStreet()
//							+ amapLocation.getAoiName());
					mLocationClient.stopLocation();
					mLocationClient.onDestroy();

					mapListener.onLocationChanged(amapLocation);
				}
			}
		};

		mLocationClient.setLocationListener(mLocationListener);
	}

	
	public static Animation rotateImageAnimation(Activity activity){
		Animation operatingAnim = AnimationUtils.loadAnimation(activity, R.anim.image_rotate);  
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		operatingAnim.setInterpolator(linearInterpolator);
		return operatingAnim;
	}
	
	public static void EmojiSpane(Activity activity, String uri,EditText editText,String source) {
		ImageSize targetImageSize = new ImageSize(50, 50); 
		Bitmap bitmap=ImageLoader.getInstance().loadImageSync(uri, targetImageSize);
		ImageSpan imageSpan = new ImageSpan(activity,bitmap,ImageSpan.ALIGN_BOTTOM);
		SpannableString spannableString = new SpannableString(String.format("[%s]", source)); // image 是图片名称的前缀
		spannableString.setSpan(imageSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		editText.append(spannableString);
	}

	public static void setUpLocationStyle(AMap aMap) {
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.ic_maps_indicator_search_position));
		myLocationStyle.strokeWidth(3);
		myLocationStyle.strokeColor(0x70d3fc);
		myLocationStyle.radiusFillColor(Color.Transparent);
		aMap.setMyLocationStyle(myLocationStyle);
	}

	public static void AsyncSingleThread(final LocalSingleThreadListener threadListener) {
		final ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);
		singleThreadExecutor.execute(new Runnable() {
			public void run() {
				try {
					threadListener.doTaskExecutor();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					singleThreadExecutor.shutdown();
				}
			}
		});
	}

	public static DisplayImageOptions getCircleOptions() {
		return GlobalParams.getInstance().circle_options;
	}

	public static String zerofill(int val) {
		return String.format("%05d", val);
	}

	/**
	 * 
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param width
	 * @param height
	 * @return
	 */
	public static String getLocationImage(String longitude, String latitude,
			int width, int height) {
//		http://restapi.amap.com/v3/staticmap?scale=2&location=113.91464,22.572049&zoom=19&size=330*220&markers=mid,,A:113.91464,22.572049&key=ee95e52bf08006f63fd29bcfbcf21df0
		String url = String
				.format("http://restapi.amap.com/v3/staticmap?scale=2&location=%s,%s&zoom=19&size=%d*%d&markers=mid,,A:%s,%s&key=ee95e52bf08006f63fd29bcfbcf21df0",
						longitude, latitude, width, height, longitude, latitude);
		return url;
	}

	public static String getLocationRegeo(String latitude, String longitude) {
		return String
				.format("http://restapi.amap.com/v3/geocode/regeo?output=json&location=%s,%s&key=ee95e52bf08006f63fd29bcfbcf21df0&radius=%d&extensions=all",
						longitude, latitude, 1000);
	}

	public static Location getLocation(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		String provider = LocationManager.GPS_PROVIDER;
		// 获取所有可用的位置提供器
		List<String> providers = locationManager.getProviders(true);
		// if(providers.contains(LocationManager.GPS_PROVIDER)){
		provider = LocationManager.GPS_PROVIDER;
		// }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
		// provider = LocationManager.NETWORK_PROVIDER;
		// }

		Location location = locationManager.getLastKnownLocation(provider);
		return location;
	}

	public static void sharedWXFriend(Activity activity, String message,
			String text, String url) {
		final String title = com.sharemob.tinchat.lib.common.Matrix.getApplicationName(activity);
		Bitmap bmp = BitmapFactory.decodeResource(
				WXConstant.context.getResources(), R.drawable.ic_launcher);
		Bitmap thumb = Bitmap.createScaledBitmap(bmp, 100, 100, true);
		bmp.recycle();
		final byte[] thumbData = FileUtils.Bitmap2Bytes(thumb);
		WXConstant.SharedWebviewToWX(url, title, message, WXConstant.WXFriend,
				thumbData);
	}

	public static void sharedWXCircle(Activity activity, String message,
			String text, String url) {
		final String title = com.sharemob.tinchat.lib.common.Matrix
				.getApplicationName(activity);
		Bitmap bmp = BitmapFactory.decodeResource(
				WXConstant.context.getResources(), R.drawable.ic_launcher);
		Bitmap thumb = Bitmap.createScaledBitmap(bmp, 100, 100, true);
		bmp.recycle();
		final byte[] thumbData = FileUtils.Bitmap2Bytes(thumb); // ICON
		WXConstant.SharedWebviewToWX(url, title, message, WXConstant.WXCircle,
				thumbData);
	}

	public static View viewFromInflater(Context context, int resource) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(resource, null);
		return view;
	}

	public static Bitmap getImageFromAssetsFile(Activity activity,
			String fileName) {
		return BitmapUtil.getImageFromAssetsFile(activity, fileName);
	}

	public static Bitmap roundedRectangleBitmap(int width, int height,
			Bitmap image, float outerRadiusRat) {
		return LocalUtils.roundedRectangleBitmap(width, height, image,
				outerRadiusRat);
	}

	public static String constellation(String birthday) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String constellation = "白羊座";
		try {
			Date date = formatter.parse(birthday);
			final Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTime(date);
			int month = mCalendar.get(Calendar.MONTH);
			int day = mCalendar.get(Calendar.DAY_OF_MONTH);
			System.out.println("month:"+(month+1)+"--day:"+day);
			constellation = SMGlobal.getConstellation(month+1, day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return constellation;
	}

	public static void filterImage(ImageView view, Bitmap bkg) {
		float radius = 5;
		float scaleFactor = 1.0f;
		Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(),
				bkg.getConfig());
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
				/ scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);
		overlay = FastBlur.doBlur(overlay, (int) radius, true);
		view.setImageBitmap(overlay);
	}

	public static void displayImage(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				GlobalParams.getInstance().circle_options);
	}

	public static void addFragmentManager() {

	}

	public static ReleaseImageLoadingListener AsyncLoadingImage(String uri,
			ImageView imageView) {
		ReleaseImageLoadingListener loadingListener = new ReleaseImageLoadingListener() {
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				ImageView imageView = (ImageView) view;
				imageView.setImageBitmap(loadedImage);
			}
		};
		return loadingListener;
	}

	public static void SMSSDK_sendMSM(String phone) {
		cn.smssdk.SMSSDK.getVerificationCode("86", phone);
	}

	public static void SMSSDK_initSDK(Activity activity, String AppKey,
			String AppSecret) {
		SMSSDK.initSDK(activity, AppKey, AppSecret, true);
	}

	public static void unregisterEventHandler(EventHandler eventHandler) {
		SMSSDK.unregisterEventHandler(eventHandler);
	}

	/**
	 * 发起https 请求
	 * 
	 * @param address
	 * @param m
	 * @return
	 */
	public static String requestData(String address, String params) {

		HttpURLConnection conn = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());

			// ip host verify
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return urlHostName.equals(session.getPeerHost());
				}
			};

			// set ip host verify
			HttpsURLConnection.setDefaultHostnameVerifier(hv);

			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// POST
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			// set params ;post params
			if (params != null) {
				conn.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(
						conn.getOutputStream());
				out.write(params.getBytes(Charset.forName("UTF-8")));
				out.flush();
				out.close();
			}
			conn.connect();
			// get result
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String result = getContent(conn.getInputStream());
				return result;
			} else {
				System.out.println(conn.getResponseCode() + " "
						+ conn.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}

	
	/**
	 * 输入流InputStream转化为字符串
	 * @param is
	 * @return
	 */
	public static String getContent(InputStream is) {
		String content = null;
		int length = 1024 * 1024;
		byte[] byteBuffer = new byte[length];
		int size = -1;
		try {
			size = is.read(byteBuffer);
			content = new String(byteBuffer, 0, size != -1 ? size : 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static String sendChatingItemText(int ChatType,String avatar,String text){
		JSONObject object=new JSONObject();
		try {
//			CacheManager.getInstance().userObject.get("avatar");
			object.put("type", ChatType);
			object.put("avatar", avatar);
			object.put("text", text);
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static EventHandler SMSSDK_EventHandler(final Handler handler) {
		EventHandler eh = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				// if (result == SMSSDK.RESULT_COMPLETE) {
				Message message = new Message();
				Bundle bundle = new Bundle();

				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					message.what = SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE;
					handler.sendMessage(message);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// if(result == SMSSDK.RESULT_COMPLETE) {
					// boolean smart = (Boolean)data;
					// if(smart) {
					// 通过智能验证
					message.what = SMSSDK.EVENT_GET_VERIFICATION_CODE;
					HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
					if (phoneMap.containsKey("country")) {
						String country = (String) phoneMap.get("country");
						bundle.putString("country", country);
					}
					if (phoneMap.containsKey("phone")) {
						String phone = (String) phoneMap.get("phone");
						bundle.putString("phone", phone);
					}
					message.setData(bundle);
					handler.sendMessage(message);

					// } else {
					// //依然走短信验证
					//
					// }
					// }
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					message.what = SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES;
					handler.sendMessage(message);
				}
				// }else{
				//
				// }
			}
		};
		return eh;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2 - 5;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2 - 5;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst_left + 15, dst_top + 15,
				dst_right - 20, dst_bottom - 20);

		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// RadialGradient gradient = new RadialGradient(j/2,k/2,j/2,new
		// int[]{0xff5d5d5d,0xff5d5d5d,0x00ffffff},new float[]{0.f,0.8f,1.0f},
		// Shader.TileMode.CLAMP);
		// paint.setShader(gradient);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static void setTextViewDrawableLeft(Activity activity,TextView textView, String fileName) {
		Bitmap bitmap = BitmapUtil.getImageFromAssetsFile(activity, fileName);
		Drawable drawable = LocalUtils.BitmapToDrawable(bitmap);
		drawable.setBounds(0, 0, 45, 45);
		textView.setCompoundDrawables(drawable, null, null, null);
	}
	
	public static void setTextViewBackgroundDrawable(Activity activity,TextView textView, String fileName){
		Bitmap bitmap = BitmapUtil.getImageFromAssetsFile(activity, fileName);
		Drawable drawable = LocalUtils.BitmapToDrawable(bitmap);
		textView.setBackgroundDrawable(drawable);
	}

	public static void setTextViewDrawableRight(Activity activity,
			TextView textView, String fileName) {
		Bitmap bitmap = BitmapUtil.getImageFromAssetsFile(activity, fileName);
		Drawable drawable = LocalUtils.BitmapToDrawable(bitmap);
		textView.setCompoundDrawables(null, null, drawable, null);
	}

	public static void setTextViewDrawableTop(Activity activity,
			TextView textView, String fileName) {
		Bitmap bitmap = BitmapUtil.getImageFromAssetsFile(activity, fileName);
		Drawable drawable = LocalUtils.BitmapToDrawable(bitmap);
		textView.setCompoundDrawables(null, drawable, null, null);
	}

	public static void setTextViewDrawableBottom(Activity activity,
			TextView textView, String fileName) {
		Bitmap bitmap = BitmapUtil.getImageFromAssetsFile(activity, fileName);
		Drawable drawable = LocalUtils.BitmapToDrawable(bitmap);
		textView.setCompoundDrawables(null, null, null, drawable);
	}

	public static MediaPlayer CreateMediaPlayer(String dataSourcePath,
			final LocalUtils.LocalMediaPlayerListener playerListener) {
		MediaPlayer mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(dataSourcePath);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					playerListener.onCompletion();
				}
			});
		} catch (IOException e) {
			Log.e("MediaPlayer", "prepare() failed");
		}
		return mPlayer;
	}

	public static MediaRecorder CreateMediaRecording(String outputFilePath) {
		System.out.println(outputFilePath);
		File file=new File(outputFilePath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		MediaRecorder mRecorder = new MediaRecorder();
		// mRecorder.setOutputFile(signatureVoiceFile.getAbsolutePath());
		mRecorder.setOutputFile(outputFilePath);
		mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("MediaRecorder", "prepare() failed");
		}
		mRecorder.start();
		
		return mRecorder;
	}

	public static Timer getTimerTask(final Handler handler, int interval) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
		}, 1000, interval);
		return timer;
	}

	public static Timer getTimerTask(final Handler handler, int interval,
			final int what) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(what);
			}
		}, 1000, interval);
		return timer;
	}

	public static View getLayoutDatePicker(Activity activity, View view,
			WheelView year, WheelView month, WheelView day, String datetime,
			OnWheelScrollListener scrollListener) {
		int mMonth = 0;
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curYear = 1996;
		int curMonth = mMonth + 1;
		year.setViewAdapter(new NumericWheelAdapter(activity, 1950, norYear));
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month.setViewAdapter(new NumericWheelAdapter(activity, 1, 12, "%02d"));
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day.setViewAdapter(new NumericWheelAdapter(activity, 1, LocalUtils.getDay(curYear, curMonth), "%02d"));
		// day.setLabel("日");
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
			Date ageDate = formate.parse(datetime);
			cal.setTime(ageDate);

			int ageYear = cal.get(Calendar.YEAR);
			year.setCurrentItem(ageYear - 1950);

			int ageMonth = cal.get(Calendar.MONTH);
			month.setCurrentItem(ageMonth);

			int ageDay = cal.get(Calendar.DATE) - 1;
			day.setCurrentItem(ageDay);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return view;
	}

	public static View getLayoutView(Activity activity, int resource) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(resource, null);
		return headerView;
	}

	public static AbsListView.LayoutParams getLayoutParams() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return lp;
	}

	public static byte[] readAll(InputStream input) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	/**
	 * 自定义字体
	 * 
	 * @param root
	 */
	public static void applyLocalFont(final View root) {
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyLocalFont(viewGroup.getChildAt(i));
			} else if (root instanceof TextView) {
				((TextView) root).setTypeface(GlobalParams.getInstance()
						.getLocalTypeface());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String   getVoiceChatingItemPath(){
		String Directory=String.format("%s/%s/%s",Environment.getExternalStorageDirectory(), "TinChat","voice");
		String voicePath = String.format("%s/%s.amr",Directory,System.currentTimeMillis());
		return  voicePath; 
	}
	
	
	public static void SendVoiceChatingItem(Activity activity,String time,String voicePath){
		if("0".equals(time.replace("\"", ""))){
			LocalUtils.ToastShort(activity,"录音时长不足一秒");
		}else{
			ArrayList<String> paths=new ArrayList<String>();
			paths.add(voicePath);
			String userid=CacheManager.getInstance().userInfo.uid;
			LocalUtils.UploadMultipartFile(activity,paths,userid,SMGlobal.UPLOAD_VOICE_Chating,SMGlobal.CHATING_VOICE_ACTION);

//			JSONObject object=new JSONObject();
//			try {
//				File file=new File(voicePath);
//				object.put("voice", file.getName());
//				object.put("type", SMGlobal.MsgType.Voice);
//				object.put("time", time);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			System.out.println(voicePath);
//			Intent intent=new Intent(SMGlobal.CHATING_VOICE_ACTION);
//			Bundle bundle=new Bundle();
//			bundle.putString(SMGlobal.VOICE_KEY, object.toString());
//			intent.putExtras(bundle);
//			activity.sendBroadcast(intent);
		}
	}

	public static void Thread_Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定已安装完整签名信息,包括MD5指纹
	 */
	public void getSingInfo(Context context, String packageName) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			parseSignature(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Drawable BitmapToDrawable(Bitmap bmp) {

		return ImageUtils.BitmapToDrawable(bmp);
	}

	public void parseSignature(byte[] signature) {
		try {
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(signature));
			String pubKey = cert.getPublicKey().toString();
			String signNumber = cert.getSerialNumber().toString();
			System.out.println("signName:" + cert.getSigAlgName());
			System.out.println("pubKey:" + pubKey);
			System.out.println("signNumber:" + signNumber);
			System.out.println("subjectDN:" + cert.getSubjectDN().toString());
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取APK的签名信息
	 * 
	 * @param apkPath
	 * @return
	 */
	private String showUninstallAPKSignatures(String apkPath) {
		String PATH_PackageParser = "android.content.pm.PackageParser";
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			Class pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// MediaApplication.logD(DownloadApk.class, "pkgParser:" +
			// pkgParser.toString());
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			// PackageParser.Package mPkgInfo = packageParser.parsePackage(new
			// File(apkPath), apkPath,
			// metrics, 0);
			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = PackageManager.GET_SIGNATURES;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);

			typeArgs = new Class[2];
			typeArgs[0] = pkgParserPkg.getClass();
			typeArgs[1] = Integer.TYPE;
			Method pkgParser_collectCertificatesMtd = pkgParserCls
					.getDeclaredMethod("collectCertificates", typeArgs);
			valueArgs = new Object[2];
			valueArgs[0] = pkgParserPkg;
			valueArgs[1] = PackageManager.GET_SIGNATURES;
			pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"mSignatures");
			Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
			// MediaApplication.logD(DownloadApk.class, "size:"+info.length);
			// MediaApplication.logD(DownloadApk.class,
			// info[0].toCharsString());
			return info[0].toCharsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void LocalAlertDialog(Activity activity) {
		new LocalAlertDialog(activity).builder().setTitle("确认手机号码")
				.setMsg("我们将发送验证短信到这个号码:+86 182-2364-7690")
				.setPositiveButton("确定", new OnClickListener() {
					public void onClick(View view) {
						System.out.println("------更新");
					}
				}).setNegativeButton("取消", new OnClickListener() {
					public void onClick(View view) {
						System.out.println("------稍后");
					}
				}).show();
	}
	
	public static void LocalAlertDialog(final Activity activity,String title,String msg) {
		new LocalAlertDialog(activity).builder().setTitle(title)
				.setMsg(msg)
				.setPositiveButton("确定", new OnClickListener() {
					public void onClick(View view) {
						activity.finish();
					}
				}).setNegativeButton("取消", new OnClickListener() {
					public void onClick(View view) {
						
					}
				}).show();
	}

	public static void LocalAlertDialog(final Activity activity,String title,String msg,final LocalAlertDialogListener listener) {
		new LocalAlertDialog(activity).builder().setTitle(title)
				.setMsg(msg)
				.setPositiveButton("确定", new OnClickListener() {
					public void onClick(View view) {
//						activity.finish();
						listener.confirm();
					}
				}).setNegativeButton("取消", new OnClickListener() {
					public void onClick(View view) {
						listener.cancel();
					}
				}).show();
	}
	public static void SelectPhotoDialog(String title, final Activity context,
			final int photo_flag, final int albume_flag) {
		new LocalSheetDialog(context)
				.builder()
				.setTitle("使用违规图片")
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("拍照", SheetItemColor.Gray,
						new OnSheetItemClickListener() {
							public void onClick(int which) {
								LocalUtils.photo(context, photo_flag);
							}
						})
				.addSheetItem("本地相册", SheetItemColor.Gray,
						new OnSheetItemClickListener() {
							public void onClick(int which) {
								Intent i = new Intent(Intent.ACTION_PICK,
										Media.EXTERNAL_CONTENT_URI);
								context.startActivityForResult(i, albume_flag);
							}
						}).show();
	}

	public static void setFlickerAnimation(ImageView iv_chat_head) {
		final Animation animation = new AlphaAnimation(1, 0);
		animation.setDuration(500);
		animation.setInterpolator(new LinearInterpolator());
		animation.setRepeatCount(Animation.INFINITE);
		animation.setRepeatMode(Animation.REVERSE);
		iv_chat_head.setAnimation(animation);
	}

	public static final String calculateDatePoor(String birthday) {
		try {
			if (TextUtils.isEmpty(birthday))
				return "0";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date birthdayDate = sdf.parse(birthday);
			String currTimeStr = sdf.format(new Date());
			Date currDate = sdf.parse(currTimeStr);
			if (birthdayDate.getTime() > currDate.getTime()) {
				return "0";
			}
			long age = (currDate.getTime() - birthdayDate.getTime())
					/(24*60*60*1000) + 1;
			String year = new DecimalFormat("0.00").format(age / 365f);
			if (TextUtils.isEmpty(year))
				return "0";
			return String.valueOf(new Double(year).intValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "0";
	}

	public static void cacheImage(String url, String path) {
		try {
			File file = new File(path);
			FileOutputStream out = new FileOutputStream(file);

			URL myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			byte buff[] = new byte[1024];
			int len = 0;
			while ((len = is.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
			is.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
		}
		return bitmap;

	}

	public static Bitmap convertBitMap(String _url) {

		URL url = null;
		try {
			url = new URL(_url);
			Bitmap pngBM = BitmapFactory.decodeStream(url.openStream());

			return pngBM;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// File cache = new File(Environment.getExternalStorageDirectory(),
		// "TinChat");
		return null;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(byte[] data) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		// 创建新的图片
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	public static final String getVerificationCode() {
		String[] beforeShuffle = new String[] { "0", "1", "2", "3", "4", "5","6", "7", "8", "9" };
		List<String> list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(5, 9);
		System.out.print(result);
		return result;
	}

	public static Fragment enterAppFragment(String fileName) {
		LuaFragment fragment = new LuaFragment();
		// Bundle bundle = new Bundle();
		// bundle.putString(LuaFragment.NAME,fileName);
		// fragment.setArguments(bundle);
		System.out.println("------" + fileName);
		return fragment;
	}

	public static Location getLocation() {
		Context context = SMGlobal.getInstance().context;
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			return location;
		} else {
			LocationListener locationListener = new LocationListener() {
				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				public void onStatusChanged(String provider, int status,Bundle extras) {

				}

				// Provider被enable时触发此函数，比如GPS被打开
				public void onProviderEnabled(String provider) {

				}

				// Provider被disable时触发此函数，比如GPS被关闭
				public void onProviderDisabled(String provider) {

				}

				// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				public void onLocationChanged(Location location) {
					if (location != null) {
						Log.e("Map","Location changed : Lat: "
										+ location.getLatitude() + " Lng: "
										+ location.getLongitude());
					}
				}
			};
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			return location;
		}
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 * 显示Toast消息
	 * 
	 * @param msg
	 */
	public static void showToast(String msg) {
		Toast mToast = Toast.makeText(SMGlobal.getInstance().context, msg,
				Toast.LENGTH_LONG);
		mToast.setText(msg);
		mToast.show();
	}

	public static void ToastShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static Uri getCropPhotoFilePath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"TinChat/tinchat_camera_temp.jpg");
		Uri imageUri = Uri.fromFile(file);
		return imageUri;
	}

	public static Uri getLiveCoverFilePath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"TinChat/tinchat_live_cover.jpg");
		Uri imageUri = Uri.fromFile(file);
		return imageUri;
	}

//	public static String getVoiceFilePath() {
//		String voicePath = String.format("%s/%s/%s/signatureVoice.amr",
//				Environment.getExternalStorageDirectory(), "TinChat","voice");
//		return voicePath;
//	}

	public static String SignatureVoiceFilePath=String.format("%s/TinChat/voice/signatureVoice.amr",Environment.getExternalStorageDirectory());
	public static String SignatureVideoFilePath=String.format("%s/TinChat/video/signatureVideo.mp4",Environment.getExternalStorageDirectory());

	public static String getLiveCoverTempFilePath() {
		String coverPath = String.format("%s/%s/%s.png",
				Environment.getExternalStorageDirectory(), "TinChat",
				String.valueOf(System.currentTimeMillis()));
		return coverPath;
	}

	public Bitmap getVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

//	public static String getVideoFilePath() {
//		String videoPath = String.format("%s/%s/tc_video.mp4",
//				Environment.getExternalStorageDirectory(), "TinChat");
//		return videoPath;
//	}

	public static String readStream(InputStream is) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while (i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
		} catch (IOException e) {
			Log.e("ReadStream", "读取文件流失败");
			return "";
		}
	}

	public static void selectedInfo(Context context, String title,
			List<String> list, final OnSheetItemClickListener listener) {

		LocalSheetDialog localSheetDialog = new LocalSheetDialog(context);
		localSheetDialog.builder();
		if (title == null || "".equals(title)) {
			// localSheetDialog.builder().setTitle(title).setCancelable(false)
			// .setCanceledOnTouchOutside(false);
		} else {
			localSheetDialog.setTitle(title).setCancelable(false)
					.setCanceledOnTouchOutside(false);
		}

		for (String itemName : list) {
			localSheetDialog.addSheetItem(itemName, SheetItemColor.Gray,
					new OnSheetItemClickListener() {
						public void onClick(int which) {
							listener.onClick(which);
						}
					});
		}

		localSheetDialog.show();
	}

	public static void DoubleWheelDaialog(final Context context, String title,
			AbstractWheelTextAdapter firstAdapter,
			AbstractWheelTextAdapter secondAdapter, final Handler handler,
			final int key) {
		LocalWheelDialog dialog = new LocalWheelDialog(context).builder();
		dialog.setTitle(title);
		final Map<String, Object> map = new HashMap<String, Object>();
		final String KEY_First_Item_Name = "First_Item_name";
		final String KEY_First_Item_Index = "First_Item_Index";
		final String KEY_Seconde_Item_Index = "Seconde_Item_Index";
		final String KEY_Seconde_Item_Name = "Seconde_Item_Name";

		map.put(KEY_First_Item_Index, 0);
		map.put(KEY_Seconde_Item_Index, 0);

		// 第一个轮询
		final WheelView wv_first = dialog.getFirstWheelView();
		wv_first.setViewAdapter(firstAdapter);

		// 第二个轮询
		final WheelView wv_second = dialog.getSecondWheelView();
		wv_second.setViewAdapter(secondAdapter);

		OnWheelScrollListener first_listener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {

			}

			public void onScrollingFinished(WheelView wheel) {
				int indexItem = wheel.getCurrentItem();
				String ItemName = wheel.getTextItem(indexItem);
				map.put(KEY_First_Item_Name, ItemName);
				map.put(KEY_First_Item_Index, indexItem);
				wv_second.setCurrentItem(indexItem);
			}
		};

		OnWheelScrollListener second_listener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				// int
				// Item_Index=Integer.parseInt(map.get(KEY_First_Item_Index).toString());
				// wheel.setCurrentItem(Item_Index);
			}

			public void onScrollingFinished(WheelView wheel) {
				int indexItem = wheel.getCurrentItem();
				String ItemName = wheel.getTextItem(indexItem);
				map.put(KEY_Seconde_Item_Index, indexItem);
				map.put(KEY_Seconde_Item_Name, ItemName);
			}
		};

		dialog.setScrollListener(first_listener, second_listener);

		dialog.setNegativeButton("取消", new OnClickListener() {
			public void onClick(View v) {
			}
		}).setPositiveButton("确定", new OnClickListener() {
			public void onClick(View v) {
				if (map.containsKey(KEY_First_Item_Index)
						&& map.containsKey(KEY_Seconde_Item_Index)) {
					int First_Item_Index = Integer.parseInt(map.get(
							KEY_First_Item_Index).toString());
					int Seconde_Item_Index = Integer.parseInt(map.get(
							KEY_Seconde_Item_Index).toString());
					if (Seconde_Item_Index < First_Item_Index) {
						Toast.makeText(context, "你输入的身高范围不正确",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Message msg = new Message();
					msg.what = key;
					Bundle bundle = new Bundle();
					String first_ItemName = wv_first
							.getTextItem(First_Item_Index);
					String second_ItemName = wv_first
							.getTextItem(Seconde_Item_Index);

					bundle.putString("KEY", String.format("%s-%s",
							first_ItemName, second_ItemName));
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
		}).show();
	}

	public static void SingleWheelDaialog(Context context, String title,
			AbstractWheelTextAdapter adapter, final Handler handler,
			final int key) {
		LocalWheelDialog dialog = new LocalWheelDialog(context).builder();
		dialog.setTitle(title);
		final Map<String, Object> map = new HashMap<String, Object>();
		final String KEY = "WHEELDAIALOG_KEY";
		OnWheelScrollListener listener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
			}

			public void onScrollingFinished(WheelView wheel) {
				int indexItem = wheel.getCurrentItem();
				String ItemName = wheel.getTextItem(indexItem);
				map.put(KEY, ItemName);
			}
		};
		WheelView wv_first = dialog.getFirstWheelView();
		wv_first.setViewAdapter(adapter);
		dialog.setScrollListener(listener, null);
		dialog.setNegativeButton("取消", new OnClickListener() {
			public void onClick(View v) {
			}
		}).setPositiveButton("确定", new OnClickListener() {
			public void onClick(View v) {
				if (map.containsKey(KEY)) {
					Message msg = new Message();
					msg.what = key;
					Bundle bundle = new Bundle();
					String value = map.get(KEY).toString();
					bundle.putString("KEY", value);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
		}).show();
	}
	
	public static void SingleWheelDaialog(final Activity activity, String title,
			String[] items,final String item_key) {
		AbstractWheelTextAdapter adapter=new ArrayWheelAdapter<String>(activity, items);
		
		LocalWheelDialog dialog = new LocalWheelDialog(activity).builder();
		dialog.setTitle(title);
		final Map<String, Object> map = new HashMap<String, Object>();
		final String KEY = "WHEELDAIALOG_KEY";
		OnWheelScrollListener listener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
			}

			public void onScrollingFinished(WheelView wheel) {
				int indexItem = wheel.getCurrentItem();
				String ItemName = wheel.getTextItem(indexItem);
				map.put(KEY, ItemName);
			}
		};
		WheelView wv_first = dialog.getFirstWheelView();
		wv_first.setViewAdapter(adapter);
		dialog.setScrollListener(listener, null);
		dialog.setNegativeButton("取消", new OnClickListener() {
			public void onClick(View v) {
			}
		}).setPositiveButton("确定", new OnClickListener() {
			public void onClick(View v) {
				if (map.containsKey(KEY)) {
//					Message msg = new Message();
//					msg.what = key;
//					Bundle bundle = new Bundle();
					String item_value = map.get(KEY).toString();
//					bundle.putString("KEY", value);
//					msg.setData(bundle);
//					handler.sendMessage(msg);
					System.out.println(item_value);
					Intent intent =new Intent();
					intent.setAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO);
					intent.putExtra(SMGlobal.BUNDLE_ITEM_KEY,item_key);
					intent.putExtra(SMGlobal.BUNDLE_ITEM_VALUE, item_value);
					activity.sendBroadcast(intent);
				}
			}
		}).show();
	}
	
	public static void OptionLocation(final Activity activity,String[] items,final String item_key){
		ChangeAddressDialog mChangeAddressDialog = new ChangeAddressDialog(activity);
		mChangeAddressDialog.setAddress(items[0], items[1],items[2]);
		mChangeAddressDialog.show();
		mChangeAddressDialog.setAddresskListener(new OnAddressCListener() {
					public void onClick(String province, String city,String area) {
						String nactive_place=String.format("%s省-%s市-%s", province,city,area);
						Intent intent =new Intent();
						intent.setAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO);
						intent.putExtra(SMGlobal.BUNDLE_ITEM_KEY,item_key);
						intent.putExtra(SMGlobal.BUNDLE_ITEM_VALUE, nactive_place);
						activity.sendBroadcast(intent);
					}
			});
	}

	public static void selectedPhotoBox(Context context,
			final OnSheetItemClickListener listener) {
		new LocalSheetDialog(context)
				.builder()
				.setTitle("🈲使用违规图片")
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("拍照", SheetItemColor.Gray,
						new OnSheetItemClickListener() {
							public void onClick(int which) {
								listener.onClick(which);
							}
						})
				.addSheetItem("本地相册", SheetItemColor.Gray,
						new OnSheetItemClickListener() {
							public void onClick(int which) {
								listener.onClick(which);
							}
						}).show();
	}

	public static void photo(Activity activity, int flag) {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = Environment.getExternalStorageDirectory()
					.getPath() + "/tempImage/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".png");
			}
			if (file != null) {
				SMGlobal.PICTURE_PATH = file.getPath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				activity.startActivityForResult(intent, flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromUri(Context activity, String uriString) {
		Uri uri = Uri.parse(uriString);
		System.out.println(uriString);
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					activity.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	public static Bitmap getBitmap(String url, int w, int h) {
		return convertToBitmap(url, w, h);
	}

	public static String getDateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new java.util.Date());
	}

	public static void startPhotoZoom(Activity activity, List<String> drr,
			Uri uri, int flag) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String address = sDateFormat.format(new java.util.Date());
		String sdPath = Environment.getExternalStorageDirectory()
				+ File.separator + "TinChat/" + address + ".JPEG";
		String uriString = new String("file://" + sdPath);
		SMGlobal.EXTRA_OUTPUT_PATH = uriString;

		Uri imageUri = Uri.parse(uriString);
		final Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 480);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false);
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, flag);
	}

//	public static void onItemClick(final Activity activity, List<Bitmap> bmp,
//			AdapterView<?> arg0, View arg1, int arg2, long arg3, final int flag) {
//		((InputMethodManager) activity
//				.getSystemService(Context.INPUT_METHOD_SERVICE))
//				.hideSoftInputFromWindow(activity.getCurrentFocus()
//						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		if (arg2 == bmp.size()) {
//			String sdcardState = Environment.getExternalStorageState();
//			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
//				// new PopupWindows(PublishDynamicActivity.this, gridview);
//				LocalUtils.selectedPhotoBox(activity,
//						new OnSheetItemClickListener() {
//							public void onClick(int which) {
//								switch (which) {
//								case 1:
//									System.out.println("--拍照---");
//									LocalUtils.photo(activity, flag);
//									break;
//								case 2:
//									System.out.println("--本地相册---");
//									Intent i = new Intent(
//											Intent.ACTION_PICK,
//											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//									activity.startActivityForResult(i,
//											SMGlobal.RESULT_LOAD_IMAGE);
//									break;
//								default:
//									break;
//								}
//							}
//						});
//
//			} else {
//				Toast.makeText(activity, "sdcard已拔出，不能选择照片", Toast.LENGTH_SHORT)
//						.show();
//			}
//		} else {
//			Intent intent = new Intent(activity,
//					SelectBrowsePhotoActivity.class);
//			intent.putExtra("ID", arg2);
//			activity.startActivity(intent);
//		}
//	}

	public static void exitView(final Activity activity, int id,
			final int animId, final int exitAnim) {
		View view = activity.findViewById(id);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.finish();
				// activity.overridePendingTransition(exitAnim, animId);
			}
		});
	}

	public static void gotoView(Activity activity, Class<?> clazz) {
		Intent intent = new Intent();
		intent.setClass(activity, clazz);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.in_from_right,
				R.anim.out_to_left);
	}

//	public static void enterFragmentActivity(Activity activity,
//			String parameter, int enterAnim, int exitAnim, boolean isfinish) {
//		Intent intent = new Intent();
//		intent.putExtra(AppFragmentActivity.NAME, parameter);
//		try {
//			Class<?> clazz = Class.forName(AppFragmentActivity.class.getName());
//			intent.setClass(activity, clazz);
//			activity.startActivity(intent);
//			if (isfinish) {
//				activity.finish();
//			}
//			activity.overridePendingTransition(enterAnim, exitAnim);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	public static void sendSayHi(final Context context,int rid){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("sid", CacheManager.getInstance().userInfo.ID);
		map.put("rid", rid);
		map.put("msgId",SMGlobal.getUUID());
		map.put("msgType",SMGlobal.SEND_TEXT_SAYHI);
		
		Map<String,Object> textMap=new HashMap<String,Object>();
		textMap.put("type", SMGlobal.MsgType.Text);
		textMap.put("sendTime",LocalUtils.getDateTime());
		textMap.put("content",String.format("%s,%s岁,%s",
				CacheManager.getInstance().userInfo.nickname,
				CacheManager.getInstance().userInfo.age,
				CacheManager.getInstance().userInfo.location));
		
		map.put("text", new JSONObject(textMap).toString());
		
		LocalUtils.requestHttp(10016,map,new RequestCallback() {
			@Override
			public void requestDidFinished(String body) {
				try {
					JSONObject object = new JSONObject(body);
					if(object.has("desc")){
						LocalUtils.ToastShort(context,object.getString("desc"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void requestDidFailed() {
				
			}
		});
	}
	public static void enterAppActivity(Activity activity,String activityName, String parameter) {
		try {
			Intent intent = new Intent();
			intent.putExtra(AppActivity.NAME, activityName);
			intent.putExtra(AppActivity.PARAMETERS, parameter);
			Class<?> clazz = Class.forName(AppActivity.class.getName());
			intent.setClass(activity, clazz);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void enterAppActivity(Activity activity, String parameter,
			int enterAnim, int exitAnim, boolean isfinish) {
		Intent intent = new Intent();
		intent.putExtra(AppActivity.NAME, parameter);
		try {
			Class<?> clazz = Class.forName(AppActivity.class.getName());
			intent.setClass(activity, clazz);
			activity.startActivity(intent);
			if (isfinish) {
				activity.finish();
			}
			activity.overridePendingTransition(enterAnim, exitAnim);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void gotoViewByAnim(Activity activity, String className,
			int enterAnim, int exitAnim, boolean isfinish) {
		Intent intent = new Intent();
		try {
			Class<?> clazz = Class.forName(className);
			intent.setClass(activity, clazz);
			activity.startActivity(intent);
			if (isfinish) {
				activity.finish();
			}
			activity.overridePendingTransition(enterAnim, exitAnim);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void BtnEvent(Activity activity, int id,
			final Callback callback) {
		View view = activity.findViewById(id);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				callback.doDown();
			}
		});
	}

	public static void setListViewHeight(ListView listView,
			BaseAdapter listAdapter) {
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(1, 1);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ listView.getPaddingTop() + listView.getPaddingBottom();
		listView.setLayoutParams(params);
	}

	public static void imageBrower(Context context, int position,
			ArrayList<CharSequence> urls) {
		ImageUtils.imageBrower(context, position, urls);
	}

	public static void setListViewHeight(GridView gridView,
			BaseAdapter listAdapter) {
		// NoScrollGridAdapter listAdapter = (NoScrollGridAdapter)
		// gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(1, 1);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight
				+ (gridView.getHeight() * (listAdapter.getCount() - 1))
				+ gridView.getPaddingTop() + gridView.getPaddingBottom();
		gridView.setLayoutParams(params);
	}

	public static void localAlertDialog(Context context, String[] args,
			OnClickListener positivelistener, OnClickListener negativelistener) {
		new LocalAlertDialog(context).builder().setTitle(args[0])
				.setMsg(args[1]).setPositiveButton(args[2], positivelistener)
				.setNegativeButton(args[3], negativelistener).show();
	}

	public static TextView getFindViewById(View convertView, int id) {
		TextView tv = (TextView) convertView.findViewById(id);
		tv.setTypeface(GlobalParams.getInstance().localFontTypeface);
		return tv;
	}

	public static void setTitleName(Activity activity, int id, String tv_name) {
		TextView tv = (TextView) activity.findViewById(id);
		tv.setTypeface(GlobalParams.getInstance().localFontTypeface);
		tv.setText(tv_name);
	}

	public static void toastShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void displayFromContent(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage("file://" + uri, imageView);
	}

	public static Map<String, String> formatDateTime(String datetime) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		String[] months = new String[] { "一月", "二月", "三月", "四月", "五月", "六月",
				"七月", "八月", "九月", "十月", "十一月", "十二月" };
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date = format.parse(datetime);
			// 获取星期几
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (week_index < 0) {
				week_index = 0;
			}
			int year = cal.get(Calendar.YEAR);
			map.put("year", String.valueOf(year));
			System.out.println(map.get("year"));

			String week = weekDays[week_index];
			map.put("week", week);
			System.out.println(map.get("week"));
			// 获取月份
			int month = (cal.get(Calendar.MONTH)) + 1;
			map.put("month", String.valueOf(months[month - 1]));
			System.out.println(map.get("month"));
			// 获取天数
			int days = cal.get(Calendar.DATE);
			map.put("day",
					days < 10 ? String.format("0%d", days) : String
							.valueOf(days));
			System.out.println(map.get("day"));
			// 获取时间
			map.put("time",
					String.format("%s:%s", date.getHours(), date.getMinutes()));
			System.out.println(map.get("time"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String getBirthday(int year, int month, int day) {
		String birthday = new StringBuilder().append((year + 1950)).append("-")
				.append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
				.append("-")
				.append(((day + 1) < 10) ? "0" + (day + 1) : (day + 1))
				.toString();
		return birthday;
	}

	public static String getConstellation(int month, int day) {
		return SMGlobal.getConstellation(month, day);
	}

	public static String getFormatDateTime(String datetime) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		String[] months = new String[] { "一月", "二月", "三月", "四月", "五月", "六月",
				"七月", "八月", "九月", "十月", "十一月", "十二月" };
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date = format.parse(datetime);
			// 获取星期几
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (week_index < 0) {
				week_index = 0;
			}
			int year = cal.get(Calendar.YEAR);
			map.put("year", String.valueOf(year));
			System.out.println(map.get("year"));

			String week = weekDays[week_index];
			map.put("week", week);
			System.out.println(map.get("week"));
			// 获取月份
			int month = (cal.get(Calendar.MONTH)) + 1;
			map.put("month", String.valueOf(months[month - 1]));
			System.out.println(map.get("month"));
			// 获取天数
			int days = cal.get(Calendar.DATE);
			map.put("day",
					days < 10 ? String.format("0%d", days) : String
							.valueOf(days));
			System.out.println(map.get("day"));
			// 获取时间
			map.put("time",
					String.format("%s:%s", date.getHours(), date.getMinutes()));
			System.out.println(map.get("time"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new JSONObject(map).toString();
	}

	public static String getIntervalFormat(String beginTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(beginTime, pos);
		long date = strtodate.getTime();
		long time = Math.abs(new Date().getTime() - date);
		int interval=(int) (time/(1000*60));
		if(interval<60){
			return String.format("%s分钟前",String.valueOf(interval) );
		}else if(interval>60&interval<60*24){
			return String.format("%s小时前", interval/60);
		}else if(interval>60*24&interval<60*24*30){
			return String.format("%s天前", interval/(60*24));
		}else if(interval>60*24*30){
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			return  formater.format(strtodate);
		}
		 return "公元前";
	}

	public static String getDateFromLong(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(time * 1000);
		String sDateTime = format.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
		return format.format(dt);
	}

	public static void initILoadingLayout(PullToRefreshListView pullToRefresh) {
		String LastUpdatedLabel = LocalUtils.getDateTime();
		// 刚下拉时，显示的提示
		ILoadingLayout startLabels = pullToRefresh.getLoadingLayoutProxy(true,
				false);
		startLabels.setLastUpdatedLabel(String.format("最后刷新时间:%s",
				LastUpdatedLabel));
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在努力加载");// 刷新时
		startLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示
		// 刚下拉时，显示的提示
		ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false,
				true);
		endLabels.setLastUpdatedLabel(String.format("最后刷新时间:%s",
				LastUpdatedLabel));
		endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在努力加载");// 刷新时
		endLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示
	}

//	public static void releaseXinQing(Activity activity) {
//		int enterAnim = R.anim.in_from_right;
//		int exitAnim = R.anim.out_to_left;
//		Class<?> clazz = ReleaseDynamicActivity.class;
//		Intent intent = new Intent();
//		intent.setClass(activity, clazz);
//		activity.startActivity(intent);
//		activity.overridePendingTransition(enterAnim, exitAnim);
//	}

//	public static void finish(Activity activity) {
//		activity.finish();
//	}

	public static void setDrawableLeft(Activity activity, TextView view,
			Drawable drawable, int width, int height) {
		drawable.setBounds(0, 0, width, height);
		view.setCompoundDrawables(drawable, null, null, null);
	}

	public static void setDrawableTop(Activity activity, TextView view,
			Drawable drawable, int width, int height) {
		drawable.setBounds(0, 0, width, height);
		view.setCompoundDrawables(null, drawable, null, null);
	}

	public static void loadSplashImage(Activity activity, LinearLayout layout,
			String url) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = (int) (metrics.heightPixels * 0.85f);

		// LinearLayout group = (LinearLayout) activity.findViewById(id);
		ImageView imageView = new ImageView(activity);
		imageView.setLayoutParams(new LayoutParams(width, height));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		ImageUtils.loaderImageForCache(url, imageView, false);

		layout.addView(imageView);
	}
	public static void exit(Activity activity) {
		 MyApplication.getInstance().exit();
		 android.os.Process.killProcess(android.os.Process.myPid());
		 System.exit(0);
         ActivityManager manager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
         manager.killBackgroundProcesses(activity.getPackageName());
	}
	public static void setGridView(Activity activity, GridView listView,
			ArrayList<CharSequence> urlList) {
		final int MaxLength_GridView = 100;
		int size = urlList.size();
		if (size == 0) {
			listView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.VISIBLE);
		}
		int length = MaxLength_GridView;
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		listView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		listView.setColumnWidth(itemWidth); // 设置列表项宽
		listView.setHorizontalSpacing(5); // 设置列表项水平间距
		listView.setStretchMode(GridView.NO_STRETCH);
		listView.setNumColumns(size); // 设置列数量=列表集合数
	}
	
	
	/**
	  * @author jerry.chen
	  * @param brithday
	  * @return
	  * @throws ParseException
	  *             根据生日获取年龄;
	  */
	 public static int getCurrentAgeByBirthdate(String brithday)
	   throws ParseException, Exception {
	  try {
	   Calendar calendar = Calendar.getInstance();
	   SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	   String currentTime = formatDate.format(calendar.getTime());
	   Date today = formatDate.parse(currentTime);
	   Date brithDay = formatDate.parse(brithday);
	 
	   return today.getYear() - brithDay.getYear();
	  } catch (Exception e) {
	   return 0;
	  }
	 }
}
