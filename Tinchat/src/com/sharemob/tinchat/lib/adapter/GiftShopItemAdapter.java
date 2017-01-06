/**
 *  文件名:CommentAdapter.java
 *  修改人:lihangjie
 *  创建时间:2016-5-2 上午11:25:26
 */
package com.sharemob.tinchat.lib.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.lib.common.SMGlobal;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2016-5-2 上午11:25:26]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class GiftShopItemAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private AppActivity activity;
	private Map<String,Object> params=new HashMap<String, Object>();
	private List<ShoppingGiftEntity> items=new ArrayList<ShoppingGiftEntity>();
	
	public GiftShopItemAdapter(AppActivity activity) {
		this.activity=activity;
	}
	public void addParam(String key,Object value){
		params.put(key, value);
	}
	public void addObject(String json) {
		try {
			JSONObject object=new JSONObject(json);
			ShoppingGiftEntity entity=new ShoppingGiftEntity();
			entity.gift_name=object.getString("name");
			entity.gift_moral=object.getString("moral");
			entity.gift_icon=object.getString("path");
			entity.gift_price=object.getInt("price");
			entity.gid=object.getInt("ID");
			this.items.add(entity);
			int count = getItemCount();
			this.notifyItemInserted(count);
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void addArray(String json){
		if(json==null||"".equals(json)){
			return;
		}
		try {
			JSONArray array=new JSONArray(json);
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				addObject(object.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		ShoppingGiftViewHolder  holder=(ShoppingGiftViewHolder)viewHolder;
		final ShoppingGiftEntity entity=items.get(position);
		String url=String.format("%s%s",CacheManager.getInstance().userInfo.server_addr ,entity.gift_icon);
		ImageLoader.getInstance().displayImage(url, holder.gift_icon);
		holder.gift_name.setText(entity.gift_name);
		holder.gift_moral.setText(entity.gift_moral);
		holder.gift_price.setText(String.format("价格:%d 元/朵", entity.gift_price));
		holder.gift_btn_give.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("senduid",CacheManager.getInstance().userInfo.uid);
				map.put("loveruid",params.get("loveruid"));
				map.put("gid", entity.gid);
				LocalUtils.requestHttp(SMGlobal.CMD.GIVE_GIFT_TO_OTHER, map,new RequestCallback() {
					@Override
					public void requestDidFinished(String body) {
						try {
							JSONObject object=new JSONObject(body);
							if(object.has("result")&&object.getInt("result")==0){
								LocalUtils.showToast(object.getString("desc"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void requestDidFailed() {
						
					}
				});
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(activity).inflate(R.layout.layout_item_shopping_gift, parent, false);
		return new ShoppingGiftViewHolder(view);
	}
}

final class ShoppingGiftViewHolder extends  RecyclerView.ViewHolder{
	View rootView;
	ImageView gift_icon;
	TextView gift_name;
	TextView gift_moral;
	TextView gift_price;
	Button gift_btn_give;
	
	public ShoppingGiftViewHolder(View view) {
		super(view);
		rootView=view;
		gift_icon= (ImageView) view.findViewById(R.id.gift_icon);
		gift_name =LocalUtils.getFindViewById(view,R.id.gift_tv_name);
		gift_moral =LocalUtils.getFindViewById(view,R.id.gift_tv_moral);
		gift_price =LocalUtils.getFindViewById(view,R.id.gift_tv_price);
		gift_btn_give= (Button) view.findViewById(R.id.gift_btn_give);
	}
	
}

final class ShoppingGiftEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String gift_icon;
	String gift_name;
	String gift_moral;
	int gift_price;
	int gid;
}