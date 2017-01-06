package com.sharemob.tinchat.lib.adapter.shopping;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.modules.shopping.GoodsActivity;

final class SmallToyItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<SmallToyEntity> list = new ArrayList<SmallToyEntity>();
    
    public SmallToyItemAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        return list.size();
    }
    
    public void addItem(SmallToyEntity entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    public void addArray(String json) {
    	try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				SmallToyEntity entity=new SmallToyEntity();
				entity.img=object.getString("img");
				entity.subtitle=object.getString("subtitle");
				entity.title=object.getString("title");
				addItem(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SmallToyViewHolder holder = (SmallToyViewHolder)viewHolder;
        SmallToyEntity item = (SmallToyEntity)list.get(position);
        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle);
        ImageLoader.getInstance().displayImage(item.img, holder.image);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalUtils.gotoView(activity, GoodsActivity.class);
            }
        });
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.shopping_sextoy_item, parent, false);
        return new SmallToyViewHolder(view);
    }
}