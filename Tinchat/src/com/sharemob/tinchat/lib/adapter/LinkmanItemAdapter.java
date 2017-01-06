package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class LinkmanItemAdapter extends RecyclerView.Adapter<ViewHolder> {

	ArrayList<LinkmanItem> list = new ArrayList<LinkmanItem>();

	private Activity activity;
	public LinkmanItemAdapter(Activity activity) {
		this.activity=activity;
	}
	@Override
	public int getItemCount()
	{
		return list.size();
	}
	
	public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void daleteData(int position) {
		list.remove(position);
		notifyItemRemoved(position);
		notifyItemChanged(position);
	}
	
	public void addItem(String json) {
		try {
			LinkmanItem item = convertEntity(new JSONObject(json));
			this.list.add(item);
			int count = getItemCount();
			this.notifyItemInserted(count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public LinkmanItem convertEntity(JSONObject object) throws JSONException{
		LinkmanItem item=new LinkmanItem();
		item.uid=object.getString("uid");
		item.avatar=object.getString("avatar");
		item.nickname=object.getString("nickname");
		item.location=object.getString("location");
		item.sex=object.getInt("sex");
		String birthday=object.getString("birthday");
		item.age=LocalUtils.calculateDatePoor(birthday);
		item.constellation=LocalUtils.constellation(birthday);
		
		String time=object.getString("time");
		
		item.time=LocalUtils.getIntervalFormat(time);
		
		return item;
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
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int paramInt) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_listview_friend,viewGroup,false);
		return new LinkmanItemViewHolder(itemView);
	}
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
			 	final LinkmanItemViewHolder  viewHolder=(LinkmanItemViewHolder)holder;
			 	LinkmanItem entity=list.get(position);
			 	onBindViewHolderLinkmanItem(viewHolder, entity, position);
	}	
	public void enterUserSpace(final String uid){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("uid",uid);
		LocalUtils.enterAppActivity(activity, map, "MyspaceActivity");
	}
	public void onBindViewHolderLinkmanItem(final LinkmanItemViewHolder viewHolder,final LinkmanItem entity, int position) {
		viewHolder.distance_time.setText(entity.time);
		viewHolder.avatar.setScaleType(ScaleType.FIT_CENTER);
		String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,entity.avatar); 
		ImageLoader.getInstance().displayImage(url, viewHolder.avatar,GlobalParams.getInstance().round_options);
		
		viewHolder.nickname.setText(entity.nickname);
		viewHolder.location.setText(entity.location);	
		if(entity.sex==Gender.MALE.ordinal()){
			viewHolder.sex_age.setBackgroundResource(R.drawable.gender_boy);
		}else{
			viewHolder.sex_age.setBackgroundResource(R.drawable.gender_girl);
		}
		viewHolder.sex_age.setText(entity.age);
		
		viewHolder.constellation.setText(entity.constellation);
		viewHolder.rootView.setBackgroundResource(R.drawable.selector_list_item);
		viewHolder.rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterUserSpace(entity.uid);
			}
		});
	}

}
final class LinkmanItemViewHolder extends  RecyclerView.ViewHolder
{
	ImageView avatar;
	TextView  nickname;
	TextView  distance_time;
	TextView  sex_age;
	TextView  location;
	TextView  constellation;
	View rootView;
	public LinkmanItemViewHolder(View view) {
		super(view);
		rootView=view;
		avatar = (ImageView) view.findViewById(R.id.linkman_avatar);
		nickname =LocalUtils. getFindViewById( view,R.id.friend_nickname);
		sex_age=(TextView)LocalUtils.getFindViewById(view,R.id.friend_sex_age);
		location=(TextView)LocalUtils.getFindViewById(view,R.id.friend_location);
		constellation=(TextView)LocalUtils.getFindViewById(view,R.id.constellation);
		distance_time=(TextView)LocalUtils.getFindViewById(view,R.id.friend_distance_time);
	}
}
final class LinkmanItem implements Serializable{
	private static final long serialVersionUID = 1L;
	String uid;
	String avatar;
	String location;
	String age;
	String nickname;
	String time;
	String constellation;
	int sex;
}
