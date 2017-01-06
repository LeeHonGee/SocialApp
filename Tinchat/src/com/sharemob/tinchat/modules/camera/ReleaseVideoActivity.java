/**
 *  文件名:ReleaseVideoActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-10-5 上午12:50:41
 */
package com.sharemob.tinchat.modules.camera;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseActivity;
import com.sharemob.tinchat.base.LocalVideoView;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-10-5 上午12:50:41]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class ReleaseVideoActivity extends BaseActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release_video);
		//状态栏返回按钮
		 LocalUtils.exitView(this,R.id.title_back, 0, R.anim.out_from_top);
		
		TextView tv_location_map=(TextView)findViewById(R.id.tv_location_map);
//		if(CacheManager.getInstance().getAccount().getAddress()!=null){
//			tv_location_map.setText(CacheManager.getInstance().getAccount().getAddress());
//		}
		
		final EditText et_release_text=(EditText)findViewById(R.id.et_release_text);
		et_release_text.setFocusable(true);
		et_release_text.setFocusableInTouchMode(true);
		et_release_text.requestFocus();
		
		 Timer timer = new Timer();    
         timer.schedule(new TimerTask()    
         {    
             public void run()     
             {    
                 InputMethodManager inputManager =    
                     (InputMethodManager)et_release_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);    
                 inputManager.showSoftInput(et_release_text, 0);    
             }    
         },500);
         
		final LocalVideoView videoView = (LocalVideoView)findViewById(R.id.iv_release_video);
//		videoView.setVisibility(visibility)
//		View.OnLongClickListener
//		File file=new File("");
//		file.length()
		videoView.setVideoPath(LocalUtils.SignatureVideoFilePath);
		videoView.start();
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  
            public void onPrepared(MediaPlayer mp) {
            	mp.setVolume(0, 0);
                mp.setLooping(true); 
                mp.start();  
            }  
        });  
		
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
            public void onCompletion(MediaPlayer mp) {  
            	mp.reset();
//            	videoView.setVideoPath(Utils.getVideoFilePath());
            	mp.setVolume(0, 0);
            	videoView.start();  
            	
            }  
        });  
//		videoView.setOnErrorListener(new MediaPlayer.OnErrorListener(){
//			public boolean onError(MediaPlayer mp, int what, int extra) {
//				return true;
//			}
//			
//		});
	}
	protected void keyEventOfListView(int position, int id) {

	}

}
