package com.sharemob.tinchat.lib.adapter.center;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.Gender;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;
import com.sharemob.tinchat.modules.dao.entity.UserInfo;

public class CenterItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int USERINFO=0;
	private static final int ITEM_TOP=1;
	private static final int ITEM_BOTTOM=2;
	
    private Activity activity;
    private JSONArray jsonArray=new JSONArray();
    
    public CenterItemAdapter(Activity activity) {
        this.activity = activity;
    }
    
    public int getItemCount() {
    	return jsonArray.length();
    }
    
    public void addArray(String json) {
    	try {
			jsonArray=new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public int getItemViewType(int position) {
        try {
            if(position < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(position);
                return object.getInt("type");
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return super.getItemViewType(position);
    }
    
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        try {
			JSONObject object=jsonArray.getJSONObject(position);
			int type=object.getInt("type");
			switch (type) {
			case ITEM_TOP:
			case ITEM_BOTTOM:
				onBindViewHolderItemRecyclerView(viewHolder,object.getJSONArray("body"));
				break;
			case USERINFO:
				onBindViewHolderUserInfo(viewHolder);
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    
    public void onBindViewHolderUserInfo(RecyclerView.ViewHolder viewHolder){
    	CenterUserInfoViewHolder holder = (CenterUserInfoViewHolder)viewHolder;
//    	try {
    		holder.rootView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LocalUtils.enterAppActivity(activity, new HashMap<String, Object>(), "AccountInfoActivity");
				}
			});
    		
//			String avatar=object.getString("avatar");
    		UserInfo userInfo=CacheManager.getInstance().userInfo;
    		String avatar=String.format(CacheManager.LOCAL_IMAGE_URL, userInfo.avatar);
    		System.out.println("userInfo-----avatar:"+avatar);
			ImageLoader.getInstance().displayImage(avatar, holder.avatar);
//			int sex=object.getInt("sex");
			int sex=userInfo.sex;
			System.out.println("userInfo-----sex:"+sex);
			if(sex==Gender.FEMALE.ordinal()){
				holder.sex_age.setBackgroundResource(R.drawable.gender_girl);	
			}else if(sex==Gender.MALE.ordinal()){
				holder.sex_age.setBackgroundResource(R.drawable.gender_boy);			
			}
//			String birthday=object.getString("birthday");
			String birthday=userInfo.birthday;
			System.out.println("userInfo-----birthday:"+birthday);
			String age=LocalUtils.calculateDatePoor(birthday);
			holder.sex_age.setText(age);
			
//			int ID=object.getInt("ID");
			int ID=userInfo.ID;
			System.out.println("userInfo-----ID:"+ID);
			holder.ID.setText(String.format("ID:%05d", ID));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }
    
    public void onBindViewHolderItemRecyclerView(RecyclerView.ViewHolder viewHolder,JSONArray array){
    	System.out.println("-------------"+array.toString());
    	CenterItemListViewHolder holder=(CenterItemListViewHolder)viewHolder;
    	CenterItemListAdapter adapter=new CenterItemListAdapter(activity,array.toString());
//    	adapter.addArray(array.toString());
    	RecyclerView localRecyclerView = holder.item_list;
    	localRecyclerView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    	
    	 LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localRecyclerView.getLayoutParams();
         localLayoutParams.height = (array.length() * LocalUtils.dip2px(this.activity, 55.0F));
         localRecyclerView.setLayoutParams(localLayoutParams);
    }
    
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	View view = null;
    	switch (viewType) {
		case USERINFO:
			view = LayoutInflater.from(activity).inflate(R.layout.fragment_center_userinfo, parent, false);
	        return new CenterUserInfoViewHolder(view);
		case ITEM_TOP:
		case ITEM_BOTTOM:
			view = LayoutInflater.from(activity).inflate(R.layout.fragment_center_item_recyclerview, parent, false);
			return new CenterItemListViewHolder(view);
		default:
			break;
		}
       return null;
    }
}