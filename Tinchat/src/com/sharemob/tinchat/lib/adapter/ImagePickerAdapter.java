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

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.component.LocalButton;
import com.sharemob.tinchat.lib.LocalUtils;
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
public class ImagePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String IMAGEPICKER_TYPE = "type";
	public  static int Limit=9;
	private AppActivity activity;
	private List<ImageEntity> list = new ArrayList<ImageEntity>();
	private ArrayList<CharSequence> pictures = new ArrayList<CharSequence>();
	private LocalButton btn_send,btn_preview,btn_imagePath; 
	private String ActionName=SMGlobal.CHATING_IMAGE_ACTION;
	public int upload_type=SMGlobal.UPLOAD_IMAGE_Avatar;
	public ImagePickerAdapter(AppActivity activity) {
		this.activity = activity;
		loadTakePicture();
	}
	
	public void setLimit(int limit){
		Limit=limit;
	}
	
	public void setActionName(String action_name,int type){
		ActionName=action_name;
		upload_type=type;
	}
	public void addItem(ImageEntity entity) {
		this.list.add(entity);
		int count = getItemCount();
		this.notifyItemInserted(count);
	}
	
	public void setInjectEvent(LocalButton btn_send,LocalButton btn_preview,LocalButton btn_imagepath){
		this.btn_send=btn_send;
		this.btn_preview=btn_preview;
		this.btn_imagePath=btn_imagepath;
	}
	
	private void loadTakePicture(){
		ImageEntity entity=new ImageEntity();
		entity.type=SMGlobal.ImagePicker.TakePicture;
		list.add(entity);
	}
	
	private  void ScanningPhotoAlbum(){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
            ContentResolver mContentResolver = activity.getContentResolver(); 
            // 只查询jpeg和png的图片  
            Cursor mCursor = mContentResolver.query(mImageUri, null,  
                    MediaStore.Images.Media.MIME_TYPE + "=? or "  
                            + MediaStore.Images.Media.MIME_TYPE + "=?",  
                    new String[] { "image/jpeg", "image/png" },  
                    MediaStore.Images.Media.DATE_MODIFIED);
            Log.e("Scanning", mCursor.getCount() + ""); 
            while (mCursor.moveToNext())  
            {  
                // 获取图片的路径  
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                ImageEntity entity=new ImageEntity();
                entity.type=SMGlobal.ImagePicker.Pictures;
                entity.path=String.format("file://%s", path);
                entity.checked=false;
                addItem(entity);
//                btn_imagePath.setText(String.format("%d张照片", this.list.size()));
                Log.i("Scanning", path);  
            }
		}
	}
	
	public void uploadImages(){
		
	}
	
	public void SendSelectedImages(String userid){
		JSONArray array=new JSONArray();
		ArrayList<String> paths=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			ImageEntity entity=list.get(i);
			if(entity.checked){
				try {
					JSONObject object=new JSONObject();
					paths.add(entity.path.replace("file://", ""));
					object.put("filename", entity.path);
					object.put("type", SMGlobal.Chat.Send_Image);
					object.put("uploadImage_type", upload_type);
					array.put(object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				continue;
			}
		}
		if(SMGlobal.UPLOAD_IMAGE_Dynamic==upload_type){
			Intent intent=new Intent(ActionName);
			Bundle bundle=new Bundle();
			bundle.putString(SMGlobal.IMAGE_KEY, array.toString());
			intent.putExtras(bundle);
			activity.sendBroadcast(intent);
		}else if(SMGlobal.UPLOAD_IMAGE_MyPhoto==upload_type||SMGlobal.UPLOAD_IMAGE_Avatar==upload_type){
			LocalUtils.UploadMultipartFile(activity,paths,userid,upload_type,ActionName);
		}
		
		
//		Intent intent=new Intent(ActionName);
//		Bundle bundle=new Bundle();
//		bundle.putString(SMGlobal.IMAGE_KEY, array.toString());
//		bundle.putStringArrayList(SMGlobal.UPLOAD_IMAGE_KEY, paths);
//		intent.putExtras(bundle);
//		activity.sendBroadcast(intent);
	}
	
	
	
	public static void SendTakePicture(Activity activity,String localPath,int upload_type){
//		JSONArray array=new JSONArray();
//		JSONObject object=new JSONObject();
//		try {
//			object.put("filename", localPath);
//			object.put("type", SMGlobal.Chat.Send_Image);
//			array.put(object);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		ArrayList<String> paths=new ArrayList<String>();
		paths.add(localPath);
		LocalUtils.UploadMultipartFile(activity,paths,CacheManager.getInstance().userInfo.uid,upload_type,SMGlobal.CHATING_IMAGE_ACTION);
//		Intent intent=new Intent(SMGlobal.CHATING_IMAGE_ACTION);
//		Bundle bundle=new Bundle();
//		bundle.putString(SMGlobal.IMAGE_KEY, array.toString());
//		bundle.putString("name",uploadFilename);
//		bundle.putString("filename", localPath);
//		intent.putExtras(bundle);
//		System.out.println(array.toString());
//		activity.sendBroadcast(intent);
	}
	
	public void previewSelectedPictures(){
		if(getPictures().size()==0){
			LocalUtils.ToastShort(activity, "当前没有照片可预览,请选择!");
		}else{
			LocalUtils.imageBrower(activity, 0, getPictures());
		}
	}
	
	public void loadPhotoAlbum(){
		LocalUtils.AsyncSingleThread(new LocalUtils.LocalSingleThreadListener() {
			@Override
			public void doTaskExecutor() {
				ScanningPhotoAlbum();
			}
		});
	}
	
	public int getItemViewType(int position) {
		super.getItemViewType(position);
		ImageEntity entity=list.get(position);
		return entity.type;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;
		switch (viewType) {
		case SMGlobal.ImagePicker.TakePicture:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagepicker_item_takepictures, parent, false);
			break;
		case SMGlobal.ImagePicker.Pictures:
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagepicker_item_pictures, parent, false);
			break;
		}
		if(viewType==SMGlobal.ImagePicker.TakePicture){
			return new TakePictureViewHolder(view);
		}else if(viewType==SMGlobal.ImagePicker.Pictures){
			return new PicturesViewHolder(view);
		}
		return null;
	}

	public void onBindViewHolderTakePicture(final RecyclerView.ViewHolder viewholder,ImageEntity entity,final int position) {
		TakePictureViewHolder  viewHolder=(TakePictureViewHolder)viewholder;
		viewHolder.imagepicker_takepicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LocalUtils.photo(activity, SMGlobal.TAKE_PICTURE);
			}
		});
	}
	
	public void onBindViewHolderPictures(final RecyclerView.ViewHolder viewholder,final ImageEntity entity,final int position){
		PicturesViewHolder  viewHolder=(PicturesViewHolder)viewholder;
		final String uri=entity.path;
		ImageLoader.getInstance().displayImage(uri, viewHolder.picture, activity.loadingListener);
		viewHolder.imagepicker_item_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(pictures.size()<Limit){
					if(isChecked){
						entity.checked=true;
						pictures.add(uri);
						listenerBtnEvent();
					}else{
						entity.checked=false;
						pictures.remove(uri);
						listenerBtnEvent();
					}
				}else{
					if(isChecked){
						entity.checked=true;
						buttonView.setChecked(false);
						LocalUtils.ToastShort(activity, String.format("你最多只能选择%d个图片",Limit));
					}else{
						entity.checked=false;
						pictures.remove(uri);
						listenerBtnEvent();
					}
				}
				System.out.println(pictures.size());
			}
		});
		
		if(entity.checked){
			viewHolder.imagepicker_item_check.setChecked(true);
		}else{
			viewHolder.imagepicker_item_check.setChecked(false);
		}
	}
	
	private void listenerBtnEvent(){
		if(btn_send!=null&&btn_preview!=null){
			if(getPictures().size()==0){
				btn_send.setText("发送");
				btn_preview.setText("预览");
			}else{
				btn_send.setText(String.format("发送(%d/%d)", getPictures().size(),Limit));
				btn_preview.setText(String.format("预览(%d/%d)", getPictures().size(),Limit));
			}
		}
	}
	
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		ImageEntity entity=list.get(position);
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
	
	public ImagePickerAdapter setAdapter(ArrayList<ImageEntity> list) {
		this.list = list;
		return this;
	}

	public ArrayList<CharSequence> getPictures(){
		return this.pictures;
	}
}

final class TakePictureViewHolder extends RecyclerView.ViewHolder {
	LinearLayout imagepicker_takepicture;
	TextView imagepicker_tv;
	public TakePictureViewHolder(View view) {
		super(view);
		imagepicker_takepicture=(LinearLayout)view.findViewById(R.id.imagepicker_takepicture);
		imagepicker_tv=(TextView)view.findViewById(R.id.imagepicker_tv);
		
		Drawable drawable =view.getContext().getResources().getDrawable(R.drawable.ic_imagepicker_camera);
		drawable.setBounds(0, 0, drawable.getMinimumWidth()*2, drawable.getMinimumHeight()*2);
		imagepicker_tv.setCompoundDrawables(null, drawable, null, null);
	}
	
}
final class PicturesViewHolder extends RecyclerView.ViewHolder {
	CheckBox imagepicker_item_check;
	ImageView picture;
	public PicturesViewHolder(View view) {
		super(view);
		imagepicker_item_check=(CheckBox)view.findViewById(R.id.imagepicker_item_check);
		picture=(ImageView)view.findViewById(R.id.imagepicker_picture);
	}
}

final class ImageEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String path;
	boolean checked;
	int type;
}
