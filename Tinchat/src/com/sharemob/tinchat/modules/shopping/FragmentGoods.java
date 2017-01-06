package com.sharemob.tinchat.modules.shopping;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.adapter.shopping.GoodsDetailsViewHolder;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;

public class FragmentGoods extends Fragment {
    private GoodsDetailsViewHolder goodsDetailsViewHolder;
    private View rootView;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods, container, false);
        return rootView;
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goodsDetailsViewHolder = new GoodsDetailsViewHolder(rootView, getActivity());
        loadData();
    }
    
    public void loadData() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        LocalUtils.requestHttp(10037, map, new AsyncHttpRequest.RequestCallback() {
            public void requestDidFinished(String json) {
                System.out.println(json);
                try {
                    JSONObject object = new JSONObject(json);
                    if(object.has("body")) {
                        JSONObject details = object.getJSONObject("body");
                        goodsDetailsViewHolder.load(details);
                        return;
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
            
            public void requestDidFailed() {
            }
        });
    }
}