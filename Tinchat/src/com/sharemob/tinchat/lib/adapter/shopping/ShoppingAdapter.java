package com.sharemob.tinchat.lib.adapter.shopping;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int INDEX_TOP_BANNER = 0x0;
    private static final int INDEX_TOY_1 = 0x1;
    private static final int INDEX_TOY_2 = 0x2;
    private Activity activity;
    private JSONArray jsonArray;
    private ArrayList<ShoppingToyEntity> list = new ArrayList<ShoppingToyEntity>();
    
    public ShoppingAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        if(jsonArray != null) {
            return jsonArray.length();
        }
        return 0x0;
    }
    
    public void addItem(ShoppingToyEntity entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    public void addArray(String json) {
    	try {
    		jsonArray=new JSONArray(json);
    		for(int i=0;i<jsonArray.length();i++){
    			JSONObject object=jsonArray.getJSONObject(i);
    			JSONObject body=object.getJSONObject("body");
    			int type= object.getInt("type");
    			switch (type) {
    			case INDEX_TOY_2:
    				initToy2Data(body);
    				break;
    			default:
    				break;
    			}
    		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public int getItemViewType(int position) {
        try {
            if(position < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(position);
                return object.getInt("type");
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return super.getItemViewType(position);
    }
    
    void initToy2Data(JSONObject json) {
    	try {
			JSONArray array = json.getJSONArray("toys");
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				ShoppingToyEntity entity = new ShoppingToyEntity();
				entity.title = object.getString("title");
				entity.banner = object.getJSONArray("banner").toString();
				entity.toys = object.getJSONArray("toys").toString();
				entity.banner_size = Float.parseFloat(object.getString("size").toString());
			    addItem(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    	try {
			JSONObject object=jsonArray.getJSONObject(position);
			int type=object.getInt("type");
			switch (type) {
			case INDEX_TOP_BANNER:
			case INDEX_TOY_1:
				onBindViewHolderTopBanner(viewHolder,object.getJSONObject("body"));
				break;
			case INDEX_TOY_2:
				onBindViewHolderToy(viewHolder,object);
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    void onBindViewHolderTopBanner(RecyclerView.ViewHolder viewHolder,JSONObject body) {
        TopBannerViewHolder holder = (TopBannerViewHolder)viewHolder;
        try {
            JSONArray array = body.getJSONArray("banners");
            holder.banner.loadBannerSlide(null, array.toString());
            
            JSONArray json = body.getJSONArray("icons");
            BarIconAdapter adapter = new BarIconAdapter(activity);
            adapter.addArray(json.toString());
            holder.icon_gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            
            JSONArray array_saletime = body.getJSONArray("saletime");
            SaletimeAdapter adapter_saletime = new SaletimeAdapter(activity);
            adapter_saletime.addArray(array_saletime.toString());
            System.out.println("----saletime:"+array_saletime.toString());
            holder.shopping_saletime.setAdapter(adapter_saletime);
            adapter_saletime.notifyDataSetChanged();
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void onBindViewHolderToy(RecyclerView.ViewHolder viewHolder,JSONObject object) {
    	ToyListViewHolder holder=(ToyListViewHolder)viewHolder;
    		try {
				if (object.getInt("type") == 2){
					JSONArray toys_array = object.getJSONObject("body").getJSONArray("toys");
					ToyListAdapter adapter=new ToyListAdapter(activity);
					adapter.addArray(toys_array.toString());
					holder.toy_list.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					
					LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)holder.toy_list.getLayoutParams();
			        localLayoutParams.height = (toys_array.length() * LocalUtils.dip2px(this.activity, 450.0F));
			        holder.toy_list.setLayoutParams(localLayoutParams);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType) {
            case 0:
            {
                view = LayoutInflater.from(activity).inflate(R.layout.layout_shopping_top_banner, parent, false);
                return new TopBannerViewHolder(view);
            }
            case 1:
            case 2:
            {
                view = LayoutInflater.from(activity).inflate(R.layout.layout_shopping_toy_list, parent, false);
                return new ToyListViewHolder(view);
            }
        }
        return null;
    }
}