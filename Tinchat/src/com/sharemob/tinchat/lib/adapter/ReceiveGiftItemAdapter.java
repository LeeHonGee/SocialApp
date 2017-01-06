/**
 *  文件名:CommentAdapter.java
 *  修改人:lihangjie
 *  创建时间:2016-5-2 上午11:25:26
 */
package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-5-2 上午11:25:26]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class ReceiveGiftItemAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public static final int Receive=0;
	public static final int Send=1;
	private Activity activity;
	private int state=Receive;
	private Map<String,Object> params=new HashMap<String, Object>();
	private List<ReceiveGiftEntity> items=new ArrayList<ReceiveGiftEntity>();
	public int currentPage=0;
	
	public ReceiveGiftItemAdapter(Activity activity) {
		this.activity=activity;
	}
	public void setState(int state){
		this.state=state;
	}
	
	public void addParam(String key,Object value){
		params.put(key, value);
	}
	
//	public void addObject(String json) {
//		try {
//		
//		}catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
	public void addArray(String json){
		items.clear();
		System.out.println(json);
		if(json==null||"".equals(json)){
			return;
		}
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				ReceiveGiftEntity entity=new ReceiveGiftEntity();
				entity.uid=object.getString("uid");
				entity.gift_name=object.getString("name");
				entity.gift_moral=object.getString("moral");
				entity.gift_icon=object.getString("path");
				entity.avatar=object.getString("avatar");
				entity.nickname=object.getString("nickname");
				String time=object.getString("time");
				entity.time=LocalUtils.getIntervalFormat(time);
				entity.gift_moral=object.getString("moral");
				entity.ID=object.getInt("ID");
				this.items.add(entity);
				int count = getItemCount();
				this.notifyItemInserted(count);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		ReceiveGiftViewHolder  holder=(ReceiveGiftViewHolder)viewHolder;
		final ReceiveGiftEntity entity=items.get(position);
		String gift_icon_url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr ,entity.gift_icon);
		ImageLoader.getInstance().displayImage(gift_icon_url, holder.gift_icon);
		
		String avatar_url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr ,entity.avatar);
		ImageLoader.getInstance().displayImage(avatar_url, holder.avatar);
		holder.gift_name.setText(entity.gift_name);
		holder.gift_nickname.setText(entity.nickname);
//		holder.gift_moral.setText(entity.gift_moral);
		holder.gift_time.setText(String.format("时间:%s", entity.time));
//		holder.gift_nickname.setText(String.format("价格:%d 元/朵", entity.nickname));
		
		holder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("uid", entity.uid);
				LocalUtils.enterAppActivity(activity, map, "MyspaceActivity");
			}
		});
		
		switch (state) {
		case Receive:
			holder.gift_tv_title.setVisibility(View.VISIBLE);
			break;
		case Send:
			holder.gift_tv_title.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(activity).inflate(R.layout.layout_item_rev_gift, parent, false);
		return new ReceiveGiftViewHolder(view);
	}
}

final class ReceiveGiftViewHolder extends  RecyclerView.ViewHolder{
	View rootView;
	ImageView avatar;
	ImageView gift_icon;
	TextView gift_name;
	TextView gift_nickname;
	TextView gift_moral;
	TextView gift_time;
	TextView gift_tv_title;
	public ReceiveGiftViewHolder(View view) {
		super(view);
		rootView=view;
		avatar= (ImageView) view.findViewById(R.id.rev_gift_avatar);
		gift_icon= (ImageView) view.findViewById(R.id.rev_gift_icon);
		gift_name =LocalUtils.getFindViewById(view,R.id.rev_gift_tv_name);
		gift_nickname =LocalUtils.getFindViewById(view,R.id.rev_gift_tv_nickname);
		gift_time =LocalUtils.getFindViewById(view,R.id.rev_gift_tv_time);
		gift_tv_title=LocalUtils.getFindViewById(view,R.id.rev_gift_tv_title);
	}
	
}

final class ReceiveGiftEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String uid;
	String avatar;
	String nickname;
	String gift_icon;
	String gift_name;
	String gift_moral;
	String time;
	int ID;
}