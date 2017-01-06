package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.BannerSlideView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;

final class ToyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<ShoppingToyEntity> list = new ArrayList<ShoppingToyEntity>();
    
    public ToyListAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        return list.size();
    }
    
    public void addItem(ShoppingToyEntity entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    public void addArray(String json) {
    	System.out.println(json);
    	try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				ShoppingToyEntity entity=new ShoppingToyEntity();
				entity.banner=object.getJSONArray("banner").toString();
				entity.title=object.getString("title");
				entity.toys=object.getJSONArray("toys").toString();
				entity.banner_size=Float.parseFloat(object.getString("size"));
				addItem(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ShoppingToyViewHolder holder = (ShoppingToyViewHolder)viewHolder;
        ShoppingToyEntity item = (ShoppingToyEntity)list.get(position);
        holder.title.setText(item.title);
        
        BannerSlideView bannerSlideView = holder.banner;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)bannerSlideView.getLayoutParams();
        linearParams.height = (int)((float)linearParams.height * item.banner_size);
        bannerSlideView.setLayoutParams(linearParams);
        bannerSlideView.loadBannerSlide(null, item.banner);
        
        RecyclerView gridView = holder.recyclerview;
        SmallToyItemAdapter adapter = new SmallToyItemAdapter(activity);
        gridView.setAdapter(adapter);
        adapter.addArray(item.toys);
        adapter.notifyDataSetChanged();
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_shopping_toy, parent, false);
        return new ShoppingToyViewHolder(view);
    }
}