package com.sharemob.tinchat.lib.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AlertDialog;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class ChatingItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private enum ContactWay {
		WeiXin, Telephone
	}

	private AppActivity activity;
	private List<View> chating_layout=new ArrayList<View>();
	private Map<String,Object> chatingObject=new HashMap<String, Object>();
	private List<String> list = new LinkedList<String>();
	
	public ChatingItemAdapter(AppActivity activity) {
		this.activity = activity;
	}
	
	public void addView(View view){
		chating_layout.add(view);
	}
	
	public void openWSConnection(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("msgtype", SMGlobal.MsgType.Online);
		map.put("ruid",chatingObject.get("uid"));
		map.put("suid",CacheManager.getInstance().userInfo.uid);
	}
	
	public void addRevObjectInfo(String key,Object value){
		chatingObject.put(key, value);
	}
	
	public Map<String, Object> sendMessageItem(String text, int msgType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sid", Integer.valueOf(CacheManager.getInstance().userInfo.ID));
		map.put("suid", CacheManager.getInstance().userInfo.uid);
		map.put("ruid", chatingObject.get("uid"));
		map.put("rid", chatingObject.get("id"));
		map.put("msgId", SMGlobal.getUUID());
		map.put("text", text);
		map.put("sendRev", Integer.valueOf(0x0));
		map.put("msgType", Integer.valueOf(msgType));
		return map;
	}
	
	public void sendText(String text){
		HashMap<String, Object> content = new HashMap<String, Object>();
		content.put("type", Integer.valueOf(0x0));
		content.put("sendTime", LocalUtils.getDateTime());
		content.put("content", text);
		JSONObject content_text = new JSONObject(content);
		Map<String, Object> map = sendMessageItem(content_text.toString(), SMGlobal.MsgType.Text);
		JSONObject entity = new JSONObject(map);
		addItem(entity.toString());
		websocketSend(entity.toString());
	}
	
	public void websocketSend(String json){
		  String url = String.format("http://%s/api/user?cmd=%d&data=%s", CacheManager.IP, 0x2720, json);
	        CacheManager.requestHttp(url, new AsyncHttpRequest.RequestCallback() {
	            public void requestDidFinished(String body) {
	            	
	            }
	            public void requestDidFailed(){}
	        });
	}
	
	public void sendImage(String locaFilename,String uploadFilename){
		HashMap<String, Object> image = new HashMap<String, Object>();
        image.put("type", Integer.valueOf(0x1));
        image.put("sendTime", LocalUtils.getDateTime());
        image.put("content", uploadFilename);
        JSONObject image_text = new JSONObject(image);
        Map<String, Object> map = sendMessageItem(image_text.toString(), SMGlobal.MsgType.Location);
        JSONObject entity = new JSONObject(map);
        addItem(entity.toString());
        websocketSend(entity.toString());
	}
	
	 public void sendLocation(String json) {
	        try {
	            JSONObject object = new JSONObject(json);
	            object.put("sendTime", LocalUtils.getDateTime());
	            Map<String, Object> map = sendMessageItem(object.toString(), SMGlobal.MsgType.Location);
	            JSONObject entity = new JSONObject(map);
	            addItem(entity.toString());
	            websocketSend(entity.toString());
	            return;
	        } catch(JSONException e) {
	            e.printStackTrace();
	        }
	}
	 
	 public void sendVoice(String json) {
	        try {
	            JSONObject object = new JSONObject(json);
	            object.put("sendTime", LocalUtils.getDateTime());
	            Map<String, Object> map = sendMessageItem(json,SMGlobal.MsgType.Voice);
	            JSONObject entity = new JSONObject(map);
	            addItem(entity.toString());
	            websocketSend(entity.toString());
	            return;
	        } catch(JSONException e) {
	            e.printStackTrace();
	        }
	 }
	
	@Override
	public int getItemCount() {
		return list.size();
	}


    public void daleteData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position);
    }
	
	public void daleteData(int cmd, int position, String msgId) {
	    daleteData(position);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("msgId", msgId);
        LocalUtils.requestHttp(cmd, map, new AsyncHttpRequest.RequestCallback() {
            public void requestDidFinished(String body) {}
            public void requestDidFailed() {}
        });
	}
	public int getItemViewType(int position) {
		if(position<list.size()){
			try {
				JSONObject object = new JSONObject(list.get(position));
				int type = object.getInt("msgType");
				int sendRev=Integer.parseInt(object.getString("sendRev"));
				switch (type) {
				case  SMGlobal.SEND_TEXT_SAYHI:
				{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Text;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Text;
					}
				}
				case SMGlobal.MsgType.Text:{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Text;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Text;
					}
				}
				break;
				case SMGlobal.MsgType.Image:{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Image;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Image;
					}
				}
				case SMGlobal.MsgType.Voice:{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Voice;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Voice;
					}
				}
				case SMGlobal.MsgType.Video:{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Video;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Video;
					}
				}
				case SMGlobal.MsgType.Location:{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Location;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Location;
					}
				}
				case SMGlobal.MsgType.Contactway:{
					if(sendRev==SMGlobal.ChatSide.SEND){
						return SMGlobal.Chat.Send_Contactway;
					}else if(sendRev==SMGlobal.ChatSide.REV){
						return SMGlobal.Chat.Receive_Contactway;
					}
				}
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return super.getItemViewType(position);
	}
	public void itemRangeRemoved(int positionStart, int itemCount) {
		for (int i = positionStart; i < itemCount; i++) {
			list.remove(positionStart);
		}
		notifyItemRangeRemoved(positionStart, itemCount);
	}

	public void addItem(String json) {
		this.list.add(json);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}
	
	public ChatingItemAdapter setAdapter(LinkedList<String> list) {
		this.list = list;
		return this;
	}
	
	public void setArray(String json) {
//		this.list.clear();
		try {
			JSONArray array = new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				this.list.add(object.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void removeItem(int index) {
		list.remove(index);
		notifyDataSetChanged();
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;
		switch (viewType) {
		case SMGlobal.Chat.Send_Text:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_right_text, parent, false);
			break;
		case SMGlobal.Chat.Send_Image:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_right_photo, parent, false);
			break;
		case SMGlobal.Chat.Send_Location:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_right_location, parent, false);
			break;
		case SMGlobal.Chat.Send_Voice:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_right_voice, parent, false);
			break;
		case SMGlobal.Chat.Send_Video:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_right_video, parent, false);
			break;
		case SMGlobal.Chat.Send_Contactway:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_right_contactway, parent, false);
			break;
		case SMGlobal.Chat.Receive_Text:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_left_text, parent, false);
			break;
		case SMGlobal.Chat.Receive_Image:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_left_photo, parent, false);
			break;
		case SMGlobal.Chat.Receive_Location:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_left_location, parent, false);
			break;
		case SMGlobal.Chat.Receive_Voice:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_left_voice, parent, false);
			break;
		case SMGlobal.Chat.Receive_Video:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_left_video, parent, false);
			break;
		case SMGlobal.Chat.Receive_Contactway:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_left_contactway, parent, false);
			break;
		default:
			break;
		}
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				for(View view :chating_layout){
					activity.hideFaceToolAndInputFromWindow();
					view.setVisibility(view.GONE);
				}
			}
		});
		if(viewType==SMGlobal.Chat.Send_Text||viewType==SMGlobal.Chat.Receive_Text){
			return new TextViewHolder(view);
		}else if(viewType==SMGlobal.Chat.Send_Image||viewType==SMGlobal.Chat.Receive_Image){
			return new ImageViewHolder(view);
		}else if(viewType==SMGlobal.Chat.Send_Voice||viewType==SMGlobal.Chat.Receive_Voice){
			return new VoiceViewHolder(view);
		}else if(viewType==SMGlobal.Chat.Send_Video||viewType==SMGlobal.Chat.Receive_Video){
			return new VideoViewHolder(view);
		}else if(viewType==SMGlobal.Chat.Send_Location||viewType==SMGlobal.Chat.Receive_Location){
			return new LocationViewHolder(view);
		}else if(viewType==SMGlobal.Chat.Send_Contactway||viewType==SMGlobal.Chat.Receive_Contactway){
			return new ContactwayViewHolder(view);
		}
		
		return null;
	}

	public void onBindViewHolderText(final RecyclerView.ViewHolder viewholder,final JSONObject json,final int position) throws JSONException {
		TextViewHolder  viewHolder=(TextViewHolder)viewholder;
		String content=null;
		try {
			JSONObject object=new JSONObject(json.getString("text"));
			content=URLDecoder.decode(object.getString("content"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		if (json.has("time")) {
//			String sendtime = json.getString("time");
//			viewHolder.chating_tv_sendtime.setVisibility(View.VISIBLE);
//			viewHolder.chating_tv_sendtime.setText(sendtime);
//		} else {
//			viewHolder.chating_tv_sendtime.setVisibility(View.GONE);
//		}
		viewHolder.chating_tv_content.setVisibility(View.VISIBLE);
		
		SpannableString spannableString = new SpannableString(content);
		Pattern sinaPatten = Pattern.compile("\\[[^\\]]+\\]", Pattern.CASE_INSENSITIVE);
		Matcher matcher = sinaPatten.matcher(content);
		ImageSize targetImageSize = new ImageSize(50, 50); 
		while (matcher.find()) {
			String key = matcher.group();
			String source=key.substring(key.indexOf("[")+1, key.lastIndexOf("]"));
			String uri=new String("assets://emojis/emoji_"+ source+".png");
			Bitmap bitmap=ImageLoader.getInstance().loadImageSync(uri, targetImageSize);
			ImageSpan imageSpan = new ImageSpan(activity, bitmap);
			int end = matcher.start() + key.length();
			spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		
		viewHolder.chating_tv_content.setText(spannableString);
		String avatar_url=null;
		if(json.getString("avatar").startsWith("http://")){
			avatar_url =json.getString("avatar");
		}else{
			avatar_url =String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,json.getString("avatar"));
		}
		
		 
		 ImageLoader.getInstance().displayImage(avatar_url,
					viewHolder.chating_avatar,
					GlobalParams.getInstance().round_options);
		
		viewHolder.chating_tv_content.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
						try {
							deleteDialog(position, json.getString("msgId"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
				return false;
			}
		});
	}

	public void onBindViewHolderPhoto(final RecyclerView.ViewHolder viewholder,
		final JSONObject json,final int position) throws JSONException {
		ImageViewHolder viewHolder=(ImageViewHolder)viewholder;
		if(json.has("avatar")){
			String avatar_url=null;
			if(json.getString("avatar").startsWith("http://")){
				avatar_url =json.getString("avatar");
			}else{
				avatar_url =String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,json.getString("avatar"));
			}
			ImageLoader.getInstance().displayImage(avatar_url,
					viewHolder.chating_avatar,
					GlobalParams.getInstance().round_options);
		}
		
		String content=null;
		try {
			JSONObject object=new JSONObject(json.getString("text"));
			content=URLDecoder.decode(object.getString("content"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String photo_url =String.format("%s%s", CacheManager.getInstance().userInfo.server_addr,content) ;
//		System.out.println("---"+photo_url);
		final ArrayList<CharSequence> urls = new ArrayList<CharSequence>();
		urls.add(photo_url);
		viewHolder.chating_iv_image.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().displayImage(photo_url,
				viewHolder.chating_iv_image,
				GlobalParams.getInstance().round_options);
		
		viewHolder.chating_iv_image.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImageUtils.imageBrower(activity, 0, urls);
			}
		});

		viewHolder.chating_iv_image.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				try {
					deleteDialog(position, json.getString("msgId"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	public void deleteDialog(final int position,final String msgId){
		new AlertDialog(activity,new AlertDialog.OnDialogListener(){
			@Override
			public void delete() {
				daleteData(10010, position, msgId);
			}
		});
	}
	
	public void AsyncLoadingVoice(final ViewGroup view,final ImageView chating_btn_voice,final String voice_url){
		final AnimationDrawable AniDraw = (AnimationDrawable) chating_btn_voice.getBackground();
		 final ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);
			singleThreadExecutor.execute(new Runnable() {
				public void run() {
			      try {
			  		Uri uri = Uri.parse(voice_url);
			  		System.out.println("-------voice:"+uri.toString());
					final MediaPlayer player = MediaPlayer.create(activity, uri);
					view.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									if (player.isPlaying()) {
										player.pause();
										AniDraw.stop();
										chating_btn_voice.clearAnimation();
										AniDraw.selectDrawable(0);
										chating_btn_voice.setBackgroundResource(R.anim.play_voice_animation);
									} else {
										chating_btn_voice.setBackgroundResource(R.anim.play_voice_animation);
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
								}
							});
			      } catch (Exception e) {
			      		e.printStackTrace();
			      	}finally{
			      		singleThreadExecutor.shutdown();
			      	}
				}
			});
	}
	public void onBindViewHolderVoice(final RecyclerView.ViewHolder viewholder,final JSONObject json,final int position) throws JSONException {
		VoiceViewHolder viewHolder=(VoiceViewHolder)viewholder;
		if(json.has("avatar")){
			String avatar_url=null;
			if(json.getString("avatar").startsWith("http://")){
				avatar_url =json.getString("avatar");
			}else{
				avatar_url =String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,json.getString("avatar"));
			}
			ImageLoader.getInstance().displayImage(avatar_url,
					viewHolder.chating_avatar,
					GlobalParams.getInstance().round_options);
		}
		JSONObject object=new JSONObject(json.getString("text"));
		String voice_url =String.format("%s%s", CacheManager.getInstance().userInfo.server_addr.replace("img", "voice"),object.getString("voice")) ;
//		System.out.println(voice_url);
		viewHolder.chating_layout_voice.setVisibility(View.VISIBLE);
//		final String voice_url = json.getString("voice");
		String voice_time = object.getString("time");
		viewHolder.chating_layout_voice.setVisibility(View.VISIBLE);
		viewHolder.chating_btn_voice.setBackgroundResource(R.anim.play_voice_animation);

		AsyncLoadingVoice(viewHolder.chating_layout_voice,viewHolder.chating_btn_voice,voice_url);
		
		viewHolder.chating_voice_time.setText(voice_time);
		viewHolder.chating_layout_voice.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				try {
					deleteDialog(position, json.getString("msgId"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	public void onBindViewHolderLocation(final RecyclerView.ViewHolder viewholder,
			final JSONObject json,final int position) throws JSONException {
		final LocationViewHolder viewHolder=(LocationViewHolder)viewholder;
		
		if(json.has("avatar")){
			String avatar_url=null;
			if(json.getString("avatar").startsWith("http://")){
				avatar_url =json.getString("avatar");
			}else{
				avatar_url =String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,json.getString("avatar"));
			}
			
			ImageLoader.getInstance().displayImage(avatar_url,
					viewHolder.chating_avatar,
					GlobalParams.getInstance().round_options);
		}
		
		viewHolder.chating_layout_location.setVisibility(View.VISIBLE);
		viewHolder.chating_layout_location.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				try {
					deleteDialog(position, json.getString("msgId"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		JSONObject object=new JSONObject(json.getString("text"));
		
		String [] location=object.getString("location").split(",");
		String longitude=location[0];
		String altitude=location[1];
		String uri = LocalUtils.getLocationImage(longitude,altitude, 330, 220);
		ImageLoader.getInstance().displayImage(uri,
				viewHolder.chating_mv_location,
				GlobalParams.getInstance().round_options);
		
		String address=object.getString("address");
		String name=object.getString("name");
		viewHolder.chating_tv_location.setText(address+name);
//		final AMapLocationClient mLocationClient = new AMapLocationClient(activity);
//		AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
//		mLocationOption.setNeedAddress(true);
//		mLocationOption.setOnceLocation(true);
//		mLocationOption.setWifiActiveScan(true);
//		mLocationOption.setMockEnable(false);
//		mLocationClient.setLocationOption(mLocationOption);
//		mLocationClient.startLocation();
//
//		AMapLocationListener mLocationListener = new AMapLocationListener() {
//			@Override
//			public void onLocationChanged(AMapLocation amapLocation) {
//				if (amapLocation != null) {
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
//
//					double Altitude = amapLocation.getLatitude();
//					System.out.println(Altitude);
//					double Longitude = amapLocation.getLongitude();
//					amapLocation.getAoiName();
//					System.out.println(Longitude);
//					String url_location = LocalUtils.getLocationImage(
//							String.valueOf(Longitude),
//							String.valueOf(Altitude), 330, 220);
//					System.out.println(url_location);
//					ImageLoader.getInstance().displayImage(url_location,
//							viewHolder.chating_mv_location,
//							GlobalParams.getInstance().round_options);
//					viewHolder.chating_tv_location.setText(amapLocation.getCity()
//							+ amapLocation.getDistrict()
//							+ amapLocation.getStreet()
//							+ amapLocation.getAoiName());
//					mLocationClient.stopLocation();
//					mLocationClient.onDestroy();
//				}
//			}
//		};
//
//		mLocationClient.setLocationListener(mLocationListener);
	}
	
	public void onDestroy() {
//		WSManager.getInstance().onDestroy();
		
	}

	public void onBindViewHolderContactway(RecyclerView.ViewHolder viewholder,final JSONObject json,final int position) throws JSONException {
		final ContactwayViewHolder viewHolder=(ContactwayViewHolder)viewholder;
		String avatar_url=null;
		if(json.getString("avatar").startsWith("http://")){
			avatar_url =json.getString("avatar");
		}else{
			avatar_url =String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,json.getString("avatar"));
		}
		ImageLoader.getInstance().displayImage(avatar_url,
				viewHolder.chating_avatar,
				GlobalParams.getInstance().round_options);
		
		String text = json.getString("text");
		final int contactway = json.getInt("contactway");
		viewHolder.chating_layout_contactway.setVisibility(View.VISIBLE);
		viewHolder.chating_layout_contactway.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				deleteDialog(position, json.toString());
				return false;
			}
		});
		viewHolder.chating_text_contactway.setText(text);
		if (contactway == ContactWay.WeiXin.ordinal()) {

		} else if (contactway == ContactWay.Telephone.ordinal()) {
			Drawable drawable = activity.getResources().getDrawable(
					R.drawable.molive_status_phone);
			drawable.setBounds(0, 0, 50, 50);
			viewHolder.chating_text_contactway.setCompoundDrawables(null, null,
					drawable, null);
		}

		viewHolder.chating_btn_accept.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (contactway == ContactWay.WeiXin.ordinal()) {
					try {
						String avatar_url = json.getString("avatar");
						JSONObject object = new JSONObject();
						object.put("type", SMGlobal.Chat.Send_Text);
						object.put("avatar", avatar_url);
						object.put("text",
								"<font color=\"#535B5E\">海燕同意你交换对方的微信号:leehongee</font>");
						addItem(object.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (contactway == ContactWay.Telephone.ordinal()) {
					try {
						String avatar_url = json.getString("avatar");
						JSONObject object = new JSONObject();
						object.put("type", SMGlobal.Chat.Receive_Text);
						object.put("avatar", avatar_url);
						object.put("text", "手机号码:13138866890");
						addItem(object.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Drawable drawable = activity.getResources().getDrawable(
							R.drawable.molive_status_phone);
					viewHolder.chating_text_contactway.setCompoundDrawables(
							null, null, drawable, null);
				}
			}
		});

		viewHolder.chating_btn_inject.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (contactway == ContactWay.WeiXin.ordinal()) {

				} else if (contactway == ContactWay.Telephone.ordinal()) {

				}
			}
		});

	}

	public void onBindViewHolderVideo(final RecyclerView.ViewHolder viewholder,final JSONObject json,final int position) throws JSONException {
		VideoViewHolder viewHolder=(VideoViewHolder)viewholder;
		String avatar_url=null;
		if(json.getString("avatar").startsWith("http://")){
			avatar_url =json.getString("avatar");
		}else{
			avatar_url =String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,json.getString("avatar"));
		}
		ImageLoader.getInstance().displayImage(avatar_url,
				viewHolder.chating_avatar,
				GlobalParams.getInstance().round_options);
		
		viewHolder.chating_layout_video.setVisibility(View.VISIBLE);
		viewHolder.chating_layout_video.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				try {
					deleteDialog(position, json.getString("msgId"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		String video_time = json.getString("time");
		viewHolder.chating_text_video.setText(video_time);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if(list.size()<=position){
			return ;
		}
		String json = list.get(position);
		try {
			JSONObject object = new JSONObject(json);
			if(object.getInt("sendRev")==SMGlobal.ChatSide.SEND){
//				if(object.getString("sid").equals(CacheManager.getInstance().userInfo.ID)){
//					
//				}else{
//					
//				}
				object.put("avatar", CacheManager.getInstance().userInfo.avatar);
			}else if(object.getInt("sendRev")==SMGlobal.ChatSide.REV){
				object.put("avatar", chatingObject.get("avatar").toString());
			}
			
			int type = object.getInt("msgType");
			switch (type) {
			case SMGlobal.SEND_TEXT_SAYHI:
			case SMGlobal.MsgType.Text:
				onBindViewHolderText(viewHolder, object,position);
				break;
			case SMGlobal.MsgType.Image:
				onBindViewHolderPhoto(viewHolder, object,position);
				break;
			case SMGlobal.MsgType.Voice:
				onBindViewHolderVoice(viewHolder, object,position);
				break;
			case SMGlobal.MsgType.Video:
				onBindViewHolderVideo(viewHolder, object,position);
				break;
			case SMGlobal.MsgType.Location:
				onBindViewHolderLocation(viewHolder, object,position);
				break;
			case SMGlobal.MsgType.Contactway:
				onBindViewHolderContactway(viewHolder, object,position);
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

final class TextViewHolder extends RecyclerView.ViewHolder {
	TextView chating_tv_sendtime;
	TextView chating_tv_content;
	ImageView chating_avatar;
	public TextViewHolder(View view) {
		super(view);
		chating_avatar = (ImageView) itemView.findViewById(R.id.chating_avatar);
		chating_tv_sendtime = (TextView) itemView.findViewById(R.id.chating_tv_sendtime);
		chating_tv_content = (TextView) itemView.findViewById(R.id.chating_tv_content);
	}
}

final class ImageViewHolder extends RecyclerView.ViewHolder {
	ImageView chating_avatar;
	ImageView chating_iv_image;
	TextView chating_tv_sendtime;
	public ImageViewHolder(View view) {
		super(view);
		chating_avatar = (ImageView) itemView.findViewById(R.id.chating_avatar);
		chating_iv_image = (ImageView) itemView.findViewById(R.id.chating_iv_photo);
		chating_iv_image.setScaleType(ScaleType.FIT_CENTER);
		chating_tv_sendtime = (TextView) itemView.findViewById(R.id.chating_tv_sendtime);
	}
	
	
}

final class VoiceViewHolder extends RecyclerView.ViewHolder {
	FrameLayout chating_layout_voice;
	TextView chating_voice_time;
	TextView chating_tv_sendtime;
	ImageView chating_btn_voice;
	ImageView chating_avatar;
	
	public VoiceViewHolder(View view) {
		super(view);
		chating_layout_voice = (FrameLayout) itemView.findViewById(R.id.chating_layout_voice);
		chating_btn_voice = (ImageView) itemView.findViewById(R.id.chating_btn_voice);
		chating_voice_time = (TextView) itemView.findViewById(R.id.chating_voice_time);
		chating_avatar = (ImageView) itemView.findViewById(R.id.chating_avatar);
		chating_tv_sendtime = (TextView) itemView.findViewById(R.id.chating_tv_sendtime);
	}
	
	
}

final class VideoViewHolder extends RecyclerView.ViewHolder {
	FrameLayout chating_layout_video;
	TextView chating_text_video;
	TextView chating_tv_sendtime;
	ImageView chating_btn_video;
	ImageView chating_avatar;
	
	public VideoViewHolder(View view) {
		super(view);
		chating_layout_video = (FrameLayout) itemView.findViewById(R.id.chating_layout_video);
		chating_text_video = (TextView) itemView.findViewById(R.id.chating_text_video);
		chating_btn_video = (ImageView) itemView.findViewById(R.id.chating_btn_video);
		chating_avatar = (ImageView) itemView.findViewById(R.id.chating_avatar);
		chating_tv_sendtime = (TextView) itemView.findViewById(R.id.chating_tv_sendtime);
	}
	
	
}


final class LocationViewHolder extends RecyclerView.ViewHolder {
	FrameLayout chating_layout_location;
	ImageView chating_mv_location;
	ImageView chating_avatar;
	TextView chating_tv_location;
	TextView chating_tv_sendtime;
	public LocationViewHolder(View view) {
		super(view);
		chating_layout_location = (FrameLayout) itemView.findViewById(R.id.chating_layout_location);
		chating_mv_location = (ImageView) itemView.findViewById(R.id.chating_mv_location);
		chating_tv_location = (TextView) itemView.findViewById(R.id.chating_tv_location);
		chating_avatar = (ImageView) itemView.findViewById(R.id.chating_avatar);
		chating_tv_sendtime = (TextView) itemView.findViewById(R.id.chating_tv_sendtime);
	}
	
}

final class ContactwayViewHolder extends RecyclerView.ViewHolder {
	LinearLayout chating_layout_contactway;
	TextView chating_text_contactway;
	TextView chating_tv_sendtime;
	Button chating_btn_accept;
	Button chating_btn_inject;
	ImageView chating_avatar;
	
	public ContactwayViewHolder(View view) {
		super(view);
		chating_layout_contactway = (LinearLayout) itemView.findViewById(R.id.chating_layout_contactway);
		chating_text_contactway = (TextView) itemView.findViewById(R.id.chating_text_contactway);
		chating_btn_accept = (Button) itemView.findViewById(R.id.chating_btn_accept);
		chating_btn_inject = (Button) itemView.findViewById(R.id.chating_btn_inject);
		chating_avatar = (ImageView) itemView.findViewById(R.id.chating_avatar);
		chating_tv_sendtime = (TextView) itemView.findViewById(R.id.chating_tv_sendtime);
	}
}
