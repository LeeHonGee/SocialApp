package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import java.text.DecimalFormat;
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

public class RecommendGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private DecimalFormat format = new DecimalFormat("######0.00");
    private ArrayList<RecommendGoods> list = new ArrayList<RecommendGoods>();
    
    public RecommendGoodsAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        return list.size();
    }
    
    public void addItem(RecommendGoods entity) {
        list.add(entity);
        notifyItemInserted(getItemCount());
    }
    
    public void addArray(String json) {
    	try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				RecommendGoods entity=new RecommendGoods();
				entity.icon=object.getString("icon");
				entity.price=String.valueOf(object.getDouble("price"));
				entity.title=object.getString("title");
				addItem(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecommendGoodsViewHolder holder = (RecommendGoodsViewHolder)viewHolder;
        RecommendGoods goods = (RecommendGoods)list.get(position);
        holder.title.setText(goods.title);
        holder.price.setText(String.format("¥%s", goods.price));
        ImageLoader.getInstance().displayImage(goods.icon, holder.icon);
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_recommend_goods, parent, false);
        return new RecommendGoodsViewHolder(view);
    }
}
