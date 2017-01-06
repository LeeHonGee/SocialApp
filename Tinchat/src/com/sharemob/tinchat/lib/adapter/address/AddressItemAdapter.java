package com.sharemob.tinchat.lib.adapter.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.LocalAlertDialog;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class AddressItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
  private Activity context;
  private List<AddressItem> list = new ArrayList<AddressItem>();

  public AddressItemAdapter(Activity paramActivity)
  {
    this.context = paramActivity;
  }

  private void deleteAddressItem(final int position,final int id)
  {
    LocalAlertDialog localLocalAlertDialog = new LocalAlertDialog(this.context).builder();
    localLocalAlertDialog.setMsg("确定删除该地址吗?");
    localLocalAlertDialog.setPositiveButton("确定",new OnClickListener() {
		@Override
		public void onClick(View v) {
			daleteData(position);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", Integer.valueOf(id));
            LocalUtils.requestHttp(0x2736, map, new AsyncHttpRequest.RequestCallback() {
                public void requestDidFinished(String body) {
                	
                }
                public void requestDidFailed() {
                }
            });
		}
	});
    localLocalAlertDialog.setNegativeButton("取消",new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	});
    localLocalAlertDialog.show();
  }

  private void updateDefault(int id, int state)
  {
    HashMap<String,Object> map = new HashMap<String,Object>();
    map.put("id", Integer.valueOf(id));
    map.put("state", Integer.valueOf(state));
    map.put("uid", CacheManager.getInstance().userInfo.uid);
    LocalUtils.requestHttp(10041, map,new RequestCallback() {
		@Override
		public void requestDidFinished(String body) {
			
		}
		@Override
		public void requestDidFailed() {
			
		}
	});
  }

  public void addArray(String paramString)
  {
    this.list.clear();
    if ((paramString == null) || ("".equals(paramString)));
      try
      {
        JSONArray localJSONArray = new JSONArray(paramString);
        for(int i = 0;i < localJSONArray.length();i++){
	        JSONObject localJSONObject = localJSONArray.getJSONObject(i);
	        AddressItem item = new AddressItem();
	        item.receiver = localJSONObject.getString("receiver");
	        item.telephone = localJSONObject.getString("telephone");
	        item.address = localJSONObject.getString("address");
	        item.detailedness = localJSONObject.getString("detailedness");
	        item.id = localJSONObject.getInt("id");
	        item.state = localJSONObject.getInt("state");
	        addItem(item);
        }
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
      }
  }

  public void addItem(AddressItem item)
  {
    this.list.add(item);
    notifyItemInserted(getItemCount());
  }

  public void daleteData(int paramInt)
  {
    this.list.remove(paramInt);
    notifyItemRemoved(paramInt);
    notifyItemChanged(paramInt);
  }

  public int getItemCount()
  {
    return this.list.size();
  }

  public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder,final int position)
  {
    final AddressItem entity = (AddressItem)this.list.get(position);
    final AddressItemViewHolder holder = (AddressItemViewHolder)paramViewHolder;
    holder.receiver.setText(entity.receiver);
    holder.telephone.setText(entity.telephone);
    holder.addressinfo.setText(entity.address + entity.detailedness);
    
    holder.btn_editor.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            JsonObject object = new JsonObject();
            object.addProperty("id", Integer.valueOf(entity.id));
            LocalUtils.enterAppActivity(context, "UpdateAddressitem", object.toString());
        }
    });
    
    holder.btn_delete.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        }
    });

    if(entity.state == 0) {
        holder.chkbox_default.setChecked(false);
    } else {
        holder.chkbox_default.setChecked(true);
    }
    
    holder.chkbox_default.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	holder.chkbox_default.setChecked(true);
        	//设置默认值
        	updateDefault(entity.id, 1);
            for(int i =0; i< list.size(); i++) {
                AddressItem addressItem = (AddressItem)list.get(i);
                if(addressItem.id == entity.id) {
                    addressItem.state = 0x1;
                } else {
                   addressItem.state = 0x0;
                }
                list.set(i, addressItem);
            }
            notifyDataSetChanged();
        }
    });
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
	View view=LayoutInflater.from(this.context).inflate(R.layout.adapter_addresses_item, paramViewGroup, false);
    return new AddressItemViewHolder(view);
  }
}