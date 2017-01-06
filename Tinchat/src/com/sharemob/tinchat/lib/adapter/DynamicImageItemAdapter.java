package com.sharemob.tinchat.lib.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.GridLayoutAnimationController;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.component.CircleImageView;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class DynamicImageItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private AppActivity activity;
	private Map<String,Object> params=new HashMap<String, Object>();
	private ArrayList<CharSequence> list=new ArrayList<CharSequence>();;

	public DynamicImageItemAdapter(AppActivity activity) {
		this.activity=activity;
	}
	
	public void addParam(String key,Object value){
		params.put(key, value);
	}
	
	public void addItem(String json) {
		this.list.add(json);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}

	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dynamicimage_item, parent, false);
        return new DynamicImageViewHolder(view);
    }
	public void setArrayString(String json) {
		this.list.clear();
		try {
			JSONArray array = new JSONArray(json);
			for(int i=0;i<array.length();i++){
				String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,array.getString(i));
				addItem(url);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}
	
	public void enterUserSpace(){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("uid", params.get("uid"));
		LocalUtils.enterAppActivity(activity, map, "MyDynamicArticleActivity");
	}
	
	public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
		DynamicImageViewHolder viewHolder=(DynamicImageViewHolder)viewholder;
		String uri=list.get(position).toString();
		viewHolder.dynamicimage_item.setScaleType(ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(
				uri,
				viewHolder.dynamicimage_item,
				GlobalParams.getInstance().round_options);
		
		viewHolder.dynamicimage_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterUserSpace();
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
}

final class DynamicImageViewHolder extends  RecyclerView.ViewHolder
{
	ImageView dynamicimage_item;
	public DynamicImageViewHolder(View itemView) {
		super(itemView);
		dynamicimage_item = (ImageView) itemView.findViewById(R.id.dynamicimage_item);
	}
}