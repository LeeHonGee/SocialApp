package com.sharemob.tinchat.modules.message;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;

import com.sharemob.tinchat.R;

/**
 * 
 * @author 李航杰
 * 
 */
public class ViewPagerMessageAdapter extends PagerAdapter {

	 private List<View> views = new ArrayList<View>();
     private LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

     public ViewPagerMessageAdapter(Context context) {
         View view_message = View.inflate(context, R.layout.fragment_revgift_recyclerview, null);
         View view_question = View.inflate(context, R.layout.activity_mytip_question, null);
//         View view_sugesst = View.inflate(context, R.layout.activity_mytip_question, null);
         views.add(view_message);
         views.add(view_question);
//         views.add(view_sugesst);
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

     @Override
     public Object instantiateItem(ViewGroup container, int position) {
         View view = views.get(position);
         container.addView(view, lp);
         return view;
     }
}