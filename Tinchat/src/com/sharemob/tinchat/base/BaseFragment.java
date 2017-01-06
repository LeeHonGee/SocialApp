/**
 *  文件名:BaseActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-8-29 下午10:57:10
 */
package com.sharemob.tinchat.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.sharemob.tinchat.R;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-8-29 下午10:57:10]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public abstract class BaseFragment extends Fragment {
//	public static final int TAKE_PICTURE = 0;
//	public static final int RESULT_LOAD_IMAGE = 1;
//	public static final int CUT_PHOTO_REQUEST_CODE = 2;
//	public static final int SELECTIMG_SEARCH = 3;
//	public String path = "";
//	public Uri photoUri;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	
//	public void photo() {
//		try {
//			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//			String sdcardState = Environment.getExternalStorageState();
//			String sdcardPathDir = Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
//			File file = null;
//			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
//				// 有sd卡，是否有myImage文件夹
//				File fileDir = new File(sdcardPathDir);
//				if (!fileDir.exists()) {
//					fileDir.mkdirs();
//				}
//				// 是否有headImg文件
//				file = new File(sdcardPathDir + System.currentTimeMillis()+ ".JPEG");
//			}
//			if (file != null) {
////				String path = file.getPath();
////				photoUri = Uri.fromFile(file);
//				Uri photoUri = Uri.fromFile(file);
//				System.out.println("----------------"+file.getPath());
//				Bundle bundle=new Bundle();
//				bundle.putString(SMGlobal.PICTURE_PATH, file.getPath());
//				openCameraIntent.putExtras(bundle);
//				
//				Bundle bundle2=openCameraIntent.getExtras();
//				String path=bundle2.getString(SMGlobal.PICTURE_PATH);
//				System.out.println("----------------"+path);
//				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//
//				startActivityForResult(openCameraIntent, SMGlobal.TAKE_PICTURE);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	public void startPhotoZoom(Uri uri) {
//		try {
//			// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
//			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
//			String address = sDateFormat.format(new java.util.Date());
//			if (!FileUtils.isFileExist("")) {
//				FileUtils.createSDDir("");
//
//			}
//			drr.add(FileUtils.SDPATH + address + ".JPEG");
//			Uri imageUri = Uri.parse("file:///sdcard/formats/" + address+ ".JPEG");
//
//			final Intent intent = new Intent("com.android.camera.action.CROP");
//
//			// 照片URL地址
//			intent.setDataAndType(uri, "image/*");
//
//			intent.putExtra("crop", "true");
//			intent.putExtra("aspectX", 1);
//			intent.putExtra("aspectY", 1);
//			intent.putExtra("outputX", 480);
//			intent.putExtra("outputY", 480);
//			// 输出路径
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//			intent.putExtra(SMGlobal.PICTURE_PATH,imageUri.getPath());
//			// 输出格式
//			intent.putExtra("outputFormat",
//					Bitmap.CompressFormat.JPEG.toString());
//			// 不启用人脸识别
//			intent.putExtra("noFaceDetection", false);
//			intent.putExtra("return-data", false);
//			startActivityForResult(intent, SMGlobal.CUT_PHOTO_REQUEST_CODE);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
//				.hideSoftInputFromWindow(getActivity()
//						.getCurrentFocus().getWindowToken(),
//						InputMethodManager.HIDE_NOT_ALWAYS);
//		if (arg2 == bmp.size()) {
//			String sdcardState = Environment.getExternalStorageState();
//			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
////				new PopupWindows(PublishDynamicActivity.this, gridview);
//				Utils.selectedPhotoBox(getActivity(),new OnSheetItemClickListener(){
//					public void onClick(int which) {
//						switch (which) {
//						case 1:
//							System.out.println("--拍照---");
//							photo();
//							break;
//						case 2:
//							System.out.println("--本地相册---");
//							Intent i = new Intent(
//									Intent.ACTION_PICK,
//									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//							startActivityForResult(i, SMGlobal.RESULT_LOAD_IMAGE);
//							break;
//						default:
//							break;
//						}
//					}
//				});
//				
//			} else {
//				Toast.makeText(getActivity(), "sdcard已拔出，不能选择照片",
//						Toast.LENGTH_SHORT).show();
//			}
//		} else {
//			Intent intent = new Intent(getActivity(),SelectBrowsePhotoActivity.class);
//
//			intent.putExtra("ID", arg2);
//			startActivity(intent);
//		}
//	}
	
	protected TextView tv_title=null;
    protected void setTitle(String title_name){
    	tv_title=(TextView)this.getActivity().findViewById(R.id.tv_title);
    	tv_title.setText(title_name);
    }
//	protected abstract void keyEventOfListView(int position,int id);
	
	protected String getItemFromMap(Map<String,String> map,int _index){
		Iterator<?> iter = map.entrySet().iterator();
		String id=null;
		int index=0;
		while (iter.hasNext()) {  
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();  
            String key = entry.getKey();  
            String name = entry.getValue(); 
            if(index==_index){
            	id=key;
            	System.out.println(key + "__ " + name);  
            	break;
            }else{
            	index++;
            }
		}
		return id;	
	}
	

}
