/**
 *  文件名:AlertDialog.java
 *  修改人:lihangjie
 *  创建时间:2016-6-21 下午2:17:36
 */
package com.sharemob.tinchat.component;

import com.sharemob.tinchat.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-6-21 下午2:17:36]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class AlertDialog{
	 public interface OnDialogListener{
         public void delete();
	 }
	private Dialog mDialog;
	
	public AlertDialog(Context context,final AlertDialog.OnDialogListener dialogListener) {
		mDialog=new Dialog(context,  R.style.dialog);
		mDialog.setContentView(R.layout.dialog_operation);
        mDialog.setCanceledOnTouchOutside(true);
		Button btn_delete=(Button)mDialog.findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogListener.delete();
				mDialog.dismiss();
			}
		});
		show();
	}
	
	public void show(){
		mDialog.show();
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}

}
