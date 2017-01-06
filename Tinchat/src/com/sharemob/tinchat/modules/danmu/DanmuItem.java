/**
 *  文件名:DanmuItem.java
 *  修改人:lihangjie
 *  创建时间:2015-11-13 下午11:22:43
 */
package com.sharemob.tinchat.modules.danmu;

import android.graphics.Bitmap;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-11-13 下午11:22:43]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class DanmuItem {

	private Bitmap headerIcon;
	private String nickname;
	private String message;
	public Bitmap getHeaderIcon() {
		return headerIcon;
	}
	public void setHeaderIcon(Bitmap headerIcon) {
		this.headerIcon = headerIcon;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
