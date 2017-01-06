package com.sharemob.tinchat.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class EmotionArticleViewHolder extends RecyclerView.ViewHolder
{
  TextView commentNum;
  ImageView cover;
  View rootView;
  TextView seeNum;
  TextView subtitle;
  TextView title;

  public EmotionArticleViewHolder(View paramView)
  {
    super(paramView);
    this.rootView = paramView;
    this.cover = ((ImageView)paramView.findViewById(R.id.emotion_article_cover));
    this.title = LocalUtils.getFindViewById(paramView, R.id.emotion_article_tv_title);
    this.subtitle = LocalUtils.getFindViewById(paramView, R.id.emotion_article_tv_subtitle);
    this.seeNum = LocalUtils.getFindViewById(paramView, R.id.emotion_article_tv_see);
    this.commentNum = LocalUtils.getFindViewById(paramView, R.id.emotion_article_tv_comment);
  }
}