package com.sharemob.tinchat.lib.adapter.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class CenterItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//	private JSONArray array=new JSONArray();
	private ArrayList<CenterItem> list = new ArrayList<CenterItem>();
	private Activity activity;
	
	public CenterItemListAdapter(Activity activity,String json) {
	        this.activity = activity;
	        this.addArray(json);
	}
	@Override
	public int getItemCount() {
//		return array.length();
		return list.size();
	}
	
    public void addItem(CenterItem entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    private void addArray(String json) {
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				CenterItem barIcon=new CenterItem();
				barIcon.title=object.getString("title");
				barIcon.icon=object.getString("icon");
				barIcon.eventName=object.getString("eventName");
				addItem(barIcon);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
		final CenterItemViewHolder holder = (CenterItemViewHolder)viewHolder;
		final CenterItem barIcon = (CenterItem)list.get(position);
		holder.title.setText(barIcon.title);
	    ImageLoader.getInstance().displayImage(barIcon.icon, holder.icon);
	    
	    holder.view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_DOWN==event.getAction()){
					holder.rootView.setBackgroundColor(Color.parseColor("#e2e2e2"));
				}else if(MotionEvent.ACTION_UP==event.getAction()||(MotionEvent.ACTION_CANCEL==event.getAction())){
					holder.rootView.setBackgroundColor(Color.parseColor("#ffffff"));
				}
				return false;
			}
		});
	    
	    holder.view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(barIcon.eventName.contains("MygiftsActivity")||barIcon.eventName.contains("LinkmanActivity")){
//					LocalUtils.gotoViewByAnim(activity, barIcon.eventName,R.anim.in_from_right,R.anim.out_to_left, false);
					LocalUtils.enterActivity(activity, barIcon.eventName);
				}else{
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("uid", CacheManager.getInstance().userInfo.uid);
					LocalUtils.enterAppActivity(activity,map,barIcon.eventName);
				}
			}
		});
	}

	@Override
	 public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(activity).inflate(R.layout.fragment_center_item, parent, false);
        return new CenterItemViewHolder(view);
	}

}
