package com.sharemob.tinchat.modules.dynamic;

import com.sharemob.tinchat.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SexFragment extends Fragment
{
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
	  View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_discovery_sex, paramViewGroup, false);
	   
	  return view;
  }
}