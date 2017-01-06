/**
 *  文件名:DynamicItemAdapter.java
 *  修改人:lihangjie
 *  创建时间:2016-2-23 下午11:13:04
 */
package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AlertDialog;
import com.sharemob.tinchat.component.AlertDialog.OnDialogListener;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.component.LocalButton;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.photoview.NoScrollGridAdapter;
import com.sharemob.tinchat.lib.photoview.NoScrollGridView;
import com.sharemob.tinchat.lib.refreashtabview.extras.PullLoadMoreRecyclerView;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-2-23 下午11:13:04]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class DynamicArticleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public static final byte Article=0;
	public static final byte Comment=1;
	public static final byte Header = 3;
	public int position; 
//	private AppActivity appActivity;
//	private Activity activity;
	private Context context;
	public boolean onClickItem = true;
	private PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
	private ArrayList<String> list=new ArrayList<String>();
	public int currentPage=0;
//	public DynamicArticleItemAdapter(AppActivity activity) {
//		this.appActivity = activity;
//		this.context=appActivity.getContext();
//	}
//	
//	public DynamicArticleItemAdapter(Activity activity) {
//		this.activity = activity;
//		this.context=activity;
//	}
	
	public DynamicArticleItemAdapter(Context context) {
//		this.activity = activity;
		this.context=context;
	}
	
	public void addHeader(String json) {
		list.add(0x0, json);
		int count = getItemCount();
		notifyItemInserted(count);
	}
	
	public void clear(){
		this.list.clear();
	}
	public void setPullLoadMoreRecyclerView(PullLoadMoreRecyclerView pullLoadMoreRecyclerView){
		this.pullLoadMoreRecyclerView=pullLoadMoreRecyclerView;
	}
	
	public void addItem(String entity) {
		this.list.add(entity);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}
	
	public void addArrayComment(String json){
		if(json==null||"null".equals(json)){
			return;
		}
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				if(Comment==Integer.parseInt(object.getString("type"))){
					addItem(object.toString());
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void addArray(String json){
		if(json==null||"".equals(json)){
			return;
		}
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				addItem(object.toString());
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void daleteData(int position) {
		list.remove(position);
		notifyItemRemoved(position);
		notifyItemChanged(position);
	}

	public void daleteData(int position,int ID) {
		daleteData(position);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ID", String.valueOf(ID));
		LocalUtils.requestHttp(10050,map,new RequestCallback() {
			@Override
			public void requestDidFinished(String body) {
				
			}
			@Override
			public void requestDidFailed() {
				
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view =null;
		switch (viewType) {
		case Article:
//			if(appActivity!=null){
//				view=LayoutInflater.from(this.appActivity).inflate(R.layout.layout_item_dynamic, parent, false);
//			}else{
				view=LayoutInflater.from(context).inflate(R.layout.layout_item_dynamic, parent, false);
//			}
			return new DynamicArticleViewHolder(view);
		case Comment:
//			if(appActivity!=null){
//				view = LayoutInflater.from(appActivity).inflate(R.layout.dynamic_comment_item, parent, false);
//			}else{
				view=LayoutInflater.from(context).inflate(R.layout.dynamic_comment_item, parent, false);
//			}
			return new DynamicCommentViewHolder(view);
		case Header:
			view=LayoutInflater.from(context).inflate(R.layout.dynamic_item_header, parent, false);
			return new DynamicHeaderViewHolder(view);
		default:
			break;
		}
		return null;
	}
	public int getItemViewType(int position) {
		if(position<list.size()){
			try {
				JSONObject object = new JSONObject(list.get(position));
				int type = Integer.parseInt(object.getString("type"));
				return type;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return super.getItemViewType(position);
	}
	public void onBindViewHolderDynamicArticle(final RecyclerView.ViewHolder viewHolder,final DynamicArticleEntity entity,final int position) {
		DynamicArticleViewHolder  holder=(DynamicArticleViewHolder)viewHolder;
		ImageLoader.getInstance().displayImage(String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,entity.avatar), holder.avatar, GlobalParams.getInstance().round_options);
		holder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("uid", entity.uid);
				LocalUtils.enterAppActivity(context, map, "MyspaceActivity");
			}
		});
		
		try {
			holder.nickname.setText(URLDecoder.decode(entity.nickname, "UTF-8"));
			holder.content.setText(URLDecoder.decode(entity.content, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		holder.location.setText(entity.location);
		
		if(Gender.MALE.ordinal()==entity.sex){
			holder.sex_age.setBackgroundResource(R.drawable.gender_boy);
		}else if(Gender.FEMALE.ordinal()==entity.sex){
			holder.sex_age.setBackgroundResource(R.drawable.gender_girl);
		}
		
		holder.sex_age.setText(entity.age);
		holder.time.setText(LocalUtils.getIntervalFormat(entity.time));
		holder.tv_dianzan.setText(String.valueOf(entity.dianZan));
		
		holder.tv_see.setText(String.format("%d 阅读", entity.see));
		holder.tv_dianzan.setText(String.format("%d 赞", entity.dianZan));
		holder.tv_comment.setText(String.format("%d 评价", entity.comment));
		if (entity.imageList == null || entity.imageList.size() == 0) {
			// 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
		}else {
			// 点击回帖九宫格，查看大图
//			NoScrollGridAdapter gridAdapter=null;
//			if(appActivity!=null){
//				gridAdapter=new NoScrollGridAdapter(appActivity,appActivity.loadingListener);
//			}else{
			NoScrollGridAdapter	gridAdapter=new NoScrollGridAdapter(context,null);
//			}
			
			gridAdapter.setList(entity.imageList);
			
			holder.gridview.setAdapter(gridAdapter);
			holder.gridview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ImageUtils.imageBrower(context, position, entity.imageList);
					((Activity) context).overridePendingTransition(R.anim.in_from_zoom,R.anim.animi_remain);
				}
			});
			LocalUtils.setListViewHeight(holder.gridview,gridAdapter);
		}
		
		holder.dynamic_btn_comment.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("dynamicItemID",entity.ID);
				LocalUtils.enterAppActivity(context, map, "DynamicArticleItemActivity");
			}
		});
		
		holder.btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog=new AlertDialog(context, new OnDialogListener() {
					@Override
					public void delete() {
						daleteData(position, entity.ID);
					}
				});
				alertDialog.show();
			}
		});
		
	}
	
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		this.position=position;
		if(pullLoadMoreRecyclerView!=null){
			if(position==0){
				pullLoadMoreRecyclerView.setSwipeRefreshEnable(true);
			}else{
				pullLoadMoreRecyclerView.setSwipeRefreshEnable(false);
			}
		}
	
		
		if(list.size()<=position){
			return ;
		}
		try {
			String json=list.get(position);
			JSONObject object = new JSONObject(json);
			int type = object.getInt("type");
			switch (type) {
			case Article:
					DynamicArticleEntity article=new DynamicArticleEntity();
					article.uid=object.getString("uid");
					article.ID=object.getInt("ID");
					article.avatar=object.getString("avatar");
					article.nickname=object.getString("nickname");
					article.birthday=object.getString("birthday");
					article.sex=object.getInt("sex");
					article.age=LocalUtils.calculateDatePoor(article.birthday);
					article.content=object.getString("content");
					article.location=object.getString("location");
					
			        if (object.has("comment")){
			        	article.comment = Integer.parseInt(object.getString("comment"));
			        }
			        article.like = Integer.parseInt(object.getString("like"));
			        article.see = object.getInt("see");
					article.dianZan=object.getInt("dianZan");
					article.time=object.getString("createTime");
					System.out.println(object.getString("images"));
					JSONArray imageArray=new JSONArray(object.getString("images"));
					final ArrayList<CharSequence> imageList = new ArrayList<CharSequence>();
					for(int k=0;k<imageArray.length();k++){
						String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr,imageArray.getString(k));
						imageList.add(url);
					}
					article.imageList=imageList;
				onBindViewHolderDynamicArticle(viewHolder, article, position);
				break;
			case Comment:
				DynamicCommentEntity comment=new DynamicCommentEntity();
				comment.avatar=object.getString("avatar");
				comment.nickname=object.getString("nickname");
				comment.birthday=object.getString("birthday");
				comment.server_addr=CacheManager.getInstance().userInfo.server_addr;
				comment.sex=object.getInt("sex");
				comment.age=LocalUtils.calculateDatePoor(comment.birthday);
				
				comment.uid = object.getString("dcid");
				comment.content=object.getString("content");
				comment.time=object.getString("createTime");
				onBindViewHolderDynamicComment(viewHolder, comment, position);
				break;
			case Header:
			    DynamicHeaderEntity localDynamicHeaderEntity = new DynamicHeaderEntity();
			    localDynamicHeaderEntity.count = object.getString("count");
			    localDynamicHeaderEntity.read_times = object.getString("read_times");
			    localDynamicHeaderEntity.avatar = object.getString("avatar");
			    onBindViewHolderDynamicHeader(viewHolder, localDynamicHeaderEntity, position);
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onBindViewHolderDynamicHeader(RecyclerView.ViewHolder viewHolder,final DynamicHeaderEntity entity,final int position){
		DynamicHeaderViewHolder holder = (DynamicHeaderViewHolder) viewHolder;
//		localDynamicHeaderViewHolder.avatar.setType(1);
//		localDynamicHeaderViewHolder.avatar.setBorderRadius(5);
		ImageLoader localImageLoader = ImageLoader.getInstance();
		localImageLoader.displayImage(String.format("%s%s", CacheManager.getInstance().userInfo.server_addr,entity.avatar),
				holder.avatar,
				GlobalParams.getInstance().defaultOptions);
		holder.read_times
				.setText(entity.read_times);
		holder.count
				.setText(entity.count);
		
		
	}
	public void onBindViewHolderDynamicComment(RecyclerView.ViewHolder viewHolder,final DynamicCommentEntity entity,final int position) {
		DynamicCommentViewHolder  holder=(DynamicCommentViewHolder)viewHolder;
		holder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Map<String,Object> map=new HashMap<String, Object>();
//				map.put("nickname", entity.nickname);
//				map.put("uid", entity.uid);
//				map.put("server_addr",entity.server_addr);
//				String url=String.format("%s%s",entity.server_addr,entity.avatar);
//				map.put("avatar",url);
//				LocalUtils.enterAppActivity(activity, map, "ChatingActivity");
			}
		});
		
		ImageLoader.getInstance().displayImage(String.format("%s%s",entity.server_addr,entity.avatar), holder.avatar, GlobalParams.getInstance().circle_options);
		holder.content.setText(entity.content);
		holder.nickname.setText(entity.nickname);
		holder.time.setText(LocalUtils.getIntervalFormat(entity.time));
		if(Gender.MALE.ordinal()==entity.sex){
			holder.sex_age.setBackgroundResource(R.drawable.gender_boy);
		}else if(Gender.FEMALE.ordinal()==entity.sex){
			holder.sex_age.setBackgroundResource(R.drawable.gender_girl);
		}
		
		holder.sex_age.setText(entity.age);
		holder.rootView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AlertDialog alertDialog=new AlertDialog(context, new OnDialogListener() {
					@Override
					public void delete() {
						daleteData(position);
						Map<String,Object> map=new HashMap<String,Object>();
//						map.put("uniqueId", entity.uniqueId);
						LocalUtils.requestHttp(10052,map,new RequestCallback() {
							@Override
							public void requestDidFinished(String body) {
								
							}
							
							@Override
							public void requestDidFailed() {
								
							}
						});
					}
				});
				alertDialog.show();
				return false;
			}
		});
	}
	@Override
	public int getItemCount() {
		return list.size();
	}
}

