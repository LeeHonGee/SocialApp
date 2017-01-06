package com.sharemob.tinchat.modules.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class CounterView extends View {  

    private Paint paint;  

      
    public CounterView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);  
        paint.setStyle(Style.STROKE);
    }  

    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        
        canvas.drawRect(100,100, getWidth(), getHeight(), paint);  

    }  

}  