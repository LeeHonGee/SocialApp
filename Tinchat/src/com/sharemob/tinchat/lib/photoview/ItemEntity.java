package com.sharemob.tinchat.lib.photoview;

import java.util.ArrayList;

public class ItemEntity {
	private String avatar; // 用户头像URL
	private String title; // 标题
	private String content; // 内容
	private ArrayList<CharSequence> imageUrls; // 九宫格图片的URL集合

	public ItemEntity(String avatar,String title, String content, ArrayList<CharSequence> imageUrls) {
		super();
		this.avatar = avatar;
		this.title = title;
		this.content = content;
		this.imageUrls = imageUrls;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<CharSequence> getImageUrls() {
		return imageUrls;
	}
	
//	public String[] getImageUrlsForArgs(){
//		if(imageUrls==null){
//			return null;
//		}
//		int length=imageUrls.size();
//		String[] urls=new String[length];
//		for(int i=0;i<length;i++){
//			urls[i]=imageUrls.get(i);
//		}
//		return urls;
//	}

	public void setImageUrls(ArrayList<CharSequence> imageUrls) {
		this.imageUrls = imageUrls;
	}

	@Override
	public String toString() {
		return "ItemEntity [avatar=" + avatar + ", content=" + content + ", imageUrls=" + imageUrls + "]";
	}

}