class DynamicArticleViewHolder extends RecyclerView.ViewHolder{
	  ImageView avatar;
	  LocalButton btn_attention;
	  LinearLayout btn_more;
	  TextView content;
	  ImageButton dynamic_btn_comment;
	  ImageButton dynamic_btn_dianzan;
	  ImageView dynamic_iv;
	  NoScrollGridView gridview;
	  TextView location;
	  TextView nickname;
	  View rootView;
	  TextView sex_age;
	  TextView time;
	  TextView tv_comment;
	  TextView tv_dianzan;
	  TextView tv_see;
	public DynamicArticleViewHolder(View view) {
		super(view);
	    this.rootView = view;
	    this.btn_attention = ((LocalButton)view.findViewById(R.id.dynamic_btn_attention));
	    this.btn_attention.setFillet(Boolean.valueOf(true));
	    this.btn_attention.setRadius(10.0F);
	    this.btn_attention.setBackColorSelected("#ffdc1a");
	    this.btn_attention.setBackColor("#ffdc1a");
	    this.avatar = ((ImageView)view.findViewById(R.id.dynamic_iv_header));
	    this.nickname = LocalUtils.getFindViewById(view, R.id.dynamic_tv_nickname);
	    this.content = LocalUtils.getFindViewById(view,R.id.dynamic_tv_content);
	    this.tv_see = LocalUtils.getFindViewById(view,R.id.dynamic_tv_see);
	    this.time = LocalUtils.getFindViewById(view,R.id.dynamic_tv_time);
	    this.tv_dianzan = LocalUtils.getFindViewById(view,R.id.dynamic_tv_dianzan);
	    this.tv_comment = LocalUtils.getFindViewById(view, R.id.dynamic_tv_comment);
	    this.sex_age = LocalUtils.getFindViewById(view, R.id.dynamic_user_sex_age);
	    this.dynamic_btn_dianzan = ((ImageButton)view.findViewById(R.id.dynamic_btn_dianzan));
	    this.dynamic_btn_comment = ((ImageButton)view.findViewById(R.id.dynamic_btn_comment));
	    this.location = LocalUtils.getFindViewById(view, R.id.dynamic_tv_location);
	    this.gridview = ((NoScrollGridView)view.findViewById(R.id.dynamic_gridview));
	    this.dynamic_iv = ((ImageView)view.findViewById(R.id.dynamic_iv));
	    this.btn_more = ((LinearLayout)view.findViewById(R.id.dynamic_btn_more));
	}
}

