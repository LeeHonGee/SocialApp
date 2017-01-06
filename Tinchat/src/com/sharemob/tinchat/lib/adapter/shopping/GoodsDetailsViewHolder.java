package com.sharemob.tinchat.lib.adapter.shopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AdvanceDecoration;
import com.sharemob.tinchat.component.BannerSlideView;
import com.sharemob.tinchat.lib.LocalUtils;

public final class GoodsDetailsViewHolder {
    Activity activity;
    RecyclerView comments;
    TextView marketprice;
    TextView more_comments;
    BannerSlideView photosSlideView;
    TextView price;
    RecyclerView recommends;
    View rootView;
    TextView sales;
    TextView subtitle;
    TextView title;
    WebView wb_details;
    
    public GoodsDetailsViewHolder(View view, Activity activity) {
        rootView = view;
        this.activity = activity;
        photosSlideView = (BannerSlideView)view.findViewById(R.id.shopping_goods_photos);
        title = LocalUtils.getFindViewById(view, R.id.shopping_goods_title);
        subtitle = LocalUtils.getFindViewById(view, R.id.shopping_goods_subtitle);
        price = LocalUtils.getFindViewById(view, R.id.shopping_goods_price);
        
        marketprice = LocalUtils.getFindViewById(view, R.id.shopping_goods_marketprice);
        marketprice.getPaint().setFlags(0x10);
        
        sales = LocalUtils.getFindViewById(view, R.id.shopping_goods_sales);
        comments = (RecyclerView)view.findViewById(R.id.shopping_goods_comments);
        comments.addItemDecoration(new AdvanceDecoration(view.getContext(), OrientationHelper.HORIZONTAL));
        comments.setLayoutManager(new StaggeredGridLayoutManager(0x1, StaggeredGridLayoutManager.VERTICAL));
        
        more_comments = LocalUtils.getFindViewById(view, R.id.shopping_goods_more_comment);
        wb_details = (WebView)view.findViewById(R.id.shopping_item_wb_details);
        
        recommends = (RecyclerView)view.findViewById(R.id.shopping_goods_personalmatchs);
        recommends.addItemDecoration(new AdvanceDecoration(view.getContext(), OrientationHelper.HORIZONTAL));
        recommends.setLayoutManager(new StaggeredGridLayoutManager(0x1, StaggeredGridLayoutManager.VERTICAL));
    }
    
    static final String val="\u6708\u9500";
    public void load(JSONObject object) {
        System.out.println(object.toString());
        try {
            JSONArray photos = object.getJSONArray("photos");
            photosSlideView.loadBannerSlide(null, photos.toString());
            JSONObject goods = object.getJSONObject("goods");
            title.setText(goods.getString("title"));
            subtitle.setText(goods.getString("subtitle"));
            price.setText(String.format("￥%s", Double.valueOf(goods.getDouble("price"))));
            marketprice.setText(String.format("市场价:￥%s",Double.valueOf(goods.getDouble("marketprice"))));
            sales.setText(String.format("月销:%s", goods.getInt("sales")));
            
            JSONArray json_comments = object.getJSONArray("comments");
            GoodsCommentsAdapter commentsAdapter = new GoodsCommentsAdapter(activity);
            commentsAdapter.addArray(json_comments.toString());
            comments.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();
            
            JSONArray json_recommends = object.getJSONArray("recommends");
            RecommendGoodsAdapter recommendGoodsAdapter = new RecommendGoodsAdapter(activity);
            recommendGoodsAdapter.addArray(json_recommends.toString());
            recommends.setAdapter(recommendGoodsAdapter);
            recommendGoodsAdapter.notifyDataSetChanged();
            
            wb_details.loadUrl(object.getString("detailsUrl"));
            wb_details.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            return;
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}