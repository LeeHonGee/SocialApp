/**
 *  文件名:BaseActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-8-29 下午10:57:10
 */
package com.sharemob.tinchat.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

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
public abstract class BaseActivity extends Activity {
	
	public Context context=null;

//	public void BtnEvent(int id,final Callback callback){
//		   findViewById(id).setOnClickListener(new OnClickListener()
//	        {
//	            public void onClick(View v)
//	            {
//	            	callback.doDown();
//	            }
//	        });
//	}
	protected abstract void keyEventOfListView(int position,int id);
//	protected TextView tv_title=null;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
    }
    
//    protected void setTitle(String title_name){
//    	tv_title=(TextView)findViewById(R.id.tv_title);
//    	tv_title.setText(title_name);
//    }
    
//    protected ParallaxScollListView getInitListViewForPet(ArrayList<PetItem> list,int id,boolean listener){
//    	ParallaxScollListView listView=(ParallaxScollListView)findViewById(id);
//		ListAdapterForPet listAdapter=new ListAdapterForPet();
//		
//		listAdapter.setAdapter(list);
//		listView.setAdapter(listAdapter);
//	
//		if(listener){
//			listView.setOnItemClickListener(new OnItemClickListener(){
//	            public void onItemClick(AdapterView<?> parent, View view, int position,long id)
//	            {
//	            	keyEventOfListView(position,parent.getId());
//	            }
//	        });
//		}
//		
//		return listView;  
//	}

    

	protected String getItemFromMap(Map<String,String> map,int _index){
		Iterator iter = map.entrySet().iterator();
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
	
    protected void exit() {
        finish();
        overridePendingTransition(0,R.anim.activity_close);
        // android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    public void finishActivity(){
    	finish();
    	//关闭窗体动画显示
        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
    }
}
