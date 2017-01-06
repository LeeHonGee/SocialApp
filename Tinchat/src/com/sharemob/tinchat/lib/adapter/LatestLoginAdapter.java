package com.sharemob.tinchat.lib.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

public class LatestLoginAdapter extends BaseAdapter {

	private ArrayList<String> list = new ArrayList<String>();
	
	public LatestLoginAdapter() {
	}
	@Override
	public int getCount()
	{
		return list.size();
	}

	public void setList(ArrayList<String> list){
		this.list=list;
	}
	
	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();

			convertView = View.inflate(parent.getContext(),R.layout.layout_latestlogin_item, null);
			findViews(viewHolder, convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setView(viewHolder, position);
		return convertView;
	}
	
	private void findViews(ViewHolder viewHolder, View convertView)
	{
		viewHolder.latestlogin_month=(TextView)convertView.findViewById(R.id.latestlogin_month);
		viewHolder.latestlogin_day=LocalUtils.getFindViewById(convertView,R.id.latestlogin_day);
		viewHolder.latestlogin_location=LocalUtils.getFindViewById(convertView,R.id.latestlogin_location);
		viewHolder.latestlogin_week=LocalUtils.getFindViewById(convertView,R.id.latestlogin_week);
		viewHolder.latestlogin_time=LocalUtils.getFindViewById(convertView,R.id.latestlogin_time);
	}
	private void setView(final ViewHolder viewHolder, int position)
	{
		String json = list.get(position);
		JSONObject object=null;
		try {
			object = new JSONObject(json);
			viewHolder.latestlogin_month.setText(object.getString("month"));
			viewHolder.latestlogin_day.setText(object.getString("day"));
			viewHolder.latestlogin_week.setText(object.getString("week"));
			viewHolder.latestlogin_time.setText(object.getString("time"));
			viewHolder.latestlogin_location.setText(object.getString("location"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	protected final class ViewHolder
	{
		TextView latestlogin_month;
		TextView latestlogin_day;
		TextView latestlogin_location;
		TextView latestlogin_week;
		TextView latestlogin_time;
	}

}
