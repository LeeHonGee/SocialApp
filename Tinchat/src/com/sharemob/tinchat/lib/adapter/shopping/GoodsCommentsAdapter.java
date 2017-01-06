package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.photoview.NoScrollGridAdapter;
import android.content.Context;
import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;
import com.sharemob.tinchat.lib.photoview.NoScrollGridView;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class GoodsCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<GoodsComments> list = new ArrayList<GoodsComments>();
    
    public GoodsCommentsAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
        return list.size();
    }
    
    public void addItem(GoodsComments entity) {
        list.add(entity);
        int count = getItemCount();
        notifyItemInserted(count);
    }
    
    public void addArray(String json) {
    	try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				GoodsComments comments=new GoodsComments();
				comments.content=object.getString("content");
				comments.nickname=object.getString("nickname");
				comments.grade=object.getInt("grade");
				comments.time=object.getLong("time");
				JSONArray urlsArray=object.getJSONArray("urls");
				for(int k=0;k<urlsArray.length();k++){
					JSONObject jsonObject=urlsArray.optJSONObject(k);
					String url=jsonObject.getString("url");
					comments.urls.add(url);
				}
				addItem(comments);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GoodsCommentsViewHolder holder = (GoodsCommentsViewHolder)viewHolder;
        final GoodsComments entity = (GoodsComments)list.get(position);
        holder.time.setText(LocalUtils.getDateFromLong("yyyy-MM-dd", entity.time));
        holder.content.setText(entity.content);
        String anonymity = String.format("%s***%s", 
        		entity.nickname.substring(0x0, 0x1),
        		entity.nickname.substring(entity.nickname.length()-2,entity.nickname.length()-1));
        holder.nickname.setText(anonymity);
        
        NoScrollGridAdapter gridAdapter = new NoScrollGridAdapter(activity,null);
        gridAdapter.setList(entity.urls);
        holder.attach_photos.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        
        holder.attach_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageUtils.imageBrower(activity, position, entity.urls);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        LocalUtils.setListViewHeight(holder.attach_photos, gridAdapter);
        holder.goods_comment_grade.removeAllViews();
        ImageView grade = new ImageView(activity);
//        grade.setBackgroundResource(0x7f0200d7);
        holder.goods_comment_grade.addView(grade);
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_goods_comment, parent, false);
        return new GoodsCommentsViewHolder(view);
    }
}