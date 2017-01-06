package com.sharemob.tinchat.component;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{  
  
        private int space=5;  
  
        public SpaceItemDecoration(int space) {  
            this.space = space;  
        }  
  
        @Override  
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {  
  
        	   outRect.left = space;
               outRect.bottom = space;
               //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
               if (parent.getChildPosition(view) %3==0) {
                   outRect.left = 0;
               }
        }  
    }