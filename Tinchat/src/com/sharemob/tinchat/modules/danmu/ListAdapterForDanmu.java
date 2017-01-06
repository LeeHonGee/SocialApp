/**
 *  文件名:ListAdapterForDanmu.java
 *  修改人:lihangjie
 *  创建时间:2015-11-13 下午11:18:40
 */
package com.sharemob.tinchat.modules.danmu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.NoScrollListview;
import com.sharemob.tinchat.lib.BitmapUtil;
import com.sharemob.tinchat.lib.ImageUtils;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-11-13 下午11:18:40]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class ListAdapterForDanmu extends BaseAdapter {
	private static final String TAG="ListAdapterForDanmu";
	private LayoutInflater mInflater;
	private List<DanmuItem> list = null;
	private Context context;
	private Animation animation;
	private LayoutParams params;
	public ListAdapterForDanmu(Context context) {
		this.context=context;
		this.mInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		this.animation=AnimationUtils.loadAnimation(this.context,(R.anim.component_fade_out));
		this.params= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	}
	public int getCount()
	{
		return list.size();
	}
	public void remove(final View view,final int position){
		deleteCell(view, position);
		
		//fadein这个确实会造成列表大图的闪烁，如果是小图那个效果不太明显
//		list.remove(position);
//		notifyDataSetChanged();
	}
	
	public Object getItem(int position)
	{
		return list.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View view = null; 
		
		if (convertView == null)
		{
			view =mInflater.inflate(R.layout.layout_danmu_item, parent, false); 
			view.setLayoutParams(params);
			findViews(view);
		}
		else if (((ViewHolder)convertView.getTag()).needInflate) {  
            view = mInflater.inflate(R.layout.layout_danmu_item, parent, false);  
            findViews(view);  
        }  
		else{
			view=convertView;
		}
		
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		setView(viewHolder, position);
		return view;
	}

	private void setView(final ViewHolder viewHolder, int position)
	{
		DanmuItem item = list.get(position);
		viewHolder.header_icon.setBackgroundDrawable(ImageUtils.BitmapToDrawable(item.getHeaderIcon()));
		viewHolder.nickname.setText(item.getNickname());
		viewHolder.message.setText(item.getMessage());	

			
	}

	public void setAdapter(List<DanmuItem> list)
	{
		this.list = list;
	}

	private void findViews(View convertView)
	{
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.header_icon = (ImageView) convertView.findViewById(R.id.user_header_icon);
		viewHolder.nickname = (TextView) convertView.findViewById(R.id.user_nickname);
		viewHolder.message=(TextView)convertView.findViewById(R.id.user_message);
		viewHolder.needInflate = false;
		convertView.setTag(viewHolder);
	}

	private void deleteCell(final View view, final int index) {
		animation.setAnimationListener(new AnimationListener() {  
	        public void onAnimationEnd(Animation arg) {  
	        	list.remove(index);
	        	
	        	ViewHolder vh = (ViewHolder)view.getTag(); 
	        	vh.needInflate = true;
	            notifyDataSetChanged();
	        }
	        public void onAnimationRepeat(Animation animation) {}  
	        public void onAnimationStart(Animation animation) {}  
	    });
		animation.setDuration(1000);
	    view.startAnimation(animation);
	    
	    
	    
	    
//	    AnimationListener listener=new AnimationListener() {  
//	        public void onAnimationEnd(Animation arg) {  
//	        	list.remove(index);
//	        	
//	        	ViewHolder vh = (ViewHolder)view.getTag(); 
//	        	vh.needInflate = true;
//	            notifyDataSetChanged();
//	        }
//	        public void onAnimationRepeat(Animation animation) {}  
//	        public void onAnimationStart(Animation animation) {}  
//	    };
//	    collapse(view, listener);
	}  
	
	private void collapse(final View v, AnimationListener al) {  
	    final int initialHeight = v.getMeasuredHeight();
	    final int ANIMATION_DURATION=1000;
	    Animation anim = new Animation() {  
	        protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {  
	            if (interpolatedTime == 1) {  
	                v.setVisibility(View.GONE);  
	            }  
	            else {  
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);  
	                v.requestLayout();  
	            }  
	        }  
	        public boolean willChangeBounds() {  
	            return true;  
	        }  
	    };  
	  
	    if (al!=null) {  
	        anim.setAnimationListener(al);  
	    }  
	    anim.setDuration(ANIMATION_DURATION);  
	    v.startAnimation(anim);  
	}  
	
	private  final class ViewHolder
	{
		ImageView header_icon;
		TextView nickname;
		TextView message;
		boolean needInflate;
	}


}
