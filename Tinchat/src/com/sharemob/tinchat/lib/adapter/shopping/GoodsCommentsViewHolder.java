package com.sharemob.tinchat.lib.adapter.shopping;

import android.support.v7.widget.RecyclerView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.photoview.NoScrollGridView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View;
import com.sharemob.tinchat.lib.LocalUtils;

final class GoodsCommentsViewHolder extends RecyclerView.ViewHolder {
    NoScrollGridView attach_photos;
    TextView content;
    LinearLayout goods_comment_grade;
    TextView nickname;
    View rootView;
    TextView time;
    
    public GoodsCommentsViewHolder(View view) {
        super(view);
        rootView = view;
        goods_comment_grade = (LinearLayout)view.findViewById(R.id.goods_comment_grade);
        nickname = LocalUtils.getFindViewById(view, R.id.goods_comment_nickname);
        time = LocalUtils.getFindViewById(view, R.id.goods_comment_time);
        content = LocalUtils.getFindViewById(view, R.id.goods_comment_content);
        attach_photos = (NoScrollGridView)view.findViewById(R.id.goods_comment_attach_photos);
    }
}