//package com.sharemob.tinchat.modules.verification;
//
//import com.sharemob.tinchat.R;
//import com.sharemob.tinchat.lib.LocalUtils;
//
//import android.animation.ArgbEvaluator;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.app.Activity;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//public class VerificationMainActivity extends Activity {
//    private ShowView showView;
//    private LinearLayout relativeLayout;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        setContentView(R.layout.activity_verification_main);
//        
//        
//        relativeLayout= (LinearLayout) findViewById(R.id.rl);
//        LocalUtils.applyLocalFont(relativeLayout);
//        
//        LocalUtils.exitView(this, R.id.title_back, R.anim.in_from_right, R.anim.out_to_right);
//        
//        showView= (ShowView) findViewById(R.id.show);
//        showView.setNumString("400");
//        showView.setTextString("信用不错");
//        showView.setEvaluateString("评估时间:2016.04.23");
//        //背景渐变动画实现
//        ValueAnimator coloranim= ObjectAnimator.ofInt(relativeLayout, "backgroundColor",0xFFBCFBD4, 0xFFFF8080, 0xFF8080FF);
//        //要根据更新条计算时间计算，这里只是简单写的数字
//        coloranim.setDuration(4000);
//        coloranim.setEvaluator(new ArgbEvaluator());
//        coloranim.start();
//
//        int [] icons=new int[]{R.drawable.credited_manager,R.drawable.card_credited};
//        Button user_verification_manager=(Button)findViewById(R.id.user_verification_manager);
//        user_verification_manager.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				LocalUtils.gotoViewByAnim(
//						VerificationMainActivity.this,
//						CreditManagerActivity.class.getName(), R.anim.in_from_right, R.anim.out_to_left, false);
//			}
//		});
//        
//        Button user_verification_promote=(Button)findViewById(R.id.user_verification_promote);
//        user_verification_promote.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				LocalUtils.gotoViewByAnim(
//						VerificationMainActivity.this,
//						PromoteCreditActivity.class.getName(), R.anim.in_from_right, R.anim.out_to_left, false);
//			}
//		});
//        
//        for(int i=0;i<2;i++){
//			Drawable drawable =this.getResources().getDrawable(icons[i]);
//			drawable.setBounds(0, 0, 60, 60); //必须设置图片大小，否则不显示
//	    	if(i==0){
//	    		user_verification_manager.setCompoundDrawables(drawable , null, null, null);
//	    	}else{
//	    		user_verification_promote.setCompoundDrawables(drawable , null, null, null);
//	    	}
//		}
//    }
//    //550 100  //650 150 //450 50
//}
