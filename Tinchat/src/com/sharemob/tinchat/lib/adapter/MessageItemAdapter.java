package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;
import com.sharemob.tinchat.modules.message.MessageActivity;

public class MessageItemAdapter extends RecyclerView.Adapter<ViewHolder>  {

	private List<MessageItem> list = new LinkedList<MessageItem>();
	private Activity activity;
	public enum Message_Type{
		User,Official
	}
	public MessageItemAdapter(Activity activity) {
		this.activity = activity;
	}

	public void daleteData(int position) {
		list.remove(position);
		notifyItemRemoved(position);
		notifyItemChanged(position);
	}
	
	public void addItem(String json) {
		try {
			MessageItem item = convertEntity(new JSONObject(json));
			this.list.add(item);
			int count = getItemCount();
			this.notifyItemInserted(count);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getItemCount()
	{
		return list.size();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	public MessageItem convertEntity(JSONObject object) throws JSONException{
		MessageItem entity=new MessageItem();
		final int type=object.getInt("type");
		entity.type=type;
		if(type==Message_Type.User.ordinal()){
			int sex=object.getInt("sex");
			if(object.has("uid")){
				String uid=object.getString("uid");
				entity.uid=uid;
			}
			
			String birthday=object.getString("birthday");
			String age=LocalUtils.calculateDatePoor(birthday);
			entity.number=object.getString("number");
			entity.constellation=LocalUtils.constellation(birthday);
			if(object.has("time")){
				entity.time=LocalUtils.getIntervalFormat(object.getString("time"));
			}
			
			entity.age=age;
			entity.sex=sex;
		}
		entity.avatar=object.getString("avatar");
		entity.title=object.getString("title");
		entity.text=object.getString("content");
		entity.ID=object.getInt("ID");
		return entity;
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
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_item,viewGroup,false);
		return new MessageItemViewHolder(itemView);
	}
	
	public void onBindViewHolderMessageItem(final MessageItemViewHolder viewHolder,final MessageItem entity, int position) {
		if(entity.type==Message_Type.User.ordinal()){
			
			if(Integer.parseInt(entity.number)==0){
				viewHolder.tv_num.setVisibility(View.GONE);
			}else{
				viewHolder.tv_num.setVisibility(View.VISIBLE);
			}
			
			viewHolder.tv_num.setText(entity.number);
			if(Gender.MALE.ordinal()==entity.sex){
				viewHolder.tv_marker.setText(entity.age);
				viewHolder.tv_marker.setBackgroundResource(R.drawable.gender_boy);
			}else{
				viewHolder.tv_marker.setText(entity.age);
				viewHolder.tv_marker.setBackgroundResource(R.drawable.gender_girl);
			}
			viewHolder.tv_time.setVisibility(View.VISIBLE);
			viewHolder.tv_time.setText(entity.time);
			viewHolder.tv_constellation.setText(entity.constellation);
		}else if(Message_Type.Official.ordinal()==entity.type){
			viewHolder.tv_marker.setVisibility(View.GONE);
			viewHolder.tv_num.setVisibility(View.GONE);
			viewHolder.tv_constellation.setVisibility(View.GONE);
		}
		String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,entity.avatar);
		viewHolder.avatar.setScaleType(ScaleType.FIT_XY);
		ImageLoader.getInstance().displayImage(url, viewHolder.avatar,GlobalParams.getInstance().round_options);
		viewHolder.tv_title.setText(entity.title);
		try {
			JSONObject object=new JSONObject(entity.text);
			String sendTime=LocalUtils.getDateFromLong(object.getLong("sendTime"));
			viewHolder.tv_time.setText(LocalUtils.getIntervalFormat(sendTime));
			
			String content=null;
			int msgType=object.getInt("type");
			switch (msgType) {
			case SMGlobal.MsgType.Text:
				content=object.getString("content");
				break;
			case SMGlobal.MsgType.Voice:
				content=new String("[语音]");
				break;
			case SMGlobal.MsgType.Video:
				content=new String("[视频]");
				break;
			case SMGlobal.MsgType.Location:
				content=new String("[位置]");
				break;
			case SMGlobal.MsgType.Image:
				content=new String("[图片]");
				break;
			default:
				break;
			}
			viewHolder.tv_text.setText(content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		viewHolder.rootView.setBackgroundResource(R.drawable.selector_list_item);
		viewHolder.rootView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(entity.type==Message_Type.Official.ordinal()){
					LocalUtils.gotoView(activity,MessageActivity.class);
				}else if(Message_Type.User.ordinal()==entity.type){
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("nickname", entity.title);
					map.put("sid", entity.ID);
					map.put("rid", CacheManager.getInstance().userInfo.ID);
					map.put("avatar",entity.avatar);
					LocalUtils.enterAppActivity(activity, map, "ChatingActivity");
					viewHolder.tv_num.setVisibility(View.GONE);
				}
			}
		});
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
			 	final MessageItemViewHolder  viewHolder=(MessageItemViewHolder)holder;
			 	MessageItem entity=list.get(position);
			 	onBindViewHolderMessageItem(viewHolder, entity, position);
	}
}

final class MessageItem implements Serializable{
	private static final long serialVersionUID = 1L;
	String uid;
	String avatar;
	String title;
	String text;
	String time;
	String number;
	String age;
	String constellation;
	int ID;
	int sex;
	int type;
}

final class MessageItemViewHolder extends  RecyclerView.ViewHolder
{
	ImageView avatar;
	TextView tv_title;
	TextView tv_text;
	TextView tv_time;
	TextView tv_marker;;
	TextView tv_num;
	TextView tv_constellation;
	View rootView;
	public MessageItemViewHolder(View convertView) {
		super(convertView);
		rootView=convertView;
//		 CheckBox cb2=(CheckBox)convertView.findViewById(R.id.message_iv_header);
//		 cb2.setChecked(checked)
		avatar = (ImageView) convertView.findViewById(R.id.message_iv_header);
		tv_title =LocalUtils. getFindViewById( convertView,R.id.message_tv_title);
		tv_time=LocalUtils.getFindViewById( convertView,R.id.message_tv_time);
		tv_text=LocalUtils.getFindViewById( convertView,R.id.message_tv_content);
		tv_marker=LocalUtils.getFindViewById( convertView,R.id.message_tv_marker);
		tv_num=LocalUtils.getFindViewById( convertView,R.id.message_tv_num);
		tv_constellation=LocalUtils.getFindViewById( convertView,R.id.message_tv_constellation);
	}
}