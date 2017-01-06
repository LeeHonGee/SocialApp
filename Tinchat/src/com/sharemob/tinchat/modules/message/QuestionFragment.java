/**
 *  文件名:QuestionFragment.java
 *  修改人:lihangjie
 *  创建时间:2016-4-20 上午10:45:22
 */
package com.sharemob.tinchat.modules.message;

import com.sharemob.tinchat.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-4-20 上午10:45:22]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class QuestionFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question,container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	}
	
}
