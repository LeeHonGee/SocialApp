/**
 *  文件名:CropImageView.java
 *  修改人:lihangjie
 *  创建时间:2015-10-3 下午10:59:32
 */
package com.sharemob.tinchat.modules.camera;

import com.sharemob.tinchat.base.CropFloatDrawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2015-10-3 下午10:59:32]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public class CropView extends View {
	protected float oriRationWH = 0;
	protected Drawable mDrawable;
	protected CropFloatDrawable mFloatDrawable;
	protected Rect mDrawableSrc = new Rect();// 图片Rect变换时的Rect
	protected Rect mDrawableDst = new Rect();// 图片Rect
	protected Rect cropRect = new Rect();// 浮层的Rect
	protected boolean isFrist = true;
	protected Context mContext;

	public CropView(Context context) {
		super(context);
		init(context);
	}

	public CropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CropView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);

	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		this.mContext = context;
		try {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				this.setLayerType(LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mFloatDrawable = new CropFloatDrawable(context);
	}

	public void setDrawable(Drawable mDrawable, Rect rect) {
		this.mDrawable = mDrawable;
		this.cropRect=rect;
		this.isFrist = true;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {

		if (mDrawable == null) {
			return;
		}

		if (mDrawable.getIntrinsicWidth() == 0|| mDrawable.getIntrinsicHeight() == 0) {
			return;
		}

		configureBounds();
		// 在画布上花图片
		mDrawable.draw(canvas);
		canvas.save();
		// 在画布上画浮层FloatDrawable,Region.Op.DIFFERENCE是表示Rect交集的补集
		canvas.clipRect(this.cropRect, Region.Op.DIFFERENCE);
		// 在交集的补集上画上灰色用来区分
		canvas.drawColor(Color.parseColor("#a0000000"));
		canvas.restore();
		// 画浮层
		mFloatDrawable.draw(canvas);
	}

	protected void configureBounds() {
		if (isFrist) {
			oriRationWH = ((float) mDrawable.getIntrinsicWidth())/ ((float) mDrawable.getIntrinsicHeight());

			mDrawableSrc.set(0, 0, getWidth(), getHeight());
			mDrawableDst.set(mDrawableSrc);

			isFrist = false;
		}

		mDrawable.setBounds(mDrawableDst);
		mFloatDrawable.setBounds(cropRect);
	}

	// 进行图片的裁剪，所谓的裁剪就是根据Drawable的新的坐标在画布上创建一张新的图片
	public Bitmap getCropImage() {
		Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(),Config.RGB_565);
		Canvas canvas = new Canvas(tmpBitmap);
		mDrawable.draw(canvas);

		Matrix matrix = new Matrix();
		float scale = (float) (mDrawableSrc.width())/ (float) (mDrawableDst.width());
		matrix.postScale(scale,scale);

		Bitmap ret = Bitmap.createBitmap(tmpBitmap, cropRect.left,
				cropRect.top, cropRect.width(),cropRect.height(), matrix, true);
		tmpBitmap.recycle();
		tmpBitmap = null;

		return ret;
	}

	public int dipTopx(Context context, float dpValue) {
		final float dipx = context.getResources().getDisplayMetrics().density*dpValue+0.5f;
		return (int) dipx;
	}
}
