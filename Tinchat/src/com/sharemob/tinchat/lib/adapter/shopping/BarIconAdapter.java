package com.sharemob.tinchat.lib.adapter.shopping;
import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;

import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;

public class BarIconAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<BarIcon> list = new ArrayList<BarIcon>();
    
    public BarIconAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        return list.size();
    }
    
    public void addItem(BarIcon entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    public void addArray(String json) {
    	if(json==null||"null".equals(json)){
			return;
		}
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				BarIcon barIcon=new BarIcon();
				barIcon.title=object.getString("title");
				barIcon.icon=object.getString("icon");
				addItem(barIcon);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        BarIconViewHolder holder = (BarIconViewHolder)viewHolder;
        BarIcon barIcon = (BarIcon)list.get(position);
        holder.title.setText(barIcon.title);
        ImageLoader.getInstance().displayImage(barIcon.icon, holder.icon);
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_shopping_icon_item, parent, false);
        return new BarIconViewHolder(view);
    }
}