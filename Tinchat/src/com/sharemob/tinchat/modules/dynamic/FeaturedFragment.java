package com.sharemob.tinchat.modules.dynamic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeaturedFragment extends Fragment
{
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.cloneInContext(new ContextThemeWrapper(getActivity(), 2131230737)).inflate(2130903085, paramViewGroup, false);
  }
}