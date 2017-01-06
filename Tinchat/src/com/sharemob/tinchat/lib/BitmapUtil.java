package com.sharemob.tinchat.lib;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {

	public static Bitmap getImageFromAssetsFile(Context activity,String fileName)  
	{  
	      Bitmap image = null;  
	      InputStream is=null;
	      AssetManager am = activity.getAssets();  
	      try  
	      {  
	          is = am.open(fileName); 
	          image = BitmapFactory.decodeStream(is);  
	      }catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      } finally{
	    	  try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	      }
	      return image;  
	  }  
	/**
	 * 
	 * @param width
	 *            图像的宽度
	 * @param height
	 *            图像的高度
	 * @param image
	 *            源图片
	 * @param outerRadiusRat
	 *            圆角的大小
	 * @return 圆角图片
	 */
//	public static Bitmap roundedRectangleBitmap(int width, int height,
//			Bitmap image, float outerRadiusRat) {
//		// 根据源文件新建一个darwable对象
//		Drawable imageDrawable = new BitmapDrawable(image);
//		// 新建一个新的输出图片
//		Bitmap output = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		Canvas canvas = new Canvas(output);
//		// 新建一个矩形
//		RectF outerRect = new RectF(0, 0, width, height);
//		// 产生一个红色的圆角矩形
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setColor(Color.RED);
//		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
//
//		// 将源图片绘制到这个圆角矩形上
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//		imageDrawable.setBounds(0, 0, width, height);
//		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
//		imageDrawable.draw(canvas);
//		canvas.restore();
//
//		return output;
//	}
//
//	public static Bitmap getBitmap(String url) {
//		 URL myFileURL;
//	        Bitmap bitmap=null;
//	        try{
//	            myFileURL = new URL(url);
//	            //获得连接
//	            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
//	            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
//	            conn.setConnectTimeout(6000);
//	            //连接设置获得数据流
//	            conn.setDoInput(true);
//	            //不使用缓存
//	            conn.setUseCaches(false);
//	            //这句可有可无，没有影响
//	            //conn.connect();
//	            //得到数据流
//	            InputStream is = conn.getInputStream();
//	            //解析得到图片
//	            bitmap = BitmapFactory.decodeStream(is);
//	            //关闭数据流
//	            is.close();
//	        }catch(Exception e){
//	            e.printStackTrace();
//	        }
//	         
//	        return bitmap;
//	}
	
//	 public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) { 
//	     
//	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
//	            bitmap.getHeight(), Config.ARGB_8888);
//	        //得到画布
//	        Canvas canvas = new Canvas(output); 
//	     
//	        
//	       //将画布的四角圆化
//	        final int color = Color.RED;  
//	        final Paint paint = new Paint();  
//	        //得到与图像相同大小的区域  由构造的四个值决定区域的位置以及大小
//	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
//	        final RectF rectF = new RectF(rect);  
//	        //值越大角度越明显
//	        final float roundPx = 50;  
//	       
//	        paint.setAntiAlias(true);  
//	        canvas.drawARGB(0, 0, 0, 0);  
//	        paint.setColor(color);  
//	        //drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
//	        canvas.drawRoundRect(rectF, roundPx,roundPx, paint);  
//	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
//	        canvas.drawBitmap(bitmap, rect, rect, paint);  
//	       
//	        return output;  
//	      } 
}