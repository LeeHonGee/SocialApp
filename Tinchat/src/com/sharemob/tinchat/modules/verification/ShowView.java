package com.sharemob.tinchat.modules.verification;

import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.lib.common.SMGlobal;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by linchen on 16/1/12.
 */
public class ShowView extends View {
    private Context context;
    private final  static int DEFAULT_PADDING=20;
    String[] stringArray=new String[]{
    		"350","",
    		"较差","",
    		"","520",
    		"","",
    		"中等","",
    		"690","",
    		"良好","",
    		"","870",
    		"","优秀",
    		"","极好"};

    private final static float SWEEP_ANGLE=240F;
    private final static float START_ANGLE=165F;
    private int count=0;
    private  int distance_from_two_acr;
    //正中间字体
    private String  textString="信用良好";

    //信用分数
    private String numString="0";
    
    //评估时间
    private String evaluateString="评估时间:2016.04.22";

    //评估的画笔
    private Paint evaluatePaint;
  //信用分数的画笔
    private Paint numPaint;
    //外半圆环的画笔
    private Paint outArcPaint;
    //内圆环的画笔
    private Paint inArcPaint;
    //文本画笔
    private Paint textPaint;

    //刻度线的深画笔
    private Paint keduDarkPaint;
    //刻度线的浅的画笔
    private Paint keduLightPaint;
    //刻度字体画笔
    private Paint keduFontPaint;
    private Paint changePaint;