final class DynamicArticleEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	  int ID;
	  String age;
	  String avatar;
	  String birthday;
	  int comment;
	  String content;
	  int dianZan;
	  ArrayList<CharSequence> imageList;
	  String images;
	  int like;
	  String location;
	  String nickname;
	  int see;
	  int sex;
	  String time;
	  String uid;
}

final class DynamicHeaderViewHolder extends RecyclerView.ViewHolder{
	  ImageView avatar;
	  TextView count;
	  TextView read_times;
	  View rootView;

	  public DynamicHeaderViewHolder(View paramView)
	  {
	    super(paramView);
	    this.rootView = paramView;
	    this.avatar = ((ImageView)paramView.findViewById(R.id.mine_dynamic_avatar));
	    this.count = ((TextView)paramView.findViewById(R.id.mine_dynamic_count));
	    this.read_times = ((TextView)paramView.findViewById(R.id.mine_dynamic_read_times));
	  }
}

final class DynamicCommentViewHolder extends RecyclerView.ViewHolder{
	View rootView;
	ImageView avatar;
	TextView content;
	TextView nickname;
	TextView sex_age;
	TextView time;
	
	public DynamicCommentViewHolder(View view) {
		super(view);
		rootView=view;
		avatar= (ImageView) view.findViewById(R.id.comment_avatar);
		nickname =LocalUtils.getFindViewById(view,R.id.comment_nickname);
		sex_age=LocalUtils.getFindViewById(view, R.id.comment_sex_age);
		content = LocalUtils.getFindViewById(view,R.id.comment_content);
		time=LocalUtils.getFindViewById(view, R.id.comment_time);
		
	}
}

final class DynamicCommentEntity implements Serializable{
	  private static final long serialVersionUID = 1L;
	  String age;
	  String avatar;
	  String birthday;
	  String content;
	  String location;
	  String nickname;
	  String server_addr;
	  int sex;
	  String time;
	  String uid;
}

final class DynamicHeaderEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String avatar;
	String count;
	String read_times;
}