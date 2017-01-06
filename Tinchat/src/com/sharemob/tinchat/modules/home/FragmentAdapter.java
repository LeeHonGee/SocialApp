package com.sharemob.tinchat.modules.home;

import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] titles;
    
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }
    
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    
    public Fragment getItem(int position) {
        System.out.println(position);
        return (Fragment)fragments.get(position);
    }
    
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
    
    public CharSequence getPageTitle(int position) {
        return titles[(position % titles.length)];
    }
    
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        return fragment;
    }
    
    public int getCount() {
        return titles.length;
    }
}
