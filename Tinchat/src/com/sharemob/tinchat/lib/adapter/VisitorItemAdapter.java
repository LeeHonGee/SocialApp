package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class VisitorItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private ArrayList<VisitorEntity> list = new ArrayList<VisitorEntity>();
	private Activity activity;
	public VisitorItemAdapter(Activity activity) {
		this.activity=activity;
	}
	
	public VisitorEntity convertEntity(JSONObject object) throws JSONException{
		VisitorEntity item=new VisitorEntity();
		item.uid=object.getString("uid");
		item.avatar=object.getString("avatar");
		item.nickname=object.getString("nickname");
		item.content=object.getString("content");
		item.sex=object.getInt("sex");
		String birthday=object.getString("birthday");
		item.age=LocalUtils.calculateDatePoor(birthday);
		item.time=object.getString("time");
		
		return item;
	}
	public void addItem(String json) {
		try {
			VisitorEntity item = convertEntity(new JSONObject(json));
			this.list.add(item);
			int count = getItemCount();
			this.notifyItemInserted(count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void setArray(String json) {
		this.list.clear();
		try {
			JSONArray array = new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				this.list.add(convertEntity(object));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}
	@Override
	public int getItemCount() {
		return list.size();
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int paramInt) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_visitor_item,viewGroup,false);
		return new VisitorViewHolder(itemView);
	}
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final VisitorViewHolder viewHolder = (VisitorViewHolder) holder;
		VisitorEntity entity = list.get(position);
		onBindViewHolderVisitor(viewHolder, entity, position);
	}	
	public void enterUserSpace(final String uid){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("uid",uid);
		map.put("myuid",CacheManager.getInstance().userInfo.uid);
		LocalUtils.enterAppActivity(activity, map, "MyspaceActivity");
	}
	public void onBindViewHolderVisitor(final VisitorViewHolder viewHolder,final VisitorEntity entity, int position) {
		viewHolder.avatar.setScaleType(ScaleType.FIT_CENTER);
		String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,entity.avatar); 
		ImageLoader.getInstance().displayImage(url, viewHolder.avatar,GlobalParams.getInstance().round_options);
		
		viewHolder.nickname.setText(entity.nickname);
		if(entity.sex==Gender.MALE.ordinal()){
			viewHolder.sex_age.setBackgroundResource(R.drawable.gender_boy);
		}else{
			viewHolder.sex_age.setBackgroundResource(R.drawable.gender_girl);
		}
		viewHolder.sex_age.setText(entity.age);
		
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			Date date=sdf.parse(entity.time);
			viewHolder.time.setText(String.format("%02d月%02d日", date.getMonth(),date.getDate()));
			viewHolder.content.setText(String.format("%02d:%02d %s",date.getHours(),date.getMinutes(),entity.content));
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		viewHolder.rootView.setBackgroundResource(R.drawable.selector_list_item);
		viewHolder.rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterUserSpace(entity.uid);
			}
		});
	}
}

final class VisitorViewHolder extends RecyclerView.ViewHolder{
	View rootView;
	ImageView avatar;
	TextView  nickname;
	TextView  content;
	TextView  time;
	TextView  sex_age;
	public VisitorViewHolder(View view) {
		super(view);
		rootView=view;
		avatar= (ImageView) view.findViewById(R.id.visitor_avatar);
		nickname =LocalUtils.getFindViewById(view,R.id.visitor_nickname);
		sex_age=LocalUtils.getFindViewById(view, R.id.visitor_sex_age);
		content = LocalUtils.getFindViewById(view,R.id.visitor_content);
		time=LocalUtils.getFindViewById(view, R.id.visitor_time);
	}
}

final class VisitorEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String uid;
	String nickname;
	String avatar;
	String content;
	String time;
	String age;
	int sex;
}
