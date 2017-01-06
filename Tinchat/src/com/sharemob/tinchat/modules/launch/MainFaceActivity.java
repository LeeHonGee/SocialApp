/**
 *  文件名:MainTestActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-11-11 下午8:04:13
 */
package com.sharemob.tinchat.modules.launch;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseActivity;
import com.sharemob.tinchat.base.NoScrollListview;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.modules.danmu.CacheMemory;
import com.sharemob.tinchat.modules.danmu.DanmuItem;
import com.sharemob.tinchat.modules.danmu.ListAdapterForDanmu;
import com.sharemob.tinchat.modules.danmu.RemoveTaskMgr;
import com.sharemob.tinchat.modules.danmu.SendTaskMgr;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-11-11 下午8:04:13]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
@SuppressLint("NewApi")
public class MainFaceActivity extends BaseActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_input_editor);
		
		
	}
	
	protected  void keyEventOfListView(int position, int id){}

}
