package com.sharemob.tinchat.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.common.GlobalParams;

public class ClearEditText extends EditText implements OnFocusChangeListener,TextWatcher {
	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	private Context context;
	/**
	 * 控件是否有焦点
	 */
	private boolean hasFoucs=true;

	public ClearEditText(Context context) {
		this(context, null);
		this.context=context;
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		this.context=context;
	}

	public void setInputFilter(int maxLength){
		addTextChangedListener(new MaxLengthWatcher(maxLength, this));  
//		setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
	}
	
	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, R.drawable.style_edittext_border);
		this.context=context;
		init();
		setFocusableInTouchMode(true);
	}
	
	public void antoRequestFocus(){
		
		requestFocus();
		setFocusableInTouchMode(true);
		InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInputFromInputMethod(getWindowToken(),0);  
	}
	
//	public void setText(CharSequence text){
//		
//	}

	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			// throw new
			// NullPointerException("You can add drawableRight attribute in XML");
			mClearDrawable = getResources().getDrawable(
					R.drawable.selector_text_del);
		}

		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
		
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}
//	private Paint paint = new Paint();
//	  @Override
//     protected void onDraw(Canvas canvas) {
//        paint.setStyle(Style.STROKE);
//        paint.setStrokeWidth(1);
//          if(this.isFocused() == true){
//        	paint.setColor(Color.parseColor("#c8c8c8"));
//          }
//        else{
//        	paint.setColor(Color.parseColor("#c8c8c8"));
//        }
//        canvas.drawRoundRect(new RectF(this.getScrollX(), this.getScrollY(), this.getWidth()+this.getScrollX(), this.getHeight()+ this.getScrollY()-1), 1,1, paint);
//        super.onDraw(canvas);
//	 }
	
	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
