package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.common.SMGlobal;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private Activity activity;
	private List<PoisEntity> list = new ArrayList<PoisEntity>();
	
	public LocationAdapter(Activity activity) {
		this.activity = activity;
		
	}
	
	public void setAdapter(ArrayList<String> json) {
		
		for(int i=0;i<json.size();i++){
			try {
				JSONObject object = new JSONObject(json.get(i));
				PoisEntity entity=new PoisEntity();
				entity.name=object.getString("name");
				entity.address=object.getString("address");
				entity.location=object.getString("location");
				entity.selected=(i==0?true:false);
				addItem(entity);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void SendLocation(){
		for(int i=0;i<list.size();i++){
			PoisEntity entity=list.get(i);
			if(entity.selected){
				try {
					JSONObject object=new JSONObject();
					object.put("location", entity.location);
					object.put("type", SMGlobal.MsgType.Location);
					object.put("address", entity.address);
					object.put("name", entity.name);
					
					Intent intent=new Intent(SMGlobal.CHATING_LOCATION_ACTION);
					Bundle bundle=new Bundle();
					bundle.putString(SMGlobal.LOCATION_KEY, object.toString());
					intent.putExtras(bundle);
					System.out.println( object.toString());
					activity.sendBroadcast(intent);
					
					break;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				continue;
			}
		}
		
	
	}
	
	public void addItem(PoisEntity entity) {
		this.list.add(entity);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.location_address_item, parent, false);
		return new PoisViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder view,final int position) {
		final PoisEntity entity=list.get(position);
		final PoisViewHolder  viewHolder=(PoisViewHolder)view;
		viewHolder.location_tv_name.setText(entity.name);
		viewHolder.location_tv_address.setText(entity.address);
		if(entity.selected){
			entity.selected=true;
			viewHolder.location_item_select.setVisibility(View.VISIBLE);
		}else{
			entity.selected=false;
			viewHolder.location_item_select.setVisibility(View.GONE);
		}
		viewHolder.rootView.setBackgroundResource(R.drawable.selector_list_item);
		viewHolder.rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				entity.selected=true;
				onClickRadioButton(position);
				System.out.println(entity.location);
			}
		});
	}
	
	public void onClickRadioButton(int position){
		for(int i=0;i<list.size();i++){
			if(position!=i){
				list.get(i).selected=false;
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
	
	final class PoisViewHolder extends RecyclerView.ViewHolder {
		ImageView location_item_select;
		TextView location_tv_name;
		TextView location_tv_address;
		FrameLayout rootView;
		public PoisViewHolder(View view) {
			super(view);
			rootView=(FrameLayout)view.findViewById(R.id.location_rootview);
			location_item_select=(ImageView)view.findViewById(R.id.location_item_select);
			location_tv_name=(TextView)view.findViewById(R.id.location_tv_name);
			location_tv_address=(TextView)view.findViewById(R.id.location_tv_address);
		}
	}

	final class PoisEntity implements Serializable{
		private static final long serialVersionUID = 1L;
		String name;
		String address;
		String location;
//		double latitude;
//		double longitude;
		boolean selected=false;
	}
}
