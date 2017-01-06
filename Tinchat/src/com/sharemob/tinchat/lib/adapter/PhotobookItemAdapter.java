package com.sharemob.tinchat.lib.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.component.CircleImageView;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class PhotobookItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private AppActivity activity;
	private ArrayList<CharSequence> list=new ArrayList<CharSequence>();;

	public PhotobookItemAdapter(AppActivity activity) {
		this.activity=activity;
	}
	
	public PhotobookItemAdapter setList(ArrayList<CharSequence> urls){
		this.list = urls;
		return this;
	}
	public void addItem(String json) {
		this.list.add(json);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photobook_item, parent, false);
        return new PhotobookItemViewHolder(view);
    }
	public void setArrayString(String json) {
		this.list.clear();
		try {
			JSONArray array = new JSONArray(json);
			for(int i=0;i<array.length();i++){
				String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,array.getString(i));
				addItem(url);
				System.out.println(url);
			}
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
				String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,object.getString("filename"));
				addItem(url);
//				System.out.println(url);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
		PhotobookItemViewHolder viewHolder=(PhotobookItemViewHolder)viewholder;
		String uri=list.get(position).toString();
		viewHolder.photobook_item.setScaleType(ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(
				uri,
				viewHolder.photobook_item);
		
		viewHolder.photobook_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageUtils.imageBrower(activity, 0, list);
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
}

final class PhotobookItemViewHolder extends  RecyclerView.ViewHolder
{
	ImageView photobook_item;
	public PhotobookItemViewHolder(View itemView) {
		super(itemView);
		photobook_item = (ImageView) itemView.findViewById(R.id.photobook_item);
	}
}