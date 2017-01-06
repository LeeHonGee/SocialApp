package com.sharemob.tinchat.lib.adapter.shopping;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class SaletimeViewHolder extends RecyclerView.ViewHolder {
    TextView costprice;
    ImageView image;
    TextView price;
    View rootView;
    TextView saveprice;
    
    public SaletimeViewHolder(View view) {
        super(view);
        rootView = view;
        saveprice = (TextView)view.findViewById(R.id.shopping_saletime_saveprice);
        price = LocalUtils.getFindViewById(view, R.id.shopping_saletime_price);
        costprice = LocalUtils.getFindViewById(view, R.id.shopping_saletime_costprice);
        costprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        image = (ImageView)view.findViewById(R.id.shopping_saletime_img);
    }
}
