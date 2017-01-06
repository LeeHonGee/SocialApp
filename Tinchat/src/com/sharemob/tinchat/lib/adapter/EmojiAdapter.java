package com.sharemob.tinchat.lib.adapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.SMGlobal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class EmojiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private List<EmojiEntity> list=new ArrayList<EmojiEntity>();
//	public static String EMOJI_ACTION="com.sharemob.android.EMOJI_ACTION";
//	public static String EMIJI_KEY="EMOJI_KEY";
	public static String EMIJI="EMOJI";
	private Activity activity;
	
	public EmojiAdapter(Activity activity) {
		this.activity = activity;
//		AsyncLoadResourceToAdapterList();
	}
	public void addItem(EmojiEntity entity) {
		this.list.add(entity);
		int count = getItemCount();
		this.notifyItemInserted(count);
		
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_item, parent, false);
		
		return new EmojiViewHolder(view);
	}

	public void AsyncLoadResourceToAdapterList(){
		LocalUtils.AsyncSingleThread(new LocalUtils.LocalSingleThreadListener() {
			@Override
			public void doTaskExecutor() {
				LocalUtils.emoji(activity, new LocalUtils.LocalEmojiListener() {
				@Override
				public void loadImage(String name) {
					EmojiEntity entity=new EmojiEntity();
					entity.character="emojis"+File.separator+name;
					addItem(entity);
				}
			});
			}
		});
	}
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder _viewHolder, int position) {
		final EmojiEntity entity=list.get(position);
		final EmojiViewHolder  viewHolder=(EmojiViewHolder)_viewHolder;
		viewHolder.emoji_iv.setTag(entity);
		ImageUtils.dispalyFromAssets(entity.character, viewHolder.emoji_iv);
		
		viewHolder.emoji_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			String character=entity.character.replace("emojis/emoji_", "").replace(".png", "");
//			System.out.println(character);
			
			Intent intent=new Intent(SMGlobal.CHATING_EMOJI_ACTION);
			Bundle bundle=new Bundle();
			
//			viewHolder.emoji_iv.setDrawingCacheEnabled(true);
//			Bitmap src=viewHolder.emoji_iv.getDrawingCache();
//			Bitmap bitmap = Bitmap.createBitmap(src);
//			viewHolder.emoji_iv.setDrawingCacheEnabled(false);
//			bundle.putParcelable(EMIJI, bitmap);
			bundle.putString(SMGlobal.EMIJI_KEY, character);
			intent.putExtras(bundle);
			activity.sendBroadcast(intent);
//			bitmap.recycle();
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
	
}

final class EmojiViewHolder extends RecyclerView.ViewHolder {
	ImageView emoji_iv;
	public EmojiViewHolder(View view) {
		super(view);
		emoji_iv=(ImageView)view.findViewById(R.id.emoji_iv);
	}
}

final class EmojiEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	int id;
	String character;
}