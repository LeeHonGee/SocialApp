/**
 *  文件名:PetItem.java
 *  修改人:lihangjie
 *  创建时间:2015-9-6 下午2:22:34
 */
package com.sharemob.tinchat.base;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-9-6 下午2:22:34]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class PetItem {

	private String Id;
	private int icon;
	private String name;
	private int level;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
