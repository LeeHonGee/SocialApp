package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.DrawableCenterButton;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyUserAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int[] user_icon_btn_res=new int[]{R.drawable.user_icon_chatbtn,R.drawable.user_icon_hibtn};
	private ArrayList<String> list=new ArrayList<String>();
	public static final byte USER=0;
	public static final byte AD=1;
	public int currentPage=0;
	private Context context;
	
	public NearbyUserAdapter(Context context) {
		this.context=context;
	}
	@Override
	public int getItemCount() {
		return list.size();
	}
	public void clear(){
		this.list.clear();
	}
	public int getItemViewType(int position) {
		if(position<list.size()){
			try {
				JSONObject object = new JSONObject(list.get(position));
				int type = Integer.parseInt(object.getString("type"));
				return type;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return super.getItemViewType(position);
	}
	public void addItem(String entity) {
		this.list.add(entity);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}
	
	public void addArray(String json){
		if(json==null||"".equals(json)){
			return;
		}
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				addItem(object.toString());
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if(list.size()<=position){
			return ;
		}
		try {
			String json=list.get(position);
			JSONObject object = new JSONObject(json);
			int type = object.getInt("type");
			switch (type) {
			case USER:
				NearbyUser entity=new NearbyUser();
				entity.uid=object.getString("uid");
				entity.avatar=object.getString("avatar");
				entity.nickname=object.getString("nickname");
				entity.birthday=object.getString("birthday");
				entity.sex=object.getInt("sex");
				entity.ID=object.getInt("ID");
				entity.age=LocalUtils.calculateDatePoor(entity.birthday);
//				entity.server_addr=object.getString("server_addr");
				entity.signature=object.getString("signature");
				if("".equals(entity.signature)){
					entity.signature=new String("该用户真懒,什么都没写");
				}
				entity.location=object.getString("location");
				if("".equals(entity.location)){
					entity.location=new String("该用户对地理位置开启保密");
				}
				entity.loginTime=object.getString("loginTime");
				onBindViewHolderNearbyUser(viewHolder, entity, position);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public void onBindViewHolderNearbyUser(RecyclerView.ViewHolder viewHolder,final NearbyUser entity, int position) {
		NearbyUserViewHolder  holder=(NearbyUserViewHolder)viewHolder;
		String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,entity.avatar);
		ImageLoader.getInstance().displayImage(url, holder.avatar, GlobalParams.getInstance().round_options);
		
		holder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("uid", entity.uid);
				LocalUtils.enterAppActivity(context, map, "MyspaceActivity");
			}
		});
		
		try {
			holder.nickname.setText(URLDecoder.decode(entity.nickname, "UTF-8"));
			holder.signature.setText(URLDecoder.decode(entity.signature, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		holder.location.setText(entity.location);
		
		if(Gender.MALE.ordinal()==entity.sex){
			holder.sex_age.setBackgroundResource(R.drawable.gender_boy);
		}else if(Gender.FEMALE.ordinal()==entity.sex){
			holder.sex_age.setBackgroundResource(R.drawable.gender_girl);
		}
		
		holder.sex_age.setText(entity.age);
		holder.loginTime.setText(LocalUtils.getIntervalFormat(entity.loginTime));
		
		for(int i=0;i<user_icon_btn_res.length;i++){
			Drawable drawable =context.getResources().getDrawable(user_icon_btn_res[i]);
			drawable.setBounds(0, 0, 60, 60); //必须设置图片大小，否则不显示
	    	if(i==0){
	    		holder.btn_chatting.setCompoundDrawables(drawable , null, null, null);
	    	}else{
	    		holder.btn_sayhi.setCompoundDrawables(drawable , null, null, null);
	    	}
		}
		
		holder.btn_chatting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("nickname", entity.nickname);
				map.put("uid", entity.uid);
				map.put("sid", entity.ID);
				map.put("rid", CacheManager.getInstance().userInfo.ID);
				map.put("avatar", entity.avatar);
				LocalUtils.enterAppActivity(context, map, "ChatingActivity");
			}
		});
		
		holder.btn_sayhi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LocalUtils.sendSayHi(context, entity.ID);
			}
		});
	}

	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view =null;
		switch (viewType) {
		case USER:
			view = LayoutInflater.from(context).inflate(R.layout.layout_nearbyuser_item, parent, false);
			return new NearbyUserViewHolder(view);
		}
		return null;
	}

}

final class NearbyUserViewHolder extends RecyclerView.ViewHolder{
	ImageView avatar;
	TextView  nickname;
	TextView  signature;
	TextView  location;
	TextView  loginTime;
	TextView  sex_age;
	DrawableCenterButton btn_chatting;
	DrawableCenterButton btn_sayhi;
	public NearbyUserViewHolder(View view) {
		super(view);
		avatar= (ImageView) view.findViewById(R.id.nearbyuser_avatar);
		location=LocalUtils.getFindViewById(view,R.id.nearbyuser_location);
		nickname =LocalUtils.getFindViewById(view,R.id.nearbyuser_nickname);
		sex_age=LocalUtils.getFindViewById(view, R.id.nearbyuser_sex_age);
		signature = LocalUtils.getFindViewById(view,R.id.nearbyuser_signature);
		loginTime=LocalUtils.getFindViewById(view, R.id.nearbyuser_distance_time);
		btn_chatting=(DrawableCenterButton)view.findViewById(R.id.nearbyuser_btn_chat);
		btn_sayhi=(DrawableCenterButton)view.findViewById(R.id.nearbyuser_btn_sayhi);
	}
}
final class NearbyUser implements Serializable{
	private static final long serialVersionUID = 1L;
	String uid;
	String avatar;
	String birthday;
	String nickname;
	String signature;
	String location;
	String age;
	String loginTime;
	String server_addr;
	int sex;
	int ID;
}
