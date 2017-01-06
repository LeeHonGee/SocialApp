/**
 *  文件名:ColorButton.java
 *  修改人:lihangjie
 *  创建时间:2016-5-17 下午11:57:33
 */
package com.sharemob.tinchat.component;

import com.sharemob.tinchat.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-5-17 下午11:57:33]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class ColorButton extends Button {

	/**
	 * @param context
	 */
	public ColorButton(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ColorButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ColorButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, R.drawable.style_btn_login_register);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
