/**
 *  文件名:CropPhotoActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-10-3 上午11:04:51
 */
package com.sharemob.tinchat.modules.camera;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.style.BulletSpan;
import android.widget.ImageView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseActivity;
import com.sharemob.tinchat.base.Callback;
import com.sharemob.tinchat.base.CropImageView;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-10-3 上午11:04:51]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class CropPhotoActivity extends BaseActivity {


	private CropImageView iv_pre_crop_photo=null;
	private Bitmap crop_bitmap;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化layout配置
		this.setContentView(R.layout.activity_crop_photo);
		//获取上一个activity传递参数
//		Bundle bundle = this.getIntent().getExtras();
		iv_pre_crop_photo=(CropImageView)findViewById(R.id.iv_pre_crop_photo);
		File file=new File(LocalUtils.getCropPhotoFilePath().getPath());
		crop_bitmap = ImageUtils.compressImageFromFile(file.getAbsolutePath());
		iv_pre_crop_photo.setDrawable(ImageUtils.BitmapToDrawable(crop_bitmap), 300,300);
		
//		iv_pre_crop_photo.setImageBitmap(camorabitmap);
		//返回按键
		 LocalUtils.exitView(this,R.id.title_back, 0, R.anim.out_from_top);
		
		
		
		 LocalUtils.BtnEvent(this,R.id.btn_crop_finish, new Callback() {
			public void doDown() {
				  Intent intent = new Intent();
//				  Bitmap bitmap= iv_pre_crop_photo.getCropImage();
				  Bundle bundle=new Bundle();
//				  bundle.putParcelable("release", crop_bitmap);
//				  intent.putExtra("bundle", bundle);
//		          intent.setClass(CropPhotoActivity.this, ReleasePhotoActivity.class);
		          startActivity(intent);
		          finish();
		          overridePendingTransition(R.anim.in_from_bottom,0);
			}
		});
	}
	protected void keyEventOfListView(int position, int id) {

	}

}