    private int arcEnd;
    private float start=350;
    public ShowView(Context context) {
        this(context, null);
    }

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();

    }


    private void init() {
        distance_from_two_acr=DensityUtil.dip2px(context,18);
        outArcPaint=new Paint();
        outArcPaint.setAntiAlias(true);
        outArcPaint.setStrokeWidth(12);
        outArcPaint.setColor(Color.parseColor("#e3e3e3"));
        outArcPaint.setAlpha(80);
        outArcPaint.setStyle(Paint.Style.STROKE);

        inArcPaint=new Paint();
        inArcPaint.setAntiAlias(true);
        inArcPaint.setStrokeWidth(30);
        inArcPaint.setColor(Color.parseColor("#ffffff"));
        inArcPaint.setAlpha(60);
        inArcPaint.setStyle(Paint.Style.STROKE);
        //正中间字体画笔
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(60);
        textPaint.setTypeface(GlobalParams.getInstance().getLocalTypeface());
        textPaint.setColor(Color.parseColor("#ffffff"));

        //信用分数
        numPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        numPaint.setTextSize(150);
        numPaint.setStyle(Paint.Style.STROKE);
//        numPaint.setTypeface(GlobalParams.getInstance().getLocalTypeface());
        numPaint.setColor(Color.parseColor("#ffffff"));
        
        keduDarkPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        keduDarkPaint.setStrokeWidth(10);
        keduDarkPaint.setStyle(Paint.Style.STROKE);
        keduDarkPaint.setColor(Color.parseColor("#ffffff"));
        keduDarkPaint.setAlpha(30);

        keduLightPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        keduLightPaint.setStrokeWidth(6);
        keduLightPaint.setTypeface(GlobalParams.getInstance().getLocalTypeface());
        keduLightPaint.setStyle(Paint.Style.STROKE);
        keduLightPaint.setColor(Color.parseColor("#ffffff"));
        keduLightPaint.setAlpha(120);

        evaluatePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        evaluatePaint.setTextSize(27);
        evaluatePaint.setAlpha(60);
        evaluatePaint.setTypeface(GlobalParams.getInstance().getLocalTypeface());
        evaluatePaint.setColor(Color.parseColor("#c2c2c2"));
        
        
        keduFontPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        keduFontPaint.setTextSize(20);
        keduFontPaint.setColor(Color.parseColor("#ffffff"));
        keduFontPaint.setAlpha(120);

        changePaint=new Paint();
        changePaint.setAntiAlias(true);
        changePaint.setStrokeWidth(12);
        changePaint.setAlpha(70);
        changePaint.setStyle(Paint.Style.STROKE);

    }

    
    private void resetChangePaintColor(int evalute){
//        int evalute=Integer.valueOf(numString);
        if(evalute<520){
        	 changePaint.setColor(Color.parseColor("#ff5c93"));
        }else if(evalute>=520&&evalute<690){
        	 changePaint.setColor(Color.parseColor("#ffe89a"));
        }else if(evalute>=690&&evalute<870){
       	 	changePaint.setColor(Color.parseColor("#e7fde6"));
       }else {
        	 changePaint.setColor(Color.parseColor("#6bc867"));
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wrap_Len = DensityUtil.dip2px(context, 300);
        int width = measureDimension(wrap_Len, widthMeasureSpec);
        int height = measureDimension(wrap_Len, heightMeasureSpec);
        len=Math.min(width,height);
        //保证他是一个正方形
        setMeasuredDimension(len,len);

    }
    public int measureDimension(int defaultSize, int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = defaultSize;   //UNSPECIFIED
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获得中心点坐标
        int centerX= len/2;
        int centerY= len/2;
//        Log.e("the center is","centerX="+centerX+"    centerY"+centerY);
        //只要传入相对的坐标即可,画最最外圈的圆环
        RectF rectF=new RectF(0+DEFAULT_PADDING,0+DEFAULT_PADDING
                ,getMeasuredWidth()-DEFAULT_PADDING,getMeasuredHeight()-DEFAULT_PADDING);
        canvas.drawArc(rectF,START_ANGLE-10, SWEEP_ANGLE-10,false,outArcPaint);
        //内圆环画圈
        rectF=new RectF(0+DEFAULT_PADDING+distance_from_two_acr,0+DEFAULT_PADDING+distance_from_two_acr,getMeasuredWidth()-DEFAULT_PADDING-distance_from_two_acr
                ,getMeasuredHeight()-DEFAULT_PADDING-distance_from_two_acr);
        canvas.drawArc(rectF,START_ANGLE-10, SWEEP_ANGLE-10,false,inArcPaint);
        //中间字体的绘制
        drawText(canvas,centerX,centerY);
        //进行内圆环刻度线的draw
        drawtheLine(canvas,centerX,centerY);

        //动态通知圆环
        rectF=new RectF(0+DEFAULT_PADDING,0+DEFAULT_PADDING
                ,getMeasuredWidth()-DEFAULT_PADDING,getMeasuredHeight()-DEFAULT_PADDING);

        float angle=7*(start-350)/20;

        canvas.drawArc(rectF,START_ANGLE-10, angle,false,changePaint);
        post(new Runnable() {
            @Override
            public void run() {
            	postInvalidateDelayed(10);
                if (start<arcEnd){
                    start+=(40/14)*2;
                    count++;
                    Log.e("aaaaaa",count+"");
                }
                //动态信用值
                int temp=Integer.parseInt(numString);
                resetChangePaintColor(temp);
                if(temp<temp_evalute&& temp_evalute<=520){
                	temp+=10;
                	numString=String.valueOf(temp);
                }else if(temp<temp_evalute&& temp_evalute>520){
                	temp+=20;
                	numString=String.valueOf(temp);
                }
                
                if(temp>0&&temp<=520){
                	textString="信用较低";
                }else if(temp>520&&temp<=690){
                	textString="信用中等";
                }else if(temp>690&&temp<=870){
                	textString="信用良好";
                }else if(temp>870&&temp<=900){
                	textString="信用优秀";
                }else{
                	textString="信用极好";
                }
            }
        });
    }

    private void drawtheLine(Canvas canvas,int centerX,int centerY) {
        canvas.save();
        int rotateRadius=12;
        //我们将画布旋转,实际上是旋转我们的坐标轴
        canvas.rotate(-114,getMeasuredWidth()/2,getMeasuredHeight()/2);
        int lineStartY= (int) (DEFAULT_PADDING+distance_from_two_acr-inArcPaint.getStrokeWidth()/2-1);
        int lineEndY= (int) (lineStartY+inArcPaint.getStrokeWidth());
        //每次画布旋转的角度
//       
        for(int i=0;i<20;i++){
            if (i%5==0||i==19){
            	canvas.drawLine(centerX, lineStartY, centerX, lineEndY, keduLightPaint);
            }else {
            	canvas.drawLine(centerX, lineStartY, centerX, lineEndY, keduDarkPaint);
            }
            float textLength=keduFontPaint.measureText(stringArray[i]);
            canvas.drawText(stringArray[i],centerX-textLength/2,lineEndY+30,keduFontPaint);
            canvas.rotate(rotateRadius,centerX,centerY);
        }
        canvas.restore();

    }

    private void drawText(Canvas canvas,int centerX,int centerY){
        if (!TextUtils.isEmpty(textString)){
            //计算字体的长度
            float textWidth=textPaint.measureText(textString);
            //textPaint.descent()-textPaint.ascent()测量字体高度
            canvas.drawText(textString,centerX-textWidth/2,centerY+10+(textPaint.descent()-textPaint.ascent()),textPaint);
        }
        if (!TextUtils.isEmpty(textString)){
            //计算字体的长度
            float textWidth=evaluatePaint.measureText(evaluateString);
            //textPaint.descent()-textPaint.ascent()测量字体高度
            canvas.drawText(evaluateString,centerX-textWidth/2,centerY+100+(evaluatePaint.descent()-evaluatePaint.ascent()),evaluatePaint);
        }
        if (!TextUtils.isEmpty(textString)){
            float textWidth=numPaint.measureText(numString);
            canvas.drawText(numString,centerX-textWidth/2,centerY,numPaint);
        }
    }

    private int len;

    //是否计算padding
    private void ensurePadding() {
        int paddingleft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        int width = getWidth() - paddingleft - paddingRight;
        int height = getHeight() - paddingBottom - paddingTop;
        len = Math.min(width, height) / 2;


    }

    public void setTextString(String string){
        textString=string;
    }

    public void setEvaluateString(String evaluateString){
    	this.evaluateString=evaluateString;
    }

    private int temp_evalute=0;
    public void setNumString(String s){
        int temp=Integer.valueOf(s);
        temp_evalute=temp;
//        if(temp<=450){
        	int evalute=(temp*120/450);
        	arcEnd= temp;
//        }
        
//        numString=String.valueOf(s);
//        arcEnd= Integer.parseInt(s);
        if (arcEnd<=350){
            arcEnd=350;
        }else if (arcEnd>=950){
            arcEnd=950;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
