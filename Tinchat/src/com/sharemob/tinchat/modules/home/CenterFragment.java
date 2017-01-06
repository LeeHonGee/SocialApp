package com.sharemob.tinchat.modules.home;

import android.os.Bundle;

import com.androlua.LuaFragment;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;

public class CenterFragment extends LuaFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		layout_ID=R.layout.layout_fragment;
		this.luaFilename=CenterFragment.class.getSimpleName();
		super.onCreate(savedInstanceState);
		LocalUtils.applyLocalFont(getActivity().getWindow().getDecorView());
		MyApplication.getInstance().addActivity(this.getActivity());
	}
}
