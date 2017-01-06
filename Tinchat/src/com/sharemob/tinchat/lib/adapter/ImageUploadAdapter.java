/**
 *  文件名:ImagePickerActivity.java
 *  修改人:lihangjie
 *  创建时间:2016-6-22 上午11:26:30
 */
package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.provider.CalendarContract.CalendarAlerts;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-6-22 上午11:26:30]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class ImageUploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String IMAGEPICKER_TYPE = "type";
	public int upload_type;
	public String action;
	private AppActivity activity;
	private List<UploadPhotoEntity> list = new ArrayList<UploadPhotoEntity>();
	
	public ImageUploadAdapter(AppActivity activity,String action,int upload_type) {
		this.activity = activity;
		this.action=action;
		this.upload_type=upload_type;
		loadTakePicture();
	}
	
	public void addItem(UploadPhotoEntity entity) {
		this.list.add(entity);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}
	
	public void addArray(String json){
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				UploadPhotoEntity entity=new UploadPhotoEntity();
				if(object.getString("filename").startsWith("file:")){
					entity.path=object.getString("filename");
				}else{
					String path=CacheManager.getInstance().userInfo.server_addr+object.getString("filename");
					entity.path=path;
					entity.name=object.getString("filename");
				}
				
				if(object.has("ID")){
					entity.id=object.getInt("ID");
				}
				entity.type=SMGlobal.ImagePicker.Pictures;
				entity.upload_type=upload_type;
//				System.out.println(object.toString());
				addItem(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadTakePicture(){
		UploadPhotoEntity entity=new UploadPhotoEntity();
		entity.type=SMGlobal.ImagePicker.TakePicture;
		entity.upload_type=upload_type;
		list.add(entity);
	}
	
	public int getItemViewType(int position) {
		super.getItemViewType(position);
		UploadPhotoEntity entity=list.get(position);
		return entity.type;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;
		switch (viewType) {
		case SMGlobal.ImagePicker.TakePicture:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploadphoto_item_takepictures, parent, false);
			break;
		case SMGlobal.ImagePicker.Pictures:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploadphoto_item_pictures, parent, false);
			break;
		}
		if(viewType==SMGlobal.ImagePicker.TakePicture){
			return new UploadTakePhotoViewHolder(view);
		}else if(viewType==SMGlobal.ImagePicker.Pictures){
			return new UploadPhotoViewHolder(view);
		}
		return null;
	}

	public void onBindViewHolderTakePicture(final RecyclerView.ViewHolder viewholder,final UploadPhotoEntity entity,final int position) {
		UploadTakePhotoViewHolder  viewHolder=(UploadTakePhotoViewHolder)viewholder;
		viewHolder.uploadphoto_takepicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					JSONObject object=new JSONObject();
					object.put("action",action);
					object.put("type", upload_type);
					object.put("upload_type",upload_type);
					Class<?> clazz = Class.forName(AppActivity.class.getName());
					Intent intent = new Intent();
					intent.putExtra(AppActivity.NAME, "ImagePickerActivity");
					intent.putExtra(AppActivity.PARAMETERS,object.toString());
					intent.setClass(activity, clazz);
					activity.startActivity(intent);
					activity.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ArrayList<CharSequence> getMyPhotosLocalPath(){
		ArrayList<CharSequence> urls=new ArrayList<CharSequence>();
		for(UploadPhotoEntity entity:list){
			if(entity.type==SMGlobal.ImagePicker.Pictures){
				urls.add(entity.path);
			}
		}
		return urls;
	}
	
	public ArrayList<CharSequence> getMyPhotosFilename(){
		ArrayList<CharSequence> urls=new ArrayList<CharSequence>();
		for(UploadPhotoEntity entity:list){
			if(entity.type==SMGlobal.ImagePicker.Pictures){
				urls.add(entity.path);
			}
		}
		return urls;
	}
	public void deleteCurrentPhoto(final int position,final int photobookID,final String name){
		LocalUtils.localAlertDialog(activity,new String[]{null,"您确定要删除当前照片?","确定","取消"},
				new OnClickListener() {
					public void onClick(View v) {
						daleteData(position, photobookID,name);
					}
				},
				new OnClickListener() {
					public void onClick(View v) {
						System.out.println("----------取消");
					}
				});
	}
	public void onBindViewHolderPictures(final RecyclerView.ViewHolder viewholder,final UploadPhotoEntity entity,final int position){
		UploadPhotoViewHolder  viewHolder=(UploadPhotoViewHolder)viewholder;
		final String uri=entity.path;
		viewHolder.picture.setScaleType(ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(uri, viewHolder.picture,GlobalParams.getInstance().round_options);
		 viewHolder.picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageUtils.imageBrower(activity.getContext(), 0, getMyPhotosLocalPath());
			}
		});
		 
			 viewHolder.uploadimage_item_delete.setVisibility(View.VISIBLE);
			 viewHolder.uploadimage_item_delete.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						switch (entity.upload_type) {
						case SMGlobal.UPLOAD_IMAGE_Dynamic:
							daleteData(position);
							break;
						case SMGlobal.UPLOAD_IMAGE_MyPhoto:
							deleteCurrentPhoto(position,entity.id,entity.name);
							break;
						default:
							break;
						}
					}
			 });
	}
	
	public void daleteData(int position) {
		list.remove(position);
		notifyItemRemoved(position);
		notifyItemChanged(position);
	}
	
	public void daleteData(int position,final int photobookID,String name) {
		daleteData(position);
		JSONObject object=new JSONObject();
		try {
//			object.put("ID", photobookID);
			object.put("name", name);
			object.put("uid", CacheManager.getInstance().userInfo.uid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data=object.toString();
		String url=String.format("http://%s/api/user?cmd=%d&data=%s",CacheManager.IP,10027,data);
		System.out.println(url);
		CacheManager.requestHttp(url, new RequestCallback() {
			@Override
			public void requestDidFinished(String body) {
				System.out.println(body);
				CacheManager.getInstance().userInfo.deletePhotobook(photobookID);
			}
			@Override
			public void requestDidFailed() {
				
			}
		});
	}
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		UploadPhotoEntity entity=list.get(position);
			switch (entity.type) {
			case SMGlobal.ImagePicker.TakePicture:
				onBindViewHolderTakePicture(viewHolder, entity, position);
				break;
			case SMGlobal.ImagePicker.Pictures:
				onBindViewHolderPictures(viewHolder, entity, position);
				break;
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
	
	public ImageUploadAdapter setAdapter(ArrayList<UploadPhotoEntity> list) {
		this.list = list;
		return this;
	}
//
//	public ArrayList<CharSequence> getPictures(){
//		return this.pictures;
//	}
}

final class UploadTakePhotoViewHolder extends RecyclerView.ViewHolder {
	LinearLayout uploadphoto_takepicture;
	ImageView uploadphoto;
	public UploadTakePhotoViewHolder(View view) {
		super(view);
		uploadphoto_takepicture=(LinearLayout)view.findViewById(R.id.uploadphoto_takepicture);
		uploadphoto=(ImageView)view.findViewById(R.id.uploadphoto);
	}
	
}
final class UploadPhotoViewHolder extends RecyclerView.ViewHolder {
	ImageView picture;
	ImageView uploadimage_item_delete;
	public UploadPhotoViewHolder(View view) {
		super(view);
		picture=(ImageView)view.findViewById(R.id.uploadphoto_picture);
//		picture.setImageBitmap(bm)
//		picture.setScaleType(ScaleType.FIT_XY);
		uploadimage_item_delete=(ImageView)view.findViewById(R.id.uploadimage_item_delete);
	}
}

final class UploadPhotoEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String path;
	String name;
	int id;
	int type;
	int upload_type;
}
