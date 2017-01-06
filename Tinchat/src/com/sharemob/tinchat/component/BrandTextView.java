package com.sharemob.tinchat.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sharemob.tinchat.lib.common.GlobalParams;

public class BrandTextView extends TextView {

      public BrandTextView(Context context, AttributeSet attrs, int defStyle) {
          super(context, attrs, defStyle);
      }
     public BrandTextView(Context context, AttributeSet attrs) {
          super(context, attrs);
      }
     public BrandTextView(Context context) {
         super(context);
         this.setTypeface(GlobalParams.getInstance().localFontTypeface);
     }
     public void setTypeface(Typeface tf, int style) {
           if (style == Typeface.BOLD) {
                super.setTypeface(GlobalParams.getInstance().localFontTypeface);
            } else {
               super.setTypeface(GlobalParams.getInstance().localFontTypeface);
            }
      }
 }