package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.modules.shopping.GoodsActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class SaletimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<Saletime> list = new ArrayList<Saletime>();
    
    public SaletimeAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        return list.size();
    }
    
    public void addItem(Saletime entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    public void addArray(String json) {
    		this.list.clear();
    		try {
    			JSONArray array=new JSONArray(json);
    			for(int i=0;i<array.length();i++){
    				JSONObject object=array.getJSONObject(i);
    				Saletime saletime=new Saletime();
    				saletime.costprice=object.getDouble("costprice");
    				saletime.price=object.getDouble("price");
    				saletime.icon=object.getString("icon");
    				saletime.saveprice=saletime.costprice-saletime.price;
    				addItem(saletime);
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SaletimeViewHolder holder = (SaletimeViewHolder)viewHolder;
        Saletime saletime = (Saletime)list.get(position);
        holder.costprice.setText(String.format("¥%d",(int)saletime.costprice));
        holder.price.setText(String.format("¥%d",(int)saletime.price));
        holder.saveprice.setText(String.format("立省¥%d",(int)saletime.saveprice));
        
        ImageLoader.getInstance().displayImage(saletime.icon, holder.image);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocalUtils.gotoView(activity, GoodsActivity.class);
            }
        });
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_sale_time_item, parent, false);
        return new SaletimeViewHolder(view);
    }
}