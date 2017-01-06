package com.sharemob.tinchat.modules.shopping;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;

public class FragmentGoodsDetails extends Fragment {
    private View rootView;
    private WebView wb_details;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_details, container, false);
        return rootView;
    }
    
    public void loadWebView(String url) {
        wb_details.loadUrl(url);
        wb_details.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wb_details = (WebView)rootView.findViewById(R.id.shopping_item_wb_details);
    }
    
    public void onResume() {
        super.onResume();
        loadData();
    }
    
    public void loadData() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        LocalUtils.requestHttp(10036, map, new AsyncHttpRequest.RequestCallback() {
            public void requestDidFinished(String json) {
                System.out.println(json);
                try {
                    JSONObject object = new JSONObject(json);
                    loadWebView(object.getString("url"));
                    return;
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
            
            public void requestDidFailed() {
            }
        });
    }
}