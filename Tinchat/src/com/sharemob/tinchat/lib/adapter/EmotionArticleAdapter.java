package com.sharemob.tinchat.lib.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;

public class EmotionArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
  private Context context;
  private ArrayList<String> list = new ArrayList<String>();

  public EmotionArticleAdapter(Context context)
  {
    this.context = context;
  }

  public void addArray(String json)
  {
    this.list.clear();
    if (!(json == null) || !("".equals(json))){
		try {
			JSONArray array = new JSONArray(json);
			for(int i=0;i<array.length();i++){
				String entity=array.getJSONObject(i).toString();
				System.out.println(entity);
	    		addItem(entity);
	    	}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
  }

  public void addItem(String entity)
  {
    this.list.add(entity);
    notifyItemInserted(getItemCount());
  }

  public int getItemCount()
  {
    return this.list.size();
  }

  public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int position)
  {
    try
    {
      JSONObject json = new JSONObject(this.list.get(position));
      String cover = json.getString("cover");
      String title = json.getString("title");
      String subtitle = json.getString("subtitle");
      int see = json.getInt("see");
      int comment = json.getInt("comment");
      
      EmotionArticleViewHolder localEmotionArticleViewHolder = (EmotionArticleViewHolder)paramViewHolder;
      ImageLoader.getInstance().displayImage(cover, localEmotionArticleViewHolder.cover);
      localEmotionArticleViewHolder.title.setText(title);
      localEmotionArticleViewHolder.subtitle.setText(subtitle);
      localEmotionArticleViewHolder.seeNum.setText(String.valueOf(see));
      localEmotionArticleViewHolder.commentNum.setText(String.valueOf(comment));
    }catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
	View view=LayoutInflater.from(this.context).inflate(R.layout.emotion_article_title_item, paramViewGroup, false);
   
	return new EmotionArticleViewHolder(view);
  }
}