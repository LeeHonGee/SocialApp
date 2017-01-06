package com.sharemob.tinchat.lib.adapter.address;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;

final class AddressItemViewHolder extends RecyclerView.ViewHolder
{
  TextView addressinfo;
  TextView btn_delete;
  TextView btn_editor;
  CheckBox chkbox_default;
  TextView receiver;
  View rootView;
  TextView telephone;

  public AddressItemViewHolder(View view)
  {
    super(view);
    this.rootView = view;
    this.receiver = LocalUtils.getFindViewById(view, R.id.address_tv_receiver);
    this.telephone = LocalUtils.getFindViewById(view, R.id.address_tv_telephone);
    this.addressinfo = LocalUtils.getFindViewById(view, R.id.address_tv_addressinfo);
    this.btn_editor = LocalUtils.getFindViewById(view, R.id.address_btn_edit);
    this.btn_delete = LocalUtils.getFindViewById(view, R.id.address_btn_delete);
    this.chkbox_default = ((CheckBox)view.findViewById(R.id.address_btn_default));
  }
}