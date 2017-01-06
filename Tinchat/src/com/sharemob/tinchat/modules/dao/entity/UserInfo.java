/**
 *  文件名:Account.java
 *  修改人:lihangjie
 *  创建时间:2015-9-17 上午10:45:44
 */
package com.sharemob.tinchat.modules.dao.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sharemob.tinchat.lib.LocalUtils;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-9-17 上午10:45:44]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class UserInfo {
	public int ID;
	public String uid;
	public String IDCard;
	public String server_addr;
	public String password;
	public String avatar;
	public String age;
	public String birthday=new String("1990-01-01");
	public String nickname;
	public String realname;
	public String constellation;
	public String signature;
	public String country;
	public String province;
	public String district;
	public String city;
	public String phone;
	public String location;
	public String voice;
	public String video;
	public String fellow_townsman;
	public String address;
	public int sex;
	public int marriage;
	public int visitor_count;
	public double longitude;
	public double latitude;
	public Marriageseeking marriageseeking;
	public List<Photobook> photobooks=new ArrayList<Photobook>();
	
	
	public void setBirthday(String birthday){
		this.birthday=birthday;
		age=LocalUtils.calculateDatePoor(birthday);
		constellation=LocalUtils.constellation(birthday);
	}
	
	public void loadData(String json){
		try {
			JSONObject entity=new JSONObject(json);
			uid=entity.getString("uid");
			server_addr=entity.getString("server_addr");
			avatar=entity.getString("avatar");
			sex=entity.getInt("sex");
			ID=entity.getInt("ID");
			marriage=entity.getInt("marriage");
			visitor_count=entity.getInt("visitor_count");
			avatar=entity.getString("avatar");
			nickname=entity.getString("nickname");
			birthday=entity.getString("birthday");
			location=entity.getString("location");
			
			realname = entity.getString("realname");
			if(entity.has("IDCard")) {
                String idcard = entity.getString("IDCard");
                if(idcard.length() > 0x5) {
                    IDCard = String.format("%s********%s", idcard.substring(0x0, 0x5), idcard.substring((idcard.length() - 0x4), idcard.length()));
                }
            }
			voice=entity.getString("voice");
			country=entity.getString("country");
			city=entity.getString("city");
			province=entity.getString("province");
			district=entity.getString("district");
			phone=entity.getString("phone");
			signature=entity.getString("signature");
			if(entity.has("photobooks")){
				photobooks=getPhotobooks(entity.getJSONArray("photobooks"));
			}
			if(entity.has("marriageseeking")){
				marriageseeking=new Marriageseeking(entity.getJSONObject("marriageseeking"));
			}
			
			
			setBirthday(birthday);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void addPhotobook(String name){
		Photobook photobook=new Photobook();
		photobook.name=name;
		photobooks.add(photobook);
	}
	
	public String getPhotobooks(){
		JSONArray array=new JSONArray();
		for(int i=0;i<photobooks.size();i++){
			JSONObject object=new JSONObject();
			Photobook photobook=photobooks.get(i);
			try {
				object.put("ID", photobook.ID);
				object.put("filename", photobook.name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(object);
		}
		return array.toString();
	}
	
	public List<Photobook> getPhotobooks(JSONArray array){
		List<Photobook> photobooks=new ArrayList<Photobook>();
			try {
				for(int i=0;i<array.length();i++){
					Photobook photobook=new Photobook();
					JSONObject object=array.getJSONObject(i);
					photobook.name=object.getString("filename");
					photobook.ID=object.getInt("ID");
					photobooks.add(photobook);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return photobooks;
	}
	
	public void deletePhotobook(int id){
		for(int i=0;i<photobooks.size();i++){
			if(photobooks.get(i).ID==id){
				photobooks.remove(i);
				break;
			}
		}
	}
}



final class Photobook{
	String name;
	int ID;
}

final class Marriageseeking{
	public String education;
	public String hasCar;
	public String hasHouse;
	public String profession;
	public String bodily;
	public String income;
	public String fellow_townsman;
	public String marital_status;
	
	public Marriageseeking(JSONObject entity) {
		try {
			education=entity.getString("education");
			hasCar=entity.getString("hasCar");
			hasHouse=entity.getString("hasHouse");
			profession=entity.getString("profession");
			bodily=entity.getString("bodily");
			income=entity.getString("income");
			fellow_townsman=entity.getString("fellow_townsman");
			marital_status=entity.getString("marital_status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}