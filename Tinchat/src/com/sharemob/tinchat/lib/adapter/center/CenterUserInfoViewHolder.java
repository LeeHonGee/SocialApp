package com.sharemob.tinchat.lib.adapter.center;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class CenterUserInfoViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    ImageView avatar;
    TextView nickname;
    TextView ID;
    TextView sex_age;
    
    public CenterUserInfoViewHolder(View view) {
        super(view);
        rootView = view;
        nickname = LocalUtils.getFindViewById(view, R.id.fragment_center_userinfo_nickname);
        ID = LocalUtils.getFindViewById(view, R.id.fragment_center_userinfo_ID);
        sex_age = LocalUtils.getFindViewById(view, R.id.fragment_center_userinfo_sexAndage);
        avatar = (ImageView)view.findViewById(R.id.fragment_center_userinfo_avatar);
    }
}