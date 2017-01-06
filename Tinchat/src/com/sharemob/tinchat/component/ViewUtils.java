/**
 *  文件名:ViewUtils.java
 *  修改人:lihangjie
 *  创建时间:2016-3-28 上午11:58:08
 */
package com.sharemob.tinchat.component;

import com.sharemob.tinchat.lib.common.GlobalParams;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-3-28 上午11:58:08]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class ViewUtils {

	public static TextView getFindViewById(View convertView,int id){
		TextView tv=(TextView) convertView.findViewById(id);
		tv.setTypeface(GlobalParams.getInstance().localFontTypeface);
		return tv;
	}
	
    public void gotoViewByAnim(Context ctx,Class<?> clazz,int enterAnim,int exitAnim,boolean isfinish){
		Intent intent = new Intent(); 
        intent.setClass(ctx,clazz);
        ctx.startActivity(intent);
        if(isfinish){
//        	ctx.finish();
        }
//        ctx.overridePendingTransition(enterAnim,exitAnim);
	}
}
