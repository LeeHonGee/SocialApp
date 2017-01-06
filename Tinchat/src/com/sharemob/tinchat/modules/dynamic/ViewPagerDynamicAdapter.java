package com.sharemob.tinchat.modules.dynamic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.sharemob.tinchat.R;

/**
 * 
 * @author 李航杰
 * 
 */
public class ViewPagerDynamicAdapter extends PagerAdapter {

	 private List<View> views = new ArrayList<View>();
     private ArrayList<String> titleContainer=null;
     public ViewPagerDynamicAdapter(Context context,ArrayList<String> titleContainer) {
         View view1 = View.inflate(context, R.layout.fragment_nearby_dynamic, null);
         View view2 = View.inflate(context, R.layout.fragment_nearby_user, null);
         
         views.add(view1);
         views.add(view2);
         this.titleContainer=titleContainer;
     }
     

     @Override
     public int getCount() {
         return views.size();
     }

     @Override
     public boolean isViewFromObject(View arg0, Object arg1) {
         return arg0 == arg1;
     }

     @Override
     public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView(views.get(position));
         super.destroyItem(container, position, object);
     }

     @Override
     public int getItemPosition(Object object) {
         return views.indexOf(object);
     }

     //每次滑动的时候生成的组件
     @Override
     public Object instantiateItem(ViewGroup container, int position) {
         ((ViewPager) container).addView(views.get(position));
         return views.get(position);
     }
//     @Override
//     public Object instantiateItem(ViewGroup container, int position) {
//         View view = views.get(position);
//         container.addView(view, lp);
//         return view;
//     }
     
     public CharSequence getPageTitle(int position) {  
         return this.titleContainer.get(position);  
     } 
}